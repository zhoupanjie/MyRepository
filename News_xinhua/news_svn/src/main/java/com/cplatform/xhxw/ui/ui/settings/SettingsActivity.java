package com.cplatform.xhxw.ui.ui.settings;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.content.LocalBroadcastManager;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

import com.cplatform.xhxw.ui.App;
import com.cplatform.xhxw.ui.AppPushManager;
import com.cplatform.xhxw.ui.Constants;
import com.cplatform.xhxw.ui.R;
import com.cplatform.xhxw.ui.StatisticalKey;
import com.cplatform.xhxw.ui.http.APIClient;
import com.cplatform.xhxw.ui.http.ResponseUtil;
import com.cplatform.xhxw.ui.http.net.GetVersionInfoRequest;
import com.cplatform.xhxw.ui.http.net.GetVersionInfoResponse;
import com.cplatform.xhxw.ui.http.responseType.NewsDetTextSize;
import com.cplatform.xhxw.ui.model.UserInfo;
import com.cplatform.xhxw.ui.receiver.StartServiceReceiver;
import com.cplatform.xhxw.ui.ui.about.AboutActivity;
import com.cplatform.xhxw.ui.ui.base.BaseActivity;
import com.cplatform.xhxw.ui.ui.base.view.SwitchButton;
import com.cplatform.xhxw.ui.ui.feedback.FeedbackActivity;
import com.cplatform.xhxw.ui.ui.login.LoginActivity;
import com.cplatform.xhxw.ui.util.Actions;
import com.cplatform.xhxw.ui.util.AppBrightnessManager;
import com.cplatform.xhxw.ui.util.ClearUtil;
import com.cplatform.xhxw.ui.util.ClearUtil.OnClearListener;
import com.cplatform.xhxw.ui.util.CommonUtils;
import com.cplatform.xhxw.ui.util.DisplayImageOptionsUtil;
import com.cplatform.xhxw.ui.util.Engine;
import com.cplatform.xhxw.ui.util.HuanCunSizeUtil;
import com.cplatform.xhxw.ui.util.ShareUtil;
import com.cplatform.xhxw.ui.util.UpdateVersion;
import com.google.gson.Gson;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.umeng.analytics.MobclickAgent;
import com.wbtech.ums.UmsAgent;

/**
 * 设置界面 Created by cy-love on 14-1-17.
 */
public class SettingsActivity extends BaseActivity implements OnClearListener {

	/**
	 * 用户登录
	 */
	public static final int REQUEST_CODE_LOGIN = 1;

//	@InjectView(R.id.iv_avatar)
//	ImageView mAvatar;
//	@InjectView(R.id.tv_nickname)
//	TextView mNickname;
	@InjectView(R.id.btn_logout)
	Button mLogout;
	@InjectView(R.id.sw_push_setting)
	SwitchButton mPushSetting;
	@InjectView(R.id.tv_text_size)
	TextView mTextSize; // 新闻详情当前显示字体大小

//	/** 意见反馈 */
//	@InjectView(R.id.setting_feed_back)
//	RelativeLayout backFeedLayout;

	/** 关于 */
	@InjectView(R.id.setting_about)
	RelativeLayout aboutLayout;

	/** 清除缓存 */
	@InjectView(R.id.setting_clear)
	RelativeLayout clearLayout;

	@InjectView(R.id.clear_progress)
	ProgressBar clearProgress;

	@InjectView(R.id.clear_text)
	TextView clearText;

	private RelativeLayout mRecommendLo;
//	private RelativeLayout mVersionUpdateLo;
	private RelativeLayout mChangePwdLo;
	private SwitchButton mNightModeSBtn;
//	private ImageView mNewVersionAlert;

	private ProgressDialog mProgressDialog;

	// 清除状态
	private boolean isClearing = false;

	private ClearUtil clearUtil;

	private boolean isClearAvailable = false;

