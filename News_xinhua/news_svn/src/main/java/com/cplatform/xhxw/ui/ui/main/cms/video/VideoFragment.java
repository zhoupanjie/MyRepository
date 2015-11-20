//package com.cplatform.xhxw.ui.ui.main.cms.video;
//
//import java.util.ArrayList;
//import java.util.List;
//
//import android.content.pm.ActivityInfo;
//import android.os.Bundle;
//import android.view.LayoutInflater;
//import android.view.MotionEvent;
//import android.view.View;
//import android.view.View.OnClickListener;
//import android.view.View.OnTouchListener;
//import android.view.ViewGroup;
//import android.view.Window;
//import android.view.WindowManager;
//import android.widget.AbsListView;
//import android.widget.AbsListView.OnScrollListener;
//import android.widget.AbsoluteLayout;
//import android.widget.AbsoluteLayout.LayoutParams;
//import android.widget.AdapterView;
//import android.widget.BaseAdapter;
//import android.widget.ImageView;
//import android.widget.LinearLayout;
//import android.widget.RelativeLayout;
//import android.widget.TextView;
//
//import com.cplatform.xhxw.ui.R;
//import com.cplatform.xhxw.ui.ui.base.BaseFragment;
//import com.cplatform.xhxw.ui.ui.base.view.NewsHeaderView;
//import com.cplatform.xhxw.ui.ui.base.widget.PullRefreshListView;
//import com.cplatform.xhxw.ui.ui.main.HomeActivity;
//import com.cplatform.xhxw.ui.util.DisplayImageOptionsUtil;
//import com.cplatform.xhxw.ui.util.ScreenUtil;
//import com.nostra13.universalimageloader.core.ImageLoader;
//
//public class VideoFragment extends BaseFragment implements
//		PullRefreshListView.PullRefreshListener {
//	private PullRefreshListView mListView;
//	private List<VideoData> lvd = new ArrayList<VideoData>();
//	private myAdapter adapter;
//	private Player player;
//	private int viewHeight = 0;
//	private int viewWidth = 0;
//	private int listY = 0;
//
//	private AbsoluteLayout.LayoutParams old_lp;
//
//	public static final int FLAG_TITLE = WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS;
//	public static final int FLAG_NO_TITLE = WindowManager.LayoutParams.FLAG_FULLSCREEN;
//
//	private AbsoluteLayout right_corner_layout;
//	private int currentPosition = -1;
//	private RelativeLayout textureview_layout;
//	// private SeekBar videoSeekbar;
//	private RelativeLayout video_function_layout;
//	private OnTouchListener touchLisFalse;
//	private OnTouchListener touchLisTrue;
//	private RelativeLayout content_layout;
//	private TextView title_layout;
//	private Window window;
//	private boolean isFull = false;
//	private boolean isFullPlay = false;
//	private ImageView video_full;
//
//	private int screenWidth = 0;
//	private int screenHeight = 0;
//
//	private HomeActivity activity;
//	private NewsHeaderView mHeandr;
//
//	@Override
//	public View onCreateView(LayoutInflater inflater, ViewGroup container,
//			Bundle savedInstanceState) {
//		View view = inflater.inflate(R.layout.fragment_video, container, false);
//		activity = (HomeActivity) getActivity();
//		setListData();
//		screenWidth = ScreenUtil.getScreenWidth(activity);
//		screenHeight = ScreenUtil.getScreenHeight(activity);
//		window = activity.getWindow();
//		title_layout = (TextView) view.findViewById(R.id.tt);
//		textureview_layout = (RelativeLayout) view
//				.findViewById(R.id.textureview_layout);
//		video_function_layout = (RelativeLayout) view
//				.findViewById(R.id.video_function_layout);
//		right_corner_layout = (AbsoluteLayout) view
//				.findViewById(R.id.root_layout);
//		content_layout = (RelativeLayout) view
//				.findViewById(R.id.content_layout);
//		video_full = (ImageView) view.findViewById(R.id.video_full);
//		video_full.setOnClickListener(new OnClickListener() {
//
//			@Override
//			public void onClick(View v) {
//				// TODO Auto-generated method stub
//				showFullVideo(true);
//			}
//		});
//		touchLisFalse = new OnTouchListener() {
//
//			@Override
//			public boolean onTouch(View v, MotionEvent event) {
//				switch (event.getAction()) {
//				case MotionEvent.ACTION_DOWN:
//					changeFunctionLayoutShow();
//					break;
//				case MotionEvent.ACTION_MOVE:
//					return false;
//				case MotionEvent.ACTION_UP:
//
//					break;
//				}
//				return false;
//			}
//		};
//		touchLisTrue = new OnTouchListener() {
//
//			@Override
//			public boolean onTouch(View v, MotionEvent event) {
//				switch (event.getAction()) {
//				case MotionEvent.ACTION_DOWN:
//					LayoutParams lp = new LayoutParams(viewWidth, viewHeight,
//							0, 0);
//					textureview_layout.setLayoutParams(lp);
//					mListView.setSelection(player.currentIndex + mListView.getHeaderViewsCount());
//					break;
//				case MotionEvent.ACTION_MOVE:
//					return true;
//				case MotionEvent.ACTION_UP:
//					break;
//				}
//				return true;
//			}
//		};
//		mListView = (PullRefreshListView) view
//				.findViewById(R.id.video_listview);
//		mHeandr = new NewsHeaderView(getActivity());
//		mListView.addHeaderView(mHeandr);
//		mListView.setCanLoadMore(false);
//		mListView.setCanRefresh(true);
//		mListView.setPullRefreshListener(this);
//		adapter = new myAdapter();
//		mListView.setAdapter(adapter);
//		player = new Player(textureview_layout, adapter);
//
//		mListView.setOnScrollListener(new OnScrollListener() {
//
//			@Override
//			public void onScrollStateChanged(AbsListView view, int scrollState) {
//			}
//
//			@Override
//			public void onScroll(AbsListView view, int firstVisibleItem,
//					int visibleItemCount, int totalItemCount) {
//				int currentIndex = player.currentIndex;
//				boolean isIN = false;
//				if (!player.isCorner && currentIndex > -1) {
//					for (int size = view.getChildCount(), i = 0; i < size; i++) {
//						int position = view.getPositionForView(view
//								.getChildAt(i)) - mListView.getHeaderViewsCount();
//						if (position != AdapterView.INVALID_POSITION
//								&& position == currentIndex) {
//							isIN = true;
//							currentPosition = i;
//							break;
//						}
//					}
//					if (!isIN) {
//						changeVideoPosition(true, 0, 0, 0, 0);
//						player.isPaused = true;
//						player.isPlaying = false;
//						return;
//					}
//					if (!isFull) {
//						RelativeLayout layout = null;
//						view.setVisibility(View.VISIBLE);
//						View item = view.getChildAt(currentPosition);
//						if (item != null
//								&& (item instanceof NewsHeaderView || item instanceof LinearLayout)) {
//							item = view.getChildAt(currentPosition + mListView.getHeaderViewsCount());
//						}
//						if (item != null) {
//							layout = (RelativeLayout) item;
//						}
//
//						// RelativeLayout layout = (RelativeLayout) view
//						// .getChildAt(currentPosition);
//						if (layout != null) {
//							View v = layout.getChildAt(0);
//							if (v != null) {
//								int[] location = new int[2];
//								v.getLocationOnScreen(location);// 获取在整个屏幕内的绝对坐标
//								int x = location[0];
//								int y = location[1];
//								initListY();
//								changeVideoPosition(false, viewWidth,
//										viewHeight, x, y - listY);
//								return;
//							}
//						}
//					} else {
//						view.setVisibility(View.INVISIBLE);
//						LayoutParams lp = new LayoutParams(screenHeight,
//								screenWidth, 0, 0);
//						right_corner_layout.updateViewLayout(
//								textureview_layout, lp);
//						return;
//					}
//				}
//
//				if ((currentIndex < firstVisibleItem - mListView.getHeaderViewsCount() || currentIndex > mListView
//						.getLastVisiblePosition()) && player.isPlaying) {
//					changeVideoPosition(true, 0, 0, 0, 0);
//					player.isPaused = true;
//					player.isPlaying = false;
//				}
//			}
//		});
//
//		// ButterKnife.bind(this, view);
//		return view;
//	}
//
//	public void showFullVideo(boolean isChangeFull) {
//		if (title_layout.getVisibility() == View.VISIBLE) {
//			activity.showBottomBar(false);
//			isFull = true;
//			setFullPlay(true);
//			title_layout.setVisibility(View.GONE);
//			LayoutParams lp = new LayoutParams(screenHeight, screenWidth, 0, 0);
//			video_function_layout.setVisibility(View.INVISIBLE);
//			// 设置当前窗体为全屏显示
//			WindowManager.LayoutParams lp2 = window.getAttributes();
//			lp2.flags |= WindowManager.LayoutParams.FLAG_FULLSCREEN;
//			window.setAttributes(lp2);
//			window.addFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
//			activity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
//			right_corner_layout.updateViewLayout(textureview_layout, lp);
//		} else {
//			if (!isChangeFull) {
//				changeFunctionLayoutShow();
//			} else {
//				activity.showBottomBar(true);
//				setFullPlay(false);
//				isFull = false;
//				title_layout.setVisibility(View.VISIBLE);
//				video_function_layout.setVisibility(View.INVISIBLE);
//				// 设置当前窗体为普通模式
//				WindowManager.LayoutParams attr = window.getAttributes();
//				attr.flags &= (~WindowManager.LayoutParams.FLAG_FULLSCREEN);
//				window.setAttributes(attr);
//				window.clearFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
//				activity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
//				right_corner_layout
//						.updateViewLayout(textureview_layout, old_lp);
//			}
//		}
//	}
//
//	public void changeFunctionLayoutShow() {
//		if (video_function_layout.getVisibility() == View.INVISIBLE) {
//			video_function_layout.setVisibility(View.VISIBLE);
//		} else {
//			video_function_layout.setVisibility(View.INVISIBLE);
//		}
//	}
//
//	void setListData() {
//		for (int i = 0; i <= 10; i++) {
//			VideoData vd1 = new VideoData();
//			vd1.setName("【" + i + "】" + "一起来欣赏《康美之恋》！");
//			vd1.setThumb("http://static.xw.feedss.com/uploadfile/videoimgs/20150806/63b6543c5874f607a6abd19a4c4b7a08.jpg");
//			vd1.setUrl("http://static.xw.feedss.com/uploadfile/videos/20150806/2554341be40646910911552ea494edab.mp4");
//			lvd.add(vd1);
//			VideoData vd2 = new VideoData();
//			vd2.setName("【" + i + "】" + "【新华图视】雾漫东江 渔歌曲");
//			vd2.setThumb("http://static.xw.feedss.com/uploadfile/videoimgs/20150806/5c3a6546534c0ee70a1e7ce66bd82a49.jpg");
//			vd2.setUrl("http://static.xw.feedss.com/uploadfile/videos/20150806/61f8986388eddf7c5d21d8322edb03c2.mp4");
//			lvd.add(vd2);
//			VideoData vd3 = new VideoData();
//			vd3.setName("【" + i + "】" + "新华炫视：“减压神器”《秘密花园》到底有什么秘密？");
//			vd3.setThumb("http://static.xw.feedss.com/uploadfile/bigthumb/20150804/359b636f1b2f837679ced28586be7bdd.png");
//			vd3.setUrl("http://static.xw.feedss.com/uploadfile/videos/20150804/0a88b01c1865fcee7737b9dd05b16407.mp4");
//			lvd.add(vd3);
//			VideoData vd4 = new VideoData();
//			vd4.setName("【" + i + "】" + "陈思思《雪恋》-庆祝申办冬奥会成功特别歌曲");
//			vd4.setThumb("http://static.xw.feedss.com/uploadfile/bigthumb/20150801/c49e390b7b6d1359385e3369b78909f4.png");
//			vd4.setUrl("http://static.xw.feedss.com/uploadfile/videos/20150801/1dbd4c6c60c7e59dde679515e6233e0b.mp4");
//			lvd.add(vd4);
//		}
//	}
//
//	class myAdapter extends BaseAdapter {
//
//		@Override
//		public int getCount() {
//			// TODO Auto-generated method stubs
//			return lvd.size();
//		}
//
//		@Override
//		public Object getItem(int position) {
//			// TODO Auto-generated method stub
//			return lvd.get(position);
//		}
//
//		@Override
//		public long getItemId(int position) {
//			// TODO Auto-generated method stub
//			return position;
//		}
//
//		@Override
//		public View getView(int position, View convertView, ViewGroup parent) {
//			final ViewHolder holder;
//			final int mPosition = position;
//			if (convertView == null) {
//				convertView = LayoutInflater.from(activity).inflate(
//						R.layout.video_item_layout, null);
//				holder = new ViewHolder();
//				holder.videoImage = (ImageView) convertView
//						.findViewById(R.id.video_image);
//				// holder.videoImage.setOnClickListener(new OnClickListener() {
//				//
//				// @Override
//				// public void onClick(View v) {
//				// changeFunctionLayoutShow();
//				// }
//				// });
//				holder.videoNameText = (TextView) convertView
//						.findViewById(R.id.video_title_tv);
//				holder.videoPlayBtn = (ImageView) convertView
//						.findViewById(R.id.video_play_btn);
//				convertView.setTag(holder);
//			} else {
//				holder = (ViewHolder) convertView.getTag();
//			}
//			String imageThumb = lvd.get(position).getThumb();
//			Object urlObject = holder.videoImage.getTag();
//			String imageUrl = "";
//			if (urlObject != null) {
//				imageUrl = (String) urlObject;
//			}
//			if (!imageUrl.equals(imageThumb)) {
//				ImageLoader.getInstance().displayImage(
//						lvd.get(position).getThumb(), holder.videoImage,
//						DisplayImageOptionsUtil.listNewImgOptions);
//			}
//			holder.videoImage.setTag(lvd.get(position).getThumb());
//
//			holder.videoNameText.setText(lvd.get(position).getName());
//			holder.videoPlayBtn.setVisibility(View.VISIBLE);
//			holder.videoImage.setVisibility(View.VISIBLE);
//			holder.videoNameText.setVisibility(View.VISIBLE);
//			if (player.currentIndex == position) {
//				holder.videoPlayBtn.setVisibility(View.INVISIBLE);
//				// holder.videoImage.setVisibility(View.INVISIBLE);
//				holder.videoNameText.setVisibility(View.INVISIBLE);
//
//				// if (player.isPlaying || player.playPosition == -1) {
//				// if (player != null) {
//				// holder.textureView.setVisibility(View.GONE);
//				// player.stop();
//				// holder.mSeekBar.setVisibility(View.GONE);
//				// }
//				// }
//				if (player.playPosition > 0 && player.isPaused
//						&& !player.isCorner) {
//					// changeVideoPosition(true, 0, 0, 0, 0);
//					// player.play(lvd.get(mPosition).getUrl());
//					player.isPaused = false;
//					player.isPlaying = true;
//				} else {
//					if (!isFull) {
//						changeVideoPosition(false, viewWidth, viewHeight, 0, 0);
//						player.play(lvd.get(mPosition).getUrl());
//						player.isPaused = false;
//						player.isPlaying = true;
//					}
//					// player.isCorner = false;
//				}
//			} else {
//				// player.isShowCurrent = false;
//				holder.videoPlayBtn.setVisibility(View.VISIBLE);
//				holder.videoImage.setVisibility(View.VISIBLE);
//				holder.videoNameText.setVisibility(View.VISIBLE);
//			}
//
//			holder.videoImage.setOnClickListener(new View.OnClickListener() {
//				@Override
//				public void onClick(View v) {
//					player.currentIndex = mPosition;
//					player.playPosition = -1;
//					viewHeight = holder.videoImage.getHeight();
//					viewWidth = holder.videoImage.getWidth();
//					initListY();
//					textureview_layout.setVisibility(View.VISIBLE);
//					LayoutParams lp = new LayoutParams(viewWidth, viewHeight,
//							0, 0);
//					textureview_layout.setLayoutParams(lp);
//					adapter.notifyDataSetChanged();
//				}
//			});
//			return convertView;
//		};
//	}
//
//	public void changeVideoPosition(boolean isCorner, int width, int height,
//			int x, int y) {
//		if (player.isCorner != isCorner) {
//			if (isCorner) {
//				textureview_layout.setOnTouchListener(touchLisTrue);
//			} else {
//				textureview_layout.setOnTouchListener(touchLisFalse);
//			}
//		}
//		player.isCorner = isCorner;
//		if (isCorner) {
//			int layout_height = right_corner_layout.getHeight();
//			int layout_width = right_corner_layout.getWidth();
//			LayoutParams lp = new LayoutParams(300, 300, layout_width - 320,
//					layout_height - 320);
//			old_lp = lp;
//			video_function_layout.setVisibility(View.INVISIBLE);
//			right_corner_layout.updateViewLayout(textureview_layout, lp);
//		} else {
//			LayoutParams lp = new LayoutParams(viewWidth, viewHeight, x, y);
//			old_lp = lp;
//			right_corner_layout.updateViewLayout(textureview_layout, lp);
//
//		}
//	}
//
//	public void initListY() {
//		if (content_layout != null) {
//			int[] location = new int[2];
//			content_layout.getLocationOnScreen(location);// 获取在整个屏幕内的绝对坐标
//			listY = location[1];
//		}
//	}
//
//	static class ViewHolder {
//		ImageView videoImage;
//		TextView videoNameText;
//		ImageView videoPlayBtn;
//	}
//
//	@Override
//	public void onPause() {
//		pause();
//		super.onPause();
//	}
//
//	public void pause() {
//		player.pause();
//	}
//
//	@Override
//	public void onStop() {
//		player.destroy();
//		super.onStop();
//	}
//
//	@Override
//	public void onDestroy() {
//		activity = null;
//		super.onDestroy();
//	}
//
//	public boolean isFullPlay() {
//		return isFullPlay;
//	}
//
//	public void setFullPlay(boolean isFullPlay) {
//		this.isFullPlay = isFullPlay;
//	}
//
//	@Override
//	public void onRefresh() {
//		// TODO Auto-generated method stub
//
//	}
//
//	@Override
//	public void onLoadMore() {
//		// TODO Auto-generated method stub
//
//	}
//
//}
