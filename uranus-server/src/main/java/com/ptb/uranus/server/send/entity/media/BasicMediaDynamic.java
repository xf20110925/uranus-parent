package com.ptb.uranus.server.send.entity.media;

/**
 * Created by watson zhang on 16/4/29.
 */
public class BasicMediaDynamic {
    int plat;
    int mediaName;
    int fans;
    int articles;
    int followers;

    public int getPlat() {
        return plat;
    }

    public void setPlat(int plat) {
        this.plat = plat;
    }

    public int getMediaName() {
        return mediaName;
    }

    public void setMediaName(int mediaName) {
        this.mediaName = mediaName;
    }

    public int getFans() {
        return fans;
    }

    public void setFans(int fans) {
        this.fans = fans;
    }

    public int getArticles() {
        return articles;
    }

    public void setArticles(int articles) {
        this.articles = articles;
    }

    public int getFollowers() {
        return followers;
    }

    public void setFollowers(int followers) {
        this.followers = followers;
    }
}
