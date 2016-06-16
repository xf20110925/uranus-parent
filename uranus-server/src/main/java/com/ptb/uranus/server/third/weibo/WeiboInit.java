package com.ptb.uranus.server.third.weibo;

import com.ptb.uranus.server.ThirdEntry;
import com.ptb.uranus.server.send.Sender;
import com.ptb.uranus.server.third.weibo.task.WeiboArticleHandle;
import com.ptb.uranus.server.third.weibo.task.WeiboMediaHandle;
import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by watson zhang on 16/6/15.
 */
public class WeiboInit {
    private static Logger logger = LoggerFactory.getLogger(ThirdEntry.class);
    private PropertiesConfiguration conf;
    private Sender sender;


    public WeiboInit(Sender sender){
        try {
            conf = new PropertiesConfiguration("uranus.properties");
        } catch (ConfigurationException e) {
            e.printStackTrace();
        }
        this.sender = sender;
    }

    public void startWeibo(){
        try {
            new Thread((Runnable) new WeiboArticleHandle(this.sender)).start();
            new Thread((Runnable) new WeiboMediaHandle(this.sender)).start();
        } catch (ConfigurationException e) {
            e.printStackTrace();
        }
    }
}
