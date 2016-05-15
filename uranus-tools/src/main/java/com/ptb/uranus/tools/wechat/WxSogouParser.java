package com.ptb.uranus.tools.wechat;


import com.ptb.uranus.spider.common.utils.HttpUtil;
import com.ptb.uranus.spider.common.webDriver.WebDriverProvider;
import com.ptb.utils.exception.PTBException;
import org.apache.commons.io.FileUtils;
import org.apache.http.client.fluent.Content;
import org.apache.http.client.fluent.Executor;
import org.apache.http.client.fluent.Request;
import org.apache.http.client.fluent.Response;
import org.apache.http.entity.ContentType;
import org.openqa.selenium.By;
import org.openqa.selenium.Cookie;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.phantomjs.PhantomJSDriver;

import java.io.File;
import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

/**
 * Created by eric on 15/12/8.
 * 该类主要是通过SOGOU来获取微信媒体的相关信息 <br>
 * 现在手段方式可总结为两种,一种通过访问微信的文章页面,获取微信的媒体信息,另外一种手段是通过访问SOGOU的网页方式获取文章信息<br>
 * 当前通过页面的方式是比较稳定的,通过SOGOU来访问的这种方式会受到SOGOU的单IP的访问限制问题,还没有彻底能够接解该问题的方案<br>
 */
public class WxSogouParser {

    private static String validateCode;

    private static boolean setValidateCode(String cookies, String validateCode) throws IOException {
        Executor use = Executor.newInstance().use(HttpUtil.getCookieStore(cookies, "weixin.sogou.com"));
        Content content = use.execute(Request.Post("http://weixin.sogou.com/antispider/thank.php").userAgent(HttpUtil.UA_PC_CHROME)
                .bodyString(String.format("c=%s", validateCode) + "&&r=%252Fweixin%253Ftype%253D1%2526query%253D333%2526ie%253Dutf8%2526_sug_%253Dn%2526_sug_type_%253D&v=5", ContentType.APPLICATION_FORM_URLENCODED).setHeader(
                        "Referer", "http://weixin.sogou.com/antispider/?from=%2fweixin%3Ftype%3d1%26query%3d333%26ie%3dutf8%26_sug_%3dn%26_sug_type_%3d"
                )).returnContent();
        String resp = content.asString(Charset.forName("UTF-8"));
        if (resp.contains(": 3")) {
            return false;
        } else {
            System.out.println("验证码验证成功");
            return true;
        }
    }

    /**
     * Gets last article by weixin id.
     *
     * @param weixinID the weixin id
     * @return the last article by weixin id
     */
    public static String getLastArticleByWeixinID(String weixinID, String cookies) {
        //主要逻辑是爬取搜狗的微信SOGOU页
        String sougouHomeUrl = "http://weixin.sogou.com/weixin?type=1&ie=utf8";

        sougouHomeUrl = HttpUtil.updateArgument(sougouHomeUrl, "query", URLEncoder.encode(weixinID));

        PhantomJSDriver webDriver = WebDriverProvider.createPcWebDriver(false, true);

        try {
            webDriver.manage().deleteAllCookies();
            Map<String, String> stringStringMap = HttpUtil.convertCookieAsString(cookies);

            stringStringMap.forEach((k, v) -> {
                webDriver.manage().addCookie(new Cookie(k, v, ".sogou.com", "/", null));
            });

            webDriver.get(sougouHomeUrl);
            Thread.sleep(1000);

            List<WebElement> items = null;
            int i = 0;
            while (i++ < 10) {
                Thread.sleep(1000);
                if (webDriver.getCurrentUrl().contains("spider")) {
                    pressValidateCode(cookies);
                    Thread.sleep(1000);
                    webDriver.get(sougouHomeUrl);
                    continue;
                }
                if (webDriver.getPageSource().contains("相关的官方认证订阅号")) {
                    throw new PTBException("没有该帐号");
                }
                items = webDriver.findElementsByCssSelector(".wx-rb");

                if (items != null && items.size() != 0) {
                    break;
                }
            }
            if (i >= 10) {
                throw new PTBException("已经完全限制访问");
            }


            WebElement weixinIDElement = items.stream().filter((webElement -> {
                return webElement.findElement(By.cssSelector("label[name='em_weixinhao']")).getText().equals(weixinID);
            })).findFirst().get();

            if(weixinIDElement == null) {
                throw new PTBException("没找到该微信帐号");
            }

            return weixinIDElement.findElement(By.cssSelector(" div.txt-box span.sp-txt a")).getAttribute("href");
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            webDriver.close();
            webDriver.quit();
        }
        return null;
    }

    private static void pressValidateCode(String cookies) throws IOException {

        if (validateCode != null) {
            if (setValidateCode(cookies, validateCode)) {
                return;
            }
        }

        Executor use = Executor.newInstance().use(HttpUtil.getCookieStore(cookies, "weixin.sogou.com"));
        Scanner scanner = new Scanner(System.in);
        while (true) {
            System.out.println("请输入根据当前目录的图片,输入验证码:");
            Response content = use.execute(Request.Get("http://weixin.sogou.com/antispider/util/seccode.php?tc=" + System.currentTimeMillis()).userAgent(HttpUtil.UA_PC_CHROME).setHeader(
                    "Referer", "http://weixin.sogou.com/antispider/?from=%2fweixin%3Ftype%3d1%26query%3d333%26ie%3dutf8%26_sug_%3dn%26_sug_type_%3d"));

            File file = new File("./validateCode.jpeg");
            FileUtils.copyInputStreamToFile(content.returnResponse().getEntity().getContent(), file);
            String xcode = scanner.next();
            if (xcode.length() == 6) {
                if (setValidateCode(cookies, xcode) == true) {
                    validateCode = xcode;
                    return;
                }
            }
            System.out.println("验证码不正确,请重新输入");
        }
    }

    public static void main(String[] args) throws IOException {
        String sogouCookie = "ABTEST=8|1458463628|v1; SUID=B1505A746A20900A0000000056EE638C; SNUID=CED025F48085510886F7856B975CF4; SUID=B1505A741508990A0000000056EE638D; SUV=1458463629362574; PHPSESSID=klekool09d819ajn2rh1e9lk65; SUIR=CED025F48085510886F7856B80975CF4; weixinIndexVisited=1; LSTMV=843%2C66; LCLKINT=111; seccodeErrorCount=1|Sun, 20 Mar 2016 14:04:50 GMT; refresh=1; IPLOC=CN1100";
        String gouzizatan = getLastArticleByWeixinID("gouzizatan", sogouCookie);
        System.out.println(gouzizatan);
    }


}
