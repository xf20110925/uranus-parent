package com.ptb.uranus.spider.common.utils;

import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * Created by eric on 16/3/23.
 */
public class ParseUtils {

    static final String Decollator = ":::";

    private static class CssFormular {
        String cssSelector;
        String textPostion;
        String attrkey;

        final public static String PostionAttr = "attr";
        final public static String PostionText = "text";
        final public static String PostionHtml = "html";

        public CssFormular(String formular) {
            String[] split = formular.split(Decollator);
            if (split.length >= 1) {
                this.cssSelector = split[0];
                if (split.length >= 2) {
                    this.textPostion = split[1].trim();
                }
                if (split.length >= 3) {
                    this.attrkey = split[2].trim();
                }
            }
        }

        public String getCssSelector() {
            return cssSelector;
        }

        public String getAttrkey() {
            return attrkey;
        }

        public String getTextPostion() {
            return textPostion;
        }
    }

    public static List<String> css(Document document, String formular) {
        CssFormular cssFormular = new CssFormular(formular);
        Elements elements = document.select(cssFormular.getCssSelector());

        List<String> results = elements.stream().map(element -> {
            switch (cssFormular.getTextPostion()) {
                case CssFormular.PostionAttr:
                    return element.attr(cssFormular.getAttrkey());
                case CssFormular.PostionText:
                    return element.text();
                case CssFormular.PostionHtml:
                    return element.outerHtml();
                default:
                    return element.outerHtml();
            }
        }).filter(v -> (v != null && v != "")).collect(Collectors.toList());

        return results;
    }

    public static List<String> regex(String pageSource, String formular) {
        String[] formularArr = formular.split(Decollator);
        Pattern p = Pattern.compile(formularArr[0].trim(), Pattern.DOTALL);
        Matcher matcher = p.matcher(pageSource);
        List<String> result = new ArrayList<>();
        while (matcher.find()) {
            result.add(matcher.group(Integer.parseInt(formularArr[1].trim())));
        }
        return result;
    }

    public static void main(String[] args) {
        List<String> regex = regex(" \n" +
                "            2009年12月11日 20:44:29\n" +
                "            　来源：", ".*(\\d{4}\\u5e74\\d{2}\\u6708\\d{2}\\u65e5 \\d{2}:\\d{2}:\\d{2}).* ::: 1");
        System.out.println(regex);
    }
}
