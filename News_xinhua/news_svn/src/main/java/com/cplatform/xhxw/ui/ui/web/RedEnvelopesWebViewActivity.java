package com.cplatform.xhxw.ui.ui.web;

import org.json.JSONObject;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.http.SslError;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.SslErrorHandler;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.cplatform.xhxw.ui.Constants;
import com.cplatform.xhxw.ui.R;
import com.cplatform.xhxw.ui.http.HttpClientConfig;
import com.cplatform.xhxw.ui.http.ResponseUtil;
import com.cplatform.xhxw.ui.http.net.RedEnvelopesResponse;
import com.cplatform.xhxw.ui.ui.base.BaseActivity;
import com.cplatform.xhxw.ui.ui.base.view.ShareActionSheet;
import com.cplatform.xhxw.ui.ui.login.LoginActivity;
import com.cplatform.xhxw.ui.ui.settings.BindPhoneActivity;
import com.cplatform.xhxw.ui.util.LogUtil;
import com.cplatform.xhxw.ui.util.NetUtils;
import com.cplatform.xhxw.ui.util.ShareUtil;
import com.cplatform.xhxw.ui.util.StringUtil;
import com.google.gson.Gson;

/**
 * 红包webview
 */
public class RedEnvelopesWebViewActivity extends BaseActivity implements
		RedenvelopesCallBack {

	private static final String TAG = RedEnvelopesWebViewActivity.class
			.getSimpleName();

	private WebView mWebView;
	private LinearLayout mEmptyView;
	private ProgressBar mLoadingProgress;
	private ImageView mShare;
	private boolean isLoadUrlSuccess = true;

	private String mRelativeString;
	private JSONObject mReplyDict;
	private String mDrawPrizeCallback;

	public static boolean isDrawPrize = false;

	public final static String fluxurl = "fluxurl://";

	private String flux_url = "";// 跳转的url,或者动作
	private String flux_type = "";// 跳转类型
	private String flux_title = "";// 分享的标题
	private String flux_content = "";// 分享的内容
	private String flux_thumb = "";// 分享的图标
	private String flux_callback = "";// 回调的js方法
	private String flux_ReturnUrl = "";// 从其他页面回到该页面load的地址

	private boolean skip_switch = true;// 跳转开关,避免多次点击,多次跳转

	private RelativeLayout nv_title;

	private String url;

	public static Intent getIntent(Context context, String url) {
		Intent intent = new Intent(context, RedEnvelopesWebViewActivity.class);
		Bundle bun = new Bundle();
		bun.putString("url", url);
		intent.putExtras(bun);
		return intent;
	}

	@Override
	protected String getScreenName() {
		return "RedEnvelopesWebViewActivity";
	}

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_web_view);
		initActionBar();
		initViews();
		setViewStatus(true, false);
		Bundle bun = getIntent().getExtras();
		if (bun == null) {
			LogUtil.w(TAG, "bundle is null!");
			return;
		}
		url = bun.getString("url");
		initWebView();
	}

	@Override
	protected void onNewIntent(Intent intent) {
		super.onNewIntent(intent);
		setViewStatus(true, false);
		initWebView();
	}

	private void initViews() {
		TextView title = (TextView) findViewById(R.id.tv_title);
		// title.setText(getMyTitle());
		mWebView = (WebView) findViewById(R.id.webview);
		mEmptyView = (LinearLayout) findViewById(R.id.web_activity_empty);
		mLoadingProgress = (ProgressBar) findViewById(R.id.web_activity_pb);
		mShare = (ImageView) findViewById(R.id.web_activity_share);
		mShare.setVisibility(View.VISIBLE);
		nv_title = (RelativeLayout) findViewById(R.id.nv_title);
		// if (isLive()) {
		// mShare.setVisibility(View.VISIBLE);
		// mShare.setOnClickListener(mOnClick);
		// }
		mEmptyView.setOnClickListener(mOnClick);
	}

	OnClickListener mOnClick = new OnClickListener() {

		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.web_activity_empty:
				// setViewStatus(true, false);
				// mWebView.loadUrl(getUrl());
				break;
			// case R.id.web_activity_share:
			// shareLiveNews();
			// break;
			}
		}
	};

	/**
	 * 显示未登录提示
	 */
	private void showLoginAlert() {
		new AlertDialog.Builder(RedEnvelopesWebViewActivity.this)
				.setTitle(R.string.not_login)
				.setMessage(R.string.news_collect_not_login_alert)
				.setPositiveButton(R.string.confirm,
						new DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								RedEnvelopesWebViewActivity.this.startActivity(LoginActivity
										.getIntent(RedEnvelopesWebViewActivity.this));
							}
						}).setNegativeButton(R.string.cancel, null).show();
	}

	/**
	 * 分享直播新闻
	 */
	// private void shareLiveNews() {
	// if (NetUtils.getNetworkState() == NetUtils.Status.NONE) {
	// showToast(R.string.network_invalid);
	// return;
	// }
	// MobclickAgent
	// .onEvent(RedEnvelopesWebViewActivity.this, StatisticalKey.detail_share);
	// UmsAgent.onEvent(RedEnvelopesWebViewActivity.this,
	// StatisticalKey.detail_share,
	// new String[] { StatisticalKey.key_channelid,
	// StatisticalKey.key_newsid }, new String[] {
	// App.channel_id, getNewsid() });
	// String content = getMyTitle();
	// String clickUrl = getUrl();
	// ShareUtil.isShow = false;
	// ShareActionSheet window = ShareUtil.sendTextIntent(
	// RedEnvelopesWebViewActivity.this, null, getString(R.string.share_news),
	// getString(R.string.share_news), getMyTitle(), content,
	// clickUrl, false, true, false, null, true);
	// window.showAsDropDown(mShare);
	// }

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
			private RedEnvelopesResponse response_;

			@Override
			public boolean shouldOverrideUrlLoading(WebView view, String url) {
				if (!TextUtils.isEmpty(url)) {
					try {
						url = java.net.URLDecoder.decode(url, "utf-8");

						Log.d("aaa", "loadUrl:" + url);
						if (url.startsWith("fluxurl://")) {
							// aaa 拦截后的任务
							String content = url.substring(fluxurl.length(),
									url.length());
							ResponseUtil.checkResponse(content);
							response_ = new Gson().fromJson(content,
									RedEnvelopesResponse.class);
							skip_switch = doAction(response_);
							Toast.makeText(
									RedEnvelopesWebViewActivity.this
											.getApplicationContext(), "拦截下来了",
									1).show();
							return true;
						}
						showLoadingView();
						view.loadUrl(url, HttpClientConfig.getHeader());
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				return true;
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
				Toast.makeText(RedEnvelopesWebViewActivity.this,
						R.string.load_server_failure, Toast.LENGTH_LONG).show();
			}

			@Override
			public void onReceivedError(WebView view, int errorCode,
					String description, String failingUrl) {
				isLoadUrlSuccess = false;
				setViewStatus(false, false);
				Toast.makeText(RedEnvelopesWebViewActivity.this,
						R.string.load_server_failure, Toast.LENGTH_LONG).show();
				super.onReceivedError(view, errorCode, description, failingUrl);
			}
		});
		loadUrl(url);
	}

	public boolean doAction(RedEnvelopesResponse res) {
		flux_url = res.getUrl();
		flux_type = res.getType();
		flux_title = res.getTitle();
		flux_content = res.getContent();
		flux_thumb = res.getThumb();
		flux_callback = res.getCallback();
		flux_ReturnUrl = res.getReturnUrl();
		if (skip_switch) {
			if (StringUtil.isEquals(flux_type, "native")) {
				if (StringUtil.isEquals(flux_url, "login")) {
					// 跳转到登录界面
					startActivity(LoginActivity.getIntent(this, this));
					return false;
				}
				if (StringUtil.isEquals(flux_url, "bind")) {
					// 跳转到绑定手机号界面
					startActivity(BindPhoneActivity.getIntent(this, this));
					return false;
				}
			} else if (StringUtil.isEquals(flux_type, "http")) {
				if (!TextUtils.isEmpty(flux_url)) {
					// 加载新的url网页
					loadUrl(flux_url);
				}
			} else if (StringUtil.isEquals(flux_type, "share")) {
				// 弹出分享窗口
				ShareUtil.callback = this;
				ShareUtil.isShow = false;
				ShareActionSheet window = ShareUtil.sendTextIntent(this, null,
						null, null, flux_title, flux_content, null, false,
						true, false, flux_thumb, false);
				window.showAsDropDown(nv_title);
				return false;
			}
		}
		return true;
	}

	/**
	 * webview加载url,并带用户头
	 * 
	 * @param url
	 */
	private void loadUrl(String url) {
		if (mWebView != null && !TextUtils.isEmpty(url)) {
			mWebView.loadUrl(url, HttpClientConfig.getHeader());
		}
	}

	/**
	 * webview加载分享后的JS方法
	 * 
	 * @param url
	 */
	private void loadShareJS() {
		if (mWebView != null && !TextUtils.isEmpty(flux_callback)) {
			mWebView.loadUrl("javascript:" + flux_callback + "()");
		}
	}

	private void dealWithHTMLShare() {
		if (mReplyDict == null)
			return;
		try {
			if (NetUtils.getNetworkState() == NetUtils.Status.NONE) {
				showToast(R.string.network_invalid);
				return;
			}
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
		super.onResume();
	}

	protected void onPause() {
		super.onPause();
	}

	@Override
	protected void onDestroy() {
		if (mWebView != null) {
			mWebView.destroy();
		}
		super.onDestroy();
	}

	@Override
	public void share() {
		// 分享完毕,调用js方法
		showToast("分享完成");
		loadShareJS();
		skip_switch = true;
	}

	@Override
	public void bind() {
		// 绑定成功,重新load新的URL
		if (!TextUtils.isEmpty(Constants.getBindmobile())) {
			showToast("绑定完成");
			loadUrl(flux_ReturnUrl);
		}
		skip_switch = true;
	}

	@Override
	public void login() {
		// 登录成功,重新load新的URL
		if (Constants.hasLogin()) {
			showToast("登录完成");
			loadUrl(flux_ReturnUrl);
		}
		skip_switch = true;
	}
}