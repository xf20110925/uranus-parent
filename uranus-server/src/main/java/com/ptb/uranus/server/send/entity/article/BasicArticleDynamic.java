package com.ptb.uranus.server.send.entity.article;

import java.util.List;
import java.util.Map;

/**
 * Created by eric on 16/4/25.
 */
public class BasicArticleDynamic {
    String url;
    long time = System.currentTimeMillis();
    int plat;
    int reads = -1 ;
    int likes = -1;
    int comments = -1;
    int nolikes = -1;
    int forwards = -1 ;
    int joins = -1;

    public BasicArticleDynamic() {
    }

    public BasicArticleDynamic(String url, long time, int reads, int likes, int plat, int comments, int nolikes, int forwards, int joins) {
        this.url = url;
        this.time = time;
        this.reads = reads;
        this.likes = likes;
        this.plat = plat;
        this.comments = comments;
        this.nolikes = nolikes;
        this.forwards = forwards;
        this.joins = joins;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public int getReads() {
        return reads;
    }

    public void setReads(int reads) {
        this.reads = reads;
    }

    public int getLikes() {
        return likes;
    }

    public void setLikes(int likes) {
        this.likes = likes;
    }

    public int getPlat() {
        return plat;
    }

    public void setPlat(int plat) {
        this.plat = plat;
    }

    public int getComments() {
        return comments;
    }

    public void setComments(int comments) {
        this.comments = comments;
    }

    public int getNolikes() {
        return nolikes;
    }

    public void setNolikes(int nolikes) {
        this.nolikes = nolikes;
    }

    public int getForwards() {
        return forwards;
    }

    public void setForwards(int forwards) {
        this.forwards = forwards;
    }

    public int getJoins() {
        return joins;
    }

    public void setJoins(int joins) {
        this.joins = joins;
    }
}
