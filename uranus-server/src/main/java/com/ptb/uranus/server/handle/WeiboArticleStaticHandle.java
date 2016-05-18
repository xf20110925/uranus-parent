package com.ptb.uranus.server.handle;

import com.ptb.gaia.bus.Bus;
import com.ptb.gaia.bus.message.Message;
import com.ptb.uranus.common.entity.CollectCondition;
import com.ptb.uranus.schedule.service.WeiboScheduleService;
import com.ptb.uranus.server.send.Sender;
import com.ptb.uranus.server.send.entity.article.WeiboArticleStatic;
import com.ptb.uranus.server.send.entity.convert.SendObjectConvertUtil;
import com.ptb.uranus.spider.weibo.WeiboSpider;
import com.ptb.uranus.spider.weibo.bean.WeiboArticle;
import org.apache.commons.configuration.ConfigurationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Optional;

/**
 * Created by eric on 16/4/23.
 */
public class WeiboArticleStaticHandle implements CollectHandler {
    private static Logger logger = LoggerFactory.getLogger("msg.wb.fail");
    private final WeiboScheduleService weiboScheduleService;
    Sender sender;

    public WeiboArticleStaticHandle(Sender sender) throws ConfigurationException {
        this.sender = sender;
        weiboScheduleService = new WeiboScheduleService();
    }

    public void handle(Bus bus, Message<CollectCondition> message) {
        try {
            WeiboSpider weiboSpider = new WeiboSpider();
            Optional<WeiboArticle> weiboArticle = weiboSpider.getWeiboArticleByArticleUrl(message.getBody().getConditon());
            if (weiboArticle.isPresent()) {
                WeiboArticleStatic weiboArticleStatic = SendObjectConvertUtil.weiboArticleStaticConvert(weiboArticle.get());
                weiboScheduleService.checkAndAddToMediaStaticSchedule(weiboArticle.get().getMediaId());
                weiboScheduleService.addArticleDynamicScheduler(weiboArticle.get().getPostTime(), weiboArticle.get().getArticleUrl());
                sender.sendArticleStatic(weiboArticleStatic);
            }
        } catch (Exception e) {
            logger.error(String.valueOf(message.getRaw()), e);
        }
    }

}
