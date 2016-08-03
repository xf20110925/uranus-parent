package com.ptb.uranus.server.third.v1.weibo.task;

import com.mongodb.MongoClient;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.ptb.uranus.server.send.Sender;
import com.ptb.uranus.server.send.entity.convert.SendObjectConvertUtil;
import com.ptb.uranus.server.send.entity.media.WeiboMediaStatic;
import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;
import org.bson.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by watson zhang on 16/5/31.
 */
public class WeiboMediaHandle implements Runnable{
    private static Logger logger = LoggerFactory.getLogger("smartspider.error");
    private PropertiesConfiguration conf;

    private String host;
    private int port;
    private String db;
    private String coll;

    private MongoClient dbHead;
    private MongoCollection collHead;
    private int pos;
    Sender sender;

    public WeiboMediaHandle(Sender sender) throws ConfigurationException {
        conf = new PropertiesConfiguration("uranus.properties");
        host = conf.getString("uranus.bayou.mongo.host", "192.168.5.31");
        port = conf.getInt("uranus.bayou.mongo.port", 27017);
        db = conf.getString("uranus.bayou.mongo.db", "uranus");
        coll = conf.getString("com.ptb.bayou.mongo.coll", "weiboMedia");

        dbHead = new MongoClient(host, port);
        collHead = dbHead.getDatabase(db).getCollection(coll);
        this.sender = sender;
    }

    public int sendTask(){
        try{
            FindIterable cursor = collHead.find().skip(pos);
            MongoCursor iter = cursor.iterator();
            while (iter.hasNext()){
                Document doc = (Document) iter.next();
                WeiboMediaStatic weiboMediaStatic = SendObjectConvertUtil.weiboMediaStaticConvert(doc);
                this.sender.sendMediaStatic(weiboMediaStatic);
                pos++;
            }
            return 0;
        }catch (Exception e){
            logger.error("send task error!",e);
            return -1;
        }
    }

    @Override
    public void run() {
        while (true){
            int ret = this.sendTask();
            if(ret == 0){
                pos = 0;
            }
            logger.error("error {} {}", pos, ret);
            try {
                Thread.sleep(10000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
