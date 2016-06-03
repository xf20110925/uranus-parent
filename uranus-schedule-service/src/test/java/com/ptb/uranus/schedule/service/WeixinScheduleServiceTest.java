package com.ptb.uranus.schedule.service;

import com.ptb.uranus.common.entity.CollectCondition;
import com.ptb.uranus.schedule.model.ScheduleObject;
import org.apache.commons.configuration.ConfigurationException;
import org.junit.After;
import org.junit.Test;

import java.util.Optional;
import java.util.regex.Pattern;

import static junit.framework.TestCase.assertTrue;

/**
 * Created by eric on 16/4/26.
 */
public class WeixinScheduleServiceTest {
    WeixinScheduleService weixinScheduleService;
    String biz = "MjA1ODMxMDQwMQ==";

    public WeixinScheduleServiceTest() throws ConfigurationException {
        weixinScheduleService = new WeixinScheduleService();
    }

    @Test
    public void testWeixinSchedule() throws Exception {


        //添加文章测试
        weixinScheduleService.addWeixinDetectNewArticlesSchedule(biz);
        assertTrue(weixinScheduleService.cacheDao.hasKey(weixinScheduleService.getCacheBizKeyByTemplate(biz)));
        assertTrue(weixinScheduleService.schedulerDao.getSchedulerByField("obj.conditon", Pattern.compile(String.format("^%s.*", biz))).isPresent());


        //更新调度时间测试
        Optional<ScheduleObject> schedulerByField = weixinScheduleService.schedulerDao.getSchedulerByField(
                WeixinScheduleService.ConditonField,
                weixinScheduleService.getConditionByTemplate(biz, -1));
        assertTrue(schedulerByField.isPresent());
        weixinScheduleService.updateWeixinMediaCondition((CollectCondition) schedulerByField.get().getObj(), biz, -2L);
        CollectCondition obj = (CollectCondition) weixinScheduleService.schedulerDao.getScheduler(schedulerByField.get().getId()).getObj();
        assertTrue(obj.getConditon().contains("-2"));

    }
    @Test
    public void testUpdateWeixinMediaCondition(){
         weixinScheduleService.addWeixinDetectNewArticlesSchedule(biz);
        assertTrue(weixinScheduleService.schedulerDao.getSchedulerByField("obj.conditon", Pattern.compile(String.format("^%s.*", biz))).isPresent());
        weixinScheduleService.updateWeixinMediaCondition(biz, 1464070133999L);
    }


    @After
    public void after() {
        weixinScheduleService.cacheDao.delAllValue(String.format("*%s*", biz));
        weixinScheduleService.schedulerDao.delAllScheduler();
    }
}