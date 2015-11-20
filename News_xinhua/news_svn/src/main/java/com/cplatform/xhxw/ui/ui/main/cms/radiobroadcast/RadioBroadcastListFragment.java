package com.cplatform.xhxw.ui.ui.main.cms.radiobroadcast;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.content.LocalBroadcastManager;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import butterknife.ButterKnife;
import butterknife.Bind;
import com.cplatform.xhxw.ui.Constants;
import com.cplatform.xhxw.ui.R;
import com.cplatform.xhxw.ui.StatisticalKey;
import com.cplatform.xhxw.ui.db.NewsCashDB;
import com.cplatform.xhxw.ui.db.dao.NewsCashDao;
import com.cplatform.xhxw.ui.http.APIClient;
import com.cplatform.xhxw.ui.http.ResponseUtil;
import com.cplatform.xhxw.ui.http.net.BaseResponse;
import com.cplatform.xhxw.ui.http.net.NewsListResponse;
import com.cplatform.xhxw.ui.ui.base.BaseFragment;
import com.cplatform.xhxw.ui.ui.base.widget.DefaultView;
import com.cplatform.xhxw.ui.ui.base.widget.PullRefreshListView;
import com.cplatform.xhxw.ui.ui.main.HomeActivity;
import com.cplatform.xhxw.ui.ui.main.cms.game.GameTypes;
import com.cplatform.xhxw.ui.ui.main.cms.game.GameUtil;
import com.cplatform.xhxw.ui.ui.web.WebViewActivity;
import com.cplatform.xhxw.ui.util.Actions;
import com.cplatform.xhxw.ui.util.AppBrightnessManager;
import com.cplatform.xhxw.ui.util.DateUtil;
import com.cplatform.xhxw.ui.util.ListUtil;
import com.cplatform.xhxw.ui.util.LogUtil;
import com.cplatform.xhxw.ui.util.NetUtils;
import com.google.gson.Gson;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.umeng.analytics.MobclickAgent;
import com.wbtech.ums.UmsAgent;

/**
 * 
 * @ClassName BroadcastListFragment
 * @Description TODO 广播列表
 * @Version 1.0
 * @Author zxe
 * @Creation 2015年8月18日 下午8:39:26
 * @Mender zxe
 * @Modification 2015年8月18日 下午8:39:26
 * @Copyright Copyright © 2013 - 2015 Channelsoft (Beijing) Technology Co.,
 *            Ltd.All Rights Reserved.
 * 
 */
