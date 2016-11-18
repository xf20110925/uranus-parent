package com.ptb.uranus.server.third.util;

import org.apache.commons.configuration.Configuration;
import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

public enum JedisUtil {
	instance;
	private static Logger logger = LoggerFactory.getLogger(JedisUtil.class);
	private JedisPool jedisPool;

	JedisUtil() {
		Configuration config = null;
		try {
			config = new PropertiesConfiguration("ptb.properties");
		} catch (ConfigurationException e) {
			throw new RuntimeException("ptb.properties not found", e);
		}
		// 从配置文件中获取属性值
		String host = config.getString("uranus.bayou.redis.host", "192.168.40.12");
		int port = config.getInt("uranus.bayou.redis.port", 6379);
		JedisPoolConfig jdsConfig = new JedisPoolConfig();
		jdsConfig.setMaxIdle(200);
		jdsConfig.setTestOnBorrow(true);
		jdsConfig.setTestOnReturn(true);
		jdsConfig.setTestWhileIdle(true);
		jedisPool = new JedisPool(jdsConfig, host, port);
	}

	/**
	 * 获取数据
	 */
	public String get(String key) {
		String value = null;
		Jedis jedis = null;
		try {
			jedis = jedisPool.getResource();
			value = jedis.get(key);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(e.getMessage());
		} finally {
			jedis.close();
		}
		return value;
	}

		/**
	 * 获取数据
	 */
	public Boolean exists(String key) {
		Jedis jedis = null;
		try {
			jedis = jedisPool.getResource();
			return jedis.exists(key);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(e.getMessage());
		} finally {
			jedis.close();
		}
		return false;
	}

	public static void main(String[] args) {
		String cz16111412 = JedisUtil.instance.get("CZ16111511");
		System.out.println(cz16111412);
		System.out.println(JedisUtil.instance.exists("CZ16111511s"));
	}
}
