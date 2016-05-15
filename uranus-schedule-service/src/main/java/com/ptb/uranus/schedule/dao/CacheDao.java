package com.ptb.uranus.schedule.dao;

public interface CacheDao {
    boolean hasKey(String key);

    String getValue(String key);

    void setValue(String key, String value);

    void setValue(String key, String value, int exprie);

    void delValue(String key, String value);

    void delAllValue(String lamda);

    void delALL();
}