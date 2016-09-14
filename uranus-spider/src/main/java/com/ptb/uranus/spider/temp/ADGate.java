package com.ptb.uranus.spider.temp;

import com.alibaba.fastjson.JSON;
import com.ptb.uranus.spider.common.utils.HttpUtil;

import org.apache.commons.io.FileUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.File;
import java.io.IOException;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Created by eric on 16/9/14.
 */
public class ADGate {
	public static void crawleList(String filePath) throws IOException {
		String listUrlTemplate = "http://creative.adquan.com/list-recommend/?page=%d";
		Set<String> pageList = new HashSet<>();
		for (int i = 0; i < 114; i++) {
			try {
				String pageSourceByClient = HttpUtil.getPageSourceByClient(String.format(listUrlTemplate, i));
				Document parse = Jsoup.parse(pageSourceByClient);
				parse.setBaseUri("http://creative.adquan.com/");
				Elements select = parse.select("a.title_img");
				for (Element element : select) {
					String href = element.attr("href");
					pageList.add("http://creative.adquan.com/" + href);
				}
				System.out.println(i);
			} catch (Exception e) {
				System.out.println(i);
				e.printStackTrace();
			}
		}
		FileUtils.writeLines(new File(filePath), pageList);
	}


	public static class ADGatePage {
		String title;
		String postTime;
		int read;
		int comment;
		int favorate;
		String productName;
		String agent;
		String ServiceObj;
		String desc;
		List<String> video;

		public String getAgent() {
			return agent;
		}

		public void setAgent(String agent) {
			this.agent = agent;
		}

		public String getTitle() {
			return title;
		}

		public void setTitle(String title) {
			this.title = title;
		}

		public String getPostTime() {
			return postTime;
		}

		public void setPostTime(String postTime) {
			this.postTime = postTime;
		}

		public int getRead() {
			return read;
		}

		public void setRead(int read) {
			this.read = read;
		}

		public int getComment() {
			return comment;
		}

		public void setComment(int comment) {
			this.comment = comment;
		}

		public int getFavorate() {
			return favorate;
		}

		public void setFavorate(int favorate) {
			this.favorate = favorate;
		}

		public String getProductName() {
			return productName;
		}

		public void setProductName(String productName) {
			this.productName = productName;
		}

		public String getServiceObj() {
			return ServiceObj;
		}

		public void setServiceObj(String serviceObj) {
			ServiceObj = serviceObj;
		}


		public String getDesc() {
			return desc;
		}

		public void setDesc(String desc) {
			this.desc = desc;
		}

		public List<String> getVideo() {
			return video;
		}

		public void setVideo(List<String> video) {
			this.video = video;
		}

		@Override
		public String toString() {
			return JSON.toJSONString(this);
		}
	}

	public static ADGatePage parseAdGatePage(String page) {
		ADGatePage adGatePage = new ADGatePage();
		Document parse = Jsoup.parse(page);
		adGatePage.setTitle(parse.select(".details_title").text());
		adGatePage.setPostTime(parse.select(".fenxiang").text());
		adGatePage.setRead(Integer.parseInt(parse.select(".look_num .span_01").text()));
		adGatePage.setComment(Integer.parseInt(parse.select(".look_num .span_02").text()));
		adGatePage.setFavorate(Integer.parseInt(parse.select(".look_num .span_03").text()));
		adGatePage.setProductName(parse.select("div.overview > ul > li:nth-child(2) > h4 > span").text());
		adGatePage.setAgent(parse.select("div.overview > ul > li:nth-child(3) > h4 > span").text());
		adGatePage.setServiceObj(parse.select("div.overview > ul > li:nth-child(4) > h4 > span").text());

		adGatePage.setDesc(parse.select(".deta_inner > span").text());
		Elements select = parse.select("iframe[src*=\"v.qq.com\"]");
		List<String> src1 = select.stream().map(element -> {
			String src = element.attr("src");
			return src;
		}).collect(Collectors.toList());
		adGatePage.setVideo(src1);
		return adGatePage;
	}

	public static void crawlePageContent(String filePath,String output) throws IOException {
		List<String> list = FileUtils.readLines(new File(filePath));
		List<String> outputs = new LinkedList<>();

		for (String s : list) {
			String pageSourceByClient = HttpUtil.getPageSourceByClient(s);
			ADGatePage adGatePage = parseAdGatePage(pageSourceByClient);
			outputs.add(adGatePage.toString());
		}
		FileUtils.writeLines(new File(output),outputs);
	}

	public static void main(String[] args) throws IOException {
		crawleList("./pageContent.txt");
		crawlePageContent("./pageContent.txt","outputFile.txt");

	}
}
