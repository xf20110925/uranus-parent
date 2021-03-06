package com.ptb.uranus.server.third.version2;

import com.ptb.gaia.bus.Bus;
import com.ptb.gaia.bus.kafka.KafkaBus;
import com.ptb.uranus.server.send.BusSender;
import com.ptb.uranus.server.send.Sender;
import com.ptb.uranus.server.third.version2.article.WeiboArticleHandle;
import com.ptb.uranus.server.third.version2.article.WeixinArticleDynamicHandle;
import com.ptb.uranus.server.third.version2.article.WeixinArticleStaticHandle;
import com.ptb.uranus.server.third.version2.media.WeiboMediaHandle;
import com.ptb.uranus.server.third.version2.media.WeixinMediaHandle;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ThirdEntryV2 {
	private Bus bus;
	private Sender sender;
	private ExecutorService executor;
  	private SchedulerUpdater schedulerUpdater;


	public ThirdEntryV2() {
		bus = new KafkaBus();
		sender = new BusSender(this.bus);
		bus.start(false, 5);
		executor = Executors.newFixedThreadPool(10);
	  	schedulerUpdater = new SchedulerUpdater();
	}

	public void schedule() {
		WeiboMediaHandle weiboMediaHandle = new WeiboMediaHandle(sender);
		WeiboArticleHandle weiboArticleHandle = new WeiboArticleHandle(sender, schedulerUpdater);
		WeixinMediaHandle weixinMediaHandle = new WeixinMediaHandle(sender);
		WeixinArticleStaticHandle weixinArticleStaticHandle = new WeixinArticleStaticHandle(sender, schedulerUpdater);
		WeixinArticleDynamicHandle weixinArticleDynamicHandle = new WeixinArticleDynamicHandle(sender);

		executor.execute(adapter(weiboMediaHandle));
		executor.execute(adapter(weiboArticleHandle));
		executor.execute(adapter(weixinMediaHandle));
		executor.execute(adapter(weixinArticleStaticHandle));
		executor.execute(adapter(weixinArticleDynamicHandle));
	  	executor.execute(() -> {
		  try {
			schedulerUpdater.handle();
		  } catch (InterruptedException e) {
			e.printStackTrace();
		  }
		});
	}


	private Runnable adapter(DataHandle dataHandle) {
		return () -> {
			while (true) {
				try {
					dataHandle.handle();
				} catch (Exception e) {
					e.printStackTrace();
				}
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		};
	}

	public static void main(String[] args) {
		ThirdEntryV2 third = new ThirdEntryV2();
		third.schedule();
	}
}
