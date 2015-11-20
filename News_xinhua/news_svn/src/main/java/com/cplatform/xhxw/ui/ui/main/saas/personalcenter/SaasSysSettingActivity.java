package com.cplatform.xhxw.ui.ui.main.saas.personalcenter;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

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
import com.cplatform.xhxw.ui.ui.about.AboutActivity;
import com.cplatform.xhxw.ui.ui.base.BaseActivity;
import com.cplatform.xhxw.ui.ui.base.view.SwitchButton;
import com.cplatform.xhxw.ui.ui.feedback.FeedbackActivity;
import com.cplatform.xhxw.ui.ui.settings.ChangePasswordActivity;
import com.cplatform.xhxw.ui.ui.settings.SettingsActivity;
import com.cplatform.xhxw.ui.util.AppBrightnessManager;
import com.cplatform.xhxw.ui.util.ClearUtil;
import com.cplatform.xhxw.ui.util.Engine;
import com.cplatform.xhxw.ui.util.HuanCunSizeUtil;
import com.cplatform.xhxw.ui.util.ShareUtil;
import com.cplatform.xhxw.ui.util.ClearUtil.OnClearListener;
import com.cplatform.xhxw.ui.util.CommonUtils;
import com.cplatform.xhxw.ui.util.UpdateVersion;
import com.google.gson.Gson;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.umeng.analytics.MobclickAgent;
import com.wbtech.ums.UmsAgent;

public class SaasSysSettingActivity extends BaseActivity implements OnClearListener {
	
	private TextView mTextSize; // 新闻详情当前显示字体大小

	private SwitchButton mPushSetting;
    private RelativeLayout mBackFeedLayout;
    private RelativeLayout mAboutLayout;
    private RelativeLayout mClearLayout;
    private RelativeLayout mChangeTextSizeLo;
    private ProgressBar mClearProgress;
    private TextView mClearText;
    private RelativeLayout mRecommendLo;
    private RelativeLayout mVersionUpdateLo;
    private SwitchButton mNightModeSBtn;
    private ImageView mNewVersionAlert;
    private Button mLogout;
    private RelativeLayout mChangePwdLo;
    
    private ProgressDialog mProgressDialog;
    
    //清除状态
    private boolean isClearing = false;
    
    private ClearUtil clearUtil;
    
    private boolean isClearAvailable = false;
    
