package com.ptb.uranus.server.handle;

import com.alibaba.fastjson.JSON;
import com.ptb.gaia.bus.Bus;
import com.ptb.gaia.bus.message.Message;
import com.ptb.uranus.common.entity.CollectCondition;
import com.ptb.uranus.schedule.service.WeiboScheduleService;
import com.ptb.uranus.server.send.Sender;
import com.ptb.uranus.spider.weibo.WeiboSpider;
import com.ptb.uranus.spider.weibo.bean.WeiboAccount;
import com.ptb.utils.string.RegexUtils;
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

    public WeiboNewArticlesHandle(Sender sender) throws ConfigurationException {
    }

    public void handle(Bus bus, Message<CollectCondition> message) {
        try {
            String[] conditon = message.getBody().getConditon().split(":::");
            Optional<ImmutablePair<Long, List<String>>> recentArticlesPair;

            String id = conditon[0];
            long lastTime = Long.parseLong(conditon[1]);
            String containerID;
            String weiboID = null;
            if (id.length() <= 10) {
                containerID = wbScheduleService.getContainerIDByWeiboID(id);
                weiboID = id;
            } else {
                containerID = id;
            }

            if (StringUtils.isBlank(containerID)) {
                recentArticlesPair = weiboSpider.getRecentArticlesByWeiboID(id, lastTime);
                Optional<WeiboAccount> account = weiboSpider.getWeiboAccountByWeiboID(id);
                if (account.isPresent() && account.get().getContainerID() != null) {
                    wbScheduleService.setContainerIDByWeiboID(account.get().getWeiboID(), account.get().getContainerID());
                }
            } else {
                recentArticlesPair = weiboSpider.getRecentArticlesByContainerID(id, lastTime);
            }

            if (recentArticlesPair.isPresent()) {
                List<String> recentArticles = recentArticlesPair.get().getRight();
                logger.info("wb new article: [%s]", JSON.toJSONString(recentArticles));
                wbScheduleService.addArticleStaticSchedulers(recentArticles);
                long lastestTime = recentArticlesPair.get().getLeft().longValue();

                if(recentArticles.size() > 0 && StringUtils.isBlank(weiboID)){
                    weiboID = RegexUtils.sub("http://m.weibo.cn/([\\d]*)/.*", recentArticles.get(0), 0);
                }
                if (!StringUtils.isBlank(weiboID)) {
                    wbScheduleService.updateMediaCondition(message.getBody(), weiboID, lastestTime);
                }
            } else {
                ParseErroeLogger.error(String.valueOf(message.getRaw()));
            }
        } catch (Exception e) {
            ParseErroeLogger.error(String.valueOf(message.getRaw()), e);
        }
    }
}
