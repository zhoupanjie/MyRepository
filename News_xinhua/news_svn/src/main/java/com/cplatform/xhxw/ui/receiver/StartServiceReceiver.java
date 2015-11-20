package com.cplatform.xhxw.ui.receiver;

import android.content.Context;
import android.content.Intent;
import android.os.SystemClock;
import android.support.v4.content.WakefulBroadcastReceiver;
import com.cplatform.xhxw.ui.service.DownloadService;
import com.cplatform.xhxw.ui.service.NewsCashService;
import com.cplatform.xhxw.ui.service.SyncService;
import com.cplatform.xhxw.ui.util.Actions;
import com.cplatform.xhxw.ui.util.LogUtil;

/**
 * 启动后台服务
 * Created by cy-love on 14-2-11.
 */
public class StartServiceReceiver extends WakefulBroadcastReceiver {

    private static final String TAG = StartServiceReceiver.class.getSimpleName();

    /**
     * 启动缓存广播
     */
    public static final String ACTION_START_NEWS_CASH_SERVICE = "com.xuanwen.mobile.news.startServiceReceiver.ACTION_START_NEWS_CASH_SERVICE";
    /**
     * 开始下载任务
     */
    public static final String ACTION_DOWNLOAD_JOB_START = "com.xuanwen.mobile.news.startServiceReceiver.ACTION_DOWNLOAD_JOB_START";

    /**
     * 执行同步
     */
    public static final String ACTION_SYNC_SERVICE_START = "com.xuanwen.mobile.news.startServiceReceiver.ACTION_SYNC_SERVICE_START";

    /**
     * 同步联系人
     */
    public static final String ACTION_START_SYNC_CONTACTS = "com.xuanwen.mobile.news.startServiceReceiver.ACTION_START_SYNC_CONTACTS";

    /**
     * 下载联系人
     */
    public static final String ACTION_START_DOWNLOAD_CONTACTS = "com.xuanwen.mobile.news.startServiceReceiver.ACTION_START_DOWNLOAD_CONTACTS";

    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        if (ACTION_START_NEWS_CASH_SERVICE.equals(action)) {
            Intent service = new Intent(context, NewsCashService.class);
            LogUtil.i(TAG, "启动 NewsCashService @ " + SystemClock.elapsedRealtime());
            startWakefulService(context, service);
        } else if (ACTION_DOWNLOAD_JOB_START.equals(action)) {
            Intent service = new Intent(context, DownloadService.class);
            LogUtil.i(TAG, "启动 DownloadService @ " + SystemClock.elapsedRealtime());
            startWakefulService(context, service);
        } else if (ACTION_SYNC_SERVICE_START.equals(action)) {
            Intent service = new Intent(context, SyncService.class);
            service.setAction(Actions.ACTION_SYNC_CHANNEL);
            LogUtil.i(TAG, "启动 SyncService @ " + SystemClock.elapsedRealtime());
            startWakefulService(context, service);
        }
    }

}
