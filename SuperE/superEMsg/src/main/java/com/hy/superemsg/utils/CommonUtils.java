package com.hy.superemsg.utils;

import java.util.Calendar;
import java.util.Collection;
import java.util.Map;

public class CommonUtils {
	public static <T> boolean isNotEmpty(Collection<T> datas) {
		return datas != null && datas.size() > 0;
	}

	public static <T> boolean isNotEmpty(Map<? extends Object, T> datas) {
		return datas != null && datas.size() > 0;
	}

	public static String getTimeString(Calendar c) {
		return c.get(Calendar.YEAR) + "-" + (c.get(Calendar.MONTH) + 1) + "-"
				+ c.get(Calendar.DATE);
	}

}
