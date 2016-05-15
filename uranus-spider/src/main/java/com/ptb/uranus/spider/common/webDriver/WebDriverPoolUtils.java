package com.ptb.uranus.spider.common.webDriver;

import com.ptb.uranus.spider.common.utils.HttpUtil;

/**
 * Created by eric on 15/11/17.
 */
public class WebDriverPoolUtils {
    private static WebDriverPool ProxyMobileWebDriverPool;
    private static WebDriverPool ProxyPCWebDriverPool;
    private static WebDriverPool LocalPCWebDriverPool;
    private static WebDriverPool LocalMobileDriverPool;
    private static  WebDriverPoolUtils ins;

    public static synchronized  WebDriverPoolUtils  instance() {
        if(ins == null) {
            ins = new WebDriverPoolUtils();
        }
        return ins;
    }

    public synchronized void init() {
        if (ProxyMobileWebDriverPool == null) {
            ProxyMobileWebDriverPool = new WebDriverPool(false, true, HttpUtil.UA_IPHONE6_SAFARI, WebDriverProvider.WebDriverType.Http);
        }

        if (ProxyPCWebDriverPool == null) {
            ProxyPCWebDriverPool = new WebDriverPool(false, true, HttpUtil.UA_PC_CHROME, WebDriverProvider.WebDriverType.Http);
        }

        if (LocalMobileDriverPool == null) {
            LocalMobileDriverPool = new WebDriverPool(false, true, HttpUtil.UA_IPHONE6_SAFARI, WebDriverProvider.WebDriverType.None);
        }

        if (LocalPCWebDriverPool == null) {
            LocalPCWebDriverPool = new WebDriverPool(false, true, HttpUtil.UA_PC_CHROME, WebDriverProvider.WebDriverType.None);
        }
    }

    public WebDriverPool getWebDriverFromPool(boolean isMobile, boolean isNeedProxy) {
        init();
        try {
            if (isMobile) {
                if (isNeedProxy) {
                    return ProxyMobileWebDriverPool;
                } else {
                    return LocalMobileDriverPool ;
                }
            } else {
                if (isNeedProxy) {
                    return ProxyPCWebDriverPool;
                } else {
                    return LocalPCWebDriverPool;
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private  void close(WebDriverPool webDriverPool) {
        if (webDriverPool != null) {
            webDriverPool.close();
        }
    }

    public synchronized void closeAll() {
        close(ProxyMobileWebDriverPool);
        ProxyMobileWebDriverPool = null;
        close(ProxyPCWebDriverPool);
        ProxyPCWebDriverPool = null;
        close(LocalPCWebDriverPool);
        LocalPCWebDriverPool = null;
        close(LocalMobileDriverPool);
        LocalMobileDriverPool = null;
    }

}
