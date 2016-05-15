package com.ptb.uranus.common.message.entity.wx;

/**
 * Created by eric on 15/12/7.
 * 微信帐户的基本信息
 */
public class WxAccount {
    private String biz;
    private String brief;
    private String headImg;
    private String nickName;
    private String id;
    private String qcCode;

    /**
     * Instantiates a new Wx account.
     *
     * @param biz        the biz
     * @param brief      the brief
     * @param headImgUrl the head img url
     * @param nickName   the nick name
     * @param id         the id
     * @param qrCode     the qr code
     */
    public WxAccount(String biz, String brief, String headImgUrl, String nickName, String id, String qrCode) {
        this.biz = biz;
        this.brief = brief;
        this.headImg = headImgUrl;
        this.nickName = nickName;
        this.id = id;
        this.qcCode = qrCode;
    }

    /**
     * Instantiates a new Wx account.
     */
    public WxAccount() {
    }

    /**
     * Gets biz.
     *
     * @return the biz
     */
    public String getBiz() {
        return biz;
    }

    /**
     * Sets biz.
     *
     * @param biz the biz
     */
    public void setBiz(String biz) {
        this.biz = biz;
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
     * Gets id.
     *
     * @return the id
     */
    public String getId() {
        return id;
    }

    /**
     * Sets id.
     *
     * @param id the id
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     * Gets qc code.
     *
     * @return the qc code
     */
    public String getQcCode() {
        return qcCode;
    }

    /**
     * Sets qc code.
     *
     * @param qcCode the qc code
     */
    public void setQcCode(String qcCode) {
        this.qcCode = qcCode;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        WxAccount wxAccount = (WxAccount) o;

        if (!biz.equals(wxAccount.biz)) return false;
        return id.equals(wxAccount.id);

    }

    @Override
    public int hashCode() {
        int result = biz.hashCode();
        result = 31 * result + id.hashCode();
        return result;
    }

    @Override
    public String toString() {
        return "WxAccount{" +
                "biz='" + biz + '\'' +
                ", brief='" + brief + '\'' +
                ", headImg='" + headImg + '\'' +
                ", nickName='" + nickName + '\'' +
                ", id='" + id + '\'' +
                ", qcCode='" + qcCode + '\'' +
                '}';
    }
}
