package com.ptb.uranus.server.handle;

import com.ptb.gaia.bus.Bus;
import com.ptb.gaia.bus.message.Message;
import com.ptb.uranus.common.entity.CollectCondition;
import com.ptb.uranus.server.send.Sender;
import com.ptb.uranus.schedule.service.WeixinScheduleService;
import com.ptb.uranus.spider.weixin.WeixinSpider;
import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.log4j.Logger;

import java.util.List;
import java.util.Optional;

/**
 * Created by eric on 16/4/25.
 */
public class WeixinNewArticlesHandle implements CollectHandler {
    private static Logger ParseErroeLogger = Logger.getLogger("msg.fail");

    WeixinSpider weixinSpider = new WeixinSpider();
    WeixinScheduleService wxSchedule;
    public WeixinNewArticlesHandle(Sender sender) throws ConfigurationException {
        wxSchedule = new WeixinScheduleService();
    }

    public void handle(Bus bus, Message<CollectCondition> message) {
        try {

            String[] conditon = message.getBody().getConditon().split(":::");
            String biz = conditon[0];
            long lastTime = Long.parseLong(conditon[1]);
            Optional<ImmutablePair<Long, List<String>>> recentArticles = weixinSpider.getRecentArticlesByBiz(biz, lastTime);
            if (recentArticles.isPresent()) {
                wxSchedule.addArticleStaticSchedulers(recentArticles.get().getRight());
                wxSchedule.updateWeixinMediaCondition(message.getBody(), biz, recentArticles.get().getLeft());
            }else {
                ParseErroeLogger.error(message.getRaw());
            }
        } catch (Exception e) {
            ParseErroeLogger.error(message.getRaw(), e);
        }
    }
}
