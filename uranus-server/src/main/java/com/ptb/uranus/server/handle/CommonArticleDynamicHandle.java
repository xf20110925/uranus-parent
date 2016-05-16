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
import com.ptb.uranus.spider.smart.entity.DynamicData;
import com.ptb.uranus.spider.smart.utils.SmartSpiderConverter;
import org.apache.commons.configuration.ConfigurationException;
import org.apache.log4j.Logger;

import java.util.Optional;

/**
 * Created by eric on 16/4/23.
 */
public class CommonArticleDynamicHandle implements CollectHandler {
    private static Logger ParseErrorLogger = Logger.getLogger("smartspider.error");
    private static Logger ParseSuccessLogger = Logger.getLogger("smartspider.success");
    private final Sender sender;
    SmartSpider smartSpider = new SmartSpider();
    CommonMediaScheduleService commonMediaScheduleService;

    public CommonArticleDynamicHandle(Sender sender) throws ConfigurationException {
        commonMediaScheduleService = new CommonMediaScheduleService();
        this.sender = sender;
    }

    public void handle(Bus bus, Message<CollectCondition> message) {
        String url = message.getBody().getConditon();
        try {
            Optional<SpiderResult> spiderResultOptional = smartSpider.crawl(url, "articleDynamicData");
            if (spiderResultOptional.isPresent()) {
                DynamicData dynamicData = SmartSpiderConverter.convertToDynamicData(spiderResultOptional.get());
                sender.sendArticleDynamic(SendObjectConvertUtil.commonArticleDyanmicConvert(dynamicData, url));
                ParseSuccessLogger.info(JSON.toJSONString(dynamicData));
            }else{
                ParseErrorLogger.error(message.getRaw());
            }
        } catch (Exception e) {
            ParseErrorLogger.error(message.getRaw(), e);
            return;
        }



    }
}
