package com.ptb.uranus.asistant.web.advice;

import com.ptb.gaia.bus.redis.RedisBus;
import com.ptb.uranus.asistant.web.dao.BusDao;
import com.ptb.uranus.common.message.command.Command;
import org.apache.log4j.Logger;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * Created by eric on 16/2/22.
 */
public class MonitorAdvice {
    String monitorTopic = "phone-monitor";
    static Logger logger = Logger.getLogger(MonitorAdvice.class);

    public BusDao getBusDao() {
        return busDao;
    }

    public void setBusDao(BusDao busDao) {
        this.busDao = busDao;
    }

    BusDao busDao;

    /*  @AfterReturning(
              value = "execution(* com.ptb.uranus.asistant.web.controller.WxController.getNewUrl(..))",
              returning = "response"
      )*/
    public void monitorPhone(JoinPoint jp, String response) {
        try {
            if (response.contains("mp.weixin.qq.com/s")) {
                busDao.sendMessage(monitorTopic, Command.PhoneCrawleReadNumCount, "");
            } else if (response.contains("/mp/getmasssendmsg")) {
                busDao.sendMessage(monitorTopic, Command.PhoneCrawleNewArticleCount, "");
            } else if (response.contains("\"success\":false")) {
                busDao.sendMessage(monitorTopic, Command.PhoneIdleCount, "");
            }
        } catch (Exception e) {
            logger.error("", e);
        }
    }

}
