package com.ptb.uranus.schedule;


import com.ptb.uranus.common.entity.CollectType;
import com.ptb.uranus.schedule.model.Priority;
import com.ptb.uranus.schedule.trigger.Trigger;

import java.util.List;

/**
 * Created by eric on 16/4/21.
 */
public interface Collector {
    String collect(String inputValue, CollectType collectType, Trigger trigger, Priority priority);
    List<String> collect(List<String> inputValue, CollectType collectType, Trigger trigger, Priority priority);
}
