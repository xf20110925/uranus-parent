package com.ptb.uranus.server.third.version2.media;

import com.alibaba.fastjson.JSON;
import com.jayway.jsonpath.JsonPath;
import com.ptb.uranus.server.send.Sender;
import com.ptb.uranus.server.third.entity.IdRecord;
import com.ptb.uranus.server.third.entity.UserProfile;
import com.ptb.uranus.server.third.util.ConvertUtils;
import com.ptb.uranus.server.third.util.IdRecordUtil;
import com.ptb.uranus.server.third.version2.DataHandle;
import com.ptb.uranus.server.third.version2.ReqUrlEnum;
import com.ptb.uranus.spider.common.utils.HttpUtil;

import org.apache.commons.configuration.Configuration;
import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * @DESC:
 * @VERSION:
 * @author: xuefeng
 * @Date: 2016/8/2
 * @Time: 15:10
 */
public class WeiboMediaHandle implements DataHandle {
	private Sender sender;
	private int filterFansNum = 10000;

	public WeiboMediaHandle(Sender sender) {
		this.sender = sender;
		try {
			Configuration conf = new PropertiesConfiguration("ptb.properties");
			filterFansNum= conf.getInt("uranus.bayou.wb.media.filter.fansnum");
		} catch (ConfigurationException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void handleBusEntities(String dataUrl) {
		String pageSource = HttpUtil.getPageSourceByClient(dataUrl);
		logger.info(String.format("[%d] [wb:all] pull media from url [%s]",System.currentTimeMillis(),dataUrl));
		List<UserProfile> wbMedias = JSON.parseArray(JsonPath.parse(pageSource).read("$.profile").toString(), UserProfile.class);
		wbMedias.stream().forEach(meida -> {
			//过滤掉微博粉丝小于1w的媒体
			if (meida.getFans_number() < filterFansNum) return;
			sender.sendMediaStatic(ConvertUtils.weiboMediaStaticConvert(meida));
			sender.sendMediaDynamic(ConvertUtils.weiboMediaDynamicConvert(meida));
		});
	}

	@Override
	public long getStartId() {
		Optional<IdRecord> idRecordOpt = IdRecordUtil.getIdRecord();
		return idRecordOpt.orElse(new IdRecord()).getWbMediaId();
	}

	@Override
	public void recordId(long maxId) {
		IdRecordUtil.syncWbMediaId(maxId);
	}

	@Override
	public List<String> getRangeUrls() {
		List<Long> ids = getIds(ReqUrlEnum.WB_MEDIA_RANGE_URL.getValue(), 100);
		return ids.stream().map(id -> String.format(ReqUrlEnum.WB_MEDIA_DATA_URL.getValue(), id)).collect(Collectors.toList());
	}

	public static void main(String[] args) {
		new WeiboMediaHandle(null).handle();
	}
}
