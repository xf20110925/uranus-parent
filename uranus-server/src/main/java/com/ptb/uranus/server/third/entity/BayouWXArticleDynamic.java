package com.ptb.uranus.server.third.entity;

/**
 * Created by xuefeng on 2016/5/19.
 */
public class BayouWXArticleDynamic {

    private String id;
    private String url;
    private String title;
    private int read_num;
    private int like_num;
    private long ts;

    public BayouWXArticleDynamic() {
    }

    public BayouWXArticleDynamic(String id, String url, String title, int read_num, int like_num) {
        this.id = id;
        this.url = url;
        this.title = title;
        this.read_num = read_num;
        this.like_num = like_num;
    }

    public long getTs() {
        return ts;
    }

    public void setTs(long ts) {
        this.ts = ts;
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

    public int getRead_num() {
        return read_num;
    }

    public void setRead_num(int read_num) {
        this.read_num = read_num;
    }

    public int getLike_num() {
        return like_num;
    }

    public void setLike_num(int like_num) {
        this.like_num = like_num;
    }
}
