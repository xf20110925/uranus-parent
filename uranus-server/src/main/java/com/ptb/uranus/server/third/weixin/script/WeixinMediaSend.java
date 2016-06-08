package com.ptb.uranus.server.third.weixin.script;

import com.alibaba.fastjson.JSON;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.ptb.gaia.bus.Bus;
import com.ptb.gaia.bus.kafka.KafkaBus;
import com.ptb.uranus.schedule.utils.MongoUtils;
import com.ptb.uranus.server.send.BusSender;
import com.ptb.uranus.server.send.Sender;
import com.ptb.uranus.server.send.entity.media.WeixinMediaStatic;
import com.ptb.uranus.server.third.weixin.entity.BayouWXMedia;
import com.ptb.uranus.server.third.weixin.util.ConvertUtils;
import org.bson.Document;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

/**
 * Created by xuefeng on 2016/6/7.
 */
public class WeixinMediaSend {
    public static final String DB_NAME = "uranus";
    public static final String COLL_NAME = "weixinMedia";
    Sender sender;

    public WeixinMediaSend(Sender sender) {
        this.sender = sender;
    }

    MongoCollection<Document> coll = MongoUtils.instance.getCollection(DB_NAME, COLL_NAME);

    private FindIterable<Document> findAll(int start, int end) {
        FindIterable<Document> documents = coll.find().skip(start).limit(end - start);
        return documents;
    }

    private long statCount() {
        long count = coll.count();
        return count;
    }

    AtomicLong counter = new AtomicLong(0);

    public void run() {
        long size = statCount();
        int start = 0;
        int end = 99;

        while (start < size) {
            FindIterable<Document> documents = findAll(start, end);
            List<BayouWXMedia> medias = new ArrayList<>();
            for (Document doc : documents) {
                BayouWXMedia media = JSON.parseObject(doc.toJson(), BayouWXMedia.class);
                medias.add(media);
            }
            medias.parallelStream().forEach(bayouWXMedia -> {
                WeixinMediaStatic weixinMediaStatic = ConvertUtils.convertWXMedia(bayouWXMedia);
                sender.sendMediaStatic(weixinMediaStatic);
                counter.addAndGet(1);
            });
            start = end;
            end += 100;
            System.out.println("start:" + start + "\tend:" + end );
            System.out.println("counter------->" + counter);
        }
    }

    public static void main(String[] args) {
        Bus bus = new KafkaBus();
        bus.start(false, 3);
        new WeixinMediaSend(new BusSender(bus)).run();
    }
}