package com.ptb.uranus.spider.weixin.parse;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import com.ptb.uranus.spider.common.utils.HttpUtil;
import com.ptb.uranus.spider.weixin.bean.GsData;
import org.slf4j.LoggerFactory;

/**
 * 清博指数微信提取
 * @author liujianpeng 2016-4-26
 * 
 */
public class GsDataWeixinParser {
	
	static Logger logger = LoggerFactory.getLogger(GsDataWeixinParser.class);
	/**
	 * like
	 * @param wxId
	 * @param maxPage
	 * @return
	 */
	public static Optional<List<GsData>> getWeixinAccountByIdOrName(String wxId,int maxPage){
		try {
			if(wxId.equals("")){
				return Optional.empty();
			}
			//结果
			List<GsData> result = new ArrayList<GsData>();
			int  pageNum = maxPage;
			if(maxPage<1){
				pageNum=1;
			}
			for(int i=1;i<=pageNum;i++){
				String htmlPage = HttpUtil.getPageSourceByClient("http://www.gsdata.cn/query/wx?q="+wxId+"&page="+i+"",HttpUtil.UA_PC_CHROME, null, "utf-8", "清",false);
				pageNum = getPage(htmlPage);
				Document documentHtml = Jsoup.parse(htmlPage);
				Elements eles = documentHtml.select(".article-ul li");
				List<GsData> list = eles.stream().map(ele->{
					GsData qb = new GsData();
					String wechatname = ele.select(".number-title a").text();
					String weixinId = ele.select(".wx-sp span.gray").text();//微信号
					String functionintroduce = ele.select(".wx-sp span.sp-txt").get(0).text();//功能介绍
//					String included=ele.select(".wx-sp span.sp-txt a").text();//最近收录
					String included=ele.select(".wx-sp span.sp-txt a").attr("href");//文章链接
					String qrcodeurl=ele.select(".number-codes img").attr("src");//二维码
					String headportrurl = ele.select(".number-img a").attr("href");
					qb.setWechatname(wechatname);
					qb.setWechatid(weixinId);
					qb.setFunctionintroduce(functionintroduce);
					qb.setIncluded(included);
					qb.setQrcodeurl(qrcodeurl);
					qb.setHeadportrurl(headportrurl);
					result.add(qb);
					return qb;
					
				}).collect(Collectors.toList());
			}
			return Optional.of(result);

		} catch (Exception e) {
			logger.warn("error is wxid="+wxId+" maxPage="+maxPage+" ",e);
		}
		return Optional.empty();
	}

	
	/**
	 * 返回页数
	 * @param html
	 * @return
	 */
	public static int getPage(String html){
		Document document = Jsoup.parse(html);
		int  pageNum = 1;
		Elements page = document.select(".paging li.last a");
		String regex = ".*<a.*page=(?<page>.+?)\">";
		Pattern pattern = Pattern.compile(regex);
		Matcher matcher = pattern.matcher(page.toString());
		while(matcher.find()){
			pageNum= Integer.parseInt( matcher.group("page").toString());
		}
		return pageNum;
		
	}
	
	/**
	 * 微信固定媒体搜索
	 * @param url
	 * @return
	 */
	public static String AuthenticationInfoByUrl(String url){
		try {
			if(url.equals(null)||url.equals("")){
				logger.warn("error is url="+url+"");
				return null;
			}
			String gsUrl = "http://www.gsdata.cn";
//			String html = HttpUtil.getPageSourceByClient(url, HttpUtil.UA_PC_CHROME,null,"utf-8", "清");
//			Document document=Jsoup.parse(html);
//			Elements eles = document.select(".article-ul li");
//			String   userUrl = eles.select(".number-img a").attr("href");//个人主页url
			//抓取个人主页http://www.gsdata.cn/rank/single?id=MxjuIe25N2D1Et4a
//			GsData index = new GsData();
			String htmlSource = HttpUtil.getPageSourceByClient(gsUrl+url, HttpUtil.UA_PC_CHROME,null,"utf-8", "清",false);
			Document doc = Jsoup.parse(htmlSource);
//			String wechatname = doc.select(".number-title a").text();//用户名
			Elements numberTxt = doc.select(".number-txt");
//			String wechatid = numberTxt.select(".wx-sp #wx_name").text();//微信号
//			String functionintroduce = numberTxt.select(".wx-sp .sp-txt").get(0).text();//介绍
			String authenticationInfo = numberTxt.select(".wx-sp .sp-txt").get(1).text();//认证信息
//			String qrcodeurl = doc.select(".number-codes img").attr("src");//二维码
//			index.setWechatname(wechatname);
//			index.setWechatid(wechatid);
//			index.setFunctionintroduce(functionintroduce);
//			index.setAuthenticationInfo(authenticationInfo);
//			index.setQrcodeurl(qrcodeurl);
			return authenticationInfo;
		} catch (Exception e) {
			logger.warn("error is url="+url+"",e);
		}
		return null;
	}

	
	
}