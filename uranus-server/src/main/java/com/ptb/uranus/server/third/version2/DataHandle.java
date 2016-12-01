package com.ptb.uranus.server.third.version2;


import com.jayway.jsonpath.DocumentContext;
import com.jayway.jsonpath.JsonPath;
import com.ptb.uranus.server.third.exception.BayouException;
import com.ptb.uranus.server.third.util.JedisUtil;
import com.ptb.uranus.spider.common.utils.HttpUtil;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @DESC:
 * @VERSION:
 * @author: xuefeng
 * @Date: 2016/8/2
 * @Time: 14:26
 */
public interface DataHandle {
	Logger requestLogger = Logger.getLogger("bayou.request");

	default void handle() {
		List<String> rangeUrls = null;
		try {
			rangeUrls = getRangeUrls();
		} catch (Exception e) {
			requestLogger.error(String.format("get rangeUrls error -> %s", e));
		}
		rangeUrls.parallelStream().forEach(url -> {
			try {
				handleBusEntities(url);
			} catch (Exception e) {
				requestLogger.error(String.format("handle data error -> %s", e));
			}
		});
	}

	default List<Long> getIds(String rangeUrl, int batchSize) {
		try {
			String pageSource = HttpUtil.getPageSourceByClient(rangeUrl);
			DocumentContext parse = JsonPath.parse(pageSource);
			Long minId = parse.read("$.minid", Long.class);
			Long maxId = parse.read("$.maxid", Long.class);
			minId = getStartId() > minId ? getStartId() : minId;
			List<Long> ids = new ArrayList<>();
			for (long i = minId; i <= maxId; i += batchSize) ids.add(i);
			recordId(maxId);
			return ids;
		} catch (Exception e) {
			throw new BayouException(String.format("get rangeId error->%s", e));
		}
	}

	/**
	 * 从id记录文件获取开始id
	 */
	long getStartId();

	/**
	 * 同步id到id记录文件
	 */
	void recordId(long maxId);

	/**
	 * 取得获取数据的url
	 */
	List<String> getRangeUrls();

	/**
	 * 获取数据并发送到bus
	 */
	void handleBusEntities(String dataUrl);

	default Optional<String> getByRegex(String text, Pattern pattern, int index) {
		Matcher matcher = pattern.matcher(text);
		if (matcher.find()) return Optional.of(matcher.group(index));
		return Optional.empty();
	}

	default boolean isExists(String text, Pattern pattern, int index) {
		String pmid = getByRegex(text, pattern, index).orElse("");
		if (StringUtils.isNotBlank(pmid)) {
			//白名单中存在，保留媒体发文
			boolean isExist = JedisUtil.instance.exists(pmid);
			return isExist;
		}
		return false;
	}
	/**
	 * 获取媒体白名单或者黑名单
	 * @return
	 */
	/*default Set<String> getPmids(String dataBase, String colletName){
		Set<String> pmids = new HashSet<>();
		FindIterable<Document> docs = MongoUtils.instance.getDatabase(dataBase).getCollection(colletName).find().projection(new Document().append("_id", 1));
		for (Document doc : docs) {
			pmids.add(doc.getString("_id"));
		}
		return pmids;
	};*/

}
