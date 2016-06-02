package com.ptb.uranus.server.listener;

import com.alibaba.fastjson.JSON;
import com.ptb.gaia.bus.Bus;
import com.ptb.gaia.bus.message.Message;
import com.ptb.gaia.bus.message.MessageListener;
import com.ptb.uranus.common.entity.CollectCondition;
import com.ptb.uranus.common.entity.CollectType;
import com.ptb.uranus.server.handle.*;
import com.ptb.uranus.server.send.Sender;
import org.apache.commons.configuration.ConfigurationException;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by eric on 16/4/23.
 */
public class CollectCommandListenter implements MessageListener {
    final static int CollectCommandCode = 0x10000000;
    private Sender sender;
    Map<CollectType, CollectHandler> commandHandlerMap = new HashMap<CollectType, CollectHandler>();

    public CollectCommandListenter(Sender sender) throws ConfigurationException {
        this.sender = sender;
/*        commandHandlerMap.put(CollectType.C_A_A_D, new CommonArticleDynamicHandle(sender));
        commandHandlerMap.put(CollectType.C_A_A_S, new CommonArticleStaticHandle(sender));*/
        commandHandlerMap.put(CollectType.C_WB_A_D, new WeiboArticleDynamicHandle(sender));
        commandHandlerMap.put(CollectType.C_WB_A_S, new WeiboArticleStaticHandle(sender));
        commandHandlerMap.put(CollectType.C_WB_M_D, new WeiboMediaDynamicHandle(sender));
        commandHandlerMap.put(CollectType.C_WB_M_S, new WeiboMediaStaticHandle(sender));
        commandHandlerMap.put(CollectType.C_WX_A_D, new WeixinArticleDynamicHandle(sender));
        commandHandlerMap.put(CollectType.C_WX_A_S, new WeixinArticleStaticHandle(sender));
        commandHandlerMap.put(CollectType.C_WX_M_D, new WeixinMediaDynamicHandle(sender));
        commandHandlerMap.put(CollectType.C_WX_M_S, new WeixinMediaStaticHandle(sender));
/*        commandHandlerMap.put(CollectType.C_A_A_N, new CommonNewArticlesHandle(sender));*/
        commandHandlerMap.put(CollectType.C_WX_A_N, new WeixinNewArticlesHandle(sender));
        commandHandlerMap.put(CollectType.C_WB_A_N, new WeiboNewArticlesHandle(sender));
    }

    public void receive(Bus bus, Message message) {
        Message<CollectCondition> msg = message;
        CollectHandler collectHandler = commandHandlerMap.get(msg.getBody().getCollectType());
        if (collectHandler != null) {
            collectHandler.handle(bus, msg);
        }
    }

    public boolean match(Message message) {
        if ((message.getType() & CollectCommandCode) == CollectCommandCode) {
            message.setBody(JSON.parseObject(JSON.toJSONString(message.getBody()), CollectCondition.class));
            return true;
        }
        return false;
    }
}
