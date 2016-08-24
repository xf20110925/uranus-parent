package com.ptb.uranus.spider.demo;

import com.ptb.uranus.spider.common.webDriver.WebDriverProvider;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.junit.Test;
import org.openqa.selenium.Cookie;
import org.openqa.selenium.phantomjs.PhantomJSDriver;

import java.io.IOException;
import java.util.UUID;

/**
 * Created by eric on 16/7/23.
 */
public class TestSpider {
	@Test
	public void test() throws IOException, InterruptedException {
		PhantomJSDriver webdiver = WebDriverProvider.createPcWebDriver(false,true);
		webdiver.manage().addCookie(new Cookie("SUB", UUID.randomUUID().toString(), ".weibo.com", "/", null, false));
		webdiver.get("http://d.weibo.com/102803_ctg1_1199_-_ctg1_1199#");
		Thread.sleep(3000);

		String page = webdiver.getPageSource();
		Elements select = Jsoup.parse(page).
				select("#Pl_Discover_NewMixTab__3  div.WB_innerwrap a span.text");
		for (Element element : select) {
			System.out.println(element.text().replaceAll("\\t",""));
		}

	}
}
