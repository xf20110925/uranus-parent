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
        Document document = Jsoup.parse(pageSource);
        String patter = ".*window\\.\\$render_data = (\\{.*;)</script>";
        Pattern pattern = Pattern.compile(patter);
        Matcher matcher = pattern.matcher(document.toString());

        if (matcher.find() && matcher.groupCount() > 0) {
            DocumentContext parse = JsonPath.parse(matcher.group(1));

            String title = JsonPathUtil.parse(parse, "$.stage.page[0].title.txt", "");
            String id = JsonPathUtil.parse(parse, "$.stage.page[1].id", null).toString();
            String description = JsonPathUtil.parse(parse, "$.stage.page[1].description", "");

            String name = JsonPathUtil.parse(parse, "$.stage.page[1].name", null);

            String headImg = JsonPathUtil.parse(parse, "$.stage.page[1].profile_image_url", "");

            String containerId = JsonPathUtil.parse(parse, "$.common.containerid", null);

            String activePlace = JsonPathUtil.parse(parse, "$.stage.page[1].nativePlace", null);

            long createTime = new Date(JsonPathUtil.parse(parse, "$.stage.page[1].created_at", null)).getTime() / 1000;

            String gender = JsonPathUtil.parse(parse, "$.stage.page[1].ta", null);
            gender = gender.contains("他") ? "male" : "female";

            int mblogNum = Integer.parseInt(JsonPathUtil.parse(parse, "$.stage.page[1].mblogNum", ""));
            int favourites_count = Integer.parseInt(JsonPathUtil.parse(parse, "$.stage.page[1].favourites_count", null));
            int attNum = Integer.parseInt(JsonPathUtil.parse(parse, "$.stage.page[1].attNum", null));
            int fansNum = Integer.parseInt(JsonPathUtil.parse(parse, "$.stage.page[1].fansNum", null));
            String verifiedType = JsonPathUtil.parse(parse, "$.stage.page[1].verified_type", null);
            String verifiedReason = JsonPathUtil.parse(parse, "$.stage.page[1].verified_reason", null);

            WeiboAccount weiboAccount = new WeiboAccount();
            weiboAccount.setActivePlace(activePlace);
            weiboAccount.setAttNum(attNum);
            weiboAccount.setContainerID(containerId);
            weiboAccount.setCreate_time(createTime);
            weiboAccount.setDesc(description);
            weiboAccount.setFansNum(fansNum);
            weiboAccount.setFavourites_count(favourites_count);
            weiboAccount.setGender(gender);
            weiboAccount.setHeadImg(headImg);
            weiboAccount.setMblogNum(mblogNum);
            weiboAccount.setNickName(name);
            weiboAccount.setTitle(title);
            weiboAccount.setWeiboID(id);
            weiboAccount.setVerifiedType(verifiedType);
            weiboAccount.setVerifiedReason(verifiedReason);
            return weiboAccount;
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
        String wbMobilePageSource = HttpUtil.getPageSourceByClient(weiboHomeUrl, HttpUtil.UA_PC_CHROME, WeiboUtil.getVaildWeiboCookieStore(),"utf-8", "fansNum");
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
}