	private Handler cacheHandler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			if (msg.what == 0) {
				clearText.setText(msg.getData().getString("size"));
				isClearAvailable = true;
			}
			super.handleMessage(msg);
		}
	};

	public static Intent getIntent(Context context) {
		Intent intent = new Intent(context, SettingsActivity.class);
		return intent;
	}

	@Override
	protected String getScreenName() {
		return "SettingsActivity";
	}

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_settings);
		ButterKnife.inject(this);
		initActionBar();
		mPushSetting
				.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
					@Override
					public void onCheckedChanged(CompoundButton buttonView,
							boolean isChecked) {
						AppPushManager.setOpenPush(isChecked);
						if (isChecked) {
							AppPushManager.startWork(SettingsActivity.this);
							UmsAgent.onEvent(SettingsActivity.this,
									StatisticalKey.menu_set_push,
									new String[] { StatisticalKey.key_type },
									new String[] { StatisticalKey.on });
						} else {
							AppPushManager.stopWork(SettingsActivity.this);
							UmsAgent.onEvent(SettingsActivity.this,
									StatisticalKey.menu_set_push,
									new String[] { StatisticalKey.key_type },
									new String[] { StatisticalKey.off });
						}
					}
				});
		initViews();
		getVersionInfo(false);
	}

	private void initViews() {
		mRecommendLo = (RelativeLayout) findViewById(R.id.setting_share_app);
//		mVersionUpdateLo = (RelativeLayout) findViewById(R.id.setting_update_app);
		mChangePwdLo = (RelativeLayout) findViewById(R.id.setting_change_password);
		mNightModeSBtn = (SwitchButton) findViewById(R.id.sw_day_model_setting);
		mNightModeSBtn
				.setChecked(App.getDispalyModel() == Constants.DISPLAY_MODEL_NIGHT);
//		mNewVersionAlert = (ImageView) findViewById(R.id.setting_new_version_alert_iv);
//		mNewVersionAlert.setVisibility(View.GONE);

		mRecommendLo.setOnClickListener(mOnclick);
//		mVersionUpdateLo.setOnClickListener(mOnclick);
		mChangePwdLo.setOnClickListener(mOnclick);
		mChangePwdLo.setVisibility(View.GONE);
		if (Constants.hasLogin()
				&& Constants.userInfo.getAccountType() == UserInfo.ACCOUNT_TYPE_XUNWEN) {
			mChangePwdLo.setVisibility(View.VISIBLE);
		}
		mNightModeSBtn
				.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
					@Override
					public void onCheckedChanged(CompoundButton buttonView,
							boolean isChecked) {
						int model = App.getDispalyModel();
						String type = "";
						if (model == Constants.DISPLAY_MODEL_DAY) {
							model = Constants.DISPLAY_MODEL_NIGHT;
							type = "night";
						} else {
							model = Constants.DISPLAY_MODEL_DAY;
							type = "day";
						}
						App.getPreferenceManager().setDisplayModel(model);
						AppBrightnessManager
								.setScreenBrightness(SettingsActivity.this);

						UmsAgent.onEvent(SettingsActivity.this,
								StatisticalKey.menu_set_daynight,
								new String[] { StatisticalKey.key_type },
								new String[] { type });
					}
				});
	}

	@Override
	protected void onResume() {
		init();
		super.onResume();
	}

	OnClickListener mOnclick = new OnClickListener() {

		@Override
		public void onClick(View v) {
			int id = v.getId();
			switch (id) {
			case R.id.setting_share_app:
				shareApp();
				break;
//			case R.id.setting_update_app:
//				UmsAgent.onEvent(SettingsActivity.this,
//						StatisticalKey.menu_set_update);
//				UpdateVersion updateVersion = UpdateVersion
//						.getInstance(SettingsActivity.this);
//				updateVersion.isUpdate();
//				mProgressDialog = ProgressDialog.show(SettingsActivity.this,
//						"检测更新", "请稍候...");
//				getVersionInfo(true);
//				break;
			case R.id.setting_change_password:
				Intent it = new Intent();
				it.setClass(SettingsActivity.this, ChangePasswordActivity.class);
				startActivity(it);
				break;

			default:
				break;
			}
		}
	};

	private void init() {
		if (Constants.hasLogin()) {
//			ImageLoader.getInstance().displayImage(
//					Constants.userInfo.getLogo(), mAvatar,
//					DisplayImageOptionsUtil.avatarSmallImagesOptions);
//			mNickname.setText(Constants.userInfo.getNickName());
			mLogout.setVisibility(View.VISIBLE);
			if (Constants.userInfo.getAccountType() == UserInfo.ACCOUNT_TYPE_XUNWEN) {
				mChangePwdLo.setVisibility(View.VISIBLE);
			}
		} else {
//			mAvatar.setImageResource(R.drawable.ic_avatar_bg);
//			mNickname.setText(R.string.not_login);
			mLogout.setVisibility(View.GONE);
		}
		// 设置当前字体
		switch (Constants.getNewsDetTextSize()) {
		case NewsDetTextSize.SUPER_BIG:
			mTextSize.setText(R.string.super_big_text_size);
			break;
		case NewsDetTextSize.BIG:
			mTextSize.setText(R.string.big_text_size);
			break;
		case NewsDetTextSize.MIDDLE:
			mTextSize.setText(R.string.middle_text_size);
			break;
		case NewsDetTextSize.SMALL:
			mTextSize.setText(R.string.small_text_size);
			break;
		}
		mPushSetting.setChecked(AppPushManager.hashOpenPush());

		clearText.setText(R.string.caculating);
		HuanCunSizeUtil.getCachedFileSize(getExternalCacheDir(), getCacheDir(),
				cacheHandler);
	}
