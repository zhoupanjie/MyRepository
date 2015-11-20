package com.cplatform.xhxw.ui.util;

import android.app.Activity;
import android.content.Context;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

/**
 * Created by cy-love on 14-1-28.
 */
public class KeyboardUtil {

    public static void hideSoftInput(Activity acitivity) {
        InputMethodManager imm = (InputMethodManager) acitivity.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(acitivity.getWindow().getDecorView().getApplicationWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
    }

    public static void showSoftInput(EditText et) {
        et.requestFocus();
        InputMethodManager imm = (InputMethodManager) et.getContext()
                .getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.showSoftInput(et, InputMethodManager.RESULT_UNCHANGED_SHOWN);
    }

    public static void showSoftInputDelay(final EditText et) {
        et.postDelayed(new Runnable() {

            @Override
            public void run() {
                showSoftInput(et);
            }
        }, 300);
    }

    /**
     * 隐藏键盘
     *
     * @param edit 获得焦点的控件
     */
    public static void hideKeyboard(Activity activity, EditText edit) {
        InputMethodManager im = (InputMethodManager) edit
                .getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        im.hideSoftInputFromWindow(activity.getCurrentFocus().getWindowToken(),
                InputMethodManager.HIDE_NOT_ALWAYS);
    }

    /**
     * 显示键盘
     *
     * @param edit 获得焦点的控件
     */
    public static void showKeyboard(EditText edit) {
        edit.setFocusable(true);
        edit.setFocusableInTouchMode(true);
        edit.requestFocus();
        edit.requestFocusFromTouch();
        InputMethodManager im = (InputMethodManager) edit
                .getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        im.showSoftInput(edit, InputMethodManager.RESULT_UNCHANGED_SHOWN);
    }

}
