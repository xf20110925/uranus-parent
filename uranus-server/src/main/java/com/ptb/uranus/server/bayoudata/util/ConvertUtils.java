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

import java.util.Map;

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

    public static WeixinArticleStatic convertWXArticleStatic(Map<String, String> articleMap) {
        String pageSource = articleMap.get("content");
        WxArticle wxArticle = wxArticleParser.parseArticlByPageSource(pageSource);
        wxArticle.setArticleUrl(articleMap.get("url"));
        return SendObjectConvertUtil.weixinArticleStaticConvert(wxArticle);
    }

    public static BasicArticleDynamic convertWXArticleDynamic(BayouWXArticleDynamic bayouDynamic){
        BasicArticleDynamic wxAritcleDynamic = new BasicArticleDynamic();
        wxAritcleDynamic.setReads(bayouDynamic.getRead_num());
        wxAritcleDynamic.setLikes(bayouDynamic.getLike_num());
        wxAritcleDynamic.setPlat(1);
        wxAritcleDynamic.setTime(System.currentTimeMillis());
        wxAritcleDynamic.setUrl(bayouDynamic.getUrl());
        return wxAritcleDynamic;
    }
}
