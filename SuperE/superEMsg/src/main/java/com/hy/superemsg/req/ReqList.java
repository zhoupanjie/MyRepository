package com.hy.superemsg.req;

import java.util.HashMap;
import java.util.Map;

public class ReqList {
	// key = baseapi class name;value=path
	private static Map<String, String> map = new HashMap<String, String>();
	static {
		map.put(ReqClientVersion.class.getSimpleName(), "system/clientversion");
		map.put(ReqSmsPwdSend.class.getSimpleName(), "system/smspwd/send");
		map.put(ReqSmsPwdValidate.class.getSimpleName(),
				"system/smspwd/validate");
		map.put(ReqFestivalRemind.class.getSimpleName(),
				"system/festival/remind");
		map.put(ReqMobileGetSeg.class.getSimpleName(), "system/mobile/getseg");
		map.put(ReqSystemXsmcallout.class.getSimpleName(), "system/xsmcallout");
		map.put(ReqSmsCategory.class.getSimpleName(), "sms/category");
		map.put(ReqSmsContentQuery.class.getSimpleName(), "sms/content/query");
		map.put(ReqSmsDetailQuery.class.getSimpleName(), "sms/detail/query");
		map.put(ReqMmsCategory.class.getSimpleName(), "mms/category");
		map.put(ReqMmsContentQuery.class.getSimpleName(), "mms/content/query");
		map.put(ReqMmsDetailQuery.class.getSimpleName(), "mms/detail/query");
		map.put(ReqHolidayCategory.class.getSimpleName(), "holiday/category");
		map.put(ReqHolidayContentQuery.class.getSimpleName(), "holiday/content/query");
		map.put(ReqHolidayDetailQuery.class.getSimpleName(), "holiday/detail/query");
		map.put(ReqRingSupport.class.getSimpleName(), "ring/support");
		map.put(ReqRingCategory.class.getSimpleName(), "ring/category");
		map.put(ReqRingContentQuery.class.getSimpleName(), "ring/content/query");
		map.put(ReqRingDetailQuery.class.getSimpleName(), "ring/detail/query");
		map.put(ReqRingCallOut.class.getSimpleName(), "ring/callout");
		map.put(ReqRingAddRing.class.getSimpleName(), "ring/addring");
		map.put(ReqGameCategory.class.getSimpleName(), "game/category");
		map.put(ReqGameContentQuery.class.getSimpleName(), "game/content/query");
		map.put(ReqGameDetailQuery.class.getSimpleName(), "game/detail/query");
		map.put(ReqGameDownloadAdd.class.getSimpleName(), "game/download/add");
		map.put(ReqAnimationCategory.class.getSimpleName(),
				"animation/category");
		map.put(ReqAnimationContentQuery.class.getSimpleName(),
				"animation/content/query");
		map.put(ReqAnimationDetailQuery.class.getSimpleName(),
				"animation/detail/query");
		map.put(ReqNewsCategory.class.getSimpleName(), "news/category");
		map.put(ReqNewsContentQuery.class.getSimpleName(), "news/content/query");
		map.put(ReqNewsFocusQuery.class.getSimpleName(), "news/focus/query");
		map.put(ReqNewsDetailQeury.class.getSimpleName(), "news/detail/query");
	}

	public static String getPath(String apiClassName) {
		return map.get(apiClassName);
	}
}
