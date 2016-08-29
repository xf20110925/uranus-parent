package com.ptb.uranus.spider.weibo.parse;

import com.alibaba.fastjson.JSON;
import com.ptb.uranus.spider.common.utils.HttpUtil;
import com.ptb.uranus.spider.weibo.login.LoginAccountEnum;

import org.apache.commons.lang.StringUtils;
import org.apache.http.client.CookieStore;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
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
			String pageSource = HttpUtil.getPageSourceByClient(url, HttpUtil.UA_PC_CHROME, cookieStore, "utf-8", null, true);
			String targetEle = getTargetElement(pageSource, "feed_list_newBar");
			int pageNum = getPageNum(targetEle);
			for (int i = 2; i <= pageNum; i++)
				urls.add(url.replace("Refer=top", String.format("page=%d", i)));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return urls;
	}

	private int getPageNum(String tagElement) {
		Document doc = Jsoup.parse(tagElement);
		Element pageEle = doc.select("div.layer_menu_list.W_scroll > ul").first();
		String pageNum = pageEle.getElementsByTag("li").last().text();
		int lastPageNum = 2;
		Matcher matcher = Pattern.compile("第(\\d+)页").matcher(pageNum);
		if (matcher.find()) lastPageNum = Integer.parseInt(matcher.group(1));
		return lastPageNum;
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
			List<WbTopicArticle> wbTopicArticles = urls.stream().flatMap(wbHotArticleUrl -> {
				String pageSource = HttpUtil.getPageSourceByClient(url, HttpUtil.UA_PC_CHROME, cookieStore, "utf-8", null, true);
				sleep(5);
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
			if (cookieStore != null)
				cookieQueue.add(cookieStore);
		}
		return Collections.emptyList();
	}

	public static void main(String[] args) {
		WeiboHotTopicArticleParser parser = new WeiboHotTopicArticleParser();
		String url = "http://s.weibo.com/weibo/%25E5%259C%259F%25E8%25B1%25AA%25E4%25B8%2588%25E6%25AF%258D%25E5%25A8%2598%25E6%2594%25B6%25E6%258B%25BE%25E6%259C%25AA%25E6%259D%25A5%25E5%25A5%25B3%25E5%25A9%25BF&Refer=top";
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
