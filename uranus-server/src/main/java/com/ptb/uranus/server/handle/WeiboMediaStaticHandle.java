package com.ptb.uranus.server.handle;

import com.ptb.gaia.bus.Bus;
import com.ptb.gaia.bus.message.Message;
import com.ptb.uranus.common.entity.CollectCondition;
import com.ptb.uranus.server.send.Sender;
import com.ptb.uranus.server.send.entity.convert.SendObjectConvertUtil;
import com.ptb.uranus.server.send.entity.media.WeiboMediaStatic;
import com.ptb.uranus.schedule.service.WeiboScheduleService;
import com.ptb.uranus.spider.weibo.WeiboSpider;
import com.ptb.uranus.spider.weibo.bean.WeiboAccount;
import com.ptb.utils.log.LogUtils;
import org.apache.commons.configuration.ConfigurationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Optional;

/**
 * Created by eric on 16/4/23.
 */
public class WeiboMediaStaticHandle implements CollectHandler {
    private static Logger logger = LoggerFactory.getLogger("msg.wb.fail");
    Sender sender;
    WeiboSpider weiboSpider = new WeiboSpider();
    WeiboScheduleService weiboScheduleService;

    public WeiboMediaStaticHandle(Sender sender) throws ConfigurationException {
        this.sender = sender;
        weiboScheduleService = new WeiboScheduleService();
    }

    public void handle(Bus bus, Message<CollectCondition> message) {
        try {
            Optional<WeiboAccount> wbAccount;
            if(message.getBody().getConditon().contains("http://") || message.getBody().getConditon().contains("https://")) {
                wbAccount = weiboSpider.getWeiboAccountByArticleUrl(message.getBody().getConditon());
            }else{
                wbAccount = weiboSpider.getWeiboAccountByWeiboID(message.getBody().getConditon());
            }

            if (wbAccount.isPresent()) {
                WeiboMediaStatic weiboMediaStatic = SendObjectConvertUtil.weiboMediaStaticConvert(wbAccount.get());

                sender.sendMediaStatic(weiboMediaStatic);
                weiboScheduleService.setContainerIDByWeiboID(wbAccount.get().getWeiboID(),wbAccount.get().getContainerID());

                weiboScheduleService.addDetectNewArticlesSchedule(wbAccount.get().getWeiboID());
                weiboScheduleService.addWeiboMediaDynamicSchedule(message.getBody().getConditon());
            } else {
                logger.error(String.valueOf(message.getRaw()));
                LogUtils.log("uranus-server", "get-weibo-account-by-articleurl-or-weiboid", "failed", String.valueOf(message.getRaw()));
            }
        } catch (Exception e) {
            logger.error(String.valueOf(message.getRaw()), e);
        }
    }

}
