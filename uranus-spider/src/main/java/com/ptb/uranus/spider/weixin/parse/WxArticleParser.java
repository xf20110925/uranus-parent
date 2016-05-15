package com.ptb.uranus.spider.weixin.parse;


import com.alibaba.fastjson.JSON;
import com.jayway.jsonpath.DocumentContext;
import com.jayway.jsonpath.JsonPath;
import com.ptb.uranus.spider.common.utils.HttpUtil;
import com.ptb.uranus.spider.weixin.WeixinUtil;
import com.ptb.uranus.spider.weixin.bean.ReadLikeNum;
import com.ptb.uranus.spider.weixin.bean.WxArticle;
import com.ptb.utils.string.RegexUtils;
import org.apache.log4j.Logger;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by eric on 15/12/8.
 */
public class WxArticleParser {
    Pattern bizRegex = Pattern.compile(".*var appuin = \"([^\"]*)\".*");
    Pattern headImgRegex = Pattern.compile(".*hd_head_img : \"([^\"]*)\".*");
    Pattern originalLinkRegex = Pattern.compile(".*var msg_source_url = \'([^\']*)\'.*", Pattern.DOTALL);
    Pattern coverImgRegex = Pattern.compile(".*var msg_cdn_url = \"([^\"]*)\".*");
    /**
     * The Log.
     */
    static final Logger log = Logger.getLogger(WxArticleParser.class);
    /**
     * The Wx utils.
     */
    WeixinUtil wxUtils;

    /**
     * Instantiates a new Wx article parser.
     */
    public WxArticleParser() {
        wxUtils = WeixinUtil.instance();
    }

    private WxArticle parseArticlByPageSource(String pageSource) {



        Document html = Jsoup.parse(pageSource);
        String title = html.select(".rich_media_title").first().text();
        String nickName = html.select(".profile_nickname").text();
        String author = html.select("#post-user").text();
        String content = html.select("#js_content").html();
        String wxid = html.select(".profile_inner>p:nth-of-type(1)>.profile_meta_value").text().toString();
        String brief = html.select(".profile_inner>p:nth-of-type(2)>.profile_meta_value").text().toString();
        //String postDate = html.select("#post-date").text();
        long postTime = 0;
        String biz = "";
        String headImgUrl = "";
        //原文链接
        String sourceLink = "";
        //原创标志
        String originalTag = html.select("#copyright_logo").text().toString();
        boolean isOrinal = originalTag.trim().equals("原创");
        //封面图
        String coverImgUrl = "";

        Elements scripts = html.select("script[type*=\"text/javascript\"]");
        for (Element script : scripts) {
            String text = script.toString();
            if (text.contains("var appuin =")) {
                Matcher matcher = bizRegex.matcher(text);
                if (matcher.find() && matcher.groupCount() > 0) {
                    biz = matcher.group(1);
                }

                Matcher matcher1 = headImgRegex.matcher(text);
                if (matcher1.find() && matcher1.groupCount() > 0) {
                    headImgUrl = matcher1.group(1);
                }
                String sub = RegexUtils.sub(".*var ct = \"([^\"]*)\";.*", text, 0);
                if (sub != null) {
                    postTime = Long.parseLong(sub);
                }
                if (wxid == null || wxid.length() == 0) {
                    //wxid = RegexUtils.sub(".*var user_name = \"([^\"]*)\";.*", text, 0);
                    wxid = "";
                }
                Matcher matcher2 = originalLinkRegex.matcher(text);
                if (matcher2.find() && matcher2.groupCount() > 0) {
                    sourceLink = matcher2.group(1);
                }
                Matcher matcher3 = coverImgRegex.matcher(text);
                if (matcher3.find() && matcher3.groupCount() > 0) {
                    coverImgUrl = matcher3.group(1);
                }
            }

        }

        WxArticle article = new WxArticle(title, nickName, content, wxid, brief, postTime, biz, headImgUrl, author);
        article.setSourceLink(sourceLink);
        article.setOriginal(isOrinal);
        article.setCoverImgUrl(coverImgUrl);
        return article;
    }

    /**
     * Gets article no rd num by article url.
     *
     * @param articleUrl the article url
     * @return the article no rd num by article url
     * @throws IOException the io exception
     */
    public WxArticle getArticleNoRdNumByArticleUrl(String articleUrl) throws IOException {
        String pageSource = HttpUtil.getPageByPcClient(articleUrl).toString();
        WxArticle wxArticle = parseArticlByPageSource(pageSource);
        wxArticle.setArticleUrl(articleUrl);
        return wxArticle;
    }
    public ReadLikeNum parseReadLikeNumByPageSource(String pageSource){
        Document html = Jsoup.parse(pageSource);
        String readNum = html.select("#readNum3").text();
        String likeNum = html.select("#likeNum3").text();
        return new ReadLikeNum(Integer.parseInt(readNum), Integer.parseInt(likeNum));
    }

    public ReadLikeNum parseReadLikeNumByJson(String s) {
        DocumentContext parse = JsonPath.parse(s);
        int readNum = -1, likeNum = -1;
        try{
            readNum = parse.read("$.appmsgstat.read_num");
            likeNum = parse.read("$.appmsgstat.like_num");
            int realReadNum = parse.read("$.appmsgstat.real_read_num");
            readNum = realReadNum > 0 ? realReadNum: readNum;

        }catch (Exception e){
            System.out.println("parseReadLikeNumByJson "+JSON.toJSON(parse)+e);
        }

        return new ReadLikeNum(readNum,likeNum);
    }
}

