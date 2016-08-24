package com.ptb.uranus.server.third.version1.weibo;

import com.ptb.uranus.server.send.Sender;
import com.ptb.uranus.server.third.version1.weibo.task.WeiboArticleToGaiaBus;
import com.ptb.uranus.server.third.version1.weibo.task.WeiboMediaToGaiaBus;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * Created by watson zhang on 16/6/15.
 */
public class WeiboInit {
	ScheduledExecutorService executor = Executors.newScheduledThreadPool(4);
	private Sender sender;

	public WeiboInit(Sender sender) {
		this.sender = sender;
	}

	public void startWeibo() {
		executor.scheduleAtFixedRate(new WeiboArticleToGaiaBus(this.sender), 0, 100, TimeUnit.SECONDS);
		executor.scheduleAtFixedRate(new WeiboMediaToGaiaBus(this.sender), 0, 60, TimeUnit.SECONDS);
	}

}
