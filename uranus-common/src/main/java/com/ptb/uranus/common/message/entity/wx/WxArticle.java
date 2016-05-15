package com.ptb.uranus.common.message.entity.wx;

/**
 * Created by eric on 16/1/12.
 */
public class WxArticle {
    private String articleUrl;
    private String qrCode;
    private String biz;
    private String headImgUrl;
    private String nickName;

    private String id;
    private String brief;
    private long postTime;
    private String title;
    private String content;
    private String author;

    private String sourceLink;
    private boolean isOriginal;
    //封面图
    private String coverImgUrl;

    private String featureiImg;

    /**
     * Instantiates a new Wx article.
     *
     * @param title      the title
     * @param nickName   the nick name
     * @param content    the content
     * @param wxid       the wxid
     * @param brief      the brief
     * @param postTime   the post time
     * @param biz        the biz
     * @param headImgUrl the head img url
     * @param author     the author
     */
    public WxArticle(String title, String nickName, String content, String wxid, String brief, long postTime, String biz, String headImgUrl, String author) {
        this.title = title;
        this.nickName = nickName;
        this.content = content;
        this.id = wxid;
        this.brief = brief;
        this.postTime = postTime;
        this.biz = biz;
        this.headImgUrl = headImgUrl;
        this.qrCode = String.format("http://mp.weixin.qq.com/mp/qrcode?scene=10000001&size=102&__biz=%s", biz);
        this.author = author;
    }

    /**
     * Instantiates a new Wx article.
     */
    public WxArticle() {
    }

    public String getFeatureiImg() {
        return featureiImg;
    }

    public void setFeatureiImg(String featureiImg) {
        this.featureiImg = featureiImg;
    }

    /**
     * Gets author.
     *
     * @return the author
     */
    public String getAuthor() {
        return author;
    }

    /**
     * Sets author.
     *
     * @param author the author
     */
    public void setAuthor(String author) {
        this.author = author;
    }

    /**
     * Gets article url.
     *
     * @return the article url
     */
    public String getArticleUrl() {
        return articleUrl;
    }

    /**
     * Sets article url.
     *
     * @param articleUrl the article url
     */
    public void setArticleUrl(String articleUrl) {
        this.articleUrl = articleUrl;
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
     * Gets qr code.
     *
     * @return the qr code
     */
    public String getQrCode() {
        return qrCode;
    }

    /**
     * Sets qr code.
     *
     * @param qrCode the qr code
     */
    public void setQrCode(String qrCode) {
        this.qrCode = qrCode;
    }

    /**
     * Gets head img url.
     *
     * @return the head img url
     */
    public String getHeadImgUrl() {
        return headImgUrl;
    }

    /**
     * Sets head img url.
     *
     * @param headImgUrl the head img url
     */
    public void setHeadImgUrl(String headImgUrl) {
        this.headImgUrl = headImgUrl;
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
     * Gets nick name.
     *
     * @return the nick name
     */
    public String getNickName() {
        return nickName;
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
     * Gets brief.
     *
     * @return the brief
     */
    public String getBrief() {
        return brief;
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
     * Sets content.
     *
     * @param content the content
     */
    public void setContent(String content) {
        this.content = content;
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
     * Sets id.
     *
     * @param id the id
     */
    public void setId(String id) {
        this.id = id;
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
     * Gets post time.
     *
     * @return the post time
     */
    public long getPostTime() {
        return postTime;
    }

    /**
     * Sets post time.
     *
     * @param postTime the post time
     */
    public void setPostTime(long postTime) {
        this.postTime = postTime;
    }

    public boolean isOriginal() {
        return isOriginal;
    }

    public void setOriginal(boolean original) {
        isOriginal = original;
    }

    public String getSourceLink() {
        return sourceLink;
    }

    public void setSourceLink(String sourceLink) {
        this.sourceLink = sourceLink;
    }

    public String getCoverImgUrl() {
        return coverImgUrl;
    }

    public void setCoverImgUrl(String coverImgUrl) {
        this.coverImgUrl = coverImgUrl;
    }

    @Override
    public String toString() {
        return "WxArticle{" +
                "articleUrl='" + articleUrl + '\'' +
                ", qrCode='" + qrCode + '\'' +
                ", biz='" + biz + '\'' +
                ", headImgUrl='" + headImgUrl + '\'' +
                ", nickName='" + nickName + '\'' +
                ", id='" + id + '\'' +
                ", brief='" + brief + '\'' +
                ", postTime=" + postTime +
                ", title='" + title + '\'' +
                ", content='" + content + '\'' +
                ", author='" + author + '\'' +
                ", sourceLink='" + sourceLink + '\'' +
                ", isOriginal=" + isOriginal +
                ", coverImgUrl='" + coverImgUrl + '\'' +
                '}';
    }
}
