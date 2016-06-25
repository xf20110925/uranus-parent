package com.ptb.uranus.spider.common.webDriver;

import com.ptb.uranus.spider.common.utils.HttpUtil;
import org.apache.commons.pool2.BasePooledObjectFactory;
import org.apache.commons.pool2.PooledObject;
import org.apache.commons.pool2.impl.DefaultPooledObject;
import org.openqa.selenium.phantomjs.PhantomJSDriver;

/**
 * Created by eric on 16/4/5.
 */
public class WebDriverFactory extends BasePooledObjectFactory<PhantomJSDriver> {
    String userAgent = HttpUtil.UA_PC_CHROME;
    Boolean needCache = false;
    Boolean needImage = false;
    WebDriverProvider.WebDriverType proxyType;

    public WebDriverFactory(Boolean needImage, Boolean needCache, String userAgent, WebDriverProvider.WebDriverType proxyType) {
        this.needImage = needImage;
        this.needCache = needCache;
        this.userAgent = userAgent;
        this.proxyType = proxyType;
    }

    @Override
    public PhantomJSDriver create() throws Exception {
        return WebDriverProvider.createWebDriver(needImage, needCache, userAgent, proxyType);
    }


    @Override
    public PooledObject<PhantomJSDriver> wrap(PhantomJSDriver webDriver) {
        return new DefaultPooledObject<PhantomJSDriver>(webDriver);
    }

    @Override
    public void destroyObject(PooledObject<PhantomJSDriver> p) throws Exception {
        PhantomJSDriver object = p.getObject();
        object.quit();
        super.destroyObject(p);
    }

    @Override
    public boolean validateObject(PooledObject<PhantomJSDriver> p) {
        long createTime = p.getCreateTime();
        if (System.currentTimeMillis() - createTime > 60000) {
            return false;
        } else {
            return super.validateObject(p);
        }
    }

    @Override
    public void passivateObject(PooledObject<PhantomJSDriver> p) throws Exception {
        super.passivateObject(p);
    }
}
