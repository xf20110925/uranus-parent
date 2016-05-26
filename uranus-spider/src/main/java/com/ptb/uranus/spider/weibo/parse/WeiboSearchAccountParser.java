package com.ptb.uranus.spider.weibo.parse;

import java.util.List;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import com.google.common.collect.Lists;
import com.jayway.jsonpath.JsonPath;
import com.ptb.uranus.spider.common.utils.HttpUtil;
import com.ptb.uranus.spider.common.utils.WeiboUtil;
import com.ptb.uranus.spider.weibo.bean.WeiboSearchAccount;
import org.slf4j.LoggerFactory;

/**
 * 微博搜索
 * @author liujianpeng 2016-4-27
 *
 */
public class WeiboSearchAccountParser {
	
	static Logger logger = LoggerFactory.getLogger(WeiboSearchAccountParser.class);
	
	/**
	 * 微博搜索抓取
	 * @param name
	 * @return
	 */
	public static  Optional<List<WeiboSearchAccount>> getWeiboSerachAccountByName(String name){
		try {
			String html = HttpUtil.getPageSourceByClient("http://s.weibo.com/user/"+name+"&Refer=SUer_box", HttpUtil.UA_PC_CHROME, WeiboUtil.getVaildWeiboCookieStore(),"utf-8", "pl_user_feedList");
			String regex = "view.*(.\"pid\":\"pl_user_feedList\".*}).*</script>";
			Pattern pattern = Pattern.compile(regex,Pattern.CASE_INSENSITIVE);
			Matcher matcher = pattern.matcher(html);
			String json="";
			while(matcher.find()){
				json = matcher.group(1);
			}
		    String read = JsonPath.parse(json).read("$.html");
		  //结果
			List<WeiboSearchAccount> result = Lists.newArrayList();
			Document documentHtml = Jsoup.parse(read);
			Elements eles = documentHtml.select(".pl_personlist .list_person");
			eles.stream().map(em ->{
				WeiboSearchAccount weAccount = new WeiboSearchAccount();
				String headportrait = em.select(".person_pic a img").attr("src");//头像
				String gender = em.select(".person_detail .person_addr span").get(0).attr("title");//性别
				String account = em.select(".person_detail .person_name a").text();//名称
				String address = em.select(".person_detail .person_addr span").get(1).text();//地址
				String personalpage = em.select(".person_detail .person_addr a").text();//个人页
				String company = em.select(".person_detail .person_card").text().trim();//公司
				String focus = em.select(".person_detail .person_num span").get(0).text().trim();//关注
				String fans = em.select(".person_detail .person_num span").get(1).text().trim();//粉丝
				String weibo = em.select(".person_detail .person_num span").get(2).text().trim();//粉丝
				String info = em.select(".person_detail .person_info").text();//简介
				weAccount.setHeadportrait(headportrait);
				weAccount.setGender(gender);
				weAccount.setAccount(account);
				weAccount.setAddress(address);
				weAccount.setPersonalpage(personalpage);
				weAccount.setCompany(company);
				weAccount.setFocus(focus);
				weAccount.setFans(fans);
				weAccount.setWeibo(weibo);
				weAccount.setIntroduction(info);
				return  result.add(weAccount);
			}).collect(Collectors.toList());
			return Optional.of(result);
		} catch (Exception e) {
			logger.warn("Class WeiboSearchAccountParser error is serachName="+name, e);
		}
		return Optional.empty();
	}

}
