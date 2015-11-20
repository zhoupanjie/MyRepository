package com.cplatform.xhxw.ui.util;

import android.text.TextUtils;

/**
 * Created by cy-love on 14-1-4.
 */
public class TextUtil {

    /**
     * 新闻首页简介长度处理
     */
    public static String newsDescInterception(String desc) {
        if (!TextUtils.isEmpty(desc) && desc.length() > 25) {
            return StringUtil.getString(desc.substring(0, 22), "...");
        }
        return desc;
    }

}
