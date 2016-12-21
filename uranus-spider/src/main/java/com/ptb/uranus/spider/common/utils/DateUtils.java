package com.ptb.uranus.spider.common.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * 日期类
 */
public class DateUtils {

	public final static String DATE_FORMAT_NORAL = "yyyy-MM-dd HH:mm:ss";
	public final static String DATE_FORMAT_MINUTE = "yyyy-MM-dd HH:mm";
	public final static String DATE_FORMAT_DATE = "yyyy-MM-dd";
	public final static String DATE_FORMAT_HOUR = "H";
	/**
	* 日期转换成字符串
	* @param date 
	* @return str
	*/
	public static String DateToStr(Date date, String pattern) {
	  
	   SimpleDateFormat format = new SimpleDateFormat(pattern);
	   String str = format.format(date);
	   return str;
	} 
	
	public static String DateToStr(Date date) {
		  
		   SimpleDateFormat format = new SimpleDateFormat(DATE_FORMAT_NORAL);
		   String str = format.format(date);
		   return str;
		} 

	/**
	* 字符串转换成日期
	* @param str
	* @return date
	*/
	public static Date StrToDate(String str, String pattern) {
	  
	   SimpleDateFormat format = new SimpleDateFormat(pattern);
	   Date date = null;
	   try {
	    date = format.parse(str);
	   } catch (ParseException e) {
	    e.printStackTrace();
	   }
	   return date;
	}
	
	public static Date dateAdd(int amount) {
		Calendar c = Calendar.getInstance();
		c.setTime(new Date());
		c.add(Calendar.DATE, amount);
		Date date = c.getTime();
		return date;
	}
	
	public static boolean isSameDate(Date date1, Date date2) {
        Calendar cal1 = Calendar.getInstance();
        cal1.setTime(date1);

        Calendar cal2 = Calendar.getInstance();
        cal2.setTime(date2);

        boolean isSameYear = cal1.get(Calendar.YEAR) == cal2
                .get(Calendar.YEAR);
        boolean isSameMonth = isSameYear
                && cal1.get(Calendar.MONTH) == cal2.get(Calendar.MONTH);
        boolean isSameDate = isSameMonth
                && cal1.get(Calendar.DAY_OF_MONTH) == cal2
                        .get(Calendar.DAY_OF_MONTH);

        return isSameDate;
    }
	
	public static boolean compareDate(Date date, String dateStr) {
        try {
        	Date d = StrToDate(dateStr, DATE_FORMAT_NORAL);
            if (date.getTime() > d.getTime()) {
            	return true;
            }
        } catch (Exception e) {
        }
        return false;
    }



	public static long getTime(String time) throws ParseException {
		Calendar c = Calendar.getInstance();
		if (time.contains("-")&& time.length()>14){
			Date date = DateUtils.StrToDate(time, DateUtils.DATE_FORMAT_MINUTE);
			long time1 = date.getTime();
			return time1;
		}else if(time.contains("-")&&time.length()<12){
			time=c.get(Calendar.YEAR)+"-"+time;
			Date date = DateUtils.StrToDate(time, DateUtils.DATE_FORMAT_MINUTE);
			long time1 = date.getTime();
			return time1;
		}else if (time.contains("分钟前")){
			time = time.replace("分钟前","");
			long hh =Long.parseLong(time)*60*1000;
			long l = System.currentTimeMillis() - hh;
			return l;
		}else if(time.contains("今天")){
			time = time.replace("今天","");
			String s = DateUtils.DateToStr(new Date(), DateUtils.DATE_FORMAT_DATE)+" "+time;
			Date date = DateUtils.StrToDate(s, DateUtils.DATE_FORMAT_MINUTE);
			return date.getTime();
		}
		return 0;
	}

}
