package com.ptb.uranus.spider.souhu.utils;

import org.apache.commons.lang.StringUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by xuefeng on 2016/3/11.
 */
public class CommonUtil {

    public static String Date2Str(Date date) {
        return Date2Str(date, "yyyyMMdd");
    }

    public static String Date2Str(Date date, String partern) {
        SimpleDateFormat formater = new SimpleDateFormat(partern);
        return formater.format(date);
    }

    public static Date Str2Date(String dateStr) {
        return Str2Date(dateStr, "yyyyMMdd");
    }

    public static Date Str2Date(String dateStr, String partern) {
        SimpleDateFormat formater = new SimpleDateFormat(partern);
        try {
            Date date = formater.parse(dateStr);
            return date;
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static Date addDays(Date date, int amount) {
        if (date != null) {
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(date);
            calendar.add(Calendar.DAY_OF_MONTH, amount);
            return calendar.getTime();
        }
        return null;
    }

    /**
     * 返回当前日期
     *
     * @return
     * @throws ParseException
     */
    public static Date getCurrentDate() {
        return Str2Date(Date2Str(new Date()));
    }

    public static String extractStr(String text, String regex) {
        if (StringUtils.isNotBlank(text) && StringUtils.isNotBlank(regex)) {
            Pattern p = Pattern.compile(regex);
            Matcher matcher = p.matcher(text);
            if (matcher.find()) {
                return matcher.group(1);
            }
        }
        return null;
    }

}
