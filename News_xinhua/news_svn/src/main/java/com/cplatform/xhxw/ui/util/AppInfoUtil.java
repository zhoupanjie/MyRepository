package com.cplatform.xhxw.ui.util;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;

/**
 * 获得应用信息
 * Created by cy-love on 14-1-5.
 */
public class AppInfoUtil {

    private static final String TAG = AppInfoUtil.class.getSimpleName();

    /**
     * 判断 新版本 是否可用
     *
     * @param context     上下文对象
     * @param versionCode 新的版本号
     * @return
     */
    public static boolean isNewVersionAvailable(Context context, long versionCode) {
        long code = getVersionCode(context);
        return (versionCode > code);
    }

    public static String getVsersionName(Context context) {
        try {
            PackageInfo pi = getPackageInfo(context);
            return pi.versionName;
        } catch (Exception e) {
            LogUtil.e(TAG, e);
        }
        return "";
    }

    /**
     * 返回当前包名
     *
     * @return
     */
    private static String getCurrentPkgName(Context context) {
        try {
            PackageInfo pi = getPackageInfo(context);
            return pi.packageName;
        } catch (Exception e) {
            LogUtil.e(TAG, "Exception " + e);
        }
        return "";
    }

    public static long getVersionCode(Context context) {
        try {
            return getPackageInfo(context).versionCode;
        } catch (Exception e) {
            LogUtil.e(TAG, "判断版本号获取错误::" + e.toString());
        }
        return -1;
    }

    public static PackageInfo getPackageInfo(Context context) throws Exception {
        PackageManager pm = context.getPackageManager();
        PackageInfo pi = pm.getPackageInfo(context.getPackageName(), 0);
        return pi;
    }

    /**
     * 获得manifase中得metadata数据
     * @param context
     * @param key 主键
     * @return 内容
     * @throws PackageManager.NameNotFoundException
     */
    public static String getMetaDate(Context context, String key) throws PackageManager.NameNotFoundException {
        ApplicationInfo appInfo = context.getPackageManager()
                .getApplicationInfo(context.getPackageName(),
                        PackageManager.GET_META_DATA);
        return appInfo.metaData.getString(key).trim();

    }
}
