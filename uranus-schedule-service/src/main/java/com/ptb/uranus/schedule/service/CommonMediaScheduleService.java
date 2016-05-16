package com.ptb.uranus.schedule.service;

import com.ptb.uranus.common.entity.CollectType;
import com.ptb.uranus.schedule.dao.*;
import com.ptb.uranus.schedule.model.ArticlesEntry;
import com.ptb.uranus.schedule.model.Priority;
import com.ptb.uranus.schedule.model.SchedulableCollectCondition;
import com.ptb.uranus.schedule.model.ScheduleObject;
import com.ptb.uranus.schedule.trigger.JustOneTrigger;
import com.ptb.uranus.schedule.trigger.PeriodicTrigger;
import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;

import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * Created by eric on 16/4/25.
 */
public class CommonMediaScheduleService {
    private final long dynamicDelayMill;
    private final int newCommonArticleUrlCacheSecond;
    SchedulerDao schedulerDao;
    CacheDao cacheDao;
    ArticlesEntryDao articlesEntryDao;

    public CommonMediaScheduleService() throws ConfigurationException {
        schedulerDao = new MongoSchedulerDao();
        articlesEntryDao = new MongoArticlesEntryDao();
        cacheDao = new RedisCacheDao();
        PropertiesConfiguration conf = new PropertiesConfiguration("uranus.properties");
        dynamicDelayMill = TimeUnit.MINUTES.toMillis(conf.getInt("uranus.scheduler.common.article.dynamic.delay.minute", 60 * 24));
        newCommonArticleUrlCacheSecond = (int) TimeUnit.MINUTES.toSeconds(conf.getInt("uranus.scheduler.common.article.url.cache.minute", 60 * 240));
    }

    public void addArticleStaticSchedulers(List<String> urls) {
        urls = urls.parallelStream().filter(url -> !cacheDao.hasKey(url)).collect(Collectors.toList());
        urls.forEach(url -> cacheDao.setValue(url, "1", newCommonArticleUrlCacheSecond));
        if (urls.size() == 0) {
            return;
        }

        schedulerDao.addCollSchedulers(
                urls.stream().map(
                        url -> new ScheduleObject<>(new JustOneTrigger(System.currentTimeMillis()),
                                Priority.L3, new SchedulableCollectCondition(CollectType.C_A_A_S, url))).collect(Collectors.toList()));
    }

    public void addArticleDynamicScheduler(String url, long postTime) {
        schedulerDao.addCollScheduler(new ScheduleObject<>(new JustOneTrigger(postTime * 1000 + dynamicDelayMill),
                Priority.L3, new SchedulableCollectCondition(CollectType.C_A_A_D, url)));
    }


    public void updateCommonAritclesEntrys() {
        List<ArticlesEntry> articleEntrys = articlesEntryDao.getArticleEntrys();

        if (articleEntrys.size() > 0) {
            //删除所有公共文章列表任务
            schedulerDao.delSchedulerByField("obj.collectType", CollectType.C_A_A_N.name());

            //加载最新文章列表任务

            //将任务加入数据库中
            List<ScheduleObject> collect = articleEntrys.parallelStream().map(articlesEntry -> {
                return new ScheduleObject<>(new PeriodicTrigger(articlesEntry.getPeriod(), TimeUnit.MINUTES),
                        Priority.L3, new SchedulableCollectCondition(CollectType.C_A_A_N, articlesEntry.getNewsurl()));
            }).collect(Collectors.toList());
            schedulerDao.addCollSchedulers(collect);
        }
    }


    public void delAllScheduleCommonArticlesEntry() {
        schedulerDao.delSchedulerByField("obj.collectType", CollectType.C_A_A_N.name());
    }
}
