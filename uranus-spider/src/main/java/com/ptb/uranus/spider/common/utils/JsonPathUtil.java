package com.ptb.uranus.spider.common.utils;

import com.jayway.jsonpath.DocumentContext;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Created by eric on 16/3/8.
 *
 * JSONPATH的相关工具类 <br>
 * 提供的功能:<br>
 * 1 提供了当提取不到对应字段信息给定默认值的功能<br>
 */
public class JsonPathUtil {
    /**
     * The Log.
     */
    static final Log log = LogFactory.getLog(JsonPathUtil.class);

    /**
     * 通过提供的jsonPath表达式取提取JSON串中的对应字段,如果字段为空,则给定一个默认值(!只返还字符串,外部使用需要做强转
     *
     * @param dcxt         the dcxt //jsonpath上下文信息
     * @param jsonPathLamada       the patter //抽取字段的JSONPATH表达式
     * @param defaultValue the smart value //没有取到时,使用的默认值
     * @return the string
     */
    static public String parse(DocumentContext dcxt, String jsonPathLamada, String defaultValue) {
        try {
            return dcxt.read(jsonPathLamada).toString();
        } catch (Exception e) {
            log.warn(e.getCause());
        }
        return defaultValue;
    }
}
