package com.ptb.uranus.server.third.version2;


import com.jayway.jsonpath.DocumentContext;
import com.jayway.jsonpath.JsonPath;
import com.ptb.uranus.server.third.exception.BayouException;
import com.ptb.uranus.spider.common.utils.HttpUtil;

import org.apache.log4j.Logger;

import java.util.ArrayList;
import java.util.List;

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
		rangeUrls.stream().forEach(url -> {
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

}
