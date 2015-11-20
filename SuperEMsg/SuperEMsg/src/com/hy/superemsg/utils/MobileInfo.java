package com.hy.superemsg.utils;

import java.util.Locale;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.os.Build;
import android.telephony.TelephonyManager;
import android.webkit.WebSettings;
import android.webkit.WebView;

public class MobileInfo {
	private static TelephonyManager getTelephonyManager(Context ctx) {
		return (TelephonyManager) ctx
				.getSystemService(Context.TELEPHONY_SERVICE);
	}

	public static String getMSISDN(Activity activity) {
		return getTelephonyManager(activity).getLine1Number();
	}

	public static String getIMEI(Activity activity) {
		return getTelephonyManager(activity).getDeviceId();
	}

	public static String getIMSI(Activity activity) {
		return getTelephonyManager(activity).getSubscriberId();
	}

	public static String getSIMSN(Context ctx) {
		return getTelephonyManager(ctx).getSimSerialNumber();
	}

	public static String getCountry(Activity activity) {
		return getTelephonyManager(activity).getSimCountryIso();
	}

	public static String getOperatorName(Activity activity) {
		return getTelephonyManager(activity).getSimOperatorName();
	}

	public static int getOperator(Activity activity) {
		return Integer.parseInt(getTelephonyManager(activity).getSimOperator());
	}

	public static int getSimState(Context ctx) {
		return getTelephonyManager(ctx).getSimState();
	}
	public static int getScreenWidth(Context ctx){
		return ctx.getResources().getDisplayMetrics().widthPixels;
	}
	public static int getScreenHeight(Context ctx){
		return ctx.getResources().getDisplayMetrics().heightPixels;
	}
	public static String getMobileModel() {
		return android.os.Build.MODEL;
	}

	@TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
	public static String getUA(Context ctx) {
		return WebSettings.getDefaultUserAgent(ctx);
	}

	public static String getSeparateIcon() {
		String manufacturer = android.os.Build.MANUFACTURER;
		if (manufacturer.toLowerCase(Locale.getDefault()).contains("htc")) {
			return ";";
		} else {
			return ",";
		}
	}
}
