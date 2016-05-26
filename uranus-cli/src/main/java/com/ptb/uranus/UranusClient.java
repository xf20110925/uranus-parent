package com.ptb.uranus;

import com.ptb.gaia.bus.Bus;
import com.ptb.gaia.bus.kafka.KafkaBus;
import com.ptb.gaia.bus.message.Message;
import com.ptb.uranus.common.entity.CollectCondition;
import com.ptb.uranus.common.entity.CollectType;

import java.util.List;

/**
 * The type Uranus client.
 */
public class UranusClient {


    /**
     * The Uranus client.
     */
    static UranusClient uranusClient;


    private final String recvTopic;
    /**
     * The Bus.
     */
    Bus bus;

    /**
     * Gets bus.
     *
     * @return the bus
     */
    public Bus getBus() {
        return bus;
    }

    public UranusClient(String recvResultTopic, int recvThreadNum) {
        this.recvTopic = recvResultTopic;
        bus = new KafkaBus(recvResultTopic);
        bus.start(false, recvThreadNum);
    }

    /**
     * Close. 关闭client
     */
    public void close() {
        bus.showdown();
    }


    public boolean sendCrawleCommand(CollectCondition collectCondition) {
        bus.send(new Message(recvTopic, getSendTopic(collectCondition.getCollectType()), collectCondition.getCollectType().getCode(), "1.0.0", collectCondition));
        return true;
    }

    public boolean sendCrawleCommand(List<CollectCondition> conditionList) {
        conditionList.forEach(condition -> sendCrawleCommand(condition));
        return true;
    }

    public long getBusBlockNum(CollectType collectType) throws Exception {
        return bus.getNoConsumeSize(getSendTopic(collectType));
    }

    private String getSendTopic(CollectType collectType) {
        switch (collectType) {
            case C_WB_A_D:
            case C_WB_A_S:
            case C_WB_M_S:
            case C_WB_M_D:
            case C_WB_A_N:
                return "uranus-server_v2_wb";
            case C_WX_A_S:
                return "uranus-server_v2_wx_s";
            case C_WX_A_D:
            case C_WX_M_D:
            case C_WX_A_N:
            case C_WX_M_S:
                return "uranus-server_v2_wx_d";
            case C_A_A_D:
            case C_A_A_S:
                return "uranus-server_v2";
            default:
                return "uranus-server_v2";
        }
    }

}
