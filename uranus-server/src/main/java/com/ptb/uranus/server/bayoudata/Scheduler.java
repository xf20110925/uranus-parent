package com.ptb.uranus.server.bayoudata;

import com.ptb.gaia.bus.kafka.KafkaBus;
import com.ptb.uranus.server.send.BusSender;
import org.apache.commons.configuration.ConfigurationException;

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
        scheduledExecutor.scheduleAtFixedRate(() -> wxSync.syncArticleDynamics(), 0, 5, TimeUnit.HOURS);
        scheduledExecutor.scheduleAtFixedRate(() -> wxSync.syncArticleStatics(), 0, 5, TimeUnit.HOURS);
        scheduledExecutor.scheduleAtFixedRate(() -> wxSync.syncMedias(), 0, 5, TimeUnit.HOURS);
    }

    public static void main(String[] args) throws ConfigurationException {
        new Scheduler().schedule();
    }
}
