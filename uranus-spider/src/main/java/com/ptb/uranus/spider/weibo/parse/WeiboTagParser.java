package com.ptb.uranus.spider.weibo.parse;

import com.alibaba.fastjson.JSONObject;
import com.ptb.uranus.spider.common.utils.HttpUtil;

import org.apache.http.client.CookieStore;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.cookie.BasicClientCookie2;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
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
	List<String> failLinks = new ArrayList<>();

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

	private List<String> getAllLinks(int pageNum, String link) {
		//http://d.weibo.com/1087030002_2975_1003_0?page=2
		List<String> links = new ArrayList<>();
		for (int i = 1; i <= pageNum; i++) {
			links.add(link.replaceAll("page=\\d+", String.format("page=%d", i)));
		}
		return links;
	}

	public List<JSONObject> getAccounts(
			String url, String tag) throws IOException, ScriptException {
		String targetElement = getTargetElement(url);
		Document doc = Jsoup.parse(targetElement);
		Elements elements = doc.select("ul.follow_list > li");
		List<JSONObject> weiboAccounts = elements.stream().map(ele -> parseAccount(ele, tag)).collect(Collectors.toList());
		return weiboAccounts;
	}

	private JSONObject parseAccount(Element ele, String firstTag) {
		/*WeiboAccount weiboAccount = new WeiboAccount();
		weiboAccount.setNickName(ele.select("a.S_txt1").text());
		Element dynamic = ele.select("div.info_connect").first();
		weiboAccount.setAttNum(Integer.parseInt(dynamic.select("span:eq(0)").select("em.count").text()));
		weiboAccount.setFansNum(Integer.parseInt(dynamic.select("span:eq(1)").select("em.count").text().replace("万", "10000")));
		weiboAccount.setMblogNum(Integer.parseInt(dynamic.select("span:eq(2)").select("em.count").text()));
		weiboAccount.setActivePlace(ele.select("div.info_add > span").text());
		weiboAccount.setDesc(ele.select("div.info_intro > span").text());
		weiboAccount.setHeadImg(persionNalEle.select("a.S_txt1").attr("href"));*/
		JSONObject json = new JSONObject();
		Elements persionNalEle = ele.select("div.info_name.W_fb.W_f14");
		Matcher matcher = Pattern.compile("id=(\\d+).*").matcher(persionNalEle.select("a.S_txt1").attr("usercard"));
		if (matcher.find()) {
			json.put("pmid", matcher.group(1));
		}

		/*String secondTag = ele.select("div.info_relation").text();
		HashSet<String> tags = new HashSet<>();
		tags.add(firstTag);
		if (StringUtils.isNotBlank(secondTag))
			tags.add(secondTag.split("：")[1]);
		json.put("tags", tags);*/
		json.put("tag", firstTag);

		return json;
	}

	private static CookieStore getVaildWeiboCookieStore() {
		CookieStore cookieStore = new BasicCookieStore();
		BasicClientCookie2 sub = new BasicClientCookie2("SUB", UUID.randomUUID().toString());
		sub.setDomain("d.weibo.com");
		cookieStore.addCookie(sub);
		return cookieStore;
	}

	public void run(String url, String tag, String path) throws IOException, ScriptException {

		String targetEle = getTargetElement(url);
		int pageNum = getPageNum(targetEle);
		System.out.println(String.format("共%d页", pageNum));
		List<String> allLinks = getAllLinks(pageNum, url);
		allLinks.stream().forEach(link -> {
			try {
				List<JSONObject> accounts = getAccounts(link, tag);
				System.out.println(String.format("爬取%s成功", link));
				write(accounts, path);
				Thread.sleep(new Random().nextInt(4));
			} catch (Exception e) {
				System.out.println(String.format("fail link -> %s \t failLinks -> %s", link, failLinks));
				failLinks.add(link);
			}
		});
		if (!failLinks.isEmpty()) {
			failLinks.stream().forEach(link -> {
				List<JSONObject> accounts = null;
				try {
					accounts = getAccounts(link, tag);
					write(accounts, path);
				} catch (Exception e) {
					e.printStackTrace();
				}
			});
		}
	}

	private void write(List<JSONObject> weiboAccounts, String path) {
		Path savePath = Paths.get(path);
		weiboAccounts.stream().forEach(weiboAccount -> {
			try {
				Files.write(savePath, (String.join(",", weiboAccount.getString("pmid"), weiboAccount.getString("tag")) + "\r\n").getBytes(), StandardOpenOption.CREATE, StandardOpenOption.APPEND, StandardOpenOption.SYNC);
			} catch (IOException e) {
				e.printStackTrace();
			}
		});
	}

	public static void main(String[] args) throws IOException, ScriptException {
		WeiboTagParser weiboTagParser = new WeiboTagParser();
		String tag = "作家";
		String path = String.format("g:/%s.txt", tag);
		weiboTagParser.run("http://d.weibo.com/1087030002_2975_2003_0?page=1", tag, path);
	}
}
