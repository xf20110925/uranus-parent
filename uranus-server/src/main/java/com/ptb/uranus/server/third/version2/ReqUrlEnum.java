package com.ptb.uranus.server.third.version2;

import org.apache.commons.configuration.Configuration;
import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;

import java.util.Arrays;

/**
 * @DESC:
 * @VERSION:
 * @author: xuefeng
 * @Date: 2016/8/2
 * @Time: 16:12
 */
public enum ReqUrlEnum {

	WX_MEDIA_RANGE_URL,
	WX_MEDIA_DATA_URL,

	WX_ARTICLE_STATIC_RANGE_URL,
	WX_ARTICLE_STATIC_DATA_URL,

	WX_ARTICLE_DYNAMIC_RANGE_URL,
	WX_ARTICLE_DYNAMIC_DATA_URL,

	WB_MEDIA_RANGE_URL,
	WB_MEDIA_DATA_URL,

	WB_ARTICLE_RANGE_URL,
	WB_ARTICLE_DATA_URL;


	static {
		try {
			Configuration conf = new PropertiesConfiguration("ptb.properties");
			WX_MEDIA_RANGE_URL.setValue(conf.getString("", "http://weixindata.pullword.com:12345/biz/range?auth_usr=pintuibao"));
			WX_MEDIA_DATA_URL.setValue(conf.getString("", "http://weixindata.pullword.com:12345/biz/%d?auth_usr=pintuibao"));
			WX_ARTICLE_STATIC_RANGE_URL.setValue(conf.getString("", "http://weixindata.pullword.com:12345/page/range?auth_usr=pintuibao"));
			WX_ARTICLE_STATIC_DATA_URL.setValue(conf.getString("", "http://weixindata.pullword.com:12345/page/%d?auth_usr=pintuibao"));
			WX_ARTICLE_DYNAMIC_RANGE_URL.setValue(conf.getString("", "http://weixindata.pullword.com:12345/click/range?auth_usr=pintuibao"));
			WX_ARTICLE_DYNAMIC_DATA_URL.setValue(conf.getString("", "http://weixindata.pullword.com:12345/click/%d?auth_usr=pintuibao"));
			WB_MEDIA_RANGE_URL.setValue(conf.getString("", "http://weiboapi.pullword.com:22345/profile/range?auth_usr=pintuibao"));
			WB_MEDIA_DATA_URL.setValue(conf.getString("", "http://weiboapi.pullword.com:22345/profile/%d?auth_usr=pintuibao"));
			WB_ARTICLE_RANGE_URL.setValue(conf.getString("", "http://weiboapi.pullword.com:22345/weibo/range?auth_usr=pintuibao"));
			WB_ARTICLE_DATA_URL.setValue(conf.getString("", "http://weiboapi.pullword.com:22345/weibo/%d?auth_usr=pintuibao"));
		} catch (ConfigurationException e) {
			e.printStackTrace();
		}

	}

	private String value;

	ReqUrlEnum() {
	}

	ReqUrlEnum(String value) {
		this.value = value;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public static void main(String[] args) {
		Arrays.asList(ReqUrlEnum.values()).stream().map(ReqUrlEnum::getValue).forEach(System.out::println);
	}
}
