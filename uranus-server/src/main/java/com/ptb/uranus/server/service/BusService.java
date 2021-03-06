package com.ptb.uranus.server.service;

import com.ptb.gaia.bus.Bus;
import com.ptb.gaia.bus.kafka.KafkaBus;
import com.ptb.gaia.bus.message.Message;
import com.ptb.gaia.bus.message.MessageListener;
import com.ptb.uranus.server.listener.CollectCommandListenter;
import com.ptb.uranus.server.send.BusSender;
import com.ptb.uranus.server.send.Sender;
import org.apache.commons.configuration.ConfigurationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;


/**
 * Created by eric on 16/1/11.
 */

@Component
public class BusService {
    static Logger logger = LoggerFactory.getLogger(BusService.class);

    @Autowired
    Bus bus;

    @Autowired
    BusConfig busConfig;

    public BusService() {
    }

    @PostConstruct
    public void begin() throws ConfigurationException {
        logger.debug("begin ...");
        Sender sender = new BusSender(bus);
        bus.addRecvMessageListener(new CollectCommandListenter(sender));
        String[] topics = busConfig.getListenTopic().split(",");
        for (String topic : topics) {
            bus.addConsumerTopic(topic);
        }
        if (bus instanceof KafkaBus) {
            ((KafkaBus) bus).setConsumerThread(busConfig.getConsumerNum());
        }
        bus.start(false, busConfig.workerNum);
        return;
    }

    public void addListener(MessageListener messageListener) {
        bus.addRecvMessageListener(messageListener);
    }

    public void send(String dest, byte[] message) {
        bus.send(dest, message);
    }

    public void send(Message message) {
        bus.send(message);
    }

    @PreDestroy
    public void shutdown() {
        bus.showdown();
    }

}
