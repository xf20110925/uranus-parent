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
        obj.close();
        super.returnObject(obj);
    }

    @Override
    protected void finalize() throws Throwable {
        super.finalize();
        this.close();
    }
}
