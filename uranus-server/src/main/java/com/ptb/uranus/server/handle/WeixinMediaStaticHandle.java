package com.ptb.uranus.server.handle;

import com.alibaba.fastjson.JSON;
import com.ptb.gaia.bus.Bus;
import com.ptb.gaia.bus.message.Message;
import com.ptb.uranus.common.entity.CollectCondition;
import com.ptb.uranus.schedule.service.WeixinScheduleService;
import com.ptb.uranus.server.send.Sender;
import com.ptb.uranus.server.send.entity.convert.SendObjectConvertUtil;
import com.ptb.uranus.server.send.entity.media.WeixinMediaStatic;
import com.ptb.uranus.spider.weixin.WeixinSpider;
import com.ptb.uranus.spider.weixin.bean.WxAccount;
import com.ptb.utils.log.LogUtils;
import com.ptb.utils.string.RegexUtils;
import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Optional;

/**
 * Created by eric on 16/4/23.
 */
public class WeixinMediaStaticHandle implements CollectHandler {
    private static Logger ParseErroeLogger = LoggerFactory.getLogger("msg.fail");
    private final Sender sender;

    WeixinScheduleService wxSchedule;
    WeixinSpider weixinSpider = new WeixinSpider();

    public WeixinMediaStaticHandle(Sender sender) throws ConfigurationException {
        this.sender = sender;
        wxSchedule = new WeixinScheduleService();

    }

    public void handle(Bus bus, Message<CollectCondition> message) {
        try {
            LogUtils.logInfo("uranus-server", "C_WX_M_S recv", LogUtils.ActionResult.success, "");
            Optional<String> identify;
            try {
                if (!message.getBody().getConditon().matches("(?:http://|https://).*")) {
                    Optional<ImmutablePair<Long, List<String>>> recentArticlesByBiz = weixinSpider.getRecentArticlesByBiz(message.getBody().getConditon(), -1);
                    if (recentArticlesByBiz.isPresent()) {
                        message.getBody().setConditon(recentArticlesByBiz.get().getRight().get(0));
                    }
                }
                identify = weixinSpider.getMediaIdentifyByArticleUrl(message.getBody().getConditon());
            } catch (Exception e) {
                identify = Optional.empty();
            }

            Optional<WxAccount> wxAccount = weixinSpider.getWeixinAccountByArticleUrl(message.getBody().getConditon());
            if (wxAccount.isPresent()) {
                WeixinMediaStatic weixinMediaStatic = SendObjectConvertUtil.weixinMediaStaticConvert(wxAccount.get(), identify);
                sender.sendMediaStatic(weixinMediaStatic);
                String biz = RegexUtils.sub(".*__biz=([^#&]*).*", message.getBody().getConditon(), 0);
                wxSchedule.addWeixinDetectNewArticlesSchedule(biz);
                LogUtils.logInfo("uranus-server", "C_WX_M_S send", LogUtils.ActionResult.success, "");
            } else {
                ParseErroeLogger.error(new String(message.getRaw()));
                LogUtils.log("uranus-server", "C_WX_M_S error", LogUtils.ActionResult.failed, new String(message.getRaw()));
            }
        } catch (Exception e) {
            ParseErroeLogger.error(new String(message.getRaw()), e);
            LogUtils.log("uranus-server", "C_WX_M_S exception", LogUtils.ActionResult.failed, new String(message.getRaw()+e.getLocalizedMessage()));
        }
    }
}
