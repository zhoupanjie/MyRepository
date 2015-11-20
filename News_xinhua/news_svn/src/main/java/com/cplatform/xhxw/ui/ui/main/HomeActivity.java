package com.cplatform.xhxw.ui.ui.main;

import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebSettings.LayoutAlgorithm;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.TextView;

import com.cplatform.xhxw.ui.App;
import com.cplatform.xhxw.ui.Constants;
import com.cplatform.xhxw.ui.PreferencesManager;
import com.cplatform.xhxw.ui.R;
import com.cplatform.xhxw.ui.StatisticalKey;
import com.cplatform.xhxw.ui.db.ChanneDB;
import com.cplatform.xhxw.ui.http.APIClient;
import com.cplatform.xhxw.ui.http.ResponseUtil;
import com.cplatform.xhxw.ui.http.httputils.HttpHanderUtil;
import com.cplatform.xhxw.ui.http.net.BaseRequest;
import com.cplatform.xhxw.ui.http.net.saas.ActivityCheckResponse;
import com.cplatform.xhxw.ui.http.net.saas.ActivityCheckResponse.ActivityData;
import com.cplatform.xhxw.ui.location.LocationUtil;
import com.cplatform.xhxw.ui.model.New;
import com.cplatform.xhxw.ui.patch.Patch;
import com.cplatform.xhxw.ui.receiver.StartServiceReceiver;
import com.cplatform.xhxw.ui.service.NewsCashService;
import com.cplatform.xhxw.ui.ui.base.BaseActivity;
import com.cplatform.xhxw.ui.ui.base.view.HomeOperationsBar;
import com.cplatform.xhxw.ui.ui.base.view.HomeOperationsBar.onHomeBottomBarClickListener;
import com.cplatform.xhxw.ui.ui.base.widget.InputViewSensitiveLinearLayout;
import com.cplatform.xhxw.ui.ui.main.cms.HomeFragment;
import com.cplatform.xhxw.ui.ui.main.cms.NewsFragment;
import com.cplatform.xhxw.ui.ui.main.cms.game.GameUtil;
import com.cplatform.xhxw.ui.ui.main.cms.personal.PersonalFragment;
import com.cplatform.xhxw.ui.ui.main.cms.radiobroadcast.RadioBroadcastFragment;
import com.cplatform.xhxw.ui.ui.main.cms.video.Player;
import com.cplatform.xhxw.ui.ui.main.forelanguage.activity.ForeignLanguageHomeActivity;
import com.cplatform.xhxw.ui.ui.main.forelanguage.utils.LanguageUtil;
import com.cplatform.xhxw.ui.util.AppExitUtil;
import com.cplatform.xhxw.ui.util.DateUtil;
import com.cplatform.xhxw.ui.util.LogUtil;
import com.cplatform.xhxw.ui.util.UpdateVersion;
import com.google.gson.Gson;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.umeng.analytics.MobclickAgent;
import com.umeng.socialize.controller.UMServiceFactory;
import com.umeng.socialize.controller.UMSocialService;
import com.umeng.socialize.sso.UMSsoHandler;
import com.wbtech.ums.UmsAgent;

/**
 * Created by cy-love on 13-12-24.
 */
