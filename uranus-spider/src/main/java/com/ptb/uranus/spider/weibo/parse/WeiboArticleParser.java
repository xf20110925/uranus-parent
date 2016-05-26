package com.ptb.uranus.spider.weibo.parse;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.jayway.jsonpath.DocumentContext;
import com.jayway.jsonpath.JsonPath;
import com.ptb.uranus.spider.common.utils.HttpUtil;
import com.ptb.uranus.spider.common.utils.JsonPathUtil;
import com.ptb.uranus.spider.common.utils.WeiboUtil;
import com.ptb.uranus.spider.common.webDriver.WebDriverPool;
import com.ptb.uranus.spider.common.webDriver.WebDriverPoolUtils;
import com.ptb.uranus.spider.weibo.bean.WeiboArticle;
import com.ptb.utils.exception.PTBException;
import com.ptb.utils.string.RegexUtils;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.slf4j.Logger;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.openqa.selenium.Cookie;
import org.openqa.selenium.phantomjs.PhantomJSDriver;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * Created by eric on 16/2/1.
 * 解析weibo文章主要工作类
 */
public class WeiboArticleParser {
    static Logger logger = LoggerFactory.getLogger(WeiboArticleParser.class);

    public WeiboArticle parseFromMobilePage(String articleUrl) throws IOException {
        WeiboArticle weiboArticle = new WeiboArticle();
        String articleID = RegexUtils.sub("http://.*/[\\d]*/([^\\?]*)", articleUrl, 0);
        String weiboID = RegexUtils.sub("http://.*/([\\d]*)/[^\\?]*", articleUrl, 0);
        String url = String.format("http://m.weibo.cn/%s/%s", weiboID, articleID);

        String pageSouce = HttpUtil.getPageSourceByClient(url, HttpUtil.UA_IPHONE6_SAFARI, WeiboUtil.getVaildWeiboCookieStore(), "utf-8", "created_timestamp");
        DocumentContext parse = JsonPath.parse(RegexUtils.sub(".*window\\.\\$render_data = (\\{.*);</script>", pageSouce, 0));

        int repostCount = Integer.parseInt(JsonPathUtil.parse(parse, "$.stage.single[1].mblog.reposts_count", null));
        int comments_count = Integer.parseInt(JsonPathUtil.parse(parse, "$.stage.single[1].mblog.comments_count", null));
        int like_count = Integer.parseInt(JsonPathUtil.parse(parse, "$.stage.single[1].mblog.attitudes_count", null));
        int postTime = Integer.parseInt(JsonPathUtil.parse(parse, "$.stage.single[1].mblog.created_timestamp", null));

        String content = JsonPathUtil.parse(parse, "$.stage.single[1].mblog.text", null);
        String mediaid = JsonPathUtil.parse(parse, "$.stage.single[1].mblog.user.id", null);
        String mediaName = JsonPathUtil.parse(parse, "$.stage.single[1].mblog.user.screen_name", null);
        String headImg = JsonPathUtil.parse(parse, "$.stage.single[1].mblog.user.profile_image_url", null);
        String desc = JsonPathUtil.parse(parse, "$.stage.single[1].mblog.user.description", null);
        String gender = JsonPathUtil.parse(parse, "$.stage.single[1].mblog.user.gender", null);
        String source = JsonPathUtil.parse(parse, "$.stage.single[1].mblog.source", null);
        List<String> video = parse.read("$.stage.single[1].mblog..media_info.stream_url");
        List<String> pics = parse.read("$.stage.single[1].mblog..pics..url");
        String isRetweeted = JsonPathUtil.parse(parse, "$.stage.single[1].mblog.retweeted_status", null);
        String objectType;
        if (isRetweeted != null) {
            objectType = JsonPathUtil.parse(parse, "$.stage.single[1].mblog.retweeted_status.page_info.object_type", null);
        } else {
            objectType = JsonPathUtil.parse(parse, "$.stage.single[1].mblog.page_info.object_type", null);
        }

        weiboArticle.setBrief(desc);
        weiboArticle.setCommentCount(comments_count);
        weiboArticle.setContent(content);
        weiboArticle.setGender(gender);
        weiboArticle.setHeadImg(headImg);
        weiboArticle.setImgs(null);
        weiboArticle.setLikeCount(like_count);
        weiboArticle.setMediaId(mediaid);
        weiboArticle.setMediaName(mediaName);
        weiboArticle.setPostTime(Long.valueOf(postTime));
        weiboArticle.setRepostCount(repostCount);
        weiboArticle.setVideos(null);
        weiboArticle.setSource(source);
        weiboArticle.setImgs(pics);
        weiboArticle.setVideos(video);
        weiboArticle.setObjectType(objectType);
        weiboArticle.setArticleUrl(articleUrl);

        return weiboArticle;
    }

    /**
     * Gets weibo article by article url.
     *
     * @param articleUrl the article url
     * @return the weibo article by article url
     * @throws IOException the io exception
     */
    public WeiboArticle getWeiboArticleByArticleUrl(String articleUrl) throws Exception {
        try {
            WeiboArticle weiboArticle = parseFromMobilePage(articleUrl.replace("www.weibo.com", "m.weibo.cn"));
            if (weiboArticle != null) {
                return weiboArticle;
            }
        } catch (Exception e) {

        }
        return parseFromPcPage(articleUrl.replace("m.weibo.cn", "www.weibo.com"));
    }

