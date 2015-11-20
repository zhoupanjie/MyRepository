package com.cplatform.xhxw.ui.ui.welcom;

import java.util.List;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import butterknife.ButterKnife;
import com.cplatform.xhxw.ui.Config;
import com.cplatform.xhxw.ui.Constants;
import com.cplatform.xhxw.ui.PreferencesManager;
import com.cplatform.xhxw.ui.R;
import com.cplatform.xhxw.ui.StatisticalKey;
import com.cplatform.xhxw.ui.db.AdvertisementDB;
import com.cplatform.xhxw.ui.db.ReadNewsDB;
import com.cplatform.xhxw.ui.db.dao.AdvertisementDao;
import com.cplatform.xhxw.ui.http.HttpClientConfig;
import com.cplatform.xhxw.ui.ui.advertisement.AdvertiseUtil;
import com.cplatform.xhxw.ui.ui.base.BaseNormalActivity;
import com.cplatform.xhxw.ui.ui.base.view.SliderViewItem;
import com.cplatform.xhxw.ui.ui.cyancomment.CYanUtil;
import com.cplatform.xhxw.ui.ui.guide.GuideActivity;
import com.cplatform.xhxw.ui.ui.main.HomeActivity;
import com.cplatform.xhxw.ui.ui.main.forelanguage.activity.ForeignLanguageHomeActivity;
import com.cplatform.xhxw.ui.ui.main.forelanguage.utils.LanguageUtil;
import com.cplatform.xhxw.ui.ui.web.WebViewActivity;
import com.cplatform.xhxw.ui.util.DateUtil;
import com.cplatform.xhxw.ui.util.MediaPlayerManager;
import com.cplatform.xhxw.ui.util.MediaPlayerManager.OnMediaPlayerManagerListener;
import com.cplatform.xhxw.ui.util.Util;
import com.wbtech.ums.UmsAgent;

