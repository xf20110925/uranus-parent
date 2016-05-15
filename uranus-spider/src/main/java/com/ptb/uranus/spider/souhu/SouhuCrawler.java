package com.ptb.uranus.spider.souhu;

import com.ptb.uranus.spider.souhu.Parser.factory.ParserFactory;
import com.ptb.uranus.spider.souhu.Parser.itf.BaseParser;
import com.ptb.uranus.spider.souhu.bean.NewsInfo;
import com.ptb.uranus.spider.souhu.utils.CommonUtil;
import com.ptb.uranus.spider.souhu.utils.HttpUtil;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.text.ParseException;
import java.util.*;
import java.util.concurrent.*;

/**
 * Created by xuefeng on 2016/3/11.
 */
public class SouhuCrawler {
    final String souhuIndex = "http://roll.sohu.com/date/index.shtml";
    //线程数量
    private int poolSize = 10;
    //所有页面的链接队列，线程根据此队列去抓取新闻的链接,如:http://roll.sohu.com/20160310/index_183.shtml, http://roll.sohu.com/20160308/index_19.shtml,
    private Queue<String> pageLinkQueue;

    public class fetchPageLinkes implements Callable<List<String>> {

        @Override
        public List<String> call() throws Exception {
            List<String> result = new ArrayList<>();
            while (pageLinkQueue != null && !pageLinkQueue.isEmpty()) {
                String html = HttpUtil.httpGet(pageLinkQueue.poll());
                if (html == null || "".equals(html)) {
                    continue;
                }
                Document doc = Jsoup.parse(html);
                Elements elements = doc.select("div.list14").first().getElementsByTag("ul");
                for (Element ele : elements) {
                    Elements liList = ele.getElementsByTag("li");
                    for (Element element : liList) {
                        result.add(element.select("a:eq(1)").attr("href"));
                    }
                }
            }
            return result;
        }
    }

    public Set<String> getNewsUrls(String dateStr) {
        String url = souhuIndex.replace("/date", "");
        Set<String> urlSet = null;
        Set<String> allNewsLinks = new HashSet<>();
        try {
            urlSet = getallPageLink(url);
            Date date = CommonUtil.Str2Date(dateStr);
            if (date.getTime() >= CommonUtil.getCurrentDate().getTime()) {
                urlSet.addAll(getallPageLink(souhuIndex.replace("date", CommonUtil.Date2Str(CommonUtil.addDays(new Date(), -1)))));
            } else {
                date = CommonUtil.addDays(date, -1);
                while (date.getTime() < CommonUtil.getCurrentDate().getTime()) {
                    urlSet.addAll(getallPageLink(souhuIndex.replace("date", CommonUtil.Date2Str(date))));
                    date = CommonUtil.addDays(date, 1);
                }
            }
            pageLinkQueue = new LinkedBlockingQueue<>(urlSet);
            ExecutorService es = Executors.newFixedThreadPool(poolSize);
            CompletionService<List<String>> csExector = new ExecutorCompletionService<>(es);
            for (int i = 0; i < poolSize; i++) {
                csExector.submit(new fetchPageLinkes());
            }
            for (int i = 0; i < poolSize; i++) {
                allNewsLinks.addAll(csExector.take().get());
            }
        } catch (ParseException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return allNewsLinks;
    }

    /**
     * 根据url地址获取最后一页的链接
     *
     * @param url
     * @return
     * @throws Exception
     */
    private String getLastPageLink(String url) throws Exception {
        String html = HttpUtil.httpGet(url);
        Document doc = Jsoup.parse(html);
        Element element = doc.select("div.pages").first();
        Elements elements = element.getElementsByTag("a");
        String lastPage = elements.get(0).attr("href");
        return lastPage;
    }

    /**
     * 根据首页的url链接获取最后一页的url链接，然后根据两个链接的范围
     * 计算出所有页面的链接
     *
     * @param homeIndex 某日期下首页的url链接
     * @return 页面下所有链接的集合
     */
    private Set<String> getallPageLink(String homeIndex) throws Exception {
        String lastPageLink = getLastPageLink(homeIndex);
        Set<String> pageLinkSet = new HashSet<>();
        pageLinkSet.add(homeIndex);
        pageLinkSet.add(lastPageLink);
        int pageSize = Integer.parseInt(CommonUtil.extractStr(lastPageLink, ".*?(\\d+).shtml"));
        for (int i = 1; i < pageSize; i++) {
            pageLinkSet.add(lastPageLink.replaceAll("index_\\d+", "index_" + i));
        }
        return pageLinkSet;
    }


    public NewsInfo getSpecifyNews(final String url) {
        String html = HttpUtil.httpGet(url);
        BaseParser instance = ParserFactory.getInstance(html);
        NewsInfo newsInfo = instance.getNewsInfo(url, html);
        return newsInfo;
    }

    public static void main(String[] args) {
//        String tag = new SouhuCrawler().getSpecifyNews("http://learning.sohu.com/20160313/n440260654.shtml");
//        String tag = new SouhuCrawler().getSpecifyNews("http://learning.sohu.com/20160314/n440341920.shtml");
//        String tag = new SouhuCrawler().getSpecifyNews("http://astro.women.sohu.com/20160315/n440542854.shtml");
        NewsInfo tag = new SouhuCrawler().getSpecifyNews("http://it.sohu.com/20160315/n440405554.shtml");
        System.out.println(tag);
    }

}
