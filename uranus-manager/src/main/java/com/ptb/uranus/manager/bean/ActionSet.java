package com.ptb.uranus.manager.bean;

import java.util.List;
import java.util.Map;

/**
 * Created by eric on 16/3/28.
 */
public class ActionSet {
    String crawleType;
    String urlRegex;
    String name;
    String id;
    List<Map<String, String>> actions;

    public String getCrawleType() {
        return crawleType;
    }

    public void setCrawleType(String crawleType) {
        this.crawleType = crawleType;
    }

    public String getUrlRegex() {
        return urlRegex;
    }

    public void setUrlRegex(String urlRegex) {
        this.urlRegex = urlRegex;
    }


    public List<Map<String, String>> getActions() {
        return actions;
    }

    public void setActions(List<Map<String, String>> actions) {
        this.actions = actions;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


    @Override
    public String toString() {
        return "ActionSet{" +
                "crawleType='" + crawleType + '\'' +
                ", urlRegex='" + urlRegex + '\'' +
                ", id='" + id + '\'' +
                ", actions=" + actions +
                '}';
    }
}
