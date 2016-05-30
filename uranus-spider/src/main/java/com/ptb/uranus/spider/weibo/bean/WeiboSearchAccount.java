package com.ptb.uranus.spider.weibo.bean;

/**
 * 微博搜索对象
 *
 * @author liujainpeng 2016-4-27
 */
public class WeiboSearchAccount {

    private String account;//微博账号
    private String gender;//性别
    private String address;//地址
    private String personalpage;//个人页
    private String focus;//关注
    private String introduction;//简介
    private String fans;//粉丝
    private String weibo;//微博
    private String company;//公司
    private String headportrait;//头像
    private String vertifyType;
    private String weiboId;

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getFocus() {
        return focus;
    }

    public void setFocus(String focus) {
        this.focus = focus;
    }

    public String getFans() {
        return fans;
    }

    public void setFans(String fans) {
        this.fans = fans;
    }

    public String getWeibo() {
        return weibo;
    }

    public void setWeibo(String weibo) {
        this.weibo = weibo;
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public String getHeadportrait() {
        return headportrait;
    }

    public void setHeadportrait(String headportrait) {
        this.headportrait = headportrait;
    }

    public String getIntroduction() {
        return introduction;
    }

    public void setIntroduction(String introduction) {
        this.introduction = introduction;
    }

    public String getPersonalpage() {
        return personalpage;
    }

    public void setPersonalpage(String personalpage) {
        this.personalpage = personalpage;
    }

    public String getVertifyType() {
        return vertifyType;
    }

    public void setVertifyType(String vertifyType) {
        this.vertifyType = vertifyType;
    }

    public void setWeiboId(String weiboId) {
        this.weiboId = weiboId;
    }

    public String getWeiboId() {
        return weiboId;
    }
}
