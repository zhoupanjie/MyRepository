package com.cplatform.xhxw.ui.util;

import com.cplatform.xhxw.ui.App;
import com.cplatform.xhxw.ui.Constants;

import android.app.Activity;
import android.provider.Settings;
import android.view.Window;
import android.view.WindowManager;

public class AppBrightnessManager {
	/**
	 * 获得当前屏幕亮度的模式 SCREEN_BRIGHTNESS_MODE_AUTOMATIC=1 为自动调节屏幕亮度
	 * SCREEN_BRIGHTNESS_MODE_MANUAL=0 为手动调节屏幕亮度
	 */
	public static int getScreenMode() {
		int screenMode = 0;
		try {
			screenMode = Settings.System.getInt(
					App.CONTEXT.getContentResolver(),
					Settings.System.SCREEN_BRIGHTNESS_MODE);
		} catch (Exception localException) {

		}
		return screenMode;
	}

	/**
	 * 获得当前屏幕亮度值 0--255
	 */
	public static int getScreenBrightness() {
		int screenBrightness = 255;
		try {
			screenBrightness = Settings.System.getInt(
					App.CONTEXT.getContentResolver(),
					Settings.System.SCREEN_BRIGHTNESS);
		} catch (Exception localException) {

		}
		return screenBrightness;
	}

	/**
	 * 设置当前屏幕亮度的模式 SCREEN_BRIGHTNESS_MODE_AUTOMATIC=1 为自动调节屏幕亮度
	 * SCREEN_BRIGHTNESS_MODE_MANUAL=0 为手动调节屏幕亮度
	 */
	public static void setScreenMode(int paramInt) {
		try {
			Settings.System.putInt(App.CONTEXT.getContentResolver(),
					Settings.System.SCREEN_BRIGHTNESS_MODE, paramInt);
		} catch (Exception localException) {
			localException.printStackTrace();
		}
	}

	/**
	 * 设置当前屏幕亮度值 0--255
	 */
	public static void saveScreenBrightness(int paramInt) {
		try {
			Settings.System.putInt(App.CONTEXT.getContentResolver(),
					Settings.System.SCREEN_BRIGHTNESS, paramInt);
		} catch (Exception localException) {
			localException.printStackTrace();
		}
	}

	/**
	 * 根据日间/夜间模式调整应用程序的亮度
	 */
	public static void setScreenBrightness(Activity act) {
		Window localWindow = act.getWindow();
		WindowManager.LayoutParams localLayoutParams = localWindow
				.getAttributes();
		float f = 0.08F;
		if(App.getDispalyModel() == Constants.DISPLAY_MODEL_DAY) {
			f = WindowManager.LayoutParams.BRIGHTNESS_OVERRIDE_NONE;
		}
		localLayoutParams.screenBrightness = f;
		localWindow.setAttributes(localLayoutParams);
	}
}
