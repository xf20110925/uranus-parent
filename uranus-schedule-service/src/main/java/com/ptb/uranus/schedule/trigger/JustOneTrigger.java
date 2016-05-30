package com.ptb.uranus.schedule.trigger;

import org.bson.Document;

/**
 * 定时一次爬取触发器
 */
public class JustOneTrigger extends Trigger {
    long s;

    Boolean isFirstSchedule;
    public JustOneTrigger() {
        this.c = this.getC();
        this.s = System.currentTimeMillis();
        this.isFirstSchedule = false;
    }

    public JustOneTrigger(Long startTime) {
        long curTime = System.currentTimeMillis() + 10000;
        this.s = startTime > System.currentTimeMillis() ? startTime : curTime;
        this.isFirstSchedule = true;
    }

    public Long getS() {
        return s;
    }

    public void setS(long s) {
        this.s = s;
    }

    @Override
    public long nextTriggeTime() {
        if(isFirstSchedule) {
            return System.currentTimeMillis();
        }
        return System.currentTimeMillis() - 100  > s ? Long.MAX_VALUE : s;
    }

}
