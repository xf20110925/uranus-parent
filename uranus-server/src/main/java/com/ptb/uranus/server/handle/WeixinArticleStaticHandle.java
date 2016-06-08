package com.ptb.uranus.server.handle;

import com.alibaba.fastjson.JSON;
import com.ptb.gaia.bus.Bus;
import com.ptb.gaia.bus.message.Message;
import com.ptb.uranus.common.entity.CollectCondition;
import com.ptb.uranus.server.send.Sender;
import com.ptb.uranus.server.send.entity.article.WeixinArticleStatic;
import com.ptb.uranus.server.send.entity.convert.SendObjectConvertUtil;
import com.ptb.uranus.schedule.service.WeixinScheduleService;
import com.ptb.uranus.spider.weixin.WeixinSpider;
import com.ptb.uranus.spider.weixin.bean.WxArticle;
import com.ptb.utils.log.LogUtils;
import org.apache.commons.configuration.ConfigurationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Optional;

/**
 * Created by eric on 16/4/23.
 */
public class WeixinArticleStaticHandle implements CollectHandler {
    private static Logger logger = LoggerFactory.getLogger("msg.fail");
    Sender sender = null;
    WeixinSpider weixinSpider = new WeixinSpider();
    WeixinScheduleService wxSchedule;
    public WeixinArticleStaticHandle(Sender sender) throws ConfigurationException {
        this.sender = sender;
        wxSchedule = new WeixinScheduleService();
    }

    public void handle(Bus bus, Message<CollectCondition> message) {
        try{
            LogUtils.logInfo("uranus-server", "C_WX_A_S recv", LogUtils.ActionResult.success, "");
            Optional<WxArticle> wxArticle = weixinSpider.getArticleByUrl(message.getBody().getConditon());
            if(wxArticle.isPresent()){
                WeixinArticleStatic weixinArticleStatic = SendObjectConvertUtil.weixinArticleStaticConvert(wxArticle.get());
                sender.sendArticleStatic(weixinArticleStatic);
/*                wxSchedule.checkAndAddToMediaStaticSchedule(weixinArticleStatic.getUrl());*/
                wxSchedule.addArticleDynamicScheduler(wxArticle.get().getPostTime(), message.getBody().getConditon());
                LogUtils.logInfo("uranus-server", "C_WX_A_S send", LogUtils.ActionResult.success, "");
            }else{
                logger.error(JSON.toJSONString(message));
                LogUtils.log("uranus-server", "C_WX_A_S error", LogUtils.ActionResult.failed, String.valueOf(message.getRaw()));
            }
        }catch (Exception e){
            logger.error(JSON.toJSONString(message), e);
            LogUtils.log("uranus-server", "C_WX_A_S exception", LogUtils.ActionResult.failed, String.valueOf(message.getRaw()));
        }
    }
}
