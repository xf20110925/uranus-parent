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
import com.ptb.utils.log.LogUtils;
import org.apache.commons.configuration.ConfigurationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Optional;

/**
 * Created by eric on 16/4/23.
 */
public class CommonArticleDynamicHandle implements CollectHandler {
    private static Logger ParseErrorLogger = LoggerFactory.getLogger("smartspider.error");
    private static Logger ParseSuccessLogger = LoggerFactory.getLogger("smartspider.success");
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
            LogUtils.logInfo("uranus","C_A_A_D recv", LogUtils.ActionResult.success, "");
            Optional<SpiderResult> spiderResultOptional = smartSpider.crawl(url, "articleDynamicData");
            if (spiderResultOptional.isPresent()) {
                DynamicData dynamicData = SmartSpiderConverter.convertToDynamicData(spiderResultOptional.get());
                sender.sendArticleDynamic(SendObjectConvertUtil.commonArticleDyanmicConvert(dynamicData, url));
                LogUtils.logInfo("uranus","C_A_A_D send", LogUtils.ActionResult.success, "");
                if(ParseSuccessLogger.isInfoEnabled()){
                    ParseSuccessLogger.info(JSON.toJSONString(dynamicData));
                }
            }else{
                ParseErrorLogger.error(new String(message.getRaw()));
                LogUtils.logInfo("uranus","C_A_A_D error", LogUtils.ActionResult.failed, new String(message.getRaw()));
            }
        } catch (Exception e) {
            ParseErrorLogger.error(new String(message.getRaw()), e);
            LogUtils.logInfo("uranus","C_A_A_D exception", LogUtils.ActionResult.failed, new String(message.getRaw()+e.getLocalizedMessage()));
            return;
        }
    }
}
