package com.ptb.uranus.server.bayoudata;

import com.ptb.gaia.bus.kafka.KafkaBus;
import com.ptb.uranus.server.send.BusSender;
import org.apache.commons.configuration.ConfigurationException;
import scala.util.Try;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * Created by xuefeng on 2016/5/23.
 */
public class Scheduler {
    private ScheduledExecutorService scheduledExecutor = null;
    private BayouWeixinSync wxSync = null;

    private Scheduler() throws ConfigurationException {
        scheduledExecutor = Executors.newScheduledThreadPool(10);
        wxSync = new BayouWeixinSync(new BusSender(new KafkaBus()));
    }

    public void schedule() {
        scheduledExecutor.scheduleAtFixedRate(() -> {
            try {
                wxSync.syncArticleDynamics();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }, 0, 1, TimeUnit.HOURS);
        scheduledExecutor.scheduleAtFixedRate(() -> {
            try {
                wxSync.syncArticleStatics();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }, 0, 1, TimeUnit.HOURS);
        scheduledExecutor.scheduleAtFixedRate(() -> {
            try {
                wxSync.syncMedias();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }, 0, 1, TimeUnit.HOURS);
    }

    public static void main(String[] args) throws ConfigurationException {
        new Scheduler().schedule();
    }
}
