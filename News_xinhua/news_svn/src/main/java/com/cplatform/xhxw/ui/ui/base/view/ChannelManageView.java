package com.cplatform.xhxw.ui.ui.base.view;

import java.util.ArrayList;
import java.util.List;

import android.annotation.TargetApi;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.GridLayout;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import ca.laplanete.mobile.pageddragdropgrid.PagedDragDropGrid;

import com.cplatform.xhxw.ui.App;
import com.cplatform.xhxw.ui.Constants;
import com.cplatform.xhxw.ui.R;
import com.cplatform.xhxw.ui.StatisticalKey;
import com.cplatform.xhxw.ui.db.ChanneDB;
import com.cplatform.xhxw.ui.db.dao.ChanneDao;
import com.cplatform.xhxw.ui.http.APIClient;
import com.cplatform.xhxw.ui.http.net.saas.ChannelDragRequest;
import com.cplatform.xhxw.ui.ui.main.cms.ChannelDragGridAdapter;
import com.cplatform.xhxw.ui.ui.main.cms.ChannelDragGridAdapter.onChannelOperateListner;
import com.cplatform.xhxw.ui.ui.main.cms.channelsearch.SearchChannelUtil;
import com.cplatform.xhxw.ui.util.AnimationUtil;
import com.cplatform.xhxw.ui.util.LogUtil;
import com.cplatform.xhxw.ui.util.StringUtil;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.nineoldandroids.animation.Animator;
import com.nineoldandroids.animation.AnimatorListenerAdapter;
import com.nineoldandroids.view.ViewHelper;
import com.nineoldandroids.view.ViewPropertyAnimator;
import com.wbtech.ums.UmsAgent;

/**
 * 栏目管理 Created by cy-love on 14-1-9.
 */
