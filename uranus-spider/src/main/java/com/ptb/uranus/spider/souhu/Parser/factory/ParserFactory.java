package com.ptb.uranus.spider.souhu.Parser.factory;

import com.ptb.uranus.spider.souhu.Parser.itf.BaseParser;
import com.ptb.uranus.spider.souhu.Parser.itf.impl.BoxClearParser;
import com.ptb.uranus.spider.souhu.Parser.itf.impl.MainAreaParser;
import com.ptb.uranus.spider.souhu.Parser.itf.impl.WapperParser;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

/**
 * Created by xuefeng on 2016/3/16.
 */
public class ParserFactory {

    private ParserFactory() {  }

    public static BaseParser getInstance(String html){
        Document doc = Jsoup.parse(html);
        if (doc.select("div.content-box.clear").first() != null) {
            return new BoxClearParser();
        }else  if(doc.select("div.content-wrapper").first() != null){
            return new WapperParser();
        }else if (doc.getElementById("headers") != null && doc.getElementById("main") != null) {
            return new MainAreaParser();
        }else{
            throw new RuntimeException("不能解析的页面！");
        }
    }
}
