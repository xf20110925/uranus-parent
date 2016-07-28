package com.ptb.uranus.schedule.dao;

import com.alibaba.fastjson.JSON;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Sorts;
import com.mongodb.client.result.DeleteResult;
import com.ptb.uranus.common.entity.CollectType;
import com.ptb.uranus.schedule.model.ScheduleObject;
import com.ptb.uranus.schedule.utils.MongoUtils;
import org.apache.commons.configuration.Configuration;
import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;
import org.bson.Document;
import org.bson.conversions.Bson;

import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Created by eric on 16/4/21.
 */
public class MongoSchedulerDao implements SchedulerDao {
    String dbName;
    String collName = "schedule";
    String finishCollName = "schedule_history";

    public MongoSchedulerDao() throws ConfigurationException {
        Configuration conf = new PropertiesConfiguration("ptb.properties");
        dbName = conf.getString("uranus.scheduler.mongo.db","uranus");
    }

    private MongoCollection<Document> coll() {
        return MongoUtils.instance.getCollection(dbName, collName);
    }

    private MongoCollection<Document> historyColl() {
        return MongoUtils.instance.getCollection(dbName, finishCollName);
    }

    private Document convertScheduleObjToDoc(ScheduleObject scheduleObject) {
        Document doc = Document.parse(scheduleObject.toString());
        Object id = doc.put("_id", scheduleObject.getId());
        doc.remove("id");
        return doc;
    }

    private ScheduleObject convertDocToScheduleObj(Document document) {
        document.put("id", document.get("_id"));
        document.remove("_id");
        return JSON.parseObject(JSON.toJSONString(document), ScheduleObject.class);
    }

    @Override
    public boolean addCollScheduler(ScheduleObject scheduleObject) {
        coll().insertOne(convertScheduleObjToDoc(scheduleObject));
        return true;
    }

    public boolean addCollSchedulers(List<ScheduleObject> scheduleObjects) {
        if(scheduleObjects.size() > 0) {
            List<Document> collects = scheduleObjects.stream().map(s -> convertScheduleObjToDoc(s)).collect(Collectors.toList());
            coll().insertMany(collects);
        }
        return true;

    }

    @Override
    public boolean delCollScheduler(String scheduleID) {
        DeleteResult ret = coll().deleteOne(Filters.eq("_id", scheduleID));
        return ret.getDeletedCount() != 0;
    }

    @Override
    public boolean updateScheduler(ScheduleObject scheduleObject) {
        if (scheduleObject.isDone() == true) {
            Document document = convertScheduleObjToDoc(scheduleObject);
            historyColl().insertOne(document.append("addTime", new Date()));
            return delCollScheduler(scheduleObject.getId());
        } else {
            return coll().updateOne(Filters.eq("_id", scheduleObject.getId()), new Document("$set", convertScheduleObjToDoc(scheduleObject))).getModifiedCount() != 0;
        }
    }

    @Override
    public ScheduleObject getScheduler(String scheduleID) {
        return JSON.parseObject(JSON.toJSONString(coll().find(Filters.eq("_id", scheduleID)).first()), ScheduleObject.class);
    }

    @Override
    public List<ScheduleObject> getNeedScheduleObjects(int count) {
        Bson and = Filters.and(Filters.eq("done", false), Filters.lt("nTime", System.currentTimeMillis()));

        FindIterable<Document> docs = coll().find(and);
        if (count > 0) {
            docs = docs.limit(count);
        }
        List<ScheduleObject> scheduleObjects = new LinkedList<>();


        MongoCursor<Document> iterator = docs.iterator();
        while (iterator.hasNext()) {
            Document s = (Document) iterator.next();
            scheduleObjects.add(convertDocToScheduleObj(s));
        }
        return scheduleObjects;
    }

    @Override
    public List<ScheduleObject> getNeedScheduleObjectsByCollectType(CollectType collectType, int count) {
        Bson and = Filters.and(Filters.eq("obj.collectType", collectType.toString()),Filters.lt("nTime", System.currentTimeMillis()),Filters.eq("done", false));
        Bson sortBson = Sorts.ascending("priority", "nTime");
        FindIterable<Document> docs = coll().find(and).sort(sortBson);

        if (count > 0) {
            docs = docs.limit(count);
        }
        List<ScheduleObject> scheduleObjects = new LinkedList<>();

        MongoCursor<Document> iterator = docs.iterator();
        while (iterator.hasNext()) {
            Document s = (Document) iterator.next();
            scheduleObjects.add(convertDocToScheduleObj(s));
        }
        return scheduleObjects;
    }

    @Override
    public boolean delAllScheduler() {
        MongoUtils.instance.dropCollection(dbName, collName);
        return true;
    }


    @Override
    public boolean delSchedulerByField(String field,String fieldValue) {
        DeleteResult deleteResult = coll().deleteMany(Filters.eq(field, fieldValue));
        return deleteResult.getDeletedCount() > 0;
    }

    @Override
    public Optional<ScheduleObject> getSchedulerByField(String field, Object value) {
        Document first = coll().find(Filters.eq(field, value)).first();
        if (first == null) {
            return Optional.empty();
        } else {
            return Optional.of(convertDocToScheduleObj(first));
        }
    }

}
