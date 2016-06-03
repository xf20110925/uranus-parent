package com.ptb.uranus.spider.weixin.parse;

import com.jayway.jsonpath.DocumentContext;
import com.jayway.jsonpath.JsonPath;
import com.ptb.uranus.spider.weixin.bean.ReadLikeNum;
import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.http.client.fluent.Request;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.util.*;
import java.util.concurrent.atomic.AtomicLong;

/**
 * Created by xuefeng on 2016/5/31.
 */
public class BayouWeixinParser {

    static Logger requestLogger = Logger.getLogger("baiyou.request");
    static Logger logger = Logger.getLogger(BayouWeixinParser.class);
    private String RECENT_ARTILES_URL = null;
    private String READ_LIKE_URL = null;
    static AtomicLong requestNum = new AtomicLong(0);
    static AtomicLong requestErrorNum = new AtomicLong(0);

    private void loadConfig() {
        PropertiesConfiguration conf = new PropertiesConfiguration();
        try {
            conf.load("uranus.properties");
        } catch (ConfigurationException e) {
        }
        RECENT_ARTILES_URL = conf.getString("uranus.spider.wx.bayou.recentarticles.url", "http://43.241.211.196:23333/history");
        READ_LIKE_URL = conf.getString("uranus.spider.wx.bayou.readlike.url", "http://43.241.211.196:23333/readlike");
    }

    public BayouWeixinParser() {
        loadConfig();
    }

    public Optional<ReadLikeNum> getReadLikeNumByArticleUrl(String wxArticleUrl) {
        try {
            String result = post(READ_LIKE_URL, wxArticleUrl);
            DocumentContext parse = JsonPath.parse(result);
            String errorCode = parse.read("$.error").toString();
            if ("0".equals(errorCode)) {
                requestNum.addAndGet(1);
                int readNum = parse.read("$.msg.read_num");
                int likeNum = parse.read("$.msg.like_num");
                return Optional.of(new ReadLikeNum(readNum, likeNum));
            } else {
                requestErrorNum.addAndGet(1);
                requestLogger.warn(String.format("request readlike error[%s] ", parse.read("$.msg").toString()));
                requestLogger.warn(String.format("request num[%d] but fail num[%d]", requestNum.get(), requestErrorNum.get()));
            }
        } catch (IOException e) {
            logger.warn(e);
        } catch (Exception e) {
            logger.error(e);
        }
        return Optional.empty();
    }

    public Optional<ImmutablePair<Long, List<String>>> getRecentArticlesByBiz(String biz, long lastArticlePostTime) {
        String result = null;
        try {
            result = post(RECENT_ARTILES_URL, biz);
            DocumentContext parse = JsonPath.parse(result);
            String errorCode = parse.read("$.error").toString();
            if ("0".equals(errorCode)) {
                List reads = parse.read("$.msg");
                requestNum.addAndGet(1);
                List<String> urls = new ArrayList<>();
                List<Long> cts = new ArrayList<>();
                reads.stream().forEach(json -> {
                    DocumentContext doc = JsonPath.parse(json);
                    long ct = doc.read("$.ct", Long.class);
                    if (ct > lastArticlePostTime) {
                        urls.add(doc.read("$.main_url"));
                        urls.addAll(doc.read("ext_urls"));
                        cts.add(ct);
                    }
                });
                Collections.sort(cts, Collections.reverseOrder());
                return Optional.of(new ImmutablePair<>(cts.get(0), urls));
            } else {
                requestErrorNum.addAndGet(1);
                requestLogger.warn(String.format("request recent articles error[%s] ", parse.read("$.msg").toString()));
                requestLogger.warn(String.format("request num[%d] but fail num[%d]", requestNum.get(), requestErrorNum.get()));
            }
        } catch (IOException e) {
            logger.warn(e);
        } catch (Exception e) {
            logger.equals(e);
        }
        return Optional.empty();
    }

    private String post(String url, String param) throws IOException {
        Request request = Request.Post(url).bodyString(param, null);
        String result = request.execute().returnContent().asString();
        return result;
    }

    public static void main(String[] args) throws IOException {
       /* DocumentContext parse = JsonPath.parse(new File("G:\\IdeaWorkSpace\\uranus-parent\\uranus-spider\\src\\main\\resources\\recentUrls.txt"));
        List read = parse.read("$.msg");

        long lastPostTime = 1464490222;
        Long last;
        List<String> urls = new ArrayList<>();
        List<Long> postTimes = new ArrayList<>();
        read.stream().forEach(json -> {
            DocumentContext doc = JsonPath.parse(json);
            long ct = doc.read("$.ct", Long.class);
            if (ct > lastPostTime) {
                urls.add(doc.read("$.main_url"));
                urls.addAll(doc.read("ext_urls"));
                postTimes.add(ct);
            }
        });
        urls.forEach(System.out::println);
        Collections.sort(postTimes, Collections.reverseOrder());
        System.out.println(postTimes);*/

        BayouWeixinParser bayouWeixinParser = new BayouWeixinParser();
        Optional<ReadLikeNum> readLikeNumByArticleUrl = bayouWeixinParser.getReadLikeNumByArticleUrl("http://mp.weixin.qq.com/s?__biz=MzA3OTgzMzUzOA==&mid=2651224266&idx=1&sn=e6ee94cc0e9a42643d5606eb9a1f11b0#rd");
        System.out.println(readLikeNumByArticleUrl.get());

    }

}
