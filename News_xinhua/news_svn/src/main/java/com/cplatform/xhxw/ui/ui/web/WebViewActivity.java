package com.cplatform.xhxw.ui.ui.web;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.net.http.SslError;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.SslErrorHandler;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.cplatform.xhxw.ui.App;
import com.cplatform.xhxw.ui.Constants;
import com.cplatform.xhxw.ui.R;
import com.cplatform.xhxw.ui.StatisticalKey;
import com.cplatform.xhxw.ui.http.HttpClientConfig;
import com.cplatform.xhxw.ui.ui.base.BaseActivity;
import com.cplatform.xhxw.ui.ui.base.view.ShareActionSheet;
import com.cplatform.xhxw.ui.util.LogUtil;
import com.cplatform.xhxw.ui.util.NetUtils;
import com.cplatform.xhxw.ui.util.ShareUtil;
import com.umeng.analytics.MobclickAgent;
import com.wbtech.ums.UmsAgent;

/**
 * 隐私条款和服务协议 Created by cy-love on 14-2-16.
 */
public class WebViewActivity extends BaseActivity {

	private static final String TAG = WebViewActivity.class.getSimpleName();

	public static final String ACTION_SHARE_DONE = "xhxw_share_draw_prize_done";

	private static final String URL = "url";
	private static final String TITLE = "title";
	private static final String LIVE = "live";
	private static final String MSG = "msg";
	private static final String NEWSID = "newsid";
	private static final String IS_DRAW_PRIZE_ACTIVITY = "is_draw_prize_activity";

	private WebView mWebView;
	private LinearLayout mEmptyView;
	private ProgressBar mLoadingProgress;
	private ImageView mShare;
	private boolean isLoadUrlSuccess = true;
	

	private String mRelativeString;
	private JSONObject mReplyDict;
	private String mDrawPrizeCallback;

	private Receiver mReceiver;

	public static boolean isDrawPrize = false;

	/**
	 * 服务协议地址
	 */
	public static Intent getServiceIntent(Context context) {
		Intent intent = new Intent(context, WebViewActivity.class);
		intent.putExtra(URL, context.getString(R.string.app_service_url));
		intent.putExtra(TITLE, context.getString(R.string.terms_of_service));
		return intent;
	}

	/**
	 * 隐私协议地址
	 */
	public static Intent getPrivacyIntent(Context context) {
		Intent intent = new Intent(context, WebViewActivity.class);
		intent.putExtra(URL, context.getString(R.string.app_privacy_url));
		intent.putExtra(TITLE, context.getString(R.string.privacy_policy));
		return intent;
	}

	public static Intent getIntentByURL(Context context, String url,
			String title) {
		Intent intent = new Intent(context, WebViewActivity.class);
		intent.putExtra(URL, url);
		intent.putExtra(TITLE, title);
		return intent;
	}

	public static Intent getDrawPrizeIntent(Context context, String url,
			String newsid) {
		Intent intent = new Intent(context, WebViewActivity.class);
		intent.addFlags(Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT);
		intent.putExtra(URL, url);
		intent.putExtra(NEWSID, newsid);
		intent.putExtra(IS_DRAW_PRIZE_ACTIVITY, true);
		return intent;
	}

	public static Intent getLiveNewsIntent(Context context, String newsid,
			String title, boolean isLiveNews) {
		Intent intent = new Intent(context, WebViewActivity.class);
		intent.putExtra(NEWSID, newsid);
		intent.putExtra(TITLE, title);
		intent.putExtra(LIVE, isLiveNews);
		return intent;
	}

	public static Intent getMsgNewsIntent(Context context, String newsid,
			String title, boolean isMsg) {
		Intent intent = new Intent(context, WebViewActivity.class);
		intent.putExtra(NEWSID, newsid);
		intent.putExtra(TITLE, title);
		intent.putExtra(MSG, isMsg);
		return intent;
	}

