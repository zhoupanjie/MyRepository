package com.cplatform.xhxw.ui.http.httputils;

import com.loopj.android.http.AsyncHttpResponseHandler;

/**
 * 取消联网进程
 */
public class HttpHanderUtil {

    /**
     * 取消异步网络进程
     * @param handlers 进程列表
     */
    public static void cancel(AsyncHttpResponseHandler... handlers) {
        for (AsyncHttpResponseHandler item : handlers) {
            if (item != null) {
                item.cancle();
            }
        }
    }

}
