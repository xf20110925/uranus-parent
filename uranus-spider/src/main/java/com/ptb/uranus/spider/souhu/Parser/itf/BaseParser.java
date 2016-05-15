package com.ptb.uranus.spider.souhu.Parser.itf;


import com.ptb.uranus.spider.souhu.bean.NewsInfo;
import com.ptb.uranus.spider.souhu.utils.CommonUtil;
import org.apache.commons.lang.StringUtils;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Administrator on 2016/3/15.
 */
public abstract class BaseParser {
    public abstract NewsInfo getNewsInfo(String url, String html);
    public abstract Map<String, Integer> getJsInfo(String url, String html);

    /**
     * 截取字符串中的参与数和阅读数
     * @param jsInfo
     * @return
     */
    public Map<String, Integer> getRPNum(String jsInfo){
        jsInfo = CommonUtil.extractStr(jsInfo,".*?(\"participation_sum\":\\d+.*?\"outer_cmt_sum\":\\d+).*?");
        String participationNumStr = CommonUtil.extractStr(jsInfo, "\"participation_sum\":(\\d+).*?");
        Integer participationNum = StringUtils.isBlank(participationNumStr) ? null : Integer.parseInt(participationNumStr);
        String commentNumStr = CommonUtil.extractStr(jsInfo, "\"outer_cmt_sum\":(\\d+)");
        int  commentNum = StringUtils.isBlank(commentNumStr) ? null : Integer.parseInt(commentNumStr);
        Map<String, Integer> dataMap = new HashMap<>();
        dataMap.put("participationNum", participationNum);
        dataMap.put("commentNum", commentNum);
        return dataMap;
    }


}
