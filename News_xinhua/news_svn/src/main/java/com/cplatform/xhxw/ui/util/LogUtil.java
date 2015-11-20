package com.cplatform.xhxw.ui.util;

import com.cplatform.xhxw.ui.Config;

public class LogUtil {

    public enum DEBUG {
        /**
         * 开启调试信息
         */
        TRUE,
        /**
         * 关闭调试信息
         */
        FALSE
    }

    private static DEBUG getModel() {
        return Config.DEVELOPER_MODE;
    }

    /**
     * 是否已经打开调试
     * @return true开启，否则为关闭
     */
    public boolean isShowLog() {
        return getModel() == DEBUG.TRUE;
    }

    public static void d(String tag, String msg) {
        switch (getModel()) {
            case TRUE:
                LogUtils.d(msg+"");
                break;
            default:
                break;
        }
    }

    public static void d(String msg) {
        switch (getModel()) {
            case TRUE:
                LogUtils.d(msg+"");
                break;
            default:
                break;
        }
    }

    public static void d(Class<?> tag, String msg) {
        switch (getModel()) {
            case TRUE:
                LogUtils.d(msg+"");
                break;
            default:
                break;
        }
    }

    public static void e(String tag, String msg) {
        switch (getModel()) {
            case TRUE:
                LogUtils.e(msg+"");
                break;
            default:
                break;
        }
    }

    public static void e(String tag, Throwable e) {
        switch (getModel()) {
            case TRUE:
                LogUtils.e(e.toString());
                break;
            default:
                break;
        }
    }

    public static void e(Class<?> tag, String msg) {
        switch (getModel()) {
            case TRUE:
                LogUtils.e(msg+"");
                break;
            default:
                break;
        }
    }

    public static void i(String tag, String msg) {
        switch (getModel()) {
            case TRUE:
                LogUtils.i(msg+"");
                break;
            default:
                break;
        }
    }

    public static void i(Class<?> tag, String msg) {
        switch (getModel()) {
            case TRUE:
                LogUtils.i(msg+"");
                break;
            default:
                break;
        }
    }

    public static void v(String tag, String msg) {
        switch (getModel()) {
            case TRUE:
                LogUtils.v(msg+"");
                break;
            default:
                break;
        }
    }

    public static void w(String tag, String msg) {
        switch (getModel()) {
            case TRUE:
                LogUtils.w(msg+"");
                break;
            default:
                break;
        }
    }

    public static void w(String tag, Throwable e) {
        switch (getModel()) {
            case TRUE:
                LogUtils.w(e);
                break;
            default:
                break;
        }
    }

    public static void w(Throwable e) {
        switch (getModel()) {
            case TRUE:
                LogUtils.w(e);
                break;
            default:
                break;
        }
    }

    public static void w(String msg) {
        switch (getModel()) {
            case TRUE:
                LogUtils.w(msg);
                break;
            default:
                break;
        }
    }
}
