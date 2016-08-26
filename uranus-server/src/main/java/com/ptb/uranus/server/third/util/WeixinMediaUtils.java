package com.ptb.uranus.server.third.util;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.UpdateOptions;
import com.mongodb.client.result.UpdateResult;
import com.ptb.uranus.schedule.utils.MongoUtils;
import com.ptb.uranus.server.third.entity.BayouWXMedia;
import org.bson.Document;

/**
 * Created by xuefeng on 2016/6/3.
 */
public class WeixinMediaUtils {
    public static final String DB_NAME = "uranus";
    public static final String COLL_NAME = "weixinMedia";
    static MongoCollection<Document> coll = MongoUtils.instance.getDatabase(DB_NAME).getCollection(COLL_NAME);

//    static AtomicLong counter = new AtomicLong(0);
    public static UpdateResult updateWeixinMedia(String biz, BayouWXMedia media) {
        UpdateResult updateResult = null;
        try {
            Document doc = Document.parse(media.toString());
            UpdateOptions upsert = new UpdateOptions().upsert(true);
            updateResult = coll.updateOne(Filters.eq("bid", biz), new Document("$set", doc), upsert);
//            counter.addAndGet(updateResult.getModifiedCount());
        } catch (Exception e) {
            e.printStackTrace();
        }

//        System.out.print(String.format("update wxMedia num[%d] ", counter.get()));
        return updateResult;
    }
}