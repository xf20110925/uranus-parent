package com.ptb.uranus.schedule.dao;

import org.junit.Test;

import static org.junit.Assert.assertTrue;

/**
 * Created by eric on 16/4/25.
 */
public class RedisCacheDaoTest {
    CacheDao cacheDao = new RedisCacheDao();

    @Test
    public void getValue() throws Exception {
        cacheDao.setValue("1", "1");
        assertTrue(cacheDao.hasKey("1"));
        assertTrue(cacheDao.getValue("1").equals("1"));
        cacheDao.setValue("2","2",3);
        assertTrue(cacheDao.getValue("2") != null);
        Thread.sleep(5000);
        assertTrue(cacheDao.getValue("2") == null);

    }

}