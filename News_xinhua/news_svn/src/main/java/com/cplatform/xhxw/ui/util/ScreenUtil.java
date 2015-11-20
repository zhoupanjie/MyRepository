package com.cplatform.xhxw.ui.util;

import android.content.Context;
import android.view.WindowManager;

/**
 *
 * Created by cy-love on 14-1-12.
 */
public class ScreenUtil {

	private static int screenWidth = 0;
	private static int screenHeight = 0;

	public static int dip2px(Context context, int dip) {
		float scale = context.getResources().getDisplayMetrics().density;
		return (int) (dip * scale + 0.5f * (dip >= 0 ? 1 : -1));
	}

	public static int px2dip(Context context, int px) {
		float scale = context.getResources().getDisplayMetrics().density;
		return (int) (px / scale + 0.5f * (px >= 0 ? 1 : -1));
	}

	public static int getScreenWidth(Context context) {
		if (screenWidth == 0) {
			WindowManager wm = (WindowManager) context
					.getSystemService(Context.WINDOW_SERVICE);
			screenWidth = wm.getDefaultDisplay().getWidth();
		}
		return screenWidth;
	}

	public static int getScreenHeight(Context context) {
		if (screenHeight == 0) {
			WindowManager wm = (WindowManager) context
					.getSystemService(Context.WINDOW_SERVICE);
			screenHeight = wm.getDefaultDisplay().getHeight();
		}
		return screenHeight;
	}

}
