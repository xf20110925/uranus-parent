package com.ptb.uranus.server.bayou;

import com.ptb.uranus.server.third.version2.SchedulerUpdater;

import org.junit.Test;

/**
 * @DESC:
 * @VERSION:
 * @author: xuefeng
 * @Date: 2016/12/21
 * @Time: 16:38
 */
public class SchedulerUpdaterTest {
  	@Test
  	public void test() throws InterruptedException {
	  SchedulerUpdater scheduleUpdater = new SchedulerUpdater();
	  scheduleUpdater.add(new SchedulerUpdater.NewArticleScheduler("MzI3ODUzODIwOQ==", System.currentTimeMillis()));
	  scheduleUpdater.add(new SchedulerUpdater.NewArticleScheduler("MzI0MjY5MjA2OQ==", System.currentTimeMillis()));
	  scheduleUpdater.add(new SchedulerUpdater.NewArticleScheduler("MjM5NDQ0ODYwMA==", 0));
	  scheduleUpdater.add(new SchedulerUpdater.NewArticleScheduler("MjM5NDQ0ODYwMA==", System.currentTimeMillis()));
	  new Thread(() -> {
		try {
		  scheduleUpdater.handle();
		} catch (InterruptedException e) {
		  e.printStackTrace();
		}
	  }).start();
	  Thread.sleep(20000);
	}
}
