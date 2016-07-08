package com.ptb.uranus.server.third.weixin.entity;

import com.alibaba.fastjson.JSON;

import java.io.Serializable;

/**
 * Created by xuefeng on 2016/5/18.
 */
public class IdRecord implements Serializable {
    long mediaId = 0;
    long dynamicArticleId = 0;
    long staticArticleId = 0;
    long wbMediaId = 0;
    long wbArticleId = 0;

    public IdRecord() {
    }

    public IdRecord(long mediaId, long dynamicArticleId, long staticArticleId, long wbMediaId, long wbArticleId) {
        this.mediaId = mediaId;
        this.dynamicArticleId = dynamicArticleId;
        this.staticArticleId = staticArticleId;
        this.wbMediaId = wbMediaId;
        this.wbArticleId = wbArticleId;
    }

    public long getMediaId() {
        return mediaId;
    }

    public void setMediaId(long mediaId) {
        this.mediaId = mediaId;
    }

    public long getDynamicArticleId() {
        return dynamicArticleId;
    }

    public void setDynamicArticleId(long dynamicArticleId) {
        this.dynamicArticleId = dynamicArticleId;
    }

    public long getStaticArticleId() {
        return staticArticleId;
    }

    public void setStaticArticleId(long staticArticleId) {
        this.staticArticleId = staticArticleId;
    }

    public long getWbMediaId() {
        return wbMediaId;
    }

    public void setWbMediaId(long wbMediaId) {
        this.wbMediaId = wbMediaId;
    }

    public long getWbArticleId() {
        return wbArticleId;
    }

    public void setWbArticleId(long wbArticleId) {
        this.wbArticleId = wbArticleId;
    }

    @Override
    public String toString() {
        return JSON.toJSONString(this);
    }
}
