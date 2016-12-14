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
import java.util.Arrays;

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
            pconf = new PropertiesConfiguration("ptb.properties");
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
            logger.warn("url {}", url, e);
        }
        logger.error("通过手机获取不到阅读数 url {}", url);
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

    public static void main(String[] args) {
        String urls = "http://mp.weixin.qq.com/s?__biz=MjAzNzMzNTkyMQ==&mid=2653753008&idx=1&sn=4ffe7ed5e8f524a31e57f60ccff79ca5#rd\n" +
                "http://mp.weixin.qq.com/s?__biz=MjAzNzMzNTkyMQ==&mid=2653753294&idx=1&sn=3f28e5aae831d28c37be396f7f896a1c#rd\n" +
                "http://mp.weixin.qq.com/s?__biz=MjAzNzMzNTkyMQ==&mid=2653753310&idx=1&sn=b800c8484e73217230359e7e7556855a#rd\n" +
                "http://mp.weixin.qq.com/s?__biz=MjAzNzMzNTkyMQ==&mid=2653753366&idx=1&sn=e853f9ba17f0600c6c7dd0c030f91b7c#rd\n" +
                "http://mp.weixin.qq.com/s?__biz=MjAzNzMzNTkyMQ==&mid=2653753371&idx=1&sn=693c1bdcbff50aa6663b19e708651012#rd\n" +
                "http://mp.weixin.qq.com/s?__biz=MjAzNzMzNTkyMQ==&mid=2653753376&idx=1&sn=3d72c67cd33ee94a77720d2e0c8c15d6#rd\n" +
                "http://mp.weixin.qq.com/s?__biz=MjAzNzMzNTkyMQ==&mid=2653753376&idx=2&sn=00c8db0893e90a347aa2cbb1b4f1d442#rd\n" +
                "http://mp.weixin.qq.com/s?__biz=MjAzNzMzNTkyMQ==&mid=2653753454&idx=1&sn=f6d91cc49e5485a74788fa53b8bbf9ab#rd\n" +
                "http://mp.weixin.qq.com/s?__biz=MjAzNzMzNTkyMQ==&mid=2653753516&idx=1&sn=93ab671c852ade0cbef24b5db7f1d4db#rd\n" +
                "http://mp.weixin.qq.com/s?__biz=MjAzNzMzNTkyMQ==&mid=2653753533&idx=2&sn=9a76675c91135db99ba4f01772ed7a9d#rd\n" +
                "http://mp.weixin.qq.com/s?__biz=MjAzNzMzNTkyMQ==&mid=2653753561&idx=1&sn=3a61f14b443b99363d6f300a9e344903#rd\n" +
                "http://mp.weixin.qq.com/s?__biz=MjAzNzMzNTkyMQ==&mid=2653753574&idx=1&sn=d38f3c4ef5f0e039d0764656dac02120#rd\n" +
                "http://mp.weixin.qq.com/s?__biz=MjE2MDI0OTk2MQ==&mid=2650844812&idx=1&sn=9aaec9ed6f6fb0f3ac563da9a7d11e9b#rd\n" +
                "http://mp.weixin.qq.com/s?__biz=MjM1ODIzNTU2MQ==&mid=2659247958&idx=4&sn=f5eca866331b4a4833cc11b08303d029#rd\n" +
                "http://mp.weixin.qq.com/s?__biz=MjM1ODIzNTU2MQ==&mid=2659247965&idx=3&sn=c7dbe98f0587793c811cd30f81b9f356#rd\n" +
                "http://mp.weixin.qq.com/s?__biz=MjM1ODIzNTU2MQ==&mid=2659248190&idx=2&sn=786f2aad33935744fbc7a0a4cd207e0a#rd\n" +
                "http://mp.weixin.qq.com/s?__biz=MjM5MDAwMzUyMA==&mid=2650088578&idx=1&sn=6c86853080a37eb905e104d45cfc87d8#rd\n" +
                "http://mp.weixin.qq.com/s?__biz=MjM5MDAxNzA2NA==&mid=2654860774&idx=1&sn=51a7cb65298b7c80af82628da4535864#rd\n" +
                "http://mp.weixin.qq.com/s?__biz=MjM5MDAyMTA2MA==&mid=2650429602&idx=3&sn=508090245ccbca6a8e7b345661a787ff#rd\n" +
                "http://mp.weixin.qq.com/s?__biz=MjM5MDAyNzQ0MA==&mid=2651038147&idx=1&sn=8402a9a8a778c8d7ac1eebd0212cd276#rd";
        WeixinUtil weixinUtil = new WeixinUtil();
        Arrays.stream(urls.split("\n")).forEach(u ->{
            try {
                weixinUtil.addPhoneTask(u);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });


    }
}
