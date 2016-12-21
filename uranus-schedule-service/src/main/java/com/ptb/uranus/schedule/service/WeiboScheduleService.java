package com.ptb.uranus.schedule.service;

import com.ptb.uranus.common.entity.CollectCondition;
import com.ptb.uranus.common.entity.CollectType;
import com.ptb.uranus.schedule.dao.MongoSchedulerDao;
import com.ptb.uranus.schedule.dao.SchedulerDao;
import com.ptb.uranus.schedule.model.Priority;
import com.ptb.uranus.schedule.model.SchedulableCollectCondition;
import com.ptb.uranus.schedule.model.ScheduleObject;
import com.ptb.uranus.schedule.trigger.PeriodicTrigger;

import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;

import java.util.Date;
import java.util.Optional;
import java.util.concurrent.TimeUnit;
import java.util.regex.Pattern;

/**
 * Created by eric on 16/4/25.
 */
public class WeiboScheduleService {
    private final long dynamicDelayMill;
    private final long detectArticleDelayMin;
    private final int dynamicFetchCount;
    private final long dynamicFetchInterval;
    private SchedulerDao schedulerDao;
    String conditonField = "obj.conditon";
    String conditonTemplate = "%s:::%d";

    public String getConditionByTemplate(String weiboid, long times) {
        return String.format(conditonTemplate, weiboid, times);
    }

    public WeiboScheduleService() throws ConfigurationException {
        schedulerDao = new MongoSchedulerDao();
        PropertiesConfiguration conf = new PropertiesConfiguration("ptb.properties");
        dynamicDelayMill = TimeUnit.MINUTES.toMillis(conf.getInt("uranus.scheduler.weibo.article.dynamic.delay.minute", 60 * 24));
        detectArticleDelayMin = conf.getLong("uranus.scheduler.weibo.article.detect.delay.minute", 60 * 24);
        dynamicFetchCount = conf.getInt("uranus.scheduler.weibo.article.dynamic.fetch.num", 7);
        dynamicFetchInterval = conf.getInt("uranus.scheduler.weibo.article.dynamic.interval.minute", 60*24);
    }

    public void addDetectNewArticlesSchedule(String weiboID) {
        if (!(schedulerDao.getSchedulerByField("obj.conditon", Pattern.compile(String.format("%s.*", weiboID))).isPresent())) {
            schedulerDao.addCollScheduler(
                    new ScheduleObject<>(
                            new PeriodicTrigger(detectArticleDelayMin,
                                    TimeUnit.MINUTES,
                                    new Date(),
                                    new Date(Long.MAX_VALUE)),
                            Priority.L2,
                            new SchedulableCollectCondition(
                                    CollectType.C_WB_A_N,
                                    getConditionByTemplate(weiboID, -1)
                            )
                    )
            );
        }
    }

    public void updateMediaCondition(CollectCondition LastCondition, String weiboID, Long lastPushMessagePostTime) {
        Optional<ScheduleObject> schedulerByField = schedulerDao.getSchedulerByField(conditonField, LastCondition.getConditon());
        if (schedulerByField.isPresent()) {
            SchedulableCollectCondition schedulableCollectCondition = (SchedulableCollectCondition) schedulerByField.get().getObj();
            schedulableCollectCondition.setConditon(getConditionByTemplate(weiboID, lastPushMessagePostTime));
            schedulerByField.get().setObjByT(schedulableCollectCondition);
            schedulerDao.updateScheduler(schedulerByField.get());
        }else{
            addDetectNewArticlesSchedule(weiboID);
        }
    }

    public void addArticleDynamicScheduler(long postTime, String url) {
        schedulerDao.addCollScheduler(new ScheduleObject<>(new PeriodicTrigger(dynamicFetchInterval, TimeUnit.MINUTES, new Date(), dynamicFetchCount),
                Priority.L2, new SchedulableCollectCondition(CollectType.C_WB_A_D, url)));
    }
}