public class HomeActivity extends BaseActivity implements
		onHomeBottomBarClickListener {

	private static final String TAG = HomeActivity.class.getSimpleName();

	/**
	 * 判断是否启动
	 * 
	 * @return
	 */
	public static boolean isStart() {
		return isStart;
	}

	public static void setStart(boolean start) {
		isStart = start;
	}

	private static boolean isStart;
	/**
	 * 用户登录
	 */
	public static final int REQUEST_CODE_LOGIN = 1;

	public static final boolean mIsUnderEnterprise = false;

	private InputViewSensitiveLinearLayout mActivityLo;
	private WebView mActivityWV;
	private Button mEnterBtn;
	private HomeOperationsBar mBottomBar;

	private HomeFragment mCMSMainFrangment;
	private RadioBroadcastFragment rbFragment;
	private NewsFragment mVideoFragment;
	private PersonalFragment mPersonalCenterFrag;
	private Fragment mCurrentDisplayedFragment;
	private AsyncHttpResponseHandler mLoadSumCountHandler;
	// 界面旋转所需属性
	// public RelativeLayout home_activity_root_ly;
	public static final String Translate_tag = "HomeActivity";
	private ViewGroup layout;
	private AsyncHttpResponseHandler mLoadHandler;
	private int mSelectedLanguageIndex = 1;

	private TextView mLanguageSwtichLoading;
	public static boolean isPlayRadio=false;//是否播放广播，用在新闻列表列表项跳转广播时
	public static String radioContent;//传递至
	private Handler mLanguageHandler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			if (msg.what == 0) {
				Intent it = new Intent(HomeActivity.this,
						ForeignLanguageHomeActivity.class);
				startActivity(it);
				finish();
			}
			super.handleMessage(msg);
		}
	};

	@Override
	protected String getScreenName() {
		return "HomeActivity";
	}

	public static void setPerformClick(View v) {
		v.performClick();
	}

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		long oldVersion = App.getPreferenceManager().getOldVersionCode();
		Patch.mainThreadCheckVersion(this, oldVersion);
		App.getPreferenceManager().setOldVsersionCode(
				Constants.AppInfo.getVsersionCode());
		MobclickAgent.updateOnlineConfig(this.getApplicationContext());
		isStart = true;
		LayoutInflater inflater = (LayoutInflater) this
				.getSystemService(LAYOUT_INFLATER_SERVICE);
		if (layout == null)
			layout = (ViewGroup) inflater.inflate(R.layout.activity_home, null);
		setContentView(layout);
		setSwipeBackEnable(false);
		init();

		// new Thread(new Runnable() {
		// @Override
		// public void run() {
		// // 清除一周以外的已读新闻记录 7 * 24 * 60 * 60 * 1000
		// long time = DateUtil.getTime() - 604800000;
		// ReadNewsDB.delReadNewsByLtTime(getApplicationContext(), time);
		// }
		// }).start();
		Intent intent = new Intent(this, StartServiceReceiver.class);
		intent.setAction(StartServiceReceiver.ACTION_SYNC_SERVICE_START);
		sendBroadcast(intent);
		getInfo();

		LocationUtil.getAPosition(getApplicationContext(), mLocationClient);
		loadActivityInfo();
	}

	private void init() {
		initViews();
		initFragment();

	}

	private void initViews() {
		mBottomBar = (HomeOperationsBar) findViewById(R.id.enterprise_bottom_bar);
		mBottomBar.setmOnHomeBottomBarClickLs(this);
		mActivityLo = (InputViewSensitiveLinearLayout) findViewById(R.id.home_activity_lo);
		mActivityLo
				.setOnInputViewListener(new InputViewSensitiveLinearLayout.OnInputViewListener() {
					@Override
					public void onShowInputView() {
						if (mActivityLo.getVisibility() == View.VISIBLE) {
							mEnterBtn.setVisibility(View.GONE);
						}
					}

					@Override
					public void onHideInputView() {
						if (mActivityLo.getVisibility() == View.VISIBLE) {
							mEnterBtn.setVisibility(View.VISIBLE);
							mActivityWV.invalidate();
						}
					}
				});
		mActivityWV = (WebView) findViewById(R.id.home_activity_webview);
		WebSettings mWebSettings = mActivityWV.getSettings();
		mWebSettings.setJavaScriptEnabled(true);
		mWebSettings.setLayoutAlgorithm(LayoutAlgorithm.NARROW_COLUMNS);
		mWebSettings.setUseWideViewPort(false);
		mEnterBtn = (Button) findViewById(R.id.home_activity_enter_btn);
		mEnterBtn.setOnClickListener(mOnclickLi);
		mLanguageSwtichLoading = (TextView) findViewById(R.id.home_language_switch_loading);
	}

	OnClickListener mOnclickLi = new OnClickListener() {

		@Override
		public void onClick(View v) {
			if (v.getId() == R.id.home_activity_enter_btn) {
				mActivityLo.setVisibility(View.GONE);
			}
		}
	};

	private void initFragment() {
		// 添加fragment
		FragmentTransaction t = getSupportFragmentManager().beginTransaction();
		if (mCMSMainFrangment == null) {
			mCMSMainFrangment = new HomeFragment();
		}
		if (mCMSMainFrangment.isAdded()) {
			t.show(mCMSMainFrangment);
		} else {
			t.replace(R.id.fg_home, mCMSMainFrangment);
		}

		t.commit();
		mCurrentDisplayedFragment = mCMSMainFrangment;
		if (rbFragment == null) {
			rbFragment = new RadioBroadcastFragment();
		}
		if (mVideoFragment == null) {
			mVideoFragment = new NewsFragment(true);
		}

	}

	private Receiver mReceiver;

	@Override
	protected void onResume() {
		ChanneDB.clearUselessKeywordChannel(this);
		mReceiver = new Receiver();
		IntentFilter filter = new IntentFilter();
		filter.addAction(NewsCashService.ACTION_NEWS_CASH_DONE);
		filter.addAction(NewsCashService.ACTION_NEWS_CASH_CHANGE);
		registerReceiver(mReceiver, filter);
		super.onResume();
	}

	@Override
	protected void onPause() {
		if (mReceiver != null) {
			unregisterReceiver(mReceiver);
			mReceiver = null;
		}
		super.onPause();
	}

	@Override
	protected void onNewIntent(Intent intent) {
		super.onNewIntent(intent);
		setIntent(intent);
		// here we can use getIntent() to get the extra data.
	}

	public void showFlag() {
	}

	public void hideFlag() {
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (mCMSMainFrangment != null && mCMSMainFrangment.mManageView.isShow()) {
			if (mCMSMainFrangment.mManageView.ismChange()) {
				showToast(R.string.alert_setting_saved);
			}
			mCMSMainFrangment.mManageView.closeAction();
			return true;
		}

		if (mVideoFragment != null && mVideoFragment.isFullPlay()) {
			mVideoFragment.showFullVideo(true);
			return true;
		}
		boolean bo = AppExitUtil.onKeyUp(this, keyCode, event);
		if (bo) {
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}

	private class Receiver extends BroadcastReceiver {

		@Override
		public void onReceive(Context context, Intent intent) {
			String action = intent.getAction();
			if (NewsCashService.ACTION_NEWS_CASH_CHANGE.equals(action)) {
			} else if (NewsCashService.ACTION_NEWS_CASH_DONE.equals(action)) {
			}
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		LogUtil.e("", "-----" + requestCode + " resultCode---" + resultCode);
		if (resultCode != RESULT_OK) {
			return;
		}

		/** 分享 */
		UMSocialService mController = UMServiceFactory.getUMSocialService(
				"com.umeng.share");
		/** 使用SSO授权必须添加如下代码 */
		UMSsoHandler ssoHandler = mController.getConfig().getSsoHandler(
				requestCode);
		if (ssoHandler != null) {
			ssoHandler.authorizeCallBack(requestCode, resultCode, data);
		}
		super.onActivityResult(requestCode, resultCode, data);
	}

	@Override
	protected void onDestroy() {
		removeHandler();

		mActivityLo.removeAllViews();
		mActivityLo = null;
		mActivityWV.destroy();
		mActivityWV = null;
		// home_activity_root_ly.removeAllViews();
		layout.removeAllViews();
		HttpHanderUtil.cancel(mLoadSumCountHandler);
		super.onDestroy();
		isStart = false;
		// for(Downloader d:GameUtil.downloaders){
		//
		// }
	}

	public void removeHandler() {
		if (null != mLoadHandler) {
			mLoadHandler.cancle();
			mLoadHandler = null;
		}
	}

	/**
	 * 判断是否需要更新项目
	 * */
	private void getInfo() {
		UpdateVersion updateVersion = UpdateVersion.getInstance(this);
		updateVersion.isUpdate();
	}
	/**
	 * 
	 * @Name setBroadcastArguments 
	 * @Description TODO 设置广播传入参数
	 * @param item 
	 * @return void
	 * @Author zxe
	 * @Date 2015年9月2日 下午2:51:17
	*
	 */
	public void setBroadcastArguments(New item){
		isPlayRadio=true;
		radioContent = GameUtil.getJsonTextByObject(item);
	}
	/**
	 * 根据点击的导航按钮类型，切换显示不同界面
	 */
	public void switchFragmentInMainAcitivy(int buttonType) {
		Fragment switchTo = null;
		if (buttonType == HomeOperationsBar.BOTTOM_BUTTON_TYPE_NEWS) {
			switchTo = mCMSMainFrangment;
		} else if (buttonType == HomeOperationsBar.BOTTOM_BUTTON_TYPE_BROADCAST) {
			switchTo = rbFragment;
			mBottomBar
					.setSelectedItem(HomeOperationsBar.BOTTOM_BUTTON_TYPE_BROADCAST);
		} else if (buttonType == HomeOperationsBar.BOTTOM_BUTTON_TYPE_VIDEO) {
			switchTo = mVideoFragment;
		} else if (buttonType == HomeOperationsBar.BOTTOM_BUTTON_TYPE_PERSONAL) {
			if (mPersonalCenterFrag == null) {
				mPersonalCenterFrag = new PersonalFragment();
			}
			switchTo = mPersonalCenterFrag;
		}

		if (switchTo != null) {
			switchContent(mCurrentDisplayedFragment, switchTo);
		}
	}

	/**
	 * 切换fragment
	 * 
	 * @param from
	 *            当前显示的fragment
	 * @param to
	 *            切换的目标fragment
	 */
	public void switchContent(Fragment from, Fragment to) {
		if (from != to) {
			Player.release();
			mCurrentDisplayedFragment = to;
			FragmentTransaction transaction = getSupportFragmentManager()
					.beginTransaction();
			transaction.setCustomAnimations(R.anim.activity_push_right_in,
					R.anim.activity_push_left_out);
			if (!to.isAdded()) {
				transaction.hide(from).add(R.id.fg_home, to);
			} else {
				transaction.hide(from).show(to);
			}
			transaction.commitAllowingStateLoss();
		}
	}

	/**
	 * 加载当前活动信息
	 */
	private void loadActivityInfo() {
		BaseRequest request = new BaseRequest();
		request.setSaasRequest(true);
		AsyncHttpResponseHandler mHandler = new AsyncHttpResponseHandler() {

			@Override
			protected void onPreExecute() {
				if (mLoadHandler != null)
					mLoadHandler.cancle();
				mLoadHandler = this;
			}

			@Override
			public void onSuccess(int statusCode, String content) {
				ActivityCheckResponse response;
				try {
					ResponseUtil.checkResponse(content);
					response = new Gson().fromJson(content,
							ActivityCheckResponse.class);
				} catch (Exception e) {
					response = null;
					return;
				}

				if (response != null && response.isSuccess()) {
					ActivityData data = response.getData();
					dealWithActivityResponse(data);
				}
			}
		};
		APIClient.fetchActivityInfo(request, mHandler);
	}

	private void dealWithActivityResponse(ActivityData data) {
		if (data.getExist() == 1) {
			long currentDate = System.currentTimeMillis() / 1000;
			if (currentDate > data.getBegin_time()
					&& currentDate < data.getEnd_time()) {
				mActivityLo.setVisibility(View.VISIBLE);
				mActivityLo.bringToFront();
				String url = data.getActivity_url();
				if (url == null) {
					mActivityLo.setVisibility(View.GONE);
					return;
				}
				if (url.indexOf("?") > 0) {
					url = url + "&userid=" + Constants.getUid();
				} else {
					url = url + "?userid=" + Constants.getUid();
				}
				mActivityWV.loadUrl(url);
				PreferencesManager.saveActivityDisplayDate(this,
						DateUtil.getFormattedBirthday(currentDate));
			}
		}
	}

	public void showSwitchLanguageAlert() {
		mSelectedLanguageIndex = 1;
		new AlertDialog.Builder(this)
				.setTitle("选择语言")
				.setSingleChoiceItems(new String[] { "中文", "English" }, 1,
						new DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								mSelectedLanguageIndex = which;
							}
						})
				.setPositiveButton(R.string.confirm,
						new DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								LogUtil.e(TAG, " index--------------" + which);
								if (mSelectedLanguageIndex == 0) {
									UmsLanguageMode();
									dialog.dismiss();
									return;
								}
								mLanguageSwtichLoading
										.setVisibility(View.VISIBLE);
								mLanguageSwtichLoading.bringToFront();
								PreferencesManager.saveLanguageInfo(
										HomeActivity.this,
										LanguageUtil.LANGUAGE_EN);
								UmsLanguageMode();
								Intent intent = new Intent(HomeActivity.this,
										StartServiceReceiver.class);
								intent.setAction(StartServiceReceiver.ACTION_SYNC_SERVICE_START);
								sendBroadcast(intent);
								mLanguageHandler.sendEmptyMessageDelayed(0,
										2000);
							}
						})
				.setNegativeButton(R.string.cancel,
						new DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								UmsLanguageMode();
							}
						}).setOnCancelListener(new OnCancelListener() {
					@Override
					public void onCancel(DialogInterface dialog) {
						UmsLanguageMode();
					}
				}).show();
	}

	public void UmsLanguageMode() {
		UmsAgent.onEvent(HomeActivity.this, StatisticalKey.menu_languagemode,
				new String[] { StatisticalKey.key_type },
				new String[] { PreferencesManager
						.getLanguageInfo(HomeActivity.this) });
	}

	@Override
	public void startActivity(Intent intent, boolean isDefaultAnim) {
		super.startActivity(intent, false);
	}

	protected void setActivityAnim() {
		overridePendingTransition(R.anim.activity_scale_translate_rotate,
				R.anim.activity_alpha_action_exit);
	}

	@Override
	public void onHomeBottomBarClick(int buttonType) {
		switchFragmentInMainAcitivy(buttonType);
	}

	public void showBottomBar(boolean isShow) {
		if (mBottomBar != null) {
			if (isShow) {
				mBottomBar.setVisibility(View.VISIBLE);
			} else {
				mBottomBar.setVisibility(View.GONE);
			}
		}
	}

	public void setTabbarShow(boolean isShow) {
		if (mCMSMainFrangment != null) {
			mCMSMainFrangment.setTabbarShow(isShow);
		}
	}
}