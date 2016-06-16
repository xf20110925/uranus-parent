package com.ptb.uranus.server.third.weibo.script;

import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by watson zhang on 16/6/15.
 */
public class MysqlClient {
    PropertiesConfiguration conf;
    String driver = "com.mysql.cj.jdbc.Driver";
    private Connection conn;
    private Statement stmt;

    String mysqlHost;
    String mysqlUser;
    String mysqlPwd;
    String mysqlTableName;

    public MysqlClient() {
        try {
            conf = new PropertiesConfiguration("uranus.properties");
        } catch (ConfigurationException e) {
            e.printStackTrace();
        }
        mysqlHost = conf.getString("uranus.bayou.mysqlHost", "43.241.214.85:3306/weibo");
        mysqlUser = conf.getString("uranus.bayou.mysqlUser", "pintuibao");
        mysqlPwd = conf.getString("uranus.bayou.mysqlPwd", "pintuibao");
        mysqlTableName = conf.getString("uranus.bayou.mysqlTableName", "fresh_data");
    }

    public void connectMySQL() {
        try {
            Class.forName(driver).newInstance();
            conn = (Connection) DriverManager.getConnection("jdbc:mysql://"
                    + mysqlHost + "?useUnicode=true&characterEncoding=UTF8", mysqlUser, mysqlPwd);
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void close() {
        try {
            if (stmt != null) {
                stmt.close();
                stmt = null;
            }
            if (conn != null) {
                conn.close();
                conn = null;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            stmt = null;
            conn = null;
        }
    }

    public void statement() {
        if (conn == null) {
            this.connectMySQL();
        }
        try {
            stmt = (Statement) conn.createStatement();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public ResultSet resultSet(String sql) {
        ResultSet rs = null;
        if (stmt == null) {
            this.statement();
        }
        try {
            rs = stmt.executeQuery(sql);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return rs;
    }


    public List<HashMap<String, String>> result(String sql) {
        try {
            ResultSet rs = this.resultSet(sql);
            List<HashMap<String, String>> result = new ArrayList<HashMap<String, String>>();
            ResultSetMetaData md = rs.getMetaData();
            int cc = md.getColumnCount();
            while (rs.next()) {
                HashMap<String, String> columnMap = new HashMap<String, String>();
                for (int i = 1; i <= cc; i++) {
                    columnMap.put(md.getColumnName(i), rs.getString(i));
                }
                result.add(columnMap);
            }
            return result;
        } catch (SQLException e) {
            this.close();
            return null;
        }
    }


    public int getStartId(){
        int start = 0;
        List<HashMap<String, String>> rsList;
        String sql = String.format("SELECT * from %s ORDER BY id LIMIT 1",this.mysqlTableName);
        rsList = this.result(sql);
        start = Integer.parseInt(rsList.get(0).get("id"));
        return start;
    }

    public int getLastId(){
        int last = 0;
        List<HashMap<String, String>> rsList;
        String sql = String.format("SELECT * from %s ORDER BY id DESC LIMIT 1", this.mysqlTableName);
        rsList = this.result(sql);
        last = Integer.parseInt(rsList.get(0).get("id"));
        return last;
    }

    public int updateStartId(long oldStart){
        int start = 0;
        ResultSet rs;
        String sql = String.format("SELECT * from %s WHERE id > %d  ORDER BY id LIMIT 1", this.mysqlTableName, oldStart);
        rs = this.resultSet(sql);
        try {
            start = Integer.parseInt(rs.getString("id").toString());
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return start;
    }

    public int updateLastId(long oldLast){
        int last = 0;
        List<HashMap<String, String>> rs;
        String sql = String.format("SELECT * from %s WHERE id < %d  ORDER BY id DESC LIMIT 1", this.mysqlTableName, oldLast);
        rs = this.result(sql);
        last = Integer.parseInt(rs.get(0).get("id"));
        return last;
    }

    public ResultSet cycleGetDataTail(long start, int cycle) throws SQLException {
        ResultSet rs;
        String sql = String.format("SELECT * FROM %s limit %d, %d", this.mysqlTableName, start, cycle);
        rs = this.resultSet(sql);
        if(rs == null){
            return null;
        }
        if(!rs.next()){
            return null;
        }
        return rs;
    }

    public ResultSet cycleGetData(long start, int limit) throws SQLException {
        ResultSet rs;
        String sql = String.format("SELECT * FROM %s WHERE id > %d LIMIT %d", this.mysqlTableName , start, limit);
        rs = this.resultSet(sql);
        if(rs == null){
            return null;
        }

        if(!rs.next()){
            return null;
        }
        return rs;
    }

}
