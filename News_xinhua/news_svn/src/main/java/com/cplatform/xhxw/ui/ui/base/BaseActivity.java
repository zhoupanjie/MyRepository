package com.cplatform.xhxw.ui.ui.base;

import me.imid.swipebacklayout.lib.app.SwipeBackActivity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.cplatform.xhxw.ui.App;
import com.cplatform.xhxw.ui.AppPushManager;
import com.cplatform.xhxw.ui.PreferencesManager;
import com.cplatform.xhxw.ui.R;
import com.cplatform.xhxw.ui.location.LocationClientController;
import com.cplatform.xhxw.ui.ui.base.view.CheckDrawPrizeAlertDialog;
import com.cplatform.xhxw.ui.ui.base.view.ShareActionSheet;
import com.cplatform.xhxw.ui.ui.base.widget.LoadingView;
import com.cplatform.xhxw.ui.ui.main.HomeActivity;
import com.cplatform.xhxw.ui.ui.main.forelanguage.activity.ForeignLanguageHomeActivity;
import com.cplatform.xhxw.ui.ui.main.forelanguage.utils.LanguageUtil;
import com.cplatform.xhxw.ui.util.AppBrightnessManager;
import com.cplatform.xhxw.ui.util.MessageUtil;
import com.umeng.analytics.MobclickAgent;
import com.wbtech.ums.UmsAgent;

public abstract class BaseActivity extends SwipeBackActivity {

	private MessageUtil mMsg;
	private Toast mToast;
	protected boolean isDestroy;
	private LoadingView mLoadingView;
	public LocationClientController mLocationClient;
	public boolean gotoHome = true;

	protected boolean has_channel_id = false;

	/**
	 * 显示Message信息
	 */
	protected void showMessage(String msg) {
		if (isDestroy)
			return;
		if (mMsg == null)
			mMsg = new MessageUtil(this);
		mMsg.showMsg(msg);
	}

	/**
	 * 显示toast
	 */
	public void showToast(String msg) {
		if (isDestroy)
			return;
		// 不用getApplicationContext的话,导致内存泄露
		if (mToast == null)
			mToast = Toast.makeText(this.getApplicationContext(), "",
					Toast.LENGTH_SHORT);
		mToast.setText(msg);
		mToast.show();
	}

	/**
	 * 显示toast
	 */
	public void showToast(int msg) {
		showToast(getString(msg));
	}

