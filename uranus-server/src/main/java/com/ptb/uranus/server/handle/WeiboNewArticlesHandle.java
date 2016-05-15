package com.ptb.uranus.server.handle;

import com.ptb.gaia.bus.Bus;
import com.ptb.gaia.bus.message.Message;
import com.ptb.uranus.common.entity.CollectCondition;
import com.ptb.uranus.server.send.Sender;
import com.ptb.uranus.schedule.service.WeiboScheduleService;
import com.ptb.uranus.spider.weibo.WeiboSpider;
import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.log4j.Logger;

import java.util.List;
import java.util.Optional;

/**
 * Created by eric on 16/4/25.
 */
public class WeiboNewArticlesHandle implements com.ptb.uranus.server.handle.CollectHandler {
    private static Logger ParseErroeLogger = Logger.getLogger("msg.fail");
    WeiboSpider weiboSpider = new WeiboSpider();
    WeiboScheduleService wbScheduleService = new WeiboScheduleService();

    public WeiboNewArticlesHandle(Sender sender) throws ConfigurationException {
    }

    public void handle(Bus bus, Message<CollectCondition> message) {
        try {
            String[] conditon = message.getBody().getConditon().split(":::");
            String containerId = conditon[0];
            long lastTime = Long.parseLong(conditon[1]);
            Optional<ImmutablePair<Long, List<String>>> recentArticlesPair = weiboSpider.getRecentArticlesByContainerID(containerId, lastTime);
            if (recentArticlesPair.isPresent()) {
                List<String> recentArticles = recentArticlesPair.get().getRight();
                long lastestTime = recentArticlesPair.get().getLeft().longValue();
                wbScheduleService.addArticleStaticSchedulers(recentArticles);
                wbScheduleService.updateMediaCondition(message.getBody(), containerId, lastestTime);
            } else {
                ParseErroeLogger.error(message.getRaw());
            }
        } catch (Exception e) {
            ParseErroeLogger.error(message.getRaw(), e);
        }


    }
}
