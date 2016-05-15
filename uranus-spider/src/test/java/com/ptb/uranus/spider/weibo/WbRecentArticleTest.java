package com.ptb.uranus.spider.weibo;

import com.ptb.uranus.spider.weibo.WeiboSpider;
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
        Optional<ImmutablePair<Long, List<String>>> articleList = weiboSpider.getRecentArticlesByWeiboID("3974469906", 0L);
        assertTrue(articleList.isPresent());
        assertTrue(articleList.get().getRight().size() > 0);
        assertTrue(articleList.get().getLeft()>0);
    }

    @Test
    public void testGetRecentArticleByContainerID() {
        Optional<ImmutablePair<Long, List<String>>> articleList = weiboSpider.getRecentArticlesByContainerID("1005053974469906", 14588101030l);
        assertTrue(articleList.get().getRight().size() >= 0);
        assertTrue(articleList.get().getLeft()>0);
    }
}
