package com.ptb.uranus.spider.common.webDriver;

import com.ptb.uranus.spider.common.utils.HttpUtil;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.Proxy;
import org.openqa.selenium.phantomjs.PhantomJSDriver;
import org.openqa.selenium.phantomjs.PhantomJSDriverService;
import org.openqa.selenium.remote.DesiredCapabilities;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;

/**
 * Created by eric on 15/11/17.
 */
public class WebDriverProvider {
    public enum WebDriverType {
        Http,
        Sock,
        None,
    }

    /**
     * Load jquery string.
     *
     * @return the string
     */
    public static String loadJquery() {
        return "var oHead = document.getElementsByTagName('HEAD').item(0);\n" +
                "var oScript= document.createElement(\"script\");\n" +
                "oScript.type = \"text/javascript\";\n" +
                "oScript.src=\"http://libs.baidu.com/jquery/1.9.1/jquery.min.js\";\n" +
                "oHead.appendChild(oScript);";
    }

    private static Proxy getProxy(WebDriverType webDriverType) {
        Proxy proxy = new Proxy();
        try {
            switch (webDriverType) {
                case Http:
                    proxy.setHttpProxy(HttpUtil.getProxy("http").replace("http://", ""));
                    break;
                case Sock:
                    proxy.setSocksProxy(HttpUtil.getProxy("sock"));
                    break;
                default:
                    return null;
            }
        } catch (Exception e) {
            return null;
        }
        return proxy;
    }

    /**
     * Create web driver phantom js driver.
     *
     * @param isNeedImage the is need image
     * @param isNeedCache the is need cache
     * @param useragent   the useragent
     * @param proxyType
     * @return the phantom js driver
     */
    public static PhantomJSDriver createWebDriver(boolean isNeedImage, boolean isNeedCache, String useragent, WebDriverType proxyType) {
        DesiredCapabilities des = DesiredCapabilities.phantomjs();

        des.setCapability("phantomjs.page.settings.userAgent",
                useragent);
        des.setBrowserName("chrome");
        des.setCapability("webStorageEnabled", true);
        des.setCapability("locationContextEnabled", true);

        if (proxyType != null && proxyType != WebDriverType.None) {
            Proxy proxy = getProxy(proxyType);
            if(proxy != null) {
                des.setCapability("proxy", getProxy(proxyType));
            }
        }

        List<String> cliarg = new ArrayList<String>();
        cliarg.add("--ignore-ssl-errors=true");
        cliarg.add("--webdriver-loglevel=ERROR");

        des.setCapability(
                PhantomJSDriverService.PHANTOMJS_CLI_ARGS, cliarg);


        if (isNeedCache) {
            cliarg.add("--disk-cache=true");
            cliarg.add("--max-disk-cache-size=1000000");
            cliarg.add("--disk-cache-path=/tmp/");
        } else {
            cliarg.add("--disk-cache=false");
        }
        if (isNeedImage) {
            cliarg.add("--load-images=true");
        } else {
            cliarg.add("--load-images=false");
        }

        des.setCapability("phantomjs.cli.args", cliarg);
        PhantomJSDriver driver = new PhantomJSDriver(des);
        driver.setLogLevel(Level.INFO);
        driver.manage().timeouts().pageLoadTimeout(30, TimeUnit.SECONDS);
        driver.manage().timeouts().setScriptTimeout(15, TimeUnit.SECONDS);
        driver.manage().timeouts().implicitlyWait(15, TimeUnit.SECONDS);
        driver.manage().window().setSize(new Dimension(1024, 768));
        return driver;
    }

    /**
     * Create pc web driver phantom js driver.
     *
     * @param isNeedImage the is need image
     * @param isNeedCache the is need cache
     * @return the phantom js driver
     */
    public static PhantomJSDriver createPcWebDriver(boolean isNeedImage, boolean isNeedCache) {
        return createWebDriver(isNeedImage, isNeedCache, HttpUtil.UA_PC_CHROME, WebDriverType.Http);
    }


    /**
     * Create mobile web driver phantom js driver.
     *
     * @param isNeedImage the is need image
     * @param isNeedCache the is need cache
     * @return the phantom js driver
     */
    public static PhantomJSDriver createMobileWebDriver(boolean isNeedImage, boolean isNeedCache) {
        return createWebDriver(isNeedImage, isNeedCache, HttpUtil.UA_IPHONE6_SAFARI, WebDriverType.Http);
    }


}
