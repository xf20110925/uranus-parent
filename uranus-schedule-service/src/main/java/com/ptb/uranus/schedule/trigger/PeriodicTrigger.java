package com.ptb.uranus.schedule.trigger;


import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

/**
 * 周期性爬取命令
 */
public class PeriodicTrigger extends Trigger {

    long v;
    TimeUnit u;
    long st;
    long et;
    boolean isFirstSchedule;


    public PeriodicTrigger() {
        isFirstSchedule = false;
    }

    public long getEt() {
        return et;
    }

    public void setEt(long et) {
        this.et = et;
    }

    public long getSt() {
        return st;
    }

    public void setSt(long st) {
        this.st = st;
    }

    public long getV() {
        return v;
    }

    public void setV(long v) {
        this.v = v;
    }

    public TimeUnit getU() {
        return u;
    }

    public void setU(TimeUnit u) {
        this.u = u;
    }


    private Date calDate(int CollectorDays) {
        Calendar instance = Calendar.getInstance(Locale.CHINA);
        instance.add(Calendar.DATE, CollectorDays);

        return instance.getTime();
    }

    public PeriodicTrigger(long v, TimeUnit timeUnit, Date start, Date stopDate) {
        long curTime = System.currentTimeMillis();
        this.v = v;
        this.u = timeUnit;
        this.st = start.getTime();
        this.et = stopDate.getTime();
        this.st = this.st > curTime ? this.st : curTime;
        this.isFirstSchedule = true;
    }

    public PeriodicTrigger(long v, TimeUnit timeUnit, Date start, int MonitorDays) {
        long curTime = System.currentTimeMillis();
        this.v = v;
        this.u = timeUnit;
        this.st = start.getTime();
        this.et = calDate(MonitorDays).getTime();
        this.st = this.st > curTime ? this.st : curTime;
        this.isFirstSchedule = true;
    }

    /*
     * Instantiates a new Periodic trigger.
     * 默认监控3天
     *
     * @param v        the v value
     * @param timeUnit the v unit
     */
    public PeriodicTrigger(long v, TimeUnit timeUnit) {
        this.v = v;
        this.u = timeUnit;
        this.st = System.currentTimeMillis();
        this.et = calDate(3).getTime();
        this.isFirstSchedule = true;
    }

    @Override
    public long nextTriggeTime() {
        long l = u.toMillis(v);
        long curTime = System.currentTimeMillis() - 1000;

        long nt = curTime + l;
        if (isFirstSchedule == true) {
            nt = curTime;
            return nt;
        }
        nt = curTime <= st ? st : nt;
        return et < curTime ? Long.MAX_VALUE : nt;
    }
}
