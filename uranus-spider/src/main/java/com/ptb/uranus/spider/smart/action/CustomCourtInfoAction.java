package com.ptb.uranus.spider.smart.action;

import com.alibaba.fastjson.JSONObject;
import com.ptb.uranus.spider.court.CourtCase;
import com.ptb.uranus.spider.court.CourtSpider;
import com.ptb.uranus.spider.smart.Context;
import com.ptb.uranus.spider.smart.entity.SpiderConstant;
import org.json.JSONException;

/**
 * Created by eric on 16/4/11.
 */
public class CustomCourtInfoAction extends Action {

    public CustomCourtInfoAction(JSONObject jsonParam) throws JSONException {
        super(jsonParam);
    }

    @Override
    public Boolean execute(Context context) throws Exception {
        String url = (String) context.get(SpiderConstant.URL);

        CourtCase courtCase = CourtSpider.getCourtCaseByUrl(url);
        context.set(SpiderConstant.PLATFORM, "wenshu");
        context.set(SpiderConstant.TITLE, courtCase.getCaseTitle());
        context.set(SpiderConstant.CATEGORY, courtCase.getCaseType());
        context.set(SpiderConstant.SOURCE, courtCase.getCaseCourt());
        context.set(SpiderConstant.POSTTIME, String.format("%s 00:00:00", courtCase.getPostTime()));
        context.set(SpiderConstant.READNUM, courtCase.getReadNum());
        context.set(SpiderConstant.CONTENT, courtCase.getCaseContent());
        context.set(SpiderConstant.COURTCASE, courtCase);
        return null;
    }


}
