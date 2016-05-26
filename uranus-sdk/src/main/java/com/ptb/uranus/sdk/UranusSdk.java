package com.ptb.uranus.sdk;

import com.ptb.uranus.common.entity.CollectType;
import com.ptb.uranus.schedule.Collector;
import com.ptb.uranus.schedule.dao.CacheDao;
import com.ptb.uranus.schedule.dao.MongoSchedulerDao;
import com.ptb.uranus.schedule.dao.RedisCacheDao;
import com.ptb.uranus.schedule.dao.SchedulerDao;
import com.ptb.uranus.schedule.model.Priority;
import com.ptb.uranus.schedule.model.SchedulableCollectCondition;
import com.ptb.uranus.schedule.model.ScheduleObject;
import com.ptb.uranus.schedule.trigger.Trigger;
import com.ptb.uranus.sdk.exception.CollectArgsException;
import com.ptb.uranus.sdk.exception.ConfigureFileException;
import com.ptb.uranus.spider.weixin.WeixinSpider;
import com.ptb.utils.web.UrlFormatUtil;
import org.apache.commons.configuration.ConfigurationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by eric on 16/4/21.
 */
public class UranusSdk implements Collector {
    static Logger logger = LoggerFactory.getLogger(UranusSdk.class);
    static private UranusSdk instance;
    CacheDao cacheDao;
    WeixinSpider weixinSpider = new WeixinSpider();

    static String lock = new String();

    SchedulerDao schedulerDao = null;

    public static UranusSdk i() {
        if (instance == null) {
            synchronized (lock) {
                if (instance == null) {
                    try {
                        instance = new UranusSdk();
                    } catch (ConfigurationException e) {
                        throw new ConfigureFileException(e);
                    }
                }
            }
        }
        return instance;
    }

    private UranusSdk() throws ConfigurationException {
        schedulerDao = new MongoSchedulerDao();
        cacheDao = new RedisCacheDao();
    }


    public String getCorrectCondition(String srcCond, CollectType collectType) {
        String cond = null;
        String finalCondition = null;
        if ((collectType.getCode() == CollectType.C_WX_M_S.getCode()) || (collectType.getCode() == CollectType.C_WX_M_D.getCode())) {
            if (!srcCond.startsWith("http://") && !srcCond.startsWith("https://")) {
                cond = weixinSpider.getWeixinArticleUrlById(srcCond);
            } else {
                cond = srcCond;
            }
        } else {
            cond = srcCond;
        }

        finalCondition = UrlFormatUtil.format(cond);

    /*    if (!finalCondition.equals(finalCondition) && finalCondition.contains("weixin")) {
            cacheDao.setValue(finalCondition, cond);
            cacheDao.setValue(cond, finalCondition);
        }*/

        return finalCondition;
    }

    @Override
    public String collect(String srcCond, CollectType collectType, Trigger trigger, Priority priority) {

        try {
            String finalCondition = getCorrectCondition(srcCond, collectType);
            if (collectType == null || finalCondition == null || collectType == null || priority == null || trigger == null) {
                throw new CollectArgsException(String.format("输入的参数不正确 type:[%s] trigger[%s] priority[%s] inputValue[%s]", collectType, finalCondition));
            }
            ScheduleObject scheduleObject = new ScheduleObject(trigger, priority, new SchedulableCollectCondition(collectType, finalCondition));
            if (!schedulerDao.addCollScheduler(scheduleObject)) {
                return null;
            } else {
                return finalCondition;
            }
        } catch (Exception e) {
            logger.warn(String.format("cond [%s] type [%s] trgger[%s] trigger[%s]", srcCond, collectType, trigger, priority), e);
        }

        return null;
    }

    @Override
    public List<String> collect(List<String> srcConds, CollectType collectType, Trigger trigger, Priority priority) {
        try {
            List<String> finalConditions = srcConds.stream().map(srcCond -> getCorrectCondition(srcCond, collectType)).collect(Collectors.toList());
            if (collectType == null || finalConditions == null || collectType == null || priority == null || trigger == null) {
                throw new CollectArgsException(String.format("输入的参数不正确 type:[%s] trigger[%s] priority[%s] inputValue[%s]", collectType, finalConditions));
            }
            List<ScheduleObject> collects = finalConditions.stream().map(fc -> new ScheduleObject(trigger, priority, new SchedulableCollectCondition(collectType, fc))).
                    collect(Collectors.toList());
            if (schedulerDao.addCollSchedulers(collects)) {
                return finalConditions;
            }
        } catch (Exception e) {
            logger.warn(String.format("cond [%s] type [%s] trgger[%s] trigger[%s]", srcConds, collectType, trigger, priority), e);
        }

        return null;
    }

    public String urlFormat(String url) {
        return UrlFormatUtil.format(url);
    }
}
