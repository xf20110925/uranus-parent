package com.ptb.uranus.schedule.dao;

import com.ptb.uranus.schedule.utils.JedisUtil;

/**
 * Created by eric on 16/4/25.
 */
public class RedisCacheDao implements CacheDao {

    @Override
    public boolean hasKey(String key) {
        if (this.getValue(key) == null) {
            return false;
        }

        return true;
    }

    @Override
    public String getValue(String key) {
        return JedisUtil.get(key);
    }

    @Override
    public void setValue(String key, String value) {
        JedisUtil.set(key.getBytes(), value.getBytes());
    }

    @Override
    public void setValue(String key, String value, int exprieSeconds) {
        JedisUtil.set(key.getBytes(), value.getBytes(), exprieSeconds);
    }

    @Override
    public void delValue(String key, String value) {
        JedisUtil.del(key.getBytes());
    }

    @Override
    public void delAllValue(String lamda) {
        JedisUtil.deleteAllKey(lamda);

    }

    @Override
    public void delALL() {
        JedisUtil.flushAll();
    }
}
