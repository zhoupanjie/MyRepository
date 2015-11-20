package com.cplatform.xhxw.ui.util;

import android.app.AlertDialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.NetworkInfo.State;
import android.os.Build;
import android.provider.Settings;

import com.cplatform.xhxw.ui.App;
import com.cplatform.xhxw.ui.R;

/**
 * 网络状态检查工具类
 */
public class NetUtils {

    /**
     * 网络状态
     */
    public enum Status {
        /**
         * 无网络
         */
        NONE,
        /**
         * Wi-Fi
         */
        WIFI,
        /**
         * 3G,GPRS
         */
        MOBILE
    }

    /**
     * 获取当前网络状态
     *
     * @param context
     * @return
     */
    public static Status getNetworkState() {
        ConnectivityManager connManager = (ConnectivityManager) App.CONTEXT
                .getSystemService(Context.CONNECTIVITY_SERVICE);

        State state;
        // 手机网络判断
        NetworkInfo mobileInfo = connManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
        if(mobileInfo != null) {
        	state = mobileInfo.getState();
            if (state == State.CONNECTED || state == State.CONNECTING) {
                return Status.MOBILE;
            }
        }

        // Wifi网络判断
        NetworkInfo wifiInfo = connManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        if(wifiInfo != null) {
        	state = wifiInfo.getState();
            if (state == State.CONNECTED || state == State.CONNECTING) {
                return Status.WIFI;
            }
        }
        
        return Status.NONE;
    }
    
    public static final boolean isNetworkAvailable() {
    	return getNetworkState() != Status.NONE;
    }

    /**
     * 提示打开设置网络界面
     */
    public static void setNetworkMethod(final Context context){
        //提示对话框
        AlertDialog.Builder builder=new AlertDialog.Builder(context);
        builder.setTitle(R.string.networking_tips)
                .setMessage(R.string.network_settings_tips)
                .setPositiveButton(R.string.settings, new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = null;
                //判断手机系统的版本  即API大于10 就是3.0或以上版本
                if (Build.VERSION.SDK_INT > 10) {
                    intent = new Intent(Settings.ACTION_WIRELESS_SETTINGS);
                } else {
                    intent = new Intent();
                    ComponentName component = new ComponentName("com.android.settings", "com.android.settings.WirelessSettings");
                    intent.setComponent(component);
                    intent.setAction("android.intent.action.VIEW");
                }
                context.startActivity(intent);
            }
        }).setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        }).show();
    }
}
