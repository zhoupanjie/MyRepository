package com.cplatform.xhxw.ui.ui.main.cms.game;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.text.TextUtils;
import android.text.TextUtils.TruncateAt;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.ViewFlipper;
import butterknife.ButterKnife;
import butterknife.InjectView;

import com.cplatform.xhxw.ui.App;
import com.cplatform.xhxw.ui.Constants;
import com.cplatform.xhxw.ui.R;
import com.cplatform.xhxw.ui.db.NewsCashDB;
import com.cplatform.xhxw.ui.db.dao.NewsCashDao;
import com.cplatform.xhxw.ui.http.APIClient;
import com.cplatform.xhxw.ui.http.ResponseUtil;
import com.cplatform.xhxw.ui.http.net.BaseResponse;
import com.cplatform.xhxw.ui.http.net.NewsListResponse;
import com.cplatform.xhxw.ui.model.Focus;
import com.cplatform.xhxw.ui.ui.base.BaseFragment;
import com.cplatform.xhxw.ui.ui.base.view.NewsHeaderView;
import com.cplatform.xhxw.ui.ui.base.view.OnSliderImgOnClickListener;
import com.cplatform.xhxw.ui.ui.base.view.SliderView;
import com.cplatform.xhxw.ui.ui.base.widget.DefaultView;
import com.cplatform.xhxw.ui.ui.base.widget.PullRefreshListView;
import com.cplatform.xhxw.ui.ui.main.HomeActivity;
import com.cplatform.xhxw.ui.ui.main.cms.HomeFragment;
import com.cplatform.xhxw.ui.util.Actions;
import com.cplatform.xhxw.ui.util.AppBrightnessManager;
import com.cplatform.xhxw.ui.util.CommonUtils;
import com.cplatform.xhxw.ui.util.DateUtil;
import com.cplatform.xhxw.ui.util.ListUtil;
import com.cplatform.xhxw.ui.util.LogUtil;
import com.cplatform.xhxw.ui.util.NetUtils;
import com.google.gson.Gson;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.viewpagerindicator.CirclePageIndicator;

