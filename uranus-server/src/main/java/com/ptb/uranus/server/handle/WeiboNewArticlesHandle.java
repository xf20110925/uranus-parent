package com.ptb.uranus.server.handle;

import com.alibaba.fastjson.JSON;
import com.ptb.gaia.bus.Bus;
import com.ptb.gaia.bus.message.Message;
import com.ptb.uranus.common.entity.CollectCondition;
import com.ptb.uranus.schedule.service.WeiboScheduleService;
import com.ptb.uranus.server.send.Sender;
import com.ptb.uranus.server.send.entity.article.WeiboArticleStatic;
import com.ptb.uranus.server.send.entity.convert.SendObjectConvertUtil;
import com.ptb.uranus.spider.weibo.WeiboSpider;
import com.ptb.uranus.spider.weibo.bean.WeiboAccount;
import com.ptb.uranus.spider.weibo.bean.WeiboArticle;
import com.ptb.utils.log.LogUtils;
import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Optional;

/**
 * Created by eric on 16/4/25.
 */
public class WeiboNewArticlesHandle implements com.ptb.uranus.server.handle.CollectHandler {
    private static Logger ParseErroeLogger = LoggerFactory.getLogger("msg.fail");
    private static Logger logger = LoggerFactory.getLogger(WeiboNewArticlesHandle.class);
    WeiboSpider weiboSpider = new WeiboSpider();
    WeiboScheduleService wbScheduleService = new WeiboScheduleService();
    Sender sender;

    public WeiboNewArticlesHandle(Sender sender) throws ConfigurationException {
        this.sender = sender;
    }

    public void handle(Bus bus, Message<CollectCondition> message) {
        try {
            String[] conditon = message.getBody().getConditon().split(":::");
            Optional<ImmutablePair<Long, List<WeiboArticle>>> recentArticlesPair;

            String id = conditon[0];
            long lastTime = Long.parseLong(conditon[1]);
            String containerID;

            if (id.length() <= 12) {
                containerID = wbScheduleService.getContainerIDByWeiboID(id);
            } else {
                containerID = id;
            }

            if (StringUtils.isBlank(containerID) || containerID.length() < 10) {
                Optional<WeiboAccount> account = weiboSpider.getWeiboAccountByWeiboID(id);
                if (account.isPresent() && account.get().getContainerID() != null) {
                    wbScheduleService.setContainerIDByWeiboID(account.get().getWeiboID(), account.get().getContainerID());
                    containerID = account.get().getContainerID();
                }
            } else {

            }

            recentArticlesPair = weiboSpider.getRecentArticlesByContainerID(containerID, lastTime);

            if (recentArticlesPair.isPresent()) {
                List<WeiboArticle> recentArticles = recentArticlesPair.get().getRight();
                logger.info("wb new article: [%s]", JSON.toJSONString(recentArticles));
                long lastestTime = recentArticlesPair.get().getLeft().longValue();

                for (int i = 0; i < recentArticles.size(); i++) {
                    if(i == 0) {
                        String weiboID = recentArticles.get(i).getMediaId();
                        if (!StringUtils.isBlank(weiboID)) {
                            wbScheduleService.updateMediaCondition(message.getBody(), weiboID, lastestTime);
                        }
                    }
                    wbScheduleService.addArticleDynamicScheduler(recentArticles.get(i).getPostTime(), recentArticles.get(i).getArticleUrl());
                    WeiboArticleStatic weiboArticleStatic = SendObjectConvertUtil.weiboArticleStaticConvert(recentArticles.get(i));
                    sender.sendArticleStatic(weiboArticleStatic);
                }
            } else {
                ParseErroeLogger.error(String.valueOf(message.getRaw()));
                LogUtils.log("uranus-server", "get-weibo-recent-article-by-container", "failed", String.valueOf(message.getRaw()));

            }
        } catch (Exception e) {
            ParseErroeLogger.error(String.valueOf(message.getRaw()), e);
        }
    }
}
