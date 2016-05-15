package com.ptb.uranus.asistant.web.model.rep;

/**
 * Created by eric on 16/1/5.
 */
public class RequestUrlResponse {
    String newUrl;


    public RequestUrlResponse(String newUrl) {
        this.newUrl = newUrl;
    }

    public String getNewUrl() {
        return newUrl;
    }

    public void setNewUrl(String newUrl) {
        this.newUrl = newUrl;
    }
}
