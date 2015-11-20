package com.cplatform.xhxw.ui.ui.base;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.cplatform.xhxw.ui.App;
import com.cplatform.xhxw.ui.AppPushManager;
import com.cplatform.xhxw.ui.Constants;
import com.cplatform.xhxw.ui.http.httputils.HttpCheckAccountStatusUtil;
import com.cplatform.xhxw.ui.util.LogUtil;

import android.app.Activity;

public class ScreenManager {
	private static List<Map<Activity, Boolean>> activityList = new ArrayList<Map<Activity, Boolean>>();
	private static ScreenManager instance;
	
	private ScreenManager() {
	}

	public static ScreenManager getScreenManager() {
		if (instance == null) {
			instance = new ScreenManager();
		}
		return instance;
	}

	/**
	 * 退出单个Activity
	 * @param activity
	 */
	public void popActivity(Activity activity) {
		for (int i = 0; i < activityList.size(); i++) {
			Map<Activity, Boolean> data = activityList.get(i);
			if (data.keySet().contains(activity)) {
				activityList.remove(data);
			}
		}
	}

	/**
	 * 加入当前Activity
	 * @param activity
	 * @param isForeGround
	 */
	public void pushActivity(Activity activity, boolean isForeGround) {
		Map<Activity, Boolean> data = new HashMap<Activity, Boolean>();
		data.put(activity, isForeGround);
		activityList.add(data);
		if (activityList.size() == 1) {
			HttpCheckAccountStatusUtil.checkAccountStatus(activity);
			Constants.initValue(activity);
			AppPushManager.startWork(App.CONTEXT);
		}
	}

	/**
	 * 改变传入activity的状态， true为可见
	 * @param activity
	 * @param isForeGround
	 */
	public void changeActivityStates(Activity activity, boolean isForeGround) {
//		LogUtil.e("", "activity------->>>" + activity + "----isShow-----" + isForeGround);
		for (int i = 0; i < activityList.size(); i++) {
			Map<Activity, Boolean> data = activityList.get(i);
			if (data.keySet().contains(activity)) {
				data.put(activity, isForeGround);
				break;
			}
		}
		if(isForeGround) {
			for(Map<Activity, Boolean> data : activityList) {
				if(!data.keySet().contains(activity)) {
					for(Activity act : data.keySet()) {
						data.put(act, false);
					}
				}
			}
		}
	}

	/**
	 * 是否有activity在显示
	 * @return
	 */
	public boolean isAnyActivityInForeGround() {
		// int count = activityList.size();
		for (int i = 0; i < activityList.size(); i++) {
			Map<Activity, Boolean> tmp = activityList.get(i);
			if (tmp.values().contains(true)) {
				return true;
			}
		}
		return false;
	}
	
	public Activity getShownActivity() {
		for (int i = 0; i < activityList.size(); i++) {
			Map<Activity, Boolean> tmp = activityList.get(i);
			for(Activity act : tmp.keySet()) {
//				LogUtil.e("", "=======>>activity------>>" + act +
//						"------isShow------>>>>" + tmp.get(act));
				if(tmp.get(act)) {
					return act;
				}
			}
		}
		return null;
	}

	public List<Map<Activity, Boolean>> getlistActivity() {
		return activityList;
	}

	/**
	 * 退出所有Activity
	 */
	public void popAllActivityExceptOne() {
		try {
			while (true) {
				Activity activity = null;
				if (!activityList.isEmpty()) {
					Map<Activity, Boolean> tmp = activityList.get(0);
					for (Activity act : tmp.keySet()) {
						activity = act;
					}
				} else {
					break;
				}
				if (activity != null) {
					activity.finish();
					activityList.remove(0);
					activity = null;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			activityList.clear();
		}
	}
}
