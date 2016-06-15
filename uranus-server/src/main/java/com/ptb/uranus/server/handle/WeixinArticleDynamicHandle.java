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
import com.ptb.utils.log.LogUtils;
import org.apache.commons.configuration.ConfigurationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Optional;

/**
 * Created by eric on 16/4/23.
 */
public class WeixinArticleDynamicHandle implements CollectHandler {
    private static Logger ParseErroeLogger = LoggerFactory.getLogger("msg.fail");
    private Sender sender;
    WeixinSpider weixinSpider = new WeixinSpider();
    WeixinScheduleService wxSchedule;

    public WeixinArticleDynamicHandle(Sender sender) throws ConfigurationException {
        this.sender = sender;
        wxSchedule = new WeixinScheduleService();
    }

    public void handle(Bus bus, Message<CollectCondition> message) {
        try {
            LogUtils.logInfo("uranus-server", "C_WX_A_D recv", LogUtils.ActionResult.success, "");
            Optional<ReadLikeNum> readLikeNum = weixinSpider.getArticleReadLikeNumByUrl(message.getBody().getConditon(), 60);
            if(!readLikeNum.isPresent()) {
                readLikeNum = weixinSpider.getArticleReadLikeNumByUrl(message.getBody().getConditon(), 60);
            }

            if (readLikeNum.isPresent()) {
                BasicArticleDynamic wxArticleDynamic = SendObjectConvertUtil.wxArticleDynamicConvert(readLikeNum.get());
                wxArticleDynamic.setUrl(message.getBody().getConditon());
//                wxSchedule.checkAndAddToMediaStaticSchedule(wxArticleDynamic.getUrl());
                sender.sendArticleDynamic(wxArticleDynamic);
                LogUtils.logInfo("uranus-server", "C_WX_A_D send", LogUtils.ActionResult.success, "");
            } else {
                ParseErroeLogger.error(new String(message.getRaw()));
                LogUtils.log("uranus-server", "C_WX_A_D error", LogUtils.ActionResult.failed, new String(message.getRaw()));
            }
        } catch (Exception e) {
            ParseErroeLogger.error(new String(message.getRaw()), e);
            LogUtils.log("uranus-server", "C_WX_A_D exception", LogUtils.ActionResult.failed, new String(message.getRaw()+e.getLocalizedMessage()));
        }

    }
}
