package com.ptb.uranus.server.send.entity.article;

/**
 * Created by watson zhang on 16/4/27.
 */
public class WeiboArticleStatic extends BasicArticleStatic{
    private String weiboId;
    private String type;
    private String picture;
    private int isOrignal;


    public int getIsOrignal() {
        return isOrignal;
    }

    public void setIsOrignal(int isOrignal) {
        this.isOrignal = isOrignal;
    }

    public String getWeiboId() {
        return weiboId;
    }

    public void setWeiboId(String weiboId) {
        this.weiboId = weiboId;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getPicture() {
        return picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }
}
