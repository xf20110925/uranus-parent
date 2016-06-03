package com.ptb.uranus.server.third.weixin.entity;


import org.apache.commons.lang.StringUtils;

/**
 * Created by xuefeng on 2016/5/17.
 */
public class BayouWXMedia {
    private String code;
    private String qrcode;
    private String name;
    private String id;
    private String bid;
    private String info;
    private String headImage;

    public BayouWXMedia() {
    }

    public BayouWXMedia(String code, String qrcode, String name, String id, String bid, String info) {
        this.code = code;
        this.qrcode = qrcode;
        this.name = name;
        this.id = id;
        this.bid = bid;
        this.info = info;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
        if(StringUtils.isNotBlank(code)){
            headImage = "http://open.weixin.qq.com/qr/code/?username=" + code;
        }
    }

    public String getQrcode() {
        return qrcode;
    }

    public void setQrcode(String qrcode) {
        this.qrcode = qrcode;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getBid() {
        return bid;
    }

    public void setBid(String bid) {
        this.bid = bid;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public String getHeadImage() {
        return headImage;
    }

    public void setHeadImage(String headImage) {
        this.headImage = headImage;
    }

    @Override
    public String toString() {
        return "WeixinMdeia{" +
                "code='" + code + '\'' +
                ", qrcode='" + qrcode + '\'' +
                ", name='" + name + '\'' +
                ", id='" + id + '\'' +
                ", bid='" + bid + '\'' +
                ", info='" + info + '\'' +
                '}';
    }
}
