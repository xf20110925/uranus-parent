package com.ptb.uranus.spider.smart;

import com.ptb.uranus.spider.smart.entity.Article;
import com.ptb.uranus.spider.smart.entity.NewScheduleUrls;
import com.ptb.uranus.spider.smart.entity.SpiderConstant;
import com.ptb.uranus.spider.smart.utils.SmartSpiderConverter;
import org.junit.Before;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import static junit.framework.TestCase.assertTrue;

/**
 * Created by eric on 16/3/28.
 */
public class SmartSpiderConverterTest {

    SpiderResult spiderResult;

    @Before
    public void before() {
        spiderResult = new SpiderResult();
        spiderResult.executeContext = new Context();
        List<String> data = new LinkedList<String>();
        data.add("test");
        List<String> dates = new LinkedList<>();
        dates.add("2015-03-22 00:00:00");
        dates.add("2015-03-23 00:00:00");
        dates.add("2015-03-24 00:00:00");


        spiderResult.executeContext.set(SpiderConstant.TITLE, data);
        spiderResult.executeContext.set(SpiderConstant.AUTHOR, data);
        spiderResult.executeContext.set(SpiderConstant.SOURCE, data);
        spiderResult.executeContext.set(SpiderConstant.PLATFORM, "1");
        spiderResult.executeContext.set(SpiderConstant.URL, "http://news.xinhuanet.com/politics/2016-03/23/c_128827561.htm");
        spiderResult.executeContext.set(SpiderConstant.CATEGORY, data);
        spiderResult.executeContext.set(SpiderConstant.CONTENT, data);
        spiderResult.executeContext.set(SpiderConstant.LASTPOSTTIME, dates);
        spiderResult.executeContext.set(SpiderConstant.POSTTIME, dates);
        List list = new ArrayList<String>();
        list.add("http:/www.baidu.com");
        list.add("http://www.sina.com");

        spiderResult.executeContext.set(SpiderConstant.NEWURLS, list);
    }

    /**
     * 测试活得数据都正常的情况下
     *
     * @throws Exception the exception
     */
    public void testAllArticleRight() throws Exception {


        Article article = SmartSpiderConverter.convertToArticle(spiderResult);

        assertTrue(article.getAuthor().length() > 0);
        assertTrue(article.getClassify().length() > 0);
        assertTrue(article.getContent().length() > 0);
        assertTrue(article.getSource().length() > 0);
        assertTrue(article.getTitle().length() > 0);
        assertTrue(article.getUrl().length() > 0);
        assertTrue(article.getPostTime() > 0);
        assertTrue(article.getTime() > 0);

    }

    public void testConvertToNewSchedulerUrlsRight() throws Exception {
        NewScheduleUrls newScheduleUrls = SmartSpiderConverter.convertToNewSchedulerUrls(0L, spiderResult);
        assertTrue(newScheduleUrls.getUrls().size() > 0);
        assertTrue(newScheduleUrls.getLastPostTime() > 0);
    }

    public void testConvertToArticle1() throws Exception {
        spiderResult.getExecuteContext().set(SpiderConstant.PLATFORM, "");
        Article article = SmartSpiderConverter.convertToArticle(spiderResult);
        assertTrue(article.getPlat() == 0);
    }

    public void testConvertToNewSchedulerUrls1() throws Exception {

    }
}