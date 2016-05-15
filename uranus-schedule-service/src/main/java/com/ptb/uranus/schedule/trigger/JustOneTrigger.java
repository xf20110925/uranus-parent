package com.ptb.uranus.schedule.trigger;

/**
 * 定时一次爬取触发器
 */
public class JustOneTrigger extends Trigger {
    long s;

    public JustOneTrigger() {
        this.c = this.getC();
        this.s = System.currentTimeMillis();
    }

    public JustOneTrigger(Long startTime) {
        long curTime = System.currentTimeMillis() + 2000;
        this.s = startTime > System.currentTimeMillis() ? startTime : curTime;
    }

    public Long getS() {
        return s;
    }

    public void setS(long s) {
        this.s = s;
    }

    @Override
    public long nextTriggeTime() {
        return System.currentTimeMillis() - 100  > s ? Long.MAX_VALUE : s;
    }

}
