package com.ptb.uranus.spider.common.utils;

import com.ptb.utils.exception.PTBException;

import org.apache.commons.configuration.Configuration;
import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;
import org.apache.http.client.CookieStore;
import org.apache.http.client.fluent.Content;
import org.apache.http.client.fluent.Executor;
import org.apache.http.client.fluent.Request;
import org.apache.http.client.fluent.Response;
import org.apache.http.entity.ContentType;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.cookie.BasicClientCookie2;
import org.jsoup.helper.StringUtil;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by eric on 15/11/5. 这个工具类封装了常用的通过HTTP操作的一些工具 <br> 1.包括获取一个新的HTTP prxoy地址 <br>
 * 2.Cookie转换工具 <br> 3.
 */
public class HttpUtil {
	static final int TryNum = 3;
	static int ConnectTimeout = 3000;
	static int SocketTimeout = 10000;
	static int ProxyConnectTimeout = 3000;
	static int ProxySocketTimeout = 3000;

	/**
	 * The constant proxyServerAddress. 去哪里请求代理服务地址
	 */
	private static String proxyServerAddress;

	static {
		//读取爬虫相关的配置,其中包括手机调度器的地址和获取代理的地址
		try {
			Configuration conf = new PropertiesConfiguration("ptb.properties");
			proxyServerAddress = conf.getString("uranus.spider.proxy.server.address", "http://127.0.0.1:8080/proxy/get");
		} catch (ConfigurationException e) {

		}
	}

	/**
	 * The constant UA_IPHONE6_SAFARI.
	 */
	public final static String UA_IPHONE6_SAFARI = "Mozilla/5.0 (iPhone; CPU iPhone OS 7_0 like Mac OS X; en-us) AppleWebKit/537.51.1 (KHTML, like Gecko) Version/7.0 Mobile/11A465 Safari/9537.53";
	/**
	 * The constant UA_PC_CHROME.
	 */
	public final static String UA_PC_CHROME = "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_10_4) AppleWebKit/536.36 (KHTML, like Gecko) Chrome/45.0.2452.101 Safari/536.36";

	public static final String UA_PC_WINDOW = "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/39.0.2171.95 Safari/537.36 MicroMessenger/6.5.2.501 NetType/WIFI WindowsWechat";

	/**
	 * Gets proxy <br> 从爬虫服务器获取一个代理地址,
	 *
	 * @return the proxy 地址的格式为[schama]://[ip]:[port]
	 */
	static public String getProxy(String type) {
		String url = String.format(proxyServerAddress + "?type=%s", type);
		try {
			String s = Request.Get(proxyServerAddress).execute().returnContent().toString();
			return s == null || s.length() < 3 ? null : s;
		} catch (IOException e) {
			return null;
		}
	}

	/**
	 * 判断一个字符串content-type 类型,用来接收HTTP返回数据,并转换成合适的字符串编码
	 *
	 * @param contentType the content type string
	 * @return the string   gbk / UTF-8
	 */
	static public String ResponseCharset(String contentType) {
		if (contentType.contains("gbk")) {
			return "gbk";
		} else {
			return "UTF-8";
		}
	}

	/**
	 * Convert cookie as string map. <br> 将一个网络传输中的标准Cookie字符串,转换成MAP的K,V形式
	 *
	 * @param cookieStr the cookie str
	 * @return the map
	 */
	static public Map<String, String> convertCookieAsString(String cookieStr) {
		String[] cookies = cookieStr.split("; ");
		Map<String, String> cookieMap = new HashMap<String, String>();
		for (String cookie : cookies) {
			String[] kv = cookie.split("=");
			String key = kv[0];
			String value = "";
			if (kv.length >= 2) {
				value = kv[1];
			}
			cookieMap.put(key, value);
		}
		return cookieMap;
	}

	/**
	 * 将Kv cookiieMap 对应 转换成可以供http-client使用cookie
	 *
	 * @param cookieMap the cookie map
	 * @param domain    the domain
	 * @return the cookie store
	 */
	static public CookieStore getCookieStore(Map<String, String> cookieMap, String domain) {
		CookieStore cookieStore = new BasicCookieStore();

		for (Map.Entry<String, String> entry : cookieMap.entrySet()) {
			BasicClientCookie2 sub = new BasicClientCookie2(entry.getKey(), entry.getValue());
			sub.setDomain(domain);
			cookieStore.addCookie(sub);
		}

		return cookieStore;
	}

	/**
	 * 将cookie string 对应 转换成可以供http-client使用cookie
	 *
	 * @param cookies the cookies
	 * @param domain  the domain
	 * @return the cookie store
	 */
	static public CookieStore getCookieStore(String cookies, String domain) {
		CookieStore cookieStore = new BasicCookieStore();
		Map<String, String> cookieMap = convertCookieAsString(cookies);

		for (Map.Entry<String, String> entry : cookieMap.entrySet()) {
			BasicClientCookie2 sub = new BasicClientCookie2(entry.getKey(), entry.getValue());
			sub.setDomain(domain);
			cookieStore.addCookie(sub);
		}
		return cookieStore;
	}

