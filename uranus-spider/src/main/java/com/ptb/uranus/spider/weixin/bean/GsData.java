package com.ptb.uranus.spider.weixin.bean;

/**
 * 清博指数微博公众号bean
 * @author liujianpeng 2016-4-26
 *
 */
public class GsData {
	private String wechatname;
	private String wechatid;//微信号
	private String functionintroduce;//功能介绍
	private String included;//最近收录
	private String headportrurl;//头像
	private String qrcodeurl;//二维码
	private String AuthenticationInfo;//认证信息
	
	
	
	public String getHeadportrurl() {
		return headportrurl;
	}
	public void setHeadportrurl(String headportrurl) {
		this.headportrurl = headportrurl;
	}
	public String getWechatname() {
		return wechatname;
	}
	public void setWechatname(String wechatname) {
		this.wechatname = wechatname;
	}
	public String getAuthenticationInfo() {
		return AuthenticationInfo;
	}
	public void setAuthenticationInfo(String authenticationInfo) {
		AuthenticationInfo = authenticationInfo;
	}
	public String getWechatid() {
		return wechatid;
	}
	public void setWechatid(String wechatid) {
		this.wechatid = wechatid;
	}
	public String getFunctionintroduce() {
		return functionintroduce;
	}
	public void setFunctionintroduce(String functionintroduce) {
		this.functionintroduce = functionintroduce;
	}
	public String getIncluded() {
		return included;
	}
	public void setIncluded(String included) {
		this.included = included;
	}

	public String getQrcodeurl() {
		return qrcodeurl;
	}
	public void setQrcodeurl(String qrcodeurl) {
		this.qrcodeurl = qrcodeurl;
	}
	
	
	
	
	
}
