package com.hy.superemsg.utils;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class ConvertUtils {
	public final static String TAG = ConvertUtils.class.getSimpleName();

	public static int String2Int(String str, int defaultInt) {

		try {
			int num = Integer.parseInt(str);
			return num;
		} catch (NumberFormatException e) {
			e.printStackTrace();
		}
		return defaultInt;
	}

	public static <T> List<T> mapKey2List(Map<T, ? extends Object> map) {
		Iterator<T> iter = map.keySet().iterator();
		List<T> ret = new ArrayList<T>();
		while (iter.hasNext()) {
			T key = iter.next();
			ret.add(key);
		}
		return ret;

	}

	public static <T> List<T> mapValues2List(Map<? extends Object, T> map) {
		Iterator<T> iter = map.values().iterator();
		List<T> ret = new ArrayList<T>();
		while (iter.hasNext()) {
			T key = iter.next();
			ret.add(key);
		}
		return ret;

	}
}
