package com.ptb.uranus.spider.smart.entity;

/**
 * Created by xuefeng 16/4/14.
 */
public class DynamicData {
    Integer readNum;
    Integer CommentNum;
    /**
     * 参与人数
     */
    Integer involvementNum;
    Integer likeNum;
    /**
     * 认为没劲人数
     */
    Integer boringNum;
    //抓发人数
    Integer shareNum;
    Integer plat;

    public DynamicData() {
    }

    public Integer getReadNum() {
        return readNum;
    }

    public void setReadNum(Integer readNum) {
        this.readNum = readNum;
    }

    public Integer getCommentNum() {
        return CommentNum;
    }

    public void setCommentNum(Integer commentNum) {
        CommentNum = commentNum;
    }

    public Integer getInvolvementNum() {
        return involvementNum;
    }

    public void setInvolvementNum(Integer involvementNum) {
        this.involvementNum = involvementNum;
    }

    public Integer getLikeNum() {
        return likeNum;
    }

    public void setLikeNum(Integer likeNum) {
        this.likeNum = likeNum;
    }

    public Integer getBoringNum() {
        return boringNum;
    }

    public void setBoringNum(Integer boringNum) {
        this.boringNum = boringNum;
    }

    public Integer getShareNum() {
        return shareNum;
    }

    public void setShareNum(Integer shareNum) {
        this.shareNum = shareNum;
    }

    public Integer getPlat() {
        return plat;
    }

    public void setPlat(Integer plat) {
        this.plat = plat;
    }
}
