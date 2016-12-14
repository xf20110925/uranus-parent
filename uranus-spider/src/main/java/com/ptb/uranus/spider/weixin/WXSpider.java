package com.ptb.uranus.spider.weixin;

import com.alibaba.fastjson.JSON;
import com.ptb.uranus.spider.common.utils.HttpUtil;
import com.ptb.uranus.spider.weixin.bean.ReadLikeNum;
import com.ptb.uranus.spider.weixin.parse.WxArticleParser;
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
	static WxArticleParser wxArticleParser = new WxArticleParser();
	static class Param {
		private String biz;
		private String mid;
		private String sn;
		private String idx;
		private String key;
		private String uin;

		public Param(String url) {
			biz = RegexUtils.sub(".*__biz=([^#&]*).*", url, 0);
			mid = RegexUtils.sub(".*mid=([^#&]*).*", url, 0);
			sn = RegexUtils.sub(".*sn=([^#&]*).*", url, 0);
			idx = RegexUtils.sub(".*idx=([^#&]*).*", url, 0);
			key = RegexUtils.sub(".*key=([^#&]*).*", url, 0);
			uin = RegexUtils.sub(".*uin=([^#&]*).*", url, 0);
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

		public String getKey() {
			return key;
		}

		public Param setKey(String key) {
			this.key = key;
			return this;
		}

		public String getUin() {
			return uin;
		}

		public Param setUin(String uin) {
			this.uin = uin;
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

	/**
	 * @param url 包含微信key的url
	 * @return
	 * @throws IOException
	 */
	public static ReadLikeNum getReadLikeNum(String url) throws IOException {
		String params = "is_only_read=1&req_id=0811jgVb3OaNm2IlY35Ti9G5&is_temp_url=0";
		Param param = new Param(url);
		System.out.println(JSON.toJSONString(param));
		String requestUrl = "https://mp.weixin.qq.com/mp/getappmsgext?__biz=%s&mid=%s&idx=%s&sn=%s&is_need_ad=0&f=json&uin=%s&key=%s";
		requestUrl = String.format(requestUrl, param.getBiz(), param.getMid(), param.getIdx(), param.getSn(), param.getUin(), param.getKey());
		System.out.println(requestUrl);
		url = HttpUtil.updateArgument(url, "uin", param.getUin());
		url = HttpUtil.updateArgument(url, "key", param.getKey());
		CookieStore cookieStore = httpGet(url);
		String ret = HttpUtil.postByPageClient(requestUrl, params, ContentType.APPLICATION_FORM_URLENCODED, UA, cookieStore);
		ReadLikeNum readLikeNum = wxArticleParser.parseReadLikeNumByJson(ret);
		return readLikeNum;
	}

	public static void main(String[] args) throws IOException {
		String url = "http://mp.weixin.qq.com/s?__biz=MzA5MTc0NTMwNQ==&mid=2650714035&idx=1&sn=0ffaa9d915f898780686fd104dab18d5&chksm=887db2c5bf0a3bd3c8dbe08193d778a4272d885ffcdcb2e8f45679bd4ebb069884d2446e7655&scene=37&key=dbf846bda2a738098da6054f99f1048654a42f52a7fbfefac17408cc1d7687c24e24de860edb4f1db03d22a9663cf7babea1ff69e977e1fa793a336d91d5d128df31253820ba522f1cf8c4e126410d76&ascene=3&uin=MjQzNzIwMzI0MQ%3D%3D&devicetype=android-19&version=26010041&nettype=WIFI&abtest_cookie=AQABAAgAAQALhh4AAAA%3D&pass_ticket=x6SfHADzMrnAmr3cXmC822mF505PLSLTsQexjqKZYM9m%2BLmIXocqSDVODVTTdA2u";
		ReadLikeNum s = getReadLikeNum(url);
		System.out.println(s);
	}

}
