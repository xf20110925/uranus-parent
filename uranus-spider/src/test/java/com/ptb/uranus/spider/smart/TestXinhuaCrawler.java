package com.ptb.uranus.spider.smart;

import com.alibaba.fastjson.JSON;
import com.ptb.uranus.spider.smart.entity.Article;
import com.ptb.uranus.spider.smart.entity.DynamicData;
import com.ptb.uranus.spider.smart.entity.NewScheduleUrls;
import com.ptb.uranus.spider.smart.utils.SmartSpiderConverter;
import org.easymock.EasyMockSupport;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;
import java.util.Optional;

import static junit.framework.TestCase.assertTrue;

/**
 * Created by eric on 16/3/23.
 */
public class TestXinhuaCrawler extends EasyMockSupport {

    private SmartSpider smartSpider;

    @Before
    public void before() {
        smartSpider = new SmartSpider();
    }

    public void testArticleRight(String url) {
        Optional<SpiderResult> articleResult = smartSpider.crawl(url, "article");
        Article article = SmartSpiderConverter.convertToArticle(articleResult.get());
        assertTrue(article.isValidArticle());
        assertTrue(article.getUrl().length() > 0);
        assertTrue(article.getContent().length() > 0);
        assertTrue(article.getTitle().length() > 0);
        assertTrue(article.getPostTime() != article.getTime());
        assertTrue(article.getPlat() == 3);
    }

    public void testDynamicDataRight(String url) {
        Optional<SpiderResult> dynamicResult = smartSpider.crawl(url, "articleDynamicData");
        DynamicData dynamicData = SmartSpiderConverter.convertToDynamicData(dynamicResult.get());
        System.out.println(JSON.toJSON(dynamicData));
        assertTrue(dynamicData.getCommentNum() != -1);
    }

    /**
     * Test xinhua artilce 类型1.
     */
    @Test
    public void testArtilceType1() {
        String url = "http://news.xinhuanet.com/world/2016-04/05/c_128862086.htm";
        testArticleRight(url);
        testDynamicDataRight(url);
    }

    @Test
    public void testArticleType2() {
        String url = "http://news.xinhuanet.com/politics/2009-12/11/content_12633066.htm";
        testArticleRight(url);
//        testDynamicDataRight(url);
    }


    @Test
    public void testArticleType3() {
       String url = "http://news.xinhuanet.com/photo/2016-04/06/c_128868445.htm";
        testArticleRight(url);
//        testDynamicDataRight(url);
    }

    @Test
    public void testXinhuaListOne() {
        Optional<SpiderResult> articleList = smartSpider.crawl("http://search.news.cn/mb/xinhuanet/search/?nodetype=3&nodeid=1166741", "articleList");
        System.out.println(articleList);
    }


    @Test
    public void testXinhuaList() {
        String[] list = {
              /*  "http://203.192.8.57/was5/web/search?channelid=240083&prepage=100",  //时政
                "http://203.192.8.57/was5/web/search?channelid=210996&prepage=100", //国际
                "http://203.192.8.57/was5/web/search?channelid=261519&prepage=100", //本地
                "http://203.192.8.57/was5/web/search?channelid=230015&prepage=100", //法治
                "http://203.192.8.57/was5/web/search?channelid=214510&prepage=100", //财经
                "http://203.192.8.57/was5/web/search?channelid=276589&prepage=100", //汽车
                "http://203.192.8.57/was5/web/search?channelid=223342&prepage=100",//时尚 健康
                "http://203.192.8.57/was5/web/search?channelid=294491&prepage=100",//信息化
                "http://203.192.8.57/was5/web/search?channelid=280568&prepage=100", //华人
                "http://203.192.8.57/was5/web/search?channelid=234968&prepage=100", //科技 能源
                "http://203.192.8.57/was5/web/search?channelid=230141&prepage=100", //教育
                "http://203.192.8.57/was5/web/search?channelid=240083&prepage=100", //人事
                "http://203.192.8.57/was5/web/search?channelid=276589&prepage=100", //食品
                "http://203.192.8.57/was5/web/search?channelid=234968&prepage=100", //城市*/
                "http://203.192.8.57/was5/web/search?channelid=212058&prepage=100", //文化
                "http://travel.news.cn/gnylist.htm",//旅游
                "http://www.news.cn/cmzs.htm",//媒体之声
                "http://www.news.cn/",//新华网主页
                "http://www.news.cn/house/bj/24xsjx.htm", //房产
                "http://www.xinhuanet.com/mil/gundongxinwen.htm",//军事
                "http://www.news.cn/comments/zxpl.htm",  //"评论"
                "http://ent.news.cn/", //娱乐
                "http://www.news.cn/sports/",//体育
                "http://www.news.cn/caipiao/cpyw.htm", //彩票
/*
                "http://search.news.cn/mb/xinhuanet/search/?nodetype=3&nodeid=1166741", //资料
*/
                "http://www.news.cn/politics/leaders/gdxw.htm",//高层
        };
        Arrays.asList(list).stream().forEach(s -> {
            try {
                System.out.println(String.format("\n正在爬取文章列表[%s]\n", s));
                Optional<SpiderResult> articleList = smartSpider.crawl(s, "articleList");
                assertTrue(articleList.isPresent());
                NewScheduleUrls newScheduleUrls = SmartSpiderConverter.convertToNewSchedulerUrls(0L, articleList.get());
                assertTrue(newScheduleUrls.getUrls().size() > 0);
                assertTrue(newScheduleUrls.getLastPostTime() > 0);
                System.out.println(String.format("\n正在爬取文章列表[%s] 成功\n", s));
            } catch (Exception e) {
                System.out.println(String.format("crawle url [%s] error", s));
                throw e;
            }
        });
    }

    @After
    public void close() {
        smartSpider.close();
    }
}
