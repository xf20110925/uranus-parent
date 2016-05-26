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
import com.ptb.utils.string.RegexUtils;
import org.apache.commons.configuration.ConfigurationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
            Optional<String> identify;
            try {
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
            } else {
                ParseErroeLogger.error(JSON.toJSONString(message));
            }
        } catch (Exception e) {
            ParseErroeLogger.error(String.valueOf(message.getRaw()), e);
        }
    }

}
