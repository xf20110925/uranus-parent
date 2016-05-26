package com.ptb.uranus.spider.smart;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by xuefeng on 2016/3/23.
 */
public class Context {
    public static final String CHARSER = "charset";
    public static final String PAGESOURCE = "pageSource";
    public static final String DOM_DOCUMENT = "domDocument";
    public static final String URL = "URL";
    public static final String CLAWETYPE = "craweType";

    private Map<String, Object> contextMap = new HashMap<>();

    public Object get(String k) {
        return contextMap.get(k);

    }
    public boolean containsKey(String k) {
        return contextMap.containsKey(k);
    }

    public void set(String k, Object v) {
        contextMap.put(k, v);
    }
//测试用
    @Override
    public String toString() {
        return "Context{" +
                "contextMap=" + contextMap +
                '}';
    }


    public Map<String, Object> getContextMap() {
        return contextMap;
    }
}
