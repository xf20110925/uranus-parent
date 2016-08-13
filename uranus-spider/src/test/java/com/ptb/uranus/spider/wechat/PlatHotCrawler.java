package com.ptb.uranus.spider.wechat;

import com.ptb.uranus.spider.weibo.parse.WeiboHotTopicsParser;
import com.ptb.uranus.spider.weibo.parse.WeiboTagParser;
import com.ptb.uranus.spider.weixin.parse.WxSogouParser;

import org.apache.commons.lang.StringUtils;
import org.junit.Test;

import java.io.IOException;
import java.util.List;

import javax.script.ScriptException;

import static org.junit.Assert.assertTrue;

/**
 * @DESC:
 * @VERSION:
 * @author: Administrator
 * @Date: 2016/8/12
 * @Time: 19:01
 */
public class PlatHotCrawler {

	@Test
	public void hotArticlesTest() throws IOException {
		WxSogouParser wxSogouParser = new WxSogouParser();
		List<WxSogouParser.RealTimeArticle> hotArticles = wxSogouParser.getHotArticles("http://weixin.sogou.com/pcindex/pc/pc_4/1.html");
		assertTrue(hotArticles.size() > 0);
		hotArticles.forEach(hotArticle -> {
			assertTrue(hotArticle.getPubTime() > 0);
			assertTrue(hotArticle.getReadNum() >= 0);
			assertTrue(StringUtils.isNotBlank(hotArticle.getArticle().getArticleUrl()));
			assertTrue(StringUtils.isNotBlank(hotArticle.getArticle().getAuthor()));
			assertTrue(StringUtils.isNotBlank(hotArticle.getArticle().getBiz()));
			assertTrue(StringUtils.isNotBlank(hotArticle.getArticle().getContent()));
			assertTrue(StringUtils.isNotBlank(hotArticle.getArticle().getId()));
			assertTrue(StringUtils.isNotBlank(hotArticle.getArticle().getNickName()));
			assertTrue(StringUtils.isNotBlank(hotArticle.getArticle().getTitle()));
			assertTrue(StringUtils.isNotBlank(hotArticle.getArticle().getContent()));
		});
	}

	@Test
	public void wbTopicTest() throws IOException, ScriptException {
		WeiboHotTopicsParser topicsParser = new WeiboHotTopicsParser();
		List<WeiboHotTopicsParser.Topic> hotTopics = topicsParser.getHotTopics();
		assertTrue(hotTopics.size() > 0);
		hotTopics.forEach(topic -> {
			assertTrue(topic.getSearchNum() >= 0);
			assertTrue(StringUtils.isNotBlank(topic.getLink()));
			assertTrue(StringUtils.isNotBlank(topic.getName()));
		});
	}

	@Test
	public void wbMediaTagTest() {
		WeiboTagParser weiboTagParser = new WeiboTagParser();
		List<String> accounts = weiboTagParser.getAccounts("http://d.weibo.com/1087030002_2975_1002_0?page=1");
		assertTrue(accounts.size() > 0);
		accounts.forEach(account -> assertTrue(StringUtils.isNotBlank(account)));
	}
}
