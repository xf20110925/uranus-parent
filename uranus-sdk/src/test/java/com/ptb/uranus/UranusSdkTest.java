package com.ptb.uranus;

import com.ptb.uranus.common.entity.CollectType;
import com.ptb.uranus.schedule.model.Priority;
import com.ptb.uranus.schedule.trigger.JustOneTrigger;
import com.ptb.uranus.schedule.trigger.PeriodicTrigger;
import com.ptb.uranus.sdk.UranusSdk;
import org.junit.Test;

import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static org.junit.Assert.assertTrue;

/**
 * Created by eric on 16/4/21.
 */
public class UranusSdkTest {
    @Test
    public void formatWeiboTest() {
        UranusSdk.i().collect(
                "http://mp.weixin.qq.com/s?__biz=MjM5NzI3NDg4MA==&mid=2658464991&idx=1&sn=5af6511291cacfe81e837e60522d7bce&scene=4#wechat_redirect",
                CollectType.C_WX_A_S,
                new JustOneTrigger(new Date().getTime()), Priority.L1);
    }


    @Test
    public void getweixinArticleUrlByIdTest() {
        assertTrue(UranusSdk.i().collect(
                "zhanhao668",
                CollectType.C_WX_M_S,
                new JustOneTrigger(new Date().getTime()), Priority.L1) != null);
    }

    @Test
    public void addBatchCollect() {
        List<String> list = new LinkedList<String>();
        list.add(
                "https://mp.weixin.qq.com/s?__biz=MTE3MzE4MTAyMQ==&mid=403427490&idx=1&sn=b5a21e4ea3f6843f4662ef4fcb83af54&uin=MjM3NjU0OTcy&key=b28b03434249256b1b7c7d7cbb74156cad263f21fc44b4c6a69c7e492e70dfe70e00e4c6f7315c67dfae473ff365c165&devicetype=android-17&version=26030b31&lang=zh_CN&nettype=WIFI&pass_ticket=twpYesgT%2BUmkO2z5rTpcl6f%2BN1inoi3hKSvnvc7wWgM%3D"
        );
        assertTrue(UranusSdk.i().collect(
                list,
                CollectType.C_WX_A_D,
                new JustOneTrigger(new Date().getTime()), Priority.L1) != null);
    }


    @Test
    public void formatUrlTest() {
        assertTrue(UranusSdk.i().urlFormat("123").equals("123"));
    }

    @Test
    public void collectTest() {
        assertTrue(UranusSdk.i().collect("http://www.baidu.com", CollectType.C_WX_M_D,
                new PeriodicTrigger(10, TimeUnit.DAYS, new Date(), 3), Priority.L1) != null);
    }

    public void testJuesONE() {
        assertTrue(UranusSdk.i().collect("http://weibo.cn/3974469906/D8pNEnAWG",
                CollectType.C_WB_M_S,
                new JustOneTrigger(new Date().getTime()), Priority.L1) != null);
    }

    @Test
    public void collectWeixinMedia() {
        assertTrue(UranusSdk.i().collect(
                "rmrbwx"
                , CollectType.C_WX_M_S,
                new JustOneTrigger(new Date().getTime()), Priority.L1) != null);
    }

    @Test
    public void formatWeixinSogouUrl() {
        assertTrue(UranusSdk.i().
                urlFormat(
                        "https://mp.weixin.qq.com/s?__biz=MTE3MzE4MTAyMQ==&mid=403427490&idx=1&sn=b5a21e4ea3f6843f4662ef4fcb83af54&uin=MjM3NjU0OTcy&key=b28b03434249256b1b7c7d7cbb74156cad263f21fc44b4c6a69c7e492e70dfe70e00e4c6f7315c67dfae473ff365c165&devicetype=android-17&version=26030b31&lang=zh_CN&nettype=WIFI&pass_ticket=twpYesgT%2BUmkO2z5rTpcl6f%2BN1inoi3hKSvnvc7wWgM%3D"
                ).
                contains("biz="));
    }

    @Test
    public void formatWeiboMobile1Url() {
        assertTrue(UranusSdk.i().
                urlFormat("http://weibo.cn/3974469906/D8pNEnAWG").equals("http://m.weibo.cn/3974469906/D8pNEnAWG"));
    }

    @Test
    public void formatWeiboMobile2Url() {
        assertTrue(UranusSdk.i().
                urlFormat("http://www.weibo.cn/3974469906/D8pNEnAWG").equals("http://m.weibo.cn/3974469906/D8pNEnAWG"));
    }

    @Test
    public void formatWeiboMobile3Url() {
        assertTrue(UranusSdk.i().
                urlFormat("http://www.weibo.com/3974469906/D8pNEnAWG").equals("http://m.weibo.cn/3974469906/D8pNEnAWG"));

        assertTrue(UranusSdk.i().
                urlFormat("http://weibo.com/3974469906/D8pNEnAWG").equals("http://m.weibo.cn/3974469906/D8pNEnAWG"));
    }
}
