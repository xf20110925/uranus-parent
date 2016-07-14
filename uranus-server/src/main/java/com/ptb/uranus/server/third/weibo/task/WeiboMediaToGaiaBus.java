package com.ptb.uranus.server.third.weibo.task;

import com.ptb.uranus.server.send.Sender;
import com.ptb.uranus.server.send.entity.convert.SendObjectConvertUtil;
import com.ptb.uranus.server.send.entity.media.BasicMediaDynamic;
import com.ptb.uranus.server.send.entity.media.WeiboMediaStatic;
import com.ptb.uranus.server.third.weibo.script.MysqlClient;
import com.ptb.uranus.server.third.weixin.util.IdRecordUtil;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.bson.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by watson zhang on 16/5/20.
 */
public class WeiboMediaToGaiaBus implements Runnable {

    private static Logger logger = LoggerFactory.getLogger(WeiboMediaToGaiaBus.class);
    private Sender sender = null;

    public WeiboMediaToGaiaBus(Sender sender) {
        this.sender = sender;
    }

    public ImmutablePair<Long, List<Document>> resultsetTodoc(ResultSet rs) throws SQLException {
        List<Document> docList = new ArrayList<>();
        ResultSetMetaData md = rs.getMetaData();
        int cc = md.getColumnCount();
        Document doc = new Document();
        Long lastId = 0L;
        rs.beforeFirst();
        while (rs.next()) {
            doc.clear();
            for (int i = 1; i <= cc; i++) {
                doc.append(md.getColumnName(i), rs.getString(i));
            }
            lastId = lastId < rs.getLong("id") ? rs.getLong("id") : lastId;
            docList.add(doc);
        }
        return new ImmutablePair<>(lastId, docList);
    }

    @Override
    public void run() {
        List<Document> docList = null;
        Long startId = IdRecordUtil.getIdRecord().get().getWbMediaId();
        int batch = 500;

        String sql = "select * from user_profile where id > ?  LIMIT ?";
        while (true) {
            try (Connection conn = MysqlClient.instance.getConn();) {
                PreparedStatement pres = conn.prepareStatement(sql);
                pres.setLong(1, startId);
                pres.setInt(2, batch);
                ResultSet rs = pres.executeQuery();
                if (rs == null || !rs.next()) {
                    Thread.sleep(10000);
                    continue;
                }
                ImmutablePair<Long, List<Document>> pair = resultsetTodoc(rs);
                docList = pair.getRight();
                for (Document document : docList) {
                    WeiboMediaStatic weiboMediaStatic = SendObjectConvertUtil.weiboMediaStaticConvert(document);
                    BasicMediaDynamic basicMediaDynamic = SendObjectConvertUtil.weiboMediaDynamicConvert(document);
                    this.sender.sendMediaStatic(weiboMediaStatic);
                    this.sender.sendMediaDynamic(basicMediaDynamic);
                }
                startId = pair.getLeft();
                IdRecordUtil.syncWbMediaId(startId);
            } catch (Exception e) {
                logger.error(e.getMessage(), e);
            }
        }

    }
}
