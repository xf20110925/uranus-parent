package com.ptb.uranus.server.third.v2.article;

import com.alibaba.fastjson.JSON;
import com.jayway.jsonpath.JsonPath;
import com.ptb.uranus.server.send.Sender;
import com.ptb.uranus.server.third.v2.DataHandle;
import com.ptb.uranus.server.third.v2.ReqUrlEnum;
import com.ptb.uranus.server.third.entity.BayouWXArticleDynamic;
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
public class WeixinArticleDynamicHandle implements DataHandle {

	private Sender sender;

	public WeixinArticleDynamicHandle(Sender sender) {
		this.sender = sender;
	}

	@Override
	public void handleBusEntities(String dataUrl) {
		String pageSource = HttpUtil.getPageSourceByClient(dataUrl);
		List<BayouWXArticleDynamic> wxArticleDynamics = JSON.parseArray(JsonPath.parse(pageSource).read("$.clicks").toString(), BayouWXArticleDynamic.class);
		wxArticleDynamics.stream().map(ConvertUtils::convertWXArticleDynamic).forEach(sender::sendArticleDynamic);
	}

	@Override
	public long getStartId() {
		Optional<IdRecord> idRecordOpt = IdRecordUtil.getIdRecord();
		return idRecordOpt.orElse(new IdRecord()).getDynamicArticleId();
	}

	@Override
	public void recordId(long maxId) {
		IdRecordUtil.syncDynamicArticleId(maxId);
	}

	@Override
	public List<String> getRangeUrls() {
		List<Long> ids = getIds(ReqUrlEnum.WX_ARTICLE_DYNAMIC_RANGE_URL.getValue(), 100);
		return ids.stream().map(id -> String.format(ReqUrlEnum.WX_ARTICLE_DYNAMIC_DATA_URL.getValue(), id)).collect(Collectors.toList());
	}

	public static void main(String[] args) {
		new WeixinArticleDynamicHandle(null).handle();
	}
}
