package com.cplatform.xhxw.ui.util;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.net.Uri;
import android.os.Build;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationCompat.BigTextStyle;
import com.cplatform.xhxw.ui.R;

/**
 * 通知工具类
 * Created by cy-love on 14-1-31.
 */
public class NotificationUtil {

    public static final int DOWNLOAD = 1; // 下载
    public static final int DOWNLOADING = 2; // 正在下载

    private static int MESSAGE = 1000;

    /**
     * 获得新的消息id
     */
    public static int getNewMessageId() {
        return MESSAGE++;
    }

    /**
     * 创建和更新通知
     *
     * @param context      上下文
     * @param id           通知id
     * @param contentTitle 标题
     * @param contentText  内容
     * @param intent       通知触发活动
     * @param icon         通知icon
     */
    public static void notifyStatus(Context context, int id, CharSequence contentTitle, CharSequence contentText,
                                    PendingIntent intent, int icon, int flag) {
        Notification notification = newBuilder(context, contentTitle, contentText, intent, icon).build();
        notification.flags = notification.flags | flag;
        //设置声音
        notification.flags = Notification.DEFAULT_SOUND | notification.flags;
        //设置为点击清除
        notification.flags = Notification.FLAG_AUTO_CANCEL | notification.flags;
        notify(context, id, notification);
    }

    /**
     * 创建和更新通知
     *
     * @param context      上下文
     * @param id           通知id
     * @param contentTitle 标题
     * @param contentText  内容
     * @param intent       通知触发活动
     * @param icon         通知icon
     */
    public static void notifyStatus(Context context, int id, CharSequence contentTitle, CharSequence contentText,
                                    PendingIntent intent, int icon) {
        Notification notification = newBuilder(context, contentTitle, contentText, intent, icon).build();
        //设置声音
        notification.flags = Notification.DEFAULT_SOUND | notification.flags;
        //设置为点击清除
        notification.flags = Notification.FLAG_AUTO_CANCEL | notification.flags;
        //设置图标
        notification.icon = R.drawable.ic_launcher;
        notify(context, id, notification);
    }

    public static NotificationCompat.Builder newBuilder(Context context, CharSequence contentTitle, CharSequence contentText,
                                                        PendingIntent intent, int icon) {
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context);
        builder.setSmallIcon(icon);
        builder.setContentTitle(contentTitle);
        builder.setContentText(contentText);
        builder.setContentIntent(intent);
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
        	BigTextStyle bts = new BigTextStyle();
        	bts.bigText(contentText);
        	builder.setStyle(bts);
        }
        return builder;
    }

    /**
     * 创建和更新通知
     *
     * @param context      上下文
     * @param id           通知id
     * @param contentTitle 标题
     * @param contentText  内容
     * @param intent       通知触发活动
     * @param icon         通知icon
     */
    public static void notifyProgressStatus(Context context, int id, CharSequence contentTitle, CharSequence contentText,
                                            int progress, PendingIntent intent, int icon) {

        NotificationCompat.Builder builder = newBuilder(context, contentTitle, contentText, intent, icon);
        builder.setProgress(100, progress, false);
        Notification notification = builder.build();
        //设置声音
        notification.flags = Notification.DEFAULT_SOUND | notification.flags;
        //设置为点击清除
        notification.flags = Notification.FLAG_AUTO_CANCEL | notification.flags;

        notify(context, id, notification);
    }

    /**
     * 更新通知
     *
     * @param context
     * @param id           通知id
     * @param notification 通知
     */
    public static void notify(Context context, int id, Notification notification) {
        NotificationManager notificationmanager = (NotificationManager) context
                .getSystemService(Context.NOTIFICATION_SERVICE);
        notificationmanager.notify(id, notification);
    }


    /**
     * 取消通知
     *
     * @param context 上下文
     * @param id      通知id
     */
    public static void notifyCancel(Context context, int id) {
        NotificationManager notificationmanager = (NotificationManager) context
                .getSystemService(Context.NOTIFICATION_SERVICE);
        notificationmanager.cancel(id);
    }
}
