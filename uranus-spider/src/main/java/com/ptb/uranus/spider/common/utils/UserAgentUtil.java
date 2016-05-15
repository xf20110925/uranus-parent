package com.ptb.uranus.spider.common.utils;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by eric on 16/1/21.
 * 主要是对各类Useragent进行管理
 * @deprecated
 */
public class UserAgentUtil {
    /**
     * The Pc user agents.
     */
    static String[] PCUserAgents =
            {
                    "Mozilla/5.0 (Macintosh; U; Intel Mac OS X 10_6_8; en-us) AppleWebKit/534.50 (KHTML, like Gecko) Version/5.1 Safari/534.50",
                    "Mozilla/5.0 (Windows; U; Windows NT 6.1; en-us) AppleWebKit/534.50 (KHTML, like Gecko) Version/5.1 Safari/534.50",
                    "Mozilla/5.0 (compatible; MSIE 9.0; Windows NT 6.1; Trident/5.0;",
                    "Mozilla/5.0 (Macintosh; Intel Mac OS X 10.6; rv:2.0.1) Gecko/20100101 Firefox/4.0.1",
                    "Mozilla/5.0 (Windows NT 6.1; rv:2.0.1) Gecko/20100101 Firefox/4.0.1",
                    "Opera/9.80 (Macintosh; Intel Mac OS X 10.6.8; U; en) Presto/2.8.131 Version/11.11",
                    "Opera/9.80 (Windows NT 6.1; U; en) Presto/2.8.131 Version/11.11",
                    "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_7_0) AppleWebKit/535.11 (KHTML, like Gecko) Chrome/17.0.963.56 Safari/535.11"};

    /**
     * The Seed. 用来产生随机数,随机选择UA使用
     *
     */
    static AtomicInteger seed = new AtomicInteger(0);

    /**
     * Gets pc user agent.
     *
     * @return the pc user agent
     */
    public static String getPcUserAgent() {
        return "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_8_4) AppleWebKit/600.7.12 (KHTML, like Gecko) Version/6.2.7 Safari/537.85.16";
    }
}
