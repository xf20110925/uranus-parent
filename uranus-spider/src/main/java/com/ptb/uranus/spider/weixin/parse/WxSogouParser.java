package com.ptb.uranus.spider.weixin.parse;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.jayway.jsonpath.DocumentContext;
import com.jayway.jsonpath.JsonPath;
import com.ptb.uranus.spider.common.exception.SpiderException;
import com.ptb.uranus.spider.common.utils.HttpUtil;
import com.ptb.uranus.spider.common.utils.JsonPathUtil;
import com.ptb.uranus.spider.common.webDriver.WebDriverProvider;
import com.ptb.uranus.spider.smart.utils.StringUtil;
import com.ptb.uranus.spider.weixin.WeixinSpider;
import com.ptb.uranus.spider.weixin.bean.WxArticle;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang.RandomStringUtils;
import org.apache.commons.lang.StringEscapeUtils;
import org.apache.commons.lang.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.openqa.selenium.Architecture;
import org.openqa.selenium.By;
import org.openqa.selenium.Cookie;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.phantomjs.PhantomJSDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * Created by eric on 15/12/8. 该类主要是通过SOGOU来获取微信媒体的相关信息 <br> 现在手段方式可总结为两种,一种通过访问微信的文章页面,获取微信的媒体信息,另外一种手段是通过访问SOGOU的网页方式获取文章信息<br>
 * 当前通过页面的方式是比较稳定的,通过SOGOU来访问的这种方式会受到SOGOU的单IP的访问限制问题,还没有彻底能够接解该问题的方案<br>
 */
public class WxSogouParser {
	/**
	 * The Log.
	 */
	static final Logger logger = LoggerFactory.getLogger(WxSogouParser.class);

	/**
	 * Gets last article by weixin id.
	 *
	 * @param weixinID the weixin id
	 * @return the last article by weixin id
	 */
	public String getLastArticleByWeixinID(String weixinID) {
		//主要逻辑是爬取搜狗的微信SOGOU页
		String sougouHomeUrl = "http://weixin.sogou.com/weixin?type=1&ie=utf8";
		sougouHomeUrl = HttpUtil.updateArgument(sougouHomeUrl, "query", URLEncoder.encode(weixinID));

		PhantomJSDriver webDriver = WebDriverProvider.createPcWebDriver(false, true);

		try {
			webDriver.manage().deleteAllCookies();
			webDriver.manage().addCookie(new Cookie("IPLOC", "CN1100", ".sogou.com", "/", null));
			webDriver.manage().addCookie(new Cookie("SNUID", DigestUtils.md5Hex(RandomStringUtils.random(10)), ".sogou.com", "/", null));
			webDriver.manage().addCookie(new Cookie("SUID", DigestUtils.md5Hex(RandomStringUtils.random(10)), ".sogou.com", "/", null));
			webDriver.manage().addCookie(new Cookie("SUIR", String.valueOf(System.currentTimeMillis() / 1000 - 360000), ".sogou.com", "/", null));
			webDriver.manage().addCookie(new Cookie("SUV", DigestUtils.md5Hex(RandomStringUtils.random(10)), ".sogou.com", "/", null));
			webDriver.manage().addCookie(new Cookie("sct", "2", ".sogou.com", "/", null));
			webDriver.manage().addCookie(new Cookie("weixinIndexVisited", "1", ".sogou.com", "/", null));
			webDriver.manage().addCookie(new Cookie("ABTEST", String.format("1|%d|v1", System.currentTimeMillis() / 1000 - 360000), ".sogou.com", "/", null));
			webDriver.get(sougouHomeUrl);
			Thread.sleep(1000);

			List<WebElement> items = null;
			int i = 0;
			while (i++ < 10) {
				Thread.sleep(1000);
				if (webDriver.getCurrentUrl().contains("spider")) {
					throw new SpiderException("已被限制访问");
				}
				items = webDriver.findElementsByCssSelector(".wx-rb");
				if (items != null && items.size() != 0) {
					break;
				}
			}


			WebElement weixinIDElement = items.stream().filter((webElement -> {
				return webElement.findElement(By.cssSelector("label[name='em_weixinhao']")).getText().equals(weixinID);
			})).findFirst().get();

			webDriver.navigate().to(weixinIDElement.findElement(By.cssSelector(" div.txt-box span.sp-txt a")).getAttribute("href"));
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			String url = webDriver.getCurrentUrl();
			if (url.contains("mp.weixin.qq.com/s")) {
				return url;
			} else {
				return null;
			}
		} catch (InterruptedException e) {
			e.printStackTrace();
		} finally {
			webDriver.close();
			webDriver.quit();
		}
		return null;
	}