public class ChannelManageView extends LinearLayout implements
		onChannelOperateListner {

	private static final String TAG = ChannelManageView.class.getSimpleName();
	@InjectView(R.id.gl_content)
	PagedDragDropGrid mShow;
	@InjectView(R.id.gl_other)
	GridLayout mHide;
	@InjectView(R.id.ly_content)
	View mContent;
	@InjectView(R.id.view_channel_manage_layout)
	RelativeLayout view_channel_manage_layout;

	private int mColumnCount;
	private static List<ChanneDao> sShowList = new ArrayList<ChanneDao>();
	private ArrayList<ChanneDao> mHideList = new ArrayList<ChanneDao>();
	private boolean mChange;
	private OnChanneChangeListener mLis;
	private RelativeLayout mSearchBtn;
	private Context mCon;

	private ChannelDragGridAdapter mShowChannelAdapter;
	private ProgressDialog mProgressDialog;

	private boolean isShow = false;
	private Handler mhandler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			if (msg.what == 0) {
				if (mProgressDialog != null) {
					mProgressDialog.dismiss();
				}
				setMyVisibility(View.GONE);
				if (mLis != null) {
					mLis.onChanneChage(mChange);
					mChange = false;
				}
			}
			super.handleMessage(msg);
		}
	};

	public void setOnChangeListener(OnChanneChangeListener lis) {
		mLis = lis;
	}

	public ChannelManageView(Context context) {
		super(context);
		this.init(context);
	}

	public ChannelManageView(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.init(context);
	}

	@TargetApi(Build.VERSION_CODES.HONEYCOMB)
	public ChannelManageView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		this.init(context);
	}

	private void init(Context con) {
		LayoutInflater inflater = (LayoutInflater) getContext()
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View rootView = inflater.inflate(R.layout.view_channel_manage, this);
		ButterKnife.inject(this);
		mSearchBtn = (RelativeLayout) rootView.findViewById(R.id.vcm_search_lo);
		mSearchBtn.setOnClickListener(addOnClick);
		mSearchBtn.setVisibility(View.VISIBLE);
		mColumnCount = getResources().getInteger(
				R.integer.channel_manage_column_count);
		setOnTouchListener(new OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				// 对下层view屏蔽
				return true;
			}
		});
		mCon = con;
	}

	public void setMyVisibility(int visibility) {
		switch (visibility) {
		case View.VISIBLE: {
			setShow(true);
			AnimationUtil
					.startViewTopInAndVisible(getContext(), mContent, this);
		}
			break;
		case View.GONE: {
			setShow(false);
			AnimationUtil.startViewTopOutAndGone(getContext(), mContent, this);

		}
			break;
		}
	}

	@OnClick(R.id.iv_close)
	public void closeAction() {
		mProgressDialog = ProgressDialog.show(mCon, null, "处理更改中...");
		new Thread() {

			@Override
			public void run() {
				setOrder();
				ChanneDB.clearUselessKeywordChannel(App.CONTEXT);
				mhandler.sendEmptyMessage(0);
			}
		}.start();
	}

	public void setShow(List<ChanneDao> shows) {
		// Collections.copy(mShowList, shows);
		sShowList.clear();
		sShowList.addAll(shows);
		mShowChannelAdapter = new ChannelDragGridAdapter(getContext(), mShow,
				sShowList);
		mShowChannelAdapter.setmOnChanneOperateListner(this);
		mShow.setAdapter(mShowChannelAdapter);
		mShowChannelAdapter.notifyDataChanged();
	}

	public void setHide(List<ChanneDao> hides) {
		mHideList.clear();
		mHideList.addAll(hides);
		mHide.removeAllViews();
		for (ChanneDao item : mHideList) {
			this.setHideItem(item);
		}
	}

	private void setHideItem(ChanneDao item) {
		ChannelView view = getView();
		view.setTitle(item.getChannelName());
		view.setOnClickListener(addOnClick);
		view.setTag(item);
		if (item.isInVisible()) {
			view.setVisibility(View.INVISIBLE);
		} else {
			view.setVisibility(View.VISIBLE);
		}
		mHide.addView(view);
	}

	private ChannelView getView() {
		int itemWidth = Constants.screenWidth / mColumnCount;
		ChannelView view = new ChannelView(getContext());
		view.setLayoutParams(new GridLayout.LayoutParams(
				new ViewGroup.LayoutParams(itemWidth,
						ViewGroup.LayoutParams.WRAP_CONTENT)));
		return view;
	}

	private ChannelView getShowView(ChanneDao item) {
		int itemWidth = Constants.screenWidth / mColumnCount;
		ChannelView view = new ChannelView(getContext());
		view.setLayoutParams(new GridLayout.LayoutParams(
				new ViewGroup.LayoutParams(itemWidth,
						ViewGroup.LayoutParams.WRAP_CONTENT)));
		view.setTitle(item.getChannelName());

		return view;
	}

	private void onChannelRemoveFromShow(ChanneDao item) {
		mHideList.add(item);
		setHideItem(item);
	}

	private void onHideListShow(ChanneDao item) {
		for (int i = 0; i < mHide.getChildCount(); i++) {
			View v = mHide.getChildAt(i);
			ChanneDao item_tag = (ChanneDao) v.getTag();
			if (item_tag == item) {
				v.setVisibility(View.VISIBLE);
			}
		}
	}

	private OnClickListener addOnClick = new OnClickListener() {
		@Override
		public void onClick(final View v) {
			// if (CommonUtils.isFastDoubleClick(200)) {
			// return;
			// }
			if (v.getId() == R.id.vcm_search_lo) {
				if (mLis != null) {
					mLis.onSearchBtnClick();
				}
			} else {
				final ChanneDao item = (ChanneDao) v.getTag();
				if (StringUtil.parseIntegerFromString(item
						.getChannelCreateType()) == ChanneDao.CHANNEL_TYPE_KEY_WORDS) {
					SearchChannelUtil.dictAKeywordChannel(item);
					updateChannelToDb(item);
				}
				int XY[] = new int[2];
				item.setInVisible(true);
				XY = mShowChannelAdapter.addItem(item);
				mHideList.remove(item);

				int[] location = new int[2];
				v.getLocationInWindow(location); // 获取在当前窗口内的绝对坐标
				// v.getLocationOnScreen(location);//获取在整个屏幕内的绝对坐标
				mHide.removeView(v);
				view_channel_manage_layout.addView(v);
				v.setEnabled(false);
				ViewHelper.setTranslationX(v, location[0]);
				ViewHelper.setTranslationY(v, location[1]);

				ViewPropertyAnimator.animate(v).translationX(XY[0])
						.translationY(XY[1]).setDuration(200)
						.setListener(new AnimatorListenerAdapter() {
							@Override
							public void onAnimationEnd(Animator animation) {
								// Item滑出界面之后执行删除

								item.setInVisible(false);
								view_channel_manage_layout.removeView(v);
								mShowChannelAdapter.notifyDataChanged();
							}
						});
				mChange = true;
				UmsAgent.onEvent(mCon, StatisticalKey.channel_add,
						new String[] { StatisticalKey.key_channelid },
						new String[] { item.getChannelID() });
			}
		}
	};

	/**
	 * 设置显示栏目的排序
	 */
	private void setOrder() {
		List<ChanneDao> gDChannels = new ArrayList<ChanneDao>();
		List<ChanneDao> otherChannels = new ArrayList<ChanneDao>();
		for (ChanneDao cd : sShowList) {
			if (cd != null) {
				if (cd.getChannelCreateType() != null
						&& (cd.getChannelCreateType().equals(
								"" + ChanneDao.CHANNEL_TYPE_GUDING))) {
					gDChannels.add(cd);
				} else {
					otherChannels.add(cd);
				}
			}
		}
		gDChannels.addAll(otherChannels);

		int size = gDChannels.size();
		for (int i = 0; i < size; i++) {
			ChanneDao item = gDChannels.get(i);
			item.setDirty(1);
			item.setListorder(i + 1);
			mChange = true;
			int row = ChanneDB.updateChanneById(App.getCurrentApp(), item);
			LogUtil.d(TAG, "更改数据库用户栏目顺序::" + item.getChannelName()
					+ " success::" + (row == 1));
		}
		commitChannelOrder(gDChannels);
	}

	private void commitChannelOrder(List<ChanneDao> channels) {
		StringBuffer sb = new StringBuffer();
		if (channels == null || channels.size() < 1) {
			return;
		}
		for (ChanneDao cd : channels) {
			sb.append(cd.getChannelID());
			sb.append(",");
		}
		try {
			sb.deleteCharAt(sb.length() - 1);
			ChannelDragRequest cdr = new ChannelDragRequest();
			cdr.setParam(sb.toString());
			APIClient.orderChannelDrag(cdr, new AsyncHttpResponseHandler());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public interface OnChanneChangeListener {

		/**
		 * 栏目更改
		 * 
		 * @param isChange
		 *            是否更改 true：更改
		 */
		public void onChanneChage(boolean isChange);

		public void onSearchBtnClick();
	}

	@Override
	public void onAddShow(ChanneDao item) {
	}

	@Override
	public void onDeleteShow(final ChanneDao item) {
		item.setInVisible(true);
		final View v = getShowView(item);

		view_channel_manage_layout.addView(v);
		v.setEnabled(false);
		ViewHelper.setTranslationX(v, item.XY[0]);
		ViewHelper.setTranslationY(v, item.XY[1]);

		int[] mHide_location = new int[2];
		mHide.getLocationInWindow(mHide_location); // 获取在当前窗口内的绝对坐标
		int[] XY = new int[2];
		int size = mHide.getChildCount();
		int mHide_h = mHide.getHeight();
		int line = 1;
		int line_height = 100;
		if (size > 0) {
			if (size % 3 == 0) {
				line = size / 3;
			} else {
				line = size / 3 + 1;
			}
			line_height = mHide_h / line;
		}
		size++;
		if (size % 3 == 1) {
			// 新起一行,第一个
			XY[0] = 0;
			XY[1] = mHide_location[1] + mHide_h;
			// XY[1] = mGridview_location[1]+mGridview_h;
		} else if (size % 3 == 2) {
			// 第二个
			XY[0] = Constants.screenWidth * 1 / 3;
			XY[1] = mHide_location[1] + mHide_h - line_height;
		} else if (size % 3 == 0) {
			// 第三个
			XY[0] = Constants.screenWidth * 2 / 3;
			XY[1] = mHide_location[1] + mHide_h - line_height;
		}
		if (XY[1] > Constants.screenHeight - item.XY[1]) {
			XY[1] = Constants.screenHeight - item.XY[1];
		}

		ViewPropertyAnimator.animate(v).translationX(XY[0]).translationY(XY[1])
				.setDuration(200).setListener(new AnimatorListenerAdapter() {
					@Override
					public void onAnimationEnd(Animator animation) {
						item.setInVisible(false);
						view_channel_manage_layout.removeView(v);
						onHideListShow(item);
					}
				});
		onChannelRemoveFromShow(item);

		UmsAgent.onEvent(mCon, StatisticalKey.channel_del,
				new String[] { StatisticalKey.key_channelid },
				new String[] { item.getChannelID() });
		mChange = true;
		if (StringUtil.parseIntegerFromString(item.getChannelCreateType()) == ChanneDao.CHANNEL_TYPE_KEY_WORDS) {
			SearchChannelUtil.cancelDictChannel(item);
		}
		dealCancelSubData(item);
	}

	/**
	 * 保存订阅频道到数据库
	 * 
	 * @param channel
	 */
	private void updateChannelToDb(ChanneDao channel) {
		channel.setShow(0);
		ChanneDB.updateChanneById(App.CONTEXT, channel);
	}

	/**
	 * 取消订阅频道
	 * 
	 * @param channel
	 */
	private void dealCancelSubData(ChanneDao channel) {
		channel.setShow(1);
		if (StringUtil.parseIntegerFromString(channel.getChannelCreateType()) == ChanneDao.CHANNEL_TYPE_KEY_WORDS) {
			ChanneDB.delChanneByChannelId(App.CONTEXT, channel.getChannelID());
		} else {
			ChanneDB.updateChanneById(App.CONTEXT, channel);
		}
	}

	@Override
	public void onOrderChange() {
		mChange = true;
	}

	public boolean isShow() {
		return isShow;
	}

	public void setShow(boolean isShow) {
		this.isShow = isShow;
	}

	public boolean ismChange() {
		return mChange;
	}

	public void setmChange(boolean mChange) {
		this.mChange = mChange;
	}

	@Override
	protected void onDetachedFromWindow() {
		ButterKnife.reset(this);
		super.onDetachedFromWindow();
	}
}
