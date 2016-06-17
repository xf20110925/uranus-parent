package com.ptb.uranus.server.third.weibo.task;

import com.ptb.uranus.server.third.weibo.script.MongoClientm;
import com.ptb.uranus.server.third.weibo.script.MysqlClient;
import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;
import org.bson.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by watson zhang on 16/5/20.
 */
public class WeiboMediaToMongo implements Runnable {

    public class ShareData{
        private long start;
        private long last;
        private int cycle;
        private String lock;

        public long getStart() {
            return start;
        }

        public void setStart(long start) {
            this.start = start;
        }

        public long getLast() {
            return last;
        }

        public void setLast(long last) {
            this.last = last;
        }

        public int getCycle() {
            return cycle;
        }

        public void setCycle(int cycle) {
            this.cycle = cycle;
        }

        public String getLock() {
            return lock;
        }

        public void setLock(String lock) {
            this.lock = lock;
        }
    }
    private static Logger logger = LoggerFactory.getLogger(MysqlClient.class);
    private ShareData shd = new ShareData();
    private String mongoHost;
    private int mongoPort;
    private String mongoDbName;
    private String mongoColl;

    String mysqlHost;
    String mysqlUser;
    String mysqlPwd;
    String tableName;

    public WeiboMediaToMongo(String lock) throws ConfigurationException {
        PropertiesConfiguration conf;
        conf = new PropertiesConfiguration("uranus.properties");
        shd.setCycle(conf.getInt("uranus.bayou.cycleNum", 1000));
        shd.setStart(conf.getLong("uranus.bayou.startNum", 1329986614));
        shd.setLast(conf.getLong("uranus.bayou.lastNum", 1340272263));
        shd.setLock(lock);

        try {
            conf = new PropertiesConfiguration("uranus.properties");
        } catch (ConfigurationException e) {
            e.printStackTrace();
        }
        mongoHost = conf.getString("com.ptb.uranus.mongoHost", "192.168.5.31");
        mongoPort = conf.getInt("com.ptb.uranus.mongoPort", 27017);
        mongoDbName = conf.getString("com.ptb.uranus.mongoDbName", "weibo");
        mongoColl = conf.getString("com.ptb.uranus.mongoColl", "thread_test");

        mysqlHost = conf.getString("uranus.bayou.mysqlHost", "43.241.214.85:3306/weibo");
        mysqlUser = conf.getString("uranus.bayou.mysqlUser", "pintuibao");
        mysqlPwd = conf.getString("uranus.bayou.mysqlPwd", "pintuibao");
        tableName = conf.getString("uranus.bayou.mysqlMediaTableName", "user_profile");
    }

    public void getStartId(MysqlClient mysql){
        int startTmp = mysql.getStartId();
        synchronized (shd.getLock()){
            shd.setStart(startTmp);
        }
    }

    public void getLastId(MysqlClient mysql){
        int lastTmp = mysql.getLastId();
        synchronized (shd.getLock()){
            shd.setLast(lastTmp);
        }
    }

    public void updateStartId(MysqlClient mysql){
        int startTmp = mysql.updateStartId(shd.getStart());
        synchronized (shd.getLock()){
            shd.setStart(startTmp);
        }
    }

    private ResultSet updateStart(MysqlClient mysql){
        ResultSet rs = null;
        try{
            long startTime = System.currentTimeMillis();
            synchronized (shd.getLock()){
                logger.warn("start: {} [normal]", shd.getStart());
                rs = mysql.cycleGetData(shd.getStart(), shd.getCycle());
                if((rs != null) && rs.last()){
                    shd.setStart(Long.parseLong(rs.getString("id").toString()));
                }
            }
            long endTime = System.currentTimeMillis();
            logger.info("Once time: {}", endTime - startTime);
            return rs;
        }catch (SQLException e){
            logger.error("updateStart error!", e);
        }
        return rs;
    }

    public List<Document> resultsetTodoc(ResultSet rs) throws SQLException {
        List<Document> docList = new ArrayList<>();
        ResultSetMetaData md = rs.getMetaData();
        int cc = md.getColumnCount();
        int num = 0;
        Document doc = null;

        rs.first();
        while (rs.next()){
            num++;
            doc = new Document();
            for(int i = 1;i < cc; i++){
                doc.append(md.getColumnName(i), rs.getString(i));
            }
            docList.add(doc);
        }
        logger.info("column num {}", num);
        return docList;
    }

    @Override
    public void run() {
        MysqlClient mysql;
        mysql = new MysqlClient(mysqlHost, mysqlUser, mysqlPwd, tableName);
        MongoClientm mongo = new MongoClientm(this.mongoHost, this.mongoPort, this.mongoDbName, this.mongoColl);
        List<Document> docList = null;
        ResultSet rs = null;

        while (true){
            while (shd.getStart() < shd.getLast()){
                rs = this.updateStart(mysql);

                if(rs == null){
                    this.updateStartId(mysql);
                    this.getLastId(mysql);
                    continue;
                }

                try {
                    docList = this.resultsetTodoc(rs);
                } catch (SQLException e) {
                    e.printStackTrace();
                    continue;
                }
                mongo.updateObj(docList);
            }
            try {
                Thread.sleep(10000);
                this.getStartId(mysql);
                this.getLastId(mysql);
            } catch (InterruptedException e) {
                logger.error("restart failed!", e);
                return;
            }
        }
    }


    public static void main(String[] args) throws ConfigurationException {

        String lock = new String("lock");
        new Thread((Runnable)new WeiboMediaToMongo(lock)).start();
    }
}