    private Handler cacheHandler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			if(msg.what == 0) {
				mClearText.setText(msg.getData().getString("size"));
				isClearAvailable = true;
			}
			super.handleMessage(msg);
		}
    };
    
	@Override
	protected String getScreenName() {
		return null;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.saas_activity_sys_setting);
		initActionBar();
		initViews();
		initDefaultValues();
	}

	private void initDefaultValues() {
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
        
        mClearText.setText(R.string.caculating);
        HuanCunSizeUtil.getCachedFileSize(getExternalCacheDir(), getCacheDir(), cacheHandler);
	}

	private void initViews() {
		mPushSetting = (SwitchButton) findViewById(R.id.saas_setting_push_setting);
	    mBackFeedLayout = (RelativeLayout) findViewById(R.id.saas_setting_feed_back);
	    mAboutLayout = (RelativeLayout) findViewById(R.id.saas_setting_about);
	    mClearLayout = (RelativeLayout) findViewById(R.id.saas_setting_clear);
	    mClearProgress = (ProgressBar) findViewById(R.id.saas_clear_progress);
	    mClearText = (TextView) findViewById(R.id.saas_clear_text);
	    mLogout = (Button) findViewById(R.id.saas_setting_btn_logout);
		mRecommendLo = (RelativeLayout) findViewById(R.id.saas_setting_share_app);
    	mVersionUpdateLo = (RelativeLayout) findViewById(R.id.saas_setting_update_app);
    	mNightModeSBtn = (SwitchButton) findViewById(R.id.saas_setting_day_model_setting);
    	mNightModeSBtn.setChecked(App.getDispalyModel() == Constants.DISPLAY_MODEL_NIGHT);
    	mNewVersionAlert = (ImageView) findViewById(R.id.saas_setting_new_version_alert_iv);
    	mNewVersionAlert.setVisibility(View.GONE);
    	mChangeTextSizeLo = (RelativeLayout) findViewById(R.id.saas_setting_ly_news_det_text_size);
    	mTextSize = (TextView) findViewById(R.id.saas_setting_tv_text_size);
    	mChangePwdLo = (RelativeLayout) findViewById(R.id.saas_setting_change_pwd);
    	
    	mPushSetting.setOnClickListener(mOnclick);
        mBackFeedLayout.setOnClickListener(mOnclick);
        mAboutLayout.setOnClickListener(mOnclick);
        mClearLayout.setOnClickListener(mOnclick);
        mLogout.setOnClickListener(mOnclick);
    	mRecommendLo.setOnClickListener(mOnclick);
    	mVersionUpdateLo.setOnClickListener(mOnclick);
    	mChangeTextSizeLo.setOnClickListener(mOnclick);
    	mChangePwdLo.setOnClickListener(mOnclick);
    	mChangePwdLo.setVisibility(View.GONE);
    	if(Constants.hasLogin()) {
    		mChangePwdLo.setVisibility(View.VISIBLE);
    	}
    	mNightModeSBtn.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				int model = App.getDispalyModel();
				model = (model == Constants.DISPLAY_MODEL_DAY ? Constants.DISPLAY_MODEL_NIGHT : Constants.DISPLAY_MODEL_DAY);
				App.getPreferenceManager().setDisplayModel(model);
				AppBrightnessManager.setScreenBrightness(SaasSysSettingActivity.this);
			}
		});
    	mPushSetting.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                AppPushManager.setOpenPush(isChecked);
                if (isChecked) {
                    AppPushManager.startWork(SaasSysSettingActivity.this);
                } else {
                    AppPushManager.stopWork(SaasSysSettingActivity.this);
                }
            }
        });
	}
	
	OnClickListener mOnclick = new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			int id = v.getId();
			switch (id) {
			case R.id.saas_setting_share_app:
				shareApp();
				break;
			case R.id.saas_setting_update_app:
				UpdateVersion updateVersion = UpdateVersion.getInstance(SaasSysSettingActivity.this);
		        updateVersion.isUpdate();
		        mProgressDialog = ProgressDialog.show(SaasSysSettingActivity.this, "检测更新", "请稍候...");
		        mProgressDialog.setCancelable(true);
		        getVersionInfo(true);
		        break;
			case R.id.saas_setting_ly_news_det_text_size:
				onChangeNewsTextSizeAction();
				break;
			case R.id.saas_setting_clear:
				setClear();
				break;
			case R.id.saas_setting_feed_back:
				setBackFeed();
				break;
			case R.id.saas_setting_about:
				setAbout();
				break;
			case R.id.saas_setting_btn_logout:
				onLogOutAction();
				break;
			case R.id.saas_setting_change_pwd:
				Intent it = new Intent();
				it.setClass(SaasSysSettingActivity.this, ChangePasswordActivity.class);
				startActivity(it);
				break;

			default:
				break;
			}
		}
	};
	
	public void onLogOutAction() {
        if (CommonUtils.isFastDoubleClick()) return;
        new AlertDialog.Builder(this)
                .setTitle(R.string.hint)
                .setMessage(R.string.logout_hint)
                .setNegativeButton(R.string.cancel, null)
                .setPositiveButton(R.string.logout, new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Constants.logout();
                        setResult(RESULT_OK);
                        finish();
                    }
                }).show();
    }
	
	public void onChangeNewsTextSizeAction() {
        if (CommonUtils.isFastDoubleClick())
            return;
        new AlertDialog.Builder(this).setTitle(getString(R.string.main_body_typeface))
                .setSingleChoiceItems(
                new String[]{getString(R.string.super_big_text_size), getString(R.string.big_text_size),
                        getString(R.string.middle_text_size), getString(R.string.small_text_size)},
                Constants.getNewsDetTextSize(),
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
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
                        dialog.dismiss();
                    }

                }).setNegativeButton(R.string.cancel, null).show();
    }
	
	/**
	 * 意见反馈
	 */
	public void setBackFeed() {
        startActivity(FeedbackActivity.getIntent(this));
    }
	
	/**
	 * 关于
	 */
	public void setAbout() {
    	startActivity(AboutActivity.getIntent(this));
    }
	
	/**
	 * 清楚缓存
	 */
	public void setClear() {
    	if(isClearAvailable) {
    		clearUtil = new ClearUtil(this, getExternalCacheDir(), getCacheDir());
        	clearUtil.setClearListener(this);
        	clearUtil.execute();
    	}
    }

	@Override
	public void onPreClear() {
		isClearing = true;
		mClearText.setVisibility(View.GONE);
		mClearProgress.setVisibility(View.VISIBLE);
	}

	@Override
	public void onPostClear() {
		clearUtil = null;
		isClearing = false;
		mClearProgress.setVisibility(View.GONE);
		mClearText.setVisibility(View.VISIBLE);
		mClearText.setText("0.0M");
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
	
	private void shareApp() {
    	MobclickAgent.onEvent(this, StatisticalKey.menu_set_recommend);
    	
    	UmsAgent.onEvent(this,StatisticalKey.menu_set_recommend);
    	
        String share = getString(R.string.share);
        ShareUtil.sendTextIntent(this, null,share, share, share, getResources().getString(R.string.invite_content),
                getResources().getString(R.string.invite_content_cick_url),false,true,false, null, true);
    }
    
	/** 获取版本属性 */
	private void getVersionInfo(final boolean isManual) {

		GetVersionInfoRequest request = new GetVersionInfoRequest();
		request.setDevice_type("android_phone");
		request.setVersion_no(Engine.getVersionCode(this));

		APIClient.getVersionInfo(request, new AsyncHttpResponseHandler() {

			@Override
			public void onFinish() {
				if(mProgressDialog != null) {
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
								mNewVersionAlert.setVisibility(View.VISIBLE);
							} else {
								mNewVersionAlert.setVisibility(View.GONE);
								if(isManual) {
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

	@Override
	protected void onResume() {
		if(Constants.hasLogin()) {
			mChangePwdLo.setVisibility(View.VISIBLE);
		}
		super.onResume();
	}
}
