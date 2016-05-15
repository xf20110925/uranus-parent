package com.ptb.uranus.scheduler;

import com.ptb.uranus.common.entity.CollectType;
import org.apache.commons.configuration.ConfigurationException;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertTrue;

/**
 * Created by eric on 16/4/24.
 */
public class CrawleTaskTest {

    private CrawleTask crawleTask;

    @Before
    public void before() throws ConfigurationException {
        crawleTask = new CrawleTask();
    }

    @Test
    public void testCalScheduleNum() throws ConfigurationException {
        int k = 4;
        int lastnum = 0;
        while (k-- > 0) {
            int j = crawleTask.calScheduleNum(CollectType.C_WX_A_S);
            if (lastnum != j) {
                assertTrue(true);
            }
            lastnum = j;
        }


    }

    @After
    public void after() {
        crawleTask.cancel();
    }

}