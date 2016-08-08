package com.ptb.uranus.spider.weibo.parse;

import com.alibaba.fastjson.JSONObject;
import com.ptb.uranus.spider.common.utils.HttpUtil;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
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
 * @Date: 2016/8/5
 * @Time: 17:01
 */
public class WeiboHotTopicsParser {

	private String getTargetElement(String url) throws IOException, ScriptException {
		String pageSource = HttpUtil.getPageSourceByClient(url, HttpUtil.UA_PC_CHROME, null, "utf-8", null);
		Document doc = Jsoup.parse(pageSource);
		Elements scripts = doc.getElementsByTag("script");
		Optional<Element> retOpt = scripts.stream().filter(element -> element.toString().contains("pl_Srank_swf")).findFirst();
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

	public List<JSONObject> getHotTopics(String url) throws IOException, ScriptException {
		String targetElement = getTargetElement(url);
		Document doc = Jsoup.parse(targetElement);
		Elements elements = doc.getElementsByTag("tr");
		List<JSONObject> rets = elements.stream().map(ele -> {
			try {
				String topic = ele.select("td.td_02").text();
				long searchNum = Long.parseLong(ele.select("td.td_03").text());
				JSONObject retJson = new JSONObject();
				retJson.put("topic", topic);
				retJson.put("searchNum", searchNum);
				return retJson;
			} catch (Exception e) {
				return null;
			}
		}).filter(Objects::nonNull).collect(Collectors.toList());
		return rets;
	}

	public static void main(String[] args) throws IOException, ScriptException {
		WeiboHotTopicsParser weiboTagParser = new WeiboHotTopicsParser();
		List<JSONObject> hotTopics = weiboTagParser.getHotTopics("http://s.weibo.com/top/summary");
		System.out.println(hotTopics);
	}
}
