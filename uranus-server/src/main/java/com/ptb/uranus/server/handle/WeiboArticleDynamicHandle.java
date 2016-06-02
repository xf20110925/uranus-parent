package com.ptb.uranus.server.handle;

import com.ptb.gaia.bus.Bus;
import com.ptb.gaia.bus.message.Message;
import com.ptb.uranus.common.entity.CollectCondition;
import com.ptb.uranus.server.send.Sender;
import com.ptb.uranus.server.send.entity.article.BasicArticleDynamic;
import com.ptb.uranus.server.send.entity.convert.SendObjectConvertUtil;
import com.ptb.uranus.schedule.service.WeiboScheduleService;
import com.ptb.uranus.spider.weibo.WeiboSpider;
import com.ptb.uranus.spider.weibo.bean.WeiboArticle;
import org.apache.commons.configuration.ConfigurationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Optional;

/**
 * Created by eric on 16/4/23.
 */
public class WeiboArticleDynamicHandle implements CollectHandler {
    private static Logger ParseErroeLogger = LoggerFactory.getLogger("msg.fail");
    private Sender sender;
    WeiboSpider weiboSpider;
    WeiboScheduleService weiboScheduleService;

    public WeiboArticleDynamicHandle(Sender sender) throws ConfigurationException {
        this.sender = sender;
        weiboScheduleService = new WeiboScheduleService();
        weiboSpider = new WeiboSpider();
    }

    public void handle(Bus bus, Message<CollectCondition> message) {
        try {
            Optional<WeiboArticle> weiboArticle = weiboSpider.getWeiboArticleByArticleUrl(message.getBody().getConditon());
            if (weiboArticle.isPresent()) {
                BasicArticleDynamic wbArticleDynamic = SendObjectConvertUtil.weiboArticleDynamicConvert(weiboArticle.get());
//                weiboScheduleService.checkAndAddToMediaStaticSchedule(weiboArticle.get().getMediaId());
                sender.sendArticleDynamic(wbArticleDynamic);
            } else {
                ParseErroeLogger.error(String.valueOf(message.getRaw()));
            }
        } catch (Exception e) {
            ParseErroeLogger.error(String.valueOf(message.getRaw()), e);
        }

    }
}
