package com.ptb.uranus.server.send.entity.convert;

import com.ptb.ourea.function.TextAnalyzeResult;
import com.ptb.ourea.function.TextAnalyzer;
import com.ptb.ourea.function.TextAnalyzerFactory;
import com.ptb.ourea.function.TextAnalyzerFactory.AnalyzerType;
import com.ptb.uranus.server.send.entity.article.BasicArticleDynamic;
import com.ptb.uranus.server.send.entity.article.BasicArticleStatic;
import com.ptb.uranus.server.send.entity.article.WeiboArticleStatic;
import com.ptb.uranus.server.send.entity.article.WeixinArticleStatic;
import com.ptb.uranus.server.send.entity.media.BasicMediaDynamic;
import com.ptb.uranus.server.send.entity.media.WeiboMediaDynamic;
import com.ptb.uranus.server.send.entity.media.WeiboMediaStatic;
import com.ptb.uranus.server.send.entity.media.WeixinMediaStatic;
import com.ptb.uranus.spider.smart.entity.Article;
import com.ptb.uranus.spider.smart.entity.DynamicData;
import com.ptb.uranus.spider.weibo.bean.WeiboAccount;
import com.ptb.uranus.spider.weibo.bean.WeiboArticle;
import com.ptb.uranus.spider.weixin.bean.ReadLikeNum;
import com.ptb.uranus.spider.weixin.bean.WxAccount;
import com.ptb.uranus.spider.weixin.bean.WxArticle;
import org.apache.commons.configuration.ConfigurationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Optional;

/**
 * Created by watson zhang on 16/4/26.
 */
public class SendObjectConvertUtil {
    private static Logger logger = LoggerFactory.getLogger(SendObjectConvertUtil.class);
    private static TextAnalyzer textAnalyzer;

    static {
        try {
            textAnalyzer = TextAnalyzerFactory.create(AnalyzerType.ANJS);
        } catch (ConfigurationException e) {
            e.printStackTrace();
        }
    }

    public SendObjectConvertUtil(){
    }

    public static WeixinMediaStatic weixinMediaStaticConvert(WxAccount wxAccount, Optional<String> identify) {
        WeixinMediaStatic weixinMediaStatic = new WeixinMediaStatic();
        weixinMediaStatic.setPlat(1);
        weixinMediaStatic.setMediaName(wxAccount.getNickName());
        weixinMediaStatic.setBiz(wxAccount.getBiz());
        if (identify.equals(Optional.empty())) {
            weixinMediaStatic.setAuthentication("");
        } else {
            weixinMediaStatic.setAuthentication(identify.get());
        }
        weixinMediaStatic.setOriginal(false);
        weixinMediaStatic.setBrief(wxAccount.getBrief());
        weixinMediaStatic.setHeadImg(wxAccount.getHeadImg());
        weixinMediaStatic.setQrCode(wxAccount.getQcCode());
        weixinMediaStatic.setWeixinId(wxAccount.getId());
        return weixinMediaStatic;
    }

    public static WeixinArticleStatic weixinArticleStaticConvert(WxArticle wxArticle) {
        TextAnalyzeResult textAnalyzeResult;
        logger.error("weixin article static!1");
        textAnalyzeResult = textAnalyzer.ArticleAnalyze(wxArticle.getContent());
        logger.error("weixin article static!2");
        WeixinArticleStatic weixinArticleStatic = new WeixinArticleStatic();
        logger.error("weixin article static!3");
        weixinArticleStatic.setUrl(wxArticle.getArticleUrl());
        weixinArticleStatic.setSurface(wxArticle.getCoverImgUrl());
        weixinArticleStatic.setAuthor(wxArticle.getAuthor());
        weixinArticleStatic.setClassify("");
        weixinArticleStatic.setContent(wxArticle.getContent());
        weixinArticleStatic.setPlat(1);
        weixinArticleStatic.setPostTime(wxArticle.getPostTime());
        weixinArticleStatic.setSource(wxArticle.getSourceLink());
        weixinArticleStatic.setTitle(wxArticle.getTitle());
        weixinArticleStatic.setSplitwords(textAnalyzeResult.getSplitword());
        weixinArticleStatic.setKeywords(textAnalyzeResult.getAbstracts());
        weixinArticleStatic.setLinks(textAnalyzeResult.getHyperLink());
        weixinArticleStatic.setOriginal(wxArticle.isOriginal());
        weixinArticleStatic.setBiz(wxArticle.getBiz());
        weixinArticleStatic.setWeixinId(wxArticle.getId());
        return weixinArticleStatic;
    }

