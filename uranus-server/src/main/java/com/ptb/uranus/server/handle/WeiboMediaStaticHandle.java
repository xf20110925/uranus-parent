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
import org.apache.commons.configuration.ConfigurationException;
import org.apache.log4j.Logger;

import java.util.Optional;

/**
 * Created by eric on 16/4/23.
 */
public class WeiboMediaStaticHandle implements CollectHandler {
    private static Logger logger = Logger.getLogger("msg.wb.fail");
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
                weiboScheduleService.addDetectNewArticlesSchedule(wbAccount.get().getContainerID(),wbAccount.get().getWeiboID());
                weiboScheduleService.addWeiboMediaDynamicSchedule(message.getBody().getConditon());
            } else {
                logger.error(message.getRaw());
            }
        } catch (Exception e) {
            logger.error(message.getRaw(), e);
        }
    }

}
