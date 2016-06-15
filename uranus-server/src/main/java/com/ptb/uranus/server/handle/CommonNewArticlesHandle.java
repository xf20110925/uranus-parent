package com.ptb.uranus.server.handle;

import com.alibaba.fastjson.JSON;
import com.ptb.gaia.bus.Bus;
import com.ptb.gaia.bus.message.Message;
import com.ptb.uranus.common.entity.CollectCondition;
import com.ptb.uranus.server.send.Sender;
import com.ptb.uranus.schedule.service.CommonMediaScheduleService;
import com.ptb.uranus.spider.smart.SmartSpider;
import com.ptb.uranus.spider.smart.SpiderResult;
import com.ptb.uranus.spider.smart.entity.NewScheduleUrls;
import com.ptb.uranus.spider.smart.utils.SmartSpiderConverter;
import com.ptb.utils.log.LogUtils;
import org.apache.commons.configuration.ConfigurationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Optional;

/**
 * Created by eric on 16/4/25.
 */
public class CommonNewArticlesHandle implements CollectHandler {
    private static Logger ParseErrorLogger = LoggerFactory.getLogger("smartspider.error");
    private static Logger ParseSuccessLogger = LoggerFactory.getLogger("smartspider.success");
    private final Sender sender;

    CommonMediaScheduleService schedulerNewsArticlesService;
    SmartSpider smartSpider;

    public CommonNewArticlesHandle(Sender sender) throws ConfigurationException {
        schedulerNewsArticlesService = new CommonMediaScheduleService();
        smartSpider = new SmartSpider();
        this.sender = sender;
    }

    public void handle(Bus bus, Message<CollectCondition> message) {
        String url = message.getBody().getConditon();
        try {
            LogUtils.logInfo("uranus","C_A_A_N recv", LogUtils.ActionResult.success, "");
            Optional<SpiderResult> spiderResultOptional = smartSpider.crawl(url, "articleList");
            if (spiderResultOptional.isPresent()) {
                SpiderResult spiderResult = spiderResultOptional.get();
                NewScheduleUrls newScheduleUrls = SmartSpiderConverter.convertToNewSchedulerUrls(System.currentTimeMillis(), spiderResult);
                schedulerNewsArticlesService.addArticleStaticSchedulers(newScheduleUrls.getUrls());
                LogUtils.logInfo("uranus","C_A_A_N add", LogUtils.ActionResult.success, "");
                if(ParseSuccessLogger.isInfoEnabled()){
                    ParseSuccessLogger.info(JSON.toJSONString(newScheduleUrls));
                }
            }else{
                ParseErrorLogger.error(new String(message.getRaw()));
                LogUtils.logInfo("uranus","C_A_A_N error", LogUtils.ActionResult.failed, new String(message.getRaw()));
            }

        } catch (Exception e) {
            ParseErrorLogger.error(new String(message.getRaw()), e);
            LogUtils.logInfo("uranus","C_A_A_N exception", LogUtils.ActionResult.failed, new String(message.getRaw()+e.getLocalizedMessage()));
            return;
        }
    }
}
