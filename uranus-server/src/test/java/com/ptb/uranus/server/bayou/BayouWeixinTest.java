package com.ptb.uranus.server.bayou;

import com.ptb.gaia.bus.kafka.KafkaBus;
import com.ptb.uranus.server.bayoudata.BayouWeixinSync;
import com.ptb.uranus.server.send.BusSender;
import org.junit.Test;

/**
 * Created by xuefeng on 2016/5/24.
 */
public class BayouWeixinTest {
    BayouWeixinSync bayouWeixinSync = new BayouWeixinSync(new BusSender(new KafkaBus()));

    @Test
    public void syncMediaTest() {
        bayouWeixinSync.syncMedias();
    }
}
