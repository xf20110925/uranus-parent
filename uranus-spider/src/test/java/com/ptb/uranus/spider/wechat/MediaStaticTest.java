package com.ptb.uranus.spider.wechat;

import com.ptb.uranus.spider.weixin.WeixinSpider;
import com.ptb.uranus.spider.weixin.bean.WxAccount;
import com.ptb.uranus.spider.weixin.bean.WxArticle;
import org.slf4j.Logger;
import org.junit.Test;
import org.slf4j.LoggerFactory;

import java.util.Optional;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertTrue;

/**
 * Created by eric on 16/1/21.
 */
public class MediaStaticTest {
    static Logger logger = LoggerFactory.getLogger(MediaStaticTest.class);
    WeixinSpider weixinSpider = new WeixinSpider();

    public void assertAccount(WxAccount wxAccount) {
        assertTrue(wxAccount.getBiz().length() > 0);
        assertTrue(wxAccount.getBrief().length() > 0);
        assertTrue(wxAccount.getHeadImg().length() > 0);
        assertTrue(wxAccount.getId().length() > 0);
        assertTrue(wxAccount.getNickName().length() > 0);
        assertTrue(wxAccount.getQcCode().length() > 0);
    }

    /**
     * 测试内容 .给定测试给一个正确的文章地址,得到包含微信文章的各个部分
     * <p>
     * 验证方法:返回文章的各个部分都不为空
     */
    @Test
    public void testWeixinMediaStaticByAritcleNormal() {
        Optional<WxAccount> wxAccountOptional = weixinSpider.getWeixinAccountByArticleUrl("http://mp.weixin.qq.com/s?__biz=MjA1ODMxMDQwMQ==&mid=2657176668&idx=1&sn=9deb6ae256142d09c4503d593f7272a6#rd");
        if (wxAccountOptional.isPresent()) {
            WxAccount wxAccount = wxAccountOptional.get();
            assertAccount(wxAccount);
        } else {
            assertTrue(false);
        }
    }


    /**
     * 测试内容 .给定测试给一个错误的文章,返回一个空文章对象
     * <p>
     * 验证方法:返回文章options为空
     */
    @Test
    public void testWeixinMediaStaticByArticleErrorAddress() {
        Optional<WxAccount> wxAccountOptional =
                weixinSpider.getWeixinAccountByArticleUrl("http://mp.weixin.qq.com/s?__biz=MjM5ODcwOTcyMA==&mid=401889400&idx=1");
        assertFalse("返回一个空对象", wxAccountOptional.isPresent());
    }
    /**
     * 测试内容 .给定测试给一个文章链接,返回文章原文链接
     * <p>
     * 验证方法:返回文章sourceLink不为空
     */
    @Test
    public void testWeixinSourceLink() {
        Optional<WxArticle> wxArticle =
                weixinSpider.getArticleByUrl("http://mp.weixin.qq.com/s?__biz=MjM5MjUzNzM3Mw==&mid=2651822010&idx=5&sn=29df4328f3751311d8de29bc8cf4226d&3rd=MzA3MDU4NTYzMw==&scene=6#rd");
        assertTrue(wxArticle.get().getSourceLink().length() > 0);
    }

    /**
     * 测试内容 .给定测试给一个文章链接,返回文章作者的认证信息即公司全名
     * <p>
     * 验证方法:返回文章公司名称为指定值
     */
    @Test
    public void testMediaIdentification() {
        Optional<String> identification = weixinSpider.getMediaIdentifyByArticleUrl("https://mp.weixin.qq.com/s?__biz=MTE3MzE4MTAyMQ==&mid=403427490&idx=1&sn=b5a21e4ea3f6843f4662ef4fcb83af54&uin=MjM3NjU0OTcy&key=b28b03434249256b1b7c7d7cbb74156cad263f21fc44b4c6a69c7e492e70dfe70e00e4c6f7315c67dfae473ff365c165&devicetype=android-17&version=26030b31&lang=zh_CN&nettype=WIFI&pass_ticket=twpYesgT%2BUmkO2z5rTpcl6f%2BN1inoi3hKSvnvc7wWgM%3D");
        System.out.println(identification.get());
        assertEquals(identification.get(), "千钧万博(北京)信息技术有限公司");
    }


/*    *//**
     * 测试内容 .给定测试给一个正确的微信名称,得到包含微信文章的各个部分
     * <p>
     * 验证方法:返回文章的各个部分都不为空
     *//*
    @Test
    public void testWeixinMediaStaticByWeixinIDNormal() throws InterruptedException {
        int i = 0;
        while (true) {
            Optional<WxAccount> wxAccountOptional = weixinSpider.getWeixinAccountByWeixinID("design_case");
            if (wxAccountOptional.isPresent()) {
                WxAccount wxAccount = wxAccountOptional.get();
                assertAccount(wxAccount);
                System.out.println(String.format("-----------------------------------------%d", ++i));
            } else {
                assertTrue(false);
            }
            Thread.sleep(5000);
        }
    }*/
 /*
    *//**
     * 测试内容 .给定测试给一个正确的微信名称,得到包含微信文章的各个部分
     * <p>
     * 验证方法:返回文章的各个部分都不为空
     *//*
    @Test
    public void testWeixinMediaStaticByWeixinIDNotFound() {
        Optional<WxAccount> wxAccountOptional = weixinSpider.getWeixinAccountByWeixinID("design_case12312312312");
        assertFalse("返回一个空对象", wxAccountOptional.isPresent());
    }*/


}
