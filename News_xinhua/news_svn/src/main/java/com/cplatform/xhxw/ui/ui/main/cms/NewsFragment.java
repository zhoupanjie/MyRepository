package com.cplatform.xhxw.ui.ui.main.cms;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.ActivityInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AbsoluteLayout;
import android.widget.AbsoluteLayout.LayoutParams;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import butterknife.ButterKnife;
import butterknife.Bind;

import com.cplatform.xhxw.ui.App;
import com.cplatform.xhxw.ui.Constants;
import com.cplatform.xhxw.ui.R;
import com.cplatform.xhxw.ui.StatisticalKey;
import com.cplatform.xhxw.ui.db.NewsCashDB;
import com.cplatform.xhxw.ui.db.ReadNewsDB;
import com.cplatform.xhxw.ui.db.dao.NewsCashDao;
import com.cplatform.xhxw.ui.db.dao.ReadNewsDao;
import com.cplatform.xhxw.ui.http.APIClient;
import com.cplatform.xhxw.ui.http.ResponseUtil;
import com.cplatform.xhxw.ui.http.net.BaseResponse;
import com.cplatform.xhxw.ui.http.net.NewsListNewsResponse;
import com.cplatform.xhxw.ui.http.net.NewsListRequest;
import com.cplatform.xhxw.ui.http.net.NewsListResponse;
import com.cplatform.xhxw.ui.http.responseType.NewsType;
import com.cplatform.xhxw.ui.http.responseType.ShowType;
import com.cplatform.xhxw.ui.model.Ad;
import com.cplatform.xhxw.ui.model.AdvertismentsResponse;
import com.cplatform.xhxw.ui.model.Focus;
import com.cplatform.xhxw.ui.model.FunctionRecommend;
import com.cplatform.xhxw.ui.model.New;
import com.cplatform.xhxw.ui.model.Other;
import com.cplatform.xhxw.ui.model.PicShow;
import com.cplatform.xhxw.ui.model.Thumbnail;
import com.cplatform.xhxw.ui.model.Today;
import com.cplatform.xhxw.ui.ui.base.BaseAdapter;
import com.cplatform.xhxw.ui.ui.base.BaseFragment;
import com.cplatform.xhxw.ui.ui.base.adapter.NewsVideoAdapter;
import com.cplatform.xhxw.ui.ui.base.view.GridViewToday;
import com.cplatform.xhxw.ui.ui.base.view.HomeOperationsBar;
import com.cplatform.xhxw.ui.ui.base.view.NewItem;
import com.cplatform.xhxw.ui.ui.base.view.NewsAdvertiseItem;
import com.cplatform.xhxw.ui.ui.base.view.NewsAudioItem;
import com.cplatform.xhxw.ui.ui.base.view.NewsBroadcastItem;
import com.cplatform.xhxw.ui.ui.base.view.NewsFunctionRecommend;
import com.cplatform.xhxw.ui.ui.base.view.NewsFunctionRecommend.OnFunctionRecommendOnClickListener;
import com.cplatform.xhxw.ui.ui.base.view.NewsHeaderView;
import com.cplatform.xhxw.ui.ui.base.view.NewsMultiHorizontalItem;
import com.cplatform.xhxw.ui.ui.base.view.NewsMultiHorizontalItem.OnMultiImgOnClickListener;
import com.cplatform.xhxw.ui.ui.base.view.NewsSmallVideoItem;
import com.cplatform.xhxw.ui.ui.base.view.NewsVideoItem;
import com.cplatform.xhxw.ui.ui.base.view.NewsVideoItem.onItemVideoPlayLisenter;
import com.cplatform.xhxw.ui.ui.base.view.OnSliderImgOnClickListener;
import com.cplatform.xhxw.ui.ui.base.view.PhotoSingleItem;
import com.cplatform.xhxw.ui.ui.base.view.VideoNewItem;
import com.cplatform.xhxw.ui.ui.base.view.VideoSingleItem;
import com.cplatform.xhxw.ui.ui.base.widget.DefaultView;
import com.cplatform.xhxw.ui.ui.base.widget.PullRefreshListView;
import com.cplatform.xhxw.ui.ui.detailpage.NewsPageActivity;
import com.cplatform.xhxw.ui.ui.detailpage.VideoPlayerActivity;
import com.cplatform.xhxw.ui.ui.main.HomeActivity;
import com.cplatform.xhxw.ui.ui.main.cms.game.GameUtil;
import com.cplatform.xhxw.ui.ui.main.cms.video.Player;
import com.cplatform.xhxw.ui.ui.picShow.PicShowActivity;
import com.cplatform.xhxw.ui.ui.specialTopic.SpecialTopicActivity;
import com.cplatform.xhxw.ui.ui.web.WebViewActivity;
import com.cplatform.xhxw.ui.ui.web.newscollect.NewsCollectWebActivity;
import com.cplatform.xhxw.ui.util.Actions;
import com.cplatform.xhxw.ui.util.AppBrightnessManager;
import com.cplatform.xhxw.ui.util.CommonUtils;
import com.cplatform.xhxw.ui.util.DateUtil;
import com.cplatform.xhxw.ui.util.DisplayImageOptionsUtil;
import com.cplatform.xhxw.ui.util.ListUtil;
import com.cplatform.xhxw.ui.util.LogUtil;
import com.cplatform.xhxw.ui.util.NetUtils;
import com.cplatform.xhxw.ui.util.ScreenUtil;
import com.cplatform.xhxw.ui.util.ShareUtil;
import com.google.gson.Gson;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.umeng.analytics.MobclickAgent;
import com.wbtech.ums.UmsAgent;

/**
 * 主页新闻 Created by cy-love on 13-12-25.
 */
