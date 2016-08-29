package com.ptb.uranus.asistant.web.server;

import com.ptb.uranus.asistant.web.dao.WbAccountDao;
import com.ptb.uranus.asistant.web.model.RWbAcctMeta;
import com.ptb.uranus.spider.common.utils.HttpUtil;
import com.ptb.uranus.spider.common.webDriver.WebDriverProvider;

import org.apache.commons.lang.math.RandomUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.Cookie;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.phantomjs.PhantomJSDriver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.annotation.PostConstruct;

/**
 * Created by eric on 16/8/29.
 */
@Component
public class WbAccountService {
	@Autowired
	WbAccountDao wbAccountDao;
	Map<String,PhantomJSDriver> driverMap;

	public WbAccountService() {
		driverMap = new HashMap<>();
	}

	@PostConstruct
	private void init() {
		List<RWbAcctMeta> allWbAccount = wbAccountDao.getAllWbAccount();
		for (RWbAcctMeta rWbAcctMeta : allWbAccount) {
			PhantomJSDriver phantomJSDriver = WebDriverProvider.createWebDriver(true, true, HttpUtil.UA_IPHONE6_SAFARI, WebDriverProvider.WebDriverType.Http);
			phantomJSDriver.manage().deleteAllCookies();
			Cookie cookie = new Cookie("SUB", rWbAcctMeta.getCookie(), ".weibo.cn", "/", null, false);
			phantomJSDriver.manage().addCookie(cookie);
			phantomJSDriver.navigate().to("http://m.weibo.cn");
			WebElement element = phantomJSDriver.findElement(By.className("media-main"));
			if(element != null) {
				rWbAcctMeta.setValid(true);
				wbAccountDao.addWeiboCount(rWbAcctMeta);
			}else{
				rWbAcctMeta.setValid(false);
				wbAccountDao.addWeiboCount(rWbAcctMeta);
			}
		}
	}

	public void addWeiboCount(String username, String password) {
		wbAccountDao.addWeiboCount(username,password);
	}

	public void delWeiboCount(String username) {
		wbAccountDao.delWeiboCount(username);
	}

	public RWbAcctMeta getValidWbAccount() {
		List<RWbAcctMeta> rWbAcctMetas =  wbAccountDao.getAllWbAccount();
		List<RWbAcctMeta> collect = rWbAcctMetas.stream().filter(rWbAcctMeta -> rWbAcctMeta.isValid()).collect(Collectors.toList());
		if(collect.size() == 0) {
			return null;
		}
		return collect.get(RandomUtils.nextInt(collect.size() - 1));
	}

	public void setWeiboVolidatCode(String username, String volidateCookie) {

	}



	public RWbAcctMeta getInValidWbAccount() {
		List<RWbAcctMeta> rWbAcctMetas =  wbAccountDao.getAllWbAccount();
		List<RWbAcctMeta> collect = rWbAcctMetas.stream().filter(rWbAcctMeta -> !rWbAcctMeta.isValid()).collect(Collectors.toList());
		if(collect.size() == 0) {
			return null;
		}

		RWbAcctMeta rWbAcctMeta = collect.get(RandomUtils.nextInt(collect.size() - 1));

		PhantomJSDriver phantomJSDriver = driverMap.get(rWbAcctMeta.getUsername());
		if(phantomJSDriver != null) {
			phantomJSDriver.navigate().to("m.weibo.cn");
		}else{
			phantomJSDriver = WebDriverProvider.createWebDriver(true, true, HttpUtil.UA_IPHONE6_SAFARI, WebDriverProvider.WebDriverType.Http);
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		phantomJSDriver.navigate().to("https://passport.weibo.cn/signin/login?entry=mweibo&res=wel&wm=3349&r=http%3A%2F%2Fm.weibo.cn%2F");
		phantomJSDriver.findElement(By.cssSelector("#loginName")).sendKeys("15652750943");
		phantomJSDriver.findElement(By.cssSelector("#loginPassword")).sendKeys("3163504123");
		phantomJSDriver.findElement(By.cssSelector("#loginAction")).click();

		String screenshotAs = phantomJSDriver.getScreenshotAs(OutputType.BASE64);
		rWbAcctMeta.setVolidateCode(screenshotAs);
		return rWbAcctMeta;
	}
}
