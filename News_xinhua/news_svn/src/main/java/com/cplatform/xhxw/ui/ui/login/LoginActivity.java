package com.cplatform.xhxw.ui.ui.login;

import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.Toast;
import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

import com.cplatform.xhxw.ui.App;
import com.cplatform.xhxw.ui.AppPushManager;
import com.cplatform.xhxw.ui.Constants;
import com.cplatform.xhxw.ui.R;
import com.cplatform.xhxw.ui.db.ChanneDB;
import com.cplatform.xhxw.ui.http.APIClient;
import com.cplatform.xhxw.ui.http.ResponseUtil;
import com.cplatform.xhxw.ui.http.httputils.HttpChannelUtil;
import com.cplatform.xhxw.ui.http.net.BaseRequest;
import com.cplatform.xhxw.ui.http.net.ChannelListResponse;
import com.cplatform.xhxw.ui.http.net.LoginRequest;
import com.cplatform.xhxw.ui.http.net.LoginResponse;
import com.cplatform.xhxw.ui.http.net.UserOpenRequest;
import com.cplatform.xhxw.ui.model.Channe;
import com.cplatform.xhxw.ui.model.OauthVerify;
import com.cplatform.xhxw.ui.model.UserInfo;
import com.cplatform.xhxw.ui.receiver.StartServiceReceiver;
import com.cplatform.xhxw.ui.ui.base.BaseActivity;
import com.cplatform.xhxw.ui.ui.settings.UserinfoUtil;
import com.cplatform.xhxw.ui.ui.web.RedenvelopesCallBack;
import com.cplatform.xhxw.ui.util.Actions;
import com.cplatform.xhxw.ui.util.LogUtil;
import com.cplatform.xhxw.ui.util.MD5;
import com.cplatform.xhxw.ui.util.OauthKey;
import com.cplatform.xhxw.ui.util.OauthVerifyUtil;
import com.google.gson.Gson;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.tencent.mm.sdk.constants.ConstantsAPI.Token;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.controller.UMServiceFactory;
import com.umeng.socialize.controller.UMSocialService;
import com.umeng.socialize.controller.listener.SocializeListeners;
import com.umeng.socialize.controller.listener.SocializeListeners.UMAuthListener;
import com.umeng.socialize.exception.SocializeException;
import com.umeng.socialize.sso.QZoneSsoHandler;
import com.umeng.socialize.sso.TencentWBSsoHandler;
import com.umeng.socialize.sso.UMQQSsoHandler;
import com.umeng.socialize.sso.UMSsoHandler;
import com.umeng.socialize.weixin.controller.UMWXHandler;

/**
 * 登录
 */
public class LoginActivity extends BaseActivity {

	private static final String TAG = LoginActivity.class.getSimpleName();

	@InjectView(R.id.login_account)
	EditText account;
	@InjectView(R.id.login_password)
	EditText password;

	private AsyncHttpResponseHandler mLoginHandler;
	private UMSocialService mController;
	private Context mCon;
	private static RedenvelopesCallBack callback;// 从红包网页跳转的回调对象

	private int mAccountType = UserInfo.ACCOUNT_TYPE_XUNWEN;

	public static Intent getIntent(Context context) {
		Intent intent = new Intent(context, LoginActivity.class);
		return intent;
	}

	public static Intent getIntent(Context context,
			RedenvelopesCallBack redenvelopesCallBack) {
		Intent intent = new Intent(context, LoginActivity.class);
		callback = redenvelopesCallBack;
		return intent;
	}

	@Override
	protected String getScreenName() {
		return "LoginActivity";
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_login);

