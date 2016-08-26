package com.ptb.uranus.spider.weibo.parse;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.Collections;
import java.util.List;
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
 * @Date: 2016/8/25
 * @Time: 11:10
 */
public interface BaseWeiboParser {
	default String getTargetElement(String pageSource, String targetScriptIdentity) throws IOException, ScriptException {
		Document doc = Jsoup.parse(pageSource);
		Elements scripts = doc.getElementsByTag("script");
		Optional<Element> retOpt = scripts.stream().filter(element -> element.toString().contains(targetScriptIdentity)).findFirst();
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
		throw new NullPointerException("数据解析失败");
	}

	default int getPageNum(String tagElement) throws IOException, ScriptException {
		Document doc = Jsoup.parse(tagElement);
		Element pageEle = doc.select("div.W_pages").first();
		Elements pageNumEles = pageEle.select("a.page.S_txt1");
		List<Integer> pageNums = pageNumEles.stream().filter(ele -> ele.text().matches("\\d+")).map(ele -> Integer.parseInt(ele.text())).collect(Collectors.toList());
		Collections.reverse(pageNums);
		int lastPageNum = pageNums.get(0);
		return lastPageNum;
	}
}
