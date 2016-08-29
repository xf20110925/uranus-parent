package com.ptb.uranus.spider.weibo;

import com.alibaba.fastjson.JSON;
import com.ptb.uranus.spider.common.utils.HttpUtil;
import com.ptb.uranus.spider.common.webDriver.WebDriverProvider;
import com.ptb.uranus.spider.weibo.bean.WeiboAccount;
import com.ptb.uranus.spider.weibo.bean.WeiboArticle;
import com.ptb.uranus.spider.weibo.bean.WeiboSearchAccount;
import com.ptb.uranus.spider.weibo.login.ChaoJiYing;
import com.ptb.uranus.spider.weibo.parse.WeiboAccountParser;
import com.ptb.uranus.spider.weibo.parse.WeiboArticleParser;
import com.ptb.uranus.spider.weibo.parse.WeiboSearchAccountParser;
import com.ptb.utils.string.RegexUtils;

import org.apache.commons.configuration.Configuration;
import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.http.client.CookieStore;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.cookie.BasicClientCookie2;
import org.openqa.selenium.By;
import org.openqa.selenium.Cookie;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.phantomjs.PhantomJSDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Base64;
import java.util.List;
import java.util.Optional;

/**
 * Created by eric on 16/1/28. <br> 微博爬取的统一对提供接口使用服务
 */
public class WeiboSpider {
	/**
	 * The Logger.
	 */
	static Logger logger = LoggerFactory.getLogger(WeiboSpider.class);
	/**
	 * The Weibo account parser.
	 */
	private WeiboAccountParser weiboAccountParser = new WeiboAccountParser();
	/**
	 * The Weibo article parse.
	 */
	private WeiboArticleParser weiboArticleParse = new WeiboArticleParser();
	/**
	 * The Weibo
	 */
	private WeiboSearchAccountParser weiboSa = new WeiboSearchAccountParser();

	private String cjyUsername;
	private String cjyPassword;
	private String cjyCodeType;


	public WeiboSpider() {
		try {
			Configuration conf = new PropertiesConfiguration("ptb.properties");
			cjyUsername = conf.getString("uranus.spider.wb.chaojiying.username");
			cjyPassword = conf.getString("uranus.spider.wb.chaojiying.password");
			cjyCodeType = conf.getString("uranus.spider.wb.chaojiying.codeType");

		} catch (ConfigurationException e) {

		}
	}

	/**
	 * Gets weibo article  by article url. <br> URl地址一般长这个模样 http://m.weibo.cn/2093778914/DlrJ04oXA
	 * <br>
	 *
	 * @param url the url  //要爬取的URL文章地址
	 * @return the weibo article static by article url //返回WeiboArticle,如果成功则isPresent()为真,否则为假
	 */
	public Optional<WeiboArticle> getWeiboArticleByArticleUrl(String url) {
		try {

			if (url.contains("weibo")) {
				int i = 0;
				while (i++ < 3) {
					WeiboArticle wbArticle = weiboArticleParse.getWeiboArticleByArticleUrl(url);
					if (wbArticle != null) {
						return Optional.of(wbArticle);
					}

				}
			}
		} catch (Exception e) {
			logger.info("get weibo article by url [{}]", url, e);
		}
		return Optional.empty();
	}


	/**
	 * 微博人物搜索
	 */
	public Optional<List<WeiboSearchAccount>> getWeiboSerachAccountByName(String accouontName) {
		try {
			return weiboSa.getWeiboSerachAccountByName(accouontName);
		} catch (Exception e) {
			logger.warn(String.format("get weibo serach accountByName", accouontName), e);
		}
		return Optional.empty();

	}

	/**
	 * Gets weibo account  by weibo id. 微博ID 一般为URl数字部分 http://m.weibo.cn/3974469906
	 *
	 * @param weiboID the weibo id
	 * @return the weibo static by media id
	 */
	public Optional<WeiboAccount> getWeiboAccountByWeiboID(String weiboID) {
		try {
			String weiboHomeUrl = String.format("http://m.weibo.cn/u/%s/", weiboID);
			return Optional.of(weiboAccountParser.getWeiboAccount(weiboHomeUrl));
		} catch (Exception e) {
			logger.warn(String.format("get weibo account by weibo id [%s]", weiboID), e);
		}
		return Optional.empty();
	}

