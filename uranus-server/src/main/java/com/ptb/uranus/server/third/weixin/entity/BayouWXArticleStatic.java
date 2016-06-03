package com.ptb.uranus.server.third.weixin.entity;


/**
 * Created by xuefeng on 2016/5/17.
 */
public class BayouWXArticleStatic {
    private String id;
    private String url;
    private String title;
    private String content;

    public BayouWXArticleStatic() {
    }

    public BayouWXArticleStatic(String id, String url, String title, String content) {
        this.id = id;
        this.url = url;
        this.title = title;
        this.content = content;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}