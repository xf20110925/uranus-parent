package com.ptb.uranus.scheduler;

import com.alibaba.fastjson.JSON;
import com.ptb.uranus.UranusClient;
import com.ptb.uranus.common.entity.CollectCondition;
import com.ptb.uranus.common.entity.CollectType;
import com.ptb.uranus.schedule.dao.MongoSchedulerDao;
import com.ptb.uranus.schedule.dao.SchedulerDao;
import com.ptb.uranus.schedule.model.ScheduleObject;
import com.ptb.utils.log.LogUtils;
import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;
import java.util.stream.Collectors;

import static com.ptb.uranus.common.entity.CollectType.values;

/**
 * Created by eric on 16/4/22.
 */
public class CrawleTask extends TimerTask {
    static Logger scheduleLogger = LoggerFactory.getLogger("schedule.message");
    static Logger logger = LoggerFactory.getLogger(CrawleTask.class);

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
        collectTypes = Arrays.asList(values()).stream().filter(c -> {
            switch (c) {
                case C_A_A_N:
                case C_A_A_D:
                case C_A_A_S:
                    return false;
            }
            return true;
        }).collect(Collectors.toList());
        loadConf();
    }

    void loadConf() throws ConfigurationException {
        conf = new PropertiesConfiguration("ptb.properties");
        conf.setAutoSave(true);
    }

    final String ChangeRateConfigTemplate = "uranus.scheduler.type.%s.change.range.rate";
    final String MaxSendConfigTemplate = "uranus.scheduler.type.%s.send.max.num";
    final String InitSendConfigTemplate = "uranus.scheduler.type.%s.send.init.num";

    public int calScheduleNum(CollectType collectType) {
/*
        int changeRangeRate = conf.getInt(String.format(ChangeRateConfigTemplate, collectType), 10);
        int maxSendNum = conf.getInt(String.format(MaxSendConfigTemplate, collectType), 10000);*/
        return conf.getInt(String.format(InitSendConfigTemplate, collectType), 100);
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
                            scheduleLogger.info("schedele log : [{}]", scheduleObject.toString());
                        }
                    } catch (Exception e) {
                        scheduleObject.setfCnt(scheduleObject.getsCnt() + 1);
                        logger.warn(String.format("schedule collectType [%s] Error scheduleObj [%s]", collectType, JSON.toJSONString(
                                scheduleObject
                        ), e));
                        LogUtils.log("uranus-schedule", "update-schedule-object", LogUtils.ActionResult.failed, e.getMessage());
                    }

                });
            } catch (Exception e) {
                logger.warn(String.format("schedule collectType [%s] Error", collectType), e);
                LogUtils.log("uranus-schedule", "get-schedule-object", LogUtils.ActionResult.failed, e.getMessage());
            }

        });

    }

    @Override
    public boolean cancel() {
        uranusClient.close();
        return super.cancel();
    }
}
