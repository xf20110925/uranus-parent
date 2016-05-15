package com.ptb.uranus.server.send.entity.article;

/**
 * Created by watson zhang on 16/4/27.
 */
public class WeiboArticleStatic extends BasicArticleStatic{
    private String weiboId;
    private String articleType;
    private String picture;

    public String getWeiboId() {
        return weiboId;
    }

    public void setWeiboId(String weiboId) {
        this.weiboId = weiboId;
    }

    public String getArticleType() {
        return articleType;
    }

    public void setArticleType(String articleType) {
        this.articleType = articleType;
    }

    public String getPicture() {
        return picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }
}
