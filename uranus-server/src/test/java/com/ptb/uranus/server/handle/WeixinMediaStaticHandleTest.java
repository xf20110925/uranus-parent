package com.ptb.uranus.server.handle;

import com.ptb.gaia.bus.Bus;
import com.ptb.gaia.bus.kafka.KafkaBus;
import com.ptb.gaia.bus.message.Message;
import com.ptb.uranus.common.entity.CollectCondition;
import com.ptb.uranus.server.send.BusSender;
import com.ptb.uranus.server.send.Sender;
import org.junit.Test;

/**
 * Created by watson zhang on 16/4/25.
 */
public class WeixinMediaStaticHandleTest {

    @Test
    public void testHandle() throws Exception {
        CollectCondition collectCondition = new CollectCondition();
        collectCondition.setConditon("https://mp.weixin.qq.com/s?__biz=MTE3MzE4MTAyMQ==&mid=403427490&idx=1&sn=b5a21e4ea3f6843f4662ef4fcb83af54&uin=MjM3NjU0OTcy&key=b28b03434249256b1b7c7d7cbb74156cad263f21fc44b4c6a69c7e492e70dfe70e00e4c6f7315c67dfae473ff365c165&devicetype=android-17&version=26030b31&lang=zh_CN&nettype=WIFI&pass_ticket=twpYesgT%2BUmkO2z5rTpcl6f%2BN1inoi3hKSvnvc7wWgM%3D");
        Message<CollectCondition> message = new Message("uranus-server", "weixin_media_static", 1, "1.0.0", collectCondition);
        Bus bus = new KafkaBus();
        Sender sender = new BusSender(bus);
        WeixinMediaStaticHandle weixinMediaStaticHandle = new WeixinMediaStaticHandle(sender);
        bus.start(false, 0);
        weixinMediaStaticHandle.handle(bus, message);
        Thread.sleep(3000);

    }
}