package com.cplatform.xhxw.ui.util;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

/**
 * Created by cy-love on 14-1-5.
 */
public class DateUtil {

    public static long getTime() {
        return new Date().getTime();
    }

    public static long getUnixTime() {
        return new Date().getTime() / 1000;
    }

    public static String getXHAPPDetailFormmatString(long data) {
    	if(data <= 0) {
    		return "";
    	}
        long curTS = getTime();
        long selfTS = data;
        long diffTS = curTS - selfTS;
        diffTS = diffTS / 1000;

        if (diffTS <= 1) {
            return "刚刚";
        } else if (diffTS < 60) {// 1分钟内
            return String.format("%d秒前", diffTS);
        } else if (diffTS >= 60 && diffTS < 3600) {
            return String.format("%d分钟前", (diffTS / 60));
        } else if (diffTS >= 3600 && diffTS < 86400) {// 24小时内
            return String.format("%d小时前", (diffTS / 3600));
        } else { // 大于24小时
            Date dt = new Date(data);
            SimpleDateFormat sdf = new SimpleDateFormat("MM月dd日");
            return sdf.format(dt);
        }
    }

    public static String getSMessageChatFormmatString(long unixTime) {
        Calendar ca = Calendar.getInstance();
        int year = ca.get(Calendar.YEAR);
        int month = ca.get(Calendar.MONTH);
        int day = ca.get(Calendar.DAY_OF_MONTH);

        ca.setTimeInMillis(unixTime * 1000);

        int yearTmp = ca.get(Calendar.YEAR);
        int monthTmp = ca.get(Calendar.MONTH);
        int dayTmp = ca.get(Calendar.DAY_OF_MONTH);

        if (year == yearTmp && month == monthTmp && day == dayTmp) {
            SimpleDateFormat formatter = new SimpleDateFormat("HH:ss");
            return formatter.format(ca.getTime());
        }
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy年MM月dd日 HH:ss");
        return formatter.format(ca.getTime());
    }
    
    /**
     * 获取格式化后的时间字符串
     * @param date 精确到秒
     * @return ××月××日
     */
    public static String getFormattedDate(long date) {
    	StringBuilder sb = new StringBuilder();
    	Calendar ca = Calendar.getInstance();
    	ca.setTimeInMillis(date * 1000);
    	ca.setTimeZone(TimeZone.getDefault());
    	
    	sb.append(ca.get(Calendar.MONTH) + 1).append("月");
    	sb.append(ca.get(Calendar.DAY_OF_MONTH)).append("日");
    	return sb.toString();
    }
    
    /**
     * 获取格式化后的生日时间字符串
     * @param date 精确到秒
     * @return 年-月-日
     */
    public static String getFormattedBirthday(long date) {
    	StringBuilder sb = new StringBuilder();
    	Calendar ca = Calendar.getInstance();
    	ca.setTimeInMillis(date * 1000);
    	ca.setTimeZone(TimeZone.getDefault());
    	
    	sb.append(ca.get(Calendar.YEAR)).append("-");
    	sb.append(ca.get(Calendar.MONTH) + 1).append("-");
    	sb.append(ca.get(Calendar.DAY_OF_MONTH));
    	return sb.toString();
    }
    
    /**
     * 获取格式化后的时间字符串
     * @param date 精确到秒
     * @return 月-日  h:m
     */
    public static String getCommentFormattedDate(long date) {
    	StringBuilder sb = new StringBuilder();
    	Calendar ca = Calendar.getInstance();
    	ca.setTimeInMillis(date * 1000);
    	ca.setTimeZone(TimeZone.getDefault());
    	
    	sb.append(ca.get(Calendar.MONTH) + 1).append("-");
    	sb.append(ca.get(Calendar.DAY_OF_MONTH)).append(" ");
    	sb.append(ca.get(Calendar.HOUR_OF_DAY)).append(":");
    	sb.append(ca.get(Calendar.MINUTE));
    	return sb.toString();
    }
    
    /**
     * 获取特定日期的秒
     * @param date XXXX-XX-XX-hour-minute-second
     * @return 
     */
    public static long getTimeFromDate(String date) {
    	if(date == null) {
    		return 0;
    	}
    	String[] peice = date.split("-");
    	int year = Integer.valueOf(peice[0]);
    	int month = Integer.valueOf(peice[1]) - 1;
    	int day = Integer.valueOf(peice[2]);
    	int hour = peice.length > 3 ? Integer.valueOf(peice[3]) : 0;
    	int minute = peice.length > 4 ? Integer.valueOf(peice[4]) : 0;
    	int second = peice.length > 5 ? Integer.valueOf(peice[5]) : 0;
    	
    	Calendar ca = Calendar.getInstance();
    	ca.clear();
    	ca.set(year, month, day, hour, minute, second);
    	return ca.getTimeInMillis() / 1000;
    }
    
    /**
     * 获取格式化后的当前时间，格式为：hour:minute
     * @return
     */
    public static String formatCurrentTime() {
    	StringBuilder sb = new StringBuilder();
    	Calendar ca = Calendar.getInstance();
    	ca.setTimeInMillis(System.currentTimeMillis());
    	ca.setTimeZone(TimeZone.getDefault());
    	
    	sb.append(ca.get(Calendar.HOUR_OF_DAY)).append(":");
    	sb.append(ca.get(Calendar.MINUTE));
    	return sb.toString();
    }

    /**
     * 获得年月日
     * @param time 时间戳 单位秒
     * @return  yyyy-MM-dd
     */
    public static String get_YYYY_MM_DD(long time) {
        Date currentTime = new Date(time);
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        String dateString = formatter.format(currentTime);
        return dateString;
    }

    /**
     * 获得年月日
     * @return  yyyy-MM-dd
     */
    public static String get_YYYY_MM_DD() {
        return get_YYYY_MM_DD(getTime());
    }
}
