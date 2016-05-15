package com.ptb.uranus.scheduler;

import com.ptb.uranus.schedule.service.CommonMediaScheduleService;
import org.apache.commons.configuration.ConfigurationException;
import org.apache.log4j.Logger;

import java.util.TimerTask;

/**
 * Created by eric on 16/5/9.
 */
public class UpdateCommonArtcileListTask extends TimerTask {
    static Logger logger = Logger.getLogger(UpdateCommonArtcileListTask.class);
    CommonMediaScheduleService commonMediaScheduleService;


    public UpdateCommonArtcileListTask() throws ConfigurationException {
        commonMediaScheduleService = new CommonMediaScheduleService();
    }

    @Override
    public void run() {
        logger.info("更新爬虫配置媒体文章入口");
        commonMediaScheduleService.updateCommonAritclesEntrys();
    }
}
