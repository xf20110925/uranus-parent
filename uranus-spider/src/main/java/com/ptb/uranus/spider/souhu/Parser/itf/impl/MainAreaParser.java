package com.ptb.uranus.spider.souhu.Parser.itf.impl;

import com.ptb.uranus.spider.souhu.Parser.itf.BaseParser;
import com.ptb.uranus.spider.souhu.bean.NewsInfo;
import com.ptb.uranus.spider.souhu.utils.CommonUtil;
import com.ptb.uranus.spider.souhu.utils.HttpUtil;
import org.apache.commons.lang.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.Map;

/**
 * Created by xuefeng on 2016/3/16.
 */
public class MainAreaParser extends BaseParser {
    @Override
    public NewsInfo getNewsInfo(String url, String html){
        Document doc = Jsoup.parse(html);
        Element header = doc.getElementById("headers");
        String title = header.select("div.title").first().text();
        Element timeTag = header.select("div.timeTag").first();
        String pubTime = timeTag.select("span.time").text();
        Element main = doc.getElementById("main");
        String media = main.select("div.left").first().text();
        String content = main.getElementById("contentText").toString();
        NewsInfo newsInfo = new NewsInfo();
        newsInfo.setContent(content);
        newsInfo.setPubTime(pubTime);
        newsInfo.setMedia(media);
        newsInfo.setTitle(title);
        newsInfo.setMedia(media);
        Map<String, Integer> jsInfo = getJsInfo(url, html);
        if (jsInfo != null && !jsInfo.isEmpty()){
            newsInfo.setParticipationNum(jsInfo.get("participationNum"));
            newsInfo.setCommentNum(jsInfo.get("commentNum"));
        }
        return  newsInfo;
    }

    @Override
    public Map<String, Integer> getJsInfo(String url, String html) {
        String newsId = CommonUtil.extractStr(url,"http://.*?/n(\\d+).shtml");
        Document doc = Jsoup.parse(html);
        Elements scripts = doc.select("script");
        String appId = "";
        for (Element script : scripts) {
            appId = CommonUtil.extractStr(script.data(), ".*?appid = '(.*?)',.*?" );
            if (StringUtils.isNotBlank(appId)){
                break;
            }
        }
        String jsInfoUrl = "http://changyan.sohu.com/node/html?client_id={appId}&topicsid={newsId}";
        jsInfoUrl = jsInfoUrl.replace("{newsId}", newsId).replace("{appId}", appId);
        String jsInfo = HttpUtil.httpGet(jsInfoUrl);
        return getRPNum(jsInfo);
    }

}
