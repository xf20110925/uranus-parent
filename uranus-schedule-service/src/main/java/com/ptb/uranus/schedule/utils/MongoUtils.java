//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.ptb.uranus.schedule.utils;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;

import org.bson.Document;

public enum MongoUtils {
    instance;

    MongoUtils() {
    }

    public MongoDatabase getDatabase(String dbName) {
        if (dbName != null && !"".equals(dbName)) {
            MongoDatabase database = com.ptb.utils.gaiatools.MongoUtils.instance().getDatabase(dbName);
            return database;
        } else {
            return null;
        }
    }


    public MongoCollection<Document> getCollection(String dbName, String collName) {
        if (null != collName && !"".equals(collName)) {
            if (null != dbName && !"".equals(dbName)) {
                MongoCollection collection = this.getDatabase(dbName).getCollection(collName);
                return collection;
            } else {
                return null;
            }
        } else {
            return null;
        }
    }

}
