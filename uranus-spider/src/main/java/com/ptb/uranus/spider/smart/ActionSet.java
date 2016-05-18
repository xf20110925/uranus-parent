package com.ptb.uranus.spider.smart;

import com.ptb.uranus.spider.smart.action.Action;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * Created by eric on 16/3/23.
 */
public class ActionSet {

    static Logger logger = LoggerFactory.getLogger(ActionSet.class);
    List<Action> actions;
    String urlRegex;
    String crawleType;
    String name;

    public boolean match(String url, String claweType) {
        if (claweType.equals(crawleType) && url.matches(urlRegex)) {
            return true;
        }
        return false;
    }

    public SpiderResult execute(Context context,String url, String claweType) {
        context.set(Context.URL, url);
        context.set(Context.CLAWETYPE, claweType);
        for (Action action : actions) {
            try {
                action.execute(context);
            } catch (Exception e) {
            }

        }
        return new SpiderResult(context);
    }

    public List<Action> getActions() {
        return actions;
    }

    public void setActions(List<Action> actions) {
        this.actions = actions;
    }

    public String getUrlRegex() {
        return urlRegex;
    }

    public void setUrlRegex(String urlRegex) {
        this.urlRegex = urlRegex;
    }

    public String getCrawleType() {
        return crawleType;
    }

    public void setCrawleType(String crawleType) {
        this.crawleType = crawleType;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
