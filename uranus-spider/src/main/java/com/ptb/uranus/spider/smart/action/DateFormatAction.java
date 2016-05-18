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
 * Created by eric on 16/3/27.
 */
public class DateFormatAction extends Action {
    String inputKey;
    String outputKey;
    String inputFormula;
    SimpleDateFormat inFormat;
    SimpleDateFormat outFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.SIMPLIFIED_CHINESE);


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

    public String getInputFormula() {
        return inputFormula;
    }

    public void setInputFormula(String inputFormula) {
        this.inFormat = new SimpleDateFormat(inputFormula);
        this.inputFormula = inputFormula;
    }

    public SimpleDateFormat getOutFormat() {
        return outFormat;
    }

    public DateFormatAction(JSONObject jsonParam) throws JSONException {
        super(jsonParam);
    }

    @Override
    public Boolean execute(Context context) {
        try {
            Object o = context.get(inputKey);
            if (o instanceof List) {
                List<String> collect = ((List<String>) o).stream().map(k -> {
                    Date parse = null;
                    try {
                        parse = inFormat.parse(k);
                        parse.setYear(new Date().getYear());
                        return outFormat.format(parse);

                    } catch (ParseException e) {
                        return "";
                    }
                }).filter(d->d != "").collect(Collectors.toList());
                if(collect.size() > 0 && collect.get(0).length() > 0) {
                    context.set(outputKey, collect);
                }
            } else {
                Date parse = inFormat.parse((String) context.get(inputKey));
                context.set(outputKey, outFormat.format(parse));
            }

        } catch (ParseException e) {
        }

        return true;
    }
}
