package com.ptb.uranus.spider.weixin.parse;

import com.ptb.uranus.spider.common.exception.SpiderException;
import com.ptb.uranus.spider.common.utils.HttpUtil;
import com.ptb.uranus.spider.common.webDriver.WebDriverProvider;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang.RandomStringUtils;
import org.apache.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.Cookie;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.phantomjs.PhantomJSDriver;

import java.net.URLEncoder;
import java.util.List;

/**
 * Created by eric on 15/12/8.
 * 该类主要是通过SOGOU来获取微信媒体的相关信息 <br>
 * 现在手段方式可总结为两种,一种通过访问微信的文章页面,获取微信的媒体信息,另外一种手段是通过访问SOGOU的网页方式获取文章信息<br>
 * 当前通过页面的方式是比较稳定的,通过SOGOU来访问的这种方式会受到SOGOU的单IP的访问限制问题,还没有彻底能够接解该问题的方案<br>
 */
public class WxSogouParser {
    /**
     * The Log.
     */
//    static final Logger log = Logger.getLogger(WxSogouParser.class);

    /**
     * Gets last article by weixin id.
     *
     * @param weixinID the weixin id
     * @return the last article by weixin id
     */
    public String getLastArticleByWeixinID(String weixinID) {
        //主要逻辑是爬取搜狗的微信SOGOU页
        String sougouHomeUrl = "http://weixin.sogou.com/weixin?type=1&ie=utf8";
        sougouHomeUrl = HttpUtil.updateArgument(sougouHomeUrl, "query", URLEncoder.encode(weixinID));

        PhantomJSDriver webDriver = WebDriverProvider.createPcWebDriver(false, true);

        try {
            webDriver.manage().deleteAllCookies();
            webDriver.manage().addCookie(new Cookie("IPLOC", "CN1100", ".sogou.com", "/", null));
            webDriver.manage().addCookie(new Cookie("SNUID", DigestUtils.md5Hex(RandomStringUtils.random(10)), ".sogou.com", "/", null));
            webDriver.manage().addCookie(new Cookie("SUID", DigestUtils.md5Hex(RandomStringUtils.random(10)), ".sogou.com", "/", null));
            webDriver.manage().addCookie(new Cookie("SUIR", String.valueOf(System.currentTimeMillis() / 1000 - 360000), ".sogou.com", "/", null));
            webDriver.manage().addCookie(new Cookie("SUV", DigestUtils.md5Hex(RandomStringUtils.random(10)), ".sogou.com", "/", null));
            webDriver.manage().addCookie(new Cookie("sct", "2", ".sogou.com", "/", null));
            webDriver.manage().addCookie(new Cookie("weixinIndexVisited", "1", ".sogou.com", "/", null));
            webDriver.manage().addCookie(new Cookie("ABTEST", String.format("1|%d|v1", System.currentTimeMillis() / 1000 - 360000), ".sogou.com", "/", null));
            webDriver.get(sougouHomeUrl);
            Thread.sleep(1000);

            List<WebElement> items = null;
            int i = 0;
            while (i++ < 10) {
                Thread.sleep(1000);
                if (webDriver.getCurrentUrl().contains("spider")) {
                    throw new SpiderException("已被限制访问");
                }
                items = webDriver.findElementsByCssSelector(".wx-rb");
                if (items != null && items.size() != 0) {
                    break;
                }
            }


            WebElement weixinIDElement = items.stream().filter((webElement -> {
                return webElement.findElement(By.cssSelector("label[name='em_weixinhao']")).getText().equals(weixinID);
            })).findFirst().get();

            webDriver.navigate().to(weixinIDElement.findElement(By.cssSelector(" div.txt-box span.sp-txt a")).getAttribute("href"));
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            String url = webDriver.getCurrentUrl();
            if (url.contains("mp.weixin.qq.com/s")) {
                return url;
            } else {
                return null;
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            webDriver.close();
            webDriver.quit();
        }
        return null;
    }


    public static void main(String[] args) {
        WxSogouParser wxSogouParser = new WxSogouParser();
        while (true) {
            String rmrbwx = wxSogouParser.getLastArticleByWeixinID("rmrbwx");
            System.out.println(rmrbwx);
        }


    }

}
