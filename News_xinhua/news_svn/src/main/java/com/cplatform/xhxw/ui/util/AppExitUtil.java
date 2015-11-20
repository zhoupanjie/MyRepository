package com.cplatform.xhxw.ui.util;

import android.view.KeyEvent;
import com.cplatform.xhxw.ui.R;
import com.cplatform.xhxw.ui.ui.base.BaseActivity;

/**
 * app退出工具类
 * Created by cy-love on 14-2-9.
 */
public class AppExitUtil {


    private static long mExitTime;

    /**
     * 判断退出
     * @param activity
     * @param keyCode
     * @param event
     * @return
     */
    public static boolean onKeyUp(BaseActivity activity, int keyCode, KeyEvent event) {
        switch (event.getKeyCode()) {
            case KeyEvent.KEYCODE_BACK: // 响应返回按键
                if((System.currentTimeMillis() - mExitTime) > 2000) {
                    activity.showToast(R.string.app_exit_tip);
                    mExitTime = System.currentTimeMillis();
                } else {
                    activity.finish();
                }
                return true;
        }
        return false;
    }
}
