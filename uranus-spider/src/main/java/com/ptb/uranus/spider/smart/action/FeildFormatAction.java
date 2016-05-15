package com.ptb.uranus.spider.smart.action;


import com.alibaba.fastjson.JSONObject;
import com.ptb.uranus.spider.common.utils.ParseUtils;
import com.ptb.uranus.spider.smart.Context;
import org.json.JSONException;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

/**
 * Created by eric on 16/3/27.
 */
public class FeildFormatAction extends Action {
    String inputKey;
    String outputKey;
    String regexFormula;

    public FeildFormatAction(JSONObject jsonParam) throws JSONException {
        super(jsonParam);
    }

    @Override
    public Boolean execute(Context context) {
        try {
            Object o = context.get(inputKey);
            if (o instanceof List) {
                List<String> collect = ((List<String>) o).stream().map(k -> {
                    try {
                        return ParseUtils.regex(k, regexFormula).get(0);

                    } catch (Exception e) {
                        return "";
                    }
                }).collect(Collectors.toList());
                context.set(outputKey, collect);
            } else {
                context.set(outputKey, ParseUtils.regex((String) context.get(inputKey), regexFormula));
            }

        } catch (Exception e) {
        }

        return true;
    }

    public String getInputKey() {
        return inputKey;
    }

    public void setInputKey(String inputKey) {
        this.inputKey = inputKey;
    }

    public String getOutputKey() {
        return outputKey;
    }

    public void setOutputKey(String outputKey) {
        this.outputKey = outputKey;
    }

    public String getRegexFormula() {
        return regexFormula;
    }

    public void setRegexFormula(String regexFormula) {
        this.regexFormula = regexFormula;
    }
}
