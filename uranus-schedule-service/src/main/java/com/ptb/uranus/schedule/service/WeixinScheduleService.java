package com.ptb.uranus.schedule.service;

import com.alibaba.fastjson.JSON;
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
import com.ptb.utils.string.RegexUtils;
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
public class WeixinScheduleService {
    private final long dynamicDelayMill;
    private final long detectArticleDelayMin;
    public SchedulerDao schedulerDao;
    public CacheDao cacheDao;
    public final static String ConditonField = "obj.conditon";
    public final static String ConditonTemplate = "%s:::%d";


    public String getCacheBizKeyByTemplate(String biz) {
        return String.format("s::wx::biz::%s", biz);
    }


    public String getConditionByTemplate(String biz, long times) {
        return String.format(ConditonTemplate, biz, times);
    }

    public WeixinScheduleService() throws ConfigurationException {
        schedulerDao = new MongoSchedulerDao();
        cacheDao = new RedisCacheDao();
        PropertiesConfiguration conf = new PropertiesConfiguration("uranus.properties");
        dynamicDelayMill = TimeUnit.MINUTES.toMillis(conf.getInt("uranus.scheduler.weixin.article.dynamic.delay.minute", 60 * 24));
        detectArticleDelayMin = conf.getLong("uranus.scheduler.weixin.article.detect.delay.minute", 60 * 24);
    }


    public boolean isScheduleMedia(String articleUrl) {
        String biz = RegexUtils.sub(".*__biz=([^#&]*).*", articleUrl, 0);
        return cacheDao.hasKey(getCacheBizKeyByTemplate(biz));
    }

    public void checkAndAddToMediaStaticSchedule(String articleUrl) {
        if (!isScheduleMedia(articleUrl)) {
            addMediaStaticSchedule(articleUrl);
        }
    }


    public boolean addMediaStaticSchedule(String articleUrl) {
        return schedulerDao.addCollScheduler(
                new ScheduleObject<>(
                        new JustOneTrigger(new Date().getTime()),
                        Priority.L2,
                        new SchedulableCollectCondition(
                                CollectType.C_WX_M_S,
                                articleUrl
                        )
                )
        );
    }

    public void addWeixinDetectNewArticlesSchedule(String biz) {
        if (!cacheDao.hasKey(getCacheBizKeyByTemplate(biz)) &&
                !(schedulerDao.getSchedulerByField("obj.conditon", Pattern.compile(String.format("^%s.*", biz))).isPresent())) {
            schedulerDao.addCollScheduler(
                    new ScheduleObject<>(
                            new PeriodicTrigger(detectArticleDelayMin,
                                    TimeUnit.MINUTES,
                                    new Date(),
                                    new Date(Long.MAX_VALUE)),
                            Priority.L2,
                            new SchedulableCollectCondition(
                                    CollectType.C_WX_A_N,
                                    getConditionByTemplate(biz, -1)
                            )
                    )

            );
            cacheDao.setValue(getCacheBizKeyByTemplate(biz), "1");
        }
    }


    public void updateWeixinMediaCondition(CollectCondition LastCondition, String biz, Long lastPushMessagePostTime) {
        Optional<ScheduleObject> schedulerByField = schedulerDao.getSchedulerByField(ConditonField, LastCondition.getConditon());
        if (schedulerByField.isPresent()) {
            SchedulableCollectCondition schedulableCollectCondition = (SchedulableCollectCondition) schedulerByField.get().getObj();
            schedulableCollectCondition.setConditon(getConditionByTemplate(biz, lastPushMessagePostTime));
            schedulerDao.updateScheduler(schedulerByField.get());
        }
    }

    public void addArticleStaticSchedulers(List<String> urls) {
        schedulerDao.addCollSchedulers(
                urls.stream().map(
                        url -> new ScheduleObject<>(new JustOneTrigger(System.currentTimeMillis()),
                                Priority.L2, new SchedulableCollectCondition(CollectType.C_WX_A_S, UrlFormatUtil.format(url)))).collect(Collectors.toList()));
    }

    public void addArticleDynamicScheduler(long postTime, String url) {

        schedulerDao.addCollScheduler(new ScheduleObject<>(new JustOneTrigger(postTime * 1000 + dynamicDelayMill),
                Priority.L2, new SchedulableCollectCondition(CollectType.C_WX_A_D, url)));
    }

    public void addWeixinDetectNewArticlesScheduleByArticleUrl(String url) {
        String biz = RegexUtils.sub(".*__biz=([^#&]*).*", url, 0);
        addWeixinDetectNewArticlesSchedule(url);
    }

    public void updateWeixinMediaCondition(String biz, Long lastPushMessagePostTime) {
//        schedulerDao.getSchedulerByField("obj.conditon", Pattern.compile(String.format("^%s.*", biz))).
        Optional<ScheduleObject> schedulerByField = schedulerDao.getSchedulerByField(ConditonField, Pattern.compile(String.format("^%s.*", biz)));
        schedulerByField.ifPresent(scheduleObject -> {
            CollectCondition collCondition = (CollectCondition) scheduleObject.getObj();
            long postTime = Long.parseLong(collCondition.getConditon().split(":::")[1]);
            if (postTime < lastPushMessagePostTime){
                String condition =  getConditionByTemplate(biz, lastPushMessagePostTime);
                collCondition.setConditon(condition);
                try {
                    scheduleObject.setObj(JSON.toJSONString(collCondition));
                    scheduleObject.setnTime(lastPushMessagePostTime);
                    schedulerDao.updateScheduler(scheduleObject);
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }
            }
        });
    }
}
