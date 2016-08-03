package com.ptb.uranus.server.third.v1.weibo.task;

import com.ptb.uranus.server.send.Sender;
import com.ptb.uranus.server.third.entity.FreshData;
import com.ptb.uranus.server.third.entity.MysqlClient;
import com.ptb.uranus.server.third.util.ConvertUtils;
import com.ptb.uranus.server.third.util.IdRecordUtil;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * Created by watson zhang on 16/5/31.
 */
public class WeiboArticleToGaiaBus implements Runnable {
	private static Logger logger = LoggerFactory.getLogger(WeiboMediaHandle.class);
	Sender sender;

	public WeiboArticleToGaiaBus(Sender sender) {
		this.sender = sender;
	}

	@Override
	public void run() {
		try {
			long startId = IdRecordUtil.getIdRecord().get().getWbArticleId();
			String sql = "select * from fresh_data where id > ?  LIMIT ?";
			List<FreshData> freshDatas = MysqlClient.instance.query(sql, FreshData.class, Long.valueOf(startId),Long.valueOf(500));
			if (freshDatas != null && !freshDatas.isEmpty()) {
				for (FreshData freshData : freshDatas) {
					sender.sendArticleStatic(ConvertUtils.weiboArticleStaticConvert(freshData));
					sender.sendArticleDynamic(ConvertUtils.weiboArticleDynamicConvert(freshData));
					//记录下次查询id的起始值
					startId = startId < freshData.getId() ? freshData.getId() : startId;
				}
			}
			IdRecordUtil.syncWbArticleId(startId);
		} catch (Exception e) {
			logger.error(e.getMessage());
			e.printStackTrace();
		}
	}
}
