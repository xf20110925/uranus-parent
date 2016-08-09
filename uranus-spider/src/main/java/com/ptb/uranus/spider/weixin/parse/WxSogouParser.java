package com.ptb.uranus.spider.weixin.parse;

import com.alibaba.fastjson.JSON;
import com.ptb.uranus.spider.common.exception.SpiderException;
import com.ptb.uranus.spider.common.utils.HttpUtil;
import com.ptb.uranus.spider.common.webDriver.WebDriverProvider;
import com.ptb.uranus.spider.weixin.WeixinSpider;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang.RandomStringUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.openqa.selenium.By;
import org.openqa.selenium.Cookie;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.phantomjs.PhantomJSDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.util.List;
import java.util.Objects;
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
				href = weixinSpider.convertSogouWeixinUrlToRealUrl(href);
				return new RealTimeArticle(href, readNum, pubTime);
			} catch (Exception e) {
				return null;
			}
		}).filter(Objects::nonNull).collect(Collectors.toList());
		return ret;
	}

	class RealTimeArticle{
		private String url;
		private int readNum;
		private long pubTime;
		private String tag;

		public RealTimeArticle() {
		}

		public RealTimeArticle(String url, int readNum, long pubTime) {
			this.url = url;
			this.readNum = readNum;
			this.pubTime = pubTime;
		}

		public String getUrl() {
			return url;
		}

		public void setUrl(String url) {
			this.url = url;
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

		public String getTag() {
			return tag;
		}

		public void setTag(String tag) {
			this.tag = tag;
		}

		@Override
		public String toString() {
			return JSON.toJSONString(this);
		}
	}

	public static void main(String[] args) throws IOException {
		WxSogouParser wxSogouParser = new WxSogouParser();
		/*while (true) {
			String rmrbwx = wxSogouParser.getLastArticleByWeixinID("rmrbwx");
			System.out.println(rmrbwx);
		}*/
		List<RealTimeArticle> hotArticles = wxSogouParser.getHotArticles("http://weixin.sogou.com/pcindex/pc/pc_4/1.html");
		System.out.println(hotArticles);


	}

}
