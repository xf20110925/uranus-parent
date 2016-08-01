package com.ptb.uranus.server.third;

import com.ptb.gaia.bus.Bus;
import com.ptb.gaia.bus.kafka.KafkaBus;
import com.ptb.uranus.server.send.BusSender;
import com.ptb.uranus.server.send.Sender;
import com.ptb.uranus.server.third.weibo.WeiboInit;
import com.ptb.uranus.server.third.weixin.Scheduler;
import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;

/**
 * Created by watson zhang on 16/5/31.
 */
public class ThirdEntry {
    private PropertiesConfiguration conf;
    private int busWorkNum;
    private Bus bus;
    private Sender sender;
    private static Scheduler weixinScheduler;
    private static WeiboInit weiboScheduler;


    public ThirdEntry() {
        try {
            conf = new PropertiesConfiguration("ptb.properties");
        } catch (ConfigurationException e) {
            e.printStackTrace();
        }
        busWorkNum = conf.getInt("com.ptb.uranus.busWorkNum", 3);
        bus = new KafkaBus();
        sender = new BusSender(this.bus);
        bus.start(false,busWorkNum);
        weixinScheduler = new Scheduler(sender);
        weiboScheduler = new WeiboInit(sender);
    }

    public static void main(String[] args) throws ConfigurationException {
        new ThirdEntry();
        //启动微博调度器
        weiboScheduler.startWeibo();
        //启动微信调度器
//        weixinScheduler.schedule();
    }
}
