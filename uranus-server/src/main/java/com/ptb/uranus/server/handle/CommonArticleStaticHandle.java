package com.ptb.uranus.server.handle;

import com.alibaba.fastjson.JSON;
import com.ptb.gaia.bus.Bus;
import com.ptb.gaia.bus.message.Message;
import com.ptb.uranus.common.entity.CollectCondition;
import com.ptb.uranus.server.send.Sender;
import com.ptb.uranus.server.send.entity.convert.SendObjectConvertUtil;
import com.ptb.uranus.schedule.service.CommonMediaScheduleService;
import com.ptb.uranus.spider.smart.SmartSpider;
import com.ptb.uranus.spider.smart.SpiderResult;
import com.ptb.uranus.spider.smart.entity.Article;
import com.ptb.uranus.spider.smart.utils.SmartSpiderConverter;
import org.apache.commons.configuration.ConfigurationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Optional;

/**
 * Created by eric on 16/4/23.
 */
public class CommonArticleStaticHandle implements CollectHandler {
    private static Logger ParseErrorLogger = LoggerFactory.getLogger("smartspider.error");
    private static Logger ParseSuccessLogger = LoggerFactory.getLogger("smartspider.success");
    private final Sender sender;

    SmartSpider smartSpider;
    CommonMediaScheduleService commonMediaScheduleService;

    public CommonArticleStaticHandle(Sender sender) throws ConfigurationException {
        commonMediaScheduleService = new CommonMediaScheduleService();
        this.sender = sender;
        smartSpider = new SmartSpider();
    }

    public void handle(Bus bus, Message<CollectCondition> message) {
        String url = message.getBody().getConditon();
        try {
            Optional<SpiderResult> spiderResultOptional = smartSpider.crawl(url, "article");
            if (spiderResultOptional.isPresent()) {
                Article article = SmartSpiderConverter.convertToArticle(spiderResultOptional.get());
                if (article.isValidArticle() == true) {
                    commonMediaScheduleService.addArticleDynamicScheduler(article.getUrl(), article.getPostTime());
                    sender.sendArticleStatic(SendObjectConvertUtil.commonMediaArticleStaticConvert(article));
                    if(ParseSuccessLogger.isInfoEnabled()){
                        ParseSuccessLogger.info(JSON.toJSONString(article));
                    }
                } else {
                    ParseErrorLogger.error(String.valueOf(message.getRaw()));
                }
            }
        } catch (Exception e) {
            ParseErrorLogger.error(JSON.toJSONString(message), e);
            return;
        }

    }
}
