package com.ptb.uranus.spider.weixin;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.jayway.jsonpath.DocumentContext;
import com.jayway.jsonpath.JsonPath;
import com.ptb.uranus.spider.common.utils.HttpUtil;
import com.ptb.uranus.spider.weixin.bean.GsData;
import com.ptb.uranus.spider.weixin.bean.PushMessage;
import com.ptb.uranus.spider.weixin.bean.ReadLikeNum;
import com.ptb.uranus.spider.weixin.bean.WxAccount;
import com.ptb.uranus.spider.weixin.bean.WxArticle;
import com.ptb.uranus.spider.weixin.parse.BayouWeixinParser;
import com.ptb.uranus.spider.weixin.parse.GsDataWeixinParser;
import com.ptb.uranus.spider.weixin.parse.WxArticleParser;
import com.ptb.uranus.spider.weixin.parse.WxPushMessageParser;
import com.ptb.uranus.spider.weixin.parse.WxSogouParser;
import com.ptb.utils.log.LogUtils;
import com.ptb.utils.string.RegexUtils;
import com.ptb.utils.web.UrlFormatUtil;

import org.apache.commons.lang.StringEscapeUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.http.entity.ContentType;
import org.apache.http.impl.client.BasicCookieStore;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;


/**
 * Created by eric on 16/1/12.
 * 微信爬取的统一对外接口
 */
public class WeixinSpider {
    /**
     * The Logger.
     */
    static Logger logger = LoggerFactory.getLogger(WeixinSpider.class);

    /**
     * The Wx push message parser.
     */
    WxPushMessageParser wxPushMessageParser = new WxPushMessageParser();
    /**
     * The Wx article parser.
     */
    WxArticleParser wxArticleParser = new WxArticleParser();
    /**
     * The Wx account parser.
     */
    WxSogouParser wxAccountParser = new WxSogouParser();
    /**
     * 清博微信
     */
    GsDataWeixinParser gbDataWeixinParser = new GsDataWeixinParser();

    BayouWeixinParser bayouWeixinParser = new BayouWeixinParser();


    /**
     * 微信公众号like name
     *
     * @param condition
     * @param maxPageSouces
     * @return
     */
    public Optional<List<GsData>> getWeixinAccountByIdOrName(String condition, int maxPageSouces) {
        try {
            return gbDataWeixinParser.getWeixinAccountByIdOrName(condition, maxPageSouces);
        } catch (Exception e) {
            logger.warn(e.getLocalizedMessage());
        }
        return Optional.empty();
    }


    /**
     * 根据微信号抓取用户文章
     *
     * @param wechatid
     * @return
     */
    public String getWeixinArticleUrlById(String wechatid) {
        try {
            Optional<List<GsData>> OptionList = gbDataWeixinParser.getWeixinAccountByIdOrName(wechatid, 1);
            List<GsData> list = OptionList.get();
            for (int i = 0; i < list.size(); i++) {
                if (list.get(i).getWechatid().trim().equals(wechatid.trim())) {
                    String ArticleUrl = list.get(i).getIncluded();
                    return ArticleUrl;
                }
            }
        } catch (Exception e) {
            logger.warn("erro is Class getWeixinArticleByName {}", wechatid);
        }
        return null;
    }

    /**
     * 微信搜索全匹配抓取
     *
     * @param codition
     * @param maxPageSouces
     * @return
     */
    public Optional<List<GsData>> getWinxinMatchingByName(String codition, int maxPageSouces) {
        try {
            Optional<List<GsData>> OptionList = gbDataWeixinParser.getWeixinAccountByIdOrName(codition, maxPageSouces);
            List<GsData> result = OptionList.get();
            List<GsData> resultList = new ArrayList<GsData>();
            for (int i = 0; i < result.size(); i++) {
                if (codition.equals(result.get(i).getWechatname())) {
                    GsData gsdata = result.get(i);
                    String authenticationInfo = gbDataWeixinParser.AuthenticationInfoByUrl(gsdata.getHeadportrurl());
                    gsdata.setAuthenticationInfo(authenticationInfo);
                    resultList.add(gsdata);
                }
            }
            return Optional.of(resultList);
        } catch (Exception e) {
            logger.warn("error is Class getWinxinMatchingByName {}", codition);
        }
        return Optional.empty();

    }


