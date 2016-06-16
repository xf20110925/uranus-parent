package com.ptb.uranus.server;

import com.ptb.gaia.bus.Bus;
import com.ptb.gaia.bus.kafka.KafkaBus;
import com.ptb.uranus.server.send.BusSender;
import com.ptb.uranus.server.send.Sender;
import com.ptb.uranus.server.third.weibo.WeiboInit;
import com.ptb.uranus.server.third.weixin.Scheduler;
import org.apache.commons.configuration.ConfigurationException;

/**
 * Created by watson zhang on 16/5/31.
 */
public class ThirdEntry {
    private Bus bus;
    private Sender sender;
    private static Scheduler weixinScheduler;
    private static WeiboInit weiboScheduler;


    public ThirdEntry() {
        bus = new KafkaBus();
        sender = new BusSender(this.bus);
        bus.start(false,3);
        weixinScheduler = new Scheduler(sender);
        weiboScheduler = new WeiboInit(sender);
    }

    public static void main(String[] args) throws ConfigurationException {
        //启动微博调度器
        weiboScheduler.startWeibo();
        //启动微信调度器
        weixinScheduler.schedule();
    }
}
