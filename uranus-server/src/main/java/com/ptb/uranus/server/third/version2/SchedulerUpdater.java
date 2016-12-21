package com.ptb.uranus.server.third.version2;

import com.ptb.uranus.schedule.service.WeixinScheduleService;

import org.apache.commons.configuration.ConfigurationException;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * @DESC:
 * @VERSION:
 * @author: xuefeng
 * @Date: 2016/12/20
 * @Time: 18:07
 */
public class SchedulerUpdater {
  public static class NewArticleScheduler{
    private String pmid;
	private long postTime;

	public NewArticleScheduler(String pmid, long postTime) {
	  this.pmid = pmid;
	  this.postTime = postTime;
	}

	public String getPmid() {
	  return pmid;
	}

	public void setPmid(String pmid) {
	  this.pmid = pmid;
	}

	public long getPostTime() {
	  return postTime;
	}

	public void setPostTime(long postTime) {
	  this.postTime = postTime;
	}

	@Override
	public boolean equals(Object o) {
	  if (this == o) return true;
	  if (o == null || getClass() != o.getClass()) return false;
	  NewArticleScheduler scheduler = (NewArticleScheduler) o;
	  return pmid.equals(scheduler.getPmid()) ? true : false;
	}

	@Override
	public int hashCode() {
	  int result = pmid.hashCode();
	  result = 31 * result + (int) (postTime ^ (postTime >>> 32));
	  return result;
	}
  }

  private static BlockingQueue<NewArticleScheduler> bq = new LinkedBlockingQueue<NewArticleScheduler>() {
	@Override
	public boolean offer(NewArticleScheduler scheduler) {
	  if (contains(scheduler)){
	    remove(scheduler);
	  }
	  return super.offer(scheduler);
	}
  };
  private WeixinScheduleService wxScheduleService;

  public SchedulerUpdater(){
	try {
	  wxScheduleService = new WeixinScheduleService();
	} catch (ConfigurationException e) {
	  e.printStackTrace();
	}
  }

  public void add(NewArticleScheduler scheduler){
    bq.offer(scheduler);
  }

  public void handle() throws InterruptedException {
	NewArticleScheduler scheduler = bq.take();
	wxScheduleService.updateWeixinMediaCondition(scheduler.getPmid(), scheduler.getPostTime());
  }
}
