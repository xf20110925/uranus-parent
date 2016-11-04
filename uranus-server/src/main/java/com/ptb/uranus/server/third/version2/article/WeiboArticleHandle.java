package com.ptb.uranus.server.third.version2.article;

import com.alibaba.fastjson.JSON;
import com.jayway.jsonpath.JsonPath;
import com.mongodb.client.FindIterable;
import com.ptb.uranus.schedule.utils.MongoUtils;
import com.ptb.uranus.server.send.Sender;
import com.ptb.uranus.server.third.entity.FreshData;
import com.ptb.uranus.server.third.entity.IdRecord;
import com.ptb.uranus.server.third.util.ConvertUtils;
import com.ptb.uranus.server.third.util.IdRecordUtil;
import com.ptb.uranus.server.third.version2.DataHandle;
import com.ptb.uranus.server.third.version2.ReqUrlEnum;
import com.ptb.uranus.spider.common.utils.HttpUtil;
import com.ptb.uranus.spider.weibo.bean.WeiboArticle;

import org.bson.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @DESC:
 * @VERSION:
 * @author: xuefeng
 * @Date: 2016/8/2
 * @Time: 15:10
 */
public class WeiboArticleHandle implements DataHandle{
	static Logger logger = LoggerFactory.getLogger(WeiboArticle.class);

	private Sender sender;
	private Set<String> pmids;

	public WeiboArticleHandle(Sender sender) {
		pmids = getWBPmids();
		this.sender = sender;
	}

	@Override
	public void handleBusEntities(String dataUrl) {
		String pageSource = HttpUtil.getPageSourceByClient(dataUrl);
		logger.info(String.format("[%d] [wb:all] pull article data from url [%s]",System.currentTimeMillis(), dataUrl));
		List<FreshData> wxArticles = JSON.parseArray(JsonPath.parse(pageSource).read("$.weibo").toString(), FreshData.class);
		wxArticles.forEach(article -> {
			if (!pmids.contains(article.getR_user_id())) return;
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

	public Set<String> getWBPmids(){
		Set<String> pmids = new HashSet<>();
		FindIterable<Document> docs = MongoUtils.instance.getDatabase("gaia2").getCollection("wbMediaTest").find().projection(new Document().append("_id", 1));
		for (Document doc : docs) {
			pmids.add(doc.getString("_id"));
		}
		return pmids;
	}
}
