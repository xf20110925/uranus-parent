package com.ptb.uranus.schedule.service;

import com.alibaba.fastjson.JSON;
import com.ptb.uranus.common.entity.CollectCondition;
import com.ptb.uranus.common.entity.CollectType;
import com.ptb.uranus.schedule.dao.MongoSchedulerDao;
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
public class WeixinScheduleService {
    private final long dynamicDelayMill;
    private final long detectArticleDelayMin;
    public SchedulerDao schedulerDao;
    public final static String ConditonField = "obj.conditon";
    public final static String ConditonTemplate = "%s:::%d";
    private long dynamicFetchInterval;
    private int dynamicFetchCount;

    public String getConditionByTemplate(String biz, long times) {
        return String.format(ConditonTemplate, biz, times);
    }

    public WeixinScheduleService() throws ConfigurationException {
        schedulerDao = new MongoSchedulerDao();
        PropertiesConfiguration conf = new PropertiesConfiguration("ptb.properties");
        dynamicDelayMill = TimeUnit.MINUTES.toMillis(conf.getInt("uranus.scheduler.weixin.article.dynamic.delay.minute", 60 * 24));
        detectArticleDelayMin = conf.getLong("uranus.scheduler.weixin.article.detect.delay.minute", 60 * 24);
        dynamicFetchCount = conf.getInt("uranus.scheduler.weixin.article.dynamic.fetch.num", 7);
        dynamicFetchInterval = conf.getInt("uranus.scheduler.weixin.article.dynamic.interval.minute", 60*24);
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

        if (!(schedulerDao.getSchedulerByField("obj.conditon", Pattern.compile(String.format("^%s:::.*", biz))).isPresent())) {
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

        }
    }

    public void updateWeixinMediaCondition(CollectCondition LastCondition, String biz, Long lastPushMessagePostTime) {
        Optional<ScheduleObject> schedulerByField = schedulerDao.getSchedulerByField(ConditonField, LastCondition.getConditon());
        if (schedulerByField.isPresent()) {
            SchedulableCollectCondition schedulableCollectCondition = (SchedulableCollectCondition) schedulerByField.get().getObj();
            schedulableCollectCondition.setConditon(getConditionByTemplate(biz, lastPushMessagePostTime));
            schedulerDao.updateScheduler(schedulerByField.get());
        }else{
            addWeixinDetectNewArticlesSchedule(biz);
        }
    }

    public void addArticleStaticSchedulers(List<String> urls) {
        schedulerDao.addCollSchedulers(
                urls.stream().map(
                        url -> new ScheduleObject<>(new JustOneTrigger(System.currentTimeMillis()),
                                Priority.L2, new SchedulableCollectCondition(CollectType.C_WX_A_S, UrlFormatUtil.format(url)))).collect(Collectors.toList()));
    }

    public void addArticleDynamicScheduler(long postTime, String url) {
        Optional<ScheduleObject> schedulerByField = schedulerDao.getSchedulerByField("obj.conditon", url);
        if(!schedulerByField.isPresent()) {
        schedulerDao.addCollScheduler(new ScheduleObject<>(new PeriodicTrigger(dynamicFetchInterval, TimeUnit.MINUTES, new Date(), dynamicFetchCount),
                Priority.L2, new SchedulableCollectCondition(CollectType.C_WX_A_D, url)));
        }
    }

    public void updateWeixinMediaCondition(String biz, Long lastPushMessagePostTime) {
        Optional<ScheduleObject> schedulerByField = schedulerDao.getSchedulerByField(ConditonField, Pattern.compile(String.format("^%s.*", biz)));
        schedulerByField.ifPresent(scheduleObject -> {
            CollectCondition collCondition = (CollectCondition) scheduleObject.getObj();
            long postTime = Long.parseLong(collCondition.getConditon().split(":::")[1]);
            if (postTime < lastPushMessagePostTime || String.valueOf(postTime).endsWith("000")){
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

    public static void main(String[] args) throws ConfigurationException {
        SchedulerDao schedulerDao = new MongoSchedulerDao();
        Optional<ScheduleObject> schedulerByField = schedulerDao.getSchedulerByField("obj.conditon", "http://mp.weixin.qq.com/s?__biz=MjM225Nzg0MTQ3OQ==&mid=2660575418&idx=2&sn=47b8a7aea73580f5c3b50bb088b9c3ef#rd");
        schedulerByField.ifPresent(scheduleObject -> System.out.println(scheduleObject.getObj().getC()));
    }
}
