package com.ptb.uranus.spider.smart;

import com.alibaba.fastjson.JSON;
import com.ptb.uranus.spider.smart.entity.Article;
import com.ptb.uranus.spider.smart.entity.NewScheduleUrls;
import com.ptb.uranus.spider.smart.utils.SmartSpiderConverter;
import org.easymock.EasyMockSupport;
import org.junit.Before;
import org.junit.Test;

import java.util.List;
import java.util.Optional;

import static junit.framework.TestCase.assertFalse;
import static junit.framework.TestCase.assertTrue;

/**
 * Created by eric on 16/3/23.
 */
public class BaseCrawler extends EasyMockSupport {

    private SmartSpider smartSpider;

    @Before
    public void before() {
//        smartSpider = EasyMock.createMock(SmartSpider.class);
        smartSpider = new SmartSpider();
    }


    @Test()
    public void testArticleUrlEmpty() {
//        EasyMock.expect(smartSpider.crawl(null, "")).andReturn(Optional.empty()).times(1);
//        EasyMock.replay(smartSpider);
        assertFalse(smartSpider.crawl(null, "").isPresent());
    }

    @Test
    public void testArticleUrlNoMatchFound() {
//        EasyMock.expect(smartSpider.crawl("http://www.baidu.com/", "article")).
//                andReturn(Optional.empty()).times(1);
//        EasyMock.replay(smartSpider);
        assertFalse(smartSpider.crawl("http://www.baidu.com/", "article").isPresent());
    }

    @Test
    public void testArticleUrlForamtError() {
//        EasyMock.expect(smartSpider.crawl("1111", "dddd")).andReturn(Optional.empty()).times(1);
//        EasyMock.replay(smartSpider);
        assertFalse(smartSpider.crawl("www.baidu.com", "article").isPresent());
    }

    @Test
    public void testXinHuaArticleList() {

       /* SpiderResult spiderResult = new SpiderResult();
        spiderResult.executeContext = new Context();
        List list = new ArrayList<String>();
        list.add("http:/www.baidu.com");
        list.add("http://www.sina.com");

        spiderResult.executeContext.set(SpiderConstant.NEWURLS, list);

        EasyMock.expect(smartSpider.crawl("http://www.news.cn/politics/gd.htm", "article")).
                andReturn(Optional.of(spiderResult)).times(1);
        EasyMock.replay(smartSpider);*/


        Optional<SpiderResult> resultOptional = smartSpider.crawl("http://roll.sohu.com/", "articleList");
        assertTrue(resultOptional.isPresent());
        NewScheduleUrls newScheduleUrls = SmartSpiderConverter.convertToNewSchedulerUrls(System.currentTimeMillis(), resultOptional.get());
        assertTrue(newScheduleUrls.getLastPostTime() > 0);
        assertTrue(newScheduleUrls.getUrls().size() > 0);
    }


    String getStringFromContext(Context context, String key) {
        Object o = context.get(key);
        if (o instanceof List) {
            List<String> l = (List) context;
            if (l.size() > 0) {
                return l.get(0);
            } else {
                return null;
            }
        } else {
            return (String) o;
        }
    }


    private void checkArticle(Context context) {


    }

    @Test
    public void testXinHuaAritlce() {
       /* SpiderResult spiderResult = new SpiderResult();


        spiderResult.executeContext = new Context();
        spiderResult.executeContext.set(SpiderConstant.TITLE, "title");
        spiderResult.executeContext.set(SpiderConstant.AUTHOR, "title");
        spiderResult.executeContext.set(SpiderConstant.SOURCE, "title");
        spiderResult.executeContext.set(SpiderConstant.LASTPOSTTIME, "2015-03-22 00:00:00");
        spiderResult.executeContext.set(SpiderConstant.PLATFORM, "1");
        spiderResult.executeContext.set(SpiderConstant.URL, "http://news.xinhuanet.com/politics/2016-03/23/c_128827561.htm");
        spiderResult.executeContext.set(SpiderConstant.CATEGORY, "xxxx");
        spiderResult.executeContext.set(SpiderConstant.CONTENT, "12345");
        spiderResult.executeContext.set(SpiderConstant.POSTTIME, "2015-03-22 00:00:00");


        EasyMock.expect(smartSpider.crawl("http://news.xinhuanet.com/politics/2016-03/23/c_128827561.htm", "article")).
                andReturn(Optional.of(spiderResult)).times(1);

        EasyMock.replay(smartSpider);*/
        Optional<SpiderResult> resultOptional = smartSpider.crawl("http://mil.news.sina.com.cn/world/2016-03-28/doc-ifxqswxk9740684.shtml", "article");


        assertTrue(resultOptional.isPresent());
        Article article = SmartSpiderConverter.convertToArticle(resultOptional.get());

//        assertTrue(article.getAuthor().length() > 0);
        assertTrue(article.getClassify().length() > 0);
        assertTrue(article.getContent().length() > 0);
        assertTrue(article.getSource().length() > 0);
        assertTrue(article.getTitle().length() > 0);
        assertTrue(article.getUrl().length() > 0);
        assertTrue(article.getPostTime() > 0);
        assertTrue(article.getTime() > 0);
        System.out.println(JSON.toJSONString(article));
    }


    @Test
    public void testXinhua() {
        String[] list = {
                "http://203.192.8.57/was5/web/search?channelid=240083&prepage=100",  //时政

        };
    }
}
