package com.cplatform.xhxw.ui.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;
import com.cplatform.xhxw.ui.util.Actions;

/**
 * 系统广播监听
 * Created by cy-love on 14-3-9.
 */
public class AppReceiver extends BroadcastReceiver {
    public void onReceive(Context context, Intent intent) {
        // 解锁
        if (Intent.ACTION_USER_PRESENT.equals(intent.getAction())) {
            LocalBroadcastManager.getInstance(context).sendBroadcast(new Intent(Actions.ACTION_ALL_CHANNEL_RELOAD));
        }
    }
}
