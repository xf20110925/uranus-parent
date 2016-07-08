package com.ptb.uranus.server.third.weibo.task;

import com.ptb.uranus.schedule.dao.SchedulerDao;
import com.ptb.uranus.schedule.model.SchedulableCollectCondition;
import com.ptb.uranus.schedule.model.ScheduleObject;
import com.ptb.uranus.server.send.Sender;
import com.ptb.uranus.server.send.entity.article.BasicArticleDynamic;
import com.ptb.uranus.server.send.entity.article.BasicArticleStatic;
import com.ptb.uranus.server.send.entity.convert.SendObjectConvertUtil;
import com.ptb.uranus.server.third.weibo.script.MysqlClient;
import com.ptb.uranus.server.third.weixin.util.IdRecordUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Optional;
import java.util.regex.Pattern;

import static org.bouncycastle.asn1.x509.X509ObjectIdentifiers.id;

/**
 * Created by watson zhang on 16/5/31.
 */
public class WeiboArticleToGaiaBus implements Runnable {
    private static Logger logger = LoggerFactory.getLogger(WeiboMediaHandle.class);
    Sender sender;

    public WeiboArticleToGaiaBus(Sender sender) {
        this.sender = sender;
    }
   /* SchedulerDao schedulerDao;
    String conditonTemplate = "%s:::%d";
    String conditonField = "obj.conditon";


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

    }*/

    @Override
    public void run() {
        BasicArticleDynamic bad;
        BasicArticleStatic bas;

        int batch = 500;
        long startId = IdRecordUtil.getIdRecord().get().getWbArticleId();

        while (true) {
            String sql = "select * from fresh_data where id > ?  LIMIT ?";
            try (ResultSet rs = MysqlClient.executeQuery(sql, startId, batch);) {
                if (rs == null) {
                    Thread.sleep(5000);
                    continue;
                }
                while (rs.next()) {
                    bas = SendObjectConvertUtil.weiboArticleStaticConvert(rs);
                    bad = SendObjectConvertUtil.weiboArticleDynamicConvert(rs);
                    sender.sendArticleStatic(bas);
                    sender.sendArticleDynamic(bad);
                    startId = startId < rs.getLong("id") ? rs.getLong("id") : startId;
                }
                IdRecordUtil.syncWbArticleId(startId);
            } catch (SQLException | InterruptedException e) {
                logger.error(e.getMessage());
                e.printStackTrace();
            }
        }
    }
}
