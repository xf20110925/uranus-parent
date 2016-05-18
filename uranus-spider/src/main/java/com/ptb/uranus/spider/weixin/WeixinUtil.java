package com.ptb.uranus.spider.weixin;

import com.alibaba.fastjson.util.Base64;
import com.jayway.jsonpath.JsonPath;
import com.ptb.uranus.spider.common.exception.SpiderException;
import com.ptb.uranus.spider.common.utils.HttpUtil;
import com.ptb.utils.string.RegexUtils;
import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.URLEncoder;

/**
 * Created by eric on 15/11/11. <br>
 * 微信相关的工具类,主要功能如下: <br>
 * 1.将一个针对微信的URL转换成带KEY的URL(不带KEY是无法取得阅读数和点赞数,和微信文章列表的) <br>
 * 2.转换请请微信文章URL为一个最简格式和可进行识别的URL地址 <br>
 * 3.将URL地址,转转成对应REDIS对应的KEY地址 <br>
 * 4.获得阅读数
 */
public class WeixinUtil {
    /**
     * The Logger.
     */
    static Logger logger = LoggerFactory.getLogger(WeixinUtil.class);
    private final String queryKeyURL;
    private final String addTaskURL;
    private final String updateKeyURL;
    private final String addMapLinkUrl;
    private final String queryMapLinkUrl;


    /**
     * 能够转换URL地址的助手服务地址
     */
    private String redirectURL;

    /**
     * redis中用来存放请求爬取队列的名称
     */

    static private WeixinUtil instance;

    /**
     * Instance weixin utils.
     *
     * @return the weixin utils
     * @throws SpiderException the ptb clawler exception
     */
    public static WeixinUtil instance() throws SpiderException {
        if (instance == null) {
            System.setProperty("jsse.enableSNIExtension", "false");
            instance = new WeixinUtil();
        }
        return instance;
    }

    /**
     * Instantiates a new Weixin utils.
     */
    public WeixinUtil() {
        PropertiesConfiguration pconf = null;
        try {
            pconf = new PropertiesConfiguration("uranus.properties");
        } catch (ConfigurationException e) {
            throw new SpiderException("initing wx configuration error,check wx.properties file");
        }
        redirectURL = pconf.getString("uranus.spider.wx.redirect.url");
        queryKeyURL = pconf.getString("uranus.spider.wx.query.key.url");
        addTaskURL = pconf.getString("uranus.spider.wx.add.url.url");
        updateKeyURL = pconf.getString("uranus.spider.wx.update.key.url");
        addMapLinkUrl= pconf.getString("uranus.spider.wx.map.url");
        queryMapLinkUrl = pconf.getString("uranus.spider.wx.map.query.url");
    }


    public void addPhoneTask(String url) throws IOException {
        String reqUrl = String.format("%s?url=%s", addTaskURL, URLEncoder.encode(url, "UTF-8"));
        HttpUtil.getPageSourceByClient(reqUrl);
    }

    public String getPhoneResultByKey(String key) throws IOException {
        String reqUrl = String.format("%s?key=%s", queryKeyURL, URLEncoder.encode(key, "UTF-8"));
        String ret = HttpUtil.getPageSourceByClient(reqUrl, "", null, "utf-8", "data", false);
        String data = JsonPath.parse(ret).read("$.data").toString();
        if (data == null || data.equals("")) {
            return null;
        } else {
            return data;
        }
    }

    public void updateReadLikeNum(String data) throws IOException {
        String reqUrl = String.format("%s?data=%s", updateKeyURL, URLEncoder.encode(data, "UTF-8"));
        HttpUtil.getPageSourceByClient(reqUrl);
    }
    public void addMapLink(String realUrl, String sogouUrl) throws IOException {
        String reqUrl = String.format("%s?realUrl=%s&sogouUrl=%s", addMapLinkUrl, URLEncoder.encode(realUrl, "UTF-8"), URLEncoder.encode(sogouUrl, "UTF-8"));
        HttpUtil.getPageSourceByClient(reqUrl);
    }
    public String queryMapLink(String key) throws IOException {
        String reqUrl = String.format("%s?key=%s", queryMapLinkUrl, URLEncoder.encode(key, "UTF-8"));
        return HttpUtil.getPageSourceByClient(reqUrl);
    }
    /**
     * Redirect 2 real url string.
     * 转换一个微信爬取地址,使其带有有效KEY
     *
     * @param url the url
     * @return the string
     */
    public String getResultByPhoneSpider(String url, int clawlerTimeout) throws Exception {
        try {
            int second = 0;
            while (second < clawlerTimeout) {

                String data = getPhoneResultByKey(getUrlKey(url));
                if (data != null) {
                    return data;
                }
                if (data == null && second % 20 == 0) {
                    addPhoneTask(url);
                }
                Thread.sleep(1000);
                second++;
            }

        } catch (Exception e) {
            logger.error("url [{}]", url, e);
        }
        return null;
    }

