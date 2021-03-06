package com.hy.superemsg.utils;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.params.HttpClientParams;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.params.HttpProtocolParams;
import org.apache.http.util.EntityUtils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;

import com.alibaba.fastjson.JSON;
import com.hy.superemsg.req.BaseReqApi;
import com.hy.superemsg.req.ReqList;
import com.hy.superemsg.rsp.BaseRspApi;
import com.hy.superemsg.rsp.RspError;
import com.hy.superemsg.rsp.RspList;
import com.hy.superemsg.rsp.RspSuccess;

public class HttpUtils {
	private static final String SERVER_ROOT = "http://exin.qnsaas.cn:9000/exin-api-new/";
	static String tag = "HttpConnector";
	// static Uri PREFERRED_APN_URI = Uri.parse("content://telephony/carriers");
	static Uri PREFERRED_APN_URI = Uri
			.parse("content://telephony/carriers/preferapn");

	static String proxyAddress = "";// "10.0.0.200";
	static String proxyProt = "";// "80";

	public static final String CMPROXY = "10.0.0.172";
	public static final String CTPROXY = "10.0.0.200";
	public static final String PROT = "80";
	// private static TelephonyManager mtelephonyManager;
	private static HttpUtils _inst = new HttpUtils();

	public static HttpUtils getInst() {
		return _inst;
	}

	private Context ctx;

	public void initContext(Context ctx) {
		this.ctx = ctx;
	}

	private HttpUtils() {

	}

	private HttpClient createClient(Context ctx) {
		HttpParams params = new BasicHttpParams();
		HttpConnectionParams.setConnectionTimeout(params, 20000);
		HttpConnectionParams.setSoTimeout(params, 20000);
		HttpConnectionParams.setSocketBufferSize(params, 8192);
		HttpClientParams.setRedirecting(params, true);
		HttpProtocolParams.setContentCharset(params, "utf-8");
		HttpProtocolParams.setUserAgent(params, MobileInfo.getMobileModel());
		HttpClient client = new DefaultHttpClient(params);
		return client;
	}

	private String getRequest(Context ctx, String uriAPI, String post)
			throws Exception {
		String strResult;
		HttpClient httpclient = null;
		StringEntity entity;
		try {
			httpclient = createClient(ctx);
			HttpPost req = new HttpPost(SERVER_ROOT + uriAPI);
			req.addHeader("Content-Type", "application/json;charset=utf-8");
			entity = new StringEntity(post, "utf-8");
			req.setEntity(entity);
			HttpResponse rsp = httpclient.execute(req);
			if (rsp.getStatusLine().getStatusCode() == 200) {
				strResult = EntityUtils.toString(rsp.getEntity());
				return strResult.trim();
			} else {
				throw new Exception(rsp.getStatusLine().toString());
			}
		} finally {
			if (httpclient != null) {
				httpclient.getConnectionManager().shutdown();
			}
		}
	}

	public interface AsynHttpCallback {
		void onSuccess(BaseRspApi rsp);

		void onError(String error);
	}

	public void excuteTask(BaseReqApi api, AsynHttpCallback callback) {
		ConnectivityManager connMgr = (ConnectivityManager) ctx
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo networkInfo = connMgr
				.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
		boolean isWifiConn = networkInfo.isConnected();
		networkInfo = connMgr.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
		boolean isMobileConn = networkInfo.isConnected();
		if (isWifiConn || isMobileConn) {
			MyAsyncTask task = new MyAsyncTask(callback);
			task.execute(api);
		} else {
			callback.onError("没有可用的网络连接，请检查网络");
		}
	}

	private class MyAsyncTask extends
			AsyncTask<BaseReqApi, BaseReqApi, BaseRspApi> {
		private AsynHttpCallback callback;

		public MyAsyncTask(AsynHttpCallback callback) {
			this.callback = callback;
		}

		@Override
		protected BaseRspApi doInBackground(BaseReqApi... apis) {
			BaseReqApi api = apis[0];
			BaseRspApi rsp = null;
			RspSuccess success = null;
			RspError error = RspError.getInst().renew();
			try {
				String ret = getRequest(ctx,
						ReqList.getPath(api.getClass().getSimpleName()),
						api.toString());
				String className = RspList.getClassName(ReqList.getPath(api
						.getClass().getSimpleName()));
				success = (RspSuccess) JSON.parseObject(ret,
						Class.forName(className));
				error = success.transferToError();
				if (error != null) {
					rsp = error;
				} else {
					rsp = success;
				}
			} catch (Exception e) {
				error.error = e.getLocalizedMessage();
				rsp = error;
			}
			return rsp;
		}

		@Override
		protected void onPostExecute(BaseRspApi rsp) {
			if (rsp instanceof RspError) {
				callback.onError(((RspError) rsp).error);
			} else {
				callback.onSuccess(rsp);
			}
		}
	}
}
