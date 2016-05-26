package com.ptb.uranus.spider.weibo;

import com.ptb.uranus.spider.weibo.bean.WeiboAccount;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.Optional;

import static junit.framework.TestCase.assertTrue;

/**
 * Created by eric on 16/2/1.
 */
public class WbMediaTest {
    WeiboSpider weiboSpider;
    @Before
    public void Before() {
        weiboSpider = new WeiboSpider();
    }

    @Test
    public void testGetMediaStaticByMediaID() {
        Optional<WeiboAccount> weiboAccountOptional = weiboSpider.getWeiboAccountByWeiboID("2050067882");
        assertTrue(weiboAccountOptional.isPresent());
        WeiboAccount weiboAccount = weiboAccountOptional.get();
        assertTrue(weiboAccount.getContainerID().length() > 0);
        assertTrue(weiboAccount.getDesc().length() > 0);
        assertTrue(weiboAccount.getHeadImg().length() > 0);
        assertTrue(weiboAccount.getNickName().length() > 0);
        assertTrue(weiboAccount.getTitle().length() > 0);
        assertTrue(weiboAccount.getWeiboID().length() > 0);
        assertTrue(weiboAccount.getAttNum() > 0);
        assertTrue(weiboAccount.getFansNum() > 0);
        assertTrue(weiboAccount.getMblogNum() > 0);
    }

    @Test
    public void testGetMediaStaticByHomePage() {
        Optional<WeiboAccount> weiboAccountOptional = weiboSpider.getWeiboAccountByHomePage("http://weibo.com/dengchao");
        assertTrue(weiboAccountOptional.isPresent());
        WeiboAccount weiboAccount = weiboAccountOptional.get();
        assertTrue(weiboAccount.getContainerID().length() > 0);
        assertTrue(weiboAccount.getDesc().length() >= 0);
        assertTrue(weiboAccount.getHeadImg().length() > 0);
        assertTrue(weiboAccount.getNickName().length() > 0);
        assertTrue(weiboAccount.getTitle().length() > 0);
        assertTrue(weiboAccount.getWeiboID().length() > 0);
        assertTrue(weiboAccount.getAttNum() > 0);
        assertTrue(weiboAccount.getFansNum() > 0);
        assertTrue(weiboAccount.getMblogNum() > 0);
    }

    @Test
    public void testGetMediaStatcByArticleUrl(){
        Optional<WeiboAccount> weiboAccountOptional = weiboSpider.getWeiboAccountByArticleUrl("http://weibo.com/3455918702/DsUxgf5zC?type=comment");
        assertTrue(weiboAccountOptional.isPresent());
        WeiboAccount weiboAccount = weiboAccountOptional.get();
        assertTrue(weiboAccount.getContainerID().length() > 0);
        assertTrue(weiboAccount.getDesc().length() >= 0);
        assertTrue(weiboAccount.getHeadImg().length() > 0);
        assertTrue(weiboAccount.getNickName().length() > 0);
        assertTrue(weiboAccount.getTitle().length() > 0);
        assertTrue(weiboAccount.getWeiboID().length() > 0);
        assertTrue(weiboAccount.getAttNum() > 0);
        assertTrue(weiboAccount.getFansNum() > 0);
        assertTrue(weiboAccount.getMblogNum() > 0);
    }

    @After
    public void after() {
        weiboSpider.close();
    }
}