//
//	@OnClick(R.id.ly_avatar)
//	public void onAvatar() {
//		UmsAgent.onEvent(this, StatisticalKey.menu_set_head);
//		if (Constants.hasLogin()) {
//			startActivity(UserInfoActivity.getIntent(this));
//			// aaa
//			// UmsAgent.onEvent(this, StatisticalKey.);
//		} else {
//			startActivityForResult(LoginActivity.getIntent(this),
//					REQUEST_CODE_LOGIN);
//		}
//	}

	@OnClick(R.id.btn_logout)
	public void onLogOutAction() {
		if (CommonUtils.isFastDoubleClick())
			return;
		new AlertDialog.Builder(this)
				.setTitle(R.string.hint)
				.setMessage(R.string.logout_hint)
				.setNegativeButton(R.string.cancel, null)
				.setPositiveButton(R.string.logout,
						new DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								Constants.logout();
								Intent intent = new Intent(
										Actions.ACTION_SYNC_SYSTEM_CHANNE_MUST);
								LocalBroadcastManager.getInstance(
										SettingsActivity.this).sendBroadcast(
										intent);
								Intent intent2 = new Intent(
										SettingsActivity.this,
										StartServiceReceiver.class);
								intent2.setAction(StartServiceReceiver.ACTION_SYNC_SERVICE_START);
								sendBroadcast(intent2);
								setResult(RESULT_OK);
								init();
							}
						}).show();
	}

	@OnClick(R.id.ly_news_det_text_size)
	public void onChangeNewsTextSizeAction() {
		if (CommonUtils.isFastDoubleClick())
			return;
		new AlertDialog.Builder(this)
				.setTitle(getString(R.string.main_body_typeface))
				.setSingleChoiceItems(
						new String[] { getString(R.string.super_big_text_size),
								getString(R.string.big_text_size),
								getString(R.string.middle_text_size),
								getString(R.string.small_text_size) },
						Constants.getNewsDetTextSize(),
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog,
									int which) {
								int size;
								String text;
								switch (which) {
								case 0:
									size = NewsDetTextSize.SUPER_BIG;
									text = getString(R.string.super_big_text_size);
									break;
								case 1:
									size = NewsDetTextSize.BIG;
									text = getString(R.string.big_text_size);
									break;
								case 2:
									size = NewsDetTextSize.MIDDLE;
									text = getString(R.string.middle_text_size);
									break;
								default:
									size = NewsDetTextSize.SMALL;
									text = getString(R.string.small_text_size);
									break;
								}
								mTextSize.setText(text);
								Constants.setNewsDetTextSize(size);
								UmsTextMode();
								dialog.dismiss();
							}
						})
				.setNegativeButton(R.string.cancel,
						new DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								UmsTextMode();
							}
						}).setOnCancelListener(new OnCancelListener() {
					@Override
					public void onCancel(DialogInterface dialog) {
						UmsTextMode();
					}
				}).show();
	}

	public void UmsTextMode() {
		UmsAgent.onEvent(SettingsActivity.this,
				StatisticalKey.menu_set_textmode,
				new String[] { StatisticalKey.key_type },
				new String[] { Constants.getNewsDetTextSize() + "" });
	}

