package com.ptb.uranus.spider.weibo;

import com.ptb.uranus.spider.weibo.bean.WeiboArticle;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.Optional;

import static junit.framework.TestCase.assertEquals;
import static junit.framework.TestCase.assertTrue;

/**
 * Created by eric on 16/2/1.
 */
public class WbArticleTest {
    WeiboSpider weiboSpider;

    @Before
    public void Before() {
        weiboSpider = new WeiboSpider();
    }

    /**
     * Test get article static by url success.
     */
    @Test
    public void testGetArticleImgByUrlSuccess() {

        Optional<WeiboArticle> weiboArticleOptional = weiboSpider.getWeiboArticleByArticleUrl("http://weibo.com/3905981269/EfEHCq9o5");

        assertEquals(weiboArticleOptional.isPresent(), true);
        WeiboArticle weiboArticle = weiboArticleOptional.get();
        assertTrue(weiboArticle.getContent().length() > 0);
        assertTrue(weiboArticle.getHeadImg().length() > 0);
        assertTrue(weiboArticle.getMediaId().length() > 0);
        assertTrue(weiboArticle.getMediaName().length() > 0);
        assertTrue(weiboArticle.getLikeCount() >= 0);
        assertTrue(weiboArticle.getPostTime() >= 0);
        assertTrue(weiboArticle.getRepostCount() >= 0);
        assertTrue(weiboArticle.getCommentCount() >= 0);
        assertTrue(weiboArticle.getImgs().size() > 0);
        assertTrue(weiboArticle.isOriginality() == true ? weiboArticle.isOriginality() == false : false);
    }

    /**
     * Test 包含视频字段的文章
     */
    @Test
    public void testGetArticleVideoByUrlSuccess() {
        Optional<WeiboArticle> weiboArticleOptional = weiboSpider.getWeiboArticleByArticleUrl("http://m.weibo.cn/1750070171/DBIwat7Sr");

        assertEquals(weiboArticleOptional.isPresent(), true);
        WeiboArticle weiboArticle = weiboArticleOptional.get();
        assertTrue(weiboArticle.getContent().length() > 0);
        assertTrue(weiboArticle.getHeadImg().length() > 0);
        assertTrue(weiboArticle.getMediaId().length() > 0);
        assertTrue(weiboArticle.getMediaName().length() > 0);
        assertTrue(weiboArticle.getLikeCount() > 0);
        assertTrue(weiboArticle.getPostTime() > 0);
        assertTrue(weiboArticle.getRepostCount() > 0);
        assertTrue(weiboArticle.getCommentCount() > 0);
/*        assertTrue(weiboArticle.getVideos().size() > 0);*/
    }


    /**
     * Test Error article
     */
    @Test
    public void testGetArticleStaticByUrlError() {
        Optional<WeiboArticle> weiboArticleOptional = weiboSpider.getWeiboArticleByArticleUrl("http://www.weibo.com/2913327245/DxkaNB");
        assertEquals(weiboArticleOptional.isPresent(), false);
    }
    @After
    public void after() {
        weiboSpider.close();
    }

    @Test
    public void testGetArticleByPc() throws Exception {
        String url = "http://weibo.com/5659237191/DsBPdb0xu?ref=home&rid=0_0_1_2669681800934230908&type=comment";
        Optional<WeiboArticle> weiboArticle = weiboSpider.getWeiboArticleThroughPc(url);
    }

    @Test
    public void testGetRecentArticleByPc() throws Exception {
        String weiboId = "2836096185";
        long startTime = 0;
        Optional<WeiboArticle> weiboArticle = weiboSpider.getWeiboRecentArticleThroughPc(weiboId, startTime);
    }

}
