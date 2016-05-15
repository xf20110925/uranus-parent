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
 * Created by xuefeng on 16/3/23.
 */
public class TestSinaCrawler extends EasyMockSupport {

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
        assertTrue(article.getPlat() == 5);
    }

    public void testDynamicDataRight(String url) {
        Optional<SpiderResult> dynamicResult = smartSpider.crawl(url, "articleDynamicData");
        DynamicData dynamicData = SmartSpiderConverter.convertToDynamicData(dynamicResult.get());
        System.out.println(JSON.toJSON(dynamicData));
        assertTrue(dynamicData.getCommentNum() != -1);
/*        assertTrue(dynamicData.getInvolvementNum() != -1);*/
    }

    /**
     * Test sina artilce 类型1.
     */
    @Test
    public void testArtilceType1() {
        String url = "http://news.sina.com.cn/o/2016-04-07/doc-ifxrcizu3727647.shtml";
        testArticleRight(url);
        testDynamicDataRight(url);
    }

    @Test
    public void testArticleType2() {
        String url = "http://sports.sina.com.cn/basketball/nba/2016-04-07/doc-ifxrcizs6961157.shtml";
        testArticleRight(url);
        testDynamicDataRight(url);
    }


    @Test
    public void testArticleType3() {
        String url = "http://mil.news.sina.com.cn/world/2016-04-07/doc-ifxrcizu3732650.shtml";
        testArticleRight(url);
        testDynamicDataRight(url);
    }

    @Test
    public void testArticleType4() {
        String url = "http://tech.sina.com.cn/i/2016-04-07/doc-ifxrcizs6957330.shtml";
        testArticleRight(url);
        testDynamicDataRight(url);
    }

    @Test
    public void testArticleType5() {
        String url = "http://finance.sina.com.cn/stock/usstock/c/2016-04-22/doc-ifxrpvea1081061.shtml";
        testArticleRight(url);
        testDynamicDataRight(url);
    }

    @Test
    public void testArticleType6() {
        String url = "http://finance.sina.com.cn/china/gncj/2016-04-07/doc-ifxrckae7592947.shtml";
        testArticleRight(url);
        testDynamicDataRight(url);
    }

    @Test
    public void testSinaArticleList() {
        Optional<SpiderResult> articleList = smartSpider.crawl("http://roll.news.sina.com.cn/s/channel.php", "articleList");
        NewScheduleUrls newScheduleUrls = SmartSpiderConverter.convertToNewSchedulerUrls(-1L, articleList.get());
        assertTrue(newScheduleUrls.getUrls().size() > 0);
        assertTrue(newScheduleUrls.getLastPostTime() > 0);
    }

    @After
    public void close() {
        smartSpider.close();
    }
}
