package com.ptb.uranus.server.third.version2.article;

import com.alibaba.fastjson.JSON;
import com.jayway.jsonpath.JsonPath;
import com.ptb.gaia.bus.kafka.KafkaBus;
import com.ptb.uranus.server.handle.WeiboArticleDynamicHandle;
import com.ptb.uranus.server.send.BusSender;
import com.ptb.uranus.server.send.Sender;
import com.ptb.uranus.server.third.entity.BayouWXArticleDynamic;
import com.ptb.uranus.server.third.entity.IdRecord;
import com.ptb.uranus.server.third.util.ConvertUtils;
import com.ptb.uranus.server.third.util.IdRecordUtil;
import com.ptb.uranus.server.third.version2.DataHandle;
import com.ptb.uranus.server.third.version2.ReqUrlEnum;
import com.ptb.uranus.spider.common.utils.HttpUtil;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * @DESC:
 * @VERSION:
 * @author: xuefeng
 * @Date: 2016/8/2
 * @Time: 15:10
 */
public class WeixinArticleDynamicHandle implements DataHandle {
	static Logger logger = LoggerFactory.getLogger(WeiboArticleDynamicHandle.class);
	private Sender sender;
	private Set<String> pmids;

	public WeixinArticleDynamicHandle(Sender sender) {
		this.sender = sender;
		pmids = getPmids("gaia2", "wxMedia");
	}

	@Override
	public void handleBusEntities(String dataUrl) {
		String pageSource = HttpUtil.getPageSourceByClient(dataUrl);
		Pattern pattern = Pattern.compile(".*biz=([^&]*).*");
		logger.info(String.format("[%d] [wx:dynamic]  pull  from url [%s]",System.currentTimeMillis(),dataUrl));
		List<BayouWXArticleDynamic> wxArticleDynamics = JSON.parseArray(JsonPath.parse(pageSource).read("$.clicks").toString(), BayouWXArticleDynamic.class);
		wxArticleDynamics.stream().map(ConvertUtils::convertWXArticleDynamic).filter(wxArticle -> {
			String wxArticleUrl = wxArticle.getUrl();
			Matcher matcher = pattern.matcher(wxArticleUrl);
			if (matcher.find()){
				String biz = matcher.group(1);
				boolean isExist = pmids.contains(biz);
				//白名单中存在，保留媒体发文
				return isExist;
			}
			return false;
		}).forEach(sender::sendArticleDynamic);
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
		KafkaBus bus = new KafkaBus();
		Sender sender = new BusSender(bus);
		bus.start(false, 5);
		new WeixinArticleDynamicHandle(sender).handle();
	}
}
