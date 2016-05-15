package com.ptb.uranus.spider.weixin.bean;

import java.util.List;

/**
 * Created by eric on 15/11/11.
 * 一个微信列表的内容信息
 */
public class ExtMsgInfo {
    /**
     * The Title.
     */
    String title;
    /**
     * The Digest.
     */
    String digest;
    /**
     * The Content.
     */
    String content;
    /**
     * The Fileid.
     */
    Long fileid;
    /**
     * The Content url.
     */
    String content_url;
    /**
     * The Source url.
     */
    String source_url;
    /**
     * The Cover.
     */
    String cover;
    /**
     * The Subtype.
     */
    int subtype;
    /**
     * The Is multi.
     */
    int is_multi;
    /**
     * The Multi app msg item list.
     */
    List<ExtMsgInfo> multi_app_msg_item_list;

    /**
     * Gets title.
     *
     * @return the title
     */
    public String getTitle() {
        return title;
    }

    /**
     * Sets title.
     *
     * @param title the title
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * Gets digest.
     *
     * @return the digest
     */
    public String getDigest() {
        return digest;
    }

    /**
     * Sets digest.
     *
     * @param digest the digest
     */
    public void setDigest(String digest) {
        this.digest = digest;
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

    /**
     * Gets fileid.
     *
     * @return the fileid
     */
    public Long getFileid() {
        return fileid;
    }

    /**
     * Sets fileid.
     *
     * @param fileid the fileid
     */
    public void setFileid(Long fileid) {
        this.fileid = fileid;
    }

    /**
     * Gets content url.
     *
     * @return the content url
     */
    public String getContent_url() {
        return content_url;
    }

    /**
     * Sets content url.
     *
     * @param content_url the content url
     */
    public void setContent_url(String content_url) {
        this.content_url = content_url;
    }

    /**
     * Gets source url.
     *
     * @return the source url
     */
    public String getSource_url() {
        return source_url;
    }

    /**
     * Sets source url.
     *
     * @param source_url the source url
     */
    public void setSource_url(String source_url) {
        this.source_url = source_url;
    }

    /**
     * Gets cover.
     *
     * @return the cover
     */
    public String getCover() {
        return cover;
    }

    /**
     * Sets cover.
     *
     * @param cover the cover
     */
    public void setCover(String cover) {
        this.cover = cover;
    }

    /**
     * Gets subtype.
     *
     * @return the subtype
     */
    public int getSubtype() {
        return subtype;
    }

    /**
     * Sets subtype.
     *
     * @param subtype the subtype
     */
    public void setSubtype(int subtype) {
        this.subtype = subtype;
    }

    /**
     * Gets is multi.
     *
     * @return the is multi
     */
    public int getIs_multi() {
        return is_multi;
    }

    /**
     * Sets is multi.
     *
     * @param is_multi the is multi
     */
    public void setIs_multi(int is_multi) {
        this.is_multi = is_multi;
    }

    /**
     * Gets multi app msg item list.
     *
     * @return the multi app msg item list
     */
    public List<ExtMsgInfo> getMulti_app_msg_item_list() {
        return multi_app_msg_item_list;
    }

    /**
     * Sets multi app msg item list.
     *
     * @param multi_app_msg_item_list the multi app msg item list
     */
    public void setMulti_app_msg_item_list(List<ExtMsgInfo> multi_app_msg_item_list) {
        this.multi_app_msg_item_list = multi_app_msg_item_list;
    }
}
