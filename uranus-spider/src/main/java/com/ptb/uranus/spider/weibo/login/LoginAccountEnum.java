package com.ptb.uranus.spider.weibo.login;

import org.apache.http.impl.client.BasicCookieStore;

/**
 * @DESC:
 * @VERSION:
 * @author: xuefeng
 * @Date: 2016/8/25
 * @Time: 15:16
 */
public enum LoginAccountEnum {
/*	ACCOUNT1("17182616764", "ptbptb"),
	ACCOUNT2("17182617024", "vdkqyf4667o"),
	ACCOUNT3("17182617060", "vcipvc2688k"),
	ACCOUNT4("17183150469", "ovciqw0637n"),*/
	ACCOUNT5("17184651305", "udlrah5206s");
	private String account;
	private String password;
	private BasicCookieStore cookie;

	LoginAccountEnum(String account, String password) {
		WeiboLoginByHttpClinet weiboLoginByHttpClinet = new WeiboLoginByHttpClinet();
		this.account = account;
		this.password = password;
		try {
			BasicCookieStore cookie = weiboLoginByHttpClinet.login(account, password);
			this.cookie = cookie;
		} catch (Exception e) {
			System.out.println(String.format("%s登录失败", account));
		}
	}

	public String getAccount() {
		return account;
	}

	public void setAccount(String account) {
		this.account = account;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public BasicCookieStore getCookie() {
		return cookie;
	}

	public void setCookie(BasicCookieStore cookie) {
		this.cookie = cookie;
	}
}
