package com.ptb.uranus.server.third.weibo;

import com.ptb.uranus.schedule.dao.MongoSchedulerDao;
import com.ptb.uranus.schedule.dao.SchedulerDao;
import com.ptb.uranus.schedule.model.SchedulableCollectCondition;
import com.ptb.uranus.schedule.model.ScheduleObject;
import com.ptb.uranus.server.send.Sender;
import com.ptb.uranus.server.send.entity.article.BasicArticleDynamic;
import com.ptb.uranus.server.send.entity.article.BasicArticleStatic;
import com.ptb.uranus.server.send.entity.convert.SendObjectConvertUtil;
import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.TimeUnit;
import java.util.regex.Pattern;

/**
 * Created by watson zhang on 16/5/31.
 */
public class WeiboArticleHandle implements Runnable{
    PropertiesConfiguration conf;
    private static Logger logger = LoggerFactory.getLogger(WeiboMediaHandle.class);
    String driver = "com.mysql.cj.jdbc.Driver";
    String conditonField = "obj.conditon";
    String conditonTemplate = "%s:::%d";
    private Connection conn;
    private Statement stmt;

    String mysqlHost;
    String mysqlUser;
    String mysqlPwd;
    String mysqlTableName;
    int maxNum;
    int cycle;
    Sender sender;
    private SchedulerDao schedulerDao;

    public WeiboArticleHandle(Sender sender) throws ConfigurationException {
        conf = new PropertiesConfiguration("uranusThird.properties");
        mysqlHost = conf.getString("com.ptb.uranus.mysqlHost", "43.241.214.85:3306/weibo");
        mysqlUser = conf.getString("com.ptb.uranus.mysqlUser", "pintuibao");
        mysqlPwd = conf.getString("com.ptb.uranus.mysqlPwd", "pintuibao");
        mysqlTableName = conf.getString("com.ptb.uranus.mysqlTableName", "fresh_data");
        maxNum = conf.getInt("com.ptb.uranus.maxNum", 10);
        cycle = conf.getInt("com.ptb.uranus.maxNum", 10);
        this.sender = sender;
        schedulerDao = new MongoSchedulerDao();
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

    public String getConditionByTemplate(String weiboid, long times) {
        return String.format(conditonTemplate, weiboid, times);
    }

    public void updateSchedule(HashMap<String, Long> history) {
        Iterator iter = history.entrySet().iterator();
        while (iter.hasNext()) {
            Map.Entry entry = (Map.Entry) iter.next();
            Optional<ScheduleObject> first = schedulerDao.getSchedulerByField(this.conditonField, Pattern.compile(String.format("%s.*", entry.getKey())));
            if(first.isPresent()){
                SchedulableCollectCondition schedulableCollectCondition = (SchedulableCollectCondition) first.get().getObj();
                schedulableCollectCondition.setConditon(getConditionByTemplate((String)entry.getKey(), (Long) entry.getValue()));
                first.get().setObjByT(schedulableCollectCondition);
                schedulerDao.updateScheduler(first.get());
            }
        }
    }

    public void updateHistoryList(HashMap<String, Long> history, ResultSet rs) throws SQLException {
        if(history == null || rs == null){
            logger.debug("updateHistoryList error");
            return;
        }
        long lastTime;
        if(!history.containsKey(rs.getString("user_id"))){
            history.put(rs.getString("user_id"), Long.parseLong(rs.getString("time_stamp")));
        }else {
            lastTime = Long.parseLong(rs.getString("time_stamp"));
            if(history.get(rs.getString("user_id")) < lastTime){
                history.put(rs.getString("user_id"), lastTime);
            }
        }
    }

    public void sendTask(Sender sender) throws SQLException {
        BasicArticleDynamic bad;
        BasicArticleStatic bas;
        int start = 0;
        ResultSet rs;
        HashMap<String, Long> history = new HashMap<>();
        try {
            while (start < this.maxNum){
                rs = this.cycleGetDataTail(start, this.cycle);
                start += this.cycle;
                do {
                    bas = SendObjectConvertUtil.weiboArticleStaticConvert(rs);
                    bad = SendObjectConvertUtil.weiboArticleDynamicConvert(rs);
                    sender.sendArticleStatic(bas);
                    sender.sendArticleDynamic(bad);
                    this.updateHistoryList(history, rs);
                }while (rs.next());
                this.updateSchedule(history);
            }
        }catch (Exception e){
            logger.error("send task error!", e);
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
