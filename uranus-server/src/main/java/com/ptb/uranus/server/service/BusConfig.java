package com.ptb.uranus.server.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * Created by eric on 16/2/17.
 */
@Component
public class BusConfig {
    @Value("${listenTopics}")
    String listenTopic;

    @Value("${spider.worker.num}")
    int workerNum;

    @Value("${kafka.consumer.num}")
    int consumerNum;


    public String getListenTopic() {
        return listenTopic;
    }

    public void setListenTopic(String listenTopic) {
        this.listenTopic = listenTopic;
    }

    public int getWorkerNum() {
        return workerNum;
    }

    public void setWorkerNum(int workerNum) {
        this.workerNum = workerNum;
    }

    public int getConsumerNum() {
        return consumerNum;
    }

    public void setConsumerNum(int consumerNum) {
        this.consumerNum = consumerNum;
    }
}
