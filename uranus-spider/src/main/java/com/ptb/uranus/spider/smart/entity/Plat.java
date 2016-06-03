package com.ptb.uranus.spider.smart.entity;

/**
 * Created by xuefeng on 2016/5/13.
 */
public class Plat {
    String id;
    String platName;
    String platCode;

    public Plat(String id, String platName, String platCode) {
        this.id = id;
        this.platName = platName;
        this.platCode = platCode;
    }

    public Plat() {
    }

    public String getPlatName() {
        return platName;
    }

    public void setPlatName(String platName) {
        this.platName = platName;
    }

    public String getPlatCode() {
        return platCode;
    }

    public void setPlatCode(String platCode) {
        this.platCode = platCode;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
