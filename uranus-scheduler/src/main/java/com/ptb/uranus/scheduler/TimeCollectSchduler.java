package com.ptb.uranus.scheduler;


import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import sun.misc.Signal;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by eric on 16/4/22.
 */
public class TimeCollectSchduler {
    static Logger logger = LoggerFactory.getLogger(TimeCollectSchduler.class);

    Timer timer = null;
    boolean isDone = false;
    private PropertiesConfiguration conf;
    private TimerTask crawleTask;
    private UpdateCommonArtcileListTask updateCommonArticleEntryTask;

    public TimeCollectSchduler() {
        timer = new Timer("uranus-scheduler", true);
    }

    void loadConf() throws ConfigurationException {
        conf = new PropertiesConfiguration("uranus.properties");
    }

    public void start(boolean isDaemon) throws Exception {
        loadConf();

        this.crawleTask = new CrawleTask();
        this.updateCommonArticleEntryTask = new UpdateCommonArtcileListTask();


        //timer.schedule(updateCommonArticleEntryTask, 1000, conf.getLong("uranus.scheduler.crawle.entry.reload.interval.second", 36000) * 1000);
        timer.schedule(crawleTask, 1000, conf.getLong("uranus.scheduler.spider.interval.second", 500) * 1000);

        Signal sig = new Signal("INT");
        Signal.handle(sig, signal -> {
            stop();
            isDone = true;
        });

        if (isDaemon) {
            while (!isDone) {
                Thread.sleep(2000);
            }
        }
    }

    public void stop() {
        this.isDone = true;
        timer.cancel();
        crawleTask.cancel();
        logger.info("程序正常退出");
    }


    public static void main(String[] args) throws Exception {
        TimeCollectSchduler timeCollectSchduler = new TimeCollectSchduler();
        timeCollectSchduler.start(true);
    }
}
