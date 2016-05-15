package com.ptb.uranus.spider.court;

/**
 * Created by eric on 16/4/14.
 */
public class JsTemplates {
    protected static String CaseListJsRequestTemplate = "var listparam = \"%s\";\n" +
            "var index = %d;\n" +
            "var page = %d;\n" +
            "var order = %s;\n" +
            "var direction = %s;\n" +
            "var callback = arguments[arguments.length - 1];\n" +
            "\n" +
            "$.ajax({\n" +
            "                url: \"/List/ListContent\",\n" +
            "                type: \"POST\",\n" +
            "                async: true,\n" +
            "                data: { \"Param\": listparam, \"Index\": index, \"Page\": page, \"Order\": order, \"Direction\": direction },\n" +
            "                success: function (data) {\n" +
            "                \t\tcallback(data)\n" +
            "                }});";

    protected static String CaseSumaryTemplate = "var listparam = \"%s\";\n" +
            "var callback = arguments[arguments.length - 1];\n" +
            "\n" +
            "$.ajax({\n" +
            "                url: \"/Content/GetSummary\",\n" +
            "                type: \"POST\",\n" +
            "                async: true,\n" +
            "                data: { \"docId\": listparam},\n" +
            "                success: function (data) {\n" +
            "                \t\tcallback(data)\n" +
            "                }});";


}
