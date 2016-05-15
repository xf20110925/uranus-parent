package com.ptb.uranus.spider.smart.action;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.ptb.uranus.spider.common.utils.HttpUtil;
import com.ptb.uranus.spider.common.webDriver.WebDriverPool;
import com.ptb.uranus.spider.common.webDriver.WebDriverPoolUtils;
import com.ptb.uranus.spider.smart.Context;
import org.apache.http.client.fluent.Response;
import org.apache.log4j.Logger;
import org.json.JSONException;
import org.openqa.selenium.phantomjs.PhantomJSDriver;

import java.nio.charset.Charset;
import java.util.Optional;

/**
 * Created by xuefeng on 2016/3/23.
 */
public class DownloadAction extends Action {
    static Logger logger = Logger.getLogger(DownloadAction.class);

    String charset;
    String teminal;
    String dynamic;

    String jsScript;
    String jsResultKey;
    String sleep;


    public String getSleep() {
        return sleep;
    }

    public void setSleep(String sleep) {
        this.sleep = sleep;
    }

    private String useragent = HttpUtil.UA_PC_CHROME;

    public String getTeminal() {
        return teminal;
    }

    public void setTeminal(String teminal) {
        if (teminal != null && teminal.equalsIgnoreCase("mobile")) {
            this.teminal = "mobile";
            this.useragent = HttpUtil.UA_IPHONE6_SAFARI;
        } else {
            this.teminal = "pc";
            this.useragent = HttpUtil.UA_PC_CHROME;
        }
    }

    public String getDynamic() {
        return dynamic;
    }

    public String getJsScript() {
        return jsScript;
    }

    public void setJsScript(String jsScript) {
        this.jsScript = jsScript;
    }

    public String getJsResultKey() {
        return jsResultKey;
    }

    public void setJsResultKey(String jsResultKey) {
        this.jsResultKey = jsResultKey;
    }

    public void setDynamic(String dynamic) {
        if (dynamic != null && dynamic.equalsIgnoreCase("true")) {
            this.dynamic = "true";

        } else {
            this.dynamic = "false";

        }
    }

    public DownloadAction() {

    }

    public DownloadAction(JSONObject jsonParam) throws JSONException {
        super(jsonParam);
    }

    @Override
    public Boolean execute(Context context) {
        String charsetName = charset;
        String crawlerUrl = (String) context.get(Context.URL);
        try {
            Optional<String> htmlOptional = fetchHtml(context, crawlerUrl, charsetName);
            String html = htmlOptional.get();
            context.set(Context.PAGESOURCE, html);
            return true;
        } catch (Exception e) {
            logger.error(String.format("download article by url [%s] error", crawlerUrl), e);
        }
        return false;
    }

    Optional<String> fetchHtmlByWebDriver(Context context, String url, String charsetName) throws Exception {
        WebDriverPool webDriverPool = null;
        int i = 3;
        while (i-- > 0) {
            boolean isMobile = useragent.equals(HttpUtil.UA_PC_CHROME) ? false : true;
            boolean needProxy = i > 0;
            webDriverPool = WebDriverPoolUtils.instance().getWebDriverFromPool(isMobile, needProxy);

            PhantomJSDriver webDriver = null;
            try {
                webDriver = webDriverPool.borrowObject();
                webDriver.navigate().to(url);
                if (sleep != null) {
                    Thread.sleep(Integer.valueOf(sleep));
                }
                if (jsScript != null && jsScript.length() > 3) {
                    Object o = webDriver.executeScript(jsScript);
                    context.set(jsResultKey, JSON.toJSONString(o));
                }
                String pageSource = webDriver.getPageSource();
                if (webDriver.getPageSource().length() > 100) {
                    return Optional.of(pageSource);
                }
            } catch (Exception e) {
                e.printStackTrace();

            } finally {
                if (webDriver != null) {
                    webDriverPool.returnObject(webDriver);
                }
            }
        }
        return Optional.empty();
    }

    private Optional<String> fetchHtml(Context context, String url, String charsetName) throws Exception {
        if (dynamic == null || dynamic == "" || !dynamic.equals("true")) {
            return Optional.of(HttpUtil.getPageSourceByClient(url,useragent,null,charsetName,"html"));
        } else {
            return fetchHtmlByWebDriver(context, url, charsetName);
        }

    }

    public String getCharset() {
        return charset;
    }

    public DownloadAction setCharset(String charset) {
        this.charset = charset;
        return this;
    }


}
