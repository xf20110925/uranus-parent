package com.ptb.uranus.schedule.service;

import org.apache.commons.configuration.ConfigurationException;
import org.junit.After;
import org.junit.Test;

/**
 * Created by eric on 16/5/9.
 */
public class SchedServiceTest {
    CommonMediaScheduleService schedService;

    public SchedServiceTest() throws ConfigurationException {
        schedService = new CommonMediaScheduleService();
    }

    @Test
    public void test() {
        schedService.updateCommonAritclesEntrys();
    }

    @After
    public void delAllScheduleCommonArticlesEntry() throws Exception {
        schedService.delAllScheduleCommonArticlesEntry();

    }
}