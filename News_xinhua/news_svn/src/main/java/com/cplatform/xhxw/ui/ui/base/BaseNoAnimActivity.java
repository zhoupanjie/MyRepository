package com.cplatform.xhxw.ui.ui.base;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.cplatform.xhxw.ui.AppPushManager;
import com.cplatform.xhxw.ui.R;
import com.cplatform.xhxw.ui.location.LocationClientController;
import com.cplatform.xhxw.ui.ui.base.widget.LoadingView;
import com.cplatform.xhxw.ui.ui.main.HomeActivity;
import com.cplatform.xhxw.ui.util.AppBrightnessManager;
import com.cplatform.xhxw.ui.util.MessageUtil;
import com.umeng.analytics.MobclickAgent;
import com.wbtech.ums.UmsAgent;

import me.imid.swipebacklayout.lib.app.SwipeBackActivity;

/**
 * 无进入和退出动画
 */
public abstract class BaseNoAnimActivity extends FragmentActivity {

    private MessageUtil mMsg;
    private Toast mToast;
    protected boolean isDestroy;
    private LoadingView mLoadingView;
    public LocationClientController mLocationClient;

    /**
     * 显示Message信息
     */
    protected void showMessage(String msg) {
        if (isDestroy) return;
        if (mMsg == null) mMsg = new MessageUtil(this);
        mMsg.showMsg(msg);
    }

    /**
     * 显示toast
     */
    public void showToast(String msg) {
        if (isDestroy) return;
        if (mToast == null) mToast = Toast.makeText(this.getApplicationContext(), "", Toast.LENGTH_SHORT);
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
        addContentView(mLoadingView, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
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

    @Override
    public void startActivityForResult(Intent intent, int requestCode) {
        try {
        	super.startActivityForResult(intent, requestCode);
		} catch (Exception e) {
			e.printStackTrace();
			Toast.makeText(this, R.string.no_app_found, Toast.LENGTH_LONG).show();
		}
    }

    @Override
    public void finish() {
        boolean isRun = HomeActivity.isStart();
        if (!isRun) startActivity(HomeActivity.class);
        super.finish();
    }

    /**
     * 获得屏幕名称，用于友盟统计
     * @return
     */
    protected abstract String getScreenName();

    protected void onResume() {
    	UmsAgent.onResume(this);
        super.onResume();
        MobclickAgent.onPageStart(getScreenName());
        MobclickAgent.onResume(this);
        AppBrightnessManager.setScreenBrightness(this);
        ScreenManager.getScreenManager().changeActivityStates(this, true);
    }
    protected void onPause() {
    	UmsAgent.onPause(this);
        super.onPause();
        MobclickAgent.onPageEnd(getScreenName());
        MobclickAgent.onPause(this);
    }

	@Override
	protected void onStop() {
		super.onStop();
		ScreenManager.getScreenManager().changeActivityStates(this, false);
	}
}
