package com.ptb.uranus.spider.weibo.bean;

import java.util.List;

/**
 * Created by eric on 16/2/1.
 */
public class WeiboArticle {

    private Long postTime;
    private String mediaId;
    private String mediaName;
    private String content;
    private String headImg;
    private String brief;
    private String gender;
    private Integer repostCount;
    private Integer commentCount;
    private Integer likeCount;
    private List<String> imgs;
    private List<String> videos;
    private String source;   //发布的方式,网页,手机等,如果是手机则为手机型号
    private String objectType;
    private String articleUrl;
    /**
     * Instantiates a new Weibo article.
     */
    public WeiboArticle() {
    }

    /**
     * Gets source.
     *
     * @return the source
     */
    public String getSource() {
        return source;
    }

    /**
     * Sets source.
     *
     * @param source the source
     */
    public void setSource(String source) {
        this.source = source;
    }

    /**
     * Gets imgs.
     *
     * @return the imgs
     */
    public List<String> getImgs() {
        return imgs;
    }

    /**
     * Sets imgs.
     *
     * @param imgs the imgs
     */
    public void setImgs(List<String> imgs) {
        this.imgs = imgs;
    }

    /**
     * Gets videos.
     *
     * @return the videos
     */
    public List<String> getVideos() {
        return videos;
    }

    /**
     * Sets videos.
     *
     * @param videos the videos
     */
    public void setVideos(List<String> videos) {
        this.videos = videos;
    }

    /**
     * Gets like count.
     *
     * @return the like count
     */
    public Integer getLikeCount() {
        return likeCount;
    }

    /**
     * Sets like count.
     *
     * @param likeCount the like count
     */
    public void setLikeCount(Integer likeCount) {
        this.likeCount = likeCount;
    }

    /**
     * Gets post time.
     *
     * @return the post time
     */
    public Long getPostTime() {
        return postTime;
    }

    /**
     * Sets post time.
     *
     * @param postTime the post time
     */
    public void setPostTime(Long postTime) {
        this.postTime = postTime;
    }

    /**
     * Gets media id.
     *
     * @return the media id
     */
    public String getMediaId() {
        return mediaId;
    }

    /**
     * Sets media id.
     *
     * @param mediaId the media id
     */
    public void setMediaId(String mediaId) {
        this.mediaId = mediaId;
    }

    /**
     * Gets media name.
     *
     * @return the media name
     */
    public String getMediaName() {
        return mediaName;
    }

    /**
     * Sets media name.
     *
     * @param mediaName the media name
     */
    public void setMediaName(String mediaName) {
        this.mediaName = mediaName;
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
     * Gets head img.
     *
     * @return the head img
     */
    public String getHeadImg() {
        return headImg;
    }

    /**
     * Sets head img.
     *
     * @param headImg the head img
     */
    public void setHeadImg(String headImg) {
        this.headImg = headImg;
    }

    /**
     * Gets brief.
     *
     * @return the brief
     */
    public String getBrief() {
        return brief;
    }

    /**
     * Sets brief.
     *
     * @param brief the brief
     */
    public void setBrief(String brief) {
        this.brief = brief;
    }

    /**
     * Gets gender.
     *
     * @return the gender
     */
    public String getGender() {
        return gender;
    }

    /**
     * Sets gender.
     *
     * @param gender the gender
     */
    public void setGender(String gender) {
        this.gender = gender;
    }

    /**
     * Gets repost count.
     *
     * @return the repost count
     */
    public Integer getRepostCount() {
        return repostCount;
    }

    /**
     * Sets repost count.
     *
     * @param repostCount the repost count
     */
    public void setRepostCount(Integer repostCount) {
        this.repostCount = repostCount;
    }

    /**
     * Gets comment count.
     *
     * @return the comment count
     */
    public Integer getCommentCount() {
        return commentCount;
    }

    /**
     * Sets comment count.
     *
     * @param commentCount the comment count
     */
    public void setCommentCount(Integer commentCount) {
        this.commentCount = commentCount;
    }

    public String getObjectType() {
        return objectType;
    }

    public void setObjectType(String objectType) {
        this.objectType = objectType;
    }

    public String getArticleUrl() {
        return articleUrl;
    }

    public void setArticleUrl(String articleUrl) {
        this.articleUrl = articleUrl;
    }
}
