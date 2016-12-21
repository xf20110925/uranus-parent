package com.ptb.uranus.spider.weibo.parse;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.jayway.jsonpath.DocumentContext;
import com.jayway.jsonpath.JsonPath;
import com.ptb.uranus.spider.common.utils.DateUtils;
import com.ptb.uranus.spider.common.utils.HttpUtil;
import com.ptb.uranus.spider.common.utils.JsonPathUtil;
import com.ptb.uranus.spider.common.utils.WeiboUtil;
import com.ptb.uranus.spider.common.webDriver.WebDriverPool;
import com.ptb.uranus.spider.common.webDriver.WebDriverPoolUtils;
import com.ptb.uranus.spider.weibo.bean.WeiboArticle;
import com.ptb.utils.exception.PTBException;
import com.ptb.utils.string.RegexUtils;

import org.apache.commons.lang3.tuple.ImmutablePair;
import org.jsoup.Jsoup;
import org.jsoup.helper.DataUtil;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.openqa.selenium.Cookie;
import org.openqa.selenium.phantomjs.PhantomJSDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * Created by eric on 16/2/1.
 * 解析weibo文章主要工作类
 */
public class WeiboArticleParser {
    static Logger logger = LoggerFactory.getLogger(WeiboArticleParser.class);

    public WeiboArticle parseFromMobilePage(String articleUrl) {
        try {
            WeiboArticle weiboArticle = new WeiboArticle();
            String articleID = RegexUtils.sub("http://.*/[\\d]*/([^\\?]*)", articleUrl, 0);
            String weiboID = RegexUtils.sub("http://.*/([\\d]*)/[^\\?]*", articleUrl, 0);
            String url = String.format("http://m.weibo.cn/%s/%s", weiboID, articleID);

            String pageSouce = HttpUtil.getPageSourceByClient(url, HttpUtil.UA_IPHONE6_SAFARI, WeiboUtil.getVaildWeiboCookieStore(), "utf-8", "created_timestamp",true);
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
                weiboArticle.setOriginality(true);
            } else {
                objectType = JsonPathUtil.parse(parse, "$.stage.single[1].mblog.page_info.object_type", null);
                weiboArticle.setOriginality(false);
            }
            if (objectType == "") {
                objectType = "common";
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
        } catch (Exception e) {
            logger.warn("parse article url {} failed", articleUrl);
        }
        return null;

    }

    /**
     * Gets weibo article by article url.
     *
     * @param articleUrl the article url
     * @return the weibo article by article url
     * @throws IOException the io exception
     */
    public WeiboArticle getWeiboArticleByArticleUrl(String articleUrl) throws Exception {
        String articleMobileUrl = articleUrl.replaceFirst("[w\\.]*weibo.com/", "m.weibo.cn/");
        WeiboArticle weiboArticle = null;
        try {
            weiboArticle = parseFromPcPageByHttpClient(articleUrl.replace("m.weibo.cn", "weibo.com"));
        } catch (Exception e) {

        }

        if (weiboArticle == null) {
            try {
                weiboArticle = parseFromMobilePage(articleMobileUrl);
            } catch (Exception e) {

            }
        }

        if (weiboArticle == null) {
            weiboArticle = parseFromPcPageByWebDriver(articleUrl.replace("m.weibo.cn", "weibo.com"));
        }

        weiboArticle.setArticleUrl(articleMobileUrl);
        return weiboArticle;
    }

