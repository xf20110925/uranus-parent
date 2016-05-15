package com.ptb.uranus.spider.weibo;
import java.util.List;
import java.util.Optional;

import junit.framework.TestCase;

import org.apache.xpath.operations.String;
import org.junit.Test;

import com.google.common.collect.Lists;
import com.ptb.uranus.spider.weibo.bean.WeiboSearchAccount;

/**
 * 微博人物搜索测试
 * @author liujianpeng 2016-4-28
 *
 */
public class WbserachAccountTest {

	 WeiboSpider weiboSpider = new WeiboSpider();
//	 CountableThreadPool threadPool = new CountableThreadPool(7);
	 @Test
	 public void getWbSerachAccountByName(){
//		List<String> listName = Lists.newArrayList();
//		listName.add("d2");
//		listName.add("23");
//		listName.add("3223");
//		listName.add("223");
//		listName.add("444");
//		listName.add("但纷纷");
//		listName.add("额外");
//		for(int i=0;i<listName.size();i++){
//			String name = listName.get(i);
//			threadPool.execute(new Runnable() {
//				@Override
//				public void run() {
//					Optional<List<WeiboSearchAccount>> list = weiboSpider.getWeiboSerachAccountByName(name);
//					List<WeiboSearchAccount> weiboSerach =list.get();
//					System.out.println(weiboSerach.get(1).getAccount());
////					assertTrue(list.isPresent());
//				}
//			});
//		}
//		for(int i=0;i<listName.size();i++){
//			String name = listName.get(i);
			Optional<List<WeiboSearchAccount>> list = weiboSpider.getWeiboSerachAccountByName("wew");
			List<WeiboSearchAccount> weiboSerach =list.get();
		    TestCase.assertTrue(list.isPresent());
//		}
			
		
		
		

	 }
	 
	 
	
}