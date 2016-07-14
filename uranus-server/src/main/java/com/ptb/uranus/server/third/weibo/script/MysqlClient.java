
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
            instance.dataSource.setInitialSize(10);
            //最大空闲链接
            instance.dataSource.setMaxIdle(10);
            //最小空闲链接
            instance.dataSource.setMinIdle(5);
            //自动回收超时链接
            instance.dataSource.setRemoveAbandoned(true);
            instance.dataSource.setRemoveAbandonedTimeout(3600);
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

