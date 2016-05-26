package com.ptb.uranus.spider.smart;

import com.alibaba.fastjson.JSON;
import com.ptb.uranus.spider.smart.entity.Article;
import com.ptb.uranus.spider.smart.entity.DynamicData;
import com.ptb.uranus.spider.smart.entity.NewScheduleUrls;
import com.ptb.uranus.spider.smart.utils.SmartSpiderConverter;
import org.easymock.EasyMockSupport;
import org.junit.After;
import org.junit.Before;

import java.util.Optional;

import static junit.framework.TestCase.assertTrue;

/**
 * Created by xuefeng on 16/3/23.
 */
public class Test163Crawler extends EasyMockSupport {

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
        assertTrue(dynamicData.getInvolvementNum() != -1);
        assertTrue(dynamicData.getCommentNum() != -1);
    }

    /**
     * Test 163 artilce 类型1.
     */
    public void testArtilceType1() {
        String url = "http://news.163.com/16/0407/13/BK262N0J00014JB6.html";
        testArticleRight(url);
        testDynamicDataRight(url);
    }

    public void testArticleType2() {
        String url = "http://discovery.163.com/16/0405/04/BJS2LEAI000125LI.html";
        testArticleRight(url);
        testDynamicDataRight(url);
    }


    public void testArticleType3() {
        String url = "http://war.163.com/16/0407/10/BK1U8PBH00014OMD.html";
        testArticleRight(url);
        testDynamicDataRight(url);
    }

    public void testArticleType4() {
        //source可能娶不到
        String url = "http://war.163.com/photoview/4T8E0001/116517.html#p=BL87UCAC4T8E0001";
        testArticleRight(url);
        testDynamicDataRight(url);
    }

    public void testArticleType5() {
        String url = "http://news.163.com/photoview/00AP0001/115230.html#p=BK1SG5SQ00AP0001";
        testArticleRight(url);
        testDynamicDataRight(url);
    }

    public void testArticleType6() {
        String url = "http://view.163.com/16/0407/11/BK1VGQR9000159SL.html";
        testArticleRight(url);
        testDynamicDataRight(url);
    }

    public void test163ArticleList() {
        Optional<SpiderResult> articleList = smartSpider.crawl("http://news.163.com/latest/", "articleList");
        NewScheduleUrls newScheduleUrls = SmartSpiderConverter.convertToNewSchedulerUrls(-1L, articleList.get());
        assertTrue(newScheduleUrls.getUrls().size() > 0);
        assertTrue(newScheduleUrls.getLastPostTime() > 0);
    }

    @After
    public void close() {
        smartSpider.close();
    }
}