    private WeiboArticle parseFromPcPageByWebDriver(String aritcleUrl) throws Exception {
        PhantomJSDriver driver = null;
        WeiboArticle weiboArticle = new WeiboArticle();
        try {
            int i = 5;
            String pageSource = "";
            while (i-- > 0) {
                WebDriverPool webDriverFromPool = i > 0 ? WebDriverPoolUtils.instance().getWebDriverFromPool(false, true) : WebDriverPoolUtils.instance().getWebDriverFromPool(false, false);
                driver = webDriverFromPool.borrowObject();
                try {
                    driver.manage().addCookie(new Cookie("SUB", UUID.randomUUID().toString(), ".weibo.com", "/", null, false));
                    driver.navigate().to(aritcleUrl);
                    Thread.sleep(1000);
                    pageSource = driver.getPageSource();
                    if (pageSource.contains("oid")) {
                        Thread.sleep(1000);
                        break;
                    }

                } finally {
                    if (driver != null) {
                        webDriverFromPool.returnObject(driver);
                    }
                }
            }
            if (i < 0) {
                throw new PTBException(String.format("article url [%s] error ", aritcleUrl));
            }

            Document doc = Jsoup.parse(pageSource);

            weiboArticle.setMediaName(doc.select(".WB_info").text());
            weiboArticle.setMediaId(RegexUtils.sub("\\$CONFIG\\[\'oid\'\\]=\'(\\d*)\';", pageSource, 0));
            weiboArticle.setPostTime(Long.valueOf(doc.select("a[node-type=\"feed_list_item_date\"]").attr("date")) / 1000);


            try {
                weiboArticle.setCommentCount(Integer.valueOf(doc.select(".pos span[node-type=\"comment_btn_text\"] em:nth-last-child(1)").text()));
            } catch (Exception e) {
                weiboArticle.setCommentCount(0);
            }

            try {
                weiboArticle.setLikeCount(Integer.valueOf(doc.select(".pos span[node-type=\"like_status\"] em:nth-last-child(1)").text()));
            } catch (Exception e) {
                weiboArticle.setLikeCount(0);
            }

            try {
                weiboArticle.setRepostCount(Integer.valueOf(doc.select(".pos span[node-type=\"forward_btn_text\"] em:nth-last-child(1)").text()));
            } catch (Exception e) {
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
            Elements select = doc.select(".media_box img");
            if (select != null) {
                weiboArticle.setImgs(select.stream().map(e -> e.attr("src")).collect(Collectors.toList()));
            }

            if (select.size() > 0) {
                weiboArticle.setVideos(select.stream().filter(e -> e.attr("src").contains("miaopai.com")).map(
                        e -> {
                            String url = e.attr("src");
                            return url.replace("wscdn", "qncdn").replaceFirst("\\_tmp\\_.*", "mp4");
                        }
                ).collect(Collectors.toList()));
            }
            weiboArticle.setArticleUrl(aritcleUrl);
            //是否原创
            weiboArticle.setOriginality(doc.select("div.WB_feed_expand").isEmpty());
            return weiboArticle;

        } catch (Exception e) {
            logger.warn("get page {} by webdriver error", aritcleUrl, e);
            return null;
        }
    }


    public WeiboArticle parseFromPcPageByHttpClient(String aritcleUrl) throws Exception {
        WeiboArticle weiboArticle = new WeiboArticle();
        try {
            int i = 3;
            String pageSource = null;
            while (i-- > 0) {
                pageSource = HttpUtil.getPageSourceByClient(aritcleUrl, HttpUtil.UA_PC_CHROME, WeiboUtil.getVaildWeiboCookieStore(), "utf-8", null,true);
                if (pageSource != null) {
                    break;
                }
            }
            if (i < 0) {
                throw new PTBException(String.format("article url [%s] error ", aritcleUrl));
            }

            String detail = RegexUtils.sub("<script>FM.view\\((.*\"ns\":\"pl.content.weiboDetail.index\".*)\\)</script>", pageSource, 0);
            JSONObject det = JSON.parseObject(detail);
            Document doc = Jsoup.parse(det.get("html").toString());

            weiboArticle.setMediaName(doc.select(".WB_info .W_f14").text());
            weiboArticle.setMediaId(RegexUtils.sub("\\$CONFIG\\[\'oid\'\\]=\'(\\d*)\';", pageSource, 0));
            weiboArticle.setPostTime(Long.valueOf(doc.select("a[node-type=\"feed_list_item_date\"]").attr("date")) / 1000);

            String dyNum = doc.select(".pos span[node-type=\"comment_btn_text\"] em:nth-last-child(1)").text();
            String num = RegexUtils.sub("(^[0-9]*$)", dyNum, 0);
            if (num != null) {
                weiboArticle.setCommentCount(Integer.valueOf(num));
            } else {
                weiboArticle.setCommentCount(0);
            }

            dyNum = doc.select(".pos span[node-type=\"like_status\"] em:nth-last-child(1)").text();
            num = RegexUtils.sub("(^[0-9]*$)", dyNum, 0);
            if (num != null) {
                weiboArticle.setLikeCount(Integer.valueOf(num));
            } else {
                weiboArticle.setLikeCount(0);
            }

            dyNum = doc.select(".pos span[node-type=\"forward_btn_text\"] em:nth-last-child(1)").text();
            num = RegexUtils.sub("(^[0-9]*$)", dyNum, 0);
            if (num != null) {
                weiboArticle.setRepostCount(Integer.valueOf(num));
            } else {
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
            if (weiboArticle.getObjectType() == null) {
                weiboArticle.setObjectType("common");
            }
            Elements select = doc.select(".media_box img");
            if (select != null) {
                weiboArticle.setImgs(select.stream().map(e -> e.attr("src")).collect(Collectors.toList()));
            }
            weiboArticle.setArticleUrl(aritcleUrl);
            //是否原创
            weiboArticle.setOriginality(doc.select("div.WB_feed_expand").isEmpty());
            return weiboArticle;

        } catch (Exception e) {
            logger.warn("get page {} by webdriver error", aritcleUrl, e);
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
    public ImmutablePair<Long, List<WeiboArticle>> getWeiboRecentArticlesByContainerID(String containerID, long startTime) throws IOException, ParseException {

        String url = String.format(String.format("http://m.weibo.cn/container/getIndex?containerid=%s", containerID));
        String pageSource = HttpUtil.getPageSourceByClient(url, HttpUtil.UA_IPHONE6_SAFARI, WeiboUtil.getVaildWeiboCookieStore(), "utf-8", "mblog",true);
        DocumentContext parse = JsonPath.parse(pageSource);
        List<WeiboArticle> weiboArticles = new ArrayList<>();
        List<LinkedHashMap> scope = parse.read("$..mblog");
        Iterator<LinkedHashMap> iter = scope.iterator();
        long lastTime = startTime;
        while (iter.hasNext()) {
            LinkedHashMap hashMap = iter.next();
            String created_at = hashMap.get("created_at").toString();
            long createTime = DateUtils.getTime(created_at);
            if (createTime < startTime) {
                continue;
            }
            if (createTime > lastTime) {
                lastTime = createTime;
            }
            WeiboArticle weiboArticle = new WeiboArticle();
            LinkedHashMap userHash = (LinkedHashMap) hashMap.get("user");
            String mediaId = null;
            if (userHash != null) {
                weiboArticle.setMediaName((String) userHash.get("screen_name"));
                mediaId = RegexUtils.sub("/u/([0-9]*)", (String) userHash.get("profile_url"), 0);
                weiboArticle.setHeadImg((String) userHash.get("profile_image_url"));
            }

            weiboArticle.setMediaId(mediaId);
            weiboArticle.setPostTime(createTime);
            weiboArticle.setCommentCount((Integer) hashMap.get("comments_count"));
            weiboArticle.setLikeCount((Integer) hashMap.get("attitudes_count"));
            weiboArticle.setRepostCount((Integer) hashMap.get("reposts_count"));
            weiboArticle.setContent((String) hashMap.get("text"));
            weiboArticle.setSource((String) hashMap.get("source"));

            LinkedHashMap pageInfo = (LinkedHashMap) hashMap.get("page_info");
            String objType;
            if (pageInfo == null) {
                objType = "common";
            } else {
                objType = (String) pageInfo.get("object_type");
                if (objType == null) {
                    objType = "common";
                }
            }
            weiboArticle.setObjectType(objType);
            weiboArticle.setGender((String) userHash.get("gender"));
            weiboArticle.setBrief((String) userHash.get("description"));
            List<String> img = (List<String>) hashMap.get("pic_ids");
            if (img != null) {
                for (int i = 0; i < img.size(); i++) {
                    img.set(i, String.format("http://ww1.sinaimg.cn/thumbnail/%s.jpg", img.get(i)));
                }
            }
            weiboArticle.setImgs(img);
            weiboArticle.setArticleUrl(String.format("http://m.weibo.cn/%s/%s", mediaId, hashMap.get("bid")));
            if (hashMap.get("retweeted_status") == null) {
                weiboArticle.setOriginality(true);
            }else {
                weiboArticle.setOriginality(false);
            }

            weiboArticles.add(weiboArticle);
        }

        return ImmutablePair.of(lastTime, weiboArticles);
    }




    public ImmutablePair<Long, List<WeiboArticle>> getWeiboRecentArticlesThroughPc(String weiboID, long startTime) {
        try {
            List<WeiboArticle> weiboArticles = new ArrayList<>();
            int i = 3;
            String aritcleUrl = String.format("http://weibo.com/u/%s?is_all=1", weiboID);
            String pageSource = null;
            while (i-- > 0) {
                pageSource = HttpUtil.getPageSourceByClient(aritcleUrl, HttpUtil.UA_PC_CHROME, WeiboUtil.getVaildWeiboCookieStore(), "utf-8", null, true);
                if (pageSource != null) {
                    break;
                }
            }
            if (i < 0) {
                throw new PTBException(String.format("article url [%s] error ", aritcleUrl));
            }

            String detail = RegexUtils.sub("<script>FM.view\\((.*\"ns\":\"pl.content.homeFeed.index\",\"domid\":\"Pl_Official_MyProfileFeed__24\".*)\\)</script>", pageSource, 0);
            JSONObject det = JSON.parseObject(detail);
            Document doc = Jsoup.parse(det.get("html").toString());

            long lastTime = startTime;
            Elements element = doc.select(".WB_cardwrap.WB_feed_type");
            for (i = 0; i < element.size(); i++) {
                Element ele = element.get(i);
                long postTime = Long.valueOf(ele.select("a[node-type=\"feed_list_item_date\"]").attr("date")) / 1000;
                if (postTime < startTime) {
                    continue;
                }
                WeiboArticle weiboArticle = new WeiboArticle();
                weiboArticle.setMediaName(ele.select(".WB_info .W_f14").text());
                weiboArticle.setMediaId(RegexUtils.sub("\\$CONFIG\\[\'oid\'\\]=\'(\\d*)\';", pageSource, 0));
                weiboArticle.setPostTime(postTime);
                if (lastTime < postTime) {
                    lastTime = postTime;
                }

                String dyNum = ele.select(".pos span[node-type=\"comment_btn_text\"] em:nth-last-child(1)").text();
                String num = RegexUtils.sub("(^[0-9]*$)", dyNum, 0);
                if (num != null) {
                    weiboArticle.setCommentCount(Integer.valueOf(num));
                } else {
                    weiboArticle.setCommentCount(0);
                }

                dyNum = ele.select(".pos span[node-type=\"like_status\"] em:nth-last-child(1)").text();
                num = RegexUtils.sub("(^[0-9]*$)", dyNum, 0);
                if (num != null) {
                    weiboArticle.setLikeCount(Integer.valueOf(num));
                } else {
                    weiboArticle.setLikeCount(0);
                }

                dyNum = ele.select(".pos span[node-type=\"forward_btn_text\"] em:nth-last-child(1)").text();
                num = RegexUtils.sub("(^[0-9]*$)", dyNum, 0);
                if (num != null) {
                    weiboArticle.setRepostCount(Integer.valueOf(num));
                } else {
                    weiboArticle.setRepostCount(0);
                }

                weiboArticle.setContent(ele.select(".WB_text").outerHtml());
                weiboArticle.setHeadImg(ele.select(".face a img").attr("src"));
                weiboArticle.setSource(ele.select(".WB_from a:nth-last-child(1)").text());
                String objectType = ele.select("a[suda-uatrack*=\"click_title\"]").toString();
                Matcher m = Pattern.compile(".*click_title:[^\"]*-([^\":]*):[^\":]*").matcher(objectType);
                while (m.find()) {
                    weiboArticle.setObjectType(m.group(1));
                }
                if (weiboArticle.getObjectType() == null) {
                    weiboArticle.setObjectType("common");
                }
                Elements select = ele.select(".media_box img");
                if (select != null) {
                    weiboArticle.setImgs(select.stream().map(e -> e.attr("src")).collect(Collectors.toList()));
                }
                String articleId = RegexUtils.sub("/[0-9]*/(.*)\\?.*", ele.select(".WB_from a:nth-child(1)").attr("href"), 0);
                String articleUrl = String.format("http://weibo.com/%s/%s", weiboID, articleId);
                if (ele.select("WB_feed_expand") == null) {
                    weiboArticle.setOriginality(true);
                }else {
                    weiboArticle.setOriginality(false);
                }
                weiboArticle.setArticleUrl(articleUrl);
                weiboArticles.add(weiboArticle);
            }
            return ImmutablePair.of(lastTime, weiboArticles);

        } catch (Exception e) {
            logger.error("get weibo recent article error! weibo id {} ", weiboID, e);
        }
        return null;
    }

//    public static void main(String[] args) throws Exception {
//        WeiboArticleParser weiboArticleParser = new WeiboArticleParser();
//        WeiboArticle weiboArticle = weiboArticleParser.parseFromPcPageByWebDriver("http://weibo.com/2165313080/Dov3pEU2d");
//        System.out.println(JSON.toJSONString(weiboArticle));
//
//    }
}


