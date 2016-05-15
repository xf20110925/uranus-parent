package com.ptb.uranus.asistant.core.proxy;

import com.ptb.utils.web.HttpUtil;
import org.apache.http.HttpHost;
import org.apache.http.client.fluent.Request;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by eric on 16/1/26.
 */
public class ProxyBuilder {
    static public boolean checkProxyIsGood(String proxy) {
        int retryNum = 4;
        while (retryNum-- > 0) {
            try {
                Request.Get("http://fankui.help.sogou.com/index.php/web/web/index/type/4").viaProxy(proxy).connectTimeout(2000).socketTimeout(10000).execute().returnContent();
                return true;
            } catch (Exception e) {
                try {
                    Thread.sleep(500);
                } catch (InterruptedException e1) {
                    e1.printStackTrace();
                }
            }
        }
        return false;
    }

    public String checkProxySchema(String proxy) {
        String[] hostPort = proxy.split(":");
        String host = hostPort[0];
        int port = Integer.parseInt(hostPort[1]);
        int retryNum = 3;
        while (retryNum-- > 0) {
            try {
                Request.Get("http://fankui.help.sogou.com/index.php/web/web/index/type/4").userAgent(HttpUtil.UA_PC_CHROME).viaProxy(new HttpHost(host, port)).connectTimeout(2000).socketTimeout(10000).execute().returnContent();
                return String.format("http://%s", proxy);
            } catch (Exception e) {
            }
        }
        return null;
    }

    public List<String> create(int i) {
        String proxyAddress = "http://api.goubanjia.com/api/get.shtml?order=87e52437da10c3071804f3b6a3b4806a&num=100&area=%E4%B8%AD%E5%9B%BD&carrier=0&protocol=2&an1=1&an2=2&an3=3&sp1=1&sort=1&rettype=1&seprator=%0D%0A";
        try {
            String result = Request.Get(proxyAddress).execute().returnContent().toString();
            List<String> proxys = Arrays.asList(result.split("\r\n"));
            return proxys.parallelStream().map((proxy) -> {
                return checkProxySchema(proxy);
            }).filter((proxy) -> proxy != null).collect(Collectors.toList());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new ArrayList<>();
    }

    public static void main(String[] args) {
        List<String> strings = new ProxyBuilder().create(4);
        strings.forEach(k -> {
            System.out.println(checkProxyIsGood(k));
        });
    }

}
