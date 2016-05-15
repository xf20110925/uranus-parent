package com.ptb.uranus.spider.common.utils;


import com.ptb.uranus.spider.weixin.bean.WxAccount;
import com.ptb.uranus.spider.weixin.bean.WxArticle;

/**
 * Created by eric on 15/12/8.
 * 转换工具类,用来将一些对象转换成另外一些对象时使用
 *
 */
public class ConvertUtil {
    /**
     * Convert to wx account wx account.
     * 将WxArticle对象转换成WxAccount对象
     *
     * @param article the wxarticle
     * @return the wxaccount
     */
    public static WxAccount convertToWxAccount(WxArticle article) {
        return new WxAccount(article.getBiz(), article.getBrief(), article.getHeadImgUrl(), article.getNickName(), article.getId(), article.getQrCode());
    }
}
