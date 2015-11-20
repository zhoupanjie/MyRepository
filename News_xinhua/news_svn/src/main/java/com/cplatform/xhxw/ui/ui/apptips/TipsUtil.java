package com.cplatform.xhxw.ui.ui.apptips;

import com.cplatform.xhxw.ui.App;
import com.cplatform.xhxw.ui.PreferencesManager;

public class TipsUtil {
	public static final String TIP_CHANNEL_SEARCH = "tip_channel_search";
	public static final String TIP_CHANNEL_ADD= "tip_channel_add";
	public static final String TIP_LANGUAGE_CHANGE = "tip_language_change";
	public static final String TIP_DETAIL_MORE = "tip_detail_more";
	
	public static void saveTipStatus(String tipType) {
		PreferencesManager.getInstance(App.CONTEXT).saveBoolean(tipType, true);
	}
	
	public static boolean isTipShown(String tipType) {
		return PreferencesManager.getInstance(App.CONTEXT).getBooleanValue(tipType, false);
	}
}
