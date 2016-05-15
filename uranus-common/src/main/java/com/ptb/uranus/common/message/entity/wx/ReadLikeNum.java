package com.ptb.uranus.common.message.entity.wx;

/**
 * Created by eric on 16/1/12.
 */
public class ReadLikeNum {

    private int readNum;
    private int likeNum;


    /**
     * Instantiates a new Read like num.
     *
     * @param readNum the read num
     * @param likeNum the like num
     */
    public ReadLikeNum(int readNum, int likeNum) {
        this.readNum = readNum;
        this.likeNum = likeNum;
    }

    /**
     * Gets read num.
     *
     * @return the read num
     */
    public int getReadNum() {
        return readNum;
    }


    /**
     * Sets read num.
     *
     * @param readNum the read num
     */
    public void setReadNum(String readNum) {
        try {
            this.readNum = Integer.parseInt(readNum.replaceAll("[^\\d]*", ""));
        } catch (Exception e) {
            this.readNum = 0;
        }
    }

    /**
     * Sets like num.
     *
     * @param likeNum the like num
     */
    public void setLikeNum(String likeNum) {
        try {
            this.likeNum = Integer.parseInt(likeNum.replaceAll("[^\\d]*", ""));
        } catch (Exception e) {
            this.likeNum = 0;
        }
    }


    /**
     * Gets like num.
     *
     * @return the like num
     */
    public int getLikeNum() {
        return likeNum;
    }


    /**
     * Instantiates a new Read like num.
     */
    public ReadLikeNum() {
    }
}