	@Override
	protected String getScreenName() {
		return "WebViewActivity";
	}

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_web_view);
		initActionBar();
		initViews();
		setViewStatus(true, false);
		initWebView();
	}

	@Override
	protected void onNewIntent(Intent intent) {
		super.onNewIntent(intent);
		LogUtil.e(TAG, "---------------newIntent------------" + isDrawPrize()
				+ " ----callback-----" + "javacript:" + mDrawPrizeCallback);
		setViewStatus(true, false);
		initWebView();
	}

	private void initViews() {
		TextView title = (TextView) findViewById(R.id.tv_title);
		title.setText(getMyTitle());
		mWebView = (WebView) findViewById(R.id.webview);
		mEmptyView = (LinearLayout) findViewById(R.id.web_activity_empty);
		mLoadingProgress = (ProgressBar) findViewById(R.id.web_activity_pb);
		mShare = (ImageView) findViewById(R.id.web_activity_share);
		mShare.setVisibility(View.INVISIBLE);
		
		if (isLive()) {
			mShare.setVisibility(View.VISIBLE);
			mShare.setOnClickListener(mOnClick);
		}
		mEmptyView.setOnClickListener(mOnClick);
		Button backBtn = (Button) findViewById(R.id.btn_back);
		backBtn.setOnClickListener(mOnClick);
		mReceiver = new Receiver();
		IntentFilter filter = new IntentFilter();
		filter.addAction(ACTION_SHARE_DONE);
		registerReceiver(mReceiver, filter);
	}

	OnClickListener mOnClick = new OnClickListener() {

		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.web_activity_empty:
				setViewStatus(true, false);
				mWebView.loadUrl(getUrl(), HttpClientConfig.getHeader());
				break;
			case R.id.web_activity_share:
				shareLiveNews();
				break;
			case R.id.btn_back:
				goBack();
				break;
			}
		}
	};
	@Override
	public boolean onKeyUp(int keyCode, KeyEvent event) {
		if(keyCode == KeyEvent.KEYCODE_BACK) {
			goBack();
			return true;
		}
		return super.onKeyUp(keyCode, event);
	}
	//设置WebView 返回事件
	void goBack(){
		if(mWebView.canGoBack()){
			mWebView.goBack();
		}else{
			this.finish();
		}
	}
	/**
	 * 分享直播新闻
	 */
	private void shareLiveNews() {
		if (NetUtils.getNetworkState() == NetUtils.Status.NONE) {
			showToast(R.string.network_invalid);
			return;
		}
		MobclickAgent
				.onEvent(WebViewActivity.this, StatisticalKey.detail_share);
		UmsAgent.onEvent(WebViewActivity.this, StatisticalKey.detail_share,
				new String[] { StatisticalKey.key_channelid,
						StatisticalKey.key_newsid }, new String[] {
						App.channel_id, getNewsid() });
		String content = getMyTitle();
		String clickUrl = getUrl();
		ShareUtil.isShow = false;
		ShareActionSheet window = ShareUtil.sendTextIntent(
				WebViewActivity.this, null, getString(R.string.share_news),
				getString(R.string.share_news), getMyTitle(), content,
				clickUrl, false, true, false, null, true);
		window.showAsDropDown(mShare);
	}

	private void setViewStatus(boolean isToStartLoad, boolean isLoadSuccess) {
		if (isToStartLoad) {
			isLoadUrlSuccess = true;
			mLoadingProgress.setVisibility(View.VISIBLE);
		} else {
			mLoadingProgress.setVisibility(View.GONE);
			if (isLoadSuccess) {
				mEmptyView.setVisibility(View.GONE);
				mWebView.setVisibility(View.VISIBLE);
			} else {
				mEmptyView.setVisibility(View.VISIBLE);
				mWebView.setVisibility(View.GONE);
			}
		}
	}

	private void initWebView() {
		final WebSettings mWebSettings = mWebView.getSettings();
		mWebSettings.setJavaScriptEnabled(true);
		mWebSettings.setUseWideViewPort(false);

		mWebView.setWebViewClient(new WebViewClient() {
			@Override
			public boolean shouldOverrideUrlLoading(WebView view, String url) {
				if (!TextUtils.isEmpty(url)) {
					if (url.startsWith("xuanwen://")) {
						String relativeString = url.substring(10);
						try {
							mRelativeString = java.net.URLDecoder.decode(
									relativeString, "utf-8");
							mReplyDict = new JSONObject(mRelativeString);
							dealWithHTMLShare();
						} catch (UnsupportedEncodingException e) {
							e.printStackTrace();
						} catch (JSONException e) {
							e.printStackTrace();
						}
					} else {
						showLoadingView();
						view.loadUrl(url, HttpClientConfig.getHeader());
					}
					return true;
				}
				return false;
			}

			@Override
			public void onPageStarted(WebView view, String url, Bitmap favicon) {
				super.onPageStarted(view, url, favicon);
				hideLoadingView();
			}

			@Override
			public void onPageFinished(WebView view, String url) {
				if (isLoadUrlSuccess) {
					setViewStatus(false, true);
				}
				super.onPageFinished(view, url);
				hideLoadingView();
			}

			@Override
			public void doUpdateVisitedHistory(WebView view, String url,
					boolean isReload) {
			}

			@Override
			public void onReceivedSslError(WebView view,
					SslErrorHandler handler, SslError error) {
				handler.proceed();
				isLoadUrlSuccess = false;
				setViewStatus(false, false);
				Toast.makeText(WebViewActivity.this,
						R.string.load_server_failure, Toast.LENGTH_LONG).show();
			}

			@Override
			public void onReceivedError(WebView view, int errorCode,
					String description, String failingUrl) {
				isLoadUrlSuccess = false;
				setViewStatus(false, false);
				Toast.makeText(WebViewActivity.this,
						R.string.load_server_failure, Toast.LENGTH_LONG).show();
				super.onReceivedError(view, errorCode, description, failingUrl);
			}
		});
		String url = getUrl();
		LogUtil.d(TAG, "help url = " + url + "\n islive--->>" + isLive()
				+ " -- isdp--->>" + isDrawPrize());
		if (isMsg()) {
			url = HttpClientConfig.BASE_URL + "/usermsg/live";
			mWebView.loadUrl(url, initLiveHeader());
			return;
		}
		if (isLive()) {
			url = HttpClientConfig.BASE_URL + "/show/live?newsid="
					+ getNewsid();
			mWebView.loadUrl(url, initLiveHeader());
			return;
		}
		if (isDrawPrize() && getUrl() == null) {
			url = HttpClientConfig.BASE_URL + "/show/live?newsid="
					+ getNewsid();
			mWebView.loadUrl(url, initLiveHeader());
			return;
		}
		mWebView.loadUrl(url, HttpClientConfig.getHeader());
	}

	private String getUrl() {
		String url = getIntent().getStringExtra(URL);
		if (isLive()) {
			url = HttpClientConfig.BASE_URL + "/show/live?newsid="
					+ getNewsid();
		}
		return url;
	}

	private String getNewsid() {
		return getIntent().getStringExtra(NEWSID);
	}

	private String getMyTitle() {
		return getIntent().getStringExtra(TITLE);
	}

	private boolean isLive() {
		return getIntent().getBooleanExtra(LIVE, false);
	}

	private boolean isMsg() {
		return getIntent().getBooleanExtra(MSG, false);
	}

	public boolean isDrawPrize() {
		return getIntent().getBooleanExtra(IS_DRAW_PRIZE_ACTIVITY, false);
	}

	/**
	 * 生成直播页面加载的请求header
	 * 
	 * @return
	 */
	private Map<String, String> initLiveHeader() {
		Map<String, String> header = new HashMap<String, String>();
		header.put("msgid", getNewsid());
		header.put("UserAgent", "android");
		header.put("channelid", Constants.AppInfo.getPlatform());
		header.put("productid", Constants.AppInfo.getVersionName());
		String devId = TextUtils.isEmpty(Constants.getDevId()) ? "_"
				: Constants.getDevId();
		header.put("uniqueid", devId);
		header.put("deviceid", Constants.phoneStatus.getDeviceId());
		header.put("phoneType", android.os.Build.BRAND + " "
				+ android.os.Build.MODEL);
		header.put("sdkVersion", "" + android.os.Build.VERSION.RELEASE);
		header.put("phoneResolution", Constants.screenWidth + "x"
				+ Constants.screenHeight);
		if (Constants.hasLogin()) {
			header.put("userid", Constants.userInfo.getUserId());
			header.put("userToken", Constants.userInfo.getUserToken());
			header.put("enterpriseid", Constants.getEnterpriseId());
		} else {
			header.put("userid", Constants.getDevId());
		}
		return header;
	}

	private void dealWithHTMLShare() {
		if (mReplyDict == null)
			return;
		try {
			if (NetUtils.getNetworkState() == NetUtils.Status.NONE) {
				showToast(R.string.network_invalid);
				return;
			}
			MobclickAgent.onEvent(WebViewActivity.this,
					StatisticalKey.detail_share);
			UmsAgent.onEvent(WebViewActivity.this, StatisticalKey.detail_share,
					new String[] { StatisticalKey.key_channelid,
							StatisticalKey.key_newsid }, new String[] {
							App.channel_id, getNewsid() });
			JSONObject params = mReplyDict.getJSONObject("params");
			mDrawPrizeCallback = mReplyDict.getString("callback");
			String title = params.getString("title");
			String content = params.getString("content");
			String thumb = params.getString("thumb");
			if (thumb == null || thumb.trim().length() < 1) {
				thumb = null;
			}
			String clickUrl = params.getString("url");
			ShareUtil.sendTextIntent(this, null,
					getString(R.string.share_news),
					getString(R.string.share_news), title, content, clickUrl,
					false, true, false, thumb, false);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	protected void onResume() {
		isDrawPrize = isDrawPrize();
		super.onResume();
	}

	protected void onPause() {
		has_channel_id = true;
		super.onPause();
	}

	@Override
	protected void onDestroy() {
		unregisterReceiver(mReceiver);
		if (mWebView != null) {
			mWebView.destroy();
		}
		super.onDestroy();
	}

	private class Receiver extends BroadcastReceiver {
		@Override
		public void onReceive(Context context, Intent intent) {
			String action = intent.getAction();
			if (action.equals(WebViewActivity.ACTION_SHARE_DONE)) {
				if (isDrawPrize()) {
					mWebView.loadUrl("javascript:" + mDrawPrizeCallback);
				}
			}
		}
	}
}