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
public class WeiboArticleDynamicHandleTest {

    @Test
    public void testHandle() throws InterruptedException, ConfigurationException {
        CollectCondition collectCondition = new CollectCondition();
        collectCondition.setConditon("http://www.weibo.com/2165313080/Dk4wA2Ae0");
        Message<CollectCondition> message = new Message<CollectCondition>();
        message.setBody(collectCondition);
        Bus bus = new KafkaBus();
        Sender sender = new BusSender(bus);
        bus.start(false, 0);
        WeiboArticleDynamicHandle weiboArticleDynamicHandle = new WeiboArticleDynamicHandle(sender);
        weiboArticleDynamicHandle.handle(bus, message);
        Thread.sleep(3000);
    }

}