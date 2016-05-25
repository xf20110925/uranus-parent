package com.ptb.uranus.scheduler;

import com.alibaba.fastjson.JSON;
import com.ptb.uranus.UranusClient;
import com.ptb.uranus.common.entity.CollectCondition;
import com.ptb.uranus.common.entity.CollectType;
import com.ptb.uranus.schedule.dao.MongoSchedulerDao;
import com.ptb.uranus.schedule.dao.SchedulerDao;
import com.ptb.uranus.schedule.model.ScheduleObject;
import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;
import org.apache.log4j.Logger;

import java.util.*;

/**
 * Created by eric on 16/4/22.
 */
public class CrawleTask extends TimerTask {
    static Logger scheduleLogger = Logger.getLogger("schedule.message");
    static Logger logger = Logger.getLogger(CrawleTask.class);

    PropertiesConfiguration conf;
    UranusClient uranusClient;
    SchedulerDao schedulerDao;
    Map<CollectType, Integer> sendNumsMap;

    final int MIN_SEND_NUM = 100;
    List<CollectType> collectTypes;

    public CrawleTask() throws ConfigurationException {
        schedulerDao = new MongoSchedulerDao();
        uranusClient = new UranusClient("uranus-scheduleLogger", 0);
        sendNumsMap = new HashMap<>();
        collectTypes = Arrays.asList(CollectType.values());
        loadConf();
    }

    void loadConf() throws ConfigurationException {
        conf = new PropertiesConfiguration("uranus.properties");
        conf.setAutoSave(true);
    }

    final String ChangeRateConfigTemplate = "uranus.scheduler.type.%s.change.range.rate";
    final String MaxSendConfigTemplate = "uranus.scheduler.type.%s.send.max.num";
    final String InitSendConfigTemplate = "uranus.scheduler.type.%s.send.init.num";

    public int calScheduleNum(CollectType collectType) {

        int changeRangeRate = conf.getInt(String.format(ChangeRateConfigTemplate, collectType), 10);
        int maxSendNum = conf.getInt(String.format(MaxSendConfigTemplate, collectType), 10000);

        if (sendNumsMap.get(collectType) == null) {
            int sendNum = conf.getInt(String.format(InitSendConfigTemplate, collectType), 100);
            sendNumsMap.put(collectType, sendNum);
            return conf.getInt(String.format(InitSendConfigTemplate, collectType));
        }

        Integer sendNum = sendNumsMap.get(collectType);

        try {
            long blockNUM = uranusClient.getBusBlockNum(collectType);
            if (blockNUM > 0) {
                sendNum = sendNum < MIN_SEND_NUM ? MIN_SEND_NUM : sendNum * (100 - changeRangeRate) / 100;
                logger.warn(String.format("collectType [%s] is block ,block num [%d]", collectType, blockNUM));
            } else {
                sendNum = sendNum > maxSendNum ? maxSendNum : sendNum * (100 + changeRangeRate) / 100;
            }
            sendNumsMap.put(collectType, sendNum);
        } catch (Exception e) {
            logger.warn(e.getMessage(),e);
        }

        int schedNum = sendNumsMap.get(collectType);

        logger.info(String.format("collectType [%s] 's scheduleLogger num [%d]", collectType, schedNum));

        return schedNum;
    }

    @Override
    public void run() {
        try {
            loadConf();
        } catch (ConfigurationException e) {
            logger.warn("配置加载异常...........");
        }

        //开始进行调度
        collectTypes.forEach(collectType -> {
            try {
                List<ScheduleObject> scheduleObjects = schedulerDao.getNeedScheduleObjectsByCollectType(collectType, calScheduleNum(collectType));
                scheduleObjects.forEach(scheduleObject -> {
                    try {
                        boolean sendOk = uranusClient.sendCrawleCommand((CollectCondition) scheduleObject.getObj());
                        if (sendOk) {
                            scheduleObject.setlTime(System.currentTimeMillis());
                            scheduleObject.setnTime(scheduleObject.getTrigger().nextTriggeTime());
                            scheduleObject.setsCnt(scheduleObject.getsCnt() + 1);
                            schedulerDao.updateScheduler(scheduleObject);
                            scheduleLogger.info(scheduleObject.toString());
                        }
                    } catch (Exception e) {
                        scheduleObject.setfCnt(scheduleObject.getsCnt() + 1);
                        logger.warn(String.format("schedule collectType [%s] Error scheduleObj [%s]", collectType, JSON.toJSONString(
                                scheduleObject
                        )), e);
                    }

                });
            } catch (Exception e) {
                logger.warn(String.format("schedule collectType [%s] Error", collectType), e);
            }

        });

    }

    @Override
    public boolean cancel() {
        uranusClient.close();
        return super.cancel();
    }
}
