package com.ptb.uranus.server.send.entity.article;

/**
 * Created by eric on 16/4/25.
 */
public class WeixinArticleStatic extends BasicArticleStatic {
    String weixinId;
    String surface;
    boolean original;
    String biz;

    public String getSurface() {
        return surface;
    }

    public void setSurface(String surface) {
        this.surface = surface;
    }

    public boolean isOriginal() {
        return original;
    }

    public void setOriginal(boolean original) {
        this.original = original;
    }

    public String getBiz() {
        return biz;
    }

    public void setBiz(String biz) {
        this.biz = biz;
    }

    public String getWeixinId() {
        return weixinId;
    }

    public void setWeixinId(String weixinId) {
        this.weixinId = weixinId;
    }
}