	@Override
	protected void onDestroy() {
		isDestroy = true;
		super.onDestroy();
		ScreenManager.getScreenManager().popActivity(this);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == ShareActionSheet.REQUEST_CODE_SMS
				|| requestCode == ShareActionSheet.REQUEST_CODE_EMAIL) {
			ShareActionSheet.checkDrawPrize(PreferencesManager
					.getDrawPrizeUrl(this));
		}
		super.onActivityResult(requestCode, resultCode, data);
	}

	protected void initActionBar() {
		View back = findViewById(R.id.btn_back);
		if (back != null) {
			back.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View v) {
					finish();
				}
			});
		}
	}

	protected void startActivity(Class<?> cls) {
		startActivity(new Intent(this, cls));
	}

	@Override
	public void setContentView(int layoutResID) {
		super.setContentView(layoutResID);
		addLoadingLayout();
	}

	@Override
	public void setContentView(View view) {
		super.setContentView(view);
		addLoadingLayout();
	}

	@Override
	public void setContentView(View view, ViewGroup.LayoutParams params) {
		super.setContentView(view, params);
		addLoadingLayout();
	}

	private void addLoadingLayout() {
		mLoadingView = new LoadingView(getBaseContext());

		mLoadingView.setOnTouchListener(new View.OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				return true;
			}
		});
		hideLoadingView();
		addContentView(mLoadingView, new ViewGroup.LayoutParams(
				ViewGroup.LayoutParams.MATCH_PARENT,
				ViewGroup.LayoutParams.MATCH_PARENT));
	}

	public void showLoadingView() {
		showLoadingView(null);
	}

	public void showLoadingView(int res) {
		showLoadingView(getString(res));
	}

	public void showLoadingView(String msg) {
		mLoadingView.setVisibility(View.VISIBLE);
		mLoadingView.setMsg(msg);
	}

	public void hideLoadingView() {
		mLoadingView.setVisibility(View.GONE);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		UmsAgent.postClientData(this);// activity切换
		ScreenManager.getScreenManager().pushActivity(this, true);
		MobclickAgent.openActivityDurationTrack(false);
		if (AppPushManager.hashOpenPush()) {
			AppPushManager.startWork(this);
		} else {
			AppPushManager.stopWork(this);
		}
		mLocationClient = new LocationClientController(this);
	}

	public void startActivity(Intent intent, boolean isDefaultAnim) {
		super.startActivity(intent);
		if (isDefaultAnim) {
			executeStartActivityAnim();
		}
	}

	@Override
	public void startActivity(Intent intent) {
		super.startActivity(intent);
		executeStartActivityAnim();
	}

	public void startNoAnimActivity(Intent intent) {
		super.startActivity(intent);
		overridePendingTransition(0, 0);
	}

	@Override
	public void startActivityFromFragment(Fragment fragment, Intent intent,
			int requestCode) {
		super.startActivityFromFragment(fragment, intent, requestCode);
		executeStartActivityAnim();
	}

	@Override
	public void startActivityForResult(Intent intent, int requestCode) {
		try {
			super.startActivityForResult(intent, requestCode);
			executeStartActivityAnim();
		} catch (Exception e) {
			e.printStackTrace();
			Toast.makeText(this, R.string.no_app_found, Toast.LENGTH_LONG)
					.show();
		}
	}

	public void startNoAnimActivityForResult(Intent intent, int requestCode) {
		try {
			super.startActivityForResult(intent, requestCode);
			overridePendingTransition(0, 0);
		} catch (Exception e) {
			e.printStackTrace();
			Toast.makeText(this, R.string.no_app_found, Toast.LENGTH_LONG)
					.show();
		}
	}

	@Override
	public void finish() {
		if (gotoHome) {
			boolean isRun = HomeActivity.isStart()
					|| ForeignLanguageHomeActivity.isForeignStart();
			if (!isRun) {
				if (PreferencesManager.getLanguageInfo(this).equals(
						LanguageUtil.LANGUAGE_CH)) {
					startActivity(HomeActivity.class);
				} else {
					startActivity(ForeignLanguageHomeActivity.class);
				}
			}
		}
		super.finish();
		executeFinishActivityAnim();
	}

	public void finish(boolean isDefaultAnim) {
		boolean isRun = HomeActivity.isStart()
				|| ForeignLanguageHomeActivity.isForeignStart();
		if (!isRun) {
			if (PreferencesManager.getLanguageInfo(this).equals(
					LanguageUtil.LANGUAGE_CH)) {
				startActivity(HomeActivity.class);
			} else {
				startActivity(ForeignLanguageHomeActivity.class);
			}
		}
		super.finish();
		if (isDefaultAnim) {
			executeFinishActivityAnim();
		}
	}

	protected void executeStartActivityAnim() {
		overridePendingTransition(getEnterAnim(), 0);
	}

	protected void executeFinishActivityAnim() {
		overridePendingTransition(0, getExitAnim());
	}

	/**
	 * 进入动画
	 */
	protected int getEnterAnim() {
		return R.anim.activity_push_right_in;
	}

	/**
	 * 退出动画
	 */
	protected int getExitAnim() {
		return R.anim.activity_push_right_out;
	}

	/**
	 * 获得屏幕名称，用于友盟统计
	 * 
	 * @return
	 */
	protected abstract String getScreenName();

	protected void onResume() {
		super.onResume();
		App.getCurrentApp().cleanNotification();
		MobclickAgent.onPageStart(getScreenName());
		MobclickAgent.onResume(this);
		UmsAgent.onResume(this);
		AppBrightnessManager.setScreenBrightness(this);
		ScreenManager.getScreenManager().changeActivityStates(this, true);
		if (PreferencesManager.isToShowDrawPrizeAlert()) {
			new CheckDrawPrizeAlertDialog(this,
					PreferencesManager.getDrawPrizeUrl(this)).show();
			PreferencesManager.setDrawPrizeAlert(false);
		}
	}

	protected void onPause() {
		super.onPause();
		MobclickAgent.onPageEnd(getScreenName());
		MobclickAgent.onPause(this);
		if (has_channel_id) {
			UmsAgent.onPause(this, App.channel_id, App.news_id);
		} else {
			UmsAgent.onPause(this);
		}
	}

	@Override
	protected void onStop() {
		super.onStop();
		ScreenManager.getScreenManager().changeActivityStates(this, false);
	}
}
