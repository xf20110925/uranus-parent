package com.ptb.uranus.spider.weixin.mapping;

import com.jayway.jsonpath.DocumentContext;
import com.jayway.jsonpath.JsonPath;
import com.ptb.uranus.spider.common.utils.HttpUtil;
import com.ptb.uranus.spider.weixin.WeixinSpider;
import com.ptb.uranus.spider.weixin.WeixinUtil;
import org.apache.commons.lang.StringEscapeUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.Spider;
import us.codecraft.webmagic.processor.PageProcessor;
import us.codecraft.webmagic.scheduler.QueueScheduler;
import us.codecraft.webmagic.scheduler.component.BloomFilterDuplicateRemover;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Stream;



/**
 * Created by xuefeng on 2016/5/5.
 */
public class WeixinLinkMap implements PageProcessor {
    static Logger logger = Logger.getLogger(WeixinLinkMap.class);

    public static final String USER_HOME_PAGE = "http://mp\\.weixin\\.qq\\.com/profile\\?.*";
    public static final String ARTICLE_PAGE = "http[s]?://mp\\.weixin\\.qq\\.com/s\\?src.*";
    public static final String INDEX_PAGE = "http://weixin.sogou.com/pcindex/pc/pc_\\d+/\\d+\\.html";

    public static final long RUNTIME = 1 * 60 * 60 * 1000;

    private WeixinSpider spider = new WeixinSpider();
    static AtomicLong userHomePageCounter = new AtomicLong();
    static AtomicLong articlePageCounter = new AtomicLong();

    private Site site = Site
            .me()
            .setDomain("weixin.sogou.com")
            .setSleepTime(3000)
            .setUserAgent(
                    "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_7_2) AppleWebKit/537.31 (KHTML, like Gecko) Chrome/26.0.1410.65 Safari/537.31");

    @Override
    public void process(Page page) {
        if (page.getUrl().regex(USER_HOME_PAGE).match()) {
            String link = page.getUrl().get();
            link = link.replace("?", "?f=json&");
            String jsonResult = null;
            try {
                jsonResult = HttpUtil.getPageByPcClient(link).asString();
                Object read = JsonPath.parse(jsonResult).read("$.general_msg_list");
                DocumentContext parse = JsonPath.parse(read.toString());
                String result = parse.read("$.list[*]..content_url").toString().replace("[", "").replace("]", "");
                Stream.of(result.split(",")).forEach(ele -> {
                    String url = StringEscapeUtils.unescapeHtml(StringEscapeUtils.unescapeJava(ele).replace("\"", ""));
                    if (StringUtils.isBlank(url) || url.endsWith("wechat_redirect")){
                        return;
                    }
                    url = "http://mp.weixin.qq.com" + url;
                    String realUrl = null;
                    try {
                        realUrl = spider.convertSogouWeixinUrlToRealUrl(url);
                    } catch (Exception e) {
                        logger.warn(String.format("convert url[%s] to sogou url error", realUrl), e);
                    }
                    if (StringUtils.isNotBlank(realUrl)) {
                        try {
                            String urlMapKey = WeixinUtil.instance().getUrlMapKey(realUrl);
                            WeixinUtil.instance().addMapLink(urlMapKey, url);
                            userHomePageCounter.addAndGet(1);
//                            System.out.println(userHomePageCounter.get() + ":::::" + realUrl + ":::::" + url);
                        } catch (Exception e) {
                            logger.error(String.format("add mapLink[%s] error", realUrl), e);

                        }
                    }
                });

            } catch (Exception e) {
                logger.warn(String.format("handle the userPageUrl[%s] error", page.getUrl().get()), e);
            }
            //获取用户主页下的文章链接
        } else if (page.getUrl().regex(ARTICLE_PAGE).match()) {
            String url = page.getUrl().get();
            String realUrl = spider.convertSogouWeixinUrlToRealUrl(url);
            try {
                String urlMapKey = WeixinUtil.instance().getUrlMapKey(realUrl);
                WeixinUtil.instance().addMapLink(urlMapKey, url);
                articlePageCounter.addAndGet(1);
//                System.out.println(articlePageCounter.get() + "--->" + realUrl + "--->" + url);
            } catch (Exception e) {
                logger.warn(String.format("handle the articlePageUrl[%s] error", page.getUrl().get()), e);
            }
            page.addTargetRequests(page.getHtml().links().regex(ARTICLE_PAGE).all());
        } else if (page.getUrl().regex(INDEX_PAGE).match()) {
            List<String> articleList = page.getHtml().links().regex(ARTICLE_PAGE).all();
            articleList = uniqUrlList(articleList);
            page.addTargetRequests(articleList);
            List<String> userPageList = page.getHtml().links().regex(USER_HOME_PAGE).all();
            userPageList = uniqUrlList(userPageList);
            page.addTargetRequests(userPageList);
        }
    }

    @Override
    public Site getSite() {
        return site;
    }

    /**
     * list中元素去重
     *
     * @param urlList
     * @return
     */
    public List<String> uniqUrlList(List<String> urlList) {
        return new ArrayList<>(new HashSet<>(urlList));
    }

    /**
     * Gets hot article list page from sogou.
     */
    public static List<String> getHotArticleListFromSogou() {
        List<String> articles = new ArrayList<>();
        for (int i = 0; i < 20; i++) {
            for (int j = 1; j < 14; j++) {
                articles.add(String.format("http://weixin.sogou.com/pcindex/pc/pc_%d/%d.html", i, j));
            }
        }
        return articles;
    }

    public static void cacheSogouToWeixin() {
        Spider spider = Spider.create(new WeixinLinkMap()).startUrls(getHotArticleListFromSogou())
                .thread(10)
                //使用布隆过滤器进行url去重
                .setScheduler(new QueueScheduler().setDuplicateRemover(new BloomFilterDuplicateRemover(200000)));

        long startTime = System.currentTimeMillis();
        spider.start();
        while (true) {
            long endTime = System.currentTimeMillis();
            try {
                Thread.sleep(1 * 30 * 1000);
            } catch (InterruptedException e) {
            }
            if (endTime - startTime > RUNTIME) {
                spider.stop();
                logger.info(String.format("crawler numbers of weixin url is %d + %d", userHomePageCounter.get(), articlePageCounter.get()));
                return;
            }
        }
    }

    public static void main(String[] args) {
        cacheSogouToWeixin();
    }


}

