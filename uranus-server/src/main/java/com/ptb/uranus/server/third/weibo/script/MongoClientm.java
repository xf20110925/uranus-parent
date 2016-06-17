package com.ptb.uranus.server.third.weibo.script;

import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.mongodb.MongoClient;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.UpdateOptions;
import org.apache.commons.configuration.PropertiesConfiguration;
import org.bson.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

/**
 * Created by watson zhang on 16/5/18.
 */
public class MongoClientm {
    MongoCollection dc;
    private static Logger logger = LoggerFactory.getLogger(MongoClientm.class);
    UpdateOptions up = new UpdateOptions().upsert(true);
    MongoClient mongoClient;
    private String mongoHost;
    private int mongoPort;
    private String mongoDataBase;
    private String mongoColl;

    public MongoClientm(String host, int port,String dbName, String coll) {
        this.mongoHost = host;
        this.mongoPort = port;
        this.mongoDataBase = dbName;
        this.mongoColl = coll;
        mongoClient = new MongoClient(mongoHost, mongoPort);
        dc = mongoClient.getDatabase(mongoDataBase).getCollection(mongoColl);
    }

    public void restart(){
        mongoClient.close();
        mongoClient = null;
        dc = null;
        mongoClient = new MongoClient(mongoHost, mongoPort);
        dc = mongoClient.getDatabase(mongoDataBase).getCollection(mongoColl);
    }

    public void findAll(){
        FindIterable cursor = dc.find();
        MongoCursor iter = cursor.iterator();
        while (iter.hasNext()){
            System.out.println(iter.next());
        }
    }

    public void updateObj(HashMap<String, String> data){
        Document doc = new Document();
        Map.Entry dataOne;
        Iterator iter = data.entrySet().iterator();
        while (iter.hasNext()){
            dataOne = (Map.Entry)iter.next();
            doc.append(dataOne.getKey().toString(), dataOne.getValue());
        }
        long mc = dc.updateOne(Filters.eq("user_id", doc.get("user_id")), new Document("$set", doc), up).getModifiedCount();

    }

    public void updateObj(Document data){
        Document docTemp = new Document();
        dc.updateOne(Filters.eq("user_id", data.get("user_id")), docTemp.append("$set", data), up);
    }

    public void updateObj(List<Document> dataList){
        Iterator<Document> iter = dataList.iterator();
        while (iter.hasNext()){
            Document tmp = iter.next();
            dc.updateOne(Filters.eq("user_id", tmp.get("user_id")), new Document("$set", tmp), up);
        }
    }

    public void insertList(List<HashMap<String, String>> data){
        List<DBObject> objList = new ArrayList<>();
        Iterator<HashMap<String, String>> iter = data.iterator();
        while (iter.hasNext()){
            DBObject obj = new BasicDBObject();
            HashMap<String, String> temp = iter.next();
            Iterator iterMap = temp.entrySet().iterator();
            while (iterMap.hasNext()){
                Map.Entry dataOne = (Map.Entry)iterMap.next();
                obj.put(dataOne.getKey().toString(), dataOne.getValue());
            }
            objList.add(obj);
        }
        dc.insertOne(objList);
    }

}
