package com.ptb.uranus.spider.common.webDriver;

import org.apache.commons.pool2.impl.SoftReferenceObjectPool;
import org.openqa.selenium.phantomjs.PhantomJSDriver;

/**
 * Created by eric on 16/4/5.
 */
public class WebDriverPool extends SoftReferenceObjectPool<PhantomJSDriver> {
    public WebDriverPool(Boolean needImage, Boolean needCache, String userAgent, WebDriverProvider.WebDriverType proxyType) {
        super(new WebDriverFactory(needImage, needCache, userAgent, proxyType));
    }

    @Override
    public synchronized void returnObject(PhantomJSDriver obj) throws Exception {
        try {
            obj.navigate().to("http://www.sogou.com/docs/about.htm?v=1");
            super.returnObject(obj);
        } catch (Exception e) {
            obj.quit();
        }
    }

    @Override
    protected void finalize() throws Throwable {
        super.finalize();
        this.close();
    }
}