	/**
	 * 更改一个URL中对应的给定的参数KEY 将其值改成对应的VALUE值
	 *
	 * @param routerUrl the router url
	 * @param argKey    the arg key
	 * @param argValue  the arg value
	 * @return the string
	 */
	public static String updateArgument(String routerUrl, String argKey, String argValue) {
		return routerUrl.replaceFirst(String.format("%s=[^&^$]*&?", argKey), "").replaceFirst("\\?", String.format("?%s=%s&", argKey, argValue));
	}


	/**
	 * Gets page by client. 先尝试通过代理去访问一个地址,如果失败则通过本地去访问地址,共尝试3次,如果成功返回对应的RESPONSE对象,如果失败则抛出异常信息
	 *
	 * @param url         the url 要访问的URL
	 * @param ua          the ua  所使用的UserAgent
	 * @param cookieStore the cookie store 所使用的cookieStore
	 * @return the page by client   //对应页面的Response
	 * @throws IOException the io exception 访问失败会抛出异常
	 */
	private static Response getPageByClient(
			String url, String ua, CookieStore cookieStore) throws IOException {
		return getPageByClient(url, ua, cookieStore, false);
	}

	private static Response getPageByClient(
			String url, String ua, CookieStore cookieStore, Boolean useProxy) throws IOException {
		int retryNum = TryNum;
		while (retryNum-- > 0) {
			try {
				try {
					if (useProxy == true) {
						String proxy = Request.Get(proxyServerAddress).execute().returnContent().toString();
						return Executor.newInstance().use(cookieStore).execute(Request.Get(url).userAgent(ua).viaProxy(proxy).connectTimeout(ConnectTimeout).socketTimeout(SocketTimeout));
					}

				} catch (Exception e) {
				}
				return Executor.newInstance().use(cookieStore).execute(Request.Get(url).userAgent(ua).connectTimeout(ConnectTimeout).socketTimeout(SocketTimeout));
			} catch (Exception e) {

			}
		}
		throw new PTBException(String.format("访问网页失败 url[%s] ua[%s] userProxy[%s]", url, ua, useProxy));
	}


	public static String getPageSourceByClient(
			String url, String ua, CookieStore cookieStore, String charset, String nessaryString,
			boolean useProxy) {
		int retryNum = TryNum;
		while (retryNum-- > 0) {
			try {
				try {
					if (useProxy) {
						String proxy = getProxy("http");
						String s = Executor.newInstance().use(cookieStore).execute(Request.Get(url).userAgent(ua).viaProxy(proxy).connectTimeout(ConnectTimeout).socketTimeout(SocketTimeout)).returnContent().asString(Charset.forName(charset));
						if (StringUtil.isBlank(nessaryString)) {
							return s;
						}
						if (s.contains(nessaryString)) {
							return s;
						} else {
							throw new PTBException(String.format("Error visit page [%s] via proxy [%s] no contains [%s]", url, proxy, nessaryString));
						}
					}
				} catch (Exception e) {
				}

				String s = Executor.newInstance().use(cookieStore).execute(Request.Get(url).userAgent(ua).connectTimeout(ConnectTimeout).socketTimeout(SocketTimeout)).returnContent().asString(Charset.forName(charset));
				if (StringUtil.isBlank(nessaryString)) {
					return s;
				}

				if (s.contains(nessaryString)) {
					return s;
				}
			} catch (Exception e) {

			}
		}
		throw new PTBException(String.format("访问网页失败 url[%s] ua[%s] userProxy[%s] charset[%s] nessaryString[%s]", url, ua, useProxy, charset, nessaryString));
	}

	public static String getPageSourceByClient(
			String url, String ua, CookieStore cookieStore, String charset, String nessaryString) {
		return getPageSourceByClient(url, ua, cookieStore, charset, nessaryString, false);
	}


	public static String getPageSourceByClient(String url) {
		return getPageSourceByClient(url, "", null, "utf-8", null);
	}

	public static String getPageSourceByClient(String url, String ua, String charset) {
		return getPageSourceByClient(url, ua, null, charset, null);
	}


	/**
	 * Gets page by pc client.
	 *
	 * @param url the url 要访问的地址
	 * @return the page by pc client  返回的RESPONSE
	 * @throws IOException the io exception
	 */
	public static Content getPageByPcClient(String url) throws IOException {
		return getPageByClient(url, UA_PC_CHROME, null).returnContent();
	}

	/**
	 * Gets page by mobile client.
	 *
	 * @param url the url
	 * @return the page by mobile client 需要通过移动端访问的地址
	 * @throws IOException the io exception
	 */
	public static Content getPageByMobileClient(String url) throws IOException {
		return getPageByClient(url, UA_IPHONE6_SAFARI, null).returnContent();
	}

	public static String postByPageClient(
			String url, String params, ContentType paramsType, String ua,CookieStore cookieStore) {
		try {
			String content = Executor.newInstance().use(cookieStore).execute(Request.Post(url).bodyString(params, paramsType).userAgent(ua).connectTimeout(ConnectTimeout).socketTimeout(SocketTimeout)).returnContent().asString();
			return content;
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}


	public static String postByPcClient(String url, String params, ContentType paramsType) {
		return postByPageClient(url, params, paramsType, UA_PC_CHROME,null);
	}

	public static String postByMobileClient(String url, String params, ContentType paramsType) {
		return postByPageClient(url, params, paramsType, UA_IPHONE6_SAFARI,null);
	}
}