public class WelcomActivity extends BaseNormalActivity implements
		OnMediaPlayerManagerListener {

	private ImageView imageView;
	private ImageView mAdverIv;
	private ImageView mLogoIv;
	private ImageView mAdverBottomIv;
	private ImageView mFullScreenIv;
	private ImageView mFullScreenAdIv;
	private LinearLayout mRootLo;

	private MediaPlayerManager manager;
	private Animation mFadeInAni;
	private AdvertisementDao mAdverInfo;
	private AdvertisementDao mBottomAdverInfo;
	private boolean isEnterAdverDetailPage = false;
	private boolean isAdverShow = false;
	private boolean isToShowAdver = false;
	private boolean isExitHere = false;

	// private Activity aty;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		UmsAgent.init(this, HttpClientConfig.UMSAGENT_BASE_URL, 1);
		setContentView(R.layout.welcom);
		ButterKnife.bind(this);
		mRootLo = (LinearLayout) findViewById(R.id.welcom_root_lo);
		mAdverIv = (ImageView) findViewById(R.id.welcome_advertise);
		mAdverIv.setOnClickListener(mOnClick);
		imageView = (ImageView) findViewById(R.id.address_image);
		mLogoIv = (ImageView) findViewById(R.id.welcome_logo_iv);
		mAdverBottomIv = (ImageView) findViewById(R.id.welcome_advertise_bottom);
		mAdverBottomIv.setOnClickListener(mOnClick);
		mFullScreenIv = (ImageView) findViewById(R.id.loading_fullscreen_iv);
		mFullScreenAdIv = (ImageView) findViewById(R.id.loading_fullscreen_ad_iv);
		mFullScreenAdIv.setOnClickListener(mOnClick);
		if (Config.IS_SHOWFA_PACK) {
			mRootLo.setBackgroundResource(R.drawable.shoufa_loading_bg);
			mFullScreenAdIv.setVisibility(View.GONE);
			mFullScreenIv.setVisibility(View.GONE);
		}
		init();

		new Thread(new Runnable() {
			@Override
			public void run() {
				// 清除一周以外的已读新闻记录 7 * 24 * 60 * 60 * 1000
				long time = DateUtil.getTime() - 604800000;
				ReadNewsDB.delReadNewsByLtTime(getApplicationContext(), time);
			}
		}).start();

		AdvertiseUtil.startFetchLoadingAdver(this);
		CYanUtil.initCyan(this);
		// 先初始化焦点新闻的高度
		new SliderViewItem(this, null);
	}

	/**
	 * 临时注销
	 * 
	 * 播放启动音乐
	 * */
	// private void init() throws IOException {
	// manager = new MediaPlayerManager(this);
	// manager.setOnMediaPlayerManagerListener(this);
	//
	// AssetManager assetManager = this.getAssets();
	//
	// AssetFileDescriptor fileDescriptor = null;
	// try {
	// fileDescriptor = assetManager.openFd("loading.mp3");
	// } catch (IOException e) {
	// e.printStackTrace();
	// }
	// manager.setDateFile(fileDescriptor.getFileDescriptor());
	//
	// /*mediaPlayer.setDataSource(fileDescriptor.getFileDescriptor(),
	// fileDescriptor.getStartOffset(),
	// fileDescriptor.getLength());*/
	//
	// try {
	// manager.getMediaPlayer().prepare();
	// } catch (IllegalStateException e) {
	// e.printStackTrace();
	// }
	// manager.startFile();
	// }

	private void init() {
		// if (YouXiaoADUtil.isOpenYouXiaoAd(this)) {
		// aty = this;
		// }
		Util.getMyUUID();
		handler.sendEmptyMessageDelayed(1, 1500);
		mFadeInAni = AnimationUtils.loadAnimation(this,
				R.anim.translate_fade_in);
	}

	/** 音频播放完毕，回调此方法 */
	@Override
	public void onMediaPlayerManager(boolean isSuccess) {
		if (manager != null) {
			manager.myStop();
			manager = null;
		}
		// 判断是否显示过程引导
		if (Constants.isShowGuide()) {
			startActivity(new Intent(this, GuideActivity.class));
		} else {
			enterHome();
		}

		this.finish();

	}

	@Override
	public void onMediaPlayerPrepared() {

	}

	@SuppressLint("HandlerLeak")
	private Handler handler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			int msgWhat = msg.what;
			switch (msgWhat) {
			case 1:// 是否显示引导页
				if (Constants.isShowGuide()) {
					startActivity(new Intent(WelcomActivity.this,
							GuideActivity.class)
							.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
					finish();
				} else {
					isToShowAdver = ((isExistCachedAdver(true) && PreferencesManager
							.isAdverrCacheDone(WelcomActivity.this, true)) || (isExistCachedAdver(false) && PreferencesManager
							.isAdverrCacheDone(WelcomActivity.this, false)));
					if (isToShowAdver) {
						this.sendEmptyMessage(2);
					} else {
						// if (YouXiaoADUtil.isOpenYouXiaoAd(aty)) {
						// this.sendEmptyMessage(4);
						// } else {
						enterHome();
						// }
					}
				}
				break;
			case 2:// 初始化load广告
				isAdverShow = true;
				initAdver();
				this.sendEmptyMessageDelayed(3, 5000);
				break;
			case 3:// 进入主页
				if (!isEnterAdverDetailPage && !isExitHere) {
					enterHome();
				}
				break;
			// case 4:// 初始化load广告
			// if (!isAdverShow) {
			// YouXiaoADUtil.showFullScreenAd(aty);
			// this.sendEmptyMessageDelayed(3, 3000);
			// }
			// break;
			default:
				break;
			}
		}
	};

	@Override
	protected void onDestroy() {
		ButterKnife.bind(this);
		super.onDestroy();
	}

	@Override
	public void finish() {
		super.finish();
		executeFinishActivityAnim();
	}

	protected void executeFinishActivityAnim() {
		overridePendingTransition(0, getExitAnim());
	}

	/**
	 * 退出动画
	 */
	protected int getExitAnim() {
		return R.anim.activity_push_right_out;
	}

	/**
	 * 判断本地是否存在缓存的广告以及广告所属的图片
	 * 
	 * @return
	 */
	private boolean isExistCachedAdver(boolean isTopAdver) {
		int position = isTopAdver ? AdvertisementDao.ADVER_SHOW_POSITION_LOADING
				: AdvertisementDao.ADVER_SHOW_POSITION_BOTTOM;
		List<AdvertisementDao> advers = AdvertisementDB
				.getAdvertisementByShowPosition(getApplicationContext(),
						position);
		if (advers.size() > 0) {
			if (AdvertiseUtil.isAdverImgExist(getApplicationContext(),
					advers.get(0))) {
				return true;
			}
		}
		return false;
	}

	private void initAdver() {
		// 若本地存在缓存的广告以及广告所属的图片且是正在获取loading页广告图片缓存状态
		if ((isExistCachedAdver(true) && PreferencesManager.isAdverrCacheDone(
				WelcomActivity.this, true))) {
			List<AdvertisementDao> tmp = AdvertisementDB
					.getAdvertisementByShowPosition(this,
							AdvertisementDao.ADVER_SHOW_POSITION_LOADING);
			mAdverInfo = tmp.get(0);
			mFullScreenAdIv.setImageBitmap(AdvertiseUtil.scaleImgSize(AdvertiseUtil
					.generateAdverImgSaveFile(this, mAdverInfo)));
			mFullScreenAdIv.setVisibility(View.VISIBLE);
			mFullScreenIv.setVisibility(View.GONE);
			mFullScreenAdIv.startAnimation(mFadeInAni);
		}
		// 若本地不存在缓存的广告以及广告所属的图片且不是正在获取loading页广告图片缓存状态
		if ((isExistCachedAdver(false) && PreferencesManager.isAdverrCacheDone(
				WelcomActivity.this, false))) {
			List<AdvertisementDao> tmp = AdvertisementDB
					.getAdvertisementByShowPosition(this,
							AdvertisementDao.ADVER_SHOW_POSITION_BOTTOM);
//			mBottomAdverInfo = tmp.get(0);
//			mAdverBottomIv.setImageBitmap(AdvertiseUtil
//					.scaleImgSize(AdvertiseUtil.generateAdverImgSaveFile(this,
//							mBottomAdverInfo)));
//			mAdverBottomIv.setVisibility(View.VISIBLE);
//			mLogoIv.setVisibility(View.GONE);
//			mAdverBottomIv.startAnimation(mFadeInAni);
		}
	}

	OnClickListener mOnClick = new OnClickListener() {

		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.welcome_advertise:
				if ((mAdverInfo != null)
						&& (false == TextUtils
								.isEmpty(mAdverInfo.getAdverUrl()))
						&& (false == (mAdverInfo.getAdverUrl().trim()
								.equals("#")))) {
					Log.d("欢迎页广告1", mAdverInfo.getAdverUrl());
					if (isAdverShow) {
						UmsAgent.onEvent(WelcomActivity.this,
								StatisticalKey.xwad_startview_top,
								new String[] { StatisticalKey.key_newsid,
										StatisticalKey.key_title },
								new String[] { mAdverInfo.getAdverId(),
										mAdverInfo.getAdverTitle() });
						isEnterAdverDetailPage = true;
						startActivity(WebViewActivity.getIntentByURL(
								WelcomActivity.this, mAdverInfo.getAdverUrl(),
								mAdverInfo.getAdverTitle()));
						finish();
					}
				}
				break;
			case R.id.welcome_advertise_bottom:
				if ((mBottomAdverInfo != null)
						&& (false == TextUtils.isEmpty(mBottomAdverInfo
								.getAdverUrl()))
						&& (false == (mBottomAdverInfo.getAdverUrl().trim()
								.equals("#")))) {
					Log.d("欢迎页广告1", mBottomAdverInfo.getAdverUrl());
					if (isAdverShow) {
						UmsAgent.onEvent(WelcomActivity.this,
								StatisticalKey.xwad_startview_bottom,
								new String[] { StatisticalKey.key_newsid,
										StatisticalKey.key_title },
								new String[] { mBottomAdverInfo.getAdverId(),
										mBottomAdverInfo.getAdverTitle() });
						isEnterAdverDetailPage = true;
						startActivity(WebViewActivity.getIntentByURL(
								WelcomActivity.this,
								mBottomAdverInfo.getAdverUrl(),
								mBottomAdverInfo.getAdverTitle()));
						finish();
					}
				}
				break;
			case R.id.loading_fullscreen_ad_iv:
				if ((mAdverInfo != null)
						&& (false == TextUtils
								.isEmpty(mAdverInfo.getAdverUrl()))
						&& (false == (mAdverInfo.getAdverUrl().trim()
								.equals("#")))) {
					Log.d("欢迎页广告1", mAdverInfo.getAdverUrl());
					if (isAdverShow) {
						UmsAgent.onEvent(WelcomActivity.this,
								StatisticalKey.xwad_startview_top,
								new String[] { StatisticalKey.key_newsid,
										StatisticalKey.key_title },
								new String[] { mAdverInfo.getAdverId(),
										mAdverInfo.getAdverTitle() });
						isEnterAdverDetailPage = true;
						startActivity(WebViewActivity.getIntentByURL(
								WelcomActivity.this, mAdverInfo.getAdverUrl(),
								mAdverInfo.getAdverTitle()));
						finish();
					}
				}
				break;
			}
		}
	};

	@Override
	public boolean onKeyUp(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			isExitHere = true;
		}
		return super.onKeyUp(keyCode, event);
	}

	private void enterHome() {
		Intent it = new Intent();
		if (PreferencesManager.getLanguageInfo(this).equals(
				LanguageUtil.LANGUAGE_CH)) {
			it.setClass(WelcomActivity.this, HomeActivity.class);
		} else {
			it.setClass(WelcomActivity.this, ForeignLanguageHomeActivity.class);
		}
		it.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		startActivity(it);
		finish();
	}
}
