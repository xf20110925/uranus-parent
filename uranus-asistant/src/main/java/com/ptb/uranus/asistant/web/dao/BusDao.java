package com.ptb.uranus.asistant.web.dao;

import com.ptb.gaia.bus.Bus;
import com.ptb.gaia.bus.message.Message;

/**
 * Created by eric on 16/2/22.
 */
public class BusDao {
    static String asistantTopic = "uranus-asistant";

    Bus bus;

    public BusDao() {

    }

    public void sendMessage(String dest, int messageType, Object message) {
        if (bus.getRecvTopics().size() == 0) {
            bus.addConsumerTopic(asistantTopic);
            bus.start(false, 1);
        }
        bus.send(new Message(asistantTopic, dest, messageType, "1.0.0", message));
    }

    public Bus getBus() {
        return bus;
    }

    public void setBus(Bus bus) {
        this.bus = bus;
    }
}
