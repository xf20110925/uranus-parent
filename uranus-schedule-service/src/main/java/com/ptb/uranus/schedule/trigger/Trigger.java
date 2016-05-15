package com.ptb.uranus.schedule.trigger;

/**
 * Created by eric on 16/4/21.
 */
public abstract class Trigger {
    String c = this.getClass().getName();

    public String getC() {
        return c;
    }

    public void setC(String c) {
        this.c = c;
    }

    abstract public long nextTriggeTime();
}
