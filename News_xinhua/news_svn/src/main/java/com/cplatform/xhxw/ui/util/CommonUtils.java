package com.cplatform.xhxw.ui.util;

import android.content.Context;

import java.io.File;

/**
 *
 * Created by cy-love on 14-1-21.
 */
public class CommonUtils {

    private static long lastClickTime;

    /**
     * 判断是否为重复点击
     * @return true 重复
     */
    public static boolean isFastDoubleClick() {
        return isFastDoubleClick(800);
    }
    /**
     * 判断是否为重复点击
     * @param time 在time时间内
     * @return true 重复
     */
    public static boolean isFastDoubleClick(int time) {
        long timeS = System.currentTimeMillis();
        long timeD = timeS - lastClickTime;
        if ( 0 < timeD && timeD < time) {
            return true;
        }
        lastClickTime = timeS;
        return false;
    }

    /**
     * 获得缓存文件大小
     * @param context
     * @return
     */
    private String getCacheSize(Context context) {
        File cacheDir = context.getExternalCacheDir();//getCacheDir();//文件所在目录为getFilesDir();
        if (cacheDir != null) {
            String cachePath=cacheDir.getPath() +"/";
            File file = new File(cachePath);
            if (file.exists()) {
                try {
                    long l = 0;
                    if (file.isDirectory()) { //如果路径是文件夹的时候
                        l = FileSizeUtil.getFileSize(file);
                    } else {
                        l = FileSizeUtil.getFileSizes(file);
                    }
                    return FileSizeUtil.formetFileSize(l);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        return "0K";
    }

    /**
     * 获得图片名称
     *
     * @param filePath
     * @return
     */
    public static String getFileName(String filePath) {
        int index = filePath.lastIndexOf("/");
        return filePath.substring(index + 1);

    }
}
