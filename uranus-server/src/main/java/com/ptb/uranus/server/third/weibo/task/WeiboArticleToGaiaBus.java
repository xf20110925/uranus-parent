package com.ptb.uranus.server.third.weibo.task;

import com.ptb.gaia.bus.Bus;
import com.ptb.gaia.bus.kafka.KafkaBus;
import com.ptb.uranus.schedule.dao.MongoSchedulerDao;
import com.ptb.uranus.schedule.dao.SchedulerDao;
import com.ptb.uranus.schedule.model.SchedulableCollectCondition;
import com.ptb.uranus.schedule.model.ScheduleObject;
import com.ptb.uranus.server.send.BusSender;
import com.ptb.uranus.server.send.Sender;
import com.ptb.uranus.server.send.entity.article.BasicArticleDynamic;
import com.ptb.uranus.server.send.entity.article.BasicArticleStatic;
import com.ptb.uranus.server.send.entity.convert.SendObjectConvertUtil;
import com.ptb.uranus.server.third.weibo.script.MysqlClient;
import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Optional;
import java.util.regex.Pattern;

/**
 * Created by watson zhang on 16/5/31.
 */
public class WeiboArticleToGaiaBus implements Runnable {
    PropertiesConfiguration conf;
    private static Logger logger = LoggerFactory.getLogger(WeiboMediaHandle.class);
    SchedulerDao schedulerDao;
    String conditonTemplate = "%s:::%d";
    String conditonField = "obj.conditon";
    Sender sender;

    long startNum;
    long lastNum;
    int cycleNum;

    String mysqlHost;
    String mysqlUser;
    String mysqlPwd;
    String tableName;


    public WeiboArticleToGaiaBus(Sender sender) throws ConfigurationException {
        conf = new PropertiesConfiguration("uranus.properties");
        startNum = conf.getLong("uranus.bayou.startNum", 1295883035);
        lastNum = conf.getLong("uranus.bayou.lastNum", 1309360356);
        cycleNum = conf.getInt("uranus.bayou.cycleNum", 1000);
        schedulerDao = new MongoSchedulerDao();
        this.sender = sender;

        mysqlHost = conf.getString("uranus.bayou.mysqlHost", "43.241.214.85:3306/weibo");
        mysqlUser = conf.getString("uranus.bayou.mysqlUser", "pintuibao");
        mysqlPwd = conf.getString("uranus.bayou.mysqlPwd", "pintuibao");
        tableName = conf.getString("uranus.bayou.mysqlArticleTableName", "fresh_data");
    }

    public String getConditionByTemplate(String weiboid, long times) {
        return String.format(conditonTemplate, weiboid, times);
    }

    public void updateSchedule(HashMap<String, Long> history) {
        Iterator iter = history.entrySet().iterator();
        while (iter.hasNext()) {
            Map.Entry entry = (Map.Entry) iter.next();
            Optional<ScheduleObject> first = schedulerDao.getSchedulerByField(this.conditonField, Pattern.compile(String.format("%s.*", entry.getKey())));
            if (first.isPresent()) {
                SchedulableCollectCondition schedulableCollectCondition = (SchedulableCollectCondition) first.get().getObj();

                schedulableCollectCondition.setConditon(getConditionByTemplate((String) entry.getKey(), (Long) entry.getValue()));
                first.get().setObjByT(schedulableCollectCondition);
                schedulerDao.updateScheduler(first.get());
            }
        }
    }

    public void updateHistoryList(HashMap<String, Long> history, ResultSet rs) {
        try {
            if (history == null || rs == null) {
                logger.debug("updateHistoryList error");
                return;
            }
            long lastTime;
            if (!history.containsKey(rs.getString("user_id"))) {
                history.put(rs.getString("user_id"), Long.parseLong(rs.getString("time_stamp")));
            } else {
                lastTime = Long.parseLong(rs.getString("time_stamp"));
                if (history.get(rs.getString("user_id")) < lastTime) {
                    history.put(rs.getString("user_id"), lastTime);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void run() {
        MysqlClient mysqlClient = new MysqlClient(mysqlHost, mysqlUser, mysqlPwd, tableName);
        BasicArticleDynamic bad;
        BasicArticleStatic bas;

        Long startId = 1L;
        int batch = 500;

        while (true) {
            ResultSet rs = null;
            try {
                rs = mysqlClient.cycleGetData(startId, batch);
                if(rs == null) {
                    Thread.sleep(5000);
                }
                while(rs.next()) {
                    bas = SendObjectConvertUtil.weiboArticleStaticConvert(rs);
                    bad = SendObjectConvertUtil.weiboArticleDynamicConvert(rs);
                    sender.sendArticleStatic(bas);
                    sender.sendArticleDynamic(bad);
                    startId = rs.getLong("id");
                }
            } catch (SQLException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            } finally {
                if(rs != null) {
                    try {
                        rs.close();
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    public static void main(String[] args) throws ConfigurationException {

        Bus bus = new KafkaBus();
        Sender sender = new BusSender(bus);

        bus.start(false, 1);
        new Thread((Runnable) new WeiboArticleToGaiaBus(sender)).start();
    }
}
