package com.ptb.uranus.server.third.version2.article;

import com.alibaba.fastjson.JSON;
import com.jayway.jsonpath.JsonPath;
import com.ptb.uranus.server.send.Sender;
import com.ptb.uranus.server.third.entity.FreshData;
import com.ptb.uranus.server.third.entity.IdRecord;
import com.ptb.uranus.server.third.util.ConvertUtils;
import com.ptb.uranus.server.third.util.IdRecordUtil;
import com.ptb.uranus.server.third.util.JedisUtil;
import com.ptb.uranus.server.third.version2.DataHandle;
import com.ptb.uranus.server.third.version2.ReqUrlEnum;
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
public class WeiboArticleHandle implements DataHandle{
	private Sender sender;

	public WeiboArticleHandle(Sender sender) {
		this.sender = sender;
	}

	@Override
	public void handleBusEntities(String dataUrl) {
		String pageSource = HttpUtil.getPageSourceByClient(dataUrl);
		logger.info(String.format("[%d] [wb:all] pull article data from url [%s]",System.currentTimeMillis(), dataUrl));
		List<FreshData> wxArticles = JSON.parseArray(JsonPath.parse(pageSource).read("$.weibo").toString(), FreshData.class);
		wxArticles.forEach(article -> {
			if (!JedisUtil.instance.exists(article.getR_user_id())) return;
			sender.sendArticleStatic(ConvertUtils.weiboArticleStaticConvert(article));
			sender.sendArticleDynamic(ConvertUtils.weiboArticleDynamicConvert(article));
		});
	}

	@Override
	public long getStartId() {
		Optional<IdRecord> idRecordOpt = IdRecordUtil.getIdRecord();
		return idRecordOpt.orElse(new IdRecord()).getWbArticleId();
	}

	@Override
	public void recordId(long maxId) {
		IdRecordUtil.syncWbArticleId(maxId);
	}

	@Override
	public List<String> getRangeUrls() {
		List<Long> ids = getIds(ReqUrlEnum.WB_ARTICLE_RANGE_URL.getValue(), 1000);
		return ids.stream().map(id -> String.format(ReqUrlEnum.WB_ARTICLE_DATA_URL.getValue(), id)).collect(Collectors.toList());
	}

	public static void main(String[] args) {
		WeiboArticleHandle weiboArticleHandle = new WeiboArticleHandle(null);
		weiboArticleHandle.handle();
	}

}
