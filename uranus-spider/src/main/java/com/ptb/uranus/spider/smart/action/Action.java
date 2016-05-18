package com.ptb.uranus.spider.smart.action;

import com.alibaba.fastjson.JSONObject;
import com.ptb.uranus.spider.smart.Context;
import com.ptb.uranus.spider.smart.utils.StringUtil;
import org.slf4j.Logger;
import org.json.JSONException;
import org.slf4j.LoggerFactory;
import org.springframework.util.ReflectionUtils;

import javax.script.ScriptException;
import java.io.IOException;
import java.lang.reflect.Method;

/**
 * Created by xuefeng on 2016/3/22.
 */
public abstract class Action implements Comparable<Action> {
    static Logger logger = LoggerFactory.getLogger(Action.class);
    String order;

    private String className;

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public Action() {
    }

    public Action(JSONObject jsonParam) throws JSONException {


        jsonParam.forEach((k, v) -> {
            String value = String.valueOf(v);
            Method method = ReflectionUtils.findMethod(this.getClass(), String.format("set%s", StringUtil.toUpperCaseFirstOne(k)), String.class);
            //Method method = this.getClass().getDeclaredMethod();
            try {
                if(method != null) {
                    method.invoke(this, value);
                }
            } catch (Exception e) {

                logger.error("[{}] : [{}]", k, v, e);
            }

        });
    }

    public abstract Boolean execute(Context context) throws Exception;

    public String getOrder() {
        return order;
    }

    public void setOrder(String order) {
        this.order = order;
    }

    @Override
    public int compareTo(Action action) {
        try {
            return Integer.valueOf(order).compareTo(Integer.valueOf(action.getOrder()));
        }catch (Exception e) {
            return 0;
        }
    }
}
