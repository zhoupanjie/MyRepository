package com.cplatform.xhxw.ui.http;

import java.util.HashMap;
import java.util.Map;

import android.text.TextUtils;
import android.util.DisplayMetrics;

import com.cplatform.xhxw.ui.App;
import com.cplatform.xhxw.ui.Constants;
import com.cplatform.xhxw.ui.PreferencesManager;
import com.cplatform.xhxw.ui.util.AppInfoUtil;
import com.cplatform.xhxw.ui.util.LogUtil;
import com.cplatform.xhxw.ui.util.Util;
import com.loopj.android.http.AsyncHttpClient;

/**
 * Created by cy-love on 14-1-7.
 */
public class HttpClientConfig {

	// 10.130.113.5:8086
	// 10.130.18.203:9000
	// http://218.106.246.111:8888/news/list
	/**
	 * 
	 * apitest.xw.feedss.com
	 * 正式
	 * api.xw.feedss.com 
	 * api.qnsaas.com 
	 * apisaas.xw.qnsaas.com
	 * 
	 * api.xwqn.feedss.com apixw.saas.feedss.com 10.130.18.206:8888
	 * 202.108.60.21:28888 10.130.18.206:8081/saasApiServer/saas
	 * 218.106.246.85/saasApiServer/saas
	 * 测试
	 * apitest1.xw.feedss.com:9001 
	 * api212.saas.qnsaas.com
	 * apisaastest2.xw.qnsaas.com
	 * 
	 * apitest2.xw.feedss.com:81
	 * http://test1.m.xw.feedss.com:9001/saasApiServer/saas
	 */
	//正式地址
//	public static final String BASE_URL = "http://api.xw.feedss.com";// "http://apitest1.xw.feedss.com:9001";
//	// private static final String BASE_SAAS_URL = // "http://10.130.18.206:8081/saasApiServer/saas";
//	public static final String BASE_SAAS_URL = "http://api.qnsaas.com/saasApiServer/saas";// 221.208.245.211:8081/saasApiServer/saas";
//	private static final String BASE_DEV_URL = "http://apisaas.xw.qnsaas.com";// dev.api.saas.feedss.com";
//	public static final String SAAS_SHARE_URL = "http://cms.qnsaas.com/cms/wap/app/v_see.do?id=";
//	//测试地址
	public static final String BASE_URL = "http://apitest1.xw.feedss.com:9001";// "http://apitest1.xw.feedss.com:9001";
	// private static final String BASE_SAAS_URL = // "http://10.130.18.206:8081/saasApiServer/saas";
	public static final String BASE_SAAS_URL = "http://api212.saas.qnsaas.com/saasApiServer/saas";// 221.208.245.211:8081/saasApiServer/saas";
	private static final String BASE_DEV_URL = "http://apisaastest2.xw.qnsaas.com";// dev.api.saas.feedss.com";
	public static final String SAAS_SHARE_URL = "http://cms.qnsaas.com/cms/wap/app/v_see.do?id=";
	// 统计的url
	public static final String UMSAGENT_BASE_URL = "http://dc.qnsaas.com/DC/web/index.php?";

	// 活动页url
	public static final String EXCHANGE = BASE_URL + "/flux/exchange";
	// 流量兑换页url
	public static final String SHARE = BASE_URL + "/flux/share";

	/** 用于开发 登录 */
	// public static final String BASE_SAAS_URL =
	// "http://test1.m.xw.feedss.com:9001/saasApiServer/saas";
	/**
	 * 初始化链接
	 *
	 * @param client
	 */
	public static void initHttpClient(AsyncHttpClient client) {
		client.setTimeout(15 * 1000);
	}

	/**
	 * 设置请求header
	 */
	public static void setHeader(AsyncHttpClient client) {
		// client.cleanHeader();
		// Map<String, String> header = getHeader();
		// client.addHeaderAll(header);
	}

	/**
	 * 获取请求header
	 * 
	 * @return
	 */
	public static Map<String, String> getHeader() {
		Map<String, String> header = new HashMap<String, String>();
		header.put("UserAgent", "android");

		String channelName = "xuanwen";
		try {
			channelName = AppInfoUtil.getMetaDate(App.CONTEXT,
					Constants.APP_CHANNEL_KEY);
		} catch (Exception e) {
			e.printStackTrace();
		}
		header.put("channelid", channelName);

		header.put("productid", AppInfoUtil.getVsersionName(App.CONTEXT));

		String devId = TextUtils.isEmpty(Constants.getDevId()) ? "_"
				: Constants.getDevId();
		header.put("uniqueid", devId);

		header.put("deviceid", Util.getMyUUID());

		header.put("phoneType", android.os.Build.BRAND + " "
				+ android.os.Build.MODEL);
		header.put("sdkVersion", "" + android.os.Build.VERSION.RELEASE);

		DisplayMetrics dm = App.CONTEXT.getResources().getDisplayMetrics();
		header.put("phoneResolution", dm.widthPixels + "x" + dm.heightPixels);

		if (Constants.hasLogin()) {
			header.put("userid", Constants.userInfo.getUserId());
			header.put("userToken", Constants.userInfo.getUserToken());
			header.put("enterpriseid", Constants.getEnterpriseId());
			header.put("companyid", Constants.getEnterpriseId());
		} else {
			header.put("userid", Constants.getDevId());
		}
		header.put("language", PreferencesManager.getLanguageInfo(App.CONTEXT));
		return header;
	}

	public static String getAbsoluteUrl(String relativeUrl, boolean isSAAS,
			boolean isDev) {
		String baseUrl = BASE_URL;
		if (isSAAS) {
			baseUrl = BASE_SAAS_URL;
		} else if (isDev) {
			baseUrl = BASE_DEV_URL;
		}
		LogUtil.e("-----", "url----------->>>>>" + (baseUrl + relativeUrl));
		return baseUrl + relativeUrl;
	}
}
