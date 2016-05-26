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
public class TestThepaperCrawler {
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
        assertTrue(article.getPlat() == 10);
    }

    public void testDynamicDataRight(String url) {
        Optional<SpiderResult> dynamicResult = smartSpider.crawl(url, "articleDynamicData");
        DynamicData dynamicData = SmartSpiderConverter.convertToDynamicData(dynamicResult.get());
        System.out.println(JSON.toJSON(dynamicData));
        assertTrue(dynamicData.getCommentNum() != -1);
        assertTrue(dynamicData.getLikeNum() != -1);
    }


    public void testArtilceType1() {
        String url = "http://www.thepaper.cn/newsDetail_forward_1448172";
        testArticleRight(url);
        testDynamicDataRight(url);
    }

    public void testThepaperList(){
        Optional<SpiderResult> articleList = smartSpider.crawl("http://www.thepaper.cn/", "articleList");
        NewScheduleUrls newScheduleUrls = SmartSpiderConverter.convertToNewSchedulerUrls(-1L, articleList.get());
        assertTrue(newScheduleUrls.getUrls().size() > 0);
        assertTrue(newScheduleUrls.getLastPostTime() > 0);
    }

    @After
    public void close() {
        smartSpider.close();
    }
}