    /**
     * Gets article read like num by url.
     *
     * @param articleUrl the article url
     * @param timeout    the timeout
     * @return the article read like num by url
     */
    public Optional<ReadLikeNum> getArticleReadLikeNumByUrl(String articleUrl, int timeout) {
        try {
            if (articleUrl.contains("signature=")) {
                return Optional.of(getSogouWxReadLikeNum(articleUrl));
            } else {
                //首先通过八友微信接口获取数据
                Optional<ReadLikeNum> bayouWxReadLike = bayouWeixinParser.getReadLikeNumByArticleUrl(articleUrl);
                if (bayouWxReadLike.isPresent()){
                    return bayouWxReadLike;
                }
                LogUtils.logInfo("uranus-spider", "get-weixin-readlike-by-url", LogUtils.ActionResult.success, "");
                //通过微信到搜狗微信链接的转化获取数据
                try {
                    String sogouUrl = WeixinUtil.instance().queryMapLink(WeixinUtil.instance().getUrlMapKey(articleUrl));
                    if (StringUtils.isNotBlank(sogouUrl)) {
                        return Optional.of(getSogouWxReadLikeNum(sogouUrl));
                    }
                } catch (Exception e) {

                }
            }
        } catch (Exception e) {
            logger.warn(e.getLocalizedMessage());
        }
        try {
            articleUrl = WeixinUtil.instance().convertArticleUrlToBrief(articleUrl);
            Optional<ReadLikeNum> readLikeNumOptional = getArticleReadLikeNumByWXkey(articleUrl);
            if (readLikeNumOptional.isPresent()) {
                return readLikeNumOptional;
            }

            String data = WeixinUtil.instance().getResultByPhoneSpider(articleUrl, timeout);
            ReadLikeNum readLikeNum = null;
            if ((readLikeNum = JSON.parseObject(data, ReadLikeNum.class)) == null) {
                return Optional.empty();
            } else {
                return Optional.of(readLikeNum);
            }
        } catch (Exception e) {
            logger.warn(e.getLocalizedMessage());
        }
        return Optional.empty();
    }

    /**
     * get article read like number by weixin key
     *
     * @param url
     * @return
     */
    private Optional<ReadLikeNum> getArticleReadLikeNumByWXkey(String url) {

        try {
            //首先查询文章的readLikeNum是否存在
            String data = WeixinUtil.instance().getPhoneResultByKey(WeixinUtil.instance().getUrlKey(url));
            if (data != null) {
                return Optional.of(JSON.parseObject(data, ReadLikeNum.class));
            }
        } catch (Exception e) {
        }

        try {
            //查询带key的文章url是否存在
            String keyUrl = WeixinUtil.instance().getPhoneResultByKey(WeixinUtil.instance().getRedisBizKeyByArticleUrl(url));
            if (StringUtils.isBlank(keyUrl)) {
                return Optional.empty();
            }

            BasicCookieStore basicCookieStore = new BasicCookieStore();
            HttpUtil.getPageSourceByClient(keyUrl.replace("f=json",""),HttpUtil.UA_IPHONE6_SAFARI,basicCookieStore,"utf-8","l");


            String key = RegexUtils.sub(".*key=([^#&]*).*", keyUrl, 0);
            String uin = RegexUtils.sub(".*uin=([^#&]*).*", keyUrl, 0);
            String articleUrl = HttpUtil.updateArgument(url, "key", key);
            articleUrl = HttpUtil.updateArgument(articleUrl, "uin", uin);
            articleUrl = articleUrl.replace("/s", "/mp/getappmsgext");
            articleUrl = HttpUtil.updateArgument(articleUrl, "f", "json");
            articleUrl = HttpUtil.updateArgument(articleUrl, "is_need_ad", "0");
            articleUrl = HttpUtil.updateArgument(articleUrl, "is_need_reward", "0");
            articleUrl = HttpUtil.updateArgument(articleUrl, "both_add", "1");
            articleUrl = HttpUtil.updateArgument(articleUrl, "reward_uin_count", "0");

//            String s = Request.Post(articleUrl).userAgent(HttpUtil.UA_IPHONE6_SAFARI).bodyString("is_only_read=1", ContentType.APPLICATION_FORM_URLENCODED).execute().returnContent().asString();
            String s = HttpUtil.postByPageClient(articleUrl, "is_only_read=1&req_id=3016hKWFzcVzSiDSY0wlArSv", ContentType.APPLICATION_FORM_URLENCODED,HttpUtil.UA_IPHONE6_SAFARI,basicCookieStore);
            if (!s.contains("read_num")) {
                return Optional.empty();
            }
            /*
            articleUrl = String.format(articleUrl + "&%s" + "&%s", key, uin);
            webDriverFromPool = WebDriverPoolUtils.instance().getWebDriverFromPool(true, false);
            phantomJSDriver = webDriverFromPool.borrowObject();
            phantomJSDriver.navigate().to(articleUrl);
            Thread.sleep(2000);
            String html = phantomJSDriver.getPageSource();*/

            ReadLikeNum readLikeNum = wxArticleParser.parseReadLikeNumByJson(s);

            JSONObject jsonObject = (JSONObject) JSON.toJSON(readLikeNum);
            jsonObject.put("url", url);
            try {
                WeixinUtil.instance().updateReadLikeNum(jsonObject.toJSONString());
            } catch (IOException e) {
            }

            return Optional.of(readLikeNum);
        } catch (Exception e) {
            logger.warn("get readLikeNum with weixin key fail url {}", url, e);
        }
        return Optional.empty();
    }

