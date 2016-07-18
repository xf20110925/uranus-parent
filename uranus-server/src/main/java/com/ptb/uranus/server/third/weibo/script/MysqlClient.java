package com.ptb.uranus.server.third.weibo.script;

import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;
import org.apache.commons.dbcp.BasicDataSource;

import java.sql.*;


/**
 * Created by watson zhang on 16/6/15.
 */
public enum MysqlClient {
	instance;

	MysqlClient() {
	}

	BasicDataSource dataSource;

	static {
		initConfig();
	}


	private static void initConfig() {
		try {
			PropertiesConfiguration conf = new PropertiesConfiguration("uranus.properties");
			String mysqlHost = conf.getString("uranus.bayou.mysqlHost", "43.241.214.85:3306/weibo");
			String mysqlUser = conf.getString("uranus.bayou.mysqlUser", "pintuibao");
			String mysqlPwd = conf.getString("uranus.bayou.mysqlPwd", "pintuibao");
			instance.dataSource = new BasicDataSource();
			instance.dataSource.setDriverClassName("com.mysql.jdbc.Driver");
			instance.dataSource.setUrl(String.format("jdbc:mysql://%s?useUnicode=true&characterEncoding=UTF8", mysqlHost));
			instance.dataSource.setUsername(mysqlUser);
			instance.dataSource.setPassword(mysqlPwd);
			//初始化链接数
			instance.dataSource.setInitialSize(5);
			//最大空闲链接
			instance.dataSource.setMaxIdle(2);
			//最小空闲链接
			instance.dataSource.setMinIdle(0);
			//自动回收超时链接
			instance.dataSource.setRemoveAbandonedTimeout(180); // 超过时间限制，回收没有用(废弃)的连接
			instance.dataSource.setRemoveAbandoned(true); // 超过removeAbandonedTimeout时间后，是否进 行没用连接（废弃）的回收
			instance.dataSource.setTestOnBorrow(true);
			instance.dataSource.setTestOnReturn(true);
			instance.dataSource.setTestWhileIdle(true);
			instance.dataSource.setValidationQuery("SELECT 1");
			instance.dataSource.setTimeBetweenEvictionRunsMillis(1000 * 60 * 30); // 检查无效连接的时间间隔 设为30分
		} catch (ConfigurationException e) {
			e.printStackTrace();
		}
	}

	public Connection getConn() {
		try {
			return instance.dataSource.getConnection();
		} catch (SQLException e) {
			throw new RuntimeException("get conn from dataSource error");
		}
	}

	public void reInit() {
		try {
			if (instance.dataSource != null) {
				instance.dataSource.close();
				instance.dataSource = null;
				initConfig();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

/*    public ResultSet executeQuery(String sql, long startid, int batchSize) {
        try (Connection conn = instance.dataSource.getConnection();) {
            PreparedStatement preparedStatement = conn.prepareStatement(sql);
            preparedStatement.setLong(1, startid);
            preparedStatement.setInt(2, batchSize);
            ResultSet resultSet = preparedStatement.executeQuery();
            return resultSet;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }*/
}

