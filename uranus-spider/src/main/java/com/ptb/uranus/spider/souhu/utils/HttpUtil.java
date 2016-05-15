package com.ptb.uranus.spider.souhu.utils;

import org.apache.http.client.ClientProtocolException;
import org.apache.commons.lang.StringUtils;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.Date;

public class HttpUtil {
    private static final String USER_AGENT = "Mozilla/5.0";

    public static CloseableHttpClient getHttpClient() {
        PoolingHttpClientConnectionManager cm = new PoolingHttpClientConnectionManager();
        // 将最大连接数增加到200
        cm.setMaxTotal(200);
        CloseableHttpClient httpClient = HttpClients.custom()
                .setConnectionManager(cm)
                .build();
        return httpClient;
    }

    public static String httpGet(String url) {

        CloseableHttpClient client = getHttpClient();
        StringBuffer result = new StringBuffer();
        try {
            HttpGet request = new HttpGet(url);
            request.addHeader("User-Agent", USER_AGENT);
            HttpResponse response = client.execute(request);
            BufferedReader rd = new BufferedReader(
                    new InputStreamReader(response.getEntity().getContent(), "gbk"));
            String line = "";
            while ((line = rd.readLine()) != null) {
                result.append(line);
            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                client.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return result.toString();
    }
}
