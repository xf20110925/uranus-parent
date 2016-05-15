package com.ptb.uranus.asistant.web.model.rep;

/**
 * Created by eric on 15/11/25.
 */
public class WeixinAccountMetaResponse {
    private final String routeUrl;
    private final String cookieStr;

    public WeixinAccountMetaResponse(String routeUrl, String cookieStr) {
        this.routeUrl = routeUrl;
        this.cookieStr = cookieStr;
    }

    public String getRouteUrl() {
        return routeUrl;
    }

    public String getCookieStr() {
        return cookieStr;
    }
}
