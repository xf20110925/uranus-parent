package com.ptb.uranus.spider.weibo.bean;

import com.alibaba.fastjson.JSON;

/**
 * Created by Administrator on 2016/8/24 0024.
 */
public class WbSerachHot {

	private String name;
	private String url;
	private String num;


	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getNum() {
		return num;
	}

	public void setNum(String num) {
		this.num = num;
	}

	@Override
	public String toString() {
		return JSON.toJSONString(this);
	}
}
