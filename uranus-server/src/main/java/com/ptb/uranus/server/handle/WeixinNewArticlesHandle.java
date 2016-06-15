package com.ptb.uranus.server.handle;

import com.alibaba.fastjson.JSON;
import com.ptb.gaia.bus.Bus;
import com.ptb.gaia.bus.message.Message;
import com.ptb.uranus.common.entity.CollectCondition;
import com.ptb.uranus.schedule.service.WeixinScheduleService;
import com.ptb.uranus.server.send.Sender;
import com.ptb.uranus.spider.weixin.WeixinSpider;
import com.ptb.utils.log.LogUtils;
import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Optional;

/**
 * Created by eric on 16/4/25.
 */
public class WeixinNewArticlesHandle implements CollectHandler {
    private static Logger ParseErroeLogger = LoggerFactory.getLogger("msg.fail");
    private static Logger logger = LoggerFactory.getLogger(WeixinNewArticlesHandle.class);

    WeixinSpider weixinSpider = new WeixinSpider();
    WeixinScheduleService wxSchedule;

    public WeixinNewArticlesHandle(Sender sender) throws ConfigurationException {
        wxSchedule = new WeixinScheduleService();
    }

    public void handle(Bus bus, Message<CollectCondition> message) {
        try {
            LogUtils.logInfo("uranus-server", "C_WX_A_N recv", LogUtils.ActionResult.success, "");
            String[] conditon = message.getBody().getConditon().split(":::");
            String biz = conditon[0];
            long lastTime = Long.parseLong(conditon[1]);
            Optional<ImmutablePair<Long, List<String>>> recentArticles = weixinSpider.getRecentArticlesByBiz(biz, lastTime);
            if (recentArticles.isPresent()) {
                logger.info("wx new article: [%s]", JSON.toJSONString(recentArticles));
                wxSchedule.addArticleStaticSchedulers(recentArticles.get().getRight());
                wxSchedule.updateWeixinMediaCondition(biz, recentArticles.get().getLeft());
                LogUtils.logInfo("uranus-server", "C_WX_A_N add", LogUtils.ActionResult.success, "");
            } else {
                ParseErroeLogger.error(new String(message.getRaw()));
                LogUtils.log("uranus-server", "C_WX_A_N error", LogUtils.ActionResult.failed, new String(message.getRaw()));
            }
        } catch (Exception e) {
            ParseErroeLogger.error(new String(message.getRaw()), e);
            LogUtils.log("uranus-server", "C_WX_A_N exception", LogUtils.ActionResult.failed, new String(message.getRaw()+e.getLocalizedMessage()));
        }
    }
}
