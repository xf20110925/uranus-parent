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
 * Created by watson zhang on 16/4/27.
 */
public class WeiboArticleStaticHandleTest {
    @Test
    public void testHandle() throws InterruptedException, ConfigurationException {
        CollectCondition collectCondition = new CollectCondition();
        collectCondition.setConditon("http://weibo.com/1192329374/DpM3ulroD?from=page_1003061192329374_profile&wvr=6&mod=weibotime&type=comment#_rnd1461824704335");
        Message<CollectCondition> message = new Message<>("uranus-server", "article_basic_info", 2, "1.0.0", collectCondition);
        Bus bus = new KafkaBus();
        Sender sender = new BusSender(bus);
        bus.start(false, 0);
        WeiboArticleStaticHandle weiboArticleStaticHandle = new WeiboArticleStaticHandle(sender);
        weiboArticleStaticHandle.handle(bus, message);
        Thread.sleep(3000);
    }
}