		ButterKnife.inject(this);
		mCon = LoginActivity.this;
		mController = UMServiceFactory.getUMSocialService("com.umeng.login");
		mController.getConfig().setSsoHandler(new TencentWBSsoHandler());
		if (callback != null) {
			gotoHome = false;
		}
	}

	@Override
	protected void onStart() {
		super.onStart();
	}

	@Override
	protected void onStop() {
		super.onStop();
	}

	/**
	 * 注册
	 */
	@OnClick(R.id.login_register)
	public void register() {
		startActivityForResult(RegisterCheckActivity.getIntent(this), 1);
	}

	/**
	 * 登录
	 */
	@OnClick(R.id.login_ok)
	public void login_ok() {

		if (TextUtils.isEmpty(getDate(account))) {
			showToast(getString(R.string.login_account_hint_demand));
			return;
		}

		if (TextUtils.isEmpty(getDate(password))) {
			showToast(getString(R.string.login_password_hint_demand));
			return;
		}
		mAccountType = UserInfo.ACCOUNT_TYPE_XUNWEN;
		AppPushManager.stopWork(mCon);
		login(getDate(account), getDate(password));
	}

	@OnClick(R.id.btn_back)
	public void back() {
		this.finish();
	}

	/**
	 * 提取编辑框的字符串
	 */
	private String getDate(EditText editText) {
		if (editText != null) {
			return editText.getText().toString().trim();
		}
		return "";
	}

	@OnClick(R.id.fogot_password)
	public void onfogotPasswordAction() {
		startActivity(ToFindPassWordActivity.getIntent(this));
	}

//	@OnClick(R.id.login_red_envelopes_register)
//	public void onRedEnvelopesRegisterAction() {
//		startActivityForResult(RegisterTelephonActivity.getIntent(this, true),
//				2);
//	}

	@OnClick(R.id.iv_sinaweibo)
	public void onSinaweiboOauthVerifyAction() {
		mAccountType = UserInfo.ACCOUNT_TYPE_SINAWEIBO;
		doOauthVerify(SHARE_MEDIA.SINA);
	}

	@OnClick(R.id.iv_qq)
	public void onQqOauthVerifyAction() {
		mAccountType = UserInfo.ACCOUNT_TYPE_QQ;
		configQQQZonePlatform(this);
		doOauthVerify(SHARE_MEDIA.QZONE);
	}

	@OnClick(R.id.iv_weixin)
	public void onWeixinOauthVerifyAction() {
		configWeixin(this);
		mAccountType = UserInfo.ACCOUNT_TYPE_WEIXIN;
		doOauthVerify(SHARE_MEDIA.WEIXIN);
	}

