package com.cplatform.xhxw.ui.util;

import android.content.Context;
import android.util.DisplayMetrics;
import android.util.Log;

public class DensityUtil {
	/**
	 * dip转换像素px
	 */
	public static int dip2px(Context context, float dpValue) {
		try {
			final float scale = context.getResources().getDisplayMetrics().density;
			return (int) (dpValue * scale + 0.5f);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return (int) dpValue;
	}

	/**
	 * 像素px转换为dip
	 */
	public static int px2dip(Context context, float pxValue) {
		try {
			final float scale = context.getResources().getDisplayMetrics().density;
			return (int) (pxValue / scale + 0.5f);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return (int) pxValue;
	}
	
	public static int getWebViewWidth(Context context) {
		DisplayMetrics dm = context.getResources().getDisplayMetrics();
		return (int) (dm.widthPixels / dm.density);
	}
	
	public static int getNewsTitleWid(Context con) {
		DisplayMetrics dm = con.getResources().getDisplayMetrics();
		int wid = (int) (dm.widthPixels - (dm.widthPixels * 1.0f) / dm.densityDpi * 152);
		int wid1 = (int) (dm.widthPixels - dm.density * 152);
		Log.e("---", "--density---" + dm.density + " -densitydpi--" +
				dm.densityDpi + "--,,--" + dm.xdpi + ">>>>>titleW==" + wid +
				">>>>>wid1111-----" + wid1);
		return (int) (dm.widthPixels - dm.density * 152);
	}
}
