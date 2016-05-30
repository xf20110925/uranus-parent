package com.ptb.uranus.schedule.service;

import com.ptb.uranus.common.entity.CollectCondition;
import com.ptb.uranus.common.entity.CollectType;
import com.ptb.uranus.schedule.dao.CacheDao;
import com.ptb.uranus.schedule.dao.MongoSchedulerDao;
import com.ptb.uranus.schedule.dao.RedisCacheDao;
import com.ptb.uranus.schedule.dao.SchedulerDao;
import com.ptb.uranus.schedule.model.Priority;
import com.ptb.uranus.schedule.model.SchedulableCollectCondition;
import com.ptb.uranus.schedule.model.ScheduleObject;
import com.ptb.uranus.schedule.trigger.JustOneTrigger;
import com.ptb.uranus.schedule.trigger.PeriodicTrigger;
import com.ptb.utils.web.UrlFormatUtil;
import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.TimeUnit;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * Created by eric on 16/4/25.
 */
public class WeiboScheduleService {
    private final long dynamicDelayMill;
    private final long detectArticleDelayMin;
    private final long detectMediaMonthMin;
    private SchedulerDao schedulerDao;
    private CacheDao cacheDao;
    String conditonField = "obj.conditon";
    String conditonTemplate = "%s:::%d";


    public String getCacheBizKeyByTemplate(String weiboId) {
        return String.format("s::wb::cid::%s", weiboId);
    }

    public String getConditionByTemplate(String containerID, long times) {
        return String.format(conditonTemplate, containerID, times);
    }

    public WeiboScheduleService() throws ConfigurationException {
        schedulerDao = new MongoSchedulerDao();
        cacheDao = new RedisCacheDao();
        PropertiesConfiguration conf = new PropertiesConfiguration("uranus.properties");
        dynamicDelayMill = TimeUnit.MINUTES.toMillis(conf.getInt("uranus.scheduler.weibo.article.dynamic.delay.minute", 60 * 24));
        detectArticleDelayMin = conf.getLong("uranus.scheduler.weibo.article.detect.delay.minute", 60 * 24);
        detectMediaMonthMin = conf.getLong("uranus.scheduler.weibo.article.detect.delay.minute", 60 * 24 * 30);
    }


    public boolean isScheduleMedia(String weiboID) {
        return cacheDao.hasKey(getCacheBizKeyByTemplate(weiboID));
    }


    public void checkAndAddToMediaStaticSchedule(String weiboID) {
        if (!isScheduleMedia(weiboID)) {
            addMediaStaticSchedule(weiboID);
        }
    }


    public boolean addMediaStaticSchedule(String weiboID) {
        return schedulerDao.addCollScheduler(
                new ScheduleObject<>(
                        new JustOneTrigger(new Date().getTime()),
                        Priority.L2,
                        new SchedulableCollectCondition(
                                CollectType.C_WB_M_S,
                                weiboID
                        )
                )
        );
    }

    public void addDetectNewArticlesSchedule(String containerId, String weiboID) {
        if (!cacheDao.hasKey(getCacheBizKeyByTemplate(weiboID)) &&
                !(schedulerDao.getSchedulerByField("obj.conditon", Pattern.compile(String.format("%s.*", containerId))).isPresent())) {
            schedulerDao.addCollScheduler(
                    new ScheduleObject<>(
                            new PeriodicTrigger(detectArticleDelayMin,
                                    TimeUnit.MINUTES,
                                    new Date(),
                                    new Date(Long.MAX_VALUE)),
                            Priority.L2,
                            new SchedulableCollectCondition(
                                    CollectType.C_WB_A_N,
                                    getConditionByTemplate(containerId, -1)
                            )
                    )
            );
            cacheDao.setValue(getCacheBizKeyByTemplate(weiboID), "1");
        }
    }

    public void addWeiboMediaDynamicSchedule(String condition) {
        schedulerDao.addCollScheduler(
                new ScheduleObject<>(
                        new JustOneTrigger(new Date().getTime()),
                        Priority.L2,
                        new SchedulableCollectCondition(
                                CollectType.C_WB_M_D,
                                condition
                        )
                ));

    }

    public void updateMediaCondition(CollectCondition LastCondition, String containerID, Long lastPushMessagePostTime) {
        Optional<ScheduleObject> schedulerByField = schedulerDao.getSchedulerByField(conditonField, LastCondition.getConditon());
        if (!schedulerByField.isPresent()) {
            SchedulableCollectCondition schedulableCollectCondition = (SchedulableCollectCondition) schedulerByField.get().getObj();
            schedulableCollectCondition.setConditon(getConditionByTemplate(containerID, lastPushMessagePostTime));
            schedulerByField.get().setObjByT(schedulableCollectCondition);
            schedulerDao.updateScheduler(schedulerByField.get());
        }
    }


    public void addArticleStaticSchedulers(List<String> urls) {
        schedulerDao.addCollSchedulers(
                urls.stream().map(
                        url -> new ScheduleObject<>(new JustOneTrigger(System.currentTimeMillis()),
                                Priority.L2, new SchedulableCollectCondition(CollectType.C_WB_A_S, UrlFormatUtil.format(url)))).collect(Collectors.toList()));
    }

    public void addArticleDynamicScheduler(long postTime, String url) {
        schedulerDao.addCollScheduler(new ScheduleObject<>(new PeriodicTrigger(24, TimeUnit.HOURS, new Date(postTime + dynamicDelayMill), 7),
                Priority.L2, new SchedulableCollectCondition(CollectType.C_WB_A_D, url)));
    }

}