    public static BasicArticleDynamic wxArticleDynamicConvert(ReadLikeNum readLikeNum) {
        BasicArticleDynamic wxAritcleDynamic = new BasicArticleDynamic();
        wxAritcleDynamic.setReads(readLikeNum.getReadNum());
        wxAritcleDynamic.setLikes(readLikeNum.getLikeNum());
        wxAritcleDynamic.setPlat(1);
        wxAritcleDynamic.setTime(System.currentTimeMillis());
        return wxAritcleDynamic;
    }

    public static WeiboMediaStatic weiboMediaStaticConvert(WeiboAccount weiboAccount) {
        WeiboMediaStatic weiboMediaStatic = new WeiboMediaStatic();
        weiboMediaStatic.setWeiboId(weiboAccount.getWeiboID());
        weiboMediaStatic.setPlat(2);
        weiboMediaStatic.setMediaName(weiboAccount.getNickName());
        weiboMediaStatic.setAuthType(weiboAccount.getVerifiedType());
        weiboMediaStatic.setAuthDescription(weiboAccount.getVerifiedReason());
        weiboMediaStatic.setBrief(weiboAccount.getDesc());
        weiboMediaStatic.setGender(weiboAccount.getGender());
        weiboMediaStatic.setHeadImg(weiboAccount.getHeadImg());
        weiboMediaStatic.setHomePage("http://m.weibo.cn/u/" + weiboAccount.getWeiboID());
        weiboMediaStatic.setLocation(weiboAccount.getActivePlace()); //???
        weiboMediaStatic.setRegisterTime(weiboAccount.getCreate_time());
        weiboMediaStatic.setTagList("");
        return weiboMediaStatic;
    }

    public static BasicArticleDynamic weiboArticleDynamicConvert(WeiboArticle wbArticle) {
        BasicArticleDynamic wbArticleDynamic = new BasicArticleDynamic();
        wbArticleDynamic.setUrl(wbArticle.getArticleUrl());
        wbArticleDynamic.setComments(wbArticle.getCommentCount());
        wbArticleDynamic.setLikes(wbArticle.getLikeCount());
        wbArticleDynamic.setForwards(wbArticle.getRepostCount());
        wbArticleDynamic.setPlat(2);
        return wbArticleDynamic;
    }

    public static BasicArticleDynamic commonArticleDyanmicConvert(DynamicData dynamicData, String url) {
        BasicArticleDynamic basicArticleDynamic = new BasicArticleDynamic();
        basicArticleDynamic.setForwards(dynamicData.getShareNum());
        basicArticleDynamic.setComments(dynamicData.getCommentNum());
        basicArticleDynamic.setLikes(dynamicData.getLikeNum());
        basicArticleDynamic.setJoins(dynamicData.getInvolvementNum());
        basicArticleDynamic.setNolikes(dynamicData.getBoringNum());
        basicArticleDynamic.setReads(dynamicData.getReadNum());
        basicArticleDynamic.setPlat(dynamicData.getPlat());
        basicArticleDynamic.setUrl(url);
        return basicArticleDynamic;
    }

