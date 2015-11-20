package com.cplatform.xhxw.ui.http;

import android.os.Bundle;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.cplatform.xhxw.ui.http.net.BaseRequest;
import com.cplatform.xhxw.ui.util.LogUtil;

class AsyncHttpManager {

	private static AsyncHttpClient client;

	private static final String TAG = AsyncHttpManager.class.getSimpleName();

	static {
		client = new AsyncHttpClient();
		HttpClientConfig.initHttpClient(client);
	}

	public static void get(String url, Bundle bundle,
			AsyncHttpResponseHandler responseHandler) {
		get(url, getParamsFrameBundle(bundle), responseHandler, false, false);
	}

	public static void post(String url, Bundle bundle,
			AsyncHttpResponseHandler responseHandler) {
		post(url, getParamsFrameBundle(bundle), responseHandler, false, false);
	}

	public static void post(String url, BaseRequest request,
			AsyncHttpResponseHandler responseHandler) {
		RequestParams params = ModelUtil.getRequestParamsFromObject(request);
		post(url, params, responseHandler, request.getSaasRequest(),
				request.isDevRequest());
	}

	public static void get(String url, BaseRequest request,
			AsyncHttpResponseHandler responseHandler) {
		RequestParams params = ModelUtil.getRequestParamsFromObject(request);
		get(url, params, responseHandler, request.getSaasRequest(),
				request.isDevRequest());
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

	public static void get(String url, RequestParams params,
			AsyncHttpResponseHandler responseHandler, boolean isSAAS,
			boolean isDev) {
		LogUtil.d(TAG, "get:" + url);
		LogUtil.d(TAG, "" + params);
		HttpClientConfig.setHeader(client);
		client.get(HttpClientConfig.getAbsoluteUrl(url, isSAAS, isDev), params,
				responseHandler);
	}

	public static void post(String url, RequestParams params,
			AsyncHttpResponseHandler responseHandler, boolean isSAAS,
			boolean isDev) {
		LogUtil.d(TAG, "post:" + url);
		LogUtil.d(TAG, "" + params);
//		HttpClientConfig.setHeader(client);
		client.post(HttpClientConfig.getAbsoluteUrl(url, isSAAS, isDev),
				params, responseHandler);
	}
}