package com.ptb.uranus.scheduler;

import com.ptb.uranus.common.entity.CollectType;
import com.ptb.uranus.schedule.dao.MongoSchedulerDao;
import com.ptb.uranus.schedule.dao.SchedulerDao;
import com.ptb.uranus.schedule.model.Priority;
import com.ptb.uranus.schedule.model.SchedulableCollectCondition;
import com.ptb.uranus.schedule.model.ScheduleObject;
import com.ptb.uranus.schedule.trigger.PeriodicTrigger;
import org.apache.commons.configuration.ConfigurationException;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import sun.misc.Signal;

import java.util.TimerTask;
import java.util.concurrent.TimeUnit;

/**
 * Created by eric on 16/4/22.
 */
public class TimeCollectSchdulerTest {
    SchedulerDao schedulerDao = new MongoSchedulerDao();
    ScheduleObject ddddd = new ScheduleObject(new PeriodicTrigger(60, TimeUnit.SECONDS)
            , Priority.L1, new SchedulableCollectCondition(CollectType.C_A_A_D, "http://www.ppppp.com"));

    public TimeCollectSchdulerTest() throws ConfigurationException {

    }

    @Before
    public void before() throws InterruptedException {
        schedulerDao.addCollScheduler(ddddd);
    }

    @Test
    public void testSendCommand() throws Exception {

        TimeCollectSchduler timeCollectSchduler = new TimeCollectSchduler();
        timeCollectSchduler.start(false);
        Thread.sleep(4000);
        Signal.raise(new Signal("INT"));
    }



    @After
    public void after() {
        schedulerDao.delCollScheduler(ddddd.getId());
    }

}