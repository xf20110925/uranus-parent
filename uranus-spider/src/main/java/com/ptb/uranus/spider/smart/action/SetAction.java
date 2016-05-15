package com.ptb.uranus.spider.smart.action;


import com.alibaba.fastjson.JSONObject;
import com.ptb.uranus.spider.smart.Context;
import org.json.JSONException;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

/**
 * Created by xuefeng on 16/3/29.
 */
public class SetAction extends Action {
    String key;
    String value;

    public SetAction(JSONObject jsonParam) throws JSONException {
        super(jsonParam);
    }

    public SetAction() {
    }

    @Override
    public Boolean execute(Context context) {
        context.set(key, value);
        return true;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
