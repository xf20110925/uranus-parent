package com.ptb.uranus.manager.dao;

import com.ptb.uranus.manager.UranusManagerApplication;
import com.ptb.uranus.manager.bean.ActionSet;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import java.util.List;

/**
 * Created by eric on 16/3/28.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = UranusManagerApplication.class)
@WebAppConfiguration
public class SpiderDaoTest {

    @Autowired
    SpiderDao spiderDao;

    @Test
    public void testGetSpiderList() throws Exception {
        List<ActionSet> spiderList = spiderDao.getSpiderList();
        spiderList.forEach(k -> System.out.println(k));
    }
}