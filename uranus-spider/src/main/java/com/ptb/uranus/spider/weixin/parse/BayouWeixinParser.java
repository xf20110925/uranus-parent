package com.ptb.uranus.spider.weixin.parse;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.jayway.jsonpath.DocumentContext;
import com.jayway.jsonpath.JsonPath;
import com.ptb.uranus.spider.common.utils.HttpUtil;
import com.ptb.uranus.spider.weixin.bean.ReadLikeNum;

import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.http.entity.ContentType;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;

/**
 * Created by xuefeng on 2016/5/31.
 */
public class BayouWeixinParser {

	static Logger requestLogger = Logger.getLogger("bayou.request");
	static Logger logger = Logger.getLogger(BayouWeixinParser.class);
	private String RECENT_ARTILES_URL = null;
	private String READ_LIKE_URL = null;
	private String ASSITANT_READLIKE_URL = null;
	static AtomicLong requestNum = new AtomicLong(0);
	static AtomicLong requestErrorNum = new AtomicLong(0);

	private void loadConfig() {
		PropertiesConfiguration conf = new PropertiesConfiguration();
		try {
			conf.load("ptb.properties");
		} catch (ConfigurationException e) {
		}
		RECENT_ARTILES_URL = conf.getString("uranus.spider.wx.bayou.recentarticles.url", "http://43.241.211.196:23333/history");
		READ_LIKE_URL = conf.getString("uranus.spider.wx.bayou.readlike.url", "http://43.241.211.196:23333/readlike");
		ASSITANT_READLIKE_URL = conf.getString("uranus.spider.wx.readlike");
	}

	public BayouWeixinParser() {
		loadConfig();
	}

	public Optional<ReadLikeNum> getReadLikeNumByArticleUrl(String wxArticleUrl) {
		try {
			String result = HttpUtil.postByPcClient(READ_LIKE_URL, wxArticleUrl, null);
			DocumentContext parse = JsonPath.parse(result);
			String errorCode = parse.read("$.error").toString();
			if ("0".equals(errorCode)) {
				requestNum.addAndGet(1);
				int readNum = parse.read("$.msg.read_num");
				int likeNum = parse.read("$.msg.like_num");
				return Optional.of(new ReadLikeNum(readNum, likeNum));
			} else {
				requestErrorNum.addAndGet(1);
				requestLogger.warn(String.format("request readlike error[%s] ", parse.read("$.msg").toString()));
				requestLogger.warn(String.format("request success num[%d], fail num[%d]", requestNum.get(), requestErrorNum.get()));
			}
		} catch (Exception e) {
			logger.error(e);
		}
		return Optional.empty();
	}

	public Optional<ImmutablePair<Long, List<String>>> getRecentArticlesByBiz(
		String biz, long lastArticlePostTime) {
		String result = null;
		try {
			result = HttpUtil.postByPcClient(RECENT_ARTILES_URL, biz, null);
			DocumentContext parse = JsonPath.parse(result);
			String errorCode = parse.read("$.error").toString();
			if ("0".equals(errorCode)) {
				List reads = parse.read("$.msg");
				requestNum.addAndGet(1);
				List<String> urls = new ArrayList<>();
				List<Long> cts = new ArrayList<>();
				reads.stream().forEach(json -> {
					DocumentContext doc = JsonPath.parse(json);
					long ct = doc.read("$.ct", Long.class);
					if (ct > lastArticlePostTime) {
						urls.add(doc.read("$.main_url"));
						urls.addAll(doc.read("ext_urls"));
						cts.add(ct);
					}
				});
				Collections.sort(cts, Collections.reverseOrder());
				return Optional.of(new ImmutablePair<>(cts.get(0), urls));
			} else {
				requestErrorNum.addAndGet(1);
				requestLogger.warn(String.format("request recent articles error[%s] ", parse.read("$.msg").toString()));
				requestLogger.warn(String.format("request success num[%d], fail num[%d]", requestNum.get(), requestErrorNum.get()));
			}
		} catch (Exception e) {
			logger.error(e);
		}
		return Optional.empty();
	}

	public Optional<ReadLikeNum> getReadLikeByAssitant(String articleUrl) {
		String reqUrl = ASSITANT_READLIKE_URL;
		try {
			JSONObject req = new JSONObject();
			req.put("url", articleUrl);
			String readLike = HttpUtil.postByPcClient(reqUrl, JSON.toJSONString(req), ContentType.APPLICATION_JSON);
			if (StringUtils.isNotBlank(readLike))
				return Optional.of(JSON.parseObject(readLike, ReadLikeNum.class));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return Optional.empty();
	}

	public static void main(String[] args) throws IOException {
		BayouWeixinParser bayouWeixinParser = new BayouWeixinParser();
		if (args[0].startsWith("http")) {
			Optional<ReadLikeNum> readLikeNumByArticleUrl = bayouWeixinParser.getReadLikeNumByArticleUrl(args[0]);
			System.out.println(readLikeNumByArticleUrl.get());
		} else {
			Optional<ImmutablePair<Long, List<String>>> recentArticlesOpt = bayouWeixinParser.getRecentArticlesByBiz(args[0], 0);
			System.out.println(recentArticlesOpt.get().getRight().toString());
		}

	}

}
