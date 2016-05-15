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

import java.util.Optional;

import static junit.framework.TestCase.assertTrue;

/**
 * Created by eric on 16/3/23.
 */
public class TestPeopleCrawler extends EasyMockSupport {

    private SmartSpider smartSpider;

    @Before
    public void before() {
        smartSpider = new SmartSpider();
    }

    public void testArticleRight(String url) {
        Optional<SpiderResult> articleResult = smartSpider.crawl(url, "article");
        Article article = SmartSpiderConverter.convertToArticle( articleResult.get());
        assertTrue(article.isValidArticle());
        assertTrue(article.getContent().length() > 0);
        assertTrue(article.getTitle().length() > 0);
        assertTrue(article.getUrl().length() > 0);
        assertTrue(article.getPostTime() != article.getTime());
        assertTrue(article.getTime() > 0);
        assertTrue(article.getPlat() == 8);
    }

    @Test
    public void testList() {
        String url = "http://news.people.com.cn";
        try {
            System.out.println(String.format("\n正在爬取文章列表[%s]\n", "http://news.people.com.cn/"));
            Optional<SpiderResult> articleList = smartSpider.crawl("http://news.people.com.cn/", "articleList");
            assertTrue(articleList.isPresent());
            NewScheduleUrls newScheduleUrls = SmartSpiderConverter.convertToNewSchedulerUrls(0L, articleList.get());
            assertTrue(newScheduleUrls.getUrls().size() > 0);
            assertTrue(newScheduleUrls.getLastPostTime() > 0);
            System.out.println(String.format("\n正在爬取文章列表[%s] 成功\n", url));
        } catch (Exception e) {
            System.out.println(String.format("crawle url [%s] error", url));
            throw e;
        }
    }

    @Test
    public void testArticle1() {
        String url = "http://pic.people.com.cn/n1/2016/0407/c1016-28257245.html";

        Optional<SpiderResult> dyanmicResult = smartSpider.crawl(url, "articleDynamicData");
        DynamicData dynamicData = SmartSpiderConverter.convertToDynamicData(dyanmicResult.get());
        System.out.println(JSON.toJSON(dynamicData));
        assertTrue(dynamicData.getShareNum() != -1);
    }

    @Test
    public void testArticle2() {
        String url = "http://pic.people.com.cn/n1/2016/0407/c1016-28258490.html";
        testArticleRight(url);
        Optional<SpiderResult> dyanmicResult = smartSpider.crawl(url, "articleDynamicData");
        DynamicData dynamicData = SmartSpiderConverter.convertToDynamicData(dyanmicResult.get());
        System.out.println(JSON.toJSON(dynamicData));
        assertTrue(dynamicData.getShareNum() != -1);
    }

    @Test
    public void testArticle3() {
        String url = "http://society.people.com.cn/n1/2016/0407/c229589-28257960.html";
        testArticleRight(url);
        Optional<SpiderResult> dyanmicResult = smartSpider.crawl(url, "articleDynamicData");
        DynamicData dynamicData = SmartSpiderConverter.convertToDynamicData(dyanmicResult.get());
        System.out.println(JSON.toJSON(dynamicData));
        assertTrue(dynamicData.getShareNum() != -1);
    }

    @Test
    public void testArticle4() {
        String url = "http://game.people.com.cn/n1/2016/0407/c218877-28257598.html";
        testArticleRight(url);
    }


    @After
    public void close() {
        smartSpider.close();
    }
}
