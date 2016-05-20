package com.ptb.uranus.server.bayoudata.entity;

import java.io.Serializable;

/**
 * Created by xuefeng on 2016/5/18.
 */
public class IdRecord implements Serializable {
    long mediaId = 0;
    long dynamicArticleId = 0;
    long staticArticleId = 0;

    public IdRecord() {
    }

    public IdRecord(long mediaId, long dynamicArticleId, long staticArticleId) {
        this.mediaId = mediaId;
        this.dynamicArticleId = dynamicArticleId;
        this.staticArticleId = staticArticleId;
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

    @Override
    public String toString() {
        return "IdRecord{" +
                "mediaId=" + mediaId +
                ", dynamicArticleId=" + dynamicArticleId +
                ", staticArticleId=" + staticArticleId +
                '}';
    }
}
