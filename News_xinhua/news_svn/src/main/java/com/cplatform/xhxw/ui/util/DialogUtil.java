package com.cplatform.xhxw.ui.util;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import com.cplatform.xhxw.ui.R;

/**
 * 对话框工具类
 * Created by cy-love on 14-9-11.
 */
public class DialogUtil {

    /**
     * 确认对话框
     *
     * @param context
     * @param msg      内容
     * @param posiText 确认按钮文本
     * @param listener 确认按钮点击回调
     */
    public static void showAlertDialog(Context context, String msg, String posiText, DialogInterface.OnClickListener listener) {
        new AlertDialog.Builder(context)
                .setMessage(msg)
                .setPositiveButton(posiText, listener)
                .setNegativeButton(R.string.cancel, null).create().show();
    }
}
