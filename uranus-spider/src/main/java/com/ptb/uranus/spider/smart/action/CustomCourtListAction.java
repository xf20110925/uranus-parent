package com.ptb.uranus.spider.smart.action;

import com.alibaba.fastjson.JSONObject;
import com.ptb.uranus.spider.court.CourtSpider;
import com.ptb.uranus.spider.smart.Context;
import com.ptb.uranus.spider.smart.entity.SpiderConstant;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.json.JSONException;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * Created by eric on 16/4/11.
 */
public class CustomCourtListAction extends Action {

    @Override
    public Boolean execute(Context context) throws Exception {
        Calendar instance = Calendar.getInstance(Locale.CHINA);
        instance.add(Calendar.DATE, -1);
        Date yesterday = instance.getTime();
        Date today = new Date();

        List<String> articleUrlsByCourt = CourtSpider.getArticleUrlsByCourt(yesterday, today);
        context.set(SpiderConstant.NEWURLS, articleUrlsByCourt);
        context.set(SpiderConstant.LASTPOSTTIME, DateFormatUtils.format(new Date(), "yyyy-MM-dd HH:mm:ss", Locale.CHINA));
        return null;
    }

    public CustomCourtListAction(JSONObject jsonParam) throws JSONException {
        super(jsonParam);
    }
}
