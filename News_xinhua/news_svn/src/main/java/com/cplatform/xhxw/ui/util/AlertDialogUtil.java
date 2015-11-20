package com.cplatform.xhxw.ui.util;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;

/**
 * Created by cy-love on 14-10-8.
 */
public class AlertDialogUtil {

    /**
     * 提示信息
     */
    public static void showAlert(Context context,String title, String msg) {
        new AlertDialog.Builder(context)
                .setTitle(title)
                .setMessage(msg)
                .setNegativeButton("确定", null)
                .create()
                .show();
        ;
    }

    /**
     * 提示信息
     */
    public static void showAlert(Context context, String msg, String negText, DialogInterface.OnClickListener listener) {
        new AlertDialog.Builder(context)
                .setMessage(msg)
                .setPositiveButton(negText, listener)
                .setNegativeButton("取消", null)
                .create()
                .show();
        ;
    }

    /**
     * 显示选项对话框
     *
     * @param items    选项
     * @param listener 监听
     */
    public static void shoItemOption(Context context, CharSequence[] items, DialogInterface.OnClickListener listener) {
        new AlertDialog.Builder(context)
                .setItems(items, listener)
                .create()
                .show();
    }

}
