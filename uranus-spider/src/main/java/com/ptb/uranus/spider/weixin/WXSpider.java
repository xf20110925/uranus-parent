package com.ptb.uranus.spider.weixin;

import com.alibaba.fastjson.JSON;
import com.ptb.uranus.spider.common.utils.HttpUtil;
import com.ptb.utils.string.RegexUtils;

import org.apache.http.client.CookieStore;
import org.apache.http.entity.ContentType;
import org.apache.http.impl.client.BasicCookieStore;

import java.io.IOException;


/**
 * @DESC:
 * @VERSION:
 * @author: xuefeng
 * @Date: 2016/12/7
 * @Time: 15:03
 */
public class WXSpider {
	public static final String UA = "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/39.0.2171.95 Safari/537.36 MicroMessenger/6.5.2.501 NetType/WIFI WindowsWechat";

	static class Param {
		private String biz;
		private String mid;
		private String sn;
		private String idx;

		public Param(String url) {
			biz = RegexUtils.sub(".*__biz=([^#&]*).*", url, 0);
			mid = RegexUtils.sub(".*mid=([^#&]*).*", url, 0);
			sn = RegexUtils.sub(".*sn=([^#&]*).*", url, 0);
			idx = RegexUtils.sub(".*idx=([^#&]*).*", url, 0);
		}

		public String getBiz() {
			return biz;
		}

		public Param setBiz(String biz) {
			this.biz = biz;
			return this;
		}

		public String getMid() {
			return mid;
		}

		public Param setMid(String mid) {
			this.mid = mid;
			return this;
		}

		public String getSn() {
			return sn;
		}

		public Param setSn(String sn) {
			this.sn = sn;
			return this;
		}

		public String getIdx() {
			return idx;
		}

		public Param setIdx(String idx) {
			this.idx = idx;
			return this;
		}

	}

	public static CookieStore httpGet(String keyUrl) throws IOException {
		CookieStore cookieStore = new BasicCookieStore();
		HttpUtil.getPageSourceByClient(keyUrl, UA, cookieStore, "utf-8", "");
		System.out.println("Initial set of cookies:");
		if (cookieStore.getCookies().isEmpty()) {
			System.out.println("None");
		} else {
			for (int i = 0; i < cookieStore.getCookies().size(); i++) {
				System.out.println("- " + cookieStore.getCookies().get(i).toString());
			}
		}
		return cookieStore;
	}

	public static String httpPost(String url, String params, String uin, String key) throws IOException {
		Param param = new Param(url);
		System.out.println(JSON.toJSONString(param));
		String requestUrl = "https://mp.weixin.qq.com/mp/getappmsgext?__biz=%s&mid=%s&idx=%s&sn=%s&is_need_ad=0&f=json&uin=%s&key=%s";
		requestUrl = String.format(requestUrl, param.getBiz(), param.getMid(), param.getIdx(), param.getSn(), uin, key);
		System.out.println(requestUrl);
		url = HttpUtil.updateArgument(url, "uin", uin);
		url = HttpUtil.updateArgument(url, "key", key);
		CookieStore cookieStore = httpGet(url);
		String ret = HttpUtil.postByPageClient(requestUrl, params, ContentType.APPLICATION_FORM_URLENCODED, UA, cookieStore);
		return ret;
	}

	public static void main(String[] args) throws IOException {
		String url = "https://mp.weixin.qq.com/s?__biz=MzI1OTAwNDAzNA==&mid=2650795045&idx=1&sn=e7d8c3d242326aa279c7689dd5c64804#rd";
		String params = "is_only_read=1&req_id=0811jgVb3OaNm2IlY35Ti9G5&is_temp_url=0";
		String uin = "MjQzNzIwMzI0MQ%3D%3D";
		String key = "9ed31d4918c154c8b75ed13bc48407dc0975c60076f185e12efbdcd76bcc82d5fa2740859d49eb6a26dc04de4a0cb011bfd0c854cf52ab6708ed32862682988739d465533a54d913d01288ba22549287";
		String s = httpPost(url, params, uin, key);
		System.out.println(s);
	}

}
