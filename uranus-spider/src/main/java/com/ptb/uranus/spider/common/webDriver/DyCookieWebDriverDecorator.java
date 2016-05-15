package com.ptb.uranus.spider.common.webDriver;

import org.openqa.selenium.Cookie;
import org.openqa.selenium.phantomjs.PhantomJSDriver;
import org.openqa.selenium.remote.DesiredCapabilities;

import java.util.Map;
import java.util.Set;

/**
 * Created by eric on 15/11/13.
 */
public class DyCookieWebDriverDecorator extends PhantomJSDriver {

    private CookieDataSource cookieDataSource;


    /**
     * Instantiates a new Dy cookie web driver decorator.
     *
     * @param des the des
     */
    public DyCookieWebDriverDecorator(DesiredCapabilities des) {
        super(des);
    }

    /**
     * Gets cookie data source.
     *
     * @return the cookie data source
     */
    public CookieDataSource getCookieDataSource() {
        return cookieDataSource;
    }

    /**
     * Sets cookie data source.
     *
     * @param cookieDataSource the cookie data source
     * @return the cookie data source
     */
    public DyCookieWebDriverDecorator setCookieDataSource(CookieDataSource cookieDataSource) {
        this.cookieDataSource = cookieDataSource;
        return this;
    }

    @Override
    public void get(String url) {
        if(cookieDataSource != null) {
            Map<String, String> cookieMap = cookieDataSource.cookies();
            Set<Map.Entry<String, String>> entries = cookieMap.entrySet();

            for (Map.Entry<String, String> kv : entries) {
                this.manage().addCookie(new Cookie(kv.getKey(), kv.getValue()));
            }
        }
        super.get(url);
    }
}