public class NewsFragment extends BaseFragment implements OnItemClickListener,
		OnMultiImgOnClickListener, OnFunctionRecommendOnClickListener,
		OnSliderImgOnClickListener, PullRefreshListView.PullRefreshListener,
		onItemVideoPlayLisenter {

	private static final String TAG = NewsFragment.class.getSimpleName();
	@Bind(R.id.list)
	PullRefreshListView mListView;
	@Bind(R.id.def_view)
	DefaultView mDefView;
	@Bind(R.id.ly_root_content)
	View rootView;
	private NewsAdapter mAdapter;
	// 新闻视频中的GridView适配器
	private NewsVideoAdapter newVideoAdapter;
	private AsyncHttpResponseHandler mLoadHandler;
	private String mChannelid = ""; // 当前类型
	private NewsHeaderView mHeandr;
	private Receiver mBro;
	private boolean isSAASNews;
	private String mKeyword = null;
	private String mLastPublished = null;// 最后一条时间戳
	private LinearLayout llNewVideoGrid;
	private boolean isLoadMore = false;
	private DataReceiver receiverGame;

	private Activity activity;
	private int screenWidth;
	private int screenHeight;
	private Window window;
	private TextView title_layout;
	private RelativeLayout textureview_layout;
	private ImageView video_close;
	private RelativeLayout video_function_layout;
	private AbsoluteLayout right_corner_layout;
	private RelativeLayout content_layout;
	private ImageView video_full;
	private OnTouchListener touchLisFalse;
	private OnTouchListener touchLisTrue;
	private Player player;
	private int viewHeight = 0;
	private int viewWidth = 0;
	private int cornerViewHeight = 0;
	private int cornerViewWidth = 0;
	private int listY = 0;
	private AbsoluteLayout.LayoutParams old_lp;
	public static final int FLAG_TITLE = WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS;
	public static final int FLAG_NO_TITLE = WindowManager.LayoutParams.FLAG_FULLSCREEN;

	private int currentPosition = -1;
	private boolean isFull = false;
	private boolean isFullPlay = false;

	private boolean isVideo = false;
	private ProgressBar video_corner_progressbar;

	public NewsFragment() {
	}

	public NewsFragment(boolean isVideo) {
		this.isVideo = isVideo;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_news, container, false);
		ButterKnife.bind(this, view);

		activity = getActivity();
		screenWidth = ScreenUtil.getScreenWidth(activity);
		screenHeight = ScreenUtil.getScreenHeight(activity);
		window = activity.getWindow();
		title_layout = (TextView) view.findViewById(R.id.video_title);
		if (isVideo) {
			title_layout.setVisibility(View.VISIBLE);
		}
		textureview_layout = (RelativeLayout) view
				.findViewById(R.id.textureview_layout);
		video_close = (ImageView) view.findViewById(R.id.video_close_img);
		video_close.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (player != null) {
					player.stop();
				}
				if (isFullPlay) {
					showFullVideo(true);
				}
			}
		});
		video_function_layout = (RelativeLayout) view
				.findViewById(R.id.video_function_layout);
		video_corner_progressbar = (ProgressBar) view
				.findViewById(R.id.video_corner_progressbar);
		right_corner_layout = (AbsoluteLayout) view
				.findViewById(R.id.root_layout);
		content_layout = (RelativeLayout) view
				.findViewById(R.id.content_layout);
		video_full = (ImageView) view.findViewById(R.id.video_full);
		video_full.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				showFullVideo(true);
			}
		});
		touchLisFalse = new OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				switch (event.getAction()) {
				case MotionEvent.ACTION_DOWN:
					changeFunctionLayoutShow();
					break;
				case MotionEvent.ACTION_MOVE:
					return false;
				case MotionEvent.ACTION_UP:

					break;
				}
				return false;
			}
		};
		touchLisTrue = new OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				switch (event.getAction()) {
				case MotionEvent.ACTION_DOWN:
					LayoutParams lp = new LayoutParams(viewWidth, viewHeight,
							0, 0);
					textureview_layout.setLayoutParams(lp);
					mListView.setSelection(player.currentIndex
							+ mListView.getHeaderViewsCount());
					break;
				case MotionEvent.ACTION_MOVE:
					return true;
				case MotionEvent.ACTION_UP:
					break;
				}
				return true;
			}
		};
		textureview_layout.setOnTouchListener(touchLisFalse);
		return view;
	}

	public void showFullVideo(boolean isChangeFull) {
		if (!isFullPlay) {
			HomeActivity a = (HomeActivity) activity;
			a.showBottomBar(false);
			a.setTabbarShow(false);
			isFull = true;
			setFullPlay(true);
			title_layout.setVisibility(View.GONE);
			LayoutParams lp = new LayoutParams(screenHeight, screenWidth, 0, 0);
			video_function_layout.setVisibility(View.INVISIBLE);
			// 设置当前窗体为全屏显示
			WindowManager.LayoutParams lp2 = window.getAttributes();
			lp2.flags |= WindowManager.LayoutParams.FLAG_FULLSCREEN;
			window.setAttributes(lp2);
			window.addFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
			activity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
			right_corner_layout.updateViewLayout(textureview_layout, lp);
		} else {
			if (!isChangeFull) {
				changeFunctionLayoutShow();
			} else {
				HomeActivity a = (HomeActivity) activity;
				a.showBottomBar(true);
				a.setTabbarShow(true);
				setFullPlay(false);
				isFull = false;
				if (isVideo) {
					title_layout.setVisibility(View.VISIBLE);
				}
				video_function_layout.setVisibility(View.INVISIBLE);
				// 设置当前窗体为普通模式
				WindowManager.LayoutParams attr = window.getAttributes();
				attr.flags &= (~WindowManager.LayoutParams.FLAG_FULLSCREEN);
				window.setAttributes(attr);
				window.clearFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
				activity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
				right_corner_layout
						.updateViewLayout(textureview_layout, old_lp);
			}
		}
	}

	public void changeFunctionLayoutShow() {
		if (video_function_layout.getVisibility() == View.INVISIBLE) {
			video_function_layout.setVisibility(View.VISIBLE);
		} else {
			video_function_layout.setVisibility(View.INVISIBLE);
		}
	}

	public void initListY() {
		if (content_layout != null) {
			int[] location = new int[2];
			content_layout.getLocationOnScreen(location);// 获取在整个屏幕内的绝对坐标
			listY = location[1];
		}
	}

	public void changeVideoPosition(boolean isCorner, int width, int height,
			int x, int y) {
		if (player.isCorner != isCorner) {
			if (isCorner) {
				textureview_layout.setOnTouchListener(touchLisTrue);
			} else {
				textureview_layout.setOnTouchListener(touchLisFalse);
			}
		}
		player.isCorner = isCorner;
		if (isCorner) {
			int layout_height = right_corner_layout.getHeight();
			int layout_width = right_corner_layout.getWidth();
			LayoutParams lp = new LayoutParams(cornerViewWidth,
					cornerViewHeight, layout_width - cornerViewWidth,
					layout_height - cornerViewHeight);
			old_lp = lp;
			video_function_layout.setVisibility(View.INVISIBLE);
			video_corner_progressbar.setVisibility(View.VISIBLE);
			right_corner_layout.updateViewLayout(textureview_layout, lp);
		} else {
			LayoutParams lp = new LayoutParams(viewWidth, viewHeight, x, y);
			old_lp = lp;
			video_corner_progressbar.setVisibility(View.INVISIBLE);
			right_corner_layout.updateViewLayout(textureview_layout, lp);

		}
	}

	@Override
	public void onPause() {
		pause();
		super.onPause();
	}

	public void pause() {
		player.pause();
	}

	@Override
	public void onStop() {
		player.destroy();
		super.onStop();
	}

	public boolean isFullPlay() {
		return isFullPlay;
	}

	public void setFullPlay(boolean isFullPlay) {
		this.isFullPlay = isFullPlay;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		if (!isVideo) {
			mChannelid = getArguments().getString("channelid");

			if (getArguments().containsKey("channel_keyword")) {
				mKeyword = getArguments().getString("channel_keyword");
			}
		}
		mHeandr = new NewsHeaderView(getActivity());
		mListView.addHeaderView(mHeandr);

		// 判断是否为汽车频道
		if (mChannelid.equals("41")) {
			NewsFunctionRecommend vFunctionRecommend = new NewsFunctionRecommend(
					this.getActivity());
			vFunctionRecommend.setOnFunctionRecommendOnClickListener(this);
			for (int i = 0; i <= 3; i++) {
				FunctionRecommend item = new FunctionRecommend();
				item.setIndex(i);
				item.setTitle(Constants.aTitle[i]);
				item.setImgId(Constants.aImg[i]);
				item.setDetailUrl(Constants.aUrl[i]);
				vFunctionRecommend.setData(item, true, true);
			}
			// mHeandr.addView(vFunctionRecommend);
			mListView.addHeaderView(vFunctionRecommend);
		}

		mAdapter = new NewsAdapter(mAct, NewsFragment.this, this);
		mListView.setAdapter(mAdapter);
		player = new Player();
		mListView.setOnItemClickListener(this);
		mListView.setPullRefreshListener(this);
		mListView.setCanLoadMore(false);
		mListView.setCanRefresh(true);
		mDefView.setHidenOtherView(mListView);

		mListView.setOnScrollListener(new OnScrollListener() {

			@Override
			public void onScrollStateChanged(AbsListView view, int scrollState) {
			}

			@Override
			public void onScroll(AbsListView view, int firstVisibleItem,
					int visibleItemCount, int totalItemCount) {
				int currentIndex = player.currentIndex;
				boolean isIN = false;
				if (!player.isCorner && currentIndex > -1) {
					for (int size = view.getChildCount(), i = 0; i < size; i++) {
						int position = view.getPositionForView(view
								.getChildAt(i))
								- mListView.getHeaderViewsCount();
						if (position != AdapterView.INVALID_POSITION
								&& position == currentIndex) {
							isIN = true;
							currentPosition = i;
							break;
						}
					}
					if (!isIN) {
						changeVideoPosition(true, 0, 0, 0, 0);
						player.isPaused = true;
						player.isPlaying = false;
						return;
					}
					if (!isFull) {
						VideoNewItem layout = null;
						view.setVisibility(View.VISIBLE);
						View item = view.getChildAt(currentPosition);
						if (item != null
								&& (item instanceof NewsHeaderView || item instanceof LinearLayout)) {
							item = view.getChildAt(currentPosition
									+ mListView.getHeaderViewsCount());
						}
						if (item != null && item instanceof VideoNewItem) {
							layout = (VideoNewItem) item;
						}
						// RelativeLayout layout = (RelativeLayout) view
						// .getChildAt(currentPosition);
						if (layout != null) {
							// View v = layout.getChildAt(0);
							View v = layout.videoImage;
							// if (v != null) {
							// RelativeLayout layout2 = (RelativeLayout) v;
							// v = layout2.getChildAt(0);
							// }
							if (v != null) {
								int[] location = new int[2];
								v.getLocationOnScreen(location);// 获取在整个屏幕内的绝对坐标
								int x = location[0];
								int y = location[1];
								initListY();
								changeVideoPosition(false, viewWidth,
										viewHeight, x, y - listY);
								return;
							}
						}
					} else {
						view.setVisibility(View.INVISIBLE);
						LayoutParams lp = new LayoutParams(screenHeight,
								screenWidth, 0, 0);
						right_corner_layout.updateViewLayout(
								textureview_layout, lp);
						return;
					}
				}

				if ((currentIndex < firstVisibleItem
						- mListView.getHeaderViewsCount() || currentIndex > mListView
						.getLastVisiblePosition()) && player.isPlaying) {
					changeVideoPosition(true, 0, 0, 0, 0);
					player.isPaused = true;
					player.isPlaying = false;
				}
			}
		});

		mDefView.setListener(new DefaultView.OnTapListener() {
			@Override
			public void onTapAction() {
				mDefView.setStatus(DefaultView.Status.loading);
				loadData();
			}
		});
		mDefView.setStatus(DefaultView.Status.loading);
		mHeandr.setOnTouchListener(new OnTouchListener() {

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
						Log.d("zxe_" + this.getClass().getName() + "滑动时间",
								"向上滑");
					} else if (y2 - y1 > 50) {
						Log.d("zxe_" + this.getClass().getName() + "滑动时间",
								"向下滑");
					} else if (x1 - x2 > 50) {
						Log.d("zxe_" + this.getClass().getName() + "滑动时间",
								"向左滑");
					} else if (x2 - x1 > 50) {
						Log.d("zxe_" + this.getClass().getName() + "滑动时间",
								"向右滑");
					}
				}
				return true;
			}
		});
		// 加载缓存数据
		new AsyncTask<Void, Void, Void>() {

			private NewsCashDao dao_;
			private NetUtils.Status networkState_;
			private BaseResponse response_;
			List<New> news_;
			List<Focus> focus_;
			List<Today> today_;
			Thumbnail thum_;

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
					NewsListResponse res = (NewsListResponse) response_;
					if (res.getData() != null) {
						NewsListResponse.Conetnt conetnt = res.getData();
						focus_ = conetnt.getFocus();
						today_ = conetnt.getToday();
						thum_ = conetnt.getThumbnail();
						news_ = new ArrayList<New>();
						New adNew = generateAnAdverNews(conetnt.getAd());
						if (adNew != null) {
							news_.add(adNew);
						}

						List<Other> others = conetnt.getOther();
						if (!ListUtil.isEmpty(others)) {
							for (Other item : others) {
								news_.add(item.getNew());
							}
						}
						List<New> news = conetnt.getList();
						if (!ListUtil.isEmpty(news)) {
							news_.addAll(news);
						}
					}
				}
				if (!ListUtil.isEmpty(news_)) {
					for (New item : news_) {
						ReadNewsDao dao = ReadNewsDB.getReadNewsByNewsId(mAct,
								item.getNewsId());
						if (dao != null) {
							item.setRead(true);
						}
					}
				}
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
							NewsListResponse.class);
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
						updateAdapterData(news_, today_, focus_, thum_, null);
						mListView.setRefreshTime(new Date(dao_.getSaveTime()));
					}
					if (ListUtil.isEmpty(mAdapter.getData())) {
						if (networkState_ == NetUtils.Status.NONE) {
							mDefView.setStatus(DefaultView.Status.error);
						} else {
							// Log.d("aaa",
							// "doin__cacheChannelId="+mChannelid+"  "+"DefaultView.Status.loading");
							mDefView.setStatus(DefaultView.Status.loading);
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

	@Override
	public void onResume() {
		if (mBro != null) {
			LocalBroadcastManager.getInstance(mAct).unregisterReceiver(mBro);
		}
		receiverGame = new DataReceiver();
		IntentFilter filterGame = new IntentFilter(GameUtil.ACTION_GAME);
		mAct.registerReceiver(receiverGame, filterGame);

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
		mAct.unregisterReceiver(receiverGame);
		App.channel_id = null;
		App.news_id = null;
		activity = null;
		super.onDestroy();
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
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
		New item = mAdapter.getItem(curPos);
		UmsAgent.onEvent(getActivity(), StatisticalKey.news_details,
				new String[] { StatisticalKey.key_channelid,
						StatisticalKey.key_newsid, StatisticalKey.key_title },
				new String[] { mChannelid, item.getNewsId(), item.getTitle() });
		startActivity(mAdapter.getItem(curPos));
	}

	private int getPosition(int position) {
		return mHeandr == null ? position - 1 : position - 2;
	}

	private void startActivity(New item) {
		startActivity(item, 0);
	}

	/**
	 * 跳转Activity
	 * 
	 * @param item
	 * @param picIndex
	 *            跨栏图片索引
	 */
	private void startActivity(New item, int picIndex) {
		item.setRead(true);
		mAdapter.notifyDataSetChanged();
		if (TextUtils.isEmpty(item.getNewsId())) {
			showToast("新闻id为空");
			return;
		}
		App.channel_id = mChannelid;
		App.news_id = item.getNewsId();
		switch (item.getNewsType()) {
		case NewsType.SPECIAL_TOPIC:
			startActivity(SpecialTopicActivity.getIntent(mAct,
					item.getNewsId(), mChannelid));
			ReadNewsDB.saveNews(mAct, new ReadNewsDao(item.getNewsId(),
					DateUtil.getTime()));
			break;
		case NewsType.ATLAS: {
			PicShow picShow = new PicShow(new String());
			picShow.setCommentCount(item.getCommentcount());
			picShow.setNewsId(item.getNewsId());
			picShow.setTitle(item.getTitle());
			picShow.setPics(item.getPicInfo());
			startActivity(PicShowActivity.getIntent(mAct, picShow, picIndex,
					item.getNewsId(), true, HomeActivity.mIsUnderEnterprise,
					item.getTitle()));
		}
			break;
		case NewsType.LIVE:
			startActivity(WebViewActivity.getLiveNewsIntent(mAct,
					item.getNewsId(), item.getTitle(), true));
			break;
		case NewsType.COLLECT:
			startActivity(NewsCollectWebActivity.getCollectIntent(mAct,
					item.getNewsId(), item.getTitle(), true));
			break;
		case NewsType.DRAW_PRIZE:
			startActivity(WebViewActivity.getDrawPrizeIntent(mAct, null,
					item.getNewsId()));
			break;
		case NewsType.SPREAD:
			UmsAgent.onEvent(mAct, StatisticalKey.xwad_banner, new String[] {
					StatisticalKey.key_newsid, StatisticalKey.key_title },
					new String[] { item.getNewsId(), item.getTitle() });
			startActivity(WebViewActivity.getIntentByURL(mAct,
					item.getNewsUrl(), item.getTitle()));
			break;
		default:
			if (item.getNewsType() == NewsType.VIDEO
					&& item.getShowType() == ShowType.BIG_VIDEO) {
				startActivity(VideoPlayerActivity.getIntent(mAct,
						item.getNewsId(), item.getVideourl()));
			} else if (item.getNewsType() == NewsType.AUDIO
					&& item.getShowType() == ShowType.BROADCAST_AUDIO) {
				HomeActivity home = (HomeActivity) getActivity();
				home.setBroadcastArguments(item);
				home.switchFragmentInMainAcitivy(HomeOperationsBar.BOTTOM_BUTTON_TYPE_BROADCAST);
			} else if (item.getShowType() == ShowType.LIST_BIG_VIDEO) {
				// startActivity(NewsPageActivity.getIntent(mAct,
				// item.getNewsId(), HomeActivity.mIsUnderEnterprise));
			} else {
				startActivity(NewsPageActivity.getIntent(mAct,
						item.getNewsId(), HomeActivity.mIsUnderEnterprise));
			}
			break;
		}
	}

	private void loadData() {
		mLastPublished = null;
		loadData(-1);
	}

	private void loadData(int typeId) {
		// Log.d("aaa", "doin__cacheChannelId="+mChannelid+"  "+"loadData()");
		NewsListRequest request = mLastPublished == null ? new NewsListRequest(
				mChannelid, "" + 0) : new NewsListRequest(mChannelid + "",
				mLastPublished);

		if (mLastPublished == null) {
			request.setType(1);
		}

		if (isSAASNews) {
			request.setSaasRequest(true);
			request.setTypeId(typeId);
		}
		if (mKeyword != null) {
			request.setKeyword(mKeyword);
			APIClient.getSearchedChannelNewsList(request, newsListHandler);
		} else if (isVideo) {
			APIClient.videoList(request, newsListHandler);
		} else {
			APIClient.newsList(request, newsListHandler);
		}
	}

	AsyncHttpResponseHandler newsListHandler = new AsyncHttpResponseHandler() {

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
			if (mAdapter == null || mAdapter.getCount() == 0) {
				if (mDefView != null) {
					mDefView.setStatus(DefaultView.Status.error);
				}
			} else {
				showToast(R.string.load_server_failure);
			}
			mListView.onRefreshComplete(null);
			isLoadMore = false;
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

				List<New> news_;
				List<Today> today_;
				List<Focus> focus_;
				Thumbnail thum_;
				BaseResponse response_;
				private AsyncHttpResponseHandler handler_;

				@Override
				protected Boolean doInBackground(
						AsyncHttpResponseHandler... params) {
					// Log.d("aaa",
					// "doin__cacheChannelId="+mChannelid+"  "+"doInBackground");
					if (params != null && params.length > 0) {
						handler_ = params[0];
					}
					for (AsyncHttpResponseHandler p : params) {
						p = null;
					}
					try {
						ResponseUtil.checkResponse(content);
						if (TextUtils.isEmpty(mLastPublished)) {
							response_ = new Gson().fromJson(content,
									NewsListResponse.class);
						} else {
							response_ = new Gson().fromJson(content,
									NewsListNewsResponse.class);
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
						if (response_ instanceof NewsListResponse) {
							NewsListResponse res = (NewsListResponse) response_;
							if (res.getData() != null) {
								NewsListResponse.Conetnt conetnt = res
										.getData();
								// Log.d("aaa",
								// "doin__cacheChannelId="+mChannelid+"  "+"response_ instanceof NewsListResponse");
								focus_ = conetnt.getFocus();
								today_ = conetnt.getToday();
								thum_ = conetnt.getThumbnail();
								news_ = new ArrayList<New>();
								New adNew = generateAnAdverNews(conetnt.getAd());
								if (adNew != null) {
									news_.add(adNew);
								}

								List<Other> others = conetnt.getOther();
								if (!ListUtil.isEmpty(others)) {
									for (Other item : others) {
										news_.add(item.getNew());
									}
								}
								List<New> news = conetnt.getList();
								if (!ListUtil.isEmpty(news)) {
									news_.addAll(news);
								}
							}
						} else {
							NewsListNewsResponse res = (NewsListNewsResponse) response_;
							news_ = res.getData();
						}
						if (!ListUtil.isEmpty(news_)) {
							for (New item : news_) {
								ReadNewsDao dao = ReadNewsDB
										.getReadNewsByNewsId(
												mAct.getApplicationContext(),
												item.getNewsId());
								if (dao != null) {
									item.setRead(true);
								}
							}
						}
					}
					Log.d("aaa", "doin__cacheChannelId=" + mChannelid + "  "
							+ "doInBackground  return true");
					return true;
				}

				@Override
				protected void onPostExecute(Boolean result) {
					// Log.d("aaa",
					// "doin__cacheChannelId="+mChannelid+"  "+"onPostExecute");
					if (handler_.isCancled() || handler_ != mLoadHandler
							|| mLoadHandler == null) {
						return;
					}
					if (!result) {
						if (mAdapter == null || mAdapter.getCount() == 0) {
							if (mDefView != null) {
								mDefView.setStatus(DefaultView.Status.error);
							}
						}
						showToast(R.string.data_format_error);
						mListView.onRefreshComplete(null);
						isLoadMore = false;
						mListView.onLoadMoreComplete();
						return;
					}
					if (response_ != null && response_.isSuccess()) {
						if (TextUtils.isEmpty(mLastPublished)) {
							mListView.setRefreshTime(new Date());
						}
						updateAdapterData(news_, today_, focus_, thum_,
								mLastPublished);
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
						isLoadMore = false;
						mListView.onLoadMoreComplete();
					}
					if (news_ != null) {
						news_.clear();
					}

					if (today_ != null) {
						today_.clear();
					}
					if (focus_ != null) {
						focus_.clear();
					}
					thum_ = null;
					response_ = null;
					handler_ = null;
					// mLoadHandler = null;
				}
			}.execute(this);

		}
	};

	/**
	 * 
	 * @Name setNewVideoList
	 * @Description TODO 设置今日推荐列表，目前只有新华视频中有
	 * @param today_
	 * @return void
	 * @Author zxe
	 * @Date 2015年7月29日 下午5:48:58
	 *
	 */
	private void setNewVideoList(List<Today> today_) {
		if (llNewVideoGrid != null) {
			llNewVideoGrid.removeAllViews();
		}
		llNewVideoGrid = new LinearLayout(this.getActivity());
		llNewVideoGrid.setOrientation(LinearLayout.VERTICAL);
		View mLayout = LayoutInflater.from(this.getActivity()).inflate(
				R.layout.view_newvideo_splittitle, null);
		((TextView) mLayout.findViewById(R.id.tv_title)).setText("今日推荐");
		llNewVideoGrid.addView(mLayout, LinearLayout.LayoutParams.MATCH_PARENT,
				LinearLayout.LayoutParams.WRAP_CONTENT);
		GridViewToday gvNewVideo = new GridViewToday(this.getActivity());
		gvNewVideo.setNumColumns(2);
		gvNewVideo.setScrollContainer(false);
		gvNewVideo.setGravity(Gravity.CENTER);
		gvNewVideo.setStretchMode(GridView.STRETCH_COLUMN_WIDTH);

		newVideoAdapter = new NewsVideoAdapter(mAct, NewsFragment.this);
		gvNewVideo.setAdapter(newVideoAdapter);
		newVideoAdapter.addAllData(today_);
		gvNewVideo.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				if (CommonUtils.isFastDoubleClick()) {
					return;
				}
				Today item = newVideoAdapter.getItem(position);
				item.setRead(true);
				UmsAgent.onEvent(getActivity(), StatisticalKey.news_details,
						new String[] { StatisticalKey.key_channelid,
								StatisticalKey.key_newsid,
								StatisticalKey.key_title }, new String[] {
								mChannelid, item.getNewsId(), item.getTitle() });
				startActivity(NewsPageActivity.getIntent(mAct,
						item.getNewsId(), HomeActivity.mIsUnderEnterprise));
			}
		});
		llNewVideoGrid.addView(gvNewVideo,
				LinearLayout.LayoutParams.WRAP_CONTENT,
				LinearLayout.LayoutParams.WRAP_CONTENT);
		View mLayoutBottom = LayoutInflater.from(this.getActivity()).inflate(
				R.layout.view_newvideo_splittitle_bottom, null);
		llNewVideoGrid.addView(mLayoutBottom);
		mListView.addHeaderView(llNewVideoGrid);
	}

	/**
	 * 更新数据
	 * 
	 * @param published
	 */
	private void updateAdapterData(List<New> news_, List<Today> today_,
			List<Focus> focus_, Thumbnail thum_, String published) {
		if (isDestroyView) {
			return;
		}
		if (!ListUtil.isEmpty(focus_)) {
			mHeandr.setSlider(focus_, this);
		} else if (null != thum_ && !TextUtils.isEmpty(thum_.getUrl())) {
			Log.e("NewHeaderViewImage", thum_.getUrl());
			mHeandr.setNewsThumbnail(thum_.getUrl());
		}
		if (mChannelid.equals("47") && !ListUtil.isEmpty(today_)) {
			setNewVideoList(today_);
		}
		if (TextUtils.isEmpty(published)) {
			mAdapter.clearData();
		}
		if (!ListUtil.isEmpty(news_)) {
			// 判断当前频道是否为新闻视频
			mAdapter.addAllData(news_);
			mListView.setCanLoadMore(true);
		} else {
			mListView.setCanLoadMore(false);
		}

		mAdapter.notifyDataSetChanged();
		if ((mAdapter.getData() == null || mAdapter.getData().size() < 1)) {
			if (HomeFragment.currentShowChannel != null
					&& HomeFragment.currentShowChannel.equals(mChannelid)) {
				mAct.showToast("很抱歉，无相关内容");
			}
		} else {
			// aaa
			// UmsAgent.onEvent(getActivity(), "", null,
			// mChannelid, mAdapter.getCount() + "");
		}
		mDefView.setStatus(DefaultView.Status.showData);
	}

	/**
	 * 跨栏图片新闻点击事件
	 * 
	 * @param v
	 * @param item
	 *            视图对应的源数据
	 * @param index
	 *            点击的图片位置 分别为 0、1、2
	 */
	@Override
	public void onMultiImgOnClick(View v, New item, int index) {
		if (CommonUtils.isFastDoubleClick()) {
			return;
		}
		startActivity(item, index);
	}

	/**
	 * 推荐新闻点击
	 * 
	 * @param focus
	 *            推荐
	 */
	@Override
	public void onSliderImgOnClick(Focus focus) {
		if (CommonUtils.isFastDoubleClick()) {
			return;
		}
		switch (focus.getNewsType()) {
		case NewsType.SPECIAL_TOPIC:
			startActivity(SpecialTopicActivity.getIntent(mAct,
					focus.getNewsId(), mChannelid));
			break;
		case NewsType.ATLAS: {
			PicShow picShow = new PicShow(new String());
			picShow.setCommentCount(focus.getCommentcount());
			picShow.setNewsId(focus.getNewsId());
			picShow.setTitle(focus.getTitle());
			picShow.setPics(focus.getPicInfo());
			startActivity(PicShowActivity.getIntent(mAct, picShow, 0,
					focus.getNewsId(), true, HomeActivity.mIsUnderEnterprise,
					focus.getTitle()));
		}
			break;
		case NewsType.ADVERTISEMENT:
		case NewsType.SPREAD:
			UmsAgent.onEvent(mAct, StatisticalKey.xwad_slide, new String[] {
					StatisticalKey.key_newsid, StatisticalKey.key_title },
					new String[] { focus.getNewsId(), focus.getTitle() });
			startActivity(WebViewActivity.getIntentByURL(mAct,
					focus.getAdurl(), focus.getTitle()));
			break;
		case NewsType.LIVE:
			startActivity(WebViewActivity.getIntentByURL(mAct,
					focus.getLiveurl(), focus.getTitle()));
			break;
		case NewsType.COLLECT:
			startActivity(NewsCollectWebActivity.getCollectIntent(mAct,
					focus.getNewsId(), focus.getTitle(), true));
			break;
		case NewsType.DRAW_PRIZE:
			startActivity(WebViewActivity.getDrawPrizeIntent(mAct, null,
					focus.getNewsId()));
			break;
		default:
			if (focus.getNewsType() == NewsType.VIDEO
					&& focus.getShowType() == ShowType.BIG_VIDEO) {
				startActivity(VideoPlayerActivity.getIntent(mAct,
						focus.getNewsId(), focus.getVideourl()));
			} else {
				startActivity(NewsPageActivity.getIntent(mAct,
						focus.getNewsId(), HomeActivity.mIsUnderEnterprise));
			}

			break;
		}
	}

	@Override
	public void onDestroyView() {
		if (null != mLoadHandler) {
			mLoadHandler.cancle();
			mLoadHandler = null;
		}
		mListView = null;
		mAdapter.clearData();
		mAdapter.close();
		mAdapter = null;

//		ButterKnife.reset(this);
		super.onDestroyView();
	}

	@Override
	public void onRefresh() {

		UmsAgent.onEvent(getActivity(), StatisticalKey.channel_update,
				new String[] { StatisticalKey.key_channelid,
						StatisticalKey.key_type }, new String[] { mChannelid,
						StatisticalKey.pulldown });

		MobclickAgent.onEvent(getActivity(), StatisticalKey.channel_update);
		Player.release();
		loadData();
	}

	@Override
	public void onLoadMore() {
		isLoadMore = true;
		if (mAdapter != null) {
			List<New> list = mAdapter.getData();
			int size = list.size();
			if (size > 0) {
				New curNew = list.get(size - 1);
				mLastPublished = curNew.getPublished();
				loadData(curNew.getTypeId());
			}
		} else {
			isLoadMore = false;
			mListView.onLoadMoreComplete();
		}
		UmsAgent.onEvent(getActivity(), StatisticalKey.channel_update,
				new String[] { StatisticalKey.key_channelid,
						StatisticalKey.key_type }, new String[] { mChannelid,
						StatisticalKey.loadmore });
	}

	private class Receiver extends BroadcastReceiver {
		@Override
		public void onReceive(Context context, Intent intent) {
			String action = intent.getAction();
			// 显示模式改变监听
			if (!isDestroyView
					&& Actions.ACTION_DISPLAY_MODEL_CHANGE.equals(action)) {
				mAdapter.notifyDataSetChanged();
				AppBrightnessManager.setScreenBrightness(getActivity());
			} else if (!isDestroyView
					&& Actions.ACTION_CHANNE_RESELECTED.equals(action)
					&& mChannelid.equals(intent.getStringExtra("channelid"))) {
				reloadData();
			} else if (!isDestroyView
					&& Actions.ACTION_CHANNEL_RELOAD_CHECK_TIME.equals(action)
					&& mChannelid.equals(intent.getStringExtra("channelid"))
					&& isReloadNetData()) {
				reloadData();
			} else if (!isDestroyView
					&& Actions.ACTION_NEWSLIST_TEXTSIZE_CHANGE.equals(action)) {
				mAdapter.notifyDataSetChanged();
			}

		}
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

	/**
	 * 判断是否需要重新加载数据<\br> 如果十分钟内刷新过就不在刷新
	 */
	private boolean isReloadNetData() {
		return (DateUtil.getTime() - mListView.getOldRefreshTime()) > 180000;
	}

	public boolean isSAASNews() {
		return isSAASNews;
	}

	public void setSAASNews(boolean isSAASNews) {
		this.isSAASNews = isSAASNews;
	}

	/**
	 * 将新闻列表中的广告属性对应到一个新闻对象中，生成一个新闻对象，对应关系见方法内
	 * 
	 * @param content
	 * @return
	 */
	private New generateAnAdverNews(AdvertismentsResponse content) {
		if (content == null || content.getContent() == null) {
			return null;
		}
		List<Ad> ads = content.getContent();
		if (ads.size() <= 0) {
			return null;
		}
		Ad ad = ads.get(0);
		New tmp = new New();
		tmp.setNewsId(ad.getId());
		tmp.setNewsType(NewsType.ADVERTISEMENT_IN_LIST);
		tmp.setNewsUrl(ad.getUrl());
		tmp.setThumbnail(ad.getAndroidimg());
		tmp.setTitle(ad.getTitle());
		tmp.setShowType(ShowType.LIST_ADVER_BIG_IMG);
		return tmp;
	}

	/**
	 * 
	 * @Name onFunctionRecommendOnClick 汽车频道 推广功能
	 * @Description TODO
	 * @param v
	 * @param item
	 * @param index
	 * @see com.cplatform.xhxw.ui.ui.base.view.NewsFunctionRecommend.OnFunctionRecommendOnClickListener#onFunctionRecommendOnClick(android.view.View,
	 *      com.cplatform.xhxw.ui.model.New, int)
	 * @Date 2015年4月29日 下午3:22:28
	 * 
	 */
	@Override
	public void onFunctionRecommendOnClick(View v, FunctionRecommend item,
			int index) {
		// TODO Auto-generated method stub
		startActivity(WebViewActivity.getIntentByURL(mAct, item.getDetailUrl(),
				item.getTitle()));
	}

	private class DataReceiver extends BroadcastReceiver {// 继承自BroadcastReceiver的子类
		@Override
		public void onReceive(Context context, Intent intent) {// 重写onReceive方法
		}
	}

	@Override
	public void onPlayVideo(View v, New item) {
		// TODO Auto-generated method stub

	}

	/**
	 * 新闻列表适配器 Created by cy-love on 13-12-25.
	 */
	public class NewsAdapter extends BaseAdapter<New> {

		private OnMultiImgOnClickListener mMultClickLis;
		private onItemVideoPlayLisenter mOnVideoPlayLis;
		private Context con;

		public NewsAdapter(Context context,
				OnMultiImgOnClickListener multClickLis,
				onItemVideoPlayLisenter OnVideoPlayLisenter) {
			super(context);
			con = context;
			this.mMultClickLis = multClickLis;
			this.mOnVideoPlayLis = OnVideoPlayLisenter;
		}

		public NewsAdapter(Context context,
				OnMultiImgOnClickListener multClickLis,
				onItemVideoPlayLisenter OnVideoPlayLisenter, Player player) {
			super(context);
			con = context;
			this.mMultClickLis = multClickLis;
			this.mOnVideoPlayLis = OnVideoPlayLisenter;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			New item = getItem(position);
			int type = getItemViewType(position);
			if (convertView == null
					|| needCreateNewView((Integer) convertView.getTag(),
							item.getListStyle())) {
				switch (type) {
				case ShowType.IMAGE: {
					NewsMultiHorizontalItem view = new NewsMultiHorizontalItem(
							mContext);
					view.setOnMultiImgOnClickListener(mMultClickLis);
					convertView = view;
				}
					break;
				case ShowType.BIG_IMAGE: {
					convertView = new PhotoSingleItem(mContext);
				}
					break;
				case ShowType.BIG_VIDEO: {
					convertView = new VideoSingleItem(mContext);
				}
					break;
				case ShowType.AUDIO: {
					convertView = new NewsAudioItem(mContext);
				}
					break;
				case ShowType.SMALL_VIDEO: {
					convertView = new NewsSmallVideoItem(mContext);
				}
					break;
				case ShowType.LIST_BIG_VIDEO: {
					VideoNewItem view = new VideoNewItem(mContext, isVideo);
					// view.setOnItemVideoPlayLisenter(mOnVideoPlayLis);
					convertView = view;
				}
					break;
				case ShowType.BIG_IMG_NEWS: {
					convertView = new NewsVideoItem(mContext, false);
				}
					break;
				case ShowType.LIST_ADVER_BIG_IMG: {
					convertView = new NewsAdvertiseItem(mContext);
				}
					break;
				case ShowType.BROADCAST_AUDIO: {
					convertView = new NewsBroadcastItem(mContext);
				}
					break;
				default:
					if (item.getListStyle() == New.LIST_STYLE_PICS) {
						NewsMultiHorizontalItem view = new NewsMultiHorizontalItem(
								mContext);
						view.setOnMultiImgOnClickListener(mMultClickLis);
						convertView = view;
					} else {
						convertView = new NewItem(mContext);
					}
					break;
				}
				convertView.setTag(item.getListStyle());
			}
			switch (type) {
			case ShowType.IMAGE: {
				NewsMultiHorizontalItem view = (NewsMultiHorizontalItem) convertView;
				view.setData(item, isToShowDividerBg(position, true),
						isToShowDividerBg(position, false));
			}
				break;
			case ShowType.BIG_IMAGE: {
				PhotoSingleItem view = (PhotoSingleItem) convertView;
				view.setData(item);
			}
				break;
			case ShowType.BIG_VIDEO: {
				VideoSingleItem view = (VideoSingleItem) convertView;
				view.setData(item);
			}
				break;
			case ShowType.AUDIO: {
				NewsAudioItem view = (NewsAudioItem) convertView;
				view.setData(item);
			}
				break;
			case ShowType.SMALL_VIDEO: {
				NewsSmallVideoItem view = (NewsSmallVideoItem) convertView;
				view.setData(item);
			}
				break;
			case ShowType.LIST_ADVER_BIG_IMG: {
				NewsAdvertiseItem view = (NewsAdvertiseItem) convertView;
				view.setData(item);
			}
				break;
			case ShowType.LIST_BIG_VIDEO: {
				VideoNewItem view = (VideoNewItem) convertView;
				view.setData(item);
				initVideo(view, item, position);
				String imageThumb = item.getBigthumbnail();
				Object urlObject = view.videoImage.getTag();
				String imageUrl = "";
				if (urlObject != null) {
					imageUrl = (String) urlObject;
				}
				if (!imageUrl.equals(imageThumb)) {
					ImageLoader.getInstance().displayImage(imageThumb,
							view.videoImage,
							DisplayImageOptionsUtil.listNewImgOptions);
				} else if (imageThumb.equals("")) {
					ImageLoader.getInstance().displayImage(imageThumb,
							view.videoImage,
							DisplayImageOptionsUtil.listNewImgOptions);
				}
				view.videoImage.setTag(imageThumb);
			}
				break;
			case ShowType.BIG_IMG_NEWS: {
				NewsVideoItem view = (NewsVideoItem) convertView;
				view.setData(item);
			}
				break;
			case ShowType.BROADCAST_AUDIO: {
				NewsBroadcastItem view = (NewsBroadcastItem) convertView;
				view.setData(item);
			}
				break;
			default:
				if (item.getListStyle() == New.LIST_STYLE_PICS) {
					NewsMultiHorizontalItem view = (NewsMultiHorizontalItem) convertView;
					view.setData(item, isToShowDividerBg(position, true),
							isToShowDividerBg(position, false));
				} else {
					NewItem view = (NewItem) convertView;
					view.setData(item);
				}
				break;
			}
			return convertView;
		}

		@Override
		public int getItemViewType(int position) {
			int type = getItem(position).getShowType();
			return type > 13 ? 0 : type;
		}

		@Override
		public int getViewTypeCount() {
			return 13;
		}

		private boolean needCreateNewView(int olderViewType, int newViewTpe) {

			return (olderViewType == New.LIST_STYLE_PICS && newViewTpe != New.LIST_STYLE_PICS)
					|| (olderViewType != New.LIST_STYLE_PICS && newViewTpe == New.LIST_STYLE_PICS);
		}

		public void close() {
			clearData();
			mData = null;
			mContext = null;
			mInflater = null;
		}

		private boolean isToShowDividerBg(int position, boolean isTop) {
			if (isTop) {
				if (position == 0) {
					return true;
				} else {
					int type = getItemViewType(position - 1);
					if (type == ShowType.IMAGE
							|| getItem(position - 1).getListStyle() == New.LIST_STYLE_PICS) {
						return false;
					}
					return true;
				}
			} else {
				if (position == getCount() - 1) {
					return true;
				} else {
					int type = getItemViewType(position + 1);
					if (type == ShowType.IMAGE
							|| getItem(position + 1).getListStyle() == New.LIST_STYLE_PICS) {
						return false;
					}
					return true;
				}
			}
		}

		public void initVideo(final VideoNewItem holder, final New item,
				final int position) {
			// String imageThumb = lvd.get(position).getThumb();
			Object urlObject = holder.videoImage.getTag();
			String imageUrl = "";
			if (urlObject != null) {
				imageUrl = (String) urlObject;
			}
			// if (!imageUrl.equals(imageThumb)) {
			// ImageLoader.getInstance().displayImage(
			// lvd.get(position).getThumb(), holder.videoImage,
			// DisplayImageOptionsUtil.listNewImgOptions);
			// }
			// holder.videoImage.setTag(lvd.get(position).getThumb());

			// holder.videoNameText.setText(lvd.get(position).getName());
			holder.videoPlayBtn.setVisibility(View.VISIBLE);
			holder.videoImage.setVisibility(View.VISIBLE);
			holder.videoNameText.setVisibility(View.VISIBLE);
			if (player.currentIndex == position) {
				holder.videoPlayBtn.setVisibility(View.INVISIBLE);
				// holder.videoImage.setVisibility(View.INVISIBLE);
				holder.videoNameText.setVisibility(View.INVISIBLE);

				// if (player.isPlaying || player.playPosition == -1) {
				// if (player != null) {
				// holder.textureView.setVisibility(View.GONE);
				// player.stop();
				// holder.mSeekBar.setVisibility(View.GONE);
				// }
				// }
				if (player.playPosition > 0 && player.isPaused
						&& !player.isCorner) {
					// changeVideoPosition(true, 0, 0, 0, 0);
					// player.play(lvd.get(mPosition).getUrl());
					player.isPaused = false;
					player.isPlaying = true;
				} else {
					if (!isFull) {
						changeVideoPosition(false, viewWidth, viewHeight, 0, 0);
						// player.play("http://static.xw.feedss.com/uploadfile/videos/20150806/2554341be40646910911552ea494edab.mp4");
						player.initLayout(textureview_layout, mAdapter);
						player.play(item.getVideourl());
						player.isPaused = false;
						player.isPlaying = true;
					}
					// player.isCorner = false;
				}
			} else {
				// player.isShowCurrent = false;
				holder.videoPlayBtn.setVisibility(View.VISIBLE);
				holder.videoImage.setVisibility(View.VISIBLE);
				holder.videoNameText.setVisibility(View.VISIBLE);
			}

			holder.videoImage.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					player.currentIndex = position;
					player.playPosition = -1;
					viewHeight = holder.videoImage.getHeight();
					viewWidth = holder.videoImage.getWidth();
					cornerViewHeight = (int) (0.5 * viewHeight);
					cornerViewWidth = (int) (0.5 * viewWidth);
					initListY();
					textureview_layout.setVisibility(View.VISIBLE);
					LayoutParams lp = new LayoutParams(viewWidth, viewHeight,
							0, 0);
					textureview_layout.setLayoutParams(lp);
					mAdapter.notifyDataSetChanged();
				}
			});
			holder.videoShareBtn.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					ShareUtil.sendVideoTextIntent(activity, item.getTitle(),
							item.getSummary(), item.getNewsUrl(), "", true);
				}
			});
		}
	}

	public void stopPlayer() {
		if (player != null) {
			player.release();
		}
	}
}
