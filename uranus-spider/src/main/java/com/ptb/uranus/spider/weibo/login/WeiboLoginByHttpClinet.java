package com.ptb.uranus.spider.weibo.login;

import org.apache.commons.lang.StringUtils;
import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.cookie.Cookie;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Base64;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class WeiboLoginByHttpClinet {

	public static RequestConfig requestConfig = RequestConfig.custom().setSocketTimeout(30000).setConnectTimeout(30000).build();
	public static BasicCookieStore cookieStore = new BasicCookieStore();
	public static CloseableHttpClient httpClient = HttpClients.custom().setDefaultCookieStore(cookieStore).build();
	public static String callUrl = String.format("http://login.sina.com.cn/sso/prelogin.php?entry=weibo&callback=sinaSSOController.preloginCallBack&su=&rsakt=mod&client=ssologin.js(v1.4.18)&_=", System.currentTimeMillis());
	public static String ssologinUrl = "http://login.sina.com.cn/sso/login.php?client=ssologin.js(v1.4.18)";

	public static void main(String[] args) {
		WeiboLoginByHttpClinet loginClinet = new WeiboLoginByHttpClinet();
		String username = "15010911620";
		String password = "man2803797315";
		WeiboLoginByHttpClinet weiboLoginByHttpClinet = new WeiboLoginByHttpClinet();
		weiboLoginByHttpClinet.login(username, password);
		String url = "http://s.weibo.com/weibo/%25E9%25A9%25AC%25E8%2593%2589%25E5%25BE%25AE%25E4%25BF%25A1%25E6%259C%258B%25E5%258F%258B%25E5%259C%2588%25E6%2588%25AA%25E5%259B%25BE&page=2";
		String html = new WeiboLoginByHttpClinet().getHtml(loginClinet.httpClient, url, loginClinet.getCookiesString());
		System.out.println(html);
	}


	/**
	 * 登陆
	 */

	public BasicCookieStore login(String username, String password) {

		/**
		 * 第1步：http://login.sina.com.cn/sso/prelogin.php?entry=weibo&callback=sinaSSOController.preloginCallBack&su=Njc0NjEzNDM4JTQwcXEuY29t&rsakt=mod&checkpin=1&client=ssologin.js(v1.4.18)&_=
		 * 获取servertime ，pcid，pubkey，rsakv，nonce
		 * */
		String html = getHtml(httpClient, callUrl, getCookiesString());
		String servertime = StringUtils.substringBetween(html, "servertime\":", ",");
		String pcid = StringUtils.substringBetween(html, "pcid\":\"", "\",\"");
		String pubkey = StringUtils.substringBetween(html, "pubkey\":\"", "\",\"");
		String rsakv = StringUtils.substringBetween(html, "rsakv\":\"", "\"");
		String nonce = StringUtils.substringBetween(html, "nonce\":\"", "\"");
		/**
		 * 第2步： 1 用户名需要 base 64 ecode
		 *  2 密码需要加密
		 * */
		String su = encodeAccount(username);
		String sp = ssorskPassword(pubkey, servertime, nonce, password);
		/**
		 * 第3步 ：提交参数进行登陆
		 * 获取 校验 url
		 * */
		Map<String, String> params = new HashMap<String, String>();
		params.put("entry", "weibo");
		params.put("gateway", "1");
		params.put("from", "");
		params.put("savestate", "7");
		params.put("useticket", "1");
		params.put("pagerefer", "");
		params.put("vsnf", "1");
		params.put("su", su.trim());
		params.put("service", "miniblog");
		params.put("servertime", servertime);
		params.put("nonce", nonce);
		params.put("pwencode", "rsa2");
		params.put("rsakv", rsakv);
		params.put("sp", sp.trim());
		params.put("sr", "1920*1080");
		params.put("encoding", "UTF-8");
		params.put("prelt", "35");
		params.put("url", "http://weibo.com/ajaxlogin.php?framelogin=1&callback=parent.sinaSSOController.feedBackUrlCallBack");
		params.put("returntype", "META");
		html = post(httpClient, ssologinUrl, params, "utf-8", getCookiesString());
		String url = StringUtils.substringBetween(html, "location.replace('", "');");
		// url ="http://weibo.com/ajaxlogin.php?framelogin=1&callback=parent.sinaSSOController.feedBackUrlCallBack&sudaref=weibo.com";
		/**
		 * 第4步： 访问URL 操作后如果看到自己的uid 说明登陆成功，就可以采集了
		 */
		html = getHtml(httpClient, url, getCookiesString());
		System.out.println(html);
		System.out.println(getCookiesString());
		return cookieStore;

	}

	/**
	 * 加密密码
	 */
	private String ssorskPassword(String pubkey, String serverTime, String nonce, String password) {
		return PasswordUtil4Sina.getPassEncoding(pubkey, serverTime, nonce, password);
	}

	/**
	 * base64 ecode 用户名
	 */
	private String encodeAccount(String account) {
		String userName = "";
		userName = Base64.getEncoder().encodeToString(account.getBytes());
		return userName;
	}

	public String post(CloseableHttpClient httpClient, String url, Map<String, String> params, String charset, String cookie) {
		String useCharset = charset;
		if (charset == null) {
			useCharset = "utf-8";
		}
		String result = null;
		try {
			HttpPost httpPost = new HttpPost(url);
			fillHeaderWithCookie(httpPost, cookie);
			List<NameValuePair> nvps = new ArrayList<NameValuePair>();
			if (params != null) {
				for (String key : params.keySet()) {
					nvps.add(new BasicNameValuePair(key, params.get(key)));
				}
				httpPost.setEntity(new UrlEncodedFormEntity(nvps, "utf-8"));
			}
			httpPost.setConfig(requestConfig);
			CloseableHttpResponse response = httpClient.execute(httpPost);
			try {
				HttpEntity entity = response.getEntity();
				result = EntityUtils.toString(entity, useCharset);
			} finally {
				response.close();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}

	public String getHtml(CloseableHttpClient httpClient, String url, String cookie) {
		HttpClientContext context = HttpClientContext.create();
		HttpGet httpGet = new HttpGet(url);
		fillHeaderWithCookie(httpGet, cookie);
		httpGet.setConfig(requestConfig);
		String chartset = null;
		String result = null;
		try {
			CloseableHttpResponse response = httpClient.execute(httpGet, context);
			try {
				Header heads[] = response.getAllHeaders();
				for (Header header : heads) {
					if (header.getValue().toLowerCase().contains("charset")) {
						Pattern pattern = Pattern.compile("charset=[^\\w]?([-\\w]+)");
						Matcher matcher = pattern.matcher(header.getValue());
						if (matcher.find()) {
							chartset = matcher.group(1);
						}
					}
				}
				if (chartset == null) {
					chartset = "utf-8";
				} else {
					if (chartset.equals("gbk2312")) {
						chartset = "gbk";
					}
				}
				InputStream inputStream = response.getEntity().getContent();
				result = inputStream2String(inputStream, chartset);
			} finally {
				response.close();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}

	public String getCookiesString() {
		List<Cookie> cookies = cookieStore.getCookies();
		StringBuffer sb = new StringBuffer();
		if (cookies != null) {
			for (Cookie c : cookies) {
				sb.append(c.getName() + "=" + c.getValue() + ";");
			}
		}
		return sb.toString();
	}

	private void fillHeaderWithCookie(HttpRequestBase httpBase, String cookie) {
		httpBase.setHeader("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64; rv:41.0) Gecko/20100101 Firefox/41.0");
		httpBase.setHeader("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8");
		httpBase.setHeader("Accept-Language", "zh-CN,zh;q=0.8,en-us;q=0.8,en;q=0.6");
		httpBase.setHeader("Accept-Encoding", "gzip, deflate,sdch");
		httpBase.setHeader("Connection", "keep-alive");
		httpBase.setHeader("Cache-Control", "max-age=0");
		httpBase.setHeader("Cookie", cookie);
	}

	private String inputStream2String(InputStream is, String charset) {
		String temp = null;
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		try {
			int i = -1;
			while ((i = is.read()) != -1) {
				baos.write(i);
			}
			temp = baos.toString(charset);
			if (temp.contains("???") || temp.contains("�")) {
				Pattern pattern = Pattern.compile("<meta[\\s\\S]*?charset=\"{0,1}(\\S+?)\"\\s{0,10}/{0,1}>");
				Matcher matcher = pattern.matcher(temp.toLowerCase());
				if (matcher.find()) {
					charset = matcher.group(1);
				} else {
					charset = "gbk";
				}
				temp = baos.toString(charset);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				baos.close();
				is.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return temp;

	}

}
