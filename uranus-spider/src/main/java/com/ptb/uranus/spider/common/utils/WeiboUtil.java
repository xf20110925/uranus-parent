package com.ptb.uranus.spider.common.utils;

import org.apache.http.client.CookieStore;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.cookie.BasicClientCookie2;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.UUID;

/**
 * Created by eric on 16/1/28.
 * 微博相关的工具类提供以下功能
 * 1 生成一个能访问微博内容页的可用Cookistore <br>
 * 2 提供微博MID 到 URL的相互转换 <br>
 * 3 微博信息的表现形式是一个MID 一串数字,但这段数字并不能直接写成进行到对应文章的页面,这时间需要一个转换工具,将其转换成一个字符串,再通过此字符串,拼接成URL,这时的URL就可以访问到对应的文章
 */
public class WeiboUtil {
    /**
     * The Logger.
     */
    static Logger logger = LoggerFactory.getLogger(WeiboUtil.class);

    /**
     * Gets vaild weibo cookie store.
     *
     * @return the vaild weibo cookie store
     */
    public static CookieStore getVaildWeiboCookieStore() {
        CookieStore cookieStore = new BasicCookieStore();
        BasicClientCookie2 sub = new BasicClientCookie2("SUB", UUID.randomUUID().toString());
        sub.setDomain(".weibo.cn");
        cookieStore.addCookie(sub);

        BasicClientCookie2 pc = new BasicClientCookie2("SUB", UUID.randomUUID().toString());
        pc.setDomain(".weibo.com");
        cookieStore.addCookie(pc);
        return cookieStore;
    }

    /**
     * The Str 62 key.
     */
    private static String[] str62key = {"0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "a", "b", "c", "d", "e", "f", "g", "h", "i", "j", "k", "l", "m", "n", "o",
            "p", "q", "r", "s", "t", "u", "v", "w", "x", "y", "z", "A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M", "N", "O", "P", "Q", "R",
            "S", "T", "U", "V", "W", "X", "Y", "Z"};

    /**
     * url转化成mid的值
     *
     * @param url the url
     * @return string
     */
    public static String url2mid(String url) {

        String mid = "";
        String k = url.toString().substring(3, 4);//用于第四位为0时的转换
        if (!k.equals("0")) {
            for (int i = url.length() - 4; i > -4; i = i - 4) {//分别以四个为一组
                int offset1 = i < 0 ? 0 : i;
                int offset2 = i + 4;
                String str = url.toString().substring(offset1, offset2);
                str = str62to10(str);//String类型的转化成十进制的数
                // 若不是第一组，则不足7位补0
                if (offset1 > 0) {
                    while (str.length() < 7) {
                        str = '0' + str;
                    }
                }
                mid = str + mid;
            }
        } else {
            for (int i = url.length() - 4; i > -4; i = i - 4) {
                int offset1 = i < 0 ? 0 : i;
                int offset2 = i + 4;
                if (offset1 > -1 && offset1 < 1 || offset1 > 4) {
                    String str = url.toString().substring(offset1, offset2);
                    str = str62to10(str);
                    // 若不是第一组，则不足7位补0
                    if (offset1 > 0) {
                        while (str.length() < 7) {
                            str = '0' + str;
                        }
                    }
                    mid = str + mid;
                } else {
                    String str = url.toString().substring(offset1 + 1, offset2);
                    str = str62to10(str);
                    // 若不是第一组，则不足7位补0
                    if (offset1 > 0) {
                        while (str.length() < 7) {
                            str = '0' + str;
                        }
                    }
                    mid = str + mid;
                }
            }
        }
        return mid;
    }

    /**
     * mid转换成url编码以后的值
     *
     * @param mid the mid
     * @return string
     */
    public static String mid2url(String mid) {
        String url = "";
        for (int j = mid.length() - 7; j > -7; j = j - 7) {//以7个数字为一个单位进行转换
            int offset3 = j < 0 ? 0 : j;
            int offset4 = j + 7;
            // String l = mid.substring(mid.length() - 14, mid.length() - 13);
            if ((j > 0 && j < 6) && (mid.substring(mid.length() - 14, mid.length() - 13).equals("0") && mid.length() == 19)) {
                String num = mid.toString().substring(offset3 + 1, offset4);
                num = int10to62(Integer.valueOf(num));//十进制转换成62进制
                url = 0 + num + url;
                if (url.length() == 9) {
                    url = url.substring(1, url.length());
                }
            } else {
                String num = mid.toString().substring(offset3, offset4);
                num = int10to62(Integer.valueOf(num));
                url = num + url;
            }
        }

        return url;
    }

    /**
     * 62进制转换成10进制
     *
     * @param str the str
     * @return string
     */
    private static String str62to10(String str) {
        String i10 = "0";
        int c = 0;
        for (int i = 0; i < str.length(); i++) {
            int n = str.length() - i - 1;
            String s = str.substring(i, i + 1);
            for (int k = 0; k < str62key.length; k++) {
                if (s.equals(str62key[k])) {
                    int h = k;
                    c += (int) (h * Math.pow(62, n));
                    break;
                }
            }
            i10 = String.valueOf(c);
        }
        return i10;
    }

    /**
     * 10进制转换成62进制
     *
     * @param int10 the int 10
     * @return string
     */
    private static String int10to62(double int10) {
        String s62 = "";
        int w = (int) int10;
        int r = 0;
        int a = 0;
        while (w != 0) {
            r = (int) (w % 62);
            s62 = str62key[r] + s62;
            a = (int) (w / 62);
            w = (int) Math.floor(a);
        }
        return s62;
    }

    public static void main(String[] args) {
        System.out.println(WeiboUtil.mid2url("3990222200538637"));
    }
}
