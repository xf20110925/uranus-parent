package com.ptb.uranus.spider.smart;

import com.alibaba.fastjson.JSON;
import com.ptb.uranus.spider.common.webDriver.WebDriverPoolUtils;
import com.ptb.uranus.spider.smart.entity.DynamicData;
import com.ptb.uranus.spider.smart.utils.SmartSpiderConverter;
import org.apache.log4j.Logger;

import java.util.*;

/**
 * Created by eric on 16/3/23.
 */
public class SmartSpider {
    private static Logger logger = Logger.getLogger(SmartSpider.class);
    List<ActionSet> actionSetList = new ArrayList<ActionSet>();

    Map<String, List<ActionSet>> listMap = new HashMap<>();

    public SmartSpider() {
        try {
            List<ActionSet> instance = SpiderConfig.getInstance();
            instance.forEach(actionSet -> {
                List<ActionSet> list;
                if (listMap.containsKey(actionSet.getCrawleType())) {
                    list = listMap.get(actionSet.getCrawleType());
                } else {
                    list = new ArrayList<ActionSet>();
                    listMap.put(actionSet.getCrawleType(), list);
                }
                list.add(actionSet);
            });
        } catch (Exception e) {
            logger.error("init actions from mongodb error:" + e.getMessage(), e);
        }
    }

    public Optional<SpiderResult> crawl(String url, String claweType) {
        List<ActionSet> actionSetList = listMap.get(claweType);
        if (actionSetList == null) {
            logger.warn(String.format("没有匹配爬去类型 [%s]", claweType));
            return Optional.empty();
        }

        Context context = new Context();
        boolean isMatch = false;
        for (int i = 0; i < actionSetList.size(); i++) {
            ActionSet actionSet = actionSetList.get(i);
            try {
                if (actionSet.match(url, claweType)) {
                    isMatch = true;
                    actionSet.execute(context, url, claweType);
                }
            } catch (Exception e) {
                logger.error(String.format("url [%s] claweType[%s] actionType [%s]", url, claweType, actionSet),
                        e);
            }
        }

        if (isMatch == true) {
            return Optional.of(new SpiderResult(context));
        }

        return Optional.empty();
    }

    public void close() {
        WebDriverPoolUtils.instance().closeAll();
    }

    public static void main(String[] args) {
//        Optional<SpiderResult> article = new SmartSpider().crawl("http://roll.sohu.com/", "articleList");
//        System.out.println(article.get().executeContext.get("NEWURLS"));

//        Optional<SpiderResult> articles = new SmartSpider().crawl("http://roll.sohu.com", "articleList");
//        NewScheduleUrls newScheduleUrls = SmartSpiderConverter.convertToNewSchedulerUrls(-1L, articles.get());
//        System.out.println(JSON.toJSONString(newScheduleUrls));
//
        Optional<SpiderResult> articleDynamicData = new SmartSpider().crawl("http://finance.sina.com.cn/stock/usstock/c/20160406/041724536277.shtml", "articleDynamicData");
        DynamicData dynamicData = SmartSpiderConverter.convertToDynamicData(articleDynamicData.get());
        System.out.println(JSON.toJSONString(dynamicData));

    }
}
