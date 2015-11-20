package com.cplatform.xhxw.ui.ui.base;

import com.cplatform.xhxw.ui.util.AppBrightnessManager;
import com.wbtech.ums.UmsAgent;

import android.app.Activity;
import android.os.Bundle;

public class BaseNormalActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		UmsAgent.postClientData(this);// activity切换
		ScreenManager.getScreenManager().pushActivity(this, true);
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		ScreenManager.getScreenManager().popActivity(this);
	}

	@Override
	protected void onStart() {
		super.onStart();
		ScreenManager.getScreenManager().changeActivityStates(this, true);
	}

	@Override
	protected void onResume() {
		UmsAgent.onResume(this);
		super.onResume();
		AppBrightnessManager.setScreenBrightness(this);
		ScreenManager.getScreenManager().changeActivityStates(this, true);
	}

	@Override
	protected void onPause() {
		UmsAgent.onPause(this);
		super.onPause();
	}

	@Override
	protected void onStop() {
		super.onStop();
		ScreenManager.getScreenManager().changeActivityStates(this, false);
	}
}
