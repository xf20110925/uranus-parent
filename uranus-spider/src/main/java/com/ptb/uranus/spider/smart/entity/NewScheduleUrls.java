package com.ptb.uranus.spider.smart.entity;

import java.util.List;

/**
 * Created by eric on 16/3/25.
 */
public class NewScheduleUrls {
    List<String> urls;
    long lastPostTime;

    public List<String> getUrls() {
        return urls;
    }

    public void setUrls(List<String> urls) {
        this.urls = urls;
    }

    public long getLastPostTime() {
        return lastPostTime;
    }

    public void setLastPostTime(long lastPostTime) {
        this.lastPostTime = lastPostTime;
    }
}
