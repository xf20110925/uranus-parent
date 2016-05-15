package com.ptb.uranus.spider.smart.action;

import com.alibaba.fastjson.JSONObject;
import com.jayway.jsonpath.DocumentContext;
import com.jayway.jsonpath.JsonPath;
import com.ptb.uranus.spider.smart.Context;
import com.ptb.uranus.spider.smart.utils.SmartSpiderConverter;
import com.ptb.utils.string.RegexUtils;
import org.apache.log4j.Logger;
import org.json.JSONException;

import java.util.Arrays;
import java.util.List;

/**
 * Created by xuefeng on 2016/3/23.
 */
public class JsonParseAction extends Action {
    static Logger logger = Logger.getLogger(JsonParseAction.class);

    String isJsonP;
    String formula;
    String inputKey;
    String outputKey;

    public String getIsJsonP() {
        return isJsonP;
    }

    public void setIsJsonP(String isJsonP) {
        this.isJsonP = isJsonP;
    }

    public String getFormula() {
        return formula;
    }

    public void setFormula(String formula) {
        this.formula = formula;
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

    public JsonParseAction(JSONObject jsonParam) throws JSONException {
        super(jsonParam);
    }

    public JsonParseAction(String isJsonP, String formula, String inputKey, String outputKey) {
        this.isJsonP = isJsonP;
        this.formula = formula;
        this.inputKey = inputKey;
        this.outputKey = outputKey;
    }

    @Override
    public Boolean execute(Context context) {
        String json;
        DocumentContext doc;

        if (!(context.get(inputKey) instanceof DocumentContext)) {
            if (isJsonP != null && isJsonP.equals("true")) {
                json = RegexUtils.sub(".*\\((.*\\))", SmartSpiderConverter.getContextString(context, inputKey), 0);
            } else {
                json = SmartSpiderConverter.getContextString(context, inputKey);
            }
            doc = JsonPath.parse(json);
        } else {
            doc = (DocumentContext) context.get(inputKey);
        }

        Object result = doc.read(formula);

        if (result != null && !result.equals("")) {
            if (!(result instanceof List)) {
                context.set(outputKey, Arrays.asList(result));
            } else {
                context.set(outputKey, result);
            }
        }

        return true;
    }


}
