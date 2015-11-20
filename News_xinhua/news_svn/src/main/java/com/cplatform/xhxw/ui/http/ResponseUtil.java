package com.cplatform.xhxw.ui.http;

import android.text.TextUtils;


/**
 * Created by cy-love on 14-2-10.
 */
public class ResponseUtil {

    /**
     * 废弃方法,该用checkObjResponse方法
     * @param content
     * @throws NullPointerException
     */
    @Deprecated
    public static void checkResponse(String content) throws NullPointerException {
        if (TextUtils.isEmpty(content))
            throw new NullPointerException("服务器返回结果为空!!!");
    }

    public static void checkObjResponse(Object response) throws NullPointerException {
        if (response == null) {
            throw new NullPointerException("JSON解析失败!!!");
        }
    }
}