//	/** 意见反馈 */
//	@OnClick(R.id.setting_feed_back)
//	public void setBackFeed() {
//		UmsAgent.onEvent(SettingsActivity.this,
//				StatisticalKey.menu_set_feedback);
//		startActivity(FeedbackActivity.getIntent(this));
//	}

	/** 关于 */
	@OnClick(R.id.setting_about)
	public void setAbout() {
		UmsAgent.onEvent(SettingsActivity.this, StatisticalKey.menu_set_about);
		startActivity(AboutActivity.getIntent(this));
	}

	/** 清除缓存 */
	@OnClick(R.id.setting_clear)
	public void setClear() {
		if (isClearAvailable) {
			clearUtil = new ClearUtil(this, getExternalCacheDir(),
					getCacheDir());
			clearUtil.setClearListener(this);
			clearUtil.execute();
		}
		UmsAgent.onEvent(SettingsActivity.this, StatisticalKey.menu_set_clear);
	}

	@Override
	public void onPreClear() {
		isClearing = true;
		clearText.setVisibility(View.GONE);
		clearProgress.setVisibility(View.VISIBLE);
	}

	@Override
	public void onPostClear() {
		clearUtil = null;
		isClearing = false;
		clearProgress.setVisibility(View.GONE);
		clearText.setVisibility(View.VISIBLE);
		clearText.setText("0.0M");
	}

	/**
	 * 监听--返回键
	 * */
	@Override
	public boolean onKeyUp(int keyCode, KeyEvent event) {
		switch (keyCode) {
		case KeyEvent.KEYCODE_BACK:
			if (!isClearing) {
				finish();
			}
		}
		return super.onKeyUp(keyCode, event);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (resultCode == RESULT_OK && requestCode == REQUEST_CODE_LOGIN) {
			setResult(RESULT_OK);
		}
		super.onActivityResult(requestCode, resultCode, data);
	}

	private void shareApp() {
		MobclickAgent.onEvent(this, StatisticalKey.menu_set_recommend);

		UmsAgent.onEvent(this, StatisticalKey.menu_set_recommend);

		String share = getString(R.string.share);

		ShareUtil.share_type = 1;
		ShareUtil.sendTextIntent(this, null, share, share, share,
				getResources().getString(R.string.invite_content),
				getResources().getString(R.string.invite_content_cick_url),
				false, true, false, null, true);
	}

	/** 获取版本属性 */
	private void getVersionInfo(final boolean isManual) {

		GetVersionInfoRequest request = new GetVersionInfoRequest();
		request.setDevice_type("android_phone");
		request.setVersion_no(Engine.getVersionCode(this));

		APIClient.getVersionInfo(request, new AsyncHttpResponseHandler() {

			@Override
			public void onFinish() {
				if (mProgressDialog != null) {
					mProgressDialog.dismiss();
				}
			}

			@Override
			protected void onPreExecute() {
			}

			@Override
			public void onSuccess(String content) {
				final GetVersionInfoResponse response;
				try {
					ResponseUtil.checkResponse(content);
					response = new Gson().fromJson(content,
							GetVersionInfoResponse.class);
				} catch (Exception e) {
					return;
				}

				if (response != null) {
					if (response.isSuccess()) {
						if (response.getData() != null) {
							if (Engine.getVersionCode(getApplicationContext()) < Integer
									.valueOf(response.getData().getVersion_no())) {
//								mNewVersionAlert.setVisibility(View.VISIBLE);
							} else {
//								mNewVersionAlert.setVisibility(View.GONE);
								if (isManual) {
									showToast("已是最新版本");
								}
							}
						}
					}
				}
			}

			@Override
			public void onFailure(Throwable error, String content) {
			}
		});
	}

	
}