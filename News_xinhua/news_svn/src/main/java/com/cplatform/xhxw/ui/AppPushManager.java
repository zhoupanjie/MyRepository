package com.cplatform.xhxw.ui;

import android.content.Context;
import com.baidu.android.pushservice.PushConstants;
import com.baidu.android.pushservice.PushManager;

/**
 * app push 管理
 * Created by cy-love on 14-1-16.
 */
public class AppPushManager {

    private static boolean isExecute;
	
    /**
     * 绑定
     */
    public static synchronized void startWork(Context context) {
        if (isExecute) {
            return;
        }
        isExecute = true;
        if (Config.IS_FEEDSS_PUSH) {
            com.feedss.push.sdk.PushManager.getInstance().setDebugMode(true);
            com.feedss.push.sdk.PushManager.getInstance().startWork(context, 
            		Constants.PUSH_CONFIGINFO_URL, Constants.MI_CONFIGINFO_URL);
        } else {
            PushManager.startWork(context.getApplicationContext(),
                    PushConstants.LOGIN_TYPE_API_KEY,
                    context.getString(R.string.app_key_baidu_push));
        }
    }

    /**
     * 解绑
     */
    public static void stopWork(Context context) {
        isExecute = false;
        if (Config.IS_FEEDSS_PUSH) {
            com.feedss.push.sdk.PushManager.getInstance().stopWork(context.getApplicationContext());
        } else {
            PushManager.stopWork(context.getApplicationContext());
        }
    }

//    /**
//     * 判断push是否已经绑定
//     */
//    public static boolean hashPushBind() {
//        PreferencesManager pref = App.getPreferenceManager();
//        return pref.isPushBind();
//    }
//
//    /**
//     * 设置push 绑定状态
//     */
//    public static void setPushBind(boolean isPushBind) {
//        PreferencesManager pref = App.getPreferenceManager();
//        pref.setPushBind(isPushBind);
//    }

    public static boolean hashOpenPush() {
        PreferencesManager pref = App.getPreferenceManager();
        return pref.isOpenPush();
    }

    public static void setOpenPush(boolean isOpenPush) {
        PreferencesManager pref = App.getPreferenceManager();
        pref.setOpenPush(isOpenPush);
    }
}
