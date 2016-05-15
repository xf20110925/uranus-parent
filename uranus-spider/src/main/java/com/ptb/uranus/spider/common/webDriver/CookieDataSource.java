package com.ptb.uranus.spider.common.webDriver;

import java.util.Map;

/**
 * Created by eric on 15/11/13.
 */
public interface CookieDataSource {
    /**
     * Cookies map.
     *
     * @return the map
     */
    Map<String, String> cookies();
}