	/**
	 * Gets weibo account by home page. 通过输入一个URL的主页获取微博的媒体信息
	 *
	 * @param homeUrl the home url
	 * @return the weibo account by home page
	 */
	public Optional<WeiboAccount> getWeiboAccountByHomePage(String homeUrl) {
		try {
			if (homeUrl.contains("weibo")) {
				if (homeUrl.contains("weibo.cn/u/")) {
					return Optional.of(weiboAccountParser.getWeiboAccount(homeUrl));
				} else {
					String weiboID = weiboAccountParser.getWeiboIDFromUserHomePage(homeUrl);
					return getWeiboAccountByWeiboID(weiboID);
				}
			}
		} catch (Exception e) {
			logger.warn(String.format("get weibo article by url [%s]", homeUrl), e);
		}
		return Optional.empty();
	}

	/**
	 * Gets recent articles by weibo id <br> 会主动访问其主页,获取ContainerID <br> 再通过ContainerID获取最近的文章 <br>
	 *
	 * @param mediaID     the media id
	 * @param lastestTime the lastest time
	 * @return the recent articles by weibo id
	 */
	public Optional<ImmutablePair<Long, List<WeiboArticle>>> getRecentArticlesByWeiboID(
			String mediaID, Long lastestTime) {
		try {
			WeiboAccount weiboAccount = weiboAccountParser.getWeiboAccount(String.format(String.format("http://m.weibo.cn/u/%s", mediaID)));
			return this.getRecentArticlesByContainerID(weiboAccount.getContainerID(), lastestTime);
		} catch (Exception e) {
			logger.warn(String.format("get weibo recent article by mediaid [%s]", mediaID), e);
		}
		return Optional.empty();

	}

	/**
	 * Gets recent articles by container id. 建议使用此接口,获取最新文章
	 *
	 * @param weiboId   the container id  //containerID
	 * @param startTime the start time
	 * @return the recent articles by container id
	 */

	public Optional<ImmutablePair<Long, List<WeiboArticle>>> getRecentArticlesByContainerID(
			String weiboId, String containerID, Long startTime) {
		try {
			ImmutablePair<Long, List<WeiboArticle>> articleList;
			articleList = weiboArticleParse.getWeiboRecentArticlesByContainerID(containerID, startTime);
			if (articleList != null) {
				return Optional.of(articleList);
			} else {
				articleList = weiboArticleParse.getWeiboRecentArticlesThroughPc(weiboId, startTime);
				if (articleList != null) {
					return Optional.of(articleList);
				}
			}
		} catch (Exception e) {
			logger.warn("get weibo recent article by weiboID {}, containerID {}", weiboId, containerID, e);
		}
		return Optional.empty();
	}

	/**
	 * Gets recent articles by container id. 建议使用此接口,获取最新文章
	 *
	 * @param containerID the container id  //containerID
	 * @param startTime   the start time
	 * @return the recent articles by container id
	 */
	public Optional<ImmutablePair<Long, List<WeiboArticle>>> getRecentArticlesByContainerID(
			String containerID, Long startTime) {
		try {
			ImmutablePair<Long, List<WeiboArticle>> articleList;
			articleList = weiboArticleParse.getWeiboRecentArticlesByContainerID(containerID, startTime);
			if (articleList != null) {
				return Optional.of(articleList);
			}
		} catch (Exception e) {
			logger.warn(String.format("get weibo recent article by containerID [%s]", containerID), e);
		}
		return Optional.empty();
	}


	public Optional<WeiboAccount> getWeiboAccountByArticleUrl(String articleUrl) {
		try {
			String mediaId = null;
			mediaId = RegexUtils.sub(".*(?:weibo\\.com|m.weibo\\.cn)/([^/]*)/(.*)", articleUrl, 0);
		   /* if(articleUrl.contains("weibo.com")){
				mediaId = RegexUtils.sub(".*(?:weibo\\.com|m.weibo\\.cn)/([^/]*)/(.*)", articleUrl, 0);
            }else if(articleUrl.contains("weibo.cn")){
                mediaId = RegexUtils.sub(".*(?:weibo\\.com|m.weibo\\.cn)/([^/]*)/(.*)", articleUrl, 1);
            }*/
			String mediaUrl = "http://m.weibo.cn/u/" + mediaId;
			Optional<WeiboAccount> weiboAccount = getWeiboAccountByHomePage(mediaUrl);
			return weiboAccount;
		} catch (Exception e) {
			logger.warn(articleUrl, e);
		}
		return Optional.empty();
	}

