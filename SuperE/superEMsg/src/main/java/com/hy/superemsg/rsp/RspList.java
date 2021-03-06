package com.hy.superemsg.rsp;

import java.util.HashMap;
import java.util.Map;

public class RspList {
	// key = baseapi class name;value=path
	private static Map<String, String> map = new HashMap<String, String>();
	static {
		map.put("system/clientversion", RspClientVersion.class.getName());
		map.put("system/smspwd/send", RspSmsPwdSend.class.getName());
		map.put("system/smspwd/validate", RspSmsPwdValidate.class.getName());
		map.put("system/festival/remind", RspFestivalRemind.class.getName());
		map.put("system/mobile/getseg", RspMobileGetSeg.class.getName());
		map.put("system/xsmcallout", RspSystemXsmcallout.class.getName());
		map.put("sms/category", RspSmsCategory.class.getName());
		 map.put("sms/content/query", RspSmsContentQuery.class.getName());
		 map.put("sms/detail/query", RspSmsDetailQuery.class.getName());
		 map.put("mms/category", RspMmsCategory.class.getName());
		 map.put("mms/content/query", RspMmsContentQuery.class.getName());
		 map.put("mms/detail/query", RspMmsDetailQuery.class.getName());
		 map.put("holiday/category", RspHolidayCategory.class.getName());
		 map.put("holiday/content/query", RspHolidayContentQuery.class.getName());
		 map.put("holiday/detail/query", RspHolidayDetailQuery.class.getName());
		 map.put("ring/support", RspRingSupport.class.getName());
		 map.put("ring/category", RspRingCategory.class.getName());
		 map.put("ring/content/query", RspRingContentQuery.class.getName());
		 map.put("ring/detail/query", RspRingDetailQuery.class.getName());
		 map.put("ring/callout", RspRingCallOut.class.getName());
		 map.put("ring/addring", RspRingAddRing.class.getName());
		map.put("game/category", RspGameCategory.class.getName());
		 map.put("game/content/query", RspGameContentQuery.class.getName());
		 map.put("game/detail/query", RspGameDetailQuery.class.getName());
		 map.put("game/download/add", RspGameDownloadAdd.class.getName());
		 map.put("animation/category",RspAnimationCategory.class.getName());
		 map.put("animation/content/query",RspAnimationContentQuery.class.getName());
		 map.put("animation/detail/query",RspAnimationDetailQuery.class.getName());
		 map.put("news/category", RspNewsCategory.class.getName());
		 map.put("news/content/query", RspNewsContentQuery.class.getName());
		 map.put("news/focus/query", RspNewsFocusQuery.class.getName());
		 map.put("news/detail/query", RspNewsDetailQeury.class.getName());
	}

	public static String getClassName(String path) {
		return map.get(path);
	}
}
