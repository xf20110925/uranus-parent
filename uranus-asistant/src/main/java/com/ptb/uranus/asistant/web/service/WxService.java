package com.ptb.uranus.asistant.web.service;

import com.alibaba.fastjson.JSON;
import com.jayway.jsonpath.DocumentContext;
import com.jayway.jsonpath.JsonPath;
import com.ptb.uranus.asistant.core.entity.ReadLikeNum;
import com.ptb.uranus.asistant.core.util.RedisUtil;
import com.ptb.uranus.asistant.core.util.WxUtils;
import com.ptb.uranus.spider.weixin.parse.BayouWeixinParser;
import com.ptb.utils.exception.PTBException;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.Optional;
import java.util.concurrent.ConcurrentLinkedQueue;

import redis.clients.jedis.Jedis;

/**
 * Created by eric on 16/1/6.
 */
@Service
public class WxService {

	String Q_WX_redirect = "Q_WX_redirect";

	int waitTimeout = 60;
	int resultTimeout = 120;
	int retryInterval = 20;
	int keyV2ExpireSecond = 3600;
	int mapLinkTimeOut = 2 * 24 * 60 * 60;


	public String getRedirectUrl(String urlStr) {
		Jedis jedis = RedisUtil.getJedis();
		try {
			String resKey = "";

			resKey = WxUtils.getUrlKey(urlStr);
			int i = 0;
			while (i < waitTimeout) {
				String hget = jedis.get(resKey);
				if (hget != null && hget.length() > 0) {
					return hget;
				}
				if (i % retryInterval == 0) {
					jedis.lpush(Q_WX_redirect, urlStr);
				}
				i++;
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
			throw new PTBException(String.format("等待有效KEY,超时,url %s", urlStr));
		} finally {
			RedisUtil.returnResource(jedis);
		}

	}

	public String getNextRedirectUrl() {
		Jedis jedis = RedisUtil.getJedis();
		try {
			String url = jedis.lpop(Q_WX_redirect);
			if (url == null) {
				return null;
			}

			String urlKey = WxUtils.getUrlKey(url);
			String hget = jedis.get(urlKey);
			if (hget != null && hget.length() > 10) {
				return null;
			}
			return url;

		} finally {
			RedisUtil.returnResource(jedis);
		}
	}

	public void updateRealUrl(String url) {
		Jedis jedis = RedisUtil.getJedis();
		try {
			jedis.setex(WxUtils.getUrlKey(url), resultTimeout, url);
		} finally {
			RedisUtil.returnResource(jedis);
		}
	}


	public void addWxUrlToQueue(String url) {
		Jedis jedis = RedisUtil.getJedis();
		try {
			jedis.lpush(Q_WX_redirect, url);
		} finally {
			RedisUtil.returnResource(jedis);
		}
	}

	public String getValueByKey(String key) {
		Jedis jedis = RedisUtil.getJedis();
		try {
			return jedis.get(key);
		} finally {
			RedisUtil.returnResource(jedis);
		}
	}

	public void updateRealUrlAndReadNum(String data) {
		Jedis jedis = RedisUtil.getJedis();
		try {
			DocumentContext parse = JsonPath.parse(data);
			String url = parse.read("$.url").toString();
			String readNum = parse.read("$.readNum").toString();
			String likeNum = parse.read("$.likeNum").toString();
			jedis.setex(WxUtils.getUrlKeyV1(url), resultTimeout, JSON.toJSONString(new ReadLikeNum(readNum, likeNum)));
		} finally {
			RedisUtil.returnResource(jedis);
		}
	}

	public void updateRealUrlV1(String url) {
		Jedis jedis = RedisUtil.getJedis();
		try {
			jedis.setex(WxUtils.getUrlKeyV2(url), keyV2ExpireSecond, url);
		} finally {
			RedisUtil.returnResource(jedis);
		}
	}

	public void mapLinkAdd(String realUrl, String sogouUrl) {
		Jedis jedis = RedisUtil.getJedis();
		try {
			jedis.setex(realUrl, mapLinkTimeOut, sogouUrl);
		} finally {
			RedisUtil.returnResource(jedis);
		}
	}

	public String mapLinkGet(String key) {
		Jedis jedis = RedisUtil.getJedis();
		try {
			return jedis.get(key);
		} finally {
			RedisUtil.returnResource(jedis);
		}
	}

	//控制访问频率锁
	private static ConcurrentLinkedQueue<Integer> queue = new ConcurrentLinkedQueue<>(Arrays.asList(1));
	private BayouWeixinParser parser = new BayouWeixinParser();
	static Logger requestLogger = Logger.getLogger("bayou.request");

	public String getReadLikeByBaYou(String url) {
		if (queue.isEmpty()) return "";
		synchronized (queue) {
			Integer first = queue.poll();
			try {
				Thread.sleep(1000);
				Optional<com.ptb.uranus.spider.weixin.bean.ReadLikeNum> readLikeOpt = parser.getReadLikeNumByArticleUrl(url);
				if (readLikeOpt.isPresent()) {
					requestLogger.error(String.format("request success url-> %s, time->%s", url, System.currentTimeMillis() / 1000));
					return readLikeOpt.get().toString();
				}
				requestLogger.error(String.format("request failed url->%s, time->%s", url, System.currentTimeMillis() / 1000));
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				if (first != null)
					queue.add(first);
			}
			return "";
		}
	}
}
