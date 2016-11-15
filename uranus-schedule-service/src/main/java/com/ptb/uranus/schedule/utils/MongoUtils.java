//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.ptb.uranus.schedule.utils;

import com.mongodb.MongoClient;
import com.mongodb.MongoClientOptions;
import com.mongodb.ReadPreference;
import com.mongodb.ServerAddress;
import com.mongodb.WriteConcern;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;

import org.apache.commons.configuration.Configuration;
import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;
import org.apache.commons.lang.StringUtils;
import org.bson.Document;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public enum MongoUtils {
	instance;
	private List<String> hosts;
	private int port;
	private MongoClient mongoClient;

	MongoUtils() {
		try {
			Configuration conf = new PropertiesConfiguration("ptb.properties");
			String mongoHosts = conf.getString("uranus.scheduler.mongo.host");
			if (mongoHosts.contains(",")){
				this.hosts = Arrays.asList(mongoHosts.split(","));
			}else if(mongoHosts.contains("|")){
				this.hosts = Arrays.asList(mongoHosts.split("\\|"));
			}else{
				this.hosts = Arrays.asList(mongoHosts);
			}
			this.port = conf.getInt("uranus.scheduler.mongo.port");
			this.mongoClient = getMongoClientV2(hosts, port);
		} catch (ConfigurationException e) {
			e.printStackTrace();
		}
	}

	private static MongoClient getMongoClientV2(List<String>  host, int port) {
		ReadPreference readPreference = ReadPreference.secondaryPreferred();
		List<ServerAddress> addresses = host.stream().map(x -> new ServerAddress(x, port)).collect(Collectors.toList());
		MongoClientOptions.Builder options = new MongoClientOptions.Builder().readPreference(readPreference)
			.connectionsPerHost(300).connectTimeout(30000).maxWaitTime(5000).socketTimeout(0)
			.threadsAllowedToBlockForConnectionMultiplier(5000).writeConcern(WriteConcern.ACKNOWLEDGED);
		return new MongoClient(addresses, options.build());
	}

	public MongoDatabase getDatabase(String dbName) {
		try {
			if (StringUtils.isNotBlank(dbName)) {
				return instance.mongoClient.getDatabase(dbName);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		throw new IllegalArgumentException("连接mongo失败！");
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

	public void dropCollection(String dbName, String collName) {
		getCollection(dbName, collName).drop();
	}

	public static void main(String[] args) {
		MongoDatabase test = MongoUtils.instance.getDatabase("test");
	}
}
