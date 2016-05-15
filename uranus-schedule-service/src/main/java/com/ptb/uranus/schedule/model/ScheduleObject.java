package com.ptb.uranus.schedule.model;

import com.alibaba.fastjson.JSON;
import com.ptb.uranus.common.entity.CollectType;
import com.ptb.uranus.schedule.trigger.JustOneTrigger;
import com.ptb.uranus.schedule.trigger.Trigger;
import org.bson.types.ObjectId;

import java.util.Date;

/**
 * Created by eric on 16/4/21.
 */
public class ScheduleObject<T extends Scheduable> {
    String id;
    Trigger trigger;
    long lTime;
    long nTime;
    int sCnt;
    int fCnt;
    boolean isDone;
    Priority priority;
    T obj;

    public ScheduleObject() {

    }

    public boolean isDone() {
        return isDone;
    }

    public void setDone(boolean done) {
        isDone = done;
    }

    public ScheduleObject(Trigger trigger, Priority priority, T obj) {
        this.trigger = trigger;
        id = ObjectId.get().toHexString();
        lTime = -1L;
        nTime = trigger.nextTriggeTime();
        sCnt = 0;
        fCnt = 0;
        this.obj = obj;
        this.priority = priority;
        this.isDone = false;
    }


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Trigger getTrigger() {
        return trigger;
    }

    public void setTrigger(String trigger) throws ClassNotFoundException {
        String className = (String) JSON.parseObject(trigger).get("c");
        this.trigger = JSON.parseObject(trigger, (Class<Trigger>) Class.forName(className));
    }

    public T getObj() {
        return obj;
    }

    public void setObj(String obj) throws ClassNotFoundException {
        String className = (String) JSON.parseObject(obj).get("c");
        this.obj = JSON.parseObject(obj, (Class<T>) Class.forName(className));
    }


    public void setObjByT(T t) {
        this.obj = t;
    }

    public long getlTime() {
        return lTime;
    }

    public void setlTime(long lTime) {
        this.lTime = lTime;
    }

    public long getnTime() {
        return nTime;
    }

    public void setnTime(long nTime) {
        this.nTime = nTime;
        if (nTime == Long.MAX_VALUE) {
            isDone = true;
        }
    }


    public Priority getPriority() {
        return priority;
    }

    public void setPriority(Priority priority) {
        this.priority = priority;
    }


    public int getsCnt() {
        return sCnt;
    }

    public void setsCnt(int sCnt) {
        this.sCnt = sCnt;
    }

    public int getfCnt() {
        return fCnt;
    }

    public void setfCnt(int fCnt) {
        this.fCnt = fCnt;
    }

    @Override
    public String toString() {
        return JSON.toJSONString(this);
    }

    public static void main(String[] args) {
        System.out.println(
                JSON.parseObject(new ScheduleObject(new JustOneTrigger(new Date().getTime()),
                        Priority.L1, new SchedulableCollectCondition(CollectType.C_A_A_D, "ddddd")).toString(), ScheduleObject.class));
    }
}
