package com.ptb.uranus.spider.smart;

import com.ptb.uranus.spider.smart.entity.Article;
import com.ptb.uranus.spider.smart.utils.SmartSpiderConverter;
import org.junit.After;
import org.junit.Before;

import java.util.Optional;

import static junit.framework.TestCase.assertTrue;

/**
 * Created by on 16/4/7.
 */
public class TestWenshuSrawler {
    private SmartSpider smartSpider;

    @Before
    public void before() {
        smartSpider = new SmartSpider();
    }

    public void testArticleRight(Article article) {
        assertTrue(article.getUrl().length() > 0);
        assertTrue(article.getContent().length() > 0);
        assertTrue(article.getTitle().length() > 0);
        assertTrue(article.getPostTime() != article.getTime());
        assertTrue(article.getClassify().length() > 0);
        assertTrue(article.getSource().length() > 0);
    }


    public void testArtilceType1() {
        Optional<SpiderResult> articleResult = smartSpider.crawl("http://wenshu.court.gov.cn/content/content?DocID=8252121f-8260-4241-b707-018d52d151ca", "article");
        Article article = SmartSpiderConverter.convertToArticle(articleResult.get());

        testArticleRight(article);
    }

   /* @Test
    public void testThepaperList() {
        Optional<SpiderResult> articleList = smartSpider.crawl("http://wenshu.court.gov.cn/", "articleList");
        NewScheduleUrls newScheduleUrls = SmartSpiderConverter.convertToNewSchedulerUrls(-1L, articleList.get());
        assertTrue(newScheduleUrls.getUrls().size() > 0);
        assertTrue(newScheduleUrls.getLastPostTime() > 0);
    }*/

    @After
    public void close() {
        smartSpider.close();
    }
}
