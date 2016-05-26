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
public class TestSohuCrawler extends EasyMockSupport {

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
        assertTrue(article.getPlat() == 4);
    }

    /**
     * Test sohu artilce 类型1.
     */
    public void testArtilceType1() {
        String url = "http://roll.sohu.com/20160403/n443245269.shtml";
        testArticleRight(url);
        Optional<SpiderResult> dynamicResult = smartSpider.crawl(url, "articleDynamicData");
        DynamicData dynamicData = SmartSpiderConverter.convertToDynamicData(dynamicResult.get());
        System.out.println(JSON.toJSON(dynamicData));
        assertTrue(dynamicData.getCommentNum() != -1);
        assertTrue(dynamicData.getInvolvementNum() != -1);
    }

    public void testArticleType2() {
        String url = "http://yule.sohu.com/20160403/n443234853.shtml";
        testArticleRight(url);
        Optional<SpiderResult> dynamicResult = smartSpider.crawl(url, "articleDynamicData");
        DynamicData dynamicData = SmartSpiderConverter.convertToDynamicData(dynamicResult.get());
        System.out.println(JSON.toJSON(dynamicData));
        assertTrue(dynamicData.getInvolvementNum() != -1);
    }


    public void testArticleType3() {
        String url = "http://it.sohu.com/20160403/n443231695.shtml";
        testArticleRight(url);
        Optional<SpiderResult> dynamicResult = smartSpider.crawl(url, "articleDynamicData");
        DynamicData dynamicData = SmartSpiderConverter.convertToDynamicData(dynamicResult.get());
        System.out.println(JSON.toJSON(dynamicData));
        assertTrue(dynamicData.getCommentNum() != -1);
        assertTrue(dynamicData.getInvolvementNum() != -1);
        assertTrue(dynamicData.getLikeNum() != -1);
        assertTrue(dynamicData.getLikeNum() != -1);
        assertTrue(dynamicData.getBoringNum() != -1);
    }

    public void testArticleType4() {
        String url =  "http://auto.sohu.com/20160425/n445945404.shtml";
        testArticleRight(url);
        Optional<SpiderResult> dynamicResult = smartSpider.crawl(url, "articleDynamicData");
        DynamicData dynamicData = SmartSpiderConverter.convertToDynamicData(dynamicResult.get());
        System.out.println(JSON.toJSON(dynamicData));
        assertTrue(dynamicData.getCommentNum() != -1);
        assertTrue(dynamicData.getInvolvementNum() != -1);
    }

    public void testArticleType5() {
        String url = "http://fashion.sohu.com/20160403/n443235687.shtml";
        testArticleRight(url);
        Optional<SpiderResult> dynamicResult = smartSpider.crawl(url, "articleDynamicData");
        DynamicData dynamicData = SmartSpiderConverter.convertToDynamicData(dynamicResult.get());
        System.out.println(JSON.toJSON(dynamicData));
        assertTrue(dynamicData.getCommentNum() != -1);
        assertTrue(dynamicData.getInvolvementNum() != -1);
        assertTrue(dynamicData.getLikeNum() != -1);
        assertTrue(dynamicData.getLikeNum() != -1);
        assertTrue(dynamicData.getBoringNum() != -1);
    }

    public void testArticleType6() {
        String url = "http://learning.sohu.com/20160403/n443237925.shtml";
        testArticleRight(url);
        Optional<SpiderResult> dynamicResult = smartSpider.crawl(url, "articleDynamicData");
        DynamicData dynamicData = SmartSpiderConverter.convertToDynamicData(dynamicResult.get());
        System.out.println(JSON.toJSON(dynamicData));
        assertTrue(dynamicData.getCommentNum() != -1);
        assertTrue(dynamicData.getInvolvementNum() != -1);
        assertTrue(dynamicData.getLikeNum() != -1);
        assertTrue(dynamicData.getLikeNum() != -1);
        assertTrue(dynamicData.getBoringNum() != -1);
    }

    public void testArticleType7() {
        String url = "http://stock.sohu.com/20160403/n443236757.shtml";
        testArticleRight(url);
        Optional<SpiderResult> dynamicResult = smartSpider.crawl(url, "articleDynamicData");
        DynamicData dynamicData = SmartSpiderConverter.convertToDynamicData(dynamicResult.get());
        System.out.println(JSON.toJSON(dynamicData));
        assertTrue(dynamicData.getInvolvementNum() != -1);
    }

    public void testSohuListOne() {
        Optional<SpiderResult> articleList = smartSpider.crawl("http://news.sohu.com/scroll/", "articleList");
        NewScheduleUrls newScheduleUrls = SmartSpiderConverter.convertToNewSchedulerUrls(0L, articleList.get());
        assertTrue(newScheduleUrls.getUrls().size() > 0);
        assertTrue(newScheduleUrls.getLastPostTime() > 0);
    }

   /* @Test
    public void testSohuList() {
        String[] list = {
                "http://roll.sohu.com/sports/",//体育
                "http://roll.sohu.com/yule/",//娱乐
                "http://roll.sohu.com/it/",//IT数码
                "http://roll.sohu.com/auto/", //汽车
                "http://roll.sohu.com/focus/",//房产
                "http://roll.sohu.com/women/",  //"女性"
                "http://roll.sohu.com/fashion/", //时尚
                "http://roll.sohu.com/health/",//健康
                "http://roll.sohu.com/cul/", //文化
                "http://roll.sohu.com/learning/", //教育
                "http://roll.sohu.com/money/",//财经
                "http://roll.sohu.com/stock/",//证券
                "http://roll.sohu.com/games/",//游戏
                "http://roll.sohu.com/media/",//媒体
                "http://roll.sohu.com/star/",//评论
                "http://roll.sohu.com/travel/",//旅游
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
    }*/

    @After
    public void close() {
        smartSpider.close();
    }
}
