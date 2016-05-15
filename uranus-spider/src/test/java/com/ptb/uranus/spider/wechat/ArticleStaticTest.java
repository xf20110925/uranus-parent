package com.ptb.uranus.spider.wechat;

import com.ptb.uranus.spider.weixin.WeixinSpider;
import com.ptb.uranus.spider.weixin.bean.WxArticle;
import org.apache.log4j.Logger;
import org.junit.Test;

import java.util.Optional;

import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertTrue;

/**
 * Created by eric on 16/1/21.
 */
public class ArticleStaticTest {
    static Logger logger = Logger.getLogger(ArticleStaticTest.class);
    WeixinSpider weixinSpider = new WeixinSpider();


    /**
     * 测试用例 :能过URL获取文章的静态信息
     * 预期结果 :返回的article各个部分都不为空
     */
    @Test
    public void TestGetArticleByUrl() {
        Optional<WxArticle> wxArticleOptional = weixinSpider.getArticleByUrl("http://mp.weixin.qq.com/s?__biz=MjM5MzY0Mjc2MA==&mid=403270809&idx=5&sn=74aa26c170964d04f0dabfe5d0a79078");
        if (wxArticleOptional.isPresent()) {
            WxArticle article = wxArticleOptional.get();
            assertTrue(article.getArticleUrl().length() > 0);
            assertTrue(article.getAuthor().length() > 0);
            assertTrue(article.getBiz().length() > 0);
            assertTrue(article.getBrief().length() > 0);
            assertTrue(article.getContent().length() > 0);
            assertTrue(article.getHeadImgUrl().length() > 0);
            assertTrue(article.getId().length() > 0);
            assertTrue(article.getNickName().length() > 0);
            assertTrue(article.getQrCode().length() > 0);
            assertTrue(article.getTitle().length() > 0);
            assertTrue(article.getPostTime() > 0);
            assertTrue(article.getCoverImgUrl().length() > 0);
        } else {
            assertTrue(false);
        }
    }

    /**
     * 测试用例 :给定一个错误的文章地址
     * 预期结果: 返回不包含文章,但不会报错
     */
    @Test
    public void TestGetArticleByUrlError() {
        Optional<WxArticle> wxArticleOptional = weixinSpider.getArticleByUrl("http://mp.weixin.qq.com/s?__biz=MjM5NzMxOTg4MA==&mid=401742595&idx=2");
        assertFalse(wxArticleOptional.isPresent());
    }
}
