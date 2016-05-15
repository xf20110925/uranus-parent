package com.ptb.uranus.schedule.model;

import com.ptb.uranus.common.entity.CollectCondition;
import com.ptb.uranus.common.entity.CollectType;


/**
 * Created by eric on 16/4/21.
 */
public class SchedulableCollectCondition extends CollectCondition implements Scheduable {
    public SchedulableCollectCondition() {
    }

    public SchedulableCollectCondition(CollectType collectType, String conditon) {
        super(collectType, conditon);
    }

    @Override
    public String getC() {
        return this.getClass().getName();
    }
}
