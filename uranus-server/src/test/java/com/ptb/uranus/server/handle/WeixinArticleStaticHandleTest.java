package com.ptb.uranus.server.handle;

import com.ptb.gaia.bus.Bus;
import com.ptb.gaia.bus.kafka.KafkaBus;
import com.ptb.gaia.bus.message.Message;
import com.ptb.uranus.common.entity.CollectCondition;
import com.ptb.uranus.server.send.BusSender;
import com.ptb.uranus.server.send.Sender;
import com.ptb.uranus.server.send.entity.article.WeixinArticleStatic;
import org.apache.commons.configuration.ConfigurationException;
import org.junit.Test;


/**
 * Created by watson zhang on 16/4/26.
 */
public class WeixinArticleStaticHandleTest {

    @Test
    public void testHandle() throws InterruptedException, ConfigurationException {
        CollectCondition collectCondition = new CollectCondition();
        collectCondition.setConditon("http://mp.weixin.qq.com/s?__biz=MjM5NTE2MTY5NA==&mid=2651602402&idx=2&sn=2ab95f3ccd8e16c708041518aec72cbf&scene=0#wechat_redirect");
        Message<CollectCondition> message = new Message<CollectCondition>("uranus-server", "weixin_media_static", 1, "1.0.0", collectCondition);
        Bus bus = new KafkaBus();
        Sender sender = new BusSender(bus);
        bus.start(false, 0);
        WeixinArticleStaticHandle weixinArticleStaticHandle = new WeixinArticleStaticHandle(sender);
        weixinArticleStaticHandle.handle(bus, message);
        Thread.sleep(3000);
    }

}