    /**
     * Gets article by url.
     *
     * @param articleUrl the article url
     * @return the article by url
     */
    public Optional<WxArticle> getArticleByUrl(String articleUrl) {
        try {
            String rightAritcleUrl = UrlFormatUtil.format(articleUrl);
            WxArticle article = wxArticleParser.getArticleNoRdNumByArticleUrl(rightAritcleUrl);
            return Optional.of(article);
        } catch (Exception e) {
            logger.warn("get weixin article by url {} error", articleUrl, e);
            return Optional.empty();
        }
    }

    /**
     * Gets recent articles by biz.
     *
     * @param biz                 the biz
     * @param lastArticlePostTime the last article post time
     * @return the recent articles by biz
     */
    public Optional<ImmutablePair<Long, List<String>>> getRecentArticlesByBiz(String biz, long lastArticlePostTime) {
        if (!WeixinUtil.instance().isCorrectBiz(biz)) {
            return Optional.empty();
        }
        //先通过八友微信获取
        Optional<ImmutablePair<Long, List<String>>> recentArticlesPair = bayouWeixinParser.getRecentArticlesByBiz(biz, lastArticlePostTime);
        if (recentArticlesPair.isPresent()) return recentArticlesPair;
        LogUtils.log("uranus-spider", "get-weixin-recent-article-by-biz", LogUtils.ActionResult.success, "");
        try {
            List<PushMessage> recentPushList = wxPushMessageParser.getRecentPushList(biz);
            if (recentPushList.size() == 0) {
                return Optional.empty();
            }

            Long postTime = recentPushList.stream().reduce((k1, k2) -> {
                long k1Time = k1.getComm_msg_info().getDatetime();
                long k2Time = k2.getComm_msg_info().getDatetime();
                return k1Time > k2Time ? k1 : k2;
            }).get().getComm_msg_info().getDatetime();

            Set<String> articles = new HashSet<>();
            recentPushList.forEach(PushMessage -> {
                if (PushMessage.getApp_msg_ext_info() != null && PushMessage.getComm_msg_info().getDatetime() > lastArticlePostTime) {
                    articles.add(StringEscapeUtils.unescapeHtml(PushMessage.getApp_msg_ext_info().getContent_url()));
                    articles.add(StringEscapeUtils.unescapeHtml(PushMessage.getApp_msg_ext_info().getSource_url()));
                    if (PushMessage.getApp_msg_ext_info().getMulti_app_msg_item_list() != null) {
                        PushMessage.getApp_msg_ext_info().getMulti_app_msg_item_list().forEach(
                                extMsgInfo -> {
                                    articles.add(StringEscapeUtils.unescapeHtml(extMsgInfo.getContent_url()));
                                    articles.add(StringEscapeUtils.unescapeHtml(extMsgInfo.getSource_url()));
                                }
                        );
                    }
                }

            });


            if (articles.size() == 0) {
                return Optional.of(ImmutablePair.of(postTime, new ArrayList<>()));
            } else {
                return Optional.of(ImmutablePair.of(postTime,
                        articles.stream().map(url -> {
                                    try {


                                        return UrlFormatUtil.format(url);
                                    } catch (Exception e) {
                                        return "";

                                    }
                                }
                        ).filter(url -> url.contains(biz)).collect(Collectors.toList()
                        )));
            }
        } catch (Exception e) {
            logger.warn("biz {}", biz, e);
            LogUtils.log("uranus-spider", "get-weixin-recent-article", LogUtils.ActionResult.failed, e.getMessage());
            return Optional.empty();
        }
    }


