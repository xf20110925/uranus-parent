package com.ptb.uranus.schedule.dao;

import com.alibaba.fastjson.JSON;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.ptb.uranus.schedule.model.ArticlesEntry;
import com.ptb.uranus.schedule.utils.MongoUtils;
import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;
import org.bson.Document;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by eric on 16/5/9.
 */
public class MongoArticlesEntryDao implements ArticlesEntryDao {

    private final PropertiesConfiguration conf;
    private final String db;
    private final String collName = "articlesEntry";

    public MongoArticlesEntryDao() throws ConfigurationException {
        conf = new PropertiesConfiguration("ptb.properties");
        db = conf.getString("uranus.scheduler.smart.spider.mongo.db", "uranus");
    }

    private MongoCollection<Document> coll() {
        return MongoUtils.instance.getCollection(db, collName);
    }

    @Override
    public List<ArticlesEntry> getArticleEntrys() {
        FindIterable<Document> documents = coll().find();
        List list = new ArrayList<>();
        for (Document document : documents) {
            list.add(JSON.parseObject(document.toJson(), ArticlesEntry.class));
        }
        return list;
    }
}
