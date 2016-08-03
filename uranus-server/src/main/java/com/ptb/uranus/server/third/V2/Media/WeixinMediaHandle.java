package com.ptb.uranus.server.third.v2.media;

import com.alibaba.fastjson.JSON;
import com.jayway.jsonpath.JsonPath;
import com.ptb.uranus.server.send.Sender;
import com.ptb.uranus.server.third.v2.DataHandle;
import com.ptb.uranus.server.third.v2.ReqUrlEnum;
import com.ptb.uranus.server.third.entity.BayouWXMedia;
import com.ptb.uranus.server.third.entity.IdRecord;
import com.ptb.uranus.server.third.util.ConvertUtils;
import com.ptb.uranus.server.third.util.IdRecordUtil;
import com.ptb.uranus.spider.common.utils.HttpUtil;

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
public class WeixinMediaHandle implements DataHandle {

	private Sender sender;

	public WeixinMediaHandle(Sender sender) {
		this.sender = sender;
	}

	@Override
	public void handleBusEntities(String dataUrl) {
		String pageSource = HttpUtil.getPageSourceByClient(dataUrl);
		List<BayouWXMedia> wxMedias = JSON.parseArray(JsonPath.parse(pageSource).read("$.bizs").toString(), BayouWXMedia.class);
		wxMedias.stream().map(ConvertUtils::convertWXMedia).forEach(sender::sendMediaStatic);
	}

	@Override
	public long getStartId() {
		Optional<IdRecord> idRecordOpt = IdRecordUtil.getIdRecord();
		return idRecordOpt.orElse(new IdRecord()).getMediaId();
	}

	@Override
	public void recordId(long maxId) {
		IdRecordUtil.syncMediaId(maxId);
	}

	@Override
	public List<String> getRangeUrls() {
		List<Long> ids = getIds(ReqUrlEnum.WX_MEDIA_RANGE_URL.getValue(), 100);
		return ids.stream().map(id -> String.format(ReqUrlEnum.WX_MEDIA_DATA_URL.getValue(), id)).collect(Collectors.toList());
	}

	public static void main(String[] args) {
		new WeixinMediaHandle(null).handle();
	}
}