    private WeiboArticle parseFromPcPage(String aritcleUrl) throws Exception {
        WeiboArticle weiboArticle = new WeiboArticle();
        try {
            int i = 3;
            String pageSource = HttpUtil.getPageSourceByClient(aritcleUrl, HttpUtil.UA_PC_CHROME, WeiboUtil.getVaildWeiboCookieStore(), "utf-8", null);
            if (i < 0) {
                throw new PTBException(String.format("article url [%s] error ", aritcleUrl));
            }

            String detail = RegexUtils.sub("<script>FM.view\\((.*\"ns\":\"pl.content.weiboDetail.index\".*)\\)</script>", pageSource, 0);
            JSONObject det = (JSONObject) JSON.parse(detail);
            Document doc = Jsoup.parse(det.get("html").toString());

            weiboArticle.setMediaName(doc.select(".WB_info").text());
            weiboArticle.setMediaId(RegexUtils.sub("\\$CONFIG\\[\'oid\'\\]=\'(\\d*)\';", pageSource, 0));
            weiboArticle.setPostTime(Long.valueOf(doc.select("a[node-type=\"feed_list_item_date\"]").attr("date")) / 1000);

            String dyNum = doc.select(".pos span[node-type=\"comment_btn_text\"] em:nth-last-child(1)").text();
            String num = RegexUtils.sub("(^[0-9]*$)", dyNum,0);
            if(num != null){
                weiboArticle.setCommentCount(Integer.valueOf(num));
            }else {
                weiboArticle.setCommentCount(0);
            }

            dyNum = doc.select(".pos span[node-type=\"like_status\"] em:nth-last-child(1)").text();
            num = RegexUtils.sub("(^[0-9]*$)", dyNum,0);
            if(num != null){
                weiboArticle.setLikeCount(Integer.valueOf(num));
            }else {
                weiboArticle.setLikeCount(0);
            }

            dyNum = doc.select(".pos span[node-type=\"forward_btn_text\"] em:nth-last-child(1)").text();
            num = RegexUtils.sub("(^[0-9]*$)", dyNum,0);
            if(num != null){
                weiboArticle.setRepostCount(Integer.valueOf(num));
            }else {
                weiboArticle.setRepostCount(0);
            }

            weiboArticle.setContent(doc.select(".WB_text").outerHtml());
            weiboArticle.setHeadImg(doc.select(".face a img").attr("src"));
            weiboArticle.setSource(doc.select(".WB_from a:nth-last-child(1)").text());
            String objectType = doc.select("a[suda-uatrack*=\"click_title\"]").toString();
            Matcher m = Pattern.compile(".*click_title:[^\"]*-([^\":]*):[^\":]*").matcher(objectType);
            while (m.find()) {
                weiboArticle.setObjectType(m.group(1));
            }
            if(weiboArticle.getObjectType() == null){
                weiboArticle.setObjectType("common");
            }
            Elements select = doc.select(".media_box img");
            if (select != null) {
                weiboArticle.setImgs(select.stream().map(e -> e.attr("src")).collect(Collectors.toList()));
            }
            weiboArticle.setArticleUrl(aritcleUrl);
            return weiboArticle;

        } catch (Exception e) {
            logger.warn(String.format("get page [%s] by webdriver error", aritcleUrl), e);
            return null;
        }
    }


    /**
     * Gets weibo recent articles by container id.
     *
     * @param containerID the container id
     * @param startTime   the start time
     * @return the weibo recent articles by container id
     * @throws IOException the io exception
     */
    public ImmutablePair<Long, List<String>> getWeiboRecentArticlesByContainerID(String containerID, long startTime) throws IOException {
        String url = String.format(String.format("http://m.weibo.cn/page/json?containerid=%s_-_WEIBO_SECOND_PROFILE_WEIBO", containerID));
        String pageSource = HttpUtil.getPageSourceByClient(url, HttpUtil.UA_IPHONE6_SAFARI, WeiboUtil.getVaildWeiboCookieStore(), "utf-8", "mblog");
        DocumentContext parse = JsonPath.parse(pageSource);

        List<String> list = parse.read(String.format("$..card_group[?(@.mblog.created_timestamp > %d)].mblog.mid", startTime));
        List<Integer> read = parse.read("$..mblog.created_timestamp");

        long lastUpdateTime = read.stream().reduce((k1, k2) -> k1 > k2 ? k1 : k2).get();

        Long mediaID = Long.parseLong(((List) parse.read("$..user.id")).get(0).toString());
        List<String> articleUrls = list.stream().map(mid -> String.format("http://m.weibo.cn/%d/%s",
                mediaID, WeiboUtil.mid2url(mid))).
                collect(Collectors.toList());
        return ImmutablePair.of(lastUpdateTime, articleUrls);

    }

    public static void main(String[] args) throws Exception {
        WeiboArticleParser weiboArticleParser = new WeiboArticleParser();
        weiboArticleParser.parseFromPcPage("http://weibo.com/2165313080/Dov3pEU2d");

    }


}


