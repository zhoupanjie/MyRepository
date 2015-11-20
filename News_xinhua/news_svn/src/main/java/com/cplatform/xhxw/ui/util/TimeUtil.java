package com.cplatform.xhxw.ui.util;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import android.annotation.SuppressLint;
import android.text.TextUtils;

/**
 * 时间工具类
 * 
 * @author way
 * 
 */
@SuppressLint("SimpleDateFormat")
public class TimeUtil {

	public static String getTime(long time) {
		SimpleDateFormat sdf_day = new SimpleDateFormat("dd");
		Date today = new Date(System.currentTimeMillis());
		Date otherDay = new Date(time);
		int temp = Integer.parseInt(sdf_day.format(today))
				- Integer.parseInt(sdf_day.format(otherDay));
		if (temp == 0) {
			return getHourAndMin(time);
		}
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm");
		return format.format(new Date(time));
	}

	public static String getDate(long time) {
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		return format.format(new Date(time));
	}

	public static String getMonthDay(long time) {
		SimpleDateFormat format = new SimpleDateFormat("MM-dd");
		return format.format(new Date(time));
	}

	public static String getHourAndMin(long time) {
		SimpleDateFormat format = new SimpleDateFormat("HH:mm");
		return format.format(new Date(time));
	}

	public static String getNoticeTime(long timesamp) {
		String result = "";
		SimpleDateFormat sdf_day = new SimpleDateFormat("dd");
		Date today = new Date(System.currentTimeMillis());
		Date otherDay = new Date(timesamp);
		int temp = Integer.parseInt(sdf_day.format(today))
				- Integer.parseInt(sdf_day.format(otherDay));
		SimpleDateFormat sdf_week = new java.text.SimpleDateFormat("EEEE");
		String day = sdf_week.format(otherDay);
		SimpleDateFormat sdf_year = new SimpleDateFormat("yyyy");
		int year_difference = Integer.parseInt(sdf_year.format(today))
				- Integer.parseInt(sdf_year.format(otherDay));
		Calendar calendar = Calendar.getInstance();
		int week = calendar.get(Calendar.DAY_OF_WEEK) - 1;
		if (temp < week && temp > 1) {
			// 在本周,且不是昨天今天,显示周几
			return day;
		}
		switch (temp) {
		case 0:
			result = getHourAndMin(timesamp);
			break;
		case 1:
			result = "昨天";
			break;
		// case 2:
		// result = "前天";
		// break;

		default:
			// result = temp + "天前 ";

			if (year_difference == 0) {
				result = getMonthDay(timesamp);
			} else {
				result = getDate(timesamp);
			}

			break;
		}

		return result;
	}

	public static String getChatTime(String timesamp) {
		if (TextUtils.isEmpty(timesamp)) {
			return "";
		}
		timesamp = timesamp + "000";
		long time = 0;
		try {
			time = Long.parseLong(timesamp);
		} catch (Exception e) {
			return "";
		}
		String Hour_min = getHourAndMin(time);
		String result = "";
		SimpleDateFormat sdf_day = new SimpleDateFormat("dd");
		Date today = new Date(System.currentTimeMillis());
		Date otherDay = new Date(time);
		int temp = Integer.parseInt(sdf_day.format(today))
				- Integer.parseInt(sdf_day.format(otherDay));
		if (temp <= 7 && temp > 0) {
			// 在7天内,且不是当天,显示x天前
			return temp + "天前";
		}
		switch (temp) {
		case 0:
			result = Hour_min;
			break;
		default:
			// if (year_difference == 0) {
			// result = getMonthDay(timesamp);
			// } else {
			result = getDate(time) + " " + Hour_min;
			// }

			break;
		}
		return result;
	}

	public static String getChatTimeAll(long timesamp) {
		String Hour_min = getHourAndMin(timesamp);
		String result = "";
		SimpleDateFormat sdf_day = new SimpleDateFormat("dd");
		Date today = new Date(System.currentTimeMillis());
		Date otherDay = new Date(timesamp);
		int temp = Integer.parseInt(sdf_day.format(today))
				- Integer.parseInt(sdf_day.format(otherDay));
		SimpleDateFormat sdf_week = new java.text.SimpleDateFormat("EEEE");
		String day = sdf_week.format(otherDay);
		SimpleDateFormat sdf_year = new SimpleDateFormat("yyyy");
		int year_difference = Integer.parseInt(sdf_year.format(today))
				- Integer.parseInt(sdf_year.format(otherDay));
		Calendar calendar = Calendar.getInstance();
		int week = calendar.get(Calendar.DAY_OF_WEEK) - 1;
		if (temp < week && temp > 1) {
			// 在本周,且不是昨天今天,显示周几
			return day + " " + Hour_min;
		}
		switch (temp) {
		case 0:
			result = "";
			break;
		case 1:
			result = "昨天";
			break;
		// case 2:
		// result = "前天";
		// break;

		default:
			// result = temp + "天前 ";
			result = getDate(timesamp);
			break;
		}
		result = result + " " + Hour_min;
		return result;
	}
}
