package com.ptb.uranus.spider.weibo.parse;

import com.jayway.jsonpath.DocumentContext;
import com.jayway.jsonpath.JsonPath;
import com.ptb.uranus.spider.common.utils.HttpUtil;
import com.ptb.uranus.spider.common.utils.JsonPathUtil;
import com.ptb.uranus.spider.common.utils.WeiboUtil;
import com.ptb.uranus.spider.weibo.bean.WeiboAccount;
import com.ptb.utils.string.RegexUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.omg.CORBA.PUBLIC_MEMBER;

import java.io.IOException;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by eric on 16/2/1.
 * 解析weibo账户主要工作类
 */
public class WeiboAccountParser {
    /**
     * The Log.
     */
    static final Log log = LogFactory.getLog(WeiboAccountParser.class);


    private WeiboAccount parseWeiboBaseFromMPageDoc(String pageSource) throws IOException {
        DocumentContext parse = JsonPath.parse(pageSource);
        String name = JsonPathUtil.parse(parse,"userInfo.screen_name","");
        String headImg = JsonPathUtil.parse(parse,"userInfo.profile_image_url","");
        String id = JsonPathUtil.parse(parse,"userInfo.id","");
        String description = JsonPathUtil.parse(parse,"userInfo.description","");
        String gender = JsonPathUtil.parse(parse,"userInfo.gender","");
        gender = gender.contains("f") ?"male":"female";
        String title = JsonPathUtil.parse(parse,"userInfo.description","");
        String containerId = JsonPathUtil.parse(parse,"tabsInfo.tabs[1].containerid","");
        int attNum = Integer.parseInt(JsonPathUtil.parse(parse, "userInfo.follow_count", null));//关注
        int fansNum = Integer.parseInt(JsonPathUtil.parse(parse, "userInfo.followers_count", null));//粉丝
        String verifiedType = JsonPathUtil.parse(parse, "userInfo.verified_type", null);
        String verifiedReason = JsonPathUtil.parse(parse, "userInfo.verified_reason", null);
        /*String activePlace = JsonPathUtil.parse(parse2, "$.stage.page[1].nativePlace", null);
        long createTime = new Date(JsonPathUtil.parse(parse2, "$.stage.page[1].created_at", null)).getTime() / 1000;
        int mblogNum = Integer.parseInt(JsonPathUtil.parse(parse2, "$.stage.page[1].mblogNum", ""));
        int favourites_count = Integer.parseInt(JsonPathUtil.parse(parse2, "$.stage.page[1].favourites_count", null));*/
        WeiboAccount weiboAccount = new WeiboAccount();
//            weiboAccount.setActivePlace(activePlace);
        weiboAccount.setAttNum(attNum);
        weiboAccount.setContainerID(containerId);
//            weiboAccount.setCreate_time(createTime);
        weiboAccount.setDesc(description);
        weiboAccount.setFansNum(fansNum);
//            weiboAccount.setFavourites_count(favourites_count);
        weiboAccount.setGender(gender);
        weiboAccount.setHeadImg(headImg);
        weiboAccount.setMblogNum(10);
        weiboAccount.setNickName(name);
        weiboAccount.setTitle(title);
        weiboAccount.setWeiboID(id);
        weiboAccount.setVerifiedType(verifiedType);
        weiboAccount.setVerifiedReason(verifiedReason);
        return weiboAccount;
    }

    private String wbHotLinkUrl(String pageSource){
        try{
            String patter = "\\{.*}";
            Pattern pattern = Pattern.compile(patter);
            Matcher matcher = pattern.matcher(pageSource.toString());
            if (matcher.find()){
                String group = matcher.group(0);
                DocumentContext parse = JsonPath.parse(group);
                String card_group = JsonPathUtil.parse(parse, "card_group[0].scheme", "");
                return card_group;
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }



    /**
     * Gets weibo account.
     *
     * @param weiboHomeUrl the weibo home url
     * @return the weibo account
     * @throws IOException the io exception
     */
    public WeiboAccount getWeiboAccount(String weiboHomeUrl) throws IOException {
        String wbMobilePageSource = HttpUtil.getPageSourceByClient(weiboHomeUrl, HttpUtil.UA_PC_CHROME, null,"utf-8", "userInfo", false);
        return this.parseWeiboBaseFromMPageDoc(wbMobilePageSource);
    }


    /**
     * Gets weibo id from user home page.
     *
     * @param weiboHomeUrl the weibo home url
     * @return the weibo id from user home page
     * @throws IOException the io exception
     */
    public String getWeiboIDFromUserHomePage(String weiboHomeUrl) throws IOException {
        String wbMobilePageSource = HttpUtil.getPageSourceByClient(weiboHomeUrl, HttpUtil.UA_PC_CHROME, WeiboUtil.getVaildWeiboCookieStore(),"utf-8","CONFIG",true);
        String patter = ".*\\$CONFIG\\[\\'oid\\'\\]=\\'([0-9]*)\\'\\;.*";
        return RegexUtils.sub(patter, wbMobilePageSource, 0);

    }

    /**
     * 获取微博方面抓取最近文章containerid
     * @param weiboId
     * @return
     */
    public  String getWeiboContainerid(String weiboId){
        String weiboHomeUrl = String.format("http://m.weibo.cn/container/getIndex?type=uid&value=%s", weiboId);
        String wbContainerid = HttpUtil.getPageSourceByClient(weiboHomeUrl,HttpUtil.UA_PC_CHROME,null,"utf-8",null,true);
        DocumentContext parse2 = JsonPath.parse(wbContainerid);
        String containerid = JsonPathUtil.parse(parse2,"tabsInfo.tabs[1].containerid","");
        return containerid;
    }


}
