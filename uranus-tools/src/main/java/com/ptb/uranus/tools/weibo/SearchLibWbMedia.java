package com.ptb.uranus.tools.weibo;

/**
 * Created by eric on 16/6/27.
 */
public class SearchLibWbMedia {
    String user_id;
    String tou_xiang;
    String fans_number;
    String gender;
    String description;
    String weibo_number;
    String verified_type;
    String crawler_date = String.valueOf(System.currentTimeMillis());
    String user_type;
    String nick_name;
    String crawler_time_stamp = String.valueOf(System.currentTimeMillis());
    String verified_reason;
    String id = String.valueOf(System.currentTimeMillis());

    public String getNick_name() {
        return nick_name;
    }

    public void setNick_name(String nick_name) {
        this.nick_name = nick_name;
    }

    public String getUser_type() {
        return user_type;
    }

    public void setUser_type(String user_type) {
        this.user_type = user_type;
    }

    public String getVerified_reason() {
        return verified_reason;
    }

    public void setVerified_reason(String verified_reason) {
        this.verified_reason = verified_reason;
    }

    public String getVerified_type() {
        return verified_type;
    }

    public void setVerified_type(String verified_type) {
        this.verified_type = verified_type;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getTou_xiang() {
        return tou_xiang;
    }

    public void setTou_xiang(String tou_xiang) {
        this.tou_xiang = tou_xiang;
    }

    public String getFans_number() {
        return fans_number;
    }

    public void setFans_number(String fans_number) {
        this.fans_number = fans_number;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getWeibo_number() {
        return weibo_number;
    }

    public void setWeibo_number(String weibo_number) {
        this.weibo_number = weibo_number;
    }

    public String getCrawler_date() {
        return crawler_date;
    }

    public String getCrawler_time_stamp() {
        return crawler_time_stamp;
    }

    public String getId() {
        return id;
    }
}
