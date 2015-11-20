package com.cplatform.xhxw.ui.ui.main.cms.game;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageInfo;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import butterknife.ButterKnife;
import butterknife.Bind;

import com.cplatform.xhxw.ui.Constants;
import com.cplatform.xhxw.ui.R;
import com.cplatform.xhxw.ui.StatisticalKey;
import com.cplatform.xhxw.ui.db.NewsDetailCashDB;
import com.cplatform.xhxw.ui.db.ReadNewsDB;
import com.cplatform.xhxw.ui.db.dao.NewsDetailCashDao;
import com.cplatform.xhxw.ui.db.dao.ReadNewsDao;
import com.cplatform.xhxw.ui.http.APIClient;
import com.cplatform.xhxw.ui.http.ResponseUtil;
import com.cplatform.xhxw.ui.ui.base.BaseFragment;
import com.cplatform.xhxw.ui.util.DateUtil;
import com.cplatform.xhxw.ui.util.LogUtil;
import com.cplatform.xhxw.ui.util.NetUtils;
import com.cplatform.xhxw.ui.util.ShareUtil;
import com.google.gson.Gson;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.umeng.analytics.MobclickAgent;
import com.wbtech.ums.UmsAgent;

public class GameDetailFragment extends BaseFragment implements OnClickListener {
	private Context con;
	private static final String TAG = GameDetailFragment.class.getSimpleName();

