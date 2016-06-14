package com.ptb.uranus;

import com.ptb.uranus.common.entity.CollectCondition;
import com.ptb.uranus.common.entity.CollectType;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * Created by eric on 16/4/23.
 */
public class UranusClientTest {
    UranusClient uranusClient;

    @Before
    public void before() {
        uranusClient = new UranusClient("test-uranus-client", 1);
    }

    @Test
    public void sendCrawleCommand() throws Exception {
        final boolean[] isOk = {false};


  /*      uranusClient.bus.addRecvMessageListener(new MessageListener() {
            @Override
            public void receive(Bus bus, Message message) {
                isOk[0] = true;
            }

            @Override
            public boolean match(Message message) {
                return true;
            }
        });*/
        uranusClient.sendCrawleCommand(new CollectCondition(CollectType.C_WX_M_S, "MzA3NDgyNTYyMQ=="));
        int i = 0;
        while (i++ < 10 && isOk[0] == false) {
            Thread.sleep(1000);
        }
    }



    @After
    public void deinit() {
        if (uranusClient != null) {
            uranusClient.close();
            uranusClient = null;
        }
    }
}