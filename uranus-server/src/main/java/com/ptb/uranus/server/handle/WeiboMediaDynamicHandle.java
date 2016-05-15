package com.ptb.uranus.server.handle;

import com.ptb.gaia.bus.Bus;
import com.ptb.gaia.bus.message.Message;
import com.ptb.uranus.common.entity.CollectCondition;
import com.ptb.uranus.server.send.Sender;
import com.ptb.uranus.server.send.entity.convert.SendObjectConvertUtil;
import com.ptb.uranus.server.send.entity.media.BasicMediaDynamic;
import com.ptb.uranus.spider.weibo.WeiboSpider;
import com.ptb.uranus.spider.weibo.bean.WeiboAccount;
import org.apache.log4j.Logger;

import java.util.Optional;

/**
 * Created by eric on 16/4/23.
 */
public class WeiboMediaDynamicHandle implements CollectHandler {
    private static Logger logger = Logger.getLogger("msg.wb.fail");
    WeiboSpider weiboSpider = new WeiboSpider();
    Sender sender;

    public WeiboMediaDynamicHandle(Sender sender) {
        this.sender = sender;
    }

    public void handle(Bus bus, Message<CollectCondition> message) {
        try {
            Optional<WeiboAccount> wbAccount;
            if (message.getBody().getConditon().contains("http://") || message.getBody().getConditon().contains("https://")) {
                wbAccount = weiboSpider.getWeiboAccountByArticleUrl(message.getBody().getConditon());
            } else {
                wbAccount = weiboSpider.getWeiboAccountByWeiboID(message.getBody().getConditon());
            }
            if (wbAccount.isPresent()) {
                BasicMediaDynamic weiboMediaDynamic = SendObjectConvertUtil.weiboMediaDynamicConvert(wbAccount.get());
                sender.sendMediaDynamic(weiboMediaDynamic);
            }
        } catch (Exception e) {

        }
    }
}
