package com.cplatform.xhxw.ui.ui.main.forelanguage.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.FragmentTransaction;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;

import com.cplatform.xhxw.ui.App;
import com.cplatform.xhxw.ui.Constants;
import com.cplatform.xhxw.ui.PreferencesManager;
import com.cplatform.xhxw.ui.R;
import com.cplatform.xhxw.ui.db.ChanneDB;
import com.cplatform.xhxw.ui.db.ReadNewsDB;
import com.cplatform.xhxw.ui.patch.Patch;
import com.cplatform.xhxw.ui.receiver.StartServiceReceiver;
import com.cplatform.xhxw.ui.ui.apptips.TipsActivity;
import com.cplatform.xhxw.ui.ui.apptips.TipsUtil;
import com.cplatform.xhxw.ui.ui.base.BaseActivity;
import com.cplatform.xhxw.ui.ui.main.HomeActivity;
import com.cplatform.xhxw.ui.ui.main.forelanguage.utils.LanguageUtil;
import com.cplatform.xhxw.ui.util.AppExitUtil;
import com.cplatform.xhxw.ui.util.DateUtil;
import com.cplatform.xhxw.ui.util.UpdateVersion;
import com.umeng.analytics.MobclickAgent;

public class ForeignLanguageHomeActivity extends BaseActivity {

	private ForeignLanguageHomeFragment mFragment;
	private boolean mIsUnderEnterprise = false;
	private ImageView mBackBtn;
	private TextView mLanguageLoadingTv;
	
	public static boolean isForeignStart() {
		return isForeignLanActivityStart;
	}
	private static boolean isForeignLanActivityStart = false;
	
	private Handler mLanguageHandler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			if(msg.what == 0) {
				Intent it = new Intent(ForeignLanguageHomeActivity.this, HomeActivity.class);
				startActivity(it);
				finish();
			}
			super.handleMessage(msg);
		}
	};
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		long oldVersion = App.getPreferenceManager().getOldVersionCode();
        Patch.mainThreadCheckVersion(this, oldVersion);
        App.getPreferenceManager().setOldVsersionCode(Constants.AppInfo.getVsersionCode());
        MobclickAgent.updateOnlineConfig(this);
        setContentView(R.layout.activity_foreign_language_home);
        
        setSwipeBackEnable(false);
        
        mIsUnderEnterprise = false;
        init();
        clearUselessData();
        
        Intent intent = new Intent(this, StartServiceReceiver.class);
        intent.setAction(StartServiceReceiver.ACTION_SYNC_SERVICE_START);
        sendBroadcast(intent);
        
        getInfo();
        isForeignLanActivityStart = true;
        ChanneDB.gAllChanne(this);
	}

	private void init() {
		FragmentTransaction t = getSupportFragmentManager().beginTransaction();
		if(mFragment == null) {
			mFragment = new ForeignLanguageHomeFragment();
		}
		if(mFragment.isAdded()) {
			t.show(mFragment);
		} else {
			t.replace(R.id.fg_foreign_language_home, mFragment);
		}
		t.commit();
		
		mBackBtn  = (ImageView) findViewById(R.id.flan_back_btn);
		mBackBtn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				new AlertDialog.Builder(ForeignLanguageHomeActivity.this)
						.setTitle("Alert")
						.setMessage("Switch to chinese version?")
						.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog, int which) {
								mLanguageLoadingTv.bringToFront();
								mLanguageLoadingTv.setVisibility(View.VISIBLE);
								PreferencesManager.saveLanguageInfo(ForeignLanguageHomeActivity.this, 
										LanguageUtil.LANGUAGE_CH);
								mLanguageHandler.sendEmptyMessageDelayed(0, 2000);
							}
						})
						.setNegativeButton("No", null).show();
			}
		});
		
		mLanguageLoadingTv = (TextView) findViewById(R.id.flan_language_switch_loading);
		HomeActivity.setStart(true);
	}
	
	private void clearUselessData() {
		new Thread(new Runnable() {
            @Override
            public void run() {
                // 清除一周以外的已读新闻记录 7 * 24 * 60 * 60 * 1000
                long time = DateUtil.getTime() - 604800000;
                ReadNewsDB.delReadNewsByLtTime(getApplicationContext(), time);
            }
        }).start();
	}
	
	/**
     * 判断是否需要更新项目
     * */
    private void getInfo() {
        UpdateVersion updateVersion = UpdateVersion.getInstance(this);
        updateVersion.isUpdate();
    }

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		
		if(!TipsUtil.isTipShown(TipsUtil.TIP_LANGUAGE_CHANGE)) {
			startActivity(TipsActivity.getTipIntent(this, TipsUtil.TIP_LANGUAGE_CHANGE));
		}
	}

	@Override
	protected void onDestroy() {
		isForeignLanActivityStart = false;
		super.onDestroy();
	}

	@Override
	protected String getScreenName() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean onKeyUp(int keyCode, KeyEvent event) {
		boolean bo = AppExitUtil.onKeyUp(this, keyCode, event);
        if (bo) {
            return true;
        }
		return super.onKeyUp(keyCode, event);
	}
}
