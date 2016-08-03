package com.ptb.uranus.server.third.v1.weibo.task;

import com.ptb.uranus.server.send.Sender;
import com.ptb.uranus.server.send.entity.media.BasicMediaDynamic;
import com.ptb.uranus.server.send.entity.media.WeiboMediaStatic;
import com.ptb.uranus.server.third.entity.MysqlClient;
import com.ptb.uranus.server.third.entity.UserProfile;
import com.ptb.uranus.server.third.util.ConvertUtils;
import com.ptb.uranus.server.third.util.IdRecordUtil;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * Created by watson zhang on 16/5/20.
 */
public class WeiboMediaToGaiaBus implements Runnable {

	private static Logger logger = LoggerFactory.getLogger(WeiboMediaToGaiaBus.class);
	private Sender sender = null;

	public WeiboMediaToGaiaBus(Sender sender) {
		this.sender = sender;
	}

	@Override
	public void run() {
		Long startId = IdRecordUtil.getIdRecord().get().getWbMediaId();
		try {
			String sql = "select * from user_profile where id > ?  LIMIT ?";
			List<UserProfile> userProfiles = MysqlClient.instance.query(sql, UserProfile.class, Long.valueOf(startId), Long.valueOf(500));
			for (UserProfile userProfile : userProfiles) {
				WeiboMediaStatic weiboMediaStatic = ConvertUtils.weiboMediaStaticConvert(userProfile);
				BasicMediaDynamic basicMediaDynamic = ConvertUtils.weiboMediaDynamicConvert(userProfile);
				this.sender.sendMediaStatic(weiboMediaStatic);
				this.sender.sendMediaDynamic(basicMediaDynamic);
				startId = startId < userProfile.getId() ? userProfile.getId() : startId;
			}
			IdRecordUtil.syncWbMediaId(startId);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
	}
}
