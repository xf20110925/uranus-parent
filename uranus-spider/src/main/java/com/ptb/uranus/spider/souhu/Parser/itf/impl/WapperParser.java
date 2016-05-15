package com.ptb.uranus.spider.souhu.Parser.itf.impl;

import com.ptb.uranus.spider.souhu.Parser.itf.BaseParser;
import com.ptb.uranus.spider.souhu.utils.HttpUtil;
import com.ptb.uranus.spider.souhu.bean.NewsInfo;
import com.ptb.uranus.spider.souhu.utils.CommonUtil;
import org.apache.commons.lang.StringUtils;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by xuefeng on 2016/3/15.
 */
public class WapperParser extends BaseParser {
    @Override
    public NewsInfo getNewsInfo(String url, String html){
        Document doc = Jsoup.parse(html);
        Element contentWrapper = doc.select("div.content-wrapper").first();
        Element newsClear = contentWrapper.select("div.news-title").first();
        String title = newsClear.select("h1").first().text();
        String pubTime = newsClear.select("span.time").first().text();
        String media = newsClear.select("span.writer").text();
        String content = contentWrapper.getElementById("contentText").toString();
        NewsInfo newsInfo = new NewsInfo();
        newsInfo.setContent(content);
        newsInfo.setPubTime(pubTime);
        newsInfo.setMedia(media);
        newsInfo.setTitle(title);
        newsInfo.setMedia(media);
        Map<String, Integer> jsInfo = getJsInfo(url, html);
        if (jsInfo != null && !jsInfo.isEmpty()){
            newsInfo.setReadNum(jsInfo.get("readNum"));
            newsInfo.setLikeNum(jsInfo.get("likeNum"));
            newsInfo.setTreadNum(jsInfo.get("treadNum"));
            newsInfo.setParticipationNum(jsInfo.get("participationNum"));
            newsInfo.setCommentNum(jsInfo.get("commentNum"));
        }
        return  newsInfo;
    }

    @Override
    public Map<String, Integer> getJsInfo(String url, String html) {
        String newsId = CommonUtil.extractStr(url,"http://.*?/n(\\d+).shtml");
        String jsInfoUrl = "http://changyan.sohu.com/node/html?client_id=cyqemw6s1&topicsid={newsId}";
        jsInfoUrl = jsInfoUrl.replace("{newsId}", newsId);
        String jsInfo = HttpUtil.httpGet(jsInfoUrl);
        Map<String, Integer> dataMap = getRPNum(jsInfo);
        Document doc = Jsoup.parse(html);
        Elements scripts = doc.select("script");
        String mpId = "";
        for (Element script : scripts) {
            mpId = CommonUtil.extractStr(script.data(), ".*?mpId=(.*?);.*?" );
            if (StringUtils.isNotBlank(mpId)){
                break;
            }
        }
        try {
            dataMap.putAll(getArticleDetail(newsId));
            if (StringUtils.isNotBlank(mpId)){
                Map<String, Integer> richInfo = getRichInfo(newsId, mpId);
                dataMap.putAll(richInfo);
            }
            return dataMap;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     *数据格式：jQuery({"code":0,"data":{"cmsid":439474355,"liked":0,"likes":53,"treaded":0,"treads":2},
     *          "msg":"get article detail successfully"})
     * @param newsId
     * @return  获取文章的喜欢人数和没劲人数
     * @throws Exception
     */
    public static Map<String,Integer> getArticleDetail(String newsId) throws Exception {
        String articleDetailUrl = "http://life.sohu.com/fapi/jsonp/getarticledetail?callback=jQuery&cmsid={newsId}&_={timeMillions}";
        articleDetailUrl = articleDetailUrl.replace("{newsId}", newsId).replace("{timeMillions}", new Date().getTime() + "");
        String articleDetail = HttpUtil.httpGet(articleDetailUrl);
        if (StringUtils.isNotBlank(articleDetail)){
            String data = CommonUtil.extractStr(articleDetail, ".*?\"data\":(.*?),\"msg\"");
            if (StringUtils.isNotBlank(data)){
                JSONObject jsonData = new JSONObject(data);
                Map<String,Integer> dataMap = new HashMap<>();
                dataMap.put("likeNum", jsonData.getInt("likes"));
                dataMap.put("treadNum", jsonData.getInt("treads"));
                return dataMap;
            }
        }
        return  null;
    }

    /**
     * 数据格式：jQuery({"status":true,"data":{"mpid":355831,"newsid":439474355,"mppv":164780,"newspv":106,"newspass":150,"commentcnt":3,"mplevel":70,"rank":3,
     * "profileurl":"http://mp.i.sohu.com/profile?xpt=c29odW1wODNURjFWQHNvaHUuY29t","certtype":1,"certdesc":""}});
     * @param newsId
     * @param mpId
     * @return 阅读人数
     */
    public static Map<String,Integer> getRichInfo(String newsId, String mpId){
        String richInfoUrl = "http://mp.sohu.com/openapi/profile/getRichInfo?cb=jQuery&mpId={mpId}&newsId={newsId}&_={timeMillions}";
        richInfoUrl = richInfoUrl.replace("{mpId}", mpId).replace("{newsId}", newsId).replace("{timeMillions}", new Date().getTime() + "");
        String richInfo = HttpUtil.httpGet(richInfoUrl);
        if (StringUtils.isNotBlank(richInfo)){
            String readNum = CommonUtil.extractStr(richInfo, ".*?\"newspv\":(\\d+),.*?");
            Map<String, Integer> dataMap = new HashMap<>();
            if (StringUtils.isNotBlank(readNum)) {
                dataMap.put("readNum", Integer.parseInt(readNum));
            }
            return dataMap;
        }
        return null;
    }

}