	@Bind(R.id.rl_title)
	RelativeLayout rlTitle;
	@Bind(R.id.tv_back)
	TextView tvGoback;// 返回按钮
	@Bind(R.id.tv_title)
	TextView tvTitle;// 返回按钮
	@Bind(R.id.ll_bg)
	LinearLayout llBG;
	ImageView ivBG;// 顶部背景图片
	@Bind(R.id.iv_logo)
	ImageView ivLogo;// 应用图标
	@Bind(R.id.tv_app_name)
	TextView tvAppName;// 应用名称
	@Bind(R.id.tv_down_count)
	TextView tvDownCount;// 下载次数
	@Bind(R.id.tv_version)
	TextView tvVersion;// 版本名称
	@Bind(R.id.tv_size)
	TextView tvSize;// 应用大小
	@Bind(R.id.hsv_app_image)
	HorizontalScrollView hsv;
	@Bind(R.id.v_split2)
	View vSplitBG;
	@Bind(R.id.ll_app_image)
	LinearLayout llAppImage;// 应用截图
	@Bind(R.id.wv_game_introduce)
	WebView wvGameIntroduce;// 游戏介绍
	@Bind(R.id.tv_update_time)
	TextView tvUpdateTime;
	@Bind(R.id.tv_developer)
	TextView tvDeveloper;
	@Bind(R.id.tv_type)
	TextView tvType;
	@Bind(R.id.bt_down)
	TextView btDown;
	@Bind(R.id.tv_share)
	TextView tvShare;// 分享
	@Bind(R.id.bt_down_state)
	TextView btDownState;// 下载状态
	@Bind(R.id.pb_down)
	ProgressBar pbDown;// 下载进度条
	@Bind(R.id.tv_down_info)
	TextView tvDownInfo;
	@Bind(R.id.detail_container)
	RelativeLayout rlWebVeiw;
	String url = "";
	String ApkName = "";
	private AsyncHttpResponseHandler mLoadHander;
	String gameId;
	private GameDetail mGameDetail;
	private String appNameShare;
	private String urlShare;
	private DataReceiver receiverDownloadDetail;
	private Game catchGame;
	private Activity aty;
	private boolean isUpdateCount = false;
	private View rootView;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		rootView = inflater.inflate(R.layout.fragment_game_detail, container,
				false);
		ButterKnife.bind(this, rootView);
		this.initActionBar(rootView);
		this.initViews(rootView);
		return rootView;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		if (NetUtils.getNetworkState() == NetUtils.Status.NONE) {
			loadCashDate();
		} else {
			loadNetData();
		}
	}

	@Override
	public void onResume() {
		receiverDownloadDetail = new DataReceiver();
		IntentFilter filterGame = new IntentFilter(
				GameUtil.ACTION_GAME);
		con.registerReceiver(receiverDownloadDetail, filterGame);
		super.onResume();
	}

	@Override
	public void onDestroy() {
		con.unregisterReceiver(receiverDownloadDetail);
		super.onDestroy();
	}

	@Override
	public void onDestroyView() {
		if (mLoadHander != null) {
			mLoadHander.cancle();
			mLoadHander = null;
		}

		if (wvGameIntroduce != null) {
			rlWebVeiw.removeAllViews();
			wvGameIntroduce.removeAllViews();
			// 内存泄露
			wvGameIntroduce.destroy();
		}
//		ButterKnife.reset(this);
		super.onDestroyView();
	}

	@SuppressLint("SetJavaScriptEnabled")
	private void initViews(final View rootView) {
		aty = this.getActivity();
		con = this.getActivity();
		gameId = getArguments().getString("newsid");
		initBG();
		wvGameIntroduce.clearCache(true);
		wvGameIntroduce.getSettings().setJavaScriptEnabled(true);
		wvGameIntroduce.getSettings().setCacheMode(WebSettings.LOAD_NO_CACHE);
		wvGameIntroduce.getSettings().setBlockNetworkImage(true);
		// webView.setScrollBarStyle(View..SCROLLBARS_INSIDE_OVERLAY);
		wvGameIntroduce.refreshDrawableState();
		wvGameIntroduce.setWebChromeClient(new WebChromeClient());
		wvGameIntroduce.getSettings().setDefaultTextEncodingName("utf-8");
		wvGameIntroduce.getSettings().setBlockNetworkImage(false);// 网络图片显示
		llBG.setOnClickListener(this);
		tvGoback.setOnClickListener(this);
		btDown.setOnClickListener(this);
		tvShare.setOnClickListener(this);
		btDownState.setOnClickListener(this);
		tvShare.setOnClickListener(this);

		catchGame = GameManager.getGameDownCache(con, gameId);
		if (catchGame == null) {
			setBottomDownUI(false);
		} else {
			catchGame=GameManager.setGameByDownHistory(con, catchGame);
			initBottomViewByCatch(catchGame);
		}
	}
	void initBottomViewByCatch(Game item) {
		if (item.getFileSize() != 0) {
			pbDown.setVisibility(View.INVISIBLE);
			pbDown.setMax(item.getFileSize());
			if (item.getComplete() != 0) {
				pbDown.setProgress(item.getComplete());
			}
		}
		switch (item.getStateDown()) {
		case GameUtil.GAME_DOWN_UN:
			setBottomDownUI(false);
			break;
		case GameUtil.GAME_DOWN_WAIT:
			setBottomDownUI(true);
			btDownState.setText(R.string.game_down_wait);
			break;
		case GameUtil.GAME_DOWN_ING:
			setBottomDownUI(true);
			btDownState.setText(R.string.game_down_pause);
			tvDownInfo.setText(GameUtil.toMByB(item.getComplete()) + "/"
					+ GameUtil.toMByB(item.getFileSize()));
			break;
		case GameUtil.GAME_DOWN_CONTINUE:
			setBottomDownUI(true);
			btDownState.setText(R.string.game_down_pause);
			tvDownInfo.setText(GameUtil.toMByB(item.getComplete()) + "/"
					+ GameUtil.toMByB(item.getFileSize()));
			break;
		case GameUtil.GAME_DOWN_PAUSE:
			setBottomDownUI(true);
			btDownState.setText(R.string.game_down_continue);
			pbDown.setVisibility(View.VISIBLE);
			tvDownInfo.setText(GameUtil.toMByB(item.getComplete()) + "/"
					+ GameUtil.toMByB(item.getFileSize()));
			break;
		case GameUtil.GAME_DOWN_COMPLETE:
			setBottomDownUI(true);
			btDownState.setText(R.string.game_down_install);
			if ((item.getFileSize() == item.getComplete())&&(!TextUtils.isEmpty(item.getDownTime()))) {
				tvDownInfo.setText("下载时间:" + item.getDownTime());
			}
			break;
		case GameUtil.GAME_DOWN_FAILURE:
			setBottomDownUI(true);
			btDownState.setText(R.string.game_down);
			tvDownInfo.setText(R.string.game_text_down_failure);
			break;
		case GameUtil.GAME_INSTALL_ING:
			setBottomDownUI(true);
			btDownState.setText(R.string.game_down_install);
			pbDown.setVisibility(View.VISIBLE);
			tvDownInfo.setText(GameUtil.toMByB(item.getComplete()) + "/"
					+ GameUtil.toMByB(item.getFileSize()));
			break;
		case GameUtil.GAME_INSTALL_COMPLETE:
			setBottomDownUI(true);
			btDownState.setText(R.string.game_install_open);
			if ((item.getFileSize() == item.getComplete())&&(!TextUtils.isEmpty(item.getInstallTime()))) {
				tvDownInfo.setText("安装时间:" + item.getInstallTime());
			}
			break;
		case GameUtil.GAME_INSTALL_FAILURE:
			setBottomDownUI(true);
			btDownState.setText(R.string.game_down_install);
			if ((item.getFileSize() == item.getComplete())&&(!TextUtils.isEmpty(item.getDownTime()))) {
				tvDownInfo.setText("下载时间:" + item.getDownTime());
			}
			break;
		}
	}

	public void initBG() {
		LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
				Constants.screenWidth, getBGHeight());
		ivBG = new ImageView(con);
		ivBG.setScaleType(ScaleType.FIT_XY);
		ivBG.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				if (rlTitle.isShown()) {
					rlTitle.setVisibility(View.GONE);
				} else {
					rlTitle.setVisibility(View.VISIBLE);
				}
			}
		});
		llBG.addView(ivBG, lp);
	}

	private int getBGHeight() {
		double height = Constants.screenWidth * Constants.HomeSliderSize.height
				/ Constants.HomeSliderSize.width;
		return (int) Math.ceil(height) + 1;
	}

	/**
	 * 
	 * @Name setBottomDownUI
	 * @Description TODO 设置底部下载界面
	 * @param isDown
	 * @return void
	 * @Author zxe
	 * @Date 2015年6月27日 上午10:51:42
	 * 
	 */
	void setBottomDownUI(boolean isDown) {
		if (isDown) {
			btDown.setVisibility(View.GONE);
			btDownState.setVisibility(View.VISIBLE);
			pbDown.setVisibility(View.VISIBLE);
			tvDownInfo.setVisibility(View.VISIBLE);
		} else {
			btDown.setVisibility(View.VISIBLE);
			btDownState.setVisibility(View.GONE);
			pbDown.setVisibility(View.GONE);
			tvDownInfo.setVisibility(View.GONE);
		}
	}

	@Override
	public void onClick(View arg0) {
		// TODO Auto-generated method stub
		switch (arg0.getId()) {
		case R.id.tv_back: {// 返回按钮
			this.getActivity().finish();
		}
			break;
		case R.id.bt_down: {// 下载按钮
			// GameUtil.removePauseId(mGameDetail.getId());
			checkAndCreateDirectory();
			// 执行asynctask
			setStartDown(btDownState, mGameDetail, R.string.game_down_pause,
					GameUtil.GAME_DOWN_ING);
		}
			break;
		case R.id.tv_share: {// 分享按钮
			shareApp();
		}
			break;
		case R.id.bt_down_state: {// 下载状态按钮
			setOnClickDownButton(btDownState, mGameDetail);
		}
			break;
		}
	}

	public void setStartDown(final TextView ivDown, final GameDetail item,
			final int strId, final int stateDown) {
		if (GameUtil.isConnect(con)) {
			if ((false == GameUtil.isWifi(con)) && GameUtil.ISFIRSTDOWN) {
				new AlertDialog.Builder(con)
						.setMessage("非wifi环境下是否继续下载？")
						.setTitle("下载提示")
						.setPositiveButton(R.string.confirm,
								new DialogInterface.OnClickListener() {

									@Override
									public void onClick(DialogInterface dialog,
											int which) {
										dialog.dismiss();
										updateDownUI(con);
									}
								})
						.setNegativeButton(R.string.cancel,
								new DialogInterface.OnClickListener() {

									@Override
									public void onClick(DialogInterface dialog,
											int arg1) {
										// TODO Auto-generated method stub
										dialog.dismiss();
										setBottomDownUI(false);
									}

								}).show();
				GameUtil.ISFIRSTDOWN = false;
			} else {
				updateDownUI(con);
			}
		} else {
			GameManager.showNoNetworkDialog(con);
		}
	}
	void updateDownUI(Context con) {
		List<GameCashDao> lgcd = GameCashDB.getGameCacheList(con);
		List<Game> glist = new ArrayList<Game>();
		for (GameCashDao gcd : lgcd) {
			Game g = GameManager.getGameItemByGameCashDao(gcd);
			if (g.getStateDown() == GameUtil.GAME_DOWN_ING
					|| g.getStateDown() == GameUtil.GAME_DOWN_CONTINUE) {
				glist.add(g);
			}
		}
		if ((glist.size() < GameUtil.MAXDOWNAMOUNT)) {
			// saveGameData(con, gameId);
			setBottomDownUI(true);
			btDownState.setText(R.string.game_down_pause);
			mGameDetail.setStateDown(GameUtil.GAME_DOWN_ING);
			Game g=GameManager.getGameById(con, mGameDetail.getId());
			GameManager.saveGameDataByGame(con, g, GameUtil.GAME_DOWN_ING);
			GameManager.startDown(con, mGameDetail.getId(), mGameDetail.getDownloadurl(), mGameDetail.getIcon(), mGameDetail.getName(), mGameDetail.getCatname(), mGameDetail.getCatid());
		} else {
			setBottomDownUI(true);
			btDownState.setText(R.string.game_down_wait);
			mGameDetail.setStateDown(GameUtil.GAME_DOWN_WAIT);
			GameManager.setWaitDownState(con, mGameDetail.getId());
		}
	}
	void setOnClickDownButton(TextView ivDown, GameDetail item) {
		setBottomDownUI(true);
		String text = ivDown.getText().toString().trim();
		if (!TextUtils.isEmpty(text)) {
			if (item.getStateDown() == GameUtil.GAME_DOWN_UN) {// 未下载状态
				setStartDown(ivDown, item, R.string.game_down_pause,
						GameUtil.GAME_DOWN_ING);
			} else if (item.getStateDown() == GameUtil.GAME_DOWN_ING) {
				GameManager.onPauseById(con, item.getId());
				ivDown.setText(R.string.game_down_continue);
				item.setStateDown(GameUtil.GAME_DOWN_PAUSE);
			} else if (item.getStateDown() == GameUtil.GAME_DOWN_PAUSE) {
				setStartDown(ivDown, item, R.string.game_down_pause,
						GameUtil.GAME_DOWN_CONTINUE);
			} else if (item.getStateDown() == GameUtil.GAME_DOWN_CONTINUE) {
				GameManager.onPauseById(con, item.getId());
				ivDown.setText(R.string.game_down_continue);
				item.setStateDown(GameUtil.GAME_DOWN_PAUSE);
			} else if (item.getStateDown() == GameUtil.GAME_DOWN_COMPLETE) {
				if(new File(Constants.Directorys.DOWNLOAD
						+ GameUtil.getFileNameByUrl(item.getDownloadurl()))
						.exists()){
					GameUtil.installAPK((Activity) con, item.getId(),
							item.getDownloadurl(), item.getPackageName(), 2);
				}else{
					item.setStateDown(GameUtil.GAME_DOWN_UN);
					ivDown.setText(R.string.game_down);
				}
			} else if (item.getStateDown() == GameUtil.GAME_DOWN_FAILURE) {
				setStartDown(ivDown, item, R.string.game_down_pause,
						GameUtil.GAME_DOWN_ING);
			} else if (item.getStateDown() == GameUtil.GAME_INSTALL_COMPLETE) {
				GameUtil.openAppByByPackageName(con.getApplicationContext(),
						item.getPackageName());
				ivDown.setText(R.string.game_install_open);
				item.setStateDown(GameUtil.GAME_INSTALL_COMPLETE);
			} else if (item.getStateDown() == GameUtil.GAME_INSTALL_FAILURE) {
				if(new File(Constants.Directorys.DOWNLOAD
						+ GameUtil.getFileNameByUrl(item.getDownloadurl()))
						.exists()){
					GameUtil.installAPK((Activity) con, item.getId(),
							item.getDownloadurl(), item.getPackageName(), 2);
				}else{
					item.setStateDown(GameUtil.GAME_DOWN_UN);
					ivDown.setText(R.string.game_down);
				}
			}
		}
	}

	/**
	 * 
	 * @Name shareApp
	 * @Description TODO 分享
	 * @return void
	 * @Author zxe
	 * @Date 2015年6月30日 下午5:05:35
	 * 
	 */
	private void shareApp() {
		MobclickAgent.onEvent(con, StatisticalKey.menu_set_recommend);

		UmsAgent.onEvent(con, StatisticalKey.menu_set_recommend);

		String share = getString(R.string.share);

		ShareUtil.share_type = 1;
		ShareUtil.sendGameTextIntent(this.getActivity(), null, share, share, share,
				"分享 我下载了一款游戏[" + appNameShare + "]一起来加入吧!", urlShare, false,
				true, false, null, true,mGameDetail.getIcon());
	}

	/**
	 * 
	 * @Name checkAndCreateDirectory
	 * @Description TODO 检查游戏下载目录是否存在
	 * @return void
	 * @Author zxe
	 * @Date 2015年6月27日 上午10:52:18
	 * 
	 */
	public void checkAndCreateDirectory() {
		File new_dir = new File(Constants.Directorys.DOWNLOAD);
		if (!new_dir.exists()) {
			new_dir.mkdirs();
		}
	}

	/**
	 * 加载网络数据
	 */
	private void loadNetData() {
		LogUtil.d(TAG, "新闻详情请求参数：newsid=" + gameId);
		GameDetailRequest ndr = new GameDetailRequest(gameId);
		ndr.setIstest(GameUtil.istest);
		APIClient.gameDetail(ndr, asyncRepHanlder);
	}

	/**
	 * 
	 * @Name loadStatData
	 * @Description TODO 统计接口
	 * @return void
	 * @Author zxe
	 * @Date 2015年6月27日 下午3:21:34
	 * 
	 */
	private void loadStatData() {
		LogUtil.d(TAG, "游戏下载统计请求参数：id=" + gameId);
		GameDetailRequest ndr = new GameDetailRequest(gameId);
		ndr.setIstest(GameUtil.istest);
		APIClient.gameStat(ndr, ahrh);
	}

	/**
	 * 加载缓存数据
	 */
	private void loadCashDate() {
		new Thread(new Runnable() {
			@Override
			public void run() {
				final NewsDetailCashDao dao = NewsDetailCashDB
						.getNewsCashByColumnId(mAct, gameId);
				if (isAdded()) {
					mAct.runOnUiThread(new Runnable() {
						@Override
						public void run() {
							if (dao != null
									&& !TextUtils.isEmpty(dao.getJson())) {
								updateDateUI(dao.getJson());
							} else {
								loadNetData();
							}
						}
					});
				}
			}
		}).start();

	}

	void setText(TextView tv, String text) {
		if (TextUtils.isEmpty(text)) {
			tv.setText(R.string.game_text_unknown);
		} else {
			tv.setText(text);
		}
	}

	/**
	 * 更新UI数据
	 */
	private void updateDateUI(String content) {
		// LogUtil.d(TAG, "新闻详情获取json::"+content);
		GameDetailResponse response;
		try {
			ResponseUtil.checkResponse(content);
			response = new Gson().fromJson(content, GameDetailResponse.class);
		} catch (Exception e) {
			LogUtil.w(TAG, e);
			// mDefView.setStatus(DefaultView.Status.error);
			return;
		}
		if (response.isSuccess()) {
			ReadNewsDB.saveNews(mAct,
					new ReadNewsDao(gameId, DateUtil.getTime()));
			mGameDetail = response.getData();
			if (isUpdateCount) {
				isUpdateCount = false;
				mGameDetail.setStateDown(GameUtil.GAME_DOWN_COMPLETE);
				setText(tvDownCount, mGameDetail.getDownloadCount());
			} else {
				List<GameImage> lgiBackground = mGameDetail.getBackground();
				DisplayImageOptions dio = new DisplayImageOptions.Builder()
						.showStubImage(R.drawable.def_img_16_9)
						.showImageForEmptyUri(R.drawable.def_img_16_9)
						.showImageOnFail(R.drawable.def_img_16_9)
						.cacheInMemory().cacheOnDisc().build();
				// 顶部背景
				if (lgiBackground != null && (false == lgiBackground.isEmpty())) {
					for (GameImage giBackground : lgiBackground) {
						try {
							ImageLoader.getInstance().displayImage(
									giBackground.getThumb(), ivBG, dio);
						} catch (Exception e) {
							Log.e("加载图片错误", e.getMessage().toString());
						}
					}
				} else {
					ivBG.setImageResource(R.drawable.def_img_16_9);
				}
				if (TextUtils.isEmpty(mGameDetail.getIcon())) {
					ivLogo.setImageResource(R.drawable.def_img_16_9);
				} else {
					// 图标
					ImageLoader.getInstance().displayImage(
							mGameDetail.getIcon(), ivLogo, dio);
				}
				// 应用截图
				List<GameImage> lgiContent = mGameDetail.getContentimage();
				if (lgiContent != null && (false == lgiContent.isEmpty())) {
					for (GameImage giContent : lgiContent) {
						View view = LayoutInflater.from(this.getActivity())
								.inflate(R.layout.activity_index_gallery_item,
										llAppImage, false);
						ImageView img = (ImageView) view
								.findViewById(R.id.id_index_gallery_item_image);
						try {
							ImageLoader.getInstance().displayImage(
									giContent.getThumb(), img, dio);
						} catch (Exception e) {
							Log.e("加载图片错误", e.getMessage().toString());
						}
						llAppImage.addView(view);
					}
				} else {
					hsv.setVisibility(View.GONE);
					vSplitBG.setVisibility(View.GONE);
				}
				appNameShare = mGameDetail.getName();
				// tvTitle.setText(appNameShare);
				tvAppName.setText(appNameShare);
				setText(tvVersion, mGameDetail.getVersion());
				setText(tvSize, mGameDetail.getGamesize());
				setText(tvUpdateTime,
						GameUtil.timestampsToDate(mGameDetail.getUpdatetime()));
				setText(tvDeveloper, mGameDetail.getDeveloper());
				setText(tvType, mGameDetail.getCatname());
				if (TextUtils.isEmpty(mGameDetail.getDownloadCount())) {
					tvDownCount.setText("0");
				} else {
					tvDownCount.setText(mGameDetail.getDownloadCount());
				}
				if (catchGame != null) {
					mGameDetail.setStateDown(catchGame.getStateDown());
				}

				url = mGameDetail.getDownloadurl();
				urlShare = mGameDetail.getShorturl();
				wvGameIntroduce.loadData(mGameDetail.getContent(),
						"text/html; charset=UTF-8", null);
				PackageInfo pi = GameUtil.getPackageInfoByPackageName(con,
						mGameDetail.getPackageName());
				if (pi != null) {// 已安装
					setBottomDownUI(true);
					btDownState.setText(R.string.game_install_open);
					tvDownInfo.setText("已安装");
					pbDown.setVisibility(View.INVISIBLE);
					mGameDetail.setStateDown(GameUtil.GAME_INSTALL_COMPLETE);
				}
			}
		} else {
			if (response.getCode() == -2) {
				Toast.makeText(getActivity(), response.getMsg(),
						Toast.LENGTH_LONG).show();
			}
			// mDefView.setStatus(DefaultView.Status.error);
		}

		// checkCyanAccountAndShowFloatBar();
	}

	AsyncHttpResponseHandler ahrh = new AsyncHttpResponseHandler() {
		@Override
		public void onFinish() {

		}

		@Override
		protected void onPreExecute() {

		}

		@Override
		public void onSuccess(String content) {

		}

		@Override
		public void onFailure(Throwable error, String content) {
			// mDefView.setStatus(DefaultView.Status.error);
		}
	};
	AsyncHttpResponseHandler asyncRepHanlder = new AsyncHttpResponseHandler() {

		@Override
		public void onFinish() {
			Log.d("onFinish", getUrl());
			mLoadHander = null;
		}

		@Override
		protected void onPreExecute() {
			if (mLoadHander != null)
				mLoadHander.cancle();
			mLoadHander = this;
			Log.d("onPreExecute", getUrl());
		}

		@Override
		public void onSuccess(String content) {
			Log.d("onSuccess", getUrl());
			updateDateUI(content);
		}

		@Override
		public void onFailure(Throwable error, String content) {
			Log.d("onFailure", getUrl());
			// mDefView.setStatus(DefaultView.Status.error);
		}
	};

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		switch (requestCode) {
		case GameUtil.REQUEST_CODE_INSTALL: {// 安装结果
			if (GameUtil.isNullInstallList() == false) {
				List<APKInstallData> laid = GameUtil.getInstallListByUI(2);

				if (laid.isEmpty() == false) {
					for (APKInstallData aid : laid) {
						updateInstallItem(aid);
					}
				}
			}
		}
			break;
		}
	}

	public void updateInstallItem(APKInstallData aid) {
		String gameId = aid.getGameId();
		// String downUrl = data.getStringExtra("DownUrl");
		// String apkPath = data.getStringExtra("APKPath");
		String packageName = aid.getPackageName();
		setBottomDownUI(true);
		if (GameUtil.isInstallByPackageName(con, packageName)) {
			Log.d("安装结果", "成功");
			btDownState.setText(R.string.game_install_open);
			tvDownInfo.setText("安装完成");
			pbDown.setVisibility(View.INVISIBLE);
			mGameDetail.setStateDown(GameUtil.GAME_INSTALL_COMPLETE);
			GameManager.updateGameCacheByDown(con, gameId, GameUtil.GAME_TAG_INSTALL_SUCCESS, 0, 0);
		} else {
			Log.d("安装结果", "失败");
			btDownState.setText(R.string.game_down_install);
			tvDownInfo.setText("等待安装");
			pbDown.setVisibility(View.INVISIBLE);
			mGameDetail.setStateDown(GameUtil.GAME_DOWN_COMPLETE);
			GameManager.updateGameCacheByDown(con, gameId, GameUtil.GAME_TAG_INSTALL_FAILURE, 0, 0);
		}
		GameUtil.removeApkByGameId(gameId);
	}

	public void updateItem(Intent i) {
		Log.d("GameDetail下载更新", "开始");
		int runState = i.getIntExtra("RunState", 0);
		String GameId = i.getStringExtra("GameId");
		int FileSize = i.getIntExtra("FileSize", 0);
		int Complete = i.getIntExtra("Complete", 0);
		if (mGameDetail != null && GameId.equals(mGameDetail.getId())) {
			setBottomDownUI(true);
			mGameDetail.setFileSize(FileSize);
			mGameDetail.setComplete(Complete);
			pbDown.setMax(FileSize);
			pbDown.setProgress(Complete);
			// 下载结果判断
			switch (runState) {
			case GameUtil.GAME_TAG_DOWN_SUCCESS: {// 成功
				// if(GameUtil.isPause(GameId)){
				 mGameDetail.setStateDown(GameUtil.GAME_DOWN_ING);
				 btDownState.setText(R.string.game_down_pause);
				// }
				int totalSize = i.getIntExtra("totalSize", 0);
				pbDown.incrementProgressBy(totalSize);
				 tvDownInfo.setText(GameUtil.toMByB(Complete) + "/"
				 + GameUtil.toMByB(FileSize)+ GameUtil.getGameDownSpeed(totalSize));
//				tvDownInfo.setText(GameUtil.toMByB(Complete) + "/"
//						+ GameUtil.toMByB(FileSize));
				if (pbDown.getProgress() == pbDown.getMax()) {
					btDownState.setText(R.string.game_down_install);
					tvDownInfo.setText("下载完成");
					mGameDetail.setStateDown(GameUtil.GAME_DOWN_COMPLETE);
					// isUpdateCount = true;
					// loadNetData();
					int count = Integer
							.parseInt(mGameDetail.getDownloadCount()) + 1;
					setText(tvDownCount, count + "");
				}
			}
				break;
			case GameUtil.GAME_TAG_DOWN_FAILURE: {// 失败
				// if(GameUtil.isPause(GameId)){
				 mGameDetail.setStateDown(GameUtil.GAME_DOWN_ING);
				 btDownState.setText(R.string.game_down_pause);
				// }
				int totalSize = i.getIntExtra("totalSize", 0);
				pbDown.incrementProgressBy(totalSize);
				tvDownInfo.setText(GameUtil.toMByB(Complete) + "/"
						+ GameUtil.toMByB(FileSize));
				btDownState.setText(R.string.game_down);
				tvDownInfo.setText("下载失败");
				setBottomDownUI(false);
				mGameDetail.setStateDown(GameUtil.GAME_DOWN_FAILURE);
			}
				break;
			case GameUtil.GAME_TAG_DOWN_ING: {// 下载中
				mGameDetail.setStateDown(GameUtil.GAME_DOWN_ING);
				btDownState.setText(R.string.game_down_pause);
			}
			case GameUtil.GAME_TAG_DOWN_PAUSE: {// 暂停
				 mGameDetail.setStateDown(GameUtil.GAME_DOWN_PAUSE);
				 btDownState.setText(R.string.game_down_continue);
				int totalSize = i.getIntExtra("totalSize", 0);
				pbDown.incrementProgressBy(totalSize);
				// tvDownInfo.setText(GameUtil.toMByB(Complete) + "/"
				// + GameUtil.toMByB(FileSize) + "   "
				// + GameUtil.toMByB(totalSize) + "/秒");
				tvDownInfo.setText(GameUtil.toMByB(Complete) + "/"
						+ GameUtil.toMByB(FileSize));
				if (pbDown.getProgress() == pbDown.getMax()) {
					btDownState.setText(R.string.game_down_install);
					tvDownInfo.setText("下载完成");
					mGameDetail.setStateDown(GameUtil.GAME_DOWN_COMPLETE);
				}
			}
				break;
			}
		}
		Log.d("GameDetail下载更新", "结束");
	}

	private class DataReceiver extends BroadcastReceiver {

		@Override
		public void onReceive(Context arg0, Intent arg1) {
			// TODO Auto-generated method stub
			if (arg1 != null) {
				updateItem(arg1);
			}
		}

	}
}
