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
import org.apache.commons.configuration.ConfigurationException;
import org.apache.log4j.Logger;

import java.util.Optional;

/**
 * Created by eric on 16/4/23.
 */
public class WeixinArticleStaticHandle implements CollectHandler {
    private static Logger logger = Logger.getLogger("msg.fail");
    Sender sender = null;
    WeixinSpider weixinSpider = new WeixinSpider();
    WeixinScheduleService wxSchedule;
    public WeixinArticleStaticHandle(Sender sender) throws ConfigurationException {
        this.sender = sender;
        wxSchedule = new WeixinScheduleService();
    }

    public void handle(Bus bus, Message<CollectCondition> message) {
        try{
            Optional<WxArticle> wxArticle = weixinSpider.getArticleByUrl(message.getBody().getConditon());
            if(wxArticle.isPresent()){
                WeixinArticleStatic weixinArticleStatic = SendObjectConvertUtil.weixinArticleStaticConvert(wxArticle.get());
                sender.sendArticleStatic(weixinArticleStatic);

                wxSchedule.checkAndAddToMediaStaticSchedule(wxArticle.get().getArticleUrl());
                wxSchedule.addArticleDynamicScheduler(wxArticle.get().getPostTime(), message.getBody().getConditon());
            }else{
                logger.error(JSON.toJSONString(message));
            }
        }catch (Exception e){
            logger.error(JSON.toJSONString(message), e);
        }
    }

}
