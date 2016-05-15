package com.ptb.uranus.spider.weibo.bean;

/**
 * Created by eric on 15/11/4.
 */
public class WeiboAccount {
    private int mblogNum;
    /**
     * The Container id.
     */
    String containerID;
    /**
     * The Title.
     */
    String title;
    /**
     * The Desc.
     */
    String desc;
    /**
     * The Head img.
     */
    String headImg;
    /**
     * The Nick name.
     */
    String nickName;
    /**
     * The Weibo id.
     */
    String weiboID;

    /**
     * The Att num.
     */
    int attNum;
    /**
     * The Fans num.
     */
    int fansNum;
    /**
     * The Favourites count.
     */
    int favourites_count;
    /**
     * The Create time.
     */
    long create_time;
    /**
     * The Active place.
     */
    String activePlace;
    /**
     * The Gender.
     */
    String gender;
    String verifiedType;
    String verifiedReason;

    /**
     * Instantiates a new Weibo account.
     */
    public WeiboAccount() {
    }

    /**
     * Gets favourites count.
     *
     * @return the favourites count
     */
    public int getFavourites_count() {
        return favourites_count;
    }

    /**
     * Sets favourites count.
     *
     * @param favourites_count the favourites count
     */
    public void setFavourites_count(int favourites_count) {
        this.favourites_count = favourites_count;
    }

    /**
     * Gets create time.
     *
     * @return the create time
     */
    public long getCreate_time() {
        return create_time;
    }

    /**
     * Sets create time.
     *
     * @param create_time the create time
     */
    public void setCreate_time(long create_time) {
        this.create_time = create_time;
    }

    /**
     * Gets active place.
     *
     * @return the active place
     */
    public String getActivePlace() {
        return activePlace;
    }

    /**
     * Sets active place.
     *
     * @param activePlace the active place
     */
    public void setActivePlace(String activePlace) {
        this.activePlace = activePlace;
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
     * Gets mblog num.
     *
     * @return the mblog num
     */
    public int getMblogNum() {
        return mblogNum;
    }

    /**
     * Sets mblog num.
     *
     * @param mblogNum the mblog num
     */
    public void setMblogNum(int mblogNum) {
        this.mblogNum = mblogNum;
    }

    /**
     * Gets container id.
     *
     * @return the container id
     */
    public String getContainerID() {
        return containerID;
    }

    /**
     * Sets container id.
     *
     * @param containerID the container id
     */
    public void setContainerID(String containerID) {
        this.containerID = containerID;
    }

    /**
     * Gets title.
     *
     * @return the title
     */
    public String getTitle() {
        return title;
    }

    /**
     * Sets title.
     *
     * @param title the title
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * Gets desc.
     *
     * @return the desc
     */
    public String getDesc() {
        return desc;
    }

    /**
     * Sets desc.
     *
     * @param desc the desc
     */
    public void setDesc(String desc) {
        this.desc = desc;
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
     * Gets nick name.
     *
     * @return the nick name
     */
    public String getNickName() {
        return nickName;
    }

    /**
     * Sets nick name.
     *
     * @param nickName the nick name
     */
    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    /**
     * Gets weibo id.
     *
     * @return the weibo id
     */
    public String getWeiboID() {
        return weiboID;
    }

    /**
     * Sets weibo id.
     *
     * @param weiboID the weibo id
     */
    public void setWeiboID(String weiboID) {
        this.weiboID = weiboID;
    }

    /**
     * Gets att num.
     *
     * @return the att num
     */
    public int getAttNum() {
        return attNum;
    }

    /**
     * Sets att num.
     *
     * @param attNum the att num
     */
    public void setAttNum(int attNum) {
        this.attNum = attNum;
    }

    /**
     * Gets fans num.
     *
     * @return the fans num
     */
    public int getFansNum() {
        return fansNum;
    }

    /**
     * Sets fans num.
     *
     * @param fansNum the fans num
     */
    public void setFansNum(int fansNum) {
        this.fansNum = fansNum;
    }

    public String getVerifiedType() {
        return verifiedType;
    }

    public void setVerifiedType(String verifiedType) {
        this.verifiedType = verifiedType;
    }

    public String getVerifiedReason() {
        return verifiedReason;
    }

    public void setVerifiedReason(String verifiedReason) {
        this.verifiedReason = verifiedReason;
    }
}
