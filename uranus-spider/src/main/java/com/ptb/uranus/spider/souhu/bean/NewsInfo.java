package com.ptb.uranus.spider.souhu.bean;

import com.ptb.uranus.spider.souhu.utils.CommonUtil;

import java.util.Date;

/**
 * Created by xuefeng on 2016/3/15.
 */
public class NewsInfo {
    //文章标题
    private String title;
    //发布时间
    private String pubTime;
    //发布媒体
    private String media;
    //文章内容
    private String content;
    //阅读数
    private Integer readNum;
    //喜欢数
    private Integer likeNum;
    //没劲数
    private Integer treadNum;
    //参与数
    private Integer participationNum;
    //评论数
    private Integer commentNum;
    //爬取数据的时间
    private String CrawlerTime = CommonUtil.Date2Str(new Date(), "yyyy-MM-dd HH:mm:ss");

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPubTime() {
        return pubTime;
    }

    public void setPubTime(String pubTime) {
        this.pubTime = pubTime;
    }

    public String getMedia() {
        return media;
    }

    public void setMedia(String media) {
        this.media = media;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Integer getReadNum() {
        return readNum;
    }

    public void setReadNum(Integer readNum) {
        this.readNum = readNum;
    }

    public Integer getLikeNum() {
        return likeNum;
    }

    public void setLikeNum(Integer likeNum) {
        this.likeNum = likeNum;
    }

    public Integer getTreadNum() {
        return treadNum;
    }

    public void setTreadNum(Integer treadNum) {
        this.treadNum = treadNum;
    }

    public Integer getParticipationNum() {
        return participationNum;
    }

    public void setParticipationNum(Integer participationNum) {
        this.participationNum = participationNum;
    }

    public Integer getCommentNum() {
        return commentNum;
    }

    public void setCommentNum(Integer commentNum) {
        this.commentNum = commentNum;
    }

    @Override
    public String toString() {
        return "NewsInfo{" +
                "title:'" + title + '\'' +
                ", pubTime:'" + pubTime + '\'' +
                ", media:'" + media + '\'' +
                ", content:'" + content + '\'' +
                ", readNum:" + readNum +
                ", likeNum:" + likeNum +
                ", treadNum:" + treadNum +
                ", participationNum:" + participationNum +
                ", commentNum:" + commentNum +
                ", CrawlerTime:'" + CrawlerTime + '\'' +
                '}';
    }
}
