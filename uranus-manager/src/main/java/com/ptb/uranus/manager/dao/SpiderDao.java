package com.ptb.uranus.manager.dao;

import com.alibaba.fastjson.JSON;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoIterable;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.UpdateOptions;
import com.mongodb.client.result.DeleteResult;
import com.mongodb.client.result.UpdateResult;
import com.ptb.uranus.manager.bean.ActionSet;
import com.ptb.uranus.manager.bean.News;
import com.ptb.uranus.spider.smart.utils.MongoDBUtil;
import org.bson.Document;
import org.bson.types.ObjectId;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class SpiderDao {

    MongoCollection<Document> collection;
    MongoCollection<Document> newsCollection;

    public SpiderDao() {
        collection = MongoDBUtil.instance.getCollection(MongoDBUtil.uranusDB, MongoDBUtil.CollActionSet);
        newsCollection = MongoDBUtil.instance.getCollection(MongoDBUtil.uranusDB, "articlesEntry");
    }

    public List<ActionSet> getSpiderList() {

        FindIterable<Document> documents = collection.find();

        Document filterField = new Document().append("actions", 0);
        MongoIterable<ActionSet> map = documents.map(document -> {
            String s = document.toJson();
            ActionSet actionSet = JSON.parseObject(s, ActionSet.class);
            if (actionSet.getActions() == null) {
                actionSet.setActions(new ArrayList<>());
            }
            actionSet.setId(document.getObjectId("_id").toHexString());
            return actionSet;
        });


        List<ActionSet> actionSets = new ArrayList<>();
        map.into(actionSets);

        return actionSets;
    }


    public void addActionSet(ActionSet actionSet) {
        final String s = JSON.toJSONString(actionSet);
        Document document = Document.parse(s);
        collection.insertOne(document);
        return;

    }


    public void delActionSet(String oid) {
        DeleteResult id = collection.deleteOne(Filters.eq("_id", new ObjectId(oid)));
    }

    public ActionSet getActionSet(String oid) {
        Document doc = collection.find(Filters.eq("_id", new ObjectId(oid))).first();
        ActionSet actionSet = JSON.parseObject(doc.toJson(), ActionSet.class);
        actionSet.setId(doc.getObjectId("_id").toHexString());
        List<Map<String, String>> actions = actionSet.getActions();
        if (actions != null) {
            actions.sort((o1, o2) -> {
                Integer order1 = Integer.valueOf(o1.get("order"));
                Integer order2 = Integer.valueOf(o2.get("order"));
                return order1.compareTo(order2);
            });
        }

        return actionSet;
    }

    public boolean addAction(HashMap<String, Object> hashMap) {
        String oid = (String) hashMap.get("oid");
        hashMap.remove("oid");
        Document document = new Document();
        document.append("$push", new Document().append("actions", new Document(hashMap)));
        UpdateResult id = collection.updateOne(Filters.eq("_id", new ObjectId(oid)), document);
        if (id.getModifiedCount() > 0) {
            return true;
        } else {
            return false;
        }

    }

    public boolean updateActionSet(ActionSet actionSet) {
        Document parse = new Document().append("$set", Document.parse(JSON.toJSONString(actionSet)));

        parse.remove("id");
        UpdateOptions updateOptions = new UpdateOptions();
        updateOptions.upsert(true);

        UpdateResult id1 = collection.updateOne(Filters.eq("_id", new ObjectId(actionSet.getId())), parse);
        if (id1.getModifiedCount() > 0)

        {
            return true;
        } else

        {
            return false;
        }
    }

    public void clone(String oid) {
        Document doc = collection.find(Filters.eq("_id", new ObjectId(oid))).first();
        doc.remove("_id");
        collection.insertOne(doc);
    }

    public void addNews(News news) {
        final String s = JSON.toJSONString(news);
        Document result = newsCollection.find(Filters.eq("newsurl", news.getNewsurl())).first();
        if (result == null || result.equals("")) {
            Document document = Document.parse(s);
            newsCollection.insertOne(document);
        }
        return;

    }

    public List<News> getNewsList() {
        FindIterable<Document> documents = newsCollection.find();
        Document filterField = new Document().append("actions", 0);
        MongoIterable<News> map = documents.map(document -> {
            String s = document.toJson();
            News news = JSON.parseObject(s, News.class);
            news.setId(document.getObjectId("_id").toHexString());
            return news;
        });
        List<News> resultList = new ArrayList<>();
        map.into(resultList);
        return resultList;
    }

    public void delNews(String _id) {
        DeleteResult id = newsCollection.deleteOne(Filters.eq("_id", new ObjectId(_id)));
    }

    public News getNewsByID(String _id) {
        Document document = newsCollection.find(Filters.eq("_id", new ObjectId(_id))).first();
        String s = document.toJson();
        News news = JSON.parseObject(s, News.class);
        news.setId(document.getObjectId("_id").toHexString());
        return news;
    }

    public boolean updateNews(News news) {
        Document parse = new Document().append("$set", Document.parse(JSON.toJSONString(news)));
        UpdateOptions updateOptions = new UpdateOptions();
        updateOptions.upsert(true);
        UpdateResult result = newsCollection.updateOne(Filters.eq("_id", new ObjectId(news.getId())), parse);
        if (result.getModifiedCount() > 0) {
            return true;
        } else {
            return false;
        }
    }


}
