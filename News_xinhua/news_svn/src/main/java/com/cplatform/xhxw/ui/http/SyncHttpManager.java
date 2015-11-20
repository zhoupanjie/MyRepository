package com.cplatform.xhxw.ui.http;

import android.os.Bundle;
import com.loopj.android.http.RequestParams;
import com.cplatform.xhxw.ui.http.net.BaseRequest;
import com.cplatform.xhxw.ui.util.LogUtil;

/**
 * 同步http链接
 * Created by cy-love on 14-1-7.
 */
class SyncHttpManager {

    private static SyncHttpClient client;

    private static final String TAG = AsyncHttpManager.class.getSimpleName();

    static {
        client = new SyncHttpClient();
        HttpClientConfig.initHttpClient(client);
    }

    public static String get(String url, Bundle bundle) {
        return get(url, getParamsFrameBundle(bundle), false, false);
    }

    public static String post(String url, Bundle bundle) {
        return post(url, getParamsFrameBundle(bundle), false, false);
    }

    public static String post(String url, BaseRequest request) {
        RequestParams params = ModelUtil.getRequestParamsFromObject(request);
        return post(url, params, request.getSaasRequest(), request.isDevRequest());
    }

    public static String get(String url, BaseRequest request) {
        RequestParams params = ModelUtil.getRequestParamsFromObject(request);
        return get(url, params, request.getSaasRequest(), request.isDevRequest());
    }

    public static RequestParams getParamsFrameBundle(Bundle bundle) {
        RequestParams params = new RequestParams();
        if (bundle != null) {
            for (String key : bundle.keySet()) {
                Object obj = bundle.get(key);
                params.put(key, obj != null ? String.valueOf(obj)
                        : (String) null);
            }
        }
        return params;
    }

    public static String get(String url, RequestParams params, boolean isSAAS, boolean isDev) {
        LogUtil.d(TAG, "get:" + url);
        LogUtil.d(TAG, "" + params);
        HttpClientConfig.setHeader(client);
        return client.get(HttpClientConfig.getAbsoluteUrl(url, isSAAS, isDev), params);
    }

    public static String post(String url, RequestParams params, boolean isSAAS, boolean isDev) {
        LogUtil.d(TAG, "post:" + url);
        LogUtil.d(TAG, "" + params);
        HttpClientConfig.setHeader(client);
        return client.post(HttpClientConfig.getAbsoluteUrl(url, isSAAS, isDev), params);
    }

}
