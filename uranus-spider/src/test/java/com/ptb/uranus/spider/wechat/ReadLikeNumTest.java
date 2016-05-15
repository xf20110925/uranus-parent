package com.ptb.uranus.spider.wechat;

import com.ptb.uranus.spider.weixin.WeixinSpider;
import com.ptb.uranus.spider.weixin.bean.ReadLikeNum;
import junit.framework.TestCase;
import org.apache.log4j.Logger;
import org.junit.Test;

import java.util.Optional;

/**
 * Created by eric on 16/1/22.
 */
public class ReadLikeNumTest {
    static Logger logger = Logger.getLogger(MediaStaticTest.class);
    WeixinSpider weixinSpider = new WeixinSpider();


    @Test
    public void readLikeNumTest() {
        Optional<ReadLikeNum> readLikeNumOptional = weixinSpider.getArticleReadLikeNumByUrl("http://mp.weixin.qq.com/s?__biz=MjM5NTE2MTY5NA==&mid=2651602354&idx=3&sn=5e5a9a1c685cb87acd028b770b26ee3e&scene=0#rd", 60);
        TestCase.assertTrue(readLikeNumOptional.isPresent());
    }

    @Test
    public void sougouReadLikeNumTest() {
        Optional<ReadLikeNum> readLikeNumOptional = weixinSpider.getArticleReadLikeNumByUrl("http://mp.weixin.qq.com/s?__biz=MzA5OTA0NDIyMQ==&mid=2653882203&idx=3&sn=bb82d34af100b447d34f7c66864eaa60#rd", 60);
        System.out.println(readLikeNumOptional.get().getReadNum() + "\t" + readLikeNumOptional.get().getLikeNum());
        TestCase.assertTrue(readLikeNumOptional.isPresent());
    }

    @Test
    public void readLikeNumTestError() {
        Optional<ReadLikeNum> readLikeNumOptional = weixinSpider.getArticleReadLikeNumByUrl("http://mp.weixin.qq.com/s?__biz=MjM5MjEzNzYzOA==&mid=402736&idx=4&sn=9665c91e6f40", 30);
        TestCase.assertFalse(readLikeNumOptional.isPresent());
    }

}
