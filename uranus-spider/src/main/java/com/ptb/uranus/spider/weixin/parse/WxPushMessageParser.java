package com.ptb.uranus.spider.weixin.parse;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.jayway.jsonpath.JsonPath;
import com.ptb.uranus.spider.common.exception.SpiderException;
import com.ptb.uranus.spider.common.utils.HttpUtil;
import com.ptb.uranus.spider.weixin.WeixinUtil;
import com.ptb.uranus.spider.weixin.bean.PushMessage;
import com.ptb.utils.string.RegexUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.impl.client.BasicCookieStore;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by eric on 15/12/8.
 */
public class WxPushMessageParser {
    /**
     * The Log.
     */
    static final Logger logger = LoggerFactory.getLogger(WxPushMessageParser.class);

    /**
     * The Wx utils.
     */
    WeixinUtil wxUtils;

    /**
     * Instantiates a new Wx push message parser.
     */
    public WxPushMessageParser() {
        wxUtils = WeixinUtil.instance();
    }

    private String fillPushedUrlByTemplate(String biz) throws Exception {

        String url = wxUtils.getResultByPhoneSpider(String.format("http://mp.weixin.qq.com/mp/getmasssendmsg?__biz=%s#wechat_webview_type=1&wechat_redirect",
                biz), 60);

        if (url != null) {
            String key = RegexUtils.sub(".*key=([^&#]*).*", url, 0);
            String uin = RegexUtils.sub(".*uin=([^&#]*).*", url, 0);
            if (key.length() > 0) {
                return String.format("https://mp.weixin.qq.com/mp/getmasssendmsg?" +
                        "f=json&__biz=%s&uin=%s&key=%s", biz, uin, key);
            }
        }
        throw new SpiderException(String.format("error pullMessage redirect url [%s]", url));
    }

    private List<PushMessage> getPushedList(String url) throws IOException {
        BasicCookieStore basicCookieStore = new BasicCookieStore();

        String pagesource = HttpUtil.getPageSourceByClient(url.replace("f=json",""), HttpUtil.UA_IPHONE6_SAFARI, basicCookieStore, "utf-8", "ok", true);
        pagesource = HttpUtil.getPageSourceByClient(url, HttpUtil.UA_IPHONE6_SAFARI, basicCookieStore, "utf-8", "ok", true);

        JSONArray msgsJsonArray = JSON.parseObject(
                JsonPath.parse(pagesource).
                        read("$.general_msg_list").toString()
        ).getJSONArray("list");

        List<PushMessage> pushMsgs = new ArrayList<PushMessage>();
        for (Object o : msgsJsonArray) {
            PushMessage pushMsg = JSON.parseObject(o.toString(), PushMessage.class);
            pushMsgs.add(pushMsg);
        }

        return pushMsgs;
    }


    private List<PushMessage> getRecentPushListByBizKey(String biz) throws IOException {
        String redisBizKeyByBiz = WeixinUtil.instance().getRedisBizKeyByBiz(biz);
        String articleUrl = WeixinUtil.instance().getPhoneResultByKey(redisBizKeyByBiz);
        if (StringUtils.isNotBlank(articleUrl)) {
            String pushUrl = articleUrl.replaceFirst(".*\\?", "https://mp.weixin.qq.com/mp/getmasssendmsg?f=json&").replaceFirst("#.*","");
            return getPushedList(pushUrl);
        }
        return null;
    }

    /**
     * Gets recent push list.
     *
     * @param biz the biz
     * @return the recent push list
     */
    public List<PushMessage> getRecentPushList(String biz) {
        try {
            List<PushMessage> pushMessages = getRecentPushListByBizKey(biz);
            if(pushMessages != null) {
                return pushMessages;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            String requrl = HttpUtil.updateArgument(
                    fillPushedUrlByTemplate(biz)
                    , "f", "json");
            return getPushedList(requrl);
        } catch (Exception e) {
            logger.warn(e.getLocalizedMessage());
            throw new SpiderException(String.format("get recent message biz [%s] ", biz));
        }
    }


    /**
     * Gets recent push list.
     *
     * @param biz     the biz
     * @param beginId the begin id
     * @return the recent push list
     * @throws SpiderException the ptb clawler exception
     */
    public List<PushMessage> getRecentPushList(String biz, long beginId) throws SpiderException {
        try {
            String reqUrl = HttpUtil.updateArgument(HttpUtil.updateArgument(HttpUtil.updateArgument(fillPushedUrlByTemplate(biz)
                    , "frommsgid", String.valueOf(beginId)), "count", "10"), "f", "json");
            return getPushedList(reqUrl);
        } catch (Exception e) {
            logger.warn(e.getLocalizedMessage());
            throw new SpiderException(String.format("get recent message biz [%s] ", biz));
        }
    }

    public List<PushMessage> getRecentPushList(String biz, long beginId,String key,String uin) throws SpiderException {
        try {
            String reqUrl = HttpUtil.updateArgument(HttpUtil.updateArgument(String.format("http://mp.weixin.qq.com/mp/getmasssendmsg?__biz=%s&uin=%s&key=%s&devicetype=android-17&version=26030b31&lang=zh_CN&nettype=WIFI&pass_ticket=pxH8Kwigmr1DlWNbUWkZn1EO83oPE3ATCHIp8juI5mM42u787kppzAsp4dTwBUpy&wx_header=1", biz, uin, key)
                    ,  "count", "10"), "f", "json");
            if(beginId != 0) {
                reqUrl = HttpUtil.updateArgument(reqUrl,"frommsgid", String.valueOf(beginId));
            }
            return getPushedList(reqUrl);
        } catch (Exception e) {
            logger.warn(e.getLocalizedMessage());
            throw new SpiderException(String.format("get recent message biz [%s] ", biz));
        }
    }


}
