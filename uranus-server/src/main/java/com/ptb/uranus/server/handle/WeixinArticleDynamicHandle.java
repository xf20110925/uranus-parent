package com.ptb.uranus.server.handle;

import com.ptb.gaia.bus.Bus;
import com.ptb.gaia.bus.message.Message;
import com.ptb.uranus.common.entity.CollectCondition;
import com.ptb.uranus.server.send.Sender;
import com.ptb.uranus.server.send.entity.article.BasicArticleDynamic;
import com.ptb.uranus.server.send.entity.convert.SendObjectConvertUtil;
import com.ptb.uranus.schedule.service.WeixinScheduleService;
import com.ptb.uranus.spider.weixin.WeixinSpider;
import com.ptb.uranus.spider.weixin.bean.ReadLikeNum;
import org.apache.commons.configuration.ConfigurationException;
import org.apache.log4j.Logger;

import java.util.Optional;

/**
 * Created by eric on 16/4/23.
 */
public class WeixinArticleDynamicHandle implements CollectHandler {
    private static Logger ParseErroeLogger = Logger.getLogger("msg.fail");
    private Sender sender;
    WeixinSpider weixinSpider = new WeixinSpider();
    WeixinScheduleService wxSchedule;

    public WeixinArticleDynamicHandle(Sender sender) throws ConfigurationException {
        this.sender = sender;
        wxSchedule = new WeixinScheduleService();
    }

    public void handle(Bus bus, Message<CollectCondition> message) {
        try {
            Optional<ReadLikeNum> readLikeNum = weixinSpider.getArticleReadLikeNumByUrl(message.getBody().getConditon(), 60);
            if (readLikeNum.isPresent()) {
                BasicArticleDynamic wxArticleDynamic = SendObjectConvertUtil.wxArticleDynamicConvert(readLikeNum.get());
                wxArticleDynamic.setUrl(message.getBody().getConditon());
                wxSchedule.checkAndAddToMediaStaticSchedule(wxArticleDynamic.getUrl());
                sender.sendArticleDynamic(wxArticleDynamic);
            } else {
                ParseErroeLogger.error(new String(message.getRaw()));
            }
        } catch (Exception e) {
            ParseErroeLogger.error(new String(message.getRaw()), e);
        }

    }
}
