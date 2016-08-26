package com.ptb.uranus.spider.weibo.parse;

import com.alibaba.fastjson.JSON;
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
import java.util.stream.Collectors;

import static com.ptb.uranus.spider.weibo.login.WeiboLoginByHttpClinet.cookieStore;
import static java.util.Collections.emptyList;

/**
 * @DESC:
 * @VERSION:
 * @author: xuefeng
 * @Date: 2016/8/25
 * @Time: 11:48
 */
public class WeiboHotTopicArticleParser implements BaseWeiboParser {
	Queue<CookieStore> cookieQueue = new LinkedList<>();

	public WeiboHotTopicArticleParser() {
		for (LoginAccountEnum loginAccountEnum : LoginAccountEnum.values()) {
			if (loginAccountEnum.getCookie() != null)
				cookieQueue.add(loginAccountEnum.getCookie());
		}
	}

	private List<String> getAllPageLink(String url) {
		List<String> urls = new ArrayList<>();
		try {
			urls.add(url);
			String pageSource = HttpUtil.getPageSourceByClient(url, HttpUtil.UA_PC_CHROME, cookieStore, "utf-8", null, false);
			String targetEle = getTargetElement(pageSource, "feed_list_newBar");
			int pageNum = getPageNum(targetEle);
			for (int i = 2; i <= pageNum; i++)
				urls.add(url.replace("Refer=top", String.format("page=%d", i)));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return urls;
	}

	private List<WbTopicArticle> parseArticle(String targetEle) {
		try {
			Document doc = Jsoup.parse(targetEle);
			Elements eles = doc.select("div.WB_cardwrap.S_bg2.clearfix");
			List<WbTopicArticle> rets = eles.stream().map(ele -> {
				String href = ele.select("div.feed_from.W_textb > a").attr("href");
				if (StringUtils.isBlank(href)) return null;
				Elements zpzNums = ele.select("div.feed_action.clearfix > ul").select("span.line.S_line1 > em");
				long zpzNum = 0;
				if (zpzNums.size() > 0)
					zpzNum = zpzNums.stream().map(num -> StringUtils.isNotBlank(num.text()) ? Long.parseLong(num.text()) : 0).reduce(0L, (x, y) -> x + y);
				return new WbTopicArticle(href, zpzNum);
			}).filter(Objects::nonNull).collect(Collectors.toList());
			return rets;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return emptyList();
	}

	public List<WbTopicArticle> getHotArticles(String url) {
		CookieStore cookieStore = cookieQueue.poll();
		try {
			List<String> urls = getAllPageLink(url);
			CookieStore finalCookieStore = cookieStore;
			List<WbTopicArticle> wbTopicArticles = urls.stream().flatMap(wbHotArticleUrl -> {
				String pageSource = HttpUtil.getPageSourceByClient(url, HttpUtil.UA_PC_CHROME, finalCookieStore, "utf-8", null, false);
				try {
					String targetEle = getTargetElement(pageSource, "feed_list_newBar");
					return parseArticle(targetEle).stream();
				} catch (Exception e) {
					e.printStackTrace();
				}
				return null;
			}).collect(Collectors.toList());
			return wbTopicArticles;
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			cookieQueue.add(cookieStore);
		}
		return Collections.emptyList();
	}

	public static void main(String[] args) {
		WeiboHotTopicArticleParser parser = new WeiboHotTopicArticleParser();
		String url = "http://s.weibo.com/weibo/%25E9%25A9%25AC%25E8%2593%2589%25E5%25AD%25A6%25E7%25B1%258D%25E6%25A1%25A3%25E6%25A1%2588%25E6%259B%259D%25E5%2585%2589&Refer=top";
		List<WbTopicArticle> ret = parser.getHotArticles(url);
		System.out.println(ret);
	}

	static class WbTopicArticle {
		private long topic_id;
		private String article_url;
		private long zpz_num;

		public WbTopicArticle(String article_url, long zpz_num) {
			this.article_url = article_url;
			this.zpz_num = zpz_num;
		}

		public long getTopic_id() {
			return topic_id;
		}

		public void setTopic_id(long topic_id) {
			this.topic_id = topic_id;
		}

		public String getArticle_url() {
			return article_url;
		}

		public void setArticle_url(String article_url) {
			this.article_url = article_url;
		}

		public long getZpz_num() {
			return zpz_num;
		}

		public void setZpz_num(long zpz_num) {
			this.zpz_num = zpz_num;
		}

		@Override
		public String toString() {
			return JSON.toJSONString(this);
		}
	}
}