//	@OnClick(R.id.iv_renren)
//	public void onRenrenOauthVerifyAction() {
//		mAccountType = UserInfo.ACCOUNT_TYPE_RENREN;
//		doOauthVerify(SHARE_MEDIA.RENREN);
//	}

	@OnClick(R.id.iv_tencentweibo)
	public void onTencentweiboOauthVerifyAction() {
		mAccountType = UserInfo.ACCOUNT_TYPE_TENCENTWEIBO;
		doOauthVerify(SHARE_MEDIA.TENCENT);
	}

	/**
	 * 授权登录
	 * 
	 * @param type
	 *            类型
	 */
	private void doOauthVerify(final SHARE_MEDIA type) {
		showLoadingView();
		mController.doOauthVerify(this, type, new UMAuthListener() {
			@Override
			public void onError(SocializeException e, SHARE_MEDIA platform) {
				LogUtil.w(TAG, platform.name() + "授权失败:" + e.toString());
				hideLoadingView();
			}

			@Override
			public void onComplete(Bundle value, SHARE_MEDIA platform) {
				if (value != null && !TextUtils.isEmpty(value.getString("uid"))) {
					LogUtil.d(TAG, platform.name() + "授权成功");
					final String token = value.getString("access_token");
					mController.getPlatformInfo(LoginActivity.this, type,
							new SocializeListeners.UMDataListener() {
								@Override
								public void onStart() {
									LogUtil.d(TAG, "正在获得授权用户信息");
								}

								@Override
								public void onComplete(int status,
										Map<String, Object> info) {
									if (status == 200 && info != null) {
										OauthVerify oauth;
										try {
											oauth = OauthVerifyUtil.doParse(
													type, info,token);
										} catch (PackageManager.NameNotFoundException e) {
											e.printStackTrace();
											LogUtil.e(
													TAG,
													getString(R.string.user_info_parse_error));
											hideLoadingView();
											return;
										}
										userOpenLogin(oauth);
									} else {
										hideLoadingView();
										LogUtil.d(TAG, "获得用户信息发生错误：" + status);
									}
								}
							});
				} else {
					hideLoadingView();
					LogUtil.w(TAG, platform.name() + "授权失败");
				}
			}

			@Override
			public void onCancel(SHARE_MEDIA platform) {
				LogUtil.w(TAG, platform.name() + "取消授权");
				hideLoadingView();
			}

			@Override
			public void onStart(SHARE_MEDIA platform) {
				LogUtil.w(TAG, platform.name() + "开始授权");
			}

		});
		// mController.doOauthVerify(this, platform, new UMAuthListener() {
		//
		// @Override
		// public void onStart(SHARE_MEDIA platform) {
		// hideLoadingView();
		// }
		//
		// @Override
		// public void onError(SocializeException e, SHARE_MEDIA platform) {
		// hideLoadingView();
		// }
		//
		// @Override
		// public void onComplete(Bundle value, SHARE_MEDIA platform) {
		// String uid = value.getString("uid");
		// hideLoadingView();
		// if (!TextUtils.isEmpty(uid)) {
		//
		// } else {
		// }
		// }
		//
		// @Override
		// public void onCancel(SHARE_MEDIA platform) {
		// hideLoadingView();
		// }
		//
		// });
	}

	private static void configWeixin(Activity context) {
		// wx967daebe835fbeac是你在微信开发平台注册应用的AppID, 这里需要替换成你注册的AppID
		// 添加微信平台
		UMWXHandler wxHandler = new UMWXHandler(context,
				OauthKey.KEY_WEIXIN_ID, OauthKey.KEY_WEIXIN_SECRET);
		wxHandler.addToSocialSDK();

		// 支持微信朋友圈
		UMWXHandler wxCircleHandler = new UMWXHandler(context,
				OauthKey.KEY_WEIXIN_ID, OauthKey.KEY_WEIXIN_SECRET);
		wxCircleHandler.setToCircle(true);
		wxCircleHandler.addToSocialSDK();
	}

	private static void configQQQZonePlatform(Activity context) {
		// 添加QQ支持, 并且设置QQ分享内容的target url
		UMQQSsoHandler qqSsoHandler = new UMQQSsoHandler(context,
				OauthKey.KEY_QQ_ID, OauthKey.KEY_QQ_SECRET);
		qqSsoHandler.setTargetUrl("http://www.umeng.com/social");
		qqSsoHandler.addToSocialSDK();

		// 添加QZone平台
		QZoneSsoHandler qZoneSsoHandler = new QZoneSsoHandler(context,
				OauthKey.KEY_QQ_ID, OauthKey.KEY_QQ_SECRET);
		qZoneSsoHandler.addToSocialSDK();
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		switch (requestCode) {
		case 1: // 注册
			if (resultCode == RESULT_OK) {
				finish();
			}
			break;
		case 2: // 红包快捷注册,不作处理
			if (resultCode == RESULT_OK) {
				finish();
			}
			break;
		default:
			/** 使用SSO授权必须添加如下代码 */
			UMSsoHandler ssoHandler = mController.getConfig().getSsoHandler(
					requestCode);
			if (ssoHandler != null) {
				ssoHandler.authorizeCallBack(requestCode, resultCode, data);
			}
			break;
		}
	}

	/**
	 * 联网，登录
	 */
	private void login(String account, String passwd) {
		LoginRequest request = new LoginRequest();
		request.setAccount(account);
		request.setPasswd(MD5.getMd5(passwd));
		request.setSaasRequest(false);
		Constants.userInfo = new UserInfo();
		showLoadingView();
		closeKeyBroad();
		APIClient.login(request, new LoginHttpResponseHandler());
	}

	/**
	 * 第三方登录
	 */
	private void userOpenLogin(OauthVerify oauth) {
		APIClient.userOpen(new UserOpenRequest(oauth),
				new LoginHttpResponseHandler());
	}

	/**
	 * 服务器登录成功用户信息解析
	 */
	class LoginHttpResponseHandler extends AsyncHttpResponseHandler {

		@Override
		protected void onPreExecute() {
			if (mLoginHandler != null) {
				mLoginHandler.cancle();
			}
			ImageLoader.getInstance().clearDiscCache();
			ImageLoader.getInstance().clearMemoryCache();
			mLoginHandler = this;
		}

		@Override
		public void onFinish() {
			mLoginHandler = null;
			AppPushManager.startWork(LoginActivity.this);
			hideLoadingView();
		}

		@Override
		public void onSuccess(int statusCode, String content) {
			LoginResponse response;
			try {
				ResponseUtil.checkResponse(content);
				response = new Gson().fromJson(content, LoginResponse.class);
			} catch (Exception e) {
				showToast(R.string.data_format_error);
				hideLoadingView();
				LogUtil.w(TAG, e);
				return;
			}
			if (response.isSuccess()) {
				Constants.userInfo = response.getData();
				Constants.userInfo.setAccountType(mAccountType);
				Constants.saveUserInfo();
				ChanneDB.clearUselessKeywordChannel(App.CONTEXT);
				Intent intent = new Intent(
						Actions.ACTION_SYNC_SYSTEM_CHANNE_MUST);
				LocalBroadcastManager.getInstance(LoginActivity.this)
						.sendBroadcast(intent);
				Intent intent2 = new Intent(LoginActivity.this,
						StartServiceReceiver.class);
				intent2.setAction(StartServiceReceiver.ACTION_SYNC_SERVICE_START);
				sendBroadcast(intent2);
				if (Constants.userInfo.getType() != null
						&& Constants.userInfo.getType().equals("1")) {
					BaseRequest br = new BaseRequest();
					br.setSaasRequest(true);
					APIClient.asynChannelList(br, mFetchChannelListHttpHandler);
				} else {
					setResult(RESULT_OK);
					finish();
				}

				UserinfoUtil.getUserSettingProperties();
			} else {
				hideLoadingView();
				showToast(response.getMsg());
			}
		}

		@Override
		public void onFailure(Throwable error, String content) {
			showToast(R.string.load_server_failure);
			hideLoadingView();
		}
	}

	AsyncHttpResponseHandler mFetchChannelListHttpHandler = new AsyncHttpResponseHandler() {

		@Override
		public void onSuccess(int statusCode, String content) {
			ChannelListResponse response;
			try {
				ResponseUtil.checkResponse(content);
				response = new Gson().fromJson(content,
						ChannelListResponse.class);
			} catch (Exception e) {
				showToast(R.string.data_format_error);
				return;
			}
			if (response.isSuccess()) {
				List<Channe> channels = response.getData();
				if (!HttpChannelUtil.isChannelSameToServer(mCon,
						Constants.userInfo, channels)) {
					ChanneDB.delChannelByEnterpriseId(mCon, Constants.userInfo
							.getEnterprise().getId());
					for (Channe cha : channels) {
						ChanneDB.saveChannel(mCon, HttpChannelUtil
								.composeChanneDaoForEnterPrise(cha,
										Constants.userInfo));
					}
					Intent intent = new Intent();
					intent.setAction(Actions.ACTION_ENTERPRISE_CHANNEL_CHANGED);
					sendBroadcast(intent);
				}
				setResult(RESULT_OK);
				finish();
			} else {
				hideLoadingView();
				showToast(response.getMsg());
			}
		}

		@Override
		public void onFailure(Throwable error, String content) {
			showToast(R.string.load_server_failure);
			Log.e("请求失败", "Url：" + mFetchChannelListHttpHandler.getUrl()
					+ "结果：" + content);
			hideLoadingView();
		}

		@Override
		public void onFinish() {
			hideLoadingView();
			super.onFinish();
		}
	};

	/**
	 * 关闭键盘
	 */
	private void closeKeyBroad() {

		InputMethodManager m = (InputMethodManager) this
				.getSystemService(Context.INPUT_METHOD_SERVICE);
		m.hideSoftInputFromWindow(password.getWindowToken(), 0);
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		if (mLoginHandler != null) {
			mLoginHandler.cancle();
			mLoginHandler = null;
		}
		if (callback != null) {
			callback.login();
			callback = null;
		}
	}
}
