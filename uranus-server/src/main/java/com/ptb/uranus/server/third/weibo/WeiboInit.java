package com.ptb.uranus.server.third.weibo;

import com.ptb.uranus.server.ThirdEntry;
import com.ptb.uranus.server.send.Sender;
import com.ptb.uranus.server.third.weibo.task.WeiboArticleToGaiaBus;
import com.ptb.uranus.server.third.weibo.task.WeiboMediaToGaiaBus;
import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by watson zhang on 16/6/15.
 */
public class WeiboInit {
    ExecutorService executor = Executors.newFixedThreadPool(4);
    private Sender sender;

    public WeiboInit(Sender sender) {
        this.sender = sender;
    }

    public void startWeibo() {
        executor.execute(new WeiboArticleToGaiaBus(this.sender));
        executor.execute(new WeiboMediaToGaiaBus(this.sender));
    }

}
