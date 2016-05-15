package com.ptb.uranus.spider.smart.utils;


import com.mongodb.MongoClient;
import com.mongodb.MongoClientOptions;
import com.mongodb.ServerAddress;
import com.mongodb.WriteConcern;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.sun.tools.javac.util.List;
import org.apache.commons.configuration.CompositeConfiguration;
import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;
import org.apache.commons.lang.StringUtils;
import org.bson.Document;

import java.util.ArrayList;

public enum MongoDBUtil {
    instance;
    public static String uranusDB = "uranus";
    public static String CollActionSet = "actionSet";
    public static String CollPlatform = "platformCode";


    private MongoClient mongoClient = null;

    private MongoDBUtil() {
    }

    static {
        CompositeConfiguration config = new CompositeConfiguration();

        try {
            config.addConfiguration(new PropertiesConfiguration("uranus.properties"));
        } catch (ConfigurationException var4) {
            var4.printStackTrace();
        }

        String ip = config.getString("uranus.spider.smart.mongo.address");
        int port = config.getInt("uranus.spider.smart.mongo.port", 27017);
        uranusDB = config.getString("uranus.spider.smart.mongo.db", "uranus");

        MongoClientOptions.Builder options = new MongoClientOptions.Builder();
        options.connectionsPerHost(300);
        options.connectTimeout(15000);
        options.maxWaitTime(5000);
        options.socketTimeout(0);
        options.writeConcern(WriteConcern.SAFE);
        instance.mongoClient = new MongoClient(new ServerAddress(ip, port),options.build());
    }

    public MongoDatabase getDB(String dbName) {
        if (StringUtils.isNotBlank(dbName)) {
            return this.mongoClient.getDatabase(dbName);
        }
        return null;
    }

    public MongoDatabase getDefaultDB() {
        return this.getDB(uranusDB);
    }

    public MongoCollection<Document> getCollection(String db, String collName) {
        return getDB(db).getCollection(collName);
    }

    public MongoCollection<Document> getDefaultColl() {
        return getDB(uranusDB).getCollection(CollActionSet);
    }

    public void closeConnection() {
        if (mongoClient != null) {
            mongoClient.close();
            mongoClient = null;
        }
    }

}
