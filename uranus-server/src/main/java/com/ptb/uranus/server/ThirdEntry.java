package com.ptb.uranus.server;

import com.ptb.gaia.bus.Bus;
import com.ptb.gaia.bus.kafka.KafkaBus;
import com.ptb.uranus.server.send.BusSender;
import com.ptb.uranus.server.send.Sender;
import com.ptb.uranus.server.third.WeiboMediaHandle;
import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.*;

/**
 * Created by watson zhang on 16/5/31.
 */
public class ThirdEntry {
    public enum TASKNAME{
        WEIBOMEDIA,
        WEIBOARTICLE,
    }
    private static Logger logger = LoggerFactory.getLogger(ThirdEntry.class);
    private Bus bus;
    private Sender sender;
    private PropertiesConfiguration conf;
    private HashMap<TASKNAME, Runnable> thread = new HashMap<>();
    private int weiboMediaNum;
    private int weiboArticleNum;
    private int busWorkNum;

    public ThirdEntry() throws ConfigurationException {
        conf = new PropertiesConfiguration("uranusThird.properties");
        weiboMediaNum = conf.getInt("com.ptb.uranus.weiboMediaNum", 1);
        weiboArticleNum = conf.getInt("com.ptb.uranus.weiboArticleNum", 1);
        busWorkNum = conf.getInt("com.ptb.uranus.busWorkNum", 1);
        bus = new KafkaBus();

        sender = new BusSender(this.bus);

        thread.put(ThirdEntry.TASKNAME.WEIBOMEDIA, new WeiboMediaHandle(sender));
        bus.start(false,busWorkNum);
        //thread.put(ThirdEntry.TASKNAME.WEIBOARTICLE, new WeiboArticleHandle(sender));
    }

    public static void main(String[] args) throws ConfigurationException {
        logger.debug("begin ...");
        ThirdEntry thirdEntry = new ThirdEntry();

        try{
            Iterator iter = thirdEntry.thread.entrySet().iterator();
            while (iter.hasNext()){
                Map.Entry runTmp = (Map.Entry)iter.next();
                switch ((TASKNAME)runTmp.getKey()){
                    case WEIBOMEDIA:
                        for(int i = 0;i < thirdEntry.weiboMediaNum;i++){
                            ((Runnable)(runTmp.getValue())).run();
                        }
                        break;
                    case WEIBOARTICLE:
                        for(int i = 0;i < thirdEntry.weiboArticleNum;i++){
                            ((Runnable)(runTmp.getValue())).run();
                        }
                        break;
                    default:
                        break;
                }
            }
        }catch (Exception e){
            logger.error("start thread error!", e.getLocalizedMessage());
        }

        return;
    }
}