	public List<RealTimeArticle> getHotArticles(String url) throws IOException {
		WeixinSpider weixinSpider = new WeixinSpider();
		String doc = HttpUtil.getPageByPcClient(url).asString(Charset.forName("utf-8"));
		Document parse = Jsoup.parse(doc);
		Elements elements = parse.select("div.wx-news-info2");
		List<RealTimeArticle> ret = elements.stream().map(ele -> {
			try {
				Element readNumAndTimeStatmp = ele.select("div.s-p").first();
				String readText = readNumAndTimeStatmp.ownText().replaceAll("[\\u00a0|阅读|\\+]", "").trim();
				int readNum = Integer.parseInt(readText);
				long pubTime = Long.parseLong(readNumAndTimeStatmp.select("bb[v]").attr("v"));
				String href = ele.select("a[href^=\"http://mp.weixin.qq.com/s\"]").attr("href");
				Optional<WxArticle> wxArticleOpt = weixinSpider.getArticleByUrl(href);
				return new RealTimeArticle(readNum, pubTime, wxArticleOpt.get());
			} catch (Exception e) {
				return null;
			}
		}).filter(Objects::nonNull).collect(Collectors.toList());
		return ret;
	}

	public class RealTimeArticle{
		private int readNum;
		private long pubTime;
		private WxArticle article;

		public RealTimeArticle() {
		}

		public RealTimeArticle(
				int readNum, long pubTime, WxArticle article) {
			this.readNum = readNum;
			this.pubTime = pubTime;
			this.article = article;
		}

		public int getReadNum() {
			return readNum;
		}

		public void setReadNum(int readNum) {
			this.readNum = readNum;
		}

		public long getPubTime() {
			return pubTime;
		}

		public void setPubTime(long pubTime) {
			this.pubTime = pubTime;
		}

		public WxArticle getArticle() {
			return article;
		}

		public void setArticle(WxArticle article) {
			this.article = article;
		}

		@Override
		public String toString() {
			return JSON.toJSONString(this);
		}
	}

/*	public static void main(String[] args) throws IOException {
		WxSogouParser wxSogouParser = new WxSogouParser();
		*//*while (true) {
			String rmrbwx = wxSogouParser.getLastArticleByWeixinID("rmrbwx");
			System.out.println(rmrbwx);
		}*//*

		List<RealTimeArticle> hotArticles = wxSogouParser.getHotArticles("http://weixin.sogou.com/pcindex/pc/pc_4/1.html");
		System.out.println(hotArticles);


	}*/

	/**
	 * sogou文章列表
	 * @param pageContext
	 * @return
     */
	public List<String> getWxSogo(String pageContext){
		String regex2= "msgList = \\{.*\\{.*comm_msg_info.*\\{.*\\}\\}]}";
		Pattern pattern = Pattern.compile(regex2);
		Matcher m = pattern.matcher(pageContext);
		String context = null;
		while (m.find()){
			context = m.group(0).replace("msgList =","");
		}
		DocumentContext parse = JsonPath.parse(context);
		List<String> urls = parse.read("$.list[*]..content_url", List.class);
		List<String> collect = urls.stream().map(StringEscapeUtils::unescapeHtml).collect(Collectors.toList());
		return collect;

	}

	/**
	 * 提取正文
     */

	public  String getWxContext(List<String> list,String source,String txt){
		list.forEach(li ->{
			logger.info("The current crawl:"+li);
			if (li.contains("/s?timestamp=")){
				String pageSource = HttpUtil.getPageSourceByClient("http://mp.weixin.qq.com"+li,null,null,"utf-8","rich_media_content ",true);
				String document =source+Jsoup.parseBodyFragment(pageSource).text();
				new WxSogouParser().method2(txt,document);
			}
		});
		return null;
	}


	public static void method2(String file, String conent) {
		BufferedWriter out = null;
		try {
			out = new BufferedWriter(new OutputStreamWriter(
					new FileOutputStream(file, true)));
			out.write(conent+"\r\n");
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				out.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

}
