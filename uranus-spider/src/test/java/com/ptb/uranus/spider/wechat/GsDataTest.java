package com.ptb.uranus.spider.wechat;

import java.util.List;
import java.util.Optional;

import junit.framework.TestCase;

import org.slf4j.Logger;
import org.junit.Test;

import com.ptb.uranus.spider.weixin.WeixinSpider;
import com.ptb.uranus.spider.weixin.bean.GsData;
import org.slf4j.LoggerFactory;

/**
 * 测试用例
 * @author liujianpeng 2016-4-27
 *
 */
public class GsDataTest {
	
	 static Logger logger = LoggerFactory.getLogger(GsDataTest.class);
	 WeixinSpider weixinSpider = new WeixinSpider();
	 /**
	  * 测试用例
	  */
	/* @Test
	 public  void getWeixinAccountByIdOrName(){
		 Optional<List<GsData>> list = weixinSpider.getWeixinAccountByIdOrName("333", 0);
		 List<GsData> qbW =list.get();
		 System.out.println(qbW.get(0).getIncluded());
		 System.out.println(qbW.get(0).getWechatid());
		 System.out.println(qbW.get(0).getFunctionintroduce());
		 
	 }*/
	 
	 
/*	 @Test
	 public void getWeixinArticleByName(){
		 String url = weixinSpider.getWeixinArticleUrlById("whulecture");
		 System.out.println(url);
	 }*/
	 
	 
	 @Test
	 public void getWinxinMatchingByName(){
		 Long stattime=System.currentTimeMillis();
		 Optional<List<GsData>> list = weixinSpider.getWeixinAccountByIdOrName("当时我就震呆了", 3);
		 System.out.println("共耗时："+(System.currentTimeMillis()-stattime));
		 
		 List<GsData> qbW =list.get(); 
		 System.out.println(qbW.size());
		 System.out.println(qbW.get(0).getIncluded());
		 System.out.println(qbW.get(0).getWechatid());
		 System.out.println(qbW.get(0).getFunctionintroduce());
		 System.out.println(qbW.get(0).getAuthenticationInfo());
		 
	 }
	 
/*	 *//**
	  * gsdata微信测试用例
	  *//*
	 @Test
	 public void getWeixinAccountById(){
		 Long k=System.currentTimeMillis();
		 Optional<GsData> a= weixinSpider.getWeixinAccountById("q");
		 TestCase.assertTrue(a.isPresent());
		 Long k2=System.currentTimeMillis();
		 System.out.println("共耗时："+(k2-k));
	 }*/

}
