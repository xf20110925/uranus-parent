package com.ptb.uranus.asistant.web.model.rep;

/**
 * Created by eric on 15/11/25.
 */
public class WxDriverResponse {
    private final String id;
    private final String name;
    private final String state;




    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getState() {
        return state;
    }

    public WxDriverResponse(String id, String name,String state) {
        this.id = id;
        this.name = name;
        this.state = state;

    }
}
