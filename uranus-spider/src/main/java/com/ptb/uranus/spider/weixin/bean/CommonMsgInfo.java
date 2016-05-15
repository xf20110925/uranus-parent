package com.ptb.uranus.spider.weixin.bean;

/**
 * Created by eric on 15/11/11.
 * 一个公众号的文章列表的公共部分信息
 */
public class CommonMsgInfo {
    /**
     * The Id.
     */
    Long id;
    /**
     * The Type.
     */
    Long type;
    /**
     * The Datetime.
     */
    Long datetime;
    /**
     * The Fakeid.
     */
    Long fakeid;
    /**
     * The Status.
     */
    int status;
    /**
     * The Content.
     */
    String content;

    /**
     * Gets id.
     *
     * @return the id
     */
    public Long getId() {
        return id;
    }

    /**
     * Sets id.
     *
     * @param id the id
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * Gets type.
     *
     * @return the type
     */
    public Long getType() {
        return type;
    }

    /**
     * Sets type.
     *
     * @param type the type
     */
    public void setType(Long type) {
        this.type = type;
    }

    /**
     * Gets datetime.
     *
     * @return the datetime
     */
    public Long getDatetime() {
        return datetime;
    }

    /**
     * Sets datetime.
     *
     * @param datetime the datetime
     */
    public void setDatetime(Long datetime) {
        this.datetime = datetime;
    }

    /**
     * Gets fakeid.
     *
     * @return the fakeid
     */
    public Long getFakeid() {
        return fakeid;
    }

    /**
     * Sets fakeid.
     *
     * @param fakeid the fakeid
     */
    public void setFakeid(Long fakeid) {
        this.fakeid = fakeid;
    }

    /**
     * Gets status.
     *
     * @return the status
     */
    public int getStatus() {
        return status;
    }

    /**
     * Sets status.
     *
     * @param status the status
     */
    public void setStatus(int status) {
        this.status = status;
    }

    /**
     * Gets content.
     *
     * @return the content
     */
    public String getContent() {
        return content;
    }

    /**
     * Sets content.
     *
     * @param content the content
     */
    public void setContent(String content) {
        this.content = content;
    }
}
