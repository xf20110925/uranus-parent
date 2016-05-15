package com.ptb.uranus.asistant.core.entity;

/**
 * Created by eric on 16/1/13.
 */
public class ReadLikeNum {
    String readNum;
    String likeNum;

    public ReadLikeNum() {
    }

    public ReadLikeNum(String readNum, String likeNum) {
        this.readNum = readNum;
        this.likeNum = likeNum;
    }

    public String getReadNum() {
        return readNum;
    }

    public void setReadNum(String readNum) {
        this.readNum = readNum;
    }

    public String getLikeNum() {
        return likeNum;
    }

    public void setLikeNum(String likeNum) {
        this.likeNum = likeNum;
    }
}