	public Optional<WeiboArticle> getWeiboArticleThroughPc(String articleUrl) throws Exception {
		WeiboArticle weiboArticle = weiboArticleParse.parseFromPcPageByHttpClient(articleUrl);
		if (weiboArticle == null) {
			return Optional.empty();
		} else {
			return Optional.of(weiboArticle);
		}
	}

	public Optional<WeiboArticle> getWeiboRecentArticleThroughPc(
			String weiboID, Long startTime) throws Exception {
		WeiboArticle weiboArticle = null;
		weiboArticleParse.getWeiboRecentArticlesThroughPc(weiboID, startTime);
		if (weiboArticle == null) {
			return Optional.empty();
		} else {
			return Optional.of(weiboArticle);
		}
	}

	public CookieStore login(String username, String password) {
		PhantomJSDriver mobileWebDriver = WebDriverProvider.createWebDriver(true, true, HttpUtil.UA_IPHONE6_SAFARI, WebDriverProvider.WebDriverType.None);
		mobileWebDriver.navigate().to("https://passport.weibo.cn/signin/login?entry=mweibo&res=wel&wm=3349&r=http%3A%2F%2Fm.weibo.cn%2F");
		try {
			Thread.sleep(2000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		mobileWebDriver.findElement(By.cssSelector("#loginName")).sendKeys(username);
		mobileWebDriver.findElement(By.cssSelector("#loginPassword")).sendKeys(password);
		try {
			Thread.sleep(2000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		int cnt = 0;
		int retryCnt = 5;
		while (cnt++ < retryCnt) {
			try {
				String src = mobileWebDriver.findElement(By.cssSelector("#verifyCodeImage")).getAttribute("src").substring(22);
				byte[] decode = Base64.getDecoder().decode(src);
				String s = ChaoJiYing.PostPic(cjyUsername, cjyPassword, cjyCodeType, "1005", "5", "0", "haha", decode);
				String encode = JSON.parseObject(s).getString("pic_str");
				if (StringUtils.isBlank(encode)) {
					throw new RuntimeException(String.format("验证码平台出错了信息 s:%s", s));
				}
				mobileWebDriver.findElement(By.cssSelector("#loginVCode")).clear();
				mobileWebDriver.findElement(By.cssSelector("#loginVCode")).sendKeys(encode);
			} catch (Exception e) {

			}

			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {

			}

			mobileWebDriver.findElement(By.cssSelector("#loginAction")).click();

			try {
				WebElement element = mobileWebDriver.findElement(By.cssSelector("p[data-node=\"title\"]"));
				break;
			} catch (Exception e) {
				String text = mobileWebDriver.findElement(By.cssSelector(".error-label")).getText();
				logger.error(String.format("login account username [%s] password [%s]", username, password), text);
				if (text.contains("验证码")) {
				} else {
					throw new RuntimeException(String.format("login account username [%s] password [%s] info[%s]", username, password, text));
				}
			}
		}

		CookieStore cookieStore = new BasicCookieStore();
		Cookie cookie = mobileWebDriver.manage().getCookieNamed("SUB");
		BasicClientCookie2 sub = new BasicClientCookie2(cookie.getName(), cookie.getValue());
		sub.setDomain("s.weibo.com");
		sub.setPath("/");
		BasicClientCookie2 sub1 = new BasicClientCookie2(cookie.getName(), cookie.getValue());
		sub.setDomain(".weibo.com");
		sub.setPath("/");
		BasicClientCookie2 sub2 = new BasicClientCookie2(cookie.getName(), cookie.getValue());
		sub.setDomain(".weibo.cn");
		sub.setPath("/");
		sub.setExpiryDate(cookie.getExpiry());
		cookieStore.addCookie(sub);
		cookieStore.addCookie(sub1);
		cookieStore.addCookie(sub2);
		return cookieStore;
	}

	public static void main(String[] args) {
	/*	PhantomJSDriver phantomJSDriver = weiboSpider.login("18676049373", "kknwur4026x");


		String pageSourceByClient = HttpUtil.getPageSourceByClient("http://s.weibo.com/ajax/morestatus?page=2&key=222&type=shishi&_t=0&__rnd=1472475845538", HttpUtil.UA_IPHONE6_SAFARI, cookieStore, "utf-8", "{", false);
		System.out.println(pageSourceByClient);
		try {
			FileUtils.copyFile(phantomJSDriver.getScreenshotAs(OutputType.FILE), new File("./1.png"));
		} catch (IOException e) {
			e.printStackTrace();
		}*/
	}

	public void close() {
	}
}



