package com.ptb.uranus.spider.souhu.Parser.itf.impl;

import com.ptb.uranus.spider.souhu.Parser.itf.BaseParser;
import com.ptb.uranus.spider.souhu.utils.HttpUtil;
import com.ptb.uranus.spider.souhu.bean.NewsInfo;
import com.ptb.uranus.spider.souhu.utils.CommonUtil;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.util.Map;

/**
 * Created by xuefeng on 2016/3/15.
 */
public class BoxClearParser extends BaseParser {
    @Override
    public NewsInfo getNewsInfo(String url, String html){
        Document doc = Jsoup.parse(html);
        Element contentBox = doc.select("div.content-box.clear").first();
        String title = contentBox.select("h1").first().text();
        Element timeSource = contentBox.select("div.time-source").first();
        String pubTime = timeSource.select("div.time").first().text();
        String media = timeSource.getElementById("media_span").text();
        String content = contentBox.getElementById("contentText").toString();
        NewsInfo newsInfo = new NewsInfo();
        newsInfo.setContent(content);
        newsInfo.setPubTime(pubTime);
        newsInfo.setMedia(media);
        newsInfo.setTitle(title);
        newsInfo.setMedia(media);
        Map<String, Integer> jsInfo = getJsInfo(url, null);
        if (jsInfo != null && !jsInfo.isEmpty()){
            newsInfo.setParticipationNum(jsInfo.get("participationNum"));
            newsInfo.setCommentNum(jsInfo.get("commentNum"));
        }
        return  newsInfo;
    }

    @Override
    public Map<String, Integer> getJsInfo(String url, String html) {
        String jsInfoUrl = "http://changyan.sohu.com/node/html?client_id=cyqemw6s1&topicsid={newsId}";
        String newsId = CommonUtil.extractStr(url,"http://.*?/n(\\d+).shtml");
        jsInfoUrl = jsInfoUrl.replace("{newsId}", newsId);
        String jsInfo = HttpUtil.httpGet(jsInfoUrl);
        return getRPNum(jsInfo);
    }


}
