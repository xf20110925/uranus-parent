package com.ptb.uranus.spider.weibo.parse;

import com.ptb.uranus.spider.common.utils.HttpUtil;

import org.apache.http.client.CookieStore;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.cookie.BasicClientCookie2;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;

/**
 * @DESC:
 * @VERSION:
 * @author: xuefeng
 * @Date: 2016/8/4
 * @Time: 10:01
 */
public class WeiboTagParser {

	private String getTargetElement(String url) throws IOException, ScriptException {
		String pageSource = HttpUtil.getPageSourceByClient(url, HttpUtil.UA_PC_CHROME, getVaildWeiboCookieStore(), "utf-8", null);
		Document doc = Jsoup.parse(pageSource);
		Elements scripts = doc.getElementsByTag("script");
		Optional<Element> retOpt = scripts.stream().filter(element -> element.toString().contains("pl.content.signInPeople.index")).findFirst();
		if (retOpt.isPresent()) {
			String element = retOpt.get().toString();
			Pattern pattern = Pattern.compile(".*\"html\":(.*)}\\)</script>");
			Matcher matcher = pattern.matcher(element);
			if (matcher.find()) {
				String ret = matcher.group(1);
				ScriptEngine engine = new ScriptEngineManager().getEngineByName("JavaScript");
				String retsult = engine.eval(ret).toString();
				return retsult;
			}
		}
		throw new NullPointerException(String.format("数据抓取失败 -> %s", url));
	}

	private int getPageNum(String tagElement) throws IOException, ScriptException {
		Document doc = Jsoup.parse(tagElement);
		Element pageEle = doc.select("div.W_pages").first();
		Elements pageNumEles = pageEle.select("a.page.S_txt1");
		List<Integer> pageNums = pageNumEles.stream().filter(ele -> ele.text().matches("\\d+")).map(ele -> Integer.parseInt(ele.text())).collect(Collectors.toList());
		Collections.reverse(pageNums);
		int lastPageNum = pageNums.get(0);
		return lastPageNum;
	}

	public List<String> getAllLinks(String link) throws IOException, ScriptException {
		String targetElement = getTargetElement(link);
		int pageNum = getPageNum(targetElement);
		List<String> links = new ArrayList<>();
		for (int i = 1; i <= pageNum; i++) {
			links.add(link.replaceAll("page=\\d+", String.format("page=%d", i)));
		}
		return links;
	}

	public List<String> getAccounts(String url) {
		try {
			String targetElement = getTargetElement(url);
			Document doc = Jsoup.parse(targetElement);
			Elements elements = doc.select("ul.follow_list > li");
			List<String> mediaTags = elements.stream().map(ele -> getWeiboId(ele)).collect(Collectors.toList());
			return mediaTags;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return Collections.emptyList();
	}

	private String getWeiboId(Element ele) {
		Elements persionNalEle = ele.select("div.info_name.W_fb.W_f14");
		Matcher matcher = Pattern.compile("id=(\\d+).*").matcher(persionNalEle.select("a.S_txt1").attr("usercard"));
		if (matcher.find()) {
			return matcher.group(1);
		}
		return null;
	}

	private static CookieStore getVaildWeiboCookieStore() {
		CookieStore cookieStore = new BasicCookieStore();
		BasicClientCookie2 sub = new BasicClientCookie2("SUB", UUID.randomUUID().toString());
		sub.setDomain("d.weibo.com");
		cookieStore.addCookie(sub);
		return cookieStore;
	}

	public List<String> run(String url) throws IOException, ScriptException {
		List<String> allLinks = getAllLinks(url);
		List<String> rets = allLinks.stream().flatMap(link -> {
			try {
				Thread.sleep(new Random().nextInt(4));
				return getAccounts(link).stream();
			} catch (Exception e) {
				System.out.println(String.format("crawle %s fail and retry", link));
				return getAccounts(link).stream();
			}
		}).collect(Collectors.toList());
		return rets;

	}

	public static void main(String[] args) throws IOException, ScriptException {
		WeiboTagParser weiboTagParser = new WeiboTagParser();
		List<String> ret = weiboTagParser.getAccounts("http://d.weibo.com/1087030002_2975_2003_0?page=1");
		System.out.println(ret.size());
	}
}
