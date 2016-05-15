package com.ptb.uranus.server.handle;

import com.ptb.gaia.bus.Bus;
import com.ptb.gaia.bus.kafka.KafkaBus;
import com.ptb.gaia.bus.message.Message;
import com.ptb.uranus.common.entity.CollectCondition;
import com.ptb.uranus.server.send.BusSender;
import com.ptb.uranus.server.send.Sender;
import org.apache.commons.configuration.ConfigurationException;
import org.junit.Test;

/**
 * Created by watson zhang on 16/4/26.
 */
public class WeiboMediaStaticHandleTest {

    @Test
    public void testHandle() throws InterruptedException, ConfigurationException {
        CollectCondition collectCondition = new CollectCondition();
        collectCondition.setConditon("http://m.weibo.cn/u/5659237191");
        Message<CollectCondition> message = new Message<CollectCondition>("uranus-server", "weixin_media_static", 2, "1.0.0", collectCondition);
        Bus bus = new KafkaBus();
        Sender sender = new BusSender(bus);
        bus.start(false, 0);
        WeiboMediaStaticHandle weiboMediaStaticHandle = new WeiboMediaStaticHandle(sender);
        weiboMediaStaticHandle.handle(bus, message);
        Thread.sleep(3000);
    }
}