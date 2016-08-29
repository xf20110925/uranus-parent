package com.ptb.uranus.spider.weibo.parse;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.ptb.uranus.spider.common.utils.HttpUtil;
import com.ptb.uranus.spider.weibo.login.LoginAccountEnum;

import org.apache.commons.lang.StringUtils;
import org.apache.http.client.CookieStore;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.Queue;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import static java.util.Collections.emptyList;

/**
 * @DESC:
 * @VERSION:
 * @author: xuefeng
 * @Date: 2016/8/25
 * @Time: 11:48
 */
public class WeiboHotTopicArticleParser {
	Queue<CookieStore> cookieQueue = new LinkedList<>();

	public WeiboHotTopicArticleParser() {
		for (LoginAccountEnum loginAccountEnum : LoginAccountEnum.values()) {
			if (loginAccountEnum.getCookie() != null)
				cookieQueue.add(loginAccountEnum.getCookie());
		}
	}

	private List<String> getAllPageLink(String keyword) {
		String url = "http://s.weibo.com/ajax/morestatus?page=%d&key=%s&xsort=hot";
		List<String> urls = new ArrayList<>();
		try {
			int pageNum = 1;
			for (int i = 1; i <= pageNum; i++)
				urls.add(String.format(url, i, keyword));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return urls;
	}

	private List<WbTopicArticle> parseArticle(String targetEle) {
		try {
			Document doc = Jsoup.parse(targetEle);
			Elements eles = doc.select("div.card.item_weibo");
			List<WbTopicArticle> rets = eles.stream().map(ele -> {
				String actionData = ele.attr("action-data");
				String href = "";
				Pattern pattern = Pattern.compile("mid=(.*)&uid=(\\d+)");
				Matcher matcher = pattern.matcher(actionData);
				if (matcher.find()) {
					href = String.format("http://weibo.com/%s/%s", matcher.group(2), matcher.group(1));
				}
				if (StringUtils.isBlank(href)) return null;
				Elements zpzNums = ele.select("div.feeds.clearfix > span");
				long zpzNum = 0;
				if (zpzNums.size() > 0)
					zpzNum = zpzNums.stream().map(num -> num.text().matches("\\d+") ? Long.parseLong(num.text()) : 0).reduce(0L, (x, y) -> x + y);
				return new WbTopicArticle(href, zpzNum);
			}).filter(Objects::nonNull).collect(Collectors.toList());
			return rets;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return emptyList();
	}

	public List<WbTopicArticle> getHotArticles(String keyword) {
		CookieStore cookieStore = cookieQueue.poll();
		try {
			List<String> urls = getAllPageLink(keyword);
			List<WbTopicArticle> wbTopicArticles = urls.stream().flatMap(wbHotArticleUrl -> {
				String pageSource = HttpUtil.getPageSourceByClient(wbHotArticleUrl, HttpUtil.UA_PC_CHROME, cookieStore, "utf-8", null, true);
				try {
					JSONObject json = JSON.parseObject(pageSource);
					String data = json.getString("data");
					if (StringUtils.isNotBlank(data))
						return parseArticle(data).stream();
				} catch (Exception e) {
					e.printStackTrace();
				}
				return null;
			}).collect(Collectors.toList());
			return wbTopicArticles;
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (cookieStore != null)
				cookieQueue.add(cookieStore);
		}
		return Collections.emptyList();
	}

	public static void main(String[] args) {
		WeiboHotTopicArticleParser parser = new WeiboHotTopicArticleParser();
		String url = "这小懒腰伸的我给满分";
		List<WbTopicArticle> ret = parser.getHotArticles(url);
		System.out.println(ret);
	}

	public static class WbTopicArticle {
		private String articleUrl;
		private long zpzNum;

		public WbTopicArticle() {
		}

		public WbTopicArticle(String articleUrl, long zpzNum) {
			this.articleUrl = articleUrl;
			this.zpzNum = zpzNum;
		}

		public String getArticleUrl() {
			return articleUrl;
		}

		public void setArticleUrl(String articleUrl) {
			this.articleUrl = articleUrl;
		}

		public long getZpzNum() {
			return zpzNum;
		}

		public void setZpzNum(long zpzNum) {
			this.zpzNum = zpzNum;
		}

		@Override
		public String toString() {
			return JSON.toJSONString(this);
		}
	}
}