public class RadioBroadcastListFragment extends BaseFragment implements
		OnItemClickListener, PullRefreshListView.PullRefreshListener,
		InterfaceRadioPlayControl {
	private String mChannelid; // 当前类型
	@Bind(R.id.list)
	PullRefreshListView mListView;
	@Bind(R.id.def_view)
	DefaultView mDefView;
	@Bind(R.id.ll_type)
	LinearLayout llType;
	@Bind(R.id.iv_ad)
	ImageView ivAd;
	private AdapterBroadcast adapterRadio;
	private String mLastPublished = "";// 最后一条时间戳
	private AsyncHttpResponseHandler mLoadHandler;
	private static final String TAG = RadioBroadcastListFragment.class
			.getSimpleName();
	private Context con;
	private String curCatid = "";
	private String isfirst = "";// 1，有焦点图，2，无焦点图
	private String type = "";// 获取跟多0，获取最新数据1
	private Receiver mBro;
	int widthType;
	int screenWidth;
	RadioGroup rgTabs;
	ColorStateList csl;
	private List<GameTypes> typeList;// 广播类型列表
	private int isPlayFirst = 0;// 0:默认 1:播放第一条 2:播放最后一条

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_radiobroadcast_list,
				container, false);
		ButterKnife.bind(this, view);
		con = this.getActivity();
		RadioBroadcastFragment.setIrpc(this);
		return view;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		DisplayMetrics dm = getResources().getDisplayMetrics();
		screenWidth = dm.widthPixels;
		mChannelid = "999";
		mDefView.setDefaultImage(R.drawable.bg_empty_view_radio);
		mDefView.setHidenOtherView(mListView);
		setTypeLayout();
		adapterRadio = new AdapterBroadcast(this.getActivity());
		adapterRadio.setListView(mListView);
		setRadioStyleListView();
		mListView.setAdapter(adapterRadio);
		mListView.setOnItemClickListener(this);
		mListView.setPullRefreshListener(this);
		mListView.setCanLoadMore(false);
		mListView.setCanRefresh(true);
		mDefView.setListener(new DefaultView.OnTapListener() {
			@Override
			public void onTapAction() {
				mDefView.setStatus(DefaultView.Status.loading);
				firstLoadData();
			}
		});
		mDefView.setStatus(DefaultView.Status.loading);
		// 如果我们要对ViewPager设置监听，用indicator设置就行了
		loadCacheData();
		RadioBroadcastFragment.setAutoPlay();
		RadioBroadcastFragment.player.setHandlerList(mHandlerList);
	}

	void setRadioStyleListView() {
		mListView.setmHeadArrowImage(R.drawable.refresh_down_radio,
				R.drawable.progressbg_radio);
		mListView.setHeadTextColor(Color.WHITE);
	}

	/**
	 * 
	 * @Name setTypeLayout
	 * @Description TODO 设置列表头--广播分类布局
	 * @return void
	 * @Author zxe
	 * @Date 2015年8月26日 下午5:29:04
	 * 
	 */
	private void setTypeLayout() {
		csl = getResources().getColorStateList(R.color.selector_tabtext_radio);
		widthType = screenWidth / 4;
		// LinearLayout llType = new LinearLayout(con);
		// llType.setOrientation(LinearLayout.HORIZONTAL);
		HorizontalScrollView hsv = new HorizontalScrollView(con);
		hsv.setScrollbarFadingEnabled(false);
		hsv.setBackgroundResource(R.color.transparent_black_half);
		rgTabs = new RadioGroup(con);
		rgTabs.setOrientation(RadioGroup.HORIZONTAL);
		View vSplit = new View(con);
		vSplit.setBackgroundResource(R.color.transparent_white_three_quarter);
		hsv.addView(rgTabs, ViewGroup.LayoutParams.WRAP_CONTENT,
				ViewGroup.LayoutParams.WRAP_CONTENT);
		llType.addView(hsv, ViewGroup.LayoutParams.MATCH_PARENT,
				ViewGroup.LayoutParams.WRAP_CONTENT);
		llType.addView(vSplit, ViewGroup.LayoutParams.MATCH_PARENT, 1);
		// mListView.addHeaderView(llType);
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
		super.onResume();
	}

	@Override
	public void onDestroy() {
		if (mBro != null) {
			LocalBroadcastManager.getInstance(mAct).unregisterReceiver(mBro);
			mBro = null;
		}
		super.onDestroy();
	}

	@Override
	public void onDestroyView() {
		if (null != mLoadHandler) {
			mLoadHandler.cancle();
			mLoadHandler = null;
		}
		mListView = null;
		adapterRadio.clearData();
		adapterRadio.close();
		adapterRadio = null;

//		ButterKnife.reset(this);
		super.onDestroyView();
	}

	private void loadData() {
		// 第一次传;isfirst1 type1
		// 刷新 catid isfirst 2 type1
		// 加载更多 type type0 isfirst 2
		if (isfirst.equals("1")) {
			// clearGameInfomation();
		}
		RadioListRequest request = new RadioListRequest();
		request.setCatid(curCatid);
		request.setUpdatetime(mLastPublished);
		request.setCatlist(isfirst);
		request.setType(type);
		APIClient.radioList(request, radioListHandler);
		MobclickAgent.onEvent(getActivity(), StatisticalKey.channel_onclick);
		UmsAgent.onEvent(getActivity(), StatisticalKey.channel_onclick,
				new String[] { StatisticalKey.key_channelid },
				new String[] { curCatid });
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
			List<DataRadioBroadcast> game_;
			List<GameTypes> gametypes_;
			AdRadio ar;// 广告

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
					RadioListResponse res = (RadioListResponse) response_;
					if (res.getData() != null) {
						RadioListResponse.Conetnt conetnt = res.getData();
						gametypes_ = conetnt.getAudiotypes();
						game_ = new ArrayList<DataRadioBroadcast>();
						List<AdRadio> bottom = conetnt.getAd().getBottom();
						if ((bottom != null) && (bottom.size() > 0)) {
							ar = bottom.get(0);
						}
						List<DataRadioBroadcast> news = conetnt.getList();
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

			@SuppressWarnings("deprecation")
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
							RadioListResponse.class);
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
						updateAdapterData(game_, gametypes_, ar, null);
						mListView.setRefreshTime(new Date(dao_.getSaveTime()));
					}
					if (ListUtil.isEmpty(adapterRadio.getData())) {
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

	public void stopFirstPlay() {
		if (isPlayFirst != 0) {
			RadioBroadcastFragment.player
					.updateUI(RadioBroadcastUtil.STATE_RADIO_STOP);
			isPlayFirst = 0;
		}
	}

	AsyncHttpResponseHandler radioListHandler = new AsyncHttpResponseHandler() {

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
			stopFirstPlay();
			// Log.d("aaa",
			// "doin__cacheChannelId="+mChannelid+"  "+"onFailure");
			LogUtil.d(TAG, this.getUrl());
			LogUtil.d(TAG, "栏目id=" + mChannelid + "  网络链接失败");
			if (adapterRadio == null || adapterRadio.getCount() == 0) {
				if (mDefView != null) {
					mDefView.setStatus(DefaultView.Status.error);
				}
			} else {
				showToast(R.string.load_server_failure);
			}
			mListView.onRefreshComplete(null);
			mListView.startLayoutAnimation();
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

				List<DataRadioBroadcast> game_;
				List<GameTypes> gametypes_;
				BaseResponse response_;
				AdRadio ar;// 广告
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
									RadioListResponse.class);
						} else {
							response_ = new Gson().fromJson(content,
									RadioListRadioResponse.class);
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
						if (response_ instanceof RadioListResponse) {
							RadioListResponse res = (RadioListResponse) response_;
							if (res.getData() != null) {
								RadioListResponse.Conetnt conetnt = res
										.getData();
								gametypes_ = conetnt.getAudiotypes();
								curCatid = conetnt.getCatid();
								game_ = new ArrayList<DataRadioBroadcast>();
								List<AdRadio> bottom = conetnt.getAd()
										.getBottom();
								if ((bottom != null) && (bottom.size() > 0)) {
									ar = bottom.get(0);
								}
								List<DataRadioBroadcast> games = conetnt
										.getList();
								String catid = conetnt.getCatid();
								String catname = conetnt.getCatname();
								if (!ListUtil.isEmpty(games)) {
									for (int i = 0; i < games.size(); i++) {
										games.get(i).setCatid(catid);
										games.get(i).setCatname(catname);
									}
									game_.addAll(games);
								}
							}

						} else {
							RadioListRadioResponse res = (RadioListRadioResponse) response_;
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
						stopFirstPlay();
						return;
					}
					if (!result) {
						stopFirstPlay();
						if (adapterRadio == null
								|| adapterRadio.getCount() == 0) {
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
						if (!ListUtil.isEmpty(game_)) {
							RadioBroadcastUtil.saveRadioList(con, content,
									game_.get(0).getCatid());
						}
						updateAdapterData(game_, gametypes_, ar, mLastPublished);
					} else {
						stopFirstPlay();
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
	private void updateAdapterData(List<DataRadioBroadcast> listGame,
			List<GameTypes> gametypes_, AdRadio ar, String published) {
		if (isDestroyView) {
			return;
		}
		if (TextUtils.isEmpty(published)) {
			adapterRadio.clearData();
		}
		if (!ListUtil.isEmpty(gametypes_) && rgTabs != null) {
			rgTabs.removeAllViews();
			typeList = new ArrayList<GameTypes>();
			typeList.addAll(gametypes_);
			// String firstId = gametypes_.get(0).getCatid();
			for (GameTypes gt : gametypes_) {
				createTypeView(gt, curCatid);
			}
		}
		if (!ListUtil.isEmpty(listGame)) {
			adapterRadio.clearData();
			// 判断当前频道是否为新闻视频
			adapterRadio.addAllData(listGame);
			RadioBroadcastFragment.player.setListData(adapterRadio.getData());
			if (isPlayFirst == 1) {
				RadioBroadcastFragment.player.playByIndex(0);
				isPlayFirst = 0;
			} else if (isPlayFirst == 2) {
				RadioBroadcastFragment.player.playByIndex(listGame.size() - 1);
				isPlayFirst = 0;
			}
			mListView.setCanLoadMore(false);
			// mListView.setCanLoadMore(true);
		} else {
			if (isPlayFirst == 1) {
				setTurnChannelNext();
			} else if (isPlayFirst == 2) {
				setTurnChannelOrderLast();
			}
			mListView.setCanLoadMore(false);
		}
		if (ar != null) {// 底部广告
			setBottomAd(ar);
		}
		adapterRadio.notifyDataSetChanged();
		mListView.startLayoutAnimation();
		mDefView.setStatus(DefaultView.Status.showData);
	}

	private void setBottomAd(final AdRadio ar) {

		if (!TextUtils.isEmpty(ar.getImgurl())) {
			LayoutParams para = ivAd.getLayoutParams();
			if (false == (TextUtils.isEmpty(ar.getWidth()) || TextUtils
					.isEmpty(ar.getHeight()))) {
				para.width = screenWidth;
				para.height = (screenWidth / Integer.parseInt(ar.getWidth()
						.trim())) * Integer.parseInt(ar.getHeight());
				Log.d("广播广告显示大小", "广告的宽" + para.width + "广告的高:" + para.height);
				ivAd.setLayoutParams(para);
			}
			DisplayImageOptions dio = new DisplayImageOptions.Builder()
					.showStubImage(R.drawable.def_img_16_9)
					.showImageForEmptyUri(R.drawable.def_img_16_9)
					.showImageOnFail(R.drawable.def_img_16_9).cacheInMemory()
					.cacheOnDisc().build();
			ImageLoader.getInstance().displayImage(ar.getImgurl(), ivAd, dio);
			ivAd.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View arg0) {
					// TODO Auto-generated method stub
					if (!TextUtils.isEmpty(ar.getUrl())) {
						startActivity(WebViewActivity.getIntentByURL(mAct,
								ar.getUrl(), ar.getTitle()));
					}
				}
			});
			ivAd.setVisibility(View.VISIBLE);
		} else {
			ivAd.setVisibility(View.GONE);
		}
	}

	private void createTypeView(final GameTypes gt, String firstId) {
		RadioButton rb = new RadioButton(con);
		rb.setBackgroundResource(R.drawable.tab_indicator_radiobroadcast);
		rb.setButtonDrawable(getResources().getDrawable(
				android.R.color.transparent));
		rb.setPadding(20, 20, 20, 20);
		if (csl != null) {
			rb.setTextColor(csl);
		}
		rb.setTextSize(20);
		rb.setGravity(Gravity.CENTER);
		rb.setText(gt.getCatname());
		rb.setTag(gt.getCatid());
		rgTabs.addView(rb, widthType, widthType / 2);
		if (gt.getCatid().equals(firstId)) {
			rb.setChecked(true);
		}

		rb.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(CompoundButton arg0, boolean arg1) {
				if (arg1) {
					// TODO Auto-generated method stub
					String tag = arg0.getTag().toString();
					// Toast.makeText(con, "tag:" + tag + "&&text:" +
					// gt.getCatname(),
					// Toast.LENGTH_SHORT).show();
					curCatid = tag;
					Log.e("您点击的当前类型为", gt.getCatname() + "id:" + tag);
					mLastPublished = "";
					isfirst = "2";
					type = "1";
					adapterRadio.clearData();
					loadData();
				}
			}
		});

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
		if (adapterRadio != null) {
			List<DataRadioBroadcast> list = adapterRadio.getData();
			int size = list.size();
			if (size > 0) {
				DataRadioBroadcast curNew = list.get(size - 1);
				mLastPublished = curNew.getUpdatetime();
				isfirst = "2";
				type = "0";
				loadData();
			}
		} else {
			// isLoadMore = false;
			mListView.onLoadMoreComplete();
		}
	}

	private int getPosition(int position) {
		return position - mListView.getHeaderViewsCount();
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		int curPos = getPosition(position);
		DataRadioBroadcast item = adapterRadio.getData().get(curPos);
		if (item != null) {
			if (!TextUtils.isEmpty(item.getUrl())) {
				RadioBroadcastFragment.player.setCurDataRadioBroadcast(item);
				RadioBroadcastFragment.player.setCurIndex(curPos);
				RadioBroadcastFragment.player.playUrl(item.getUrl());
				RotationImageView civItem = (RotationImageView) view
						.findViewById(R.id.civ_icon);
				civItem.startRotation();
				RadioBroadcastFragment.switchTab(0);
			}
		}
	}

	/**
	 * 
	 * @Name setSingleChannelLoop
	 * @Description TODO 单频道下一条播放
	 * @return void
	 * @Author zxe
	 * @Date 2015年9月6日 下午4:39:53
	 * 
	 */
	private void setSingleChannelLoopNext() {
		if (RadioBroadcastFragment.player.isEndList()) {// 结尾,播放列表第一条
			if (ListUtil.isEmpty(RadioBroadcastFragment.player.getListData())) {
			} else {
				RadioBroadcastFragment.player.playByIndex(0);
			}
		} else {// 播放下一条
			RadioBroadcastFragment.player.nextPlay();
		}
	}

	/**
	 * 
	 * @Name setSingleChannelLoopLast
	 * @Description TODO 单频道上一条播放
	 * @return void
	 * @Author zxe
	 * @Date 2015年9月16日 上午11:28:48
	 * 
	 */
	private void setSingleChannelLoopLast() {
		if (RadioBroadcastFragment.player.getCurIndex() == 0) {// 结尾,播放列表第一条
			if (ListUtil.isEmpty(RadioBroadcastFragment.player.getListData())) {

			} else {
				RadioBroadcastFragment.player
						.playByIndex(RadioBroadcastFragment.player
								.getListData().size() - 1);
			}
		} else {// 播放下一条
			RadioBroadcastFragment.player.lastPlay();
		}
	}

	/**
	 * 
	 * @Name setTurnChannelOrder
	 * @Description TODO 转频道顺序下一条播放
	 * @return void
	 * @Author zxe
	 * @Date 2015年9月6日 下午4:43:20
	 * 
	 */
	private void setTurnChannelOrderNext() {
		if (RadioBroadcastFragment.player.isEndList()) {// 结尾,播放列表第一条
			setTurnChannelNext();
		} else {// 播放下一条
			RadioBroadcastFragment.player.nextPlay();
		}
	}

	/**
	 * 
	 * @Name setTurnChannelOrderLast
	 * @Description TODO 转频道播放上一条
	 * @return void
	 * @Author zxe
	 * @Date 2015年9月16日 上午10:53:01
	 * 
	 */
	private void setTurnChannelOrderLast() {
		if (RadioBroadcastFragment.player.getCurIndex() == 0) {// 当前播放为列表第一条
			setTurnChannelLast();
		} else {// 播放下一条
			RadioBroadcastFragment.player.lastPlay();
		}

	}

	/**
	 * 
	 * @Name setTurnChannelNext
	 * @Description TODO 设置跳转下一个频道
	 * @return void
	 * @Author zxe
	 * @Date 2015年9月23日 上午11:44:41
	 * 
	 */
	public void setTurnChannelNext() {
		int index = getCurTypeIndex();
		if (index == (typeList.size() - 1)) {// 当前类型是否为类型列表结尾,停止播放
			RadioButton rb = (RadioButton) rgTabs.findViewWithTag(typeList.get(
					0).getCatid());
			rb.setChecked(true);
		} else {// 获取下一类型数据，更新UI
			RadioButton rb = (RadioButton) rgTabs.findViewWithTag(typeList.get(
					index + 1).getCatid());
			rb.setChecked(true);
		}
		isPlayFirst = 1;
	}

	/**
	 * 
	 * @Name setTurnChannelLast
	 * @Description TODO 设置跳转上一个频道
	 * @return void
	 * @Author zxe
	 * @Date 2015年9月23日 上午11:45:50
	 * 
	 */
	public void setTurnChannelLast() {
		int index = getCurTypeIndex();
		int lastRBIndex = index;
		if (index == 0) {
			lastRBIndex = typeList.size() - 1;
		} else {// 获取下一类型数据，更新UI
			lastRBIndex = index - 1;
		}
		isPlayFirst = 2;
		RadioButton rb = (RadioButton) rgTabs.findViewWithTag(typeList.get(
				lastRBIndex).getCatid());
		rb.setChecked(true);
	}

	/**
	 * 
	 * @Name getCurTypeIndex
	 * @Description TODO 获得当前类型索引
	 * @return
	 * @return int
	 * @Author zxe
	 * @Date 2015年9月6日 下午5:23:21
	 * 
	 */
	public int getCurTypeIndex() {
		int index = 0;
		if (!TextUtils.isEmpty(curCatid)) {
			for (GameTypes gt : typeList) {
				if (curCatid.equals(gt.getCatid())) {
					index = typeList.indexOf(gt);
					break;
				}
			}
		}
		return index;
	}

	/**
	 * 
	 * @Name setNextPlay
	 * @Description TODO 下一条播放
	 * @return void
	 * @Author zxe
	 * @Date 2015年9月6日 下午4:38:11
	 * 
	 */
	@Override
	public void setNextPlay() {
		if (GameUtil.isConnect(con)) {
			if (RadioBroadcastFragment.player.getCurDataRadioBroadcast() != null) {
				if (RadioBroadcastFragment.player.getCurDataRadioBroadcast()
						.getCatid() != curCatid) {
					Log.e("下一条", "当前显示的列表非正播放的广播类型列表");
					RadioBroadcastUtil.getRadioList(con,
							RadioBroadcastFragment.player
									.getCurDataRadioBroadcast().getCatid());
				}
			}
			RadioBroadcastPlayFragment.setEnabled(false);
			// TODO Auto-generated method stub
			if (RadioBroadcastUtil.SETTING_RADIO_MODE == RadioBroadcastUtil.SETTING_RADIO_MODE_TURNCHANNELORDER) {
				setTurnChannelOrderNext();
			} else {
				setSingleChannelLoopNext();
			}
		} else {
			RadioBroadcastUtil.showTipInformationDialog(con,
					R.string.text_play_tips, R.string.text_no_network);
		}
	}

	/**
	 * 
	 * @Name setLastPlay
	 * @Description TODO 上一条播放
	 * @return void
	 * @Author zxe
	 * @Date 2015年9月16日 上午10:09:45
	 * 
	 */
	@Override
	public void setLastPlay() {
		if (GameUtil.isConnect(con)) {
			if (RadioBroadcastFragment.player.getCurDataRadioBroadcast() != null) {
				if (RadioBroadcastFragment.player.getCurDataRadioBroadcast()
						.getCatid() != curCatid) {
					Log.e("上一条", "当前显示的列表非正播放的广播类型列表");
					RadioBroadcastUtil.getRadioList(con,
							RadioBroadcastFragment.player
									.getCurDataRadioBroadcast().getCatid());
				}
			}
			RadioBroadcastPlayFragment.setEnabled(false);
			if (RadioBroadcastUtil.SETTING_RADIO_MODE == RadioBroadcastUtil.SETTING_RADIO_MODE_TURNCHANNELORDER) {
				setTurnChannelOrderLast();
			} else {
				setSingleChannelLoopLast();
			}
		} else {
			RadioBroadcastUtil.showTipInformationDialog(con,
					R.string.text_play_tips, R.string.text_no_network);
		}
	}

	private class Receiver extends BroadcastReceiver {
		@Override
		public void onReceive(Context context, Intent intent) {
			if (adapterRadio != null && intent != null) {
				String action = intent.getAction();
				// 显示模式改变监听
				if (!isDestroyView
						&& Actions.ACTION_DISPLAY_MODEL_CHANGE.equals(action)) {
					adapterRadio.notifyDataSetChanged();
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
					adapterRadio.notifyDataSetChanged();
				}
			}
		}
	}

	/**
	 * 
	 * @Name isPlay
	 * @Description TODO 是否播放
	 * @return
	 * @return boolean
	 * @Author zxe
	 * @Date 2015年9月6日 下午12:04:28
	 * 
	 */
	@Override
	public boolean isPlaying() {
		// TODO Auto-generated method stub
		return (RadioBroadcastFragment.player != null && RadioBroadcastFragment.player
				.isPlaying()) ? true : false;
	}

	@Override
	public void onRadioPause() {
		// TODO Auto-generated method stub
		if (RadioBroadcastFragment.player != null
				&& RadioBroadcastFragment.player.getMediaPlayer() != null) {
			RadioBroadcastFragment.player.pause();
		}
	}

	@Override
	public void onRadioResume() {
		if (RadioBroadcastFragment.player != null
				&& RadioBroadcastFragment.player.getMediaPlayer() != null) {
			RadioBroadcastFragment.player.play();
		}
	}

	@SuppressLint("HandlerLeak")
	private Handler mHandlerList = new Handler() {
		public void handleMessage(Message msg) {
			DataRadioBroadcast drb = RadioBroadcastFragment.player
					.getCurDataRadioBroadcast();
			if (drb != null) {
				switch (msg.what) {
				case RadioBroadcastUtil.STATE_RADIO_PLAY:
					UmsAgent.onEvent(getActivity(),
							StatisticalKey.news_details, new String[] {
									StatisticalKey.key_channelid,
									StatisticalKey.key_newsid,
									StatisticalKey.key_title },
							new String[] { drb.getCatid(), drb.getAudioid(),
									drb.getName() });
					// 设置滚动
					if (android.os.Build.VERSION.SDK_INT >= 8) {
						mListView.smoothScrollToPositionFromTop(
								RadioBroadcastFragment.player.getCurIndex(), 0);
					} else {
						mListView.setSelection(RadioBroadcastFragment.player
								.getCurIndex());
					}
					adapterRadio.updateItem(
							RadioBroadcastFragment.player.getCurIndex(), true);
					break;
				case RadioBroadcastUtil.STATE_RADIO_PAUSE:
					// 设置滚动
					if (android.os.Build.VERSION.SDK_INT >= 8) {
						mListView
								.smoothScrollToPosition(RadioBroadcastFragment.player
										.getCurIndex());
					} else {
						mListView.setSelection(RadioBroadcastFragment.player
								.getCurIndex());
					}
					adapterRadio.updateItem(
							RadioBroadcastFragment.player.getCurIndex(), false);
					break;
				case RadioBroadcastUtil.STATE_RADIO_COMPLETION:
					setNextPlay();
					break;
				}
			}
			super.handleMessage(msg);
		}
	};

	@Override
	public void firstLoadData() {
		// TODO Auto-generated method stub
		isfirst = "1";
		type = "1";
		loadData();
	}
}
