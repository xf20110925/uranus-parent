package com.ptb.uranus.spider.smart.action;

import com.alibaba.fastjson.JSONObject;
import com.ptb.uranus.spider.smart.Context;
import org.apache.log4j.Logger;
import org.json.JSONException;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;
import javax.script.SimpleBindings;

/**
 * Created by xuefeng on 2016/3/23.
 */
public class JsAction extends Action {
    static Logger logger = Logger.getLogger(JsAction.class);
    ScriptEngineManager manager = new ScriptEngineManager();
    ScriptEngine engine = manager.getEngineByName("js");

    String jsScript;
    String outputKey;

    public String getOutputKey() {
        return outputKey;
    }

    public void setOutputKey(String outputKey) {
        this.outputKey = outputKey;
    }

    public JsAction(JSONObject jsonParam) throws JSONException {
        super(jsonParam);
    }

    public String getJsScript() {
        return jsScript;
    }

    public void setJsScript(String jsScript) {
        this.jsScript = jsScript;
    }

    public JsAction() {


    }

    @Override
    public Boolean execute(Context context) throws ScriptException {
        SimpleBindings simpleBindings = new SimpleBindings(context.getContextMap());

        Object value = engine.eval(jsScript, simpleBindings);
        context.set(outputKey, value);

        return true;
    }

    public static void main(String[] args) throws JSONException, ScriptException {
       /* JsAction jsAction = new JsAction();
        jsAction.setJsScript("test=test");
        Context context = new Context();
        context.set("test", Arrays.asList(1, 2, 3, 4));
        jsAction.execute(context);
        System.out.println(context.get("test"));*/
    }


}
