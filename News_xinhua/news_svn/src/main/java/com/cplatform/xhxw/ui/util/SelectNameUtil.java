package com.cplatform.xhxw.ui.util;

import android.text.TextUtils;

public class SelectNameUtil {

    /**
     * 返回用户名称
     *
     * @param reName 备注（优先高）
     * @param niName 昵称（优先中）
     * @param name   昵称（优先低）
     * @return
     */
    public static String getName(String reName, String niName, String name) {
        if (!TextUtils.isEmpty(reName)) {
            return reName;
        }
        if (!TextUtils.isEmpty(niName)) {
            return niName;
        }
        return name;
    }

}