    /**
     * Gets weixin account by article url.
     *
     * @param articleUrl the article url
     * @return the weixin account by article url
     */
    public Optional<WxAccount> getWeixinAccountByArticleUrl(String articleUrl) {
        Optional<WxArticle> articleByUrl = this.getArticleByUrl(articleUrl);
        if (!articleByUrl.isPresent()) {
            return Optional.empty();
        }
        WxArticle article = articleByUrl.get();
        WxAccount wxAccount = new WxAccount();
        wxAccount.setBiz(RegexUtils.sub(".*biz=([^&]*).*", articleUrl, 0));
        wxAccount.setBrief(article.getBrief());
        wxAccount.setHeadImg(article.getHeadImgUrl());
        wxAccount.setId(article.getId());
        wxAccount.setNickName(article.getNickName());
        wxAccount.setQcCode(article.getQrCode());
        return Optional.of(wxAccount);
    }

    /**
     * Gets weixin account by weixin id.
     *
     * @param weixinID the weixin id
     * @return the weixin account by weixin id
     */
    public Optional<WxAccount> getWeixinAccountByWeixinID(String weixinID) {
        try {
            String lastArticleUrl = wxAccountParser.getLastArticleByWeixinID(weixinID);
            return getWeixinAccountByArticleUrl(lastArticleUrl);
        } catch (Exception e) {
            logger.warn("weixinID {}", weixinID, e);
            return Optional.empty();
        }

    }

    /**
     * Gets hot article from sogou.
     *
     * @return the hot article from sogou
     */
    public List<String> getHotArticleFromSogou() {
        List<String> articles = new ArrayList<>();
        for (int i = 0; i < 20; i++) {
            for (int j = 1; j < 14; j++) {
                try {
                    Document parse = Jsoup.parse(
                            new URL(String.format("http://weixin.sogou.com/pcindex/pc/pc_%d/%d.html", i, j)), 2000);
                    Elements src = parse.select("a[href^=\"http://mp.weixin.qq.com/s\"]");
                    List<String> urls = src.parallelStream().map(element -> {
                        String url = element.attr("href");
                        return convertSogouWeixinUrlToRealUrl(url);
                    }).distinct().collect(Collectors.toList());
                    if (urls != null) {
                        articles.addAll(urls);
                    }
                } catch (Exception e) {
                    logger.warn(e.getLocalizedMessage());
                }
            }
        }
        return articles;
    }

    public String convertSogouWeixinUrlToRealUrl(String url) {
        String pageSource = HttpUtil.getPageSourceByClient(url, HttpUtil.UA_IPHONE6_SAFARI, null, "utf-8", "biz=");
        return StringEscapeUtils.unescapeHtml(RegexUtils.sub("var msg_link = \"(.*)\";", pageSource, 0));
    }

    /**
     * get weixin media identification by article url
     *
     * @param articleUrl
     * @return
     */
    public Optional<String> getMediaIdentifyByArticleUrl(String articleUrl) {
        String redisBizkey = WeixinUtil.instance().getRedisBizKeyByArticleUrl(articleUrl);
        //在redis中获取含有微信key的文章url
        String urlWithKey = "";
        try {
            try {
                urlWithKey = WeixinUtil.instance().getPhoneResultByKey(redisBizkey);
                if (StringUtils.isNotBlank(urlWithKey)) {
                    String name = getVertifyInfo(articleUrl, urlWithKey);
                    if (StringUtils.isNotBlank(name)) {
                        return Optional.of(name);
                    }
                }
            } catch (Exception e) {

            }

            WeixinUtil.instance().getResultByPhoneSpider(articleUrl, 60);
            urlWithKey = WeixinUtil.instance().getPhoneResultByKey(redisBizkey);


            if (StringUtils.isNotBlank(urlWithKey)) {
                String name = getVertifyInfo(articleUrl, urlWithKey);
                if (StringUtils.isNotBlank(name)) {
                    return Optional.of(name);
                }
            }
        } catch (Exception e) {
            logger.warn("get identification of media exception when get ReadLikeNum:[{} {}] ", articleUrl, e.getMessage());
        }
        return Optional.empty();
    }

    private String getVertifyInfo(String articleUrl, String urlWithKey) throws IOException {
        String biz = RegexUtils.sub(".*__biz=([^#&]*).*", articleUrl, 0);
        String uin = RegexUtils.sub(".*uin=([^#&]*).*", urlWithKey, 0);
        String key = RegexUtils.sub(".*key=([^#&]*).*", urlWithKey, 0);
        String reqUrl = "http://mp.weixin.qq.com/mp/getverifyinfo?f=json&__biz=%s&uin=%s&key=%s";
        reqUrl = String.format(reqUrl, biz, uin, key);
        String ret = HttpUtil.getPageByPcClient(reqUrl).asString();
        Object vertifyInfo = JsonPath.parse(ret).read("$.name");
        if (vertifyInfo == null) {
            return null;
        } else {
            return vertifyInfo.toString();
        }
    }

