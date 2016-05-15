package com.ptb.uranus.schedule.dao;

import com.ptb.uranus.common.entity.CollectType;
import com.ptb.uranus.schedule.model.Priority;
import com.ptb.uranus.schedule.model.SchedulableCollectCondition;
import com.ptb.uranus.schedule.model.ScheduleObject;
import com.ptb.uranus.schedule.trigger.PeriodicTrigger;
import org.apache.commons.configuration.ConfigurationException;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

import static junit.framework.TestCase.assertTrue;

/**
 * Created by eric on 16/4/22.
 */
public class MongoSchedulerDaoTest {
    SchedulerDao mongoSchedulerDao = new MongoSchedulerDao();

    public MongoSchedulerDaoTest() throws ConfigurationException {
    }


    @Test
    public void testACID() throws Exception {
        ScheduleObject ddddd = new ScheduleObject(new PeriodicTrigger(1, TimeUnit.DAYS)
                , Priority.L1, new SchedulableCollectCondition(CollectType.C_A_A_D, "ddddd"));

        ScheduleObject ddddd1 = new ScheduleObject(new PeriodicTrigger(1, TimeUnit.DAYS)
                , Priority.L1, new SchedulableCollectCondition(CollectType.C_A_A_D, "ddddd"));


        assertTrue(mongoSchedulerDao.addCollScheduler(ddddd));
        List<ScheduleObject> scheduleObjects = Arrays.asList(ddddd1);
        assertTrue(mongoSchedulerDao.addCollSchedulers(scheduleObjects));

        ddddd.setfCnt(1000);
        assertTrue(mongoSchedulerDao.updateScheduler(ddddd));

        List<ScheduleObject> needScheduleObjects = mongoSchedulerDao.getNeedScheduleObjects(1);
        assertTrue(needScheduleObjects.size() > 0);

        List<ScheduleObject> needScheduleObjects1 = mongoSchedulerDao.getNeedScheduleObjectsByCollectType(CollectType.C_A_A_D, 1);
        assertTrue(needScheduleObjects1.size() > 0);

        Optional<ScheduleObject> ddddd2 = mongoSchedulerDao.getSchedulerByField("obj.conditon", "ddddd");
        assertTrue(ddddd2.isPresent());

        System.out.println(mongoSchedulerDao.getScheduler(ddddd.getId()).toString());
        assertTrue(mongoSchedulerDao.delCollScheduler(ddddd.getId()));

        assertTrue(mongoSchedulerDao.delAllScheduler());

    }
}