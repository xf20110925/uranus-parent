package com.ptb.uranus.spider.smart;

import com.ptb.uranus.spider.smart.Context;

/**
 * Created by eric on 16/3/23.
 */
public class SpiderResult {
    public Context executeContext;

    public SpiderResult(Context executeContext) {
        this.executeContext = executeContext;
    }

    public SpiderResult() {
    }

    public Context getExecuteContext() {
        return executeContext;
    }

    public void setExecuteContext(Context executeContext) {
        this.executeContext = executeContext;
    }


}