    public static WeiboArticleStatic weiboArticleStaticConvert(WeiboArticle weiboArticle) {
        TextAnalyzeResult textAnalyzeResult;
        textAnalyzeResult = textAnalyzer.ArticleAnalyze(weiboArticle.getContent());
        WeiboArticleStatic weiboArticleStatic = new WeiboArticleStatic();
        weiboArticleStatic.setWeiboId(weiboArticle.getMediaId());
        if ((weiboArticle.getImgs() != null) && (weiboArticle.getImgs().size()) > 0) {
            weiboArticleStatic.setPicture(weiboArticle.getImgs().get(0));
        } else if ((weiboArticle.getVideos() != null)&&(weiboArticle.getVideos().size()) > 0) {
            weiboArticleStatic.setPicture(weiboArticle.getVideos().get(0));
        } else {
            weiboArticleStatic.setPicture("");
        }
        weiboArticleStatic.setType((weiboArticle.getObjectType() == null)?"":weiboArticle.getObjectType());
        weiboArticleStatic.setKeywords(textAnalyzeResult.getAbstracts());
        weiboArticleStatic.setTitle("");
        weiboArticleStatic.setAuthor(weiboArticle.getMediaName());
        weiboArticleStatic.setClassify(weiboArticle.getObjectType());
        weiboArticleStatic.setContent(weiboArticle.getContent());

        if ((weiboArticle.getImgs() != null)&&(weiboArticle.getImgs().size()) > 0) {
            for (String iter : weiboArticle.getImgs()) {
                textAnalyzeResult.getHyperLink().put(iter, "");
            }
        }
        if ((weiboArticle.getVideos() != null)&&(weiboArticle.getVideos().size()) > 0) {
            for (String iter : weiboArticle.getVideos()) {
                textAnalyzeResult.getHyperLink().put(iter, "");
            }
        } else {
            textAnalyzeResult.getHyperLink().put("", "");
        }
        weiboArticleStatic.setLinks(textAnalyzeResult.getHyperLink());
        weiboArticleStatic.setPlat(2);
        weiboArticleStatic.setPostTime(weiboArticle.getPostTime());
        weiboArticleStatic.setSource(weiboArticle.getSource());
        weiboArticleStatic.setSplitwords(textAnalyzeResult.getSplitword());
        weiboArticleStatic.setUrl(weiboArticle.getArticleUrl());

        return weiboArticleStatic;

    }


    public static BasicArticleStatic commonMediaArticleStaticConvert(Article article) {
        TextAnalyzeResult textAnalyzeResult = textAnalyzer.ArticleAnalyze(article.getContent());
        BasicArticleStatic basicArticleStatic = new BasicArticleStatic();
        basicArticleStatic.setAuthor(article.getAuthor());
        basicArticleStatic.setClassify(article.getClassify());
        basicArticleStatic.setContent(article.getContent());
        basicArticleStatic.setPlat(article.getPlat());
        basicArticleStatic.setSource(article.getSource());
        basicArticleStatic.setTitle(article.getTitle());
        basicArticleStatic.setUrl(article.getUrl());
        basicArticleStatic.setPostTime(article.getPostTime());
        basicArticleStatic.setLinks(textAnalyzeResult.getHyperLink());
        basicArticleStatic.setKeywords(textAnalyzeResult.getAbstracts());
        basicArticleStatic.setSplitwords(textAnalyzeResult.getSplitword());
        return basicArticleStatic;
    }

    public static BasicMediaDynamic weiboMediaDynamicConvert(WeiboAccount weiboAccount){
        WeiboMediaDynamic weiboMediaDynamic = new WeiboMediaDynamic();
        weiboMediaDynamic.setTime(System.currentTimeMillis()/1000L);
        weiboMediaDynamic.setWeiboId(weiboAccount.getWeiboID());
        weiboMediaDynamic.setPlat(2);
        weiboMediaDynamic.setFans(weiboAccount.getFansNum());
        weiboMediaDynamic.setPostArticles(weiboAccount.getMblogNum());
        weiboMediaDynamic.setConcerns(weiboAccount.getAttNum());
        return weiboMediaDynamic;
    }
}
