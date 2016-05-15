package com.ptb.uranus.spider.smart;

import com.alibaba.fastjson.JSON;
import com.ptb.uranus.spider.smart.entity.Article;
import com.ptb.uranus.spider.smart.entity.DynamicData;
import com.ptb.uranus.spider.smart.entity.NewScheduleUrls;
import com.ptb.uranus.spider.smart.utils.SmartSpiderConverter;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.Optional;

import static junit.framework.TestCase.assertTrue;

/**
 * Created by on 16/4/7.
 */
public class TestIfengCrawler {
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
        assertTrue(article.getClassify().length() > 0);
        assertTrue(article.getSource().length() > 0);
    }

    public void testDynamicDataRight(String url) {
        Optional<SpiderResult> dynamicResult = smartSpider.crawl(url, "articleDynamicData");
        DynamicData dynamicData = SmartSpiderConverter.convertToDynamicData(dynamicResult.get());
        System.out.println(JSON.toJSON(dynamicData));
        assertTrue(dynamicData.getCommentNum() != -1);
        assertTrue(dynamicData.getInvolvementNum() != -1);
    }


    @Test
    public void testArtilceType1() {
        String url = "http://news.ifeng.com/a/20160406/48371905_0.shtml";
        testArticleRight(url);
        testDynamicDataRight(url);
    }

    @Test
    public void testArtilceType2() {
        String url = "http://sports.ifeng.com/a/20160407/48377789_0.shtml";
        testArticleRight(url);
        testDynamicDataRight(url);
    }

    @Test
    public void testArtilceType3() {
        String url = "http://finance.ifeng.com/a/20160407/14309472_0.shtml";
        testArticleRight(url);
        testDynamicDataRight(url);
    }

    @Test
    public void testArtilceType4() {
        String url = "http://ent.ifeng.com/a/20160406/42601413_0.shtml";
        testArticleRight(url);
        testDynamicDataRight(url);
    }

    @Test
    public void testArtilceType5() {
        String url = "http://fashion.ifeng.com/a/20160407/40156742_0.shtml";
        testArticleRight(url);
        testDynamicDataRight(url);
    }

    @Test
    public void testArtilceType6() {
        String url = "http://tech.ifeng.com/a/20160407/41591343_0.shtml";
        testArticleRight(url);
        testDynamicDataRight(url);
    }

    @Test
    public void testArtilceType7() {
        String url = "http://book.ifeng.com/a/20160406/19145_0.shtml";
        testArticleRight(url);
        testDynamicDataRight(url);
    }

    @Test
    public void testArtilceType8() {
        String url = "http://culture.ifeng.com/a/20160407/48380646_0.shtml";
        testArticleRight(url);
        testDynamicDataRight(url);
    }

    @Test
    public void testArtilceType9() {
        String url = "http://guoxue.ifeng.com/a/20160407/48381740_0.shtml";
        testArticleRight(url);
        testDynamicDataRight(url);
    }

    @Test
    public void testArtilceType10() {
        String url = "http://jiu.ifeng.com/a/20160407/41591020_0.shtml";
        testArticleRight(url);
        testDynamicDataRight(url);
    }

    @Test
    public void testArtilceType11() {
        String url = "http://travel.ifeng.com/a/20160407/41591294_0.shtml";
        testArticleRight(url);
        testDynamicDataRight(url);
    }

    @Test
    public void testArtilceType12() {
        String url = "http://gongyi.ifeng.com/a/20160407/41591101_0.shtml";
        testArticleRight(url);
        testDynamicDataRight(url);
    }

    @Test
    public void testArtilceType13() {
        String url = "http://yue.ifeng.com/a/20160401/39757254_0.shtml";
        testArticleRight(url);
        testDynamicDataRight(url);
    }

    @Test
    public void testArtilceType14() {
        String url = "http://talk.ifeng.com/a/20151215/41523805_0.shtml";
        testArticleRight(url);
        testDynamicDataRight(url);
    }

    @Test
    public void testArtilceType15() {
        String url = "http://innovation.ifeng.com/frontier/detail_2016_03/29/4797673_0.shtml";
        testArticleRight(url);
        testDynamicDataRight(url);
    }

    @Test
    public void testArtilceType16() {
        String url = "http://fo.ifeng.com/a/20160407/41591036_0.shtml";
        testArticleRight(url);
        testDynamicDataRight(url);
    }

    @Test
    public void testArtilceType17() {
        String url = "http://games.ifeng.com/a/20160407/41591233_0.shtml";
        testArticleRight(url);
        testDynamicDataRight(url);
    }

    @Test
    public void testThepaperList1() {
        Optional<SpiderResult> articleList = smartSpider.crawl("http://news.ifeng.com/gundong/list_0/0.shtml", "articleList");
        NewScheduleUrls newScheduleUrls = SmartSpiderConverter.convertToNewSchedulerUrls(-1L, articleList.get());
        assertTrue(newScheduleUrls.getUrls().size() > 0);
        assertTrue(newScheduleUrls.getLastPostTime() > 0);
    }

    @Test
    public void testThepaperList2() {
        Optional<SpiderResult> articleList = smartSpider.crawl("http://finance.ifeng.com/roll/", "articleList");
        NewScheduleUrls newScheduleUrls = SmartSpiderConverter.convertToNewSchedulerUrls(-1L, articleList.get());
        assertTrue(newScheduleUrls.getUrls().size() > 0);
        assertTrue(newScheduleUrls.getLastPostTime() > 0);
    }

    @Test
    public void testThepaperList3() {
        Optional<SpiderResult> articleList = smartSpider.crawl("http://ent.ifeng.com/zz/list_0/0.shtml", "articleList");
        NewScheduleUrls newScheduleUrls = SmartSpiderConverter.convertToNewSchedulerUrls(-1L, articleList.get());
        assertTrue(newScheduleUrls.getUrls().size() > 0);
        assertTrue(newScheduleUrls.getLastPostTime() > 0);
    }

    @Test
    public void testThepaperList4() {
        Optional<SpiderResult> articleList = smartSpider.crawl("http://edu.ifeng.com/gundong/list_0/0.shtml", "articleList");
        NewScheduleUrls newScheduleUrls = SmartSpiderConverter.convertToNewSchedulerUrls(-1L, articleList.get());
        assertTrue(newScheduleUrls.getUrls().size() > 0);
        assertTrue(newScheduleUrls.getLastPostTime() > 0);
    }

    @Test
    public void testThepaperList5() {
        Optional<SpiderResult> articleList = smartSpider.crawl("http://tech.ifeng.com/gundong/list_0/0.shtml", "articleList");
        NewScheduleUrls newScheduleUrls = SmartSpiderConverter.convertToNewSchedulerUrls(-1L, articleList.get());
        assertTrue(newScheduleUrls.getUrls().size() > 0);
        assertTrue(newScheduleUrls.getLastPostTime() > 0);
    }

    @After
    public void close() {
        smartSpider.close();
    }
}
