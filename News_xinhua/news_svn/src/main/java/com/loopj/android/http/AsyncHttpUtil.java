package com.loopj.android.http;

import com.cplatform.xhxw.ui.util.LogUtil;

public class AsyncHttpUtil {

	public static void log(String tag, String logText) {
        LogUtil.d(tag, logText+"");
	}
	
	public static void w(String tag, Throwable e) {
        LogUtil.w(tag, e);
	}
}
