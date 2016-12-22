package com.ptb.uranus.sdk;

import com.ptb.uranus.common.entity.CollectType;
import com.ptb.uranus.schedule.Collector;
import com.ptb.uranus.schedule.dao.MongoSchedulerDao;
import com.ptb.uranus.schedule.dao.SchedulerDao;
import com.ptb.uranus.schedule.model.Priority;
import com.ptb.uranus.schedule.model.SchedulableCollectCondition;
import com.ptb.uranus.schedule.model.ScheduleObject;
import com.ptb.uranus.schedule.service.WeiboScheduleService;
import com.ptb.uranus.schedule.service.WeixinScheduleService;
import com.ptb.uranus.schedule.trigger.Trigger;
import com.ptb.uranus.sdk.exception.CollectArgsException;
import com.ptb.uranus.sdk.exception.ConfigureFileException;
import com.ptb.uranus.spider.weixin.WeixinSpider;
import com.ptb.utils.web.UrlFormatUtil;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by eric on 16/4/21.
 */
public class UranusSdk implements Collector {
    static Logger logger = LoggerFactory.getLogger(UranusSdk.class);
    static private UranusSdk instance;
    WeixinSpider weixinSpider = new WeixinSpider();

    static String lock = new String();

    SchedulerDao schedulerDao = null;
    WeixinScheduleService weixinScheduleService = new WeixinScheduleService();
    WeiboScheduleService weiboScheduleService = new WeiboScheduleService();

    public static UranusSdk i() {
        if (instance == null) {
            synchronized (lock) {
                if (instance == null) {
                    try {
                        instance = new UranusSdk();
                    } catch (ConfigurationException e) {
                        throw new ConfigureFileException(e);
                    }
                }
            }
        }
        return instance;
    }

    private UranusSdk() throws ConfigurationException {
        schedulerDao = new MongoSchedulerDao();
    }


    public String getCorrectCondition(String srcCond, CollectType collectType) {
        String cond = null;
        String finalCondition = null;
        if ((collectType.getCode() == CollectType.C_WX_M_S.getCode()) || (collectType.getCode() == CollectType.C_WX_M_D.getCode())) {
            if (!srcCond.startsWith("http://") && !srcCond.startsWith("https://")) {
                cond = weixinSpider.getWeixinArticleUrlById(srcCond);
            } else {
                cond = srcCond;
            }
        } else {
            cond = srcCond;
        }

        if ((collectType.getCode() == CollectType.C_WX_A_N.getCode())) {

            if (srcCond.contains(":::")) {
                cond = srcCond.split(":::")[0];
            }

            try {
                Long.parseLong(new String(Base64.decodeBase64(cond)));
            } catch (Exception e) {
                logger.warn(String.format("error condition [%s]", cond), e);
                return null;
            }

        }
        finalCondition = UrlFormatUtil.format(cond);

        return finalCondition;
    }

    @Override
    public String collect(String srcCond, CollectType collectType, Trigger trigger, Priority priority) {

        try {
            String finalCondition = getCorrectCondition(srcCond, collectType);
            if (StringUtils.isBlank(finalCondition) || finalCondition == null || collectType == null || priority == null || trigger == null) {
                throw new CollectArgsException(String.format("输入的参数不正确 type:[%s]  inputValue[%s]", collectType, finalCondition));
            }
            ScheduleObject scheduleObject = new ScheduleObject(trigger, priority, new SchedulableCollectCondition(collectType, finalCondition));


            if (collectType == CollectType.C_WX_A_N) {
                weixinScheduleService.addMediaStaticSchedule(srcCond);
                return finalCondition;
            }
            if (!schedulerDao.addCollScheduler(scheduleObject)) {
                return null;
            } else {
                return finalCondition;
            }
        } catch (Exception e) {
            logger.warn(String.format("cond [%s] type [%s] trgger[%s] trigger[%s]", srcCond, collectType, trigger, priority), e);
        }

        return null;
    }

    @Override
    public List<String> collect(List<String> srcConds, CollectType collectType, Trigger trigger, Priority
            priority) {
        try {
            List<String> finalConditions = srcConds.stream().map(srcCond -> getCorrectCondition(srcCond, collectType)).filter(cond->StringUtils.isNotBlank(cond)).collect(Collectors.toList());
            if (collectType == null || finalConditions == null || collectType == null || priority == null || trigger == null) {
                throw new CollectArgsException(String.format("输入的参数不正确 type:[%s]  inputValue[%s]", collectType, finalConditions));
            }
            List<ScheduleObject> collects = finalConditions.stream().map(fc -> new ScheduleObject(trigger, priority, new SchedulableCollectCondition(collectType, fc))).
                    collect(Collectors.toList());


            if (collectType == CollectType.C_WX_A_N) {
                srcConds.forEach(a -> {
                    weixinScheduleService.addMediaStaticSchedule(a);
                });

                return finalConditions;
            }

            if (schedulerDao.addCollSchedulers(collects)) {
                return finalConditions;
            }
        } catch (Exception e) {
            logger.warn(String.format("cond [%s] type [%s] ", srcConds, collectType), e);
        }

        return null;
    }

    /*
        建议使用getCorrectCondition
     */
    @Deprecated
    public String urlFormat(String url) {
        return UrlFormatUtil.format(url);
    }


}
