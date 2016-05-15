package com.ptb.uranus.server.handle;

import com.ptb.gaia.bus.Bus;
import com.ptb.gaia.bus.message.Message;
import com.ptb.uranus.common.entity.CollectCondition;

/**
 * Created by eric on 16/4/23.
 */
public interface CollectHandler {

    void handle(Bus bus, Message<CollectCondition> message);
}
