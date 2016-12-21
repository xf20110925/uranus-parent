package com.ptb.uranus.server.handle;

import com.alibaba.fastjson.JSON;
import com.ptb.gaia.bus.Bus;
import com.ptb.gaia.bus.message.Message;
import com.ptb.uranus.common.entity.CollectCondition;
import com.ptb.uranus.schedule.service.WeiboScheduleService;
import com.ptb.uranus.server.send.Sender;
import com.ptb.uranus.server.send.entity.article.BasicArticleDynamic;
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
            LogUtils.logInfo("uranus-server", "C_WB_A_N recv", LogUtils.ActionResult.success, "");
            String[] conditon = message.getBody().getConditon().split(":::");
            Optional<ImmutablePair<Long, List<WeiboArticle>>> recentArticlesPair;

            String id = conditon[0];
            long lastTime = Long.parseLong(conditon[1]);
            String containerID = "";
            String weiboID = null;

            if (id.length() <= 10) {
                weiboID = id;
            } else {
                containerID = id;
            }

            Optional<WeiboAccount> account = weiboSpider.getWeiboAccountByWeiboID(id);
            if (account.isPresent() && account.get().getContainerID() != null) {
              containerID = account.get().getContainerID();
            }
            recentArticlesPair = weiboSpider.getRecentArticlesByContainerID(weiboID, containerID, lastTime);

            LogUtils.logInfo("uranus-server", "C_WB_A_N get", LogUtils.ActionResult.success, "");

            if (recentArticlesPair.isPresent()) {
                List<WeiboArticle> recentArticles = recentArticlesPair.get().getRight();
                long lastestTime = recentArticlesPair.get().getLeft().longValue();
                if(weiboID == null){
                    weiboID = recentArticles.get(0).getMediaId();
                }

                if (!StringUtils.isBlank(weiboID)) {
                    wbScheduleService.updateMediaCondition(message.getBody(), weiboID, lastestTime);
                }

                for (int i = 0; i < recentArticles.size(); i++) {
                    logger.info("wb new article: {}", JSON.toJSONString(recentArticles.get(i)));
                    LogUtils.logInfo("uranus-server", "C_WB_A_N add", LogUtils.ActionResult.success, "");
                    WeiboArticleStatic weiboArticleStatic = SendObjectConvertUtil.weiboArticleStaticConvert(recentArticles.get(i));
                    sender.sendArticleStatic(weiboArticleStatic);
                    BasicArticleDynamic wbArticleDynamic = SendObjectConvertUtil.weiboArticleDynamicConvert(recentArticles.get(i));
                    sender.sendArticleDynamic(wbArticleDynamic);
                    wbScheduleService.addArticleDynamicScheduler(recentArticles.get(i).getPostTime(), recentArticles.get(i).getArticleUrl());
                    LogUtils.logInfo("uranus-server", "C_WB_A_N send", LogUtils.ActionResult.success, "");
                }
            } else {
                ParseErroeLogger.error(String.valueOf(message.getRaw()));
                LogUtils.log("uranus-server", "C_WB_A_N error", LogUtils.ActionResult.failed, new String(message.getRaw()));
            }
        } catch (Exception e) {
            ParseErroeLogger.error(String.valueOf(message.getRaw()), e);
            LogUtils.log("uranus-server", "C_WB_A_N exception", LogUtils.ActionResult.failed, new String(message.getRaw()+e.getLocalizedMessage()));
        }
    }
}
