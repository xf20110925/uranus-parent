package com.ptb.uranus.spider.weibo.parse;

import com.alibaba.fastjson.JSON;
import com.ptb.uranus.spider.common.utils.HttpUtil;
import com.ptb.uranus.spider.common.utils.WeiboUtil;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import javax.script.ScriptException;

/**
 * @DESC:
 * @VERSION:
 * @author: xuefeng
 * @Date: 2016/8/5
 * @Time: 17:01
 */
public class WeiboHotTopicsParser implements BaseWeiboParser {

	public List<Topic> getHotTopics() throws IOException, ScriptException {
		String url = "http://s.weibo.com/top/summary";
		String pageSource = HttpUtil.getPageSourceByClient(url, HttpUtil.UA_PC_CHROME, null, "utf-8", null, true);
		String targetElement = getTargetElement(pageSource, "pl_Srank_swf");
		Document doc = Jsoup.parse(targetElement);
		Elements elements = doc.getElementsByTag("tr");
		List<Topic> rets = elements.stream().map(ele -> {
			try {
				Element topicEle = ele.select("td.td_02").first();
				String topicName = topicEle.getElementsByTag("a").text();
				long searchNum = Long.parseLong(ele.select("td.td_03").text());
				String link = String.format("http://s.weibo.com%s", topicEle.getElementsByTag("a").attr("href"));
				Topic topic = new Topic();
				topic.setName(topicName);
				topic.setSearchNum(searchNum);
				topic.setLink(link);
				return topic;
			} catch (Exception e) {
				return null;
			}
		}).filter(Objects::nonNull).collect(Collectors.toList());
		return rets;
	}

	public class Topic {
		private String name;
		private String link;
		private long searchNum;

		public Topic() {
		}

		public Topic(String name, String link, int searchNum) {
			this.name = name;
			this.link = link;
			this.searchNum = searchNum;
		}

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}

		public String getLink() {
			return link;
		}

		public void setLink(String link) {
			this.link = link;
		}

		public long getSearchNum() {
			return searchNum;
		}

		public void setSearchNum(long searchNum) {
			this.searchNum = searchNum;
		}

		@Override
		public String toString() {
			return JSON.toJSONString(this);
		}
	}

	public List<Topic> weiBoHotWords() {
		try {
			String html = HttpUtil.getPageSourceByClient("http://s.weibo.com/top/summary?cate=total&key=all", HttpUtil.UA_PC_CHROME, WeiboUtil.getVaildWeiboCookieStore(), "utf-8", "S_Srankhot", true);
			String regex = "<p class=\\\\\"star_name\\\\\"><a href=\\\\.\\\\/weibo\\\\(?<url>.+?)\\\\\".+?list_all\\\\\">(?<name>.+?)<\\\\/a>.+?<p class=\\\\\"star_num\\\\\"><span>(?<num>.+?)<\\\\/span>";
			Pattern pattern = Pattern.compile(regex);
			Matcher matcher = pattern.matcher(html);
			List<Topic> list = new ArrayList<>();
			while (matcher.find()) {
				Topic wbSerachHot = new Topic();
				String name = matcher.group("name");
				String[] strs = name.split("\\\\u");
				String returnStr = "";
				for (int i = 1; i < strs.length; i++) {
					returnStr += (char) Integer.valueOf(strs[i].substring(0, 4), 16).intValue() + strs[i].substring(4, strs[i].length());
				}
				wbSerachHot.setName(returnStr);
				wbSerachHot.setLink("http://s.weibo.com/weibo" + matcher.group("url"));
				wbSerachHot.setSearchNum(Integer.parseInt(matcher.group("num")));
				list.add(wbSerachHot);

			}
			List<Topic> topics = weiBoStarHotSerach();
			list.addAll(topics);
			return list;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return Collections.emptyList();
	}

	public List<Topic> weiBoStarHotSerach(){
		try {
			String html = HttpUtil.getPageSourceByClient("http://s.weibo.com/top/summary?cate=total&key=person", HttpUtil.UA_PC_CHROME, WeiboUtil.getVaildWeiboCookieStore(), "utf-8", "S_Srankhot", true);
			String regex = "<p class=\\\\\"star_name\\\\\"><a href=\\\\\"\\\\/weibo\\\\/(?<url>.+?)\\\\.+?list_person\\\\\">(?<name>.+?)<\\\\/a>.+?star_num\\\\\"><span>(?<num>.+?)<\\\\/span>";
			Pattern pattern = Pattern.compile(regex);
			Matcher matcher = pattern.matcher(html);
			List<Topic> list = new ArrayList<>();
			while (matcher.find()){
				Topic wbSerachHot = new Topic();
				int num = Integer.parseInt(matcher.group("num"));
				if (num>1000000){
					String name = matcher.group("name");
					String[] strs = name.split("\\\\u");
					String returnStr = "";
					for (int i = 1; i < strs.length; i++) {
						returnStr += (char) Integer.valueOf(strs[i].substring(0, 4), 16).intValue() + strs[i].substring(4, strs[i].length());
					}
					wbSerachHot.setName(returnStr);
					wbSerachHot.setLink("http://s.weibo.com/weibo" + matcher.group("url"));
					wbSerachHot.setSearchNum(Integer.parseInt(matcher.group("num")));
					list.add(wbSerachHot);
				}
			}
			return list;
		}catch (Exception e){
			e.printStackTrace();
		}
		return Collections.emptyList();
	}




	public static void main(String[] args) throws IOException, ScriptException {
		WeiboHotTopicsParser weiboTagParser = new WeiboHotTopicsParser();
//		List<Topic> hotTopics = weiboTagParser.getHotTopics();
		List<Topic> hotTopics = weiboTagParser.weiBoHotWords();
		System.out.println(hotTopics);
	}
}
