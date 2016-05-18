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
 * Created by on 16/4/8.
 */
public class TestQQCrawler {

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
        assertTrue(article.getPlat() == 7);
    }

    public void testDynamicDataRight(String url) {
        Optional<SpiderResult> dynamicResult = smartSpider.crawl(url, "articleDynamicData");
        DynamicData dynamicData = SmartSpiderConverter.convertToDynamicData(dynamicResult.get());
        System.out.println(JSON.toJSON(dynamicData));
        assertTrue(dynamicData.getCommentNum() != -1);
    }

    public void testArtilceType1() {
        String url = "http://news.qq.com/a/20160407/025294.htm";
        testArticleRight(url);
        testDynamicDataRight(url);
    }

    public void testArtilceType2() {
        String url = "http://finance.qq.com/a/20160407/009688.htm";
        testArticleRight(url);
        testDynamicDataRight(url);
    }

    public void testArtilceType3() {
        String url = "http://stock.qq.com/a/20160407/041632.htm";
        testArticleRight(url);
        testDynamicDataRight(url);
    }

    public void testArtilceType4() {
        String url = "http://finance.qq.com/a/20160407/034048.htm";
        testArticleRight(url);
        testDynamicDataRight(url);
    }

    public void testArtilceType5() {
        String url = "http://sports.qq.com/a/20160407/033674.htm";
        testArticleRight(url);
        testDynamicDataRight(url);
    }

    public void testArtilceType6() {
        String url = "http://ent.qq.com/a/20160407/010494.htm";
        testArticleRight(url);
        testDynamicDataRight(url);
    }

    public void testArtilceType7() {
        String url = "http://fashion.qq.com/a/20160407/007515.htm";
        testArticleRight(url);
        testDynamicDataRight(url);
    }

    public void testArtilceType8() {
        String url = "http://baby.qq.com/a/20160407/019932.htm";
        testArticleRight(url);
        testDynamicDataRight(url);
    }

    public void testArtilceType9() {
        String url = "http://health.qq.com/a/20160405/019726.htm";
        testArticleRight(url);
        testDynamicDataRight(url);
    }

    public void testArtilceType10() {
        String url = "http://auto.qq.com/a/20160407/007827.htm";
        testArticleRight(url);
        testDynamicDataRight(url);
    }

    public void testArtilceType11() {
        String url = "http://house.qq.com/a/20160406/043301.htm";
        testArticleRight(url);
        testDynamicDataRight(url);
    }

    public void testArtilceType12() {
        String url = "http://tech.qq.com/a/20160407/033111.htm";
        testArticleRight(url);
        testDynamicDataRight(url);
    }

    public void testArtilceType14() {
        String url = "http://games.qq.com/a/20160407/003330.htm#p=1";
        testArticleRight(url);
        testDynamicDataRight(url);
    }

    public void testArtilceType15() {
        String url = "http://edu.qq.com/a/20160408/010963.htm";
        testArticleRight(url);
        testDynamicDataRight(url);
    }

    public void testArtilceType16() {
        String url = "http://cul.qq.com/a/20160407/051306.htm";
        testArticleRight(url);
        testDynamicDataRight(url);
    }

    public void testArtilceType17() {
        String url = "http://foxue.qq.com/a/20160406/044545.htm";
        testArticleRight(url);
        testDynamicDataRight(url);
    }

    public void testThepaperList1() {
        Optional<SpiderResult> articleList = smartSpider.crawl("http://roll.news.qq.com/", "articleList");
        NewScheduleUrls newScheduleUrls = SmartSpiderConverter.convertToNewSchedulerUrls(-1L, articleList.get());
        assertTrue(newScheduleUrls.getUrls().size() > 0);
        assertTrue(newScheduleUrls.getLastPostTime() > 0);
    }

    public void testThepaperList2() {
        Optional<SpiderResult> articleList = smartSpider.crawl("http://v.qq.com/", "articleList");
        NewScheduleUrls newScheduleUrls = SmartSpiderConverter.convertToNewSchedulerUrls(-1L, articleList.get());
        assertTrue(newScheduleUrls.getUrls().size() > 0);
        assertTrue(newScheduleUrls.getLastPostTime() > 0);
    }

    public void testThepaperList3() {
        Optional<SpiderResult> articleList = smartSpider.crawl("http://sports.qq.com/nbavideo/", "articleList");
        NewScheduleUrls newScheduleUrls = SmartSpiderConverter.convertToNewSchedulerUrls(-1L, articleList.get());
        assertTrue(newScheduleUrls.getUrls().size() > 0);
        assertTrue(newScheduleUrls.getLastPostTime() > 0);
    }

    @After
    public void close() {
        smartSpider.close();
    }

}
