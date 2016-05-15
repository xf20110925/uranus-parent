package com.ptb.uranus.common.entity;

public class CollectCondition {
    CollectType collectType;
    String conditon;

    public CollectCondition() {
    }

    public CollectCondition(CollectType collectType, String conditon) {
        this.collectType = collectType;
        this.conditon = conditon;
    }

    public CollectType getCollectType() {
        return collectType;
    }

    public void setCollectType(CollectType collectType) {
        this.collectType = collectType;
    }

    public String getConditon() {
        return conditon;
    }

    public void setConditon(String conditon) {
        this.conditon = conditon;
    }

}