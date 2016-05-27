package com.ptb.uranus.spider.smart.utils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.ptb.uranus.spider.smart.Context;
import com.ptb.uranus.spider.smart.SpiderResult;
import com.ptb.uranus.spider.smart.entity.Article;
import com.ptb.uranus.spider.smart.entity.DynamicData;
import com.ptb.uranus.spider.smart.entity.NewScheduleUrls;
import com.ptb.uranus.spider.smart.entity.SpiderConstant;
import org.slf4j.Logger;
import org.bson.Document;
import org.slf4j.LoggerFactory;

import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by eric on 16/3/25.
 */
public class SmartSpiderConverter {
    static Logger logger = LoggerFactory.getLogger(SmartSpiderConverter.class);
    //加载mongodb平台、id对应表数据
    static JSONObject platforms = null;

    public static void initPlatform() {
        Document document = MongoDBUtil.instance.getDefaultDB().getCollection(MongoDBUtil.CollPlatform).find().first();
        platforms = JSON.parseObject(document.toJson());
    }

    public static String getContextString(Context context, String key) {
        Object o = context.get(key);
        if (o == null) {
            return "";
        }

        if (o instanceof List) {

            List<String> o1 = (List) o;
            if (o1.size() > 0) {
                for (String s : o1) {
                    if (s.length() > 0) {
                        return s;
                    }
                }
            }
            return "";
        } else {
            return o.toString();
        }
    }

    public static List<String> getContextList(Context context, String key) {
        Object o = context.get(key);
        if (o == null) {
            return new ArrayList<>();
        }

        if (o instanceof List) {
            List<String> o1 = (List) o;
            return o1;
        } else {
            return new ArrayList<>();
        }

    }

    private static SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.CHINESE);

    public static Article convertToArticle(SpiderResult spiderResult) {
        Article article = new Article();
        Context context = spiderResult.executeContext;

        article.setAuthor(getContextString(context, SpiderConstant.AUTHOR));
        article.setClassify(getContextString(context, SpiderConstant.CATEGORY));
        article.setContent(getContextString(context, SpiderConstant.CONTENT));
        article.setUrl(getContextString(context, SpiderConstant.URL));

        article.setTitle(getContextString(context, SpiderConstant.TITLE));
        article.setSource(getContextString(context, SpiderConstant.SOURCE));

        try {
            initPlatform();
            //平台名称
            String platformName = getContextString(context, SpiderConstant.PLATFORM);
            article.setPlat(Integer.parseInt(platforms.getString(platformName)));
        } catch (Exception e) {
            logger.warn(String.format("平台值不合法 spider[%s]", article.getUrl()));
        }


        try {
            article.setPostTime(dateFormat.parse(getContextString(context, SpiderConstant.POSTTIME)).getTime() / 1000);
        } catch (Exception e) {
            article.setPostTime(new Date().getTime() / 1000);
            logger.warn(String.format("没有得到发布时间 spiderResult:[%s],context:[%s]", article.getUrl(), context.get(SpiderConstant.POSTTIME).toString()));
        }

        //设置爬取到的时间
        article.setTime(System.currentTimeMillis() / 1000);

        return article;
    }

    public static NewScheduleUrls convertToNewSchedulerUrls(Long lastPostTime, SpiderResult spiderResult) {

        NewScheduleUrls newScheduleUrls = new NewScheduleUrls();
        Context context = spiderResult.executeContext;
        List<String> postTimes = getContextList(context, SpiderConstant.LASTPOSTTIME);

        Optional<Long> s = postTimes.stream().map(date -> {
            try {
                return dateFormat.parse(date).getTime() / 1000;
            } catch (Exception e) {
                logger.warn(String.format("转换时间失败,将使用当前时间 [%s]:", getContextString(context, SpiderConstant.URL)));
                return 0L;
            }
        }).reduce((k1, k2) -> {
            return k1 > k2 ? k1 : k2;
        });

        if (s.isPresent() && s.get() > 0) {
            newScheduleUrls.setLastPostTime(s.get());
        } else {
            if (lastPostTime > 1) {
                newScheduleUrls.setLastPostTime(lastPostTime);
            } else {
                newScheduleUrls.setLastPostTime(System.currentTimeMillis() / 1000);

            }
        }

        newScheduleUrls.setUrls(getContextList(context, SpiderConstant.NEWURLS));

        return newScheduleUrls;
    }

    public static Integer str2Integer(String str) {
        try {
            return Integer.parseInt(str);
        } catch (Exception e) {
            return -1;
        }
    }

    public static DynamicData convertToDynamicData(SpiderResult spiderResult) {

        DynamicData dynamicData = new DynamicData();
        Context context = spiderResult.executeContext;
        dynamicData.setBoringNum(str2Integer(getContextString(context, SpiderConstant.BORINGNUM)));
        dynamicData.setCommentNum(str2Integer(getContextString(context, SpiderConstant.COMMENTNUM)));
        dynamicData.setInvolvementNum(str2Integer(getContextString(context, SpiderConstant.INVOLVEMENTNUM)));
        dynamicData.setLikeNum(str2Integer(getContextString(context, SpiderConstant.LIKENUM)));
        dynamicData.setReadNum(str2Integer(getContextString(context, SpiderConstant.READNUM)));
        dynamicData.setShareNum(str2Integer(getContextString(context, SpiderConstant.SHARENUM)));
        try {
            initPlatform();
            //平台名称
            String platformName = getContextString(context, SpiderConstant.PLATFORM);
            dynamicData.setPlat(Integer.parseInt(platforms.getString(platformName)));
        } catch (Exception e) {
            logger.warn(String.format("平台值不合法 spider[%s]", getContextString(context, SpiderConstant.PLATFORM)));
        }
        return dynamicData;
    }


}
