package com.ptb.uranus.spider.weibo;

import com.ptb.uranus.spider.weibo.bean.WeiboArticle;
import com.ptb.utils.string.RegexUtils;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.junit.Test;

import java.util.List;
import java.util.Optional;

import static junit.framework.TestCase.assertTrue;

/**
 * Created by eric on 16/2/1.
 */
public class WbRecentArticleTest {
    WeiboSpider weiboSpider = new WeiboSpider();

    /**
     * Test get article static by url success.
     */
    @Test
    public void testGetRecentArticleByWeiboID() {
        Optional<ImmutablePair<Long, List<WeiboArticle>>> articleList = weiboSpider.getRecentArticlesByWeiboID("3974469906", 0L);
        assertTrue(articleList.isPresent());
        assertTrue(articleList.get().getRight().size() > 0);
        assertTrue(articleList.get().getLeft()>0);
    }

    @Test
    public void testGetRecentArticleByContainerID() {
        Optional<ImmutablePair<Long, List<WeiboArticle>>> articleList = weiboSpider.getRecentArticlesByContainerID("1076035060397568", 0l);
        assertTrue(articleList.get().getRight().size() >= 0);
        assertTrue(articleList.get().getLeft()>0);
    }

    public static void main(String[] args) {
        String sub = RegexUtils.sub("http://m.weibo.cn/([\\d]*)/.*", "http://m.weibo.cn/3974469906/DxYqWCx9", 0);
        System.out.println(sub);
    }
}
