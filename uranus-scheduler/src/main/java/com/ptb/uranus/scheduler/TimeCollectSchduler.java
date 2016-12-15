package com.ptb.uranus.scheduler;


import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * Created by eric on 16/4/22.
 */
public class TimeCollectSchduler {
	static Logger logger = LoggerFactory.getLogger(TimeCollectSchduler.class);

	ScheduledExecutorService scheduler = null;
	private PropertiesConfiguration conf;
	private Runnable crawleTask;

	public TimeCollectSchduler() {
		scheduler = Executors.newScheduledThreadPool(10);
	}

	void loadConf() throws ConfigurationException {
		conf = new PropertiesConfiguration("ptb.properties");
	}

	public void start() throws Exception {
		logger.warn("schedule start ...");
		loadConf();
		this.crawleTask = new CrawleTask();
		scheduler.scheduleAtFixedRate(crawleTask, 0, conf.getLong("uranus.scheduler.spider.interval.second", 500), TimeUnit.SECONDS);
	}


	public static void main(String[] args) throws Exception {
		TimeCollectSchduler timeCollectSchduler = new TimeCollectSchduler();
		timeCollectSchduler.start();
	}
}