public class GameFragment extends BaseFragment implements OnItemClickListener,
		PullRefreshListView.PullRefreshListener, OnSliderImgOnClickListener {
	private String mChannelid; // 当前类型
	@InjectView(R.id.list)
	PullRefreshListView mListView;
	@InjectView(R.id.def_view)
	DefaultView mDefView;
	private NewsHeaderView mHeandr;
	private GameAdapter adapterGame;
	private String mLastPublished = "";// 最后一条时间戳
	private AsyncHttpResponseHandler mLoadHandler;
	private static final String TAG = GameFragment.class.getSimpleName();
	private Context con;
	LinearLayout llTypeRoot;
	int VALUE_GAME_TYPE_COLUMN = 4;
	private int countType;
	private String curCatid = "";
	private String curSelectTypeName = "";
	private int curTypeIndex;
	private String isfirst = "";// 1，有焦点图，2，无焦点图
	private String type = "";// 获取跟多0，获取最新数据1
	private String order = "0";// 排序id
	private String firstTypeId = "";// 第一个类型id
	private Receiver mBro;
	private DataReceiver receiverGame;
	private ViewFlipper mFlipper;
	private RelativeLayout rlFlipper;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_game, container, false);
		ButterKnife.inject(this, view);
		con = this.getActivity();
		return view;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		mChannelid = getArguments().getString("channelid");
		mHeandr = new NewsHeaderView(getActivity());
		addGameInformation();
		mDefView.setHidenOtherView(mListView);
		llTypeRoot = new LinearLayout(con);
		mListView.addHeaderView(llTypeRoot);
		adapterGame = new GameAdapter(this.getActivity());
		adapterGame.setListView(mListView);
		mListView.setAdapter(adapterGame);
		mListView.setOnItemClickListener(this);
		mListView.setPullRefreshListener(this);
		mListView.setCanLoadMore(false);
		mListView.setCanRefresh(true);
		mDefView.setListener(new DefaultView.OnTapListener() {
			@Override
			public void onTapAction() {
				mDefView.setStatus(DefaultView.Status.loading);
				loadData();
			}
		});
		mDefView.setStatus(DefaultView.Status.loading);
		loadCacheData();
	}

	class OnTouchRefreshListView implements OnTouchListener {

		@Override
		public boolean onTouch(View arg0, MotionEvent event) {
			float x1 = 0;
			float x2 = 0;
			float y1 = 0;
			float y2 = 0;
			// 继承了Activity的onTouchEvent方法，直接监听点击事件
			if (event.getAction() == MotionEvent.ACTION_DOWN) {
				// 当手指按下的时候
				x1 = event.getX();
				y1 = event.getY();
			}
			if (event.getAction() == MotionEvent.ACTION_UP) {
				// 当手指离开的时候
				x2 = event.getX();
				y2 = event.getY();
				if (y1 - y2 > 50) {
					Log.d("zxe_" + this.getClass().getName() + "滑动时间", "向上滑");
				} else if (y2 - y1 > 50) {
					Log.d("zxe_" + this.getClass().getName() + "滑动时间", "向下滑");
				} else if (x1 - x2 > 50) {
					Log.d("zxe_" + this.getClass().getName() + "滑动时间", "向左滑");
				} else if (x2 - x1 > 50) {
					Log.d("zxe_" + this.getClass().getName() + "滑动时间", "向右滑");
				}
			}
			return true;
		}

	}

	/**
	 * 
	 * @Name addGameInformation
	 * @Description TODO 添加游戏资讯 可放在滚屏和游戏列表中间位置
	 * @return void
	 * @Author zxe
	 * @Date 2015年8月6日 下午2:52:15
	 * 
	 */
	@SuppressWarnings("deprecation")
	void addGameInformation() {
		RelativeLayout rlRoot = new RelativeLayout(con);
		rlRoot.addView(mHeandr);
		rlFlipper = new RelativeLayout(con);
		rlRoot.setOnTouchListener(new OnTouchRefreshListView());
		RelativeLayout.LayoutParams rllp = new RelativeLayout.LayoutParams(
				ViewGroup.LayoutParams.MATCH_PARENT,
				ViewGroup.LayoutParams.WRAP_CONTENT);
		rllp.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
		rllp.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
		mFlipper = new ViewFlipper(con);
		mFlipper.setFlipInterval(1000 * 3);
		mFlipper.setInAnimation(AnimationUtils.loadAnimation(con,
				R.anim.push_up_in_game));
		mFlipper.setOutAnimation(AnimationUtils.loadAnimation(con,
				R.anim.push_up_out));
		// mFlipper.setInAnimation(AnimationUtils.loadAnimation(con,
		// R.anim.push_left_in));
		// mFlipper.setOutAnimation(AnimationUtils.loadAnimation(con,
		// R.anim.push_left_out));
		// mFlipper.setInAnimation(AnimationUtils.loadAnimation(con,
		// R.anim.hyperspace_in));
		// mFlipper.setOutAnimation(AnimationUtils.loadAnimation(con,
		// R.anim.hyperspace_out));
		rlRoot.addView(rlFlipper, rllp);
		mListView.addHeaderView(rlRoot);
	}

	@Override
	public void onResume() {
		if (mBro != null) {
			LocalBroadcastManager.getInstance(mAct).unregisterReceiver(mBro);
		}
		mBro = new Receiver();
		IntentFilter filter = new IntentFilter();
		filter.addAction(Actions.ACTION_DISPLAY_MODEL_CHANGE);
		filter.addAction(Actions.ACTION_CHANNE_RESELECTED);
		filter.addAction(Actions.ACTION_CHANNEL_RELOAD_CHECK_TIME);
		filter.addAction(Actions.ACTION_NEWSLIST_TEXTSIZE_CHANGE);
		LocalBroadcastManager.getInstance(mAct).registerReceiver(mBro, filter);
		receiverGame = new DataReceiver();
		IntentFilter filterGame = new IntentFilter(GameUtil.ACTION_GAME);
		con.registerReceiver(receiverGame, filterGame);

		if (GameUtil.isNullInstallList() == false) {
			List<APKInstallData> laid = GameUtil.getInstallListByUI(1);
			if (laid.isEmpty() == false) {
				for (APKInstallData aid : laid) {
					String gameId = aid.getGameId();
					// String downUrl = data.getStringExtra("DownUrl");
					// String apkPath = data.getStringExtra("APKPath");
					String packageName = aid.getPackageName();
					adapterGame.updateInstallResult(gameId, packageName);
				}
			}
		}
		adapterGame.notifyDataSetChanged();
		super.onResume();
	}

	@Override
	public void onDestroy() {
		clearGameInfomation();
		if (mBro != null) {
			LocalBroadcastManager.getInstance(mAct).unregisterReceiver(mBro);
			mBro = null;
		}
		con.unregisterReceiver(receiverGame);

		App.channel_id = null;
		App.news_id = null;
		super.onDestroy();
	}

	@Override
	public void onDestroyView() {
		clearGameInfomation();
		if (null != mLoadHandler) {
			mLoadHandler.cancle();
			mLoadHandler = null;
		}
		mListView = null;
		adapterGame.clearData();
		adapterGame.close();
		adapterGame = null;

		ButterKnife.reset(this);
		super.onDestroyView();
	}

	public void clearGameInfomation() {
		if (null != mFlipper) {// 游戏资讯
			mFlipper.stopFlipping();
			mFlipper.removeAllViews();
		}
	}

	private void loadData() {
		// 第一次传;isfirst1 type1
		// 刷新 catid isfirst 2 type1
		// 加载更多 type type0 isfirst 2
		if (isfirst.equals("1")) {
			clearGameInfomation();
		}
		GameListRequest request = new GameListRequest();
		request.setCatid(curCatid);
		request.setUpdatetime(mLastPublished);
		request.setIsfirst(isfirst);
		request.setOrder(order);
		request.setIstest(GameUtil.istest);
		request.setType(type);
		APIClient.gameList(request, gameListHandler);
	}

	/**
	 * 重新加载数据
	 */
	private void reloadData() {
		NetUtils.Status networkState = NetUtils.getNetworkState();
		if (mDefView.getStatus() != DefaultView.Status.loading
				&& !mListView.isLoading()
				&& networkState != NetUtils.Status.NONE) {
			switch (mDefView.getVisibility()) {
			case View.VISIBLE:
				mDefView.setStatus(DefaultView.Status.loading);
				loadData();
				break;
			case View.GONE:
				mListView.triggerRefresh(true);
				break;
			}
		}
	}

	/**
	 * 
	 * @Name loadCacheData
	 * @Description TODO 加载缓存数据
	 * @return void
	 * @Author zxe
	 * @Date 2015年6月25日 下午5:29:39
	 * 
	 */
	void loadCacheData() {
		// 加载缓存数据
		new AsyncTask<Void, Void, Void>() {

			private NewsCashDao dao_;
			private NetUtils.Status networkState_;
			private BaseResponse response_;
			List<Game> game_;
			List<Focus> focus_;
			List<GameTypes> gametypes_;
			List<GameScrollbars> gamescrollbars_;

			@Override
			protected Void doInBackground(Void... params) {
				String cacheChannelId = mChannelid;
				if (HomeActivity.mIsUnderEnterprise) {
					cacheChannelId = mChannelid + "en";
				}
				// Log.d("aaa", "doin__cacheChannelId="+cacheChannelId);
				dao_ = NewsCashDB.getNewsCashByColumnId(mAct, cacheChannelId);
				networkState_ = NetUtils.getNetworkState();
				response_ = getResponse(dao_);
				if (response_ != null && response_ instanceof NewsListResponse) {
					GameListResponse res = (GameListResponse) response_;
					if (res.getData() != null) {
						GameListResponse.Conetnt conetnt = res.getData();
						focus_ = conetnt.getFocus();
						gametypes_ = conetnt.getGametypes();
						gamescrollbars_ = conetnt.getGamescrollbars();
						game_ = new ArrayList<Game>();
						List<Game> news = conetnt.getList();
						if (!ListUtil.isEmpty(news)) {
							game_.addAll(news);
						}
					}
				}
				// if (!ListUtil.isEmpty(game_)) {
				// for (Game item : game_) {
				// ReadGameDao dao = ReadGameDB.getReadGameById(mAct,
				// item.getId());
				// if (dao != null) {
				// item.setRead(true);
				// }
				// }
				// }
				return null;
			}

			private BaseResponse getResponse(NewsCashDao dao) {
				if (dao == null) {
					// Log.d("aaa",
					// "doin__cacheChannelId="+mChannelid+"  "+"dao == null");
					return null;
				}
				try {
					if (TextUtils.isEmpty(dao.getJson())) {
						// Log.d("aaa",
						// "doin__cacheChannelId="+mChannelid+"  "+"新闻列表内容为空");
						throw new NullPointerException("新闻id::" + mChannelid
								+ "  新闻列表内容为空！");
					}
					ResponseUtil.checkResponse(dao.getJson());
					// Log.d("aaa",
					// "doin__cacheChannelId="+mChannelid+"  "+"return new Gson()");
					return new Gson().fromJson(dao.getJson(),
							GameListResponse.class);
				} catch (Exception e) {
					// Log.d("aaa",
					// "doin__cacheChannelId="+mChannelid+"  "+"getResponse__Exception e");
					LogUtil.w(TAG, e);
				}
				return null;
			}

			@Override
			protected void onPostExecute(Void aVoid) {
				if (mAct != null && !isDestroyView) {
					// Log.d("aaa",
					// "doin__cacheChannelId="+mChannelid+"  "+"mAct != null && !isDestroyView");
					if (response_ != null && response_.isSuccess()) {
						updateAdapterData(game_, focus_, gamescrollbars_,
								gametypes_, null);
						mListView.setRefreshTime(new Date(dao_.getSaveTime()));
					}
					if (ListUtil.isEmpty(adapterGame.getData())) {
						if (networkState_ == NetUtils.Status.NONE) {
							mDefView.setStatus(DefaultView.Status.error);
						} else {
							// Log.d("aaa",
							// "doin__cacheChannelId="+mChannelid+"  "+"DefaultView.Status.loading");
							mDefView.setStatus(DefaultView.Status.loading);
							// 第一次加载
							isfirst = "1";
							type = "1";
							loadData();
						}
					} else if (networkState_ != NetUtils.Status.NONE
							&& isReloadNetData()) {
						// 当前为网络状态，并且距离上次加载时间超过十分钟
						mListView.triggerRefresh(true);
					}
				}
			}
		}.execute();
	}

	/**
	 * 判断是否需要重新加载数据<\br> 如果十分钟内刷新过就不在刷新数据异常
	 */
	private boolean isReloadNetData() {
		return (DateUtil.getTime() - mListView.getOldRefreshTime()) > 180000;
	}

	AsyncHttpResponseHandler gameListHandler = new AsyncHttpResponseHandler() {

		@Override
		protected void onPreExecute() {
			// Log.d("aaa",
			// "doin__cacheChannelId="+mChannelid+"  "+"onPreExecute");
			if (mLoadHandler != null)
				mLoadHandler.cancle();
			mLoadHandler = this;
		}

		/**
		 * 被取消以后调用（调用cancel返回会导致onCanceled被调用）
		 */
		protected void onCanceled() {
			// Log.d("aaa",
			// "doin__cacheChannelId="+mChannelid+"  "+"onCanceled");
		}

		@Override
		public void onFailure(Throwable error, String content) {
			// Log.d("aaa",
			// "doin__cacheChannelId="+mChannelid+"  "+"onFailure");
			LogUtil.d(TAG, this.getUrl());
			LogUtil.d(TAG, "栏目id=" + mChannelid + "  网络链接失败");
			if (adapterGame == null || adapterGame.getCount() == 0) {
				if (mDefView != null) {
					mDefView.setStatus(DefaultView.Status.error);
				}
			} else {
				showToast(R.string.load_server_failure);
			}
			mListView.onRefreshComplete(null);
			// isLoadMore = false;
			mListView.onLoadMoreComplete();
		}

		@Override
		public void onSuccess(int statusCode, final String content) {
			// Log.d("aaa",
			// "doin__cacheChannelId="+mChannelid+"  "+"onSuccess");
			LogUtil.d(TAG, this.getUrl());
			LogUtil.d(TAG, "新闻列表::id=" + mChannelid + "  published::"
					+ mLastPublished + "  json::" + content);
			new AsyncTask<AsyncHttpResponseHandler, Void, Boolean>() {

				List<Game> game_;
				// List<Today> today_;
				List<Focus> focus_;
				List<GameTypes> gametypes_;
				List<GameScrollbars> gamescrollbars_;
				// Thumbnail thum_;
				BaseResponse response_;
				private AsyncHttpResponseHandler handler_;

				@SuppressWarnings("deprecation")
				@Override
				protected Boolean doInBackground(
						AsyncHttpResponseHandler... params) {
					// Log.d("aaa",
					// "doin__cacheChannelId="+mChannelid+"  "+"doInBackground");
					if (params != null && params.length > 0) {
						handler_ = params[0];
					}
					try {
						ResponseUtil.checkResponse(content);
						if (TextUtils.isEmpty(mLastPublished)) {
							response_ = new Gson().fromJson(content,
									GameListResponse.class);
						} else {
							response_ = new Gson().fromJson(content,
									GameListGameResponse.class);
						}
					} catch (Exception e) {
						LogUtil.w(TAG, e);
						LogUtil.w(TAG, "数据：" + content);
						return false;
					}
					if (response_.isSuccess()) {
						if (TextUtils.isEmpty(mLastPublished)) {
							saveData(content);
						}
						if (response_ instanceof GameListResponse) {
							GameListResponse res = (GameListResponse) response_;
							if (res.getData() != null) {
								GameListResponse.Conetnt conetnt = res
										.getData();
								focus_ = conetnt.getFocus();
								gametypes_ = conetnt.getGametypes();
								gamescrollbars_ = conetnt.getGamescrollbars();
								game_ = new ArrayList<Game>();
								List<Game> games = conetnt.getList();
								if (!ListUtil.isEmpty(games)) {
									game_.addAll(games);
								}
							}
						} else {
							GameListGameResponse res = (GameListGameResponse) response_;
							game_ = res.getData();
						}
						// if (!ListUtil.isEmpty(game_)) {
						// for (Game item : game_) {
						// ReadGameDao dao = ReadGameDB.getReadGameById(
						// mAct.getApplicationContext(),
						// item.getId());
						// if (dao != null) {
						// item.setRead(true);
						// }
						// }
						// }
					}
					return true;
				}

				@Override
				protected void onPostExecute(Boolean result) {
					if (handler_.isCancled() || handler_ != mLoadHandler
							|| mLoadHandler == null) {
						return;
					}
					if (!result) {
						if (adapterGame == null || adapterGame.getCount() == 0) {
							if (mDefView != null) {
								mDefView.setStatus(DefaultView.Status.error);
							}
						}
						showToast(R.string.data_format_error);
						mListView.onRefreshComplete(null);
						// isLoadMore = false;
						mListView.onLoadMoreComplete();
						return;
					}
					if (response_ != null && response_.isSuccess()) {
						if (TextUtils.isEmpty(mLastPublished)) {
							mListView.setRefreshTime(new Date());
						}
						updateAdapterData(game_, focus_, gamescrollbars_,
								gametypes_, mLastPublished);
					} else {
						mDefView.setStatus(DefaultView.Status.error);
						String message = response_ == null ? "客户端无内容解析"
								: response_.getMsg();
						LogUtil.e(TAG, "栏目id=" + mChannelid + "  message:"
								+ message);
					}

					if (TextUtils.isEmpty(mLastPublished)) {
						Date date = new Date();
						Constants.setsOldNewsLoadTime(date.getTime());
						mListView.onRefreshComplete(date);
					} else {
						// isLoadMore = false;
						mListView.onLoadMoreComplete();
					}
					if (game_ != null) {
						game_.clear();
					}

					if (focus_ != null) {
						focus_.clear();
					}
					if (gamescrollbars_ != null) {
						gamescrollbars_.clear();
					}
					if (gametypes_ != null) {
						gametypes_.clear();
					}

					response_ = null;
					handler_ = null;
				}
			}.execute(this);
		}
	};

	/**
	 * 更新数据
	 * 
	 * @param published
	 */
	private void updateAdapterData(List<Game> listGame, List<Focus> focus_,
			List<GameScrollbars> gamescrollbars_, List<GameTypes> gametypes_,
			String published) {
		GameUtil.ISFINSHING = false;
		if (isDestroyView) {
			return;
		}
		if (!ListUtil.isEmpty(focus_)) {
			mHeandr.setSlider(focus_, this);
		}
		if (!ListUtil.isEmpty(gamescrollbars_)) {
			setGameInformationData(gamescrollbars_);
		}
		if (!ListUtil.isEmpty(gametypes_)) {
			getTypeLayout(gametypes_);
		}
		if (TextUtils.isEmpty(published)) {
			adapterGame.clearData();
		}

		if (!ListUtil.isEmpty(listGame)) {
			// 判断当前频道是否为新闻视频
			adapterGame.addAllData(listGame);
			mListView.setCanLoadMore(true);
		} else {
			mListView.setCanLoadMore(false);
		}
		if ((adapterGame.getData() != null)
				&& (false == adapterGame.getData().isEmpty())) {
			GameManager.setListGame(adapterGame.getData());
		}
		adapterGame.notifyDataSetChanged();
		if ((adapterGame.getData() == null || adapterGame.getData().size() < 1)) {
			if (HomeFragment.currentShowChannel != null
					&& HomeFragment.currentShowChannel.equals(mChannelid)) {
				// mAct.showToast("很抱歉，无相关内容");
			}
		} else {
			// aaa
			// UmsAgent.onEvent(getActivity(), "", null,
			// mChannelid, mAdapter.getCount() + "");
		}
		mDefView.setStatus(DefaultView.Status.showData);
	}

	/**
	 * 
	 * @Name setGameInformationData
	 * @Description TODO 设置游戏资讯
	 * @return void
	 * @Author zxe
	 * @Date 2015年8月7日 下午2:05:07
	 * 
	 */
	private void setGameInformationData(List<GameScrollbars> gamescrollbars_) {
		int count = mHeandr.getChildCount();
		int height = 0;
		int width = 0;
		for (int i = 0; i <= count; i++) {
			View v = mHeandr.getChildAt(i);
			if (v instanceof SliderView) {
				CirclePageIndicator cpi = (CirclePageIndicator) mHeandr
						.findViewById(R.id.view_indicator);
				int w = View.MeasureSpec.makeMeasureSpec(0,
						View.MeasureSpec.UNSPECIFIED);
				int h = View.MeasureSpec.makeMeasureSpec(0,
						View.MeasureSpec.UNSPECIFIED);
				cpi.measure(w, h);
				height = cpi.getMeasuredHeight();
				width = cpi.getMeasuredWidth();
				break;
			}
		}
		rlFlipper.removeAllViews();
		RelativeLayout.LayoutParams rllp;
		if (height != 0 && width != 0) {
			rllp = new RelativeLayout.LayoutParams(
					ViewGroup.LayoutParams.MATCH_PARENT, height);
			rllp.setMargins(10, 0, width + 10, 10);
		} else {
			rllp = new RelativeLayout.LayoutParams(
					ViewGroup.LayoutParams.MATCH_PARENT,
					ViewGroup.LayoutParams.WRAP_CONTENT);
			rllp.setMargins(10, 0, 250, 0);
		}
		rllp.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
		rllp.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
		rlFlipper.addView(mFlipper, rllp);
		for (GameScrollbars gs : gamescrollbars_) {
			setGameInformationChild(gs);
		}
		mFlipper.startFlipping();
	}

	/**
	 * 
	 * @Name setGameInformationChild
	 * @Description TODO 设置游戏资讯子项
	 * @param g
	 * @return void
	 * @Author zxe
	 * @Date 2015年8月7日 下午2:13:53
	 * 
	 */
	private void setGameInformationChild(GameScrollbars gs) {
		LinearLayout llChild = new LinearLayout(con);
		llChild.setGravity(Gravity.CENTER);
		llChild.setTag(gs.getId());
		llChild.setOrientation(LinearLayout.HORIZONTAL);
		ImageView ivHorn = new ImageView(con);
		ivHorn.setImageResource(R.drawable.game_title_horn);
		ivHorn.setPadding(20, 0, 10, 0);

		LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
				LayoutParams.WRAP_CONTENT, LayoutParams.FILL_PARENT);
		params.weight = 1.0f;
		TextView textView = new TextView(con);
		textView.setGravity(Gravity.LEFT | Gravity.CENTER_VERTICAL);
		textView.setTextColor(this.getResources().getColor(R.color.bestred));
		textView.setText(gs.getName());
		textView.setSingleLine(true);
		textView.setEllipsize(TruncateAt.END);
		textView.setPadding(20, 0, 20, 0);

		TextView tvEnd = new TextView(con);
		tvEnd.setGravity(Gravity.RIGHT | Gravity.CENTER_VERTICAL);
		tvEnd.setTextColor(this.getResources().getColor(R.color.bestred));
		tvEnd.setText("猛戳>>");
		llChild.addView(ivHorn);
		llChild.addView(textView, params);
		llChild.addView(tvEnd);
		mFlipper.addView(llChild);

		llChild.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				LinearLayout ll = (LinearLayout) v;
				Log.d("游戏资讯点击id", ll.getTag().toString());
				startActivity(GameDetailActivity.getIntent(mAct, ll.getTag()
						.toString(), HomeActivity.mIsUnderEnterprise));
				// Toast.makeText(con, tv.getTag().toString(),
				// Toast.LENGTH_SHORT).show();
			}
		});
	}

	/**
	 * 
	 * @Name getTypeLayout
	 * @Description TODO 获得类型布局
	 * @param lgt
	 * @return void
	 * @Author zxe
	 * @Date 2015年6月30日 下午7:07:23
	 * 
	 */
	private void getTypeLayout(List<GameTypes> lgt) {
		firstTypeId = lgt.get(0).getCatid();
		countType = lgt.size();
		llTypeRoot.removeAllViews();
		// llTypeRoot.setClickable(false);
		llTypeRoot.setContentDescription("游戏类型");
		llTypeRoot.setOrientation(LinearLayout.VERTICAL);
		llTypeRoot.setPadding(30, 30, 30, 30);
		LinearLayout llLine = null;
		LinearLayout.LayoutParams lpLine = new LinearLayout.LayoutParams(
				LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
		LinearLayout.LayoutParams lpColumn = new LinearLayout.LayoutParams(
				LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT, 1.0f);
		for (int i = 0; i < lgt.size(); i++) {
			if (0 == i % VALUE_GAME_TYPE_COLUMN) {
				llLine = new LinearLayout(con);
				llLine.setContentDescription("行类型");
				llLine.setOrientation(LinearLayout.HORIZONTAL);
				llLine.addView(getTypeTextView(lgt.get(i), i), lpColumn);
			} else {
				if (llLine != null) {
					llLine.addView(getTypeTextView(lgt.get(i), i), lpColumn);
					if ((VALUE_GAME_TYPE_COLUMN - 1) == i
							% VALUE_GAME_TYPE_COLUMN) {
						llTypeRoot.addView(llLine, lpLine);
					}
				}
			}
		}
	}

	/**
	 * 
	 * @Name updateTypeState
	 * @Description TODO 更新type状态
	 * @param tag
	 * @return void
	 * @Author zxe
	 * @Date 2015年6月30日 下午7:06:14
	 * 
	 */
	@SuppressLint("ResourceAsColor")
	private void updateTypeState(String tag, int index) {
		Log.e("当前点击游戏类型", tag);
		if (false == curSelectTypeName.equals(tag)) {
			if (false == TextUtils.isEmpty(curSelectTypeName)) {
				TextView tvOld = (TextView) llTypeRoot
						.findViewWithTag(curSelectTypeName);
				tvOld.setBackgroundResource(getGameTypeBg(curTypeIndex, false));
				setSelectTypeTextColor(tvOld, false);
			}
			TextView tvNew = (TextView) llTypeRoot.findViewWithTag(tag);
			tvNew.setBackgroundResource(getGameTypeBg(index, true));
			setSelectTypeTextColor(tvNew, true);
			curSelectTypeName = tag;
			curTypeIndex = index;
		}
	}

	/**
	 * 
	 * @Name setSelectTypeTextColor
	 * @Description TODO 设置选择类型的字体颜色
	 * @param tv
	 * @param isSelect
	 * @return void
	 * @Author zxe
	 * @Date 2015年7月1日 上午9:41:06
	 * 
	 */
	private void setSelectTypeTextColor(TextView tv, boolean isSelect) {
		if (isSelect) {
			tv.setTextColor(Color.WHITE);
		} else {
			tv.setTextColor(this.getResources().getColor(R.color.bestred));
		}
	}

	/**
	 * 
	 * @Name getGameTypeBg
	 * @Description TODO 获得对应游戏类型背景图片
	 * @param index
	 * @return
	 * @return int
	 * @Author zxe
	 * @Date 2015年6月25日 下午3:17:01
	 * 
	 */
	private int getGameTypeBg(int index, boolean isSelect) {
		int column = index % VALUE_GAME_TYPE_COLUMN;
		int line = (int) Math.floor(index / VALUE_GAME_TYPE_COLUMN);
		int maxlin = (int) Math.floor(countType / VALUE_GAME_TYPE_COLUMN);
		Log.e("添加游戏类型按钮", "行" + line + "列" + column);
		if (line == 0) {// 第一行
			if (column == 0) {// 第一列
				if (isSelect) {
					return R.drawable.game_type_up_start_select;
				} else {
					return R.drawable.game_type_up_start_default;
				}
			} else if (column == VALUE_GAME_TYPE_COLUMN - 1) {// 最后一行
				if (isSelect) {
					return R.drawable.game_type_up_end_select;
				} else {
					return R.drawable.game_type_up_end_default;
				}
			} else {
				if (isSelect) {
					return R.drawable.game_type_up_middle_select;
				} else {
					return R.drawable.game_type_up_middle_default;
				}
			}
		} else if (line == (maxlin - 1)) {// 最后一行
			if (column == 0) {// 第一列
				if (isSelect) {
					return R.drawable.game_type_down_start_select;
				} else {
					return R.drawable.game_type_down_start_default;
				}
			} else if (column == VALUE_GAME_TYPE_COLUMN - 1) {// 最后一行
				if (isSelect) {
					return R.drawable.game_type_down_end_select;
				} else {
					return R.drawable.game_type_down_end_default;
				}
			} else {
				if (isSelect) {
					return R.drawable.game_type_down_middle_select;
				} else {
					return R.drawable.game_type_down_middle_default;
				}
			}
		} else {
			if (isSelect) {
				return R.drawable.game_type_middle_select;
			} else {
				return R.drawable.game_type_middle_default;
			}
		}
	}

	/**
	 * 
	 * @Name getTypeTextView
	 * @Description TODO
	 * @param text
	 * @param line
	 *            所在行
	 * @param column
	 *            所在列
	 * @return
	 * @return TextView
	 * @Author zxe
	 * @Date 2015年6月25日 下午2:58:53
	 * 
	 */
	@SuppressLint("ResourceAsColor")
	private TextView getTypeTextView(final GameTypes gt, final int index) {
		TextView tv = new TextView(con);
		tv.setText(gt.getCatname());
		tv.setTag(gt.getCatname());
		tv.setGravity(Gravity.CENTER);
		tv.setClickable(true);
		if (TextUtils.isEmpty(curCatid) && gt.getCatid().endsWith(firstTypeId)) {
			tv.setBackgroundResource(getGameTypeBg(index, true));
			setSelectTypeTextColor(tv, true);
			curSelectTypeName = gt.getCatname();
			curCatid = gt.getCatid();
			curTypeIndex = index;
		} else if ((false == TextUtils.isEmpty(curCatid))
				&& gt.getCatid().endsWith(curCatid)) {
			tv.setBackgroundResource(getGameTypeBg(index, true));
			setSelectTypeTextColor(tv, true);
		} else {
			tv.setBackgroundResource(getGameTypeBg(index, false));
			setSelectTypeTextColor(tv, false);
		}
		tv.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {// 切换类型
				// TODO Auto-generated method stub
				if (false == arg0.isSelected()) {
					String curTag = (String) arg0.getTag();
					updateTypeState(curTag, index);
					curCatid = gt.getCatid();
					mLastPublished = "";
					isfirst = "2";
					type = "1";
					order = "0";
					adapterGame.clearData();
					loadData();
				}
			}
		});
		return tv;
	}

	/**
	 * 保存数据
	 */
	private void saveData(String content) {
		NewsCashDao dao = new NewsCashDao();
		String cacheChannelId = mChannelid;
		if (HomeActivity.mIsUnderEnterprise) {
			cacheChannelId = mChannelid + "en";
		}
		dao.setColumnId(cacheChannelId);
		dao.setJson(content);
		dao.setSaveTime(DateUtil.getTime());
		NewsCashDB.saveData(mAct, dao);
	}

	@Override
	public void onRefresh() {
		// cancelDownGame();
		// TODO Auto-generated method stub
		mLastPublished = "";
		isfirst = "1";
		type = "1";
		loadData();
	}

	@Override
	public void onLoadMore() {
		// cancelDownGame();
		// TODO Auto-generated method stub
		// isLoadMore = true;
		if (adapterGame != null) {
			List<Game> list = adapterGame.getData();
			int size = list.size();
			if (size > 0) {
				Game curNew = list.get(size - 1);
				mLastPublished = curNew.getUpdatetime();
				curCatid = curNew.getCatid();
				isfirst = "2";
				type = "0";
				order = curNew.getOrder();
				loadData();
			}
		} else {
			// isLoadMore = false;
			mListView.onLoadMoreComplete();
		}
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		// TODO Auto-generated method stub
		if (CommonUtils.isFastDoubleClick()) {
			return;
		}
		int curPos;
		if (mListView.getHeaderViewsCount() > 2) {
			curPos = getPosition(position)
					- (mListView.getHeaderViewsCount() - 2);
		} else {
			curPos = getPosition(position);
		}
		if (curPos >= 0) {
			Game item = adapterGame.getItem(curPos);
			startActivity(GameDetailActivity.getIntent(mAct, item.getId(),
					HomeActivity.mIsUnderEnterprise));
		}
	}

	private int getPosition(int position) {
		return mHeandr == null ? position - 1 : position - 2;
	}

	@Override
	public void onSliderImgOnClick(Focus focus) {
		// TODO Auto-generated method stub
		if (CommonUtils.isFastDoubleClick()) {
			return;
		}
		startActivity(GameDetailActivity.getIntent(mAct, focus.getId(),
				HomeActivity.mIsUnderEnterprise));
	}

	// 广播接收器
	private class DataReceiver extends BroadcastReceiver {// 继承自BroadcastReceiver的子类
		@Override
		public void onReceive(Context context, Intent intent) {// 重写onReceive方法
			if (adapterGame != null && intent != null) {
				adapterGame.updateItem(intent);
			}
		}
	}

	private class Receiver extends BroadcastReceiver {
		@Override
		public void onReceive(Context context, Intent intent) {
			if (adapterGame != null && intent != null) {
				String action = intent.getAction();
				// 显示模式改变监听
				if (!isDestroyView
						&& Actions.ACTION_DISPLAY_MODEL_CHANGE.equals(action)) {
					adapterGame.notifyDataSetChanged();
					AppBrightnessManager.setScreenBrightness(getActivity());
				} else if (!isDestroyView
						&& Actions.ACTION_CHANNE_RESELECTED.equals(action)
						&& mChannelid
								.equals(intent.getStringExtra("channelid"))) {
					reloadData();
				} else if (!isDestroyView
						&& Actions.ACTION_CHANNEL_RELOAD_CHECK_TIME
								.equals(action)
						&& mChannelid
								.equals(intent.getStringExtra("channelid"))
						&& isReloadNetData()) {
					reloadData();
				} else if (!isDestroyView
						&& Actions.ACTION_NEWSLIST_TEXTSIZE_CHANGE
								.equals(action)) {
					adapterGame.notifyDataSetChanged();
				}
			}
		}
	}
}
