package com.ptb.uranus.server.bayoudata.util;

import com.ptb.uranus.server.bayoudata.entity.BayouWXArticleDynamic;
import com.ptb.uranus.server.bayoudata.entity.BayouWXArticleStatic;
import com.ptb.uranus.server.bayoudata.entity.BayouWXMedia;
import com.ptb.uranus.server.send.entity.article.BasicArticleDynamic;
import com.ptb.uranus.server.send.entity.article.WeixinArticleStatic;
import com.ptb.uranus.server.send.entity.convert.SendObjectConvertUtil;
import com.ptb.uranus.server.send.entity.media.WeixinMediaStatic;
import com.ptb.uranus.spider.weixin.bean.WxArticle;
import com.ptb.uranus.spider.weixin.parse.WxArticleParser;

/**
 * Created by xuefeng on 2016/5/17.
 */
public class ConvertUtils {

    static WxArticleParser wxArticleParser = new WxArticleParser();

    public static WeixinMediaStatic convertWXMedia(BayouWXMedia wxMedia) {
        WeixinMediaStatic weixinMediaStatic = new WeixinMediaStatic();
        weixinMediaStatic.setPlat(1);
        weixinMediaStatic.setMediaName(wxMedia.getName());
        weixinMediaStatic.setBiz(wxMedia.getBid());
//        weixinMediaStatic.setOriginal(false);
//        weixinMediaStatic.setAuthentication(identify.get());
        weixinMediaStatic.setBrief(wxMedia.getInfo());
        weixinMediaStatic.setHeadImg(wxMedia.getHeadImage());
        weixinMediaStatic.setQrCode(wxMedia.getQrcode());
        weixinMediaStatic.setWeixinId(wxMedia.getCode());
        return weixinMediaStatic;
    }

    public static WeixinArticleStatic convertWXArticleStatic(BayouWXArticleStatic wxArticleStatic) {
        String pageSource = wxArticleStatic.getContent();
        WxArticle wxArticle = wxArticleParser.parseArticlByPageSource(pageSource);
        return SendObjectConvertUtil.weixinArticleStaticConvert(wxArticle);
    }

    public static BasicArticleDynamic convertWXArticleDynamic(BayouWXArticleDynamic wxArticleDynamic){
        BasicArticleDynamic wxAritcleDynamic = new BasicArticleDynamic();
        wxAritcleDynamic.setReads(wxArticleDynamic.getRead_num());
        wxAritcleDynamic.setLikes(wxArticleDynamic.getLike_num());
        wxAritcleDynamic.setPlat(1);
        wxAritcleDynamic.setTime(System.currentTimeMillis());
        return wxAritcleDynamic;
    }
}
