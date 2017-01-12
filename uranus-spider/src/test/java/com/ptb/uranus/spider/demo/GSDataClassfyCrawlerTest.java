package com.ptb.uranus.spider.demo;

import com.ptb.uranus.spider.temp.GSDataClassfyCrawler;
import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * @DESC:
 * @VERSION:
 * @author: xuefeng
 * @Date: 2017/1/12
 * @Time: 13:11
 */
public class GSDataClassfyCrawlerTest {
    @Test
    public void getWxIdsTest() throws IOException {
        String url = "http://www.gsdata.cn/newRank/getwxranks?gid=45826&date=2017-01-11&page=1&type=day&cp=all";
        List<String> wxIds = GSDataClassfyCrawler.getWxIds(url, 2);
        System.out.println(wxIds);
        Assert.assertTrue(!wxIds.isEmpty());
    }

    @Test
    public void getAllinksTest() throws IOException {
        Map<String, Map<String, String>> allLinks = GSDataClassfyCrawler.getAllinks("http://www.gsdata.cn/rank/detail");
        System.out.println(allLinks);
        Assert.assertTrue(!allLinks.isEmpty());
    }
}
