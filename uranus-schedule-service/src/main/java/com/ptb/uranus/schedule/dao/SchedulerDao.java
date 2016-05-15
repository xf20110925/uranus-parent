package com.ptb.uranus.schedule.dao;

import com.ptb.uranus.common.entity.CollectType;
import com.ptb.uranus.schedule.model.ScheduleObject;

import java.util.List;
import java.util.Optional;

/**
 * Created by eric on 16/4/21.
 */
public interface SchedulerDao {
    boolean addCollScheduler(ScheduleObject scheduleObject);

    boolean delCollScheduler(String scheduleID);

    boolean updateScheduler(ScheduleObject scheduleObject);

    ScheduleObject getScheduler(String scheduleID);

    List<ScheduleObject> getNeedScheduleObjects(int count);

    List<ScheduleObject> getNeedScheduleObjectsByCollectType(CollectType collectType, int count);

    boolean addCollSchedulers(List<ScheduleObject> scheduleObjects);

    boolean delAllScheduler();


    boolean delSchedulerByField(String field, String fieldValue);

    Optional<ScheduleObject> getSchedulerByField(String field, Object value);

}
