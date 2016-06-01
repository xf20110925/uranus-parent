package com.ptb.uranus.server.third;

import com.ptb.uranus.server.send.Sender;
import com.ptb.uranus.server.send.entity.article.BasicArticleDynamic;
import com.ptb.uranus.server.send.entity.article.BasicArticleStatic;
import com.ptb.uranus.server.send.entity.article.WeiboArticleStatic;
import com.ptb.uranus.server.send.entity.convert.SendObjectConvertUtil;
import com.ptb.utils.date.TimeUtil;
import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;
import java.util.concurrent.TimeUnit;

/**
 * Created by watson zhang on 16/5/31.
 */
public class WeiboArticleHandle implements Runnable{
    PropertiesConfiguration conf;
    private static Logger logger = LoggerFactory.getLogger(WeiboMediaHandle.class);
    private String driver = "com.mysql.cj.jdbc.Driver";
    private Connection conn = null;
    private Statement stmt = null;

    private String mysqlHost;
    private String mysqlUser;
    private String mysqlPwd;
    private String mysqlTableName;
    private int maxNum;
    private int cycle;
    private Sender sender;

    public WeiboArticleHandle(Sender sender) throws ConfigurationException {
        conf = new PropertiesConfiguration("uranusThird.properties");
        mysqlHost = conf.getString("com.ptb.uranus.mysqlHost", "43.241.214.85:33066/weibo");
        mysqlUser = conf.getString("com.ptb.uranus.mysqlUser", "pintuibao");
        mysqlPwd = conf.getString("com.ptb.uranus.mysqlPwd", "wushilong");
        mysqlTableName = conf.getString("com.ptb.uranus.mysqlTableName", "user_profile");
        maxNum = conf.getInt("com.ptb.uranus.maxNum", 10000);
        cycle = conf.getInt("com.ptb.uranus.maxNum", 1000);
        this.sender = sender;
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
            logger.error("execute error {}", sql);
        }
        return rs;
    }

    public ResultSet cycleGetDataTail(int start, int cycle) throws SQLException {
        ResultSet rs;
        //String sql = String.format("SELECT * FROM %s WHERE id < %d ORDER BY id DESC LIMIT %d", this.mysqlTableName , last, limit);
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

    public void sendTask(Sender sender) throws SQLException {
        BasicArticleDynamic bad;
        BasicArticleStatic bas;
        int start = 0;
        ResultSet rs;
        try {
            while (start < this.maxNum){
                rs = this.cycleGetDataTail(start, this.cycle);
                start += this.cycle;
                bas = SendObjectConvertUtil.weiboArticleStaticConvert(rs);
                bad = SendObjectConvertUtil.weiboArticleDynamicConvert(rs);
                sender.sendArticleStatic(bas);
                sender.sendArticleDynamic(bad);
            }
        }catch (Exception e){
            logger.error("send task error!", e.getLocalizedMessage());
        }
    }

    @Override
    public void run() {
        while (true){
            try {
                this.sendTask(this.sender);
            } catch (SQLException e) {
                e.printStackTrace();
            }
            try {
                Thread.sleep(TimeUnit.HOURS.toMillis(24));
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
