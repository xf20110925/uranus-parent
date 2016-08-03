package com.ptb.uranus.server.third.v1.weixin;

import com.ptb.uranus.server.send.Sender;
import org.apache.commons.configuration.ConfigurationException;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by xuefeng on 2016/5/23.
 */
public class Scheduler {
    private ExecutorService executor = null;
    private BayouWeixinSync wxSync = null;

     public Scheduler(Sender sender) {
        executor = Executors.newFixedThreadPool(5);
       /* Bus bus = new KafkaBus();
        bus.start(false, 3);
        wxSync = new BayouWeixinSync(new BusSender(bus));*/

        wxSync = new BayouWeixinSync(sender);
    }

    public void schedule() {
       executor.execute(() -> {
            while (true) {
                try {
                    wxSync.syncArticleDynamics();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                sleep(1000);
            }
        });
        executor.execute(() -> {
            while (true) {
                try {
                    wxSync.syncArticleStatics();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                sleep(1000);
            }
        });
        executor.execute(() -> {
            while (true) {
                try {
                    wxSync.syncMedias();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                sleep(1000);
            }
        });
    }

    public void sleep(long seepTime) {
        try {
            Thread.sleep(seepTime);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) throws ConfigurationException {
//        new Scheduler().schedule();
    }
}
