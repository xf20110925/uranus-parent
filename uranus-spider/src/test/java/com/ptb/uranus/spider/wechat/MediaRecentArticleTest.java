package com.ptb.uranus.spider.wechat;

import com.ptb.uranus.spider.weixin.WeixinSpider;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.junit.Test;

import java.util.List;
import java.util.Optional;

import static junit.framework.Assert.assertFalse;
import static junit.framework.TestCase.assertTrue;

/**
 * Created by eric on 16/1/21.
 */
public class MediaRecentArticleTest {
    WeixinSpider weixinSpider = new WeixinSpider();

    /**
     * 测试用例 : 给定媒体的BIZ
     * 预期结果: 返回对应文章的URL列表不为空
     */
    @Test
    public void testRecentArticlesByBiz() {

        Optional<ImmutablePair<Long, List<String>>> recentArticlesByBiz = weixinSpider.getRecentArticlesByBiz("MzA5MTI2MDU3Nw==", 1455372759L);
        assertTrue(recentArticlesByBiz.isPresent() && recentArticlesByBiz.get().getRight().size() > 0 && recentArticlesByBiz.get().getLeft() > 0);
    }


    /**
     * 测试用例 : 给定媒体的BIZ(错误)
     * 预期结果: 返回空的URL列表
     */
    @Test
    public void testRecentAritcleByBizError() {
        Optional<ImmutablePair<Long, List<String>>> recentArticlesByBiz = weixinSpider.getRecentArticlesByBiz("MjM5NzMxOTg4MA", 1L);
        assertFalse(recentArticlesByBiz.isPresent() && recentArticlesByBiz.get().getRight().size() > 0 && recentArticlesByBiz.get().getLeft() > 0);
    }
/*
    @Test
    public void testGetHotArticlesBySougou() {
        List<String> hotArticleFromSogou = weixinSpider.getHotArticleFromSogou();
        List<String> collect = hotArticleFromSogou.stream().map(article -> {
            return RegexUtils.sub(".*__biz=([^&]*)&.*", article, 0);
        }).distinct().collect(Collectors.toList());
        assertTrue(hotArticleFromSogou.size() > 0);
    }*/

/*    *//**
     * 测试用例 : 通过微信ID查询最近文章
     * 预期结果: 返回空的URL列表
     *//*
    @Test
    public void testRecentArticleByWeixinID() {
        Optional<List<String>> recentArticlesByBiz = weixinSpider.getRecentArticlesyWeixinID("gzwcjs");
        assertTrue(recentArticlesByBiz.isPresent() && recentArticlesByBiz.get().size() > 0);
    }


    @Test
    public void testRecentAritcleByWeixinIDError() {
        Optional<List<String>> recentArticlesByBiz = weixinSpider.getRecentArticlesyWeixinID("gzwcj");
        assertFalse(recentArticlesByBiz.isPresent()  && recentArticlesByBiz.get().size() > 0);
    }*/
}
