
package com.ptb.uranus.server.third.weibo.script;

import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;

import java.sql.*;


/**
 * Created by watson zhang on 16/6/15.
 */
public class MysqlClient {

    static Connection conn;
    static String mysqlHost;
    static String mysqlUser;
    static String mysqlPwd;

    static {
        initConfig();
        initConnection();
    }

    private static void initConfig() {
        try {
            PropertiesConfiguration conf = new PropertiesConfiguration("uranus.properties");
            mysqlHost = conf.getString("uranus.bayou.mysqlHost", "43.241.214.85:3306/weibo");
            mysqlUser = conf.getString("uranus.bayou.mysqlUser", "pintuibao");
            mysqlPwd = conf.getString("uranus.bayou.mysqlPwd", "pintuibao");
        } catch (ConfigurationException e) {
            e.printStackTrace();
        }
    }

    private static void initConnection() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver").newInstance();
            conn = DriverManager.getConnection(String.format("jdbc:mysql://%s?useUnicode=true&characterEncoding=UTF8", mysqlHost), mysqlUser, mysqlPwd);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static ResultSet executeQuery(String sql, long startid, int batchSize) {
        try {
            PreparedStatement preparedStatement = conn.prepareStatement(sql);
            preparedStatement.setLong(1, startid);
            preparedStatement.setInt(2, batchSize);
            ResultSet resultSet = preparedStatement.executeQuery();
            return resultSet;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

}