    /**
     * Convert article url to brief string.
     * 将一个微信文章的请求,转换成合法的格式
     *
     * @param articleUrl the article url
     * @return the string
     */

    public String convertArticleUrlToBrief(String articleUrl) {

        String biz = RegexUtils.sub(".*__biz=([^#&]*).*", articleUrl, 0);
        String mid = RegexUtils.sub(".*mid=([^#&]*).*", articleUrl, 0);
        String idx = RegexUtils.sub(".*idx=([^#&]*).*", articleUrl, 0);
        String sn = RegexUtils.sub(".*sn=([^#&]*).*", articleUrl, 0);

        if (biz != null && idx != null & mid != null & sn != null) {
            return String.format("http://mp.weixin.qq.com/s?__biz=%s&mid=%s&idx=%s&sn=%s#rd", biz, mid, idx, sn);
        }
        throw new SpiderException(String.format("error wx articleUrl [%s]", articleUrl));
    }


    /**
     * 将一个URL爬取请求,转换成REDIS KEY ,爬取的结果为存放在这个KEY中,通过REDIS接口获取结果
     *
     * @param url the url
     * @return the url key
     */
    public String getUrlKey(String url) {
        String biz = RegexUtils.sub(".*__biz=([^#&]*).*", url, 0);
        String mid = RegexUtils.sub(".*mid=([^#&]*).*", url, 0);
        String sn = RegexUtils.sub(".*sn=([^#&]*).*", url, 0);
        if (url.contains("mp.weixin.qq.com/s")) {
            if (biz != null && mid != null && biz.length() > 0 && mid.length() > 0) {
                return String.format("A1:%s:%s:%s", biz, mid, sn);
            }
        } else if (url.contains("getmasssendmsg")) {
            if (biz != null && biz.length() > 0) {
                return String.format("L:%s", biz);
            }
        }

        throw new SpiderException("不能获得文章的唯一标识");

    }


    /**
     * 根据文章链接，将articleUrl转换成redis key，带有微信key的url存放在这个redis key中
     *
     * @param articleUrl
     * @return
     */
    public String getRedisAkey(String articleUrl) {
        String biz = RegexUtils.sub(".*__biz=([^#&]*).*", articleUrl, 0);
        String mid = RegexUtils.sub(".*mid=([^#&]*).*", articleUrl, 0);
        String sn = RegexUtils.sub(".*sn=([^#&]*).*", articleUrl, 0);
        if (biz != null && mid != null && biz.length() > 0 && mid.length() > 0) {
            return String.format("A:%s:%s:%s", biz, mid, sn);
        }
        throw new SpiderException("不能获得文章的唯一标识Akey");
    }

    public String getRedisBizKeyByBiz(String biz) {
        if (biz != null) {
            return String.format("A2:%s", biz);
        }
        throw new SpiderException("不能获得文章的唯一标识A2key");
    }

    /**
     * 返回文章url唯一key，用于存储微信链接到搜狗链接的映射关系
     * @param articleUrl
     * @return
     */
    public String getUrlMapKey(String articleUrl){
        String  urlMapKey = null;
        try {
            urlMapKey = getRedisAkey(articleUrl);
            urlMapKey  = urlMapKey.replace("A:", "A3:");
        } catch (Exception e) {
           throw new SpiderException("不能获取文章的唯一标识A3 key");
        }
        return urlMapKey;
    }

    /**
     * 根据文章链接，将articleUrl转换成redis key
     * format:  A2:biz
     *
     * @param articleUrl
     * @return
     */
    public String getRedisBizKeyByArticleUrl(String articleUrl) {
        String biz = RegexUtils.sub(".*__biz=([^#&]*).*", articleUrl, 0);
        return getRedisBizKeyByBiz(biz);
    }

    /**
     * Is correct biz
     *
     * @param biz the biz
     * @return the boolean
     */
    public boolean isCorrectBiz(String biz) {
        try {
            long l = Long.parseLong(new String(Base64.decodeFast(biz)));
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}
