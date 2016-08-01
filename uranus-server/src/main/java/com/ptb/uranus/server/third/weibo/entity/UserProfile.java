package com.ptb.uranus.server.third.weibo.entity;

/**
 * @DESC:
 * @VERSION:
 * @author: Administrator
 * @Date: 2016/7/29
 * @Time: 17:53
 */
public class UserProfile {
	long id;
	String crawler_date;
	long crawler_time_stamp;
	String user_id;
	String nick_name;
	String tou_xiang;
	String user_type;
	String gender;
	String verified_type;
	String verified_reason;
	String description;
	int fans_number;
	int weibo_number;

	public UserProfile() {
	}


	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getCrawler_date() {
		return crawler_date;
	}

	public void setCrawler_date(String crawler_date) {
		this.crawler_date = crawler_date;
	}

	public long getCrawler_time_stamp() {
		return crawler_time_stamp;
	}

	public void setCrawler_time_stamp(long crawler_time_stamp) {
		this.crawler_time_stamp = crawler_time_stamp;
	}

	public String getUser_id() {
		return user_id;
	}

	public void setUser_id(String user_id) {
		this.user_id = user_id;
	}

	public String getNick_name() {
		return nick_name;
	}

	public void setNick_name(String nick_name) {
		this.nick_name = nick_name;
	}

	public String getTou_xiang() {
		return tou_xiang;
	}

	public void setTou_xiang(String tou_xiang) {
		this.tou_xiang = tou_xiang;
	}

	public String getUser_type() {
		return user_type;
	}

	public void setUser_type(String user_type) {
		this.user_type = user_type;
	}

	public String getGender() {
		return gender;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}

	public String getVerified_type() {
		return verified_type;
	}

	public void setVerified_type(String verified_type) {
		this.verified_type = verified_type;
	}

	public String getVerified_reason() {
		return verified_reason;
	}

	public void setVerified_reason(String verified_reason) {
		this.verified_reason = verified_reason;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public int getFans_number() {
		return fans_number;
	}

	public void setFans_number(int fans_number) {
		this.fans_number = fans_number;
	}

	public int getWeibo_number() {
		return weibo_number;
	}

	public void setWeibo_number(int weibo_number) {
		this.weibo_number = weibo_number;
	}
}