    private ReadLikeNum getSogouWxReadLikeNum(String articleUrl) throws Exception {
        articleUrl = articleUrl.replace("s?", "mp/getcomment?");
        String ret = HttpUtil.getPageByPcClient(articleUrl).toString();
        DocumentContext documentContext = JsonPath.parse(ret);
        String readNum = documentContext.read("$.read_num").toString();
        String likeNum = documentContext.read("$.like_num").toString();
        return new ReadLikeNum(Integer.parseInt(readNum), Integer.parseInt(likeNum));
    }



    public static void main(String[] args) {
        String keyUrl = "https://mp.weixin.qq.com/s?__biz=MTk5NDQ2Nzc2MQ==&mid=2650292756&idx=1&sn=907794028174c34eb252c8275436848b&scene=1&srcid=0730A7cSwpdcYFiQipJvKYEb&key=8dcebf9e179c9f3a30c8bdc17cf77a46a90d70e75db852a7679dbf7f199176baa71188918486e7d5023caf5915efdaf2&ascene=0&uin=ODg4NTkwODQw&devicetype=iMac+MacBookPro11%2C5+OSX+OSX+10.11.5+build(15F34)&version=11020201&pass_ticket=Hif5lv76kwjHbHiuDL%2F5pIS%2BewKB4ecij1SogmoXKmOY%2FyE5bos5PD%2FbEx1dT6rI";
        //String keyUrl = "https://mp.weixin.qq.com/mp/getappmsgext?__biz=MTk5NDQ2Nzc2MQ==&appmsg_type=9&mid=2650292756&sn=907794028174c34eb252c8275436848b&idx=1&scene=1&title=%E8%81%94%E6%83%B3%E9%87%8A%E6%94%BE%E8%B6%85%E8%9E%8D%E5%90%88&ct=1469839236&devicetype=iMac&nbsp;MacBookPro11,5&nbsp;OSX&nbsp;OSX&nbsp;10.11.5&nbsp;build(15F34)&version=/mmbizwap/zh_CN/htmledition/js/appmsg/index2f67f1.js&f=json&r=0.08227999929250518&is_need_ad=0&comment_id=3168543664&is_need_reward=0&both_ad=1&reward_uin_count=0&uin=ODg4NTkwODQw&key=8dcebf9e179c9f3a30c8bdc17cf77a46a90d70e75db852a7679dbf7f199176baa71188918486e7d5023caf5915efdaf2&pass_ticket=Hif5lv76kwjHbHiuDL%25252F5pIS%25252BewKB4ecij1SogmoXKmOY%25252FyE5bos5PD%25252FbEx1dT6rI&wxtoken=3632109178&devicetype=iMac%26amp%3Bnbsp%3BMacBookPro11%2C5%26amp%3Bnbsp%3BOSX%26amp%3Bnbsp%3BOSX%26amp%3Bnbsp%3B10.11.5%26amp%3Bnbsp%3Bbuild(15F34)&clientversion=11020201&x5=0";
        String key = RegexUtils.sub(".*key=([^#&]*).*", keyUrl, 0);
        String uin = RegexUtils.sub(".*uin=([^#&]*).*", keyUrl, 0);
        String articleUrl = HttpUtil.updateArgument(keyUrl, "key", key);
        articleUrl = HttpUtil.updateArgument(articleUrl, "uin", uin);
        articleUrl = articleUrl.replace("/s", "/mp/getappmsgext");
        articleUrl = HttpUtil.updateArgument(articleUrl, "f", "json");
        articleUrl = HttpUtil.updateArgument(articleUrl, "is_need_ad", "0");
        articleUrl = HttpUtil.updateArgument(articleUrl, "is_need_reward", "0");
        articleUrl = HttpUtil.updateArgument(articleUrl, "both_add", "1");
        articleUrl = HttpUtil.updateArgument(articleUrl, "reward_uin_count", "0");

        //            String s = Request.Post(articleUrl).userAgent(HttpUtil.UA_IPHONE6_SAFARI).bodyString("is_only_read=1", ContentType.APPLICATION_FORM_URLENCODED).execute().returnContent().asString();
        String s = HttpUtil.postByMobileClient(articleUrl, "is_only_read=1", ContentType.APPLICATION_FORM_URLENCODED);
        if (!s.contains("read_num")) {
            System.out.println("xxx");
        }else{
            System.out.println("ok");
        }
    }

}
