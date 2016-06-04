package com.ptb.uranus.tools;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by xuefeng on 2016/6/4.
 */
public class LogTools {
    final static Logger logger = LoggerFactory.getLogger("key.error");


    public static void log(String appName, String actionInfo, String actionResult, String errorInfo) {
        String message = String.join("\t", Long.toString(System.currentTimeMillis() / 1000), appName, actionInfo, actionResult, errorInfo);
        logger.error(message);
    }

    public static void main(String[] args) {
        log("uranus-schedule", "from-mongo", "failed", "connect-mongo-failed");
    }
}
