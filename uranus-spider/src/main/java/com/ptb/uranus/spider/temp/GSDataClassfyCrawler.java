package com.ptb.uranus.spider.temp;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.jayway.jsonpath.JsonPath;
import com.ptb.uranus.spider.common.utils.HttpUtil;
import org.apache.http.client.fluent.Executor;
import org.apache.http.client.fluent.Request;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @DESC:
 * @VERSION:
 * @author: xuefeng
 * @Date: 2017/1/10
 * @Time: 16:07
 */
public class GSDataClassfyCrawler {
    static final String userAgent = "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/54.0.2840.99 Safari/537.36";
    static final String requestWith = "XMLHttpRequest"; //X-Requested-With
    static final String linkTemplate = "http://www.gsdata.cn/newRank/getwxranks?gid=%s&date=%s&page=%d&type=day&cp=all";

    public static class GSWxMedia {
        private String wx_nickname;
        private String wx_name;
        private String readnum_all;
        private String likenum_all;

        public GSWxMedia() {
        }

        public GSWxMedia(String wx_nickname, String wx_name, String readnum_all, String likenum_all) {
            this.wx_nickname = wx_nickname;
            this.wx_name = wx_name;
            this.readnum_all = readnum_all;
            this.likenum_all = likenum_all;
        }

        public String getWx_nickname() {
            return wx_nickname;
        }

        public void setWx_nickname(String wx_nickname) {
            this.wx_nickname = wx_nickname;
        }

        public String getWx_name() {
            return wx_name;
        }

        public void setWx_name(String wx_name) {
            this.wx_name = wx_name;
        }

        public String getReadnum_all() {
            return readnum_all;
        }

        public void setReadnum_all(String readnum_all) {
            this.readnum_all = readnum_all;
        }

        public String getLikenum_all() {
            return likenum_all;
        }

        public void setLikenum_all(String likenum_all) {
            this.likenum_all = likenum_all;
        }

        @Override
        public String toString() {
            return JSON.toJSONString(this);
        }
    }

    /**
     * 解析页面不同分类的id用于拼接分类url
     *
     * @param url
     * @return
     * @throws IOException
     */
    private static Map<String, Map<String, String>> getClassfyId(String url) throws IOException {
        String content = HttpUtil.getPageSourceByClient(url, userAgent, "utf-8");
        Document document = Jsoup.parse(content);
        Elements eles = document.select("ul#wxGroup > li");
        //key为一级分类名称， value为二级分类和分类id对应关系
        Map<String, Map<String, String>> retMap = new HashMap<>();
        for (Element ele : eles) {
            String classify = ele.select("a.nav-header").first().text();
            Elements lis = ele.select("ul > li");
            HashMap<String, String> kvs = new HashMap<>();
            for (Element li : lis) {
                String secondClassify = li.text();
                String dataGid = li.attr("data-gid");
                kvs.put(secondClassify, dataGid);
            }
            retMap.put(classify, kvs);
        }
        return retMap;
    }

    /**
     * 返回所有分类连接
     *
     * @param url
     * @return
     * @throws IOException
     */
    public static Map<String, Map<String, String>> getAllinks(String url) throws IOException {
        Map<String, Map<String, String>> classfyIdMap = getClassfyId(url);
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DAY_OF_YEAR, -2);
        String nowDate = new SimpleDateFormat("yyyy-MM-dd").format(calendar.getTime());
        classfyIdMap.entrySet().stream().forEach(entry ->
                entry.getValue().entrySet().forEach(e -> {
                    String link = String.format(linkTemplate, e.getValue(), nowDate, 1);
                    e.setValue(link);
                })
        );
        return classfyIdMap;
    }

    public static List<GSWxMedia> getWxIds(String url, int retryNum) {
        String content = null;
        int i = 0;
        while (content == null && i < retryNum) {
            try {
                content = Executor.newInstance().execute(Request.Get(url).addHeader("X-Requested-With", requestWith).userAgent(userAgent).connectTimeout(10000).socketTimeout(10000)).returnContent().asString();
                i++;
            } catch (Exception e) {
                e.printStackTrace();
                System.out.println(String.format("爬取%s网页失败", url));
            }
        }
        if (content != null && content.contains("\"error\":0")) {
            String s = JsonPath.parse(content).read("$.data.rows").toString();
            List<GSWxMedia> gsWxMedias = JSONArray.parseArray(s, GSWxMedia.class);
            return gsWxMedias;
        }
        return Collections.emptyList();
    }

    public static void writeAllWxIds(String url, String fileCatalog) throws IOException {
        Map<String, Map<String, String>> allinks = getAllinks(url);
        allinks.entrySet().parallelStream().forEach(entry -> {
//        for (Map.Entry<String, Map<String, String>> entry : allinks.entrySet()) {
            for (Map.Entry<String, String> e : entry.getValue().entrySet()) {
                String link = e.getValue();
                Set<GSWxMedia> wxids = Arrays.asList(1, 2, 3, 4, 5).stream().flatMap(i -> {
                    String linkPage = link.replace("page=1", String.format("page=%d", i));
                    List<GSWxMedia> wxIds = getWxIds(linkPage, 3);
                    return wxIds.stream();
                }).collect(Collectors.toSet());
                try {
                    Path catalogPath = Paths.get(fileCatalog);
                    if (!Files.exists(catalogPath)) Files.createDirectories(catalogPath);
                    Path filePath = Paths.get(fileCatalog, entry.getKey() + ".txt");
                    if (!Files.exists(filePath)) Files.createFile(filePath);
                    Files.write(filePath, String.format("%s-->%s%s", e.getKey(), wxids, "\r\n").getBytes(), StandardOpenOption.APPEND);
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
        });

    }

    public static void main(String[] args) throws IOException {
//        Map<String, Map<String, String>> allLinks = getAllinks("http://www.gsdata.cn/rank/detail");
//        System.out.println(allLinks);
        writeAllWxIds("http://www.gsdata.cn/rank/detail", "g://gsdata");
    }
}
