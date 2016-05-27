package com.ptb.uranus.spider.smart.action;

import com.alibaba.fastjson.JSONObject;
import com.ptb.uranus.spider.common.utils.ParseUtils;
import com.ptb.uranus.spider.smart.Context;
import org.slf4j.Logger;
import org.json.JSONException;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by xuefeng on 2016/3/23.
 */
public class DomParseAction extends Action {
    static Logger logger = LoggerFactory.getLogger(DomParseAction.class);

    String formula;
    String formulaType;
    String retKeyString;

    public DomParseAction() {
    }

    public DomParseAction(JSONObject jsonParam) throws JSONException {
        super(jsonParam);
    }

    @Override
    public Boolean execute(Context context) {
        List<String> values = null;
        List<String> newValues = new ArrayList();
        if (context.containsKey(retKeyString)) {
            values = (List<String>) context.get(retKeyString);
        }
        switch (formulaType) {
            case ParamConfig.ACTION_CSSFORMULAR:
                if (!context.containsKey(Context.DOM_DOCUMENT)) {
                    String pageSource = (String) context.get(Context.PAGESOURCE);
                    context.set(Context.DOM_DOCUMENT, Jsoup.parse(pageSource));
                }
                Document document = (Document) context.get(Context.DOM_DOCUMENT);
                newValues = ParseUtils.css(document, formula);
                break;
            case ParamConfig.ACTION_REGEXFORMULAR:
                newValues = ParseUtils.regex((String) context.get(Context.PAGESOURCE), formula);
                break;
        }

        if (values != null) {
            if (newValues.size() != 0 && newValues.get(0).length() > 0) {
                values.addAll(newValues);
            }
        } else {
            values = newValues;
        }
        context.set(retKeyString, values);
        return true;
    }

    public String getFormula() {
        return formula;
    }

    public DomParseAction setFormula(String formula) {
        this.formula = formula;
        return this;
    }

    public String getFormulaType() {
        return formulaType;
    }

    public DomParseAction setFormulaType(String formulaType) {
        this.formulaType = formulaType;
        return this;
    }

    public String getRetKeyString() {
        return retKeyString;
    }

    public DomParseAction setRetKeyString(String retKeyString) {
        this.retKeyString = retKeyString;
        return this;
    }

}
