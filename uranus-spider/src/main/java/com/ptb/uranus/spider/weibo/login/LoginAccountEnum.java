package com.ptb.uranus.spider.weibo.login;

import org.apache.http.client.CookieStore;

/**
 * @DESC:
 * @VERSION:
 * @author: xuefeng
 * @Date: 2016/8/25
 * @Time: 15:16
 */
public enum LoginAccountEnum {
	ACCOUNT1("17182616764", "ptbptb"),
	ACCOUNT2("17182617024", "vdkqyf4667o"),
	ACCOUNT3("17182617060", "vcipvc2688k"),
	ACCOUNT4("17183150469", "ovciqw0637n"),
	ACCOUNT5("17184651305", "udlrah5206s"),
	ACCOUNT6("18666409787", "dvjobq2206y"),
	ACCOUNT7("18666409887", "uurnkl5384d"),
	ACCOUNT8("18666437921", "dcjncv1307w"),
	ACCOUNT9("18666447338", "hthzoi4527u"),
	ACCOUNT10("18666470193", "ccyvkn8336v"),
	ACCOUNT11("18666470194", "wthqmo3367k"),
	ACCOUNT12("18676049373", "kknwur4026x"),
	ACCOUNT13("18676280895", "skhdrn2338i"),
	ACCOUNT14("18676332881", "gmobje0310y");
	private String account;
	private String password;
	private CookieStore cookie;

	LoginAccountEnum(String account, String password) {
		this.account = account;
		this.password = password;
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

	public CookieStore getCookie() {
		return cookie;
	}

	public void setCookie(CookieStore cookie) {
		this.cookie = cookie;
	}
}
