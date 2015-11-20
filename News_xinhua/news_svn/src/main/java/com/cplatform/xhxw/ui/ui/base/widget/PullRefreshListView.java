package com.cplatform.xhxw.ui.ui.base.widget;

import java.text.SimpleDateFormat;
import java.util.Date;

import com.cplatform.xhxw.ui.R;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.view.animation.DecelerateInterpolator;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Scroller;
import android.widget.TextView;

public class PullRefreshListView extends ListView implements OnScrollListener {
	private float mLastY = -1; // save event y
	private Scroller mScroller; // ���ڻع�
	private OnScrollListener mScrollListener; // �ع�����
	// ����ˢ�ºͼ��ظ��ӿ�.
	private PullRefreshListener mListViewListener;
	// -- ͷ����View
	private XListViewHeader mHeaderView;
	// �鿴ͷ�������ݣ��������ͷ���߶ȣ���������
	// �����õ�ʱ��ˢ��
	private RelativeLayout mHeaderViewContent;
	private TextView mHeaderTimeView;
	private int mHeaderViewHeight; // ͷ��View�ĸ�
	private boolean mEnablePullRefresh = true;
	private boolean mPullRefreshing = false; // �Ƿ�ˢ��.
	// -- �ײ���View
	private XListViewFooter mFooterView;
	private boolean mEnablePullLoad;
	private boolean mPullLoading;
	private boolean mIsFooterReady = false;
	// ���б�����ڼ���б���ͼ�ĵײ�
	private int mTotalItemCount;

	// for mScroller, ����ҳü����ҳ��
	private int mScrollBack;
	private boolean mIsRefreshable;
	private final static int SCROLLBACK_HEADER = 0;// ����
	private final static int SCROLLBACK_FOOTER = 1;// �²�

	private final static int SCROLL_DURATION = 400; // ������ʱ��
	private final static int PULL_LOAD_MORE_DELTA = 50; // ������50PX��ʱ�򣬼��ظ��

	private final static float OFFSET_RADIO = 1.8f; // support iOS like pull
													// feature.
	private boolean mCanLoadMore;

	private long oldRefreshTime;
	private ImageView mArrowImageView;
	private ProgressBar mProgressBar;
	private TextView xlistview_header_hint_textview;
	private TextView xlistview_header_last_time;
	private TextView xlistview_header_time;

	/**
	 * @param context
	 */
	public PullRefreshListView(Context context) {
		super(context);
		initWithContext(context);
	}

	public PullRefreshListView(Context context, AttributeSet attrs) {
		super(context, attrs);
		initWithContext(context);
	}

	public PullRefreshListView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		initWithContext(context);
	}

	@SuppressWarnings("deprecation")
	private void initWithContext(Context context) {
		mScroller = new Scroller(context, new DecelerateInterpolator());
		// XListView need the scroll event, and it will dispatch the event to
		// user's listener (as a proxy).
		super.setOnScrollListener(this);

		// ��ʼ��ͷ��View
		mHeaderView = new XListViewHeader(context);
		mHeaderViewContent = (RelativeLayout) mHeaderView
				.findViewById(R.id.xlistview_header_content);
		mHeaderTimeView = (TextView) mHeaderView
				.findViewById(R.id.xlistview_header_time);
		mArrowImageView = (ImageView) mHeaderView
				.findViewById(R.id.xlistview_header_arrow);
		mProgressBar = (ProgressBar) mHeaderView
				.findViewById(R.id.xlistview_header_progressbar);
		xlistview_header_hint_textview = (TextView) mHeaderView
				.findViewById(R.id.xlistview_header_hint_textview);
		xlistview_header_last_time = (TextView) mHeaderView
				.findViewById(R.id.xlistview_header_last_time);
		xlistview_header_time = (TextView) mHeaderView
				.findViewById(R.id.xlistview_header_time);

		addHeaderView(mHeaderView);// ��ͷ�������ͼ��ӽ�ȥ

		// ��ʼ���ײ���View
		mFooterView = new XListViewFooter(context);

		// ��ʼ��ͷ���߶�
		mHeaderView.getViewTreeObserver().addOnGlobalLayoutListener(
				new OnGlobalLayoutListener() {
					@Override
					public void onGlobalLayout() {
						mHeaderViewHeight = mHeaderViewContent.getHeight();
						getViewTreeObserver()
								.removeGlobalOnLayoutListener(this);
					}
				});
		mIsRefreshable = false;
		if (Integer.parseInt(Build.VERSION.SDK) >= 9) {
			setOverScrollMode(View.OVER_SCROLL_NEVER);
		}
	}

	@Override
	public void setAdapter(ListAdapter adapter) {
		// ȷ��XListViewFooter�����ײ���View, ����ֻ��һ��
		if (mIsFooterReady == false) {
			mIsFooterReady = true;
			addFooterView(mFooterView);
		}
		super.setAdapter(adapter);
	}

	/**
	 * ���û��������ˢ�¹���.
	 * 
	 * @param enable
	 */
	public void setPullRefreshEnable(boolean enable) {
		mEnablePullRefresh = enable;
		if (!mEnablePullRefresh) { // ����,��������
			mHeaderViewContent.setVisibility(View.INVISIBLE);// ���Ϊfalse����������ˢ�¹���
		} else {
			mHeaderViewContent.setVisibility(View.VISIBLE);// �������ʾ����ˢ�¹���
		}
	}

	/**
	 * ���û���ü��ظ��Ĺ���.
	 * 
	 * @param enable
	 */
	public void setCanLoadMore(boolean enable) {
		mEnablePullLoad = enable;
		if (!mEnablePullLoad) {
			mFooterView.hide();// ����
			mFooterView.setOnClickListener(null);
		} else {
			mPullLoading = false;
			mFooterView.show();// ��ʾ
			mFooterView.setState(XListViewFooter.STATE_NORMAL);
			// both "����" �� "���" �����ü��ظ��.
			mFooterView.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					startLoadMore();
				}
			});
		}
	}

	/**
	 * ֹͣˢ��, ����ͷ��ͼ.
	 */
	public void onRefreshComplete(Date date) {
		if (mPullRefreshing == true) {
			mPullRefreshing = false;
			resetHeaderHeight();
		}
	}

	/**
	 * stop load more, reset footer view.
	 */
	public void onLoadMoreComplete() {
		if (mPullLoading == true) {
			mPullLoading = false;
			mFooterView.setState(XListViewFooter.STATE_NORMAL);
		}
	}

	/**
	 * @param time
	 */
	public void setRefreshTime(Date date) {
		// SimpleDateFormat formatter = new SimpleDateFormat(
		// "yyyy��MM��dd��   HH:mm:ss     ");
		// Date curDate = new Date(System.currentTimeMillis());
		// // ��ȡ��ǰʱ��
		// String str = formatter.format(curDate);
		// mHeaderTimeView.setText(str);

		if (date != null) {
			mHeaderTimeView.setVisibility(View.VISIBLE);
			oldRefreshTime = date.getTime();
			mHeaderTimeView.setText(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
					.format(date));
		} else {
			mHeaderTimeView.setVisibility(View.GONE);
		}
	}

	private void invokeOnScrolling() {
		if (mScrollListener instanceof OnXScrollListener) {
			OnXScrollListener l = (OnXScrollListener) mScrollListener;
			l.onXScrolling(this);
		}
	}

	private void updateHeaderHeight(float delta) {
		mHeaderView.setVisiableHeight((int) delta
				+ mHeaderView.getVisiableHeight());
		if (mEnablePullRefresh && !mPullRefreshing) { // δ����ˢ��״̬�����¼�ͷ
			if (mHeaderView.getVisiableHeight() > mHeaderViewHeight) {
				mHeaderView.setState(XListViewHeader.STATE_READY);
			} else {
				mHeaderView.setState(XListViewHeader.STATE_NORMAL);
			}
		}
		setSelection(0); // scroll to top each time
	}

	/**
	 * ����ͷ��ͼ�ĸ߶�
	 */
	private void resetHeaderHeight() {
		int height = mHeaderView.getVisiableHeight();
		if (height == 0) // ����ʾ.
			return;
		// ����ʾˢ�ºͱ����ʱ��ʲô������ʾ
		if (mPullRefreshing && height <= mHeaderViewHeight) {
			return;
		}
		int finalHeight = 0; // Ĭ�ϣ�������ͷ.
		// ����������ʾ����ͷ����ʱ��ˢ��
		if (mPullRefreshing && height > mHeaderViewHeight) {
			finalHeight = mHeaderViewHeight;
		}
		mScrollBack = SCROLLBACK_HEADER;
		mScroller.startScroll(0, height, 0, finalHeight - height,
				SCROLL_DURATION);
		// ����ˢ��
		invalidate();
	}

	// �ı�ײ���ͼ�߶�
	private void updateFooterHeight(float delta) {
		int height = mFooterView.getBottomMargin() + (int) delta;
		if (mEnablePullLoad && !mPullLoading) {
			if (height > PULL_LOAD_MORE_DELTA) { // �߶����Ե��ü��ظ��
				mFooterView.setState(XListViewFooter.STATE_READY);
			} else {
				mFooterView.setState(XListViewFooter.STATE_NORMAL);
			}
		}
		mFooterView.setBottomMargin(height);

		// setSelection(mTotalItemCount - 1); // scroll to bottom
	}

	private void resetFooterHeight() {
		int bottomMargin = mFooterView.getBottomMargin();
		if (bottomMargin > 0) {
			mScrollBack = SCROLLBACK_FOOTER;
			mScroller.startScroll(0, bottomMargin, 0, -bottomMargin,
					SCROLL_DURATION);
			invalidate();
		}
	}

	// ��ʼ���ظ��
	private void startLoadMore() {
		mPullLoading = true;
		mFooterView.setState(XListViewFooter.STATE_LOADING);
		if (mListViewListener != null) {
			mListViewListener.onLoadMore();
		}
	}

	// �����¼�
	@Override
	public boolean onTouchEvent(MotionEvent ev) {
		if (mLastY == -1) {
			mLastY = ev.getRawY();
		}
		if (mIsRefreshable) {
			switch (ev.getAction()) {
			case MotionEvent.ACTION_DOWN:
				mLastY = ev.getRawY();
				break;
			case MotionEvent.ACTION_MOVE:
				final float deltaY = ev.getRawY() - mLastY;
				mLastY = ev.getRawY();
				System.out.println("��ݼ�⣺" + getFirstVisiblePosition()
						+ "---->" + getLastVisiblePosition());
				if (getFirstVisiblePosition() == 0
						&& (mHeaderView.getVisiableHeight() > 0 || deltaY > 0)) {
					// ��һ����ʾ,������ʾ��������.
					updateHeaderHeight(deltaY / OFFSET_RADIO);
					invokeOnScrolling();
				} else if (getLastVisiblePosition() == mTotalItemCount - 1
						&& (mFooterView.getBottomMargin() > 0 || deltaY < 0)) {
					// ���һҳ����ֹͣ����������
					updateFooterHeight(-deltaY / OFFSET_RADIO);
				}
				break;
			default:
				mLastY = -1; // ����
				if (getFirstVisiblePosition() == 0) {
					// ����ˢ��,���ͷ����ͼ�߶ȴ����趨�߶ȡ�
					if (mEnablePullRefresh
							&& mHeaderView.getVisiableHeight() > mHeaderViewHeight) {
						mPullRefreshing = true;// ��ôˢ��
						mHeaderView.setState(XListViewHeader.STATE_REFRESHING);
						if (mListViewListener != null) {
							mListViewListener.onRefresh();
						}
					}
					resetHeaderHeight();// ˢ����ϣ�����ͷ���߶ȣ�Ҳ���Ƿ����ϲ�
				}
				if (getLastVisiblePosition() == mTotalItemCount - 1) {
					// ���ü��ظ��.
					if (mEnablePullLoad
							&& mFooterView.getBottomMargin() > PULL_LOAD_MORE_DELTA) {
						startLoadMore();// ���ײ���ͼ�߶ȴ��ڿ��Լ��ظ߶ȣ���ô�Ϳ�ʼ����
					}
					resetFooterHeight();// ���ü��ظ����ͼ�߶�
				}
				break;
			}
		}
		return super.onTouchEvent(ev);
	}

	@Override
	public void computeScroll() {
		if (mScroller.computeScrollOffset()) {
			if (mScrollBack == SCROLLBACK_HEADER) {
				mHeaderView.setVisiableHeight(mScroller.getCurrY());
			} else {
				mFooterView.setBottomMargin(mScroller.getCurrY());
			}
			postInvalidate();
			invokeOnScrolling();
		}
		super.computeScroll();
	}

	@Override
	public void setOnScrollListener(OnScrollListener l) {
		mScrollListener = l;
	}

	@Override
	public void onScrollStateChanged(AbsListView view, int scrollState) {
		if (mScrollListener != null) {
			mScrollListener.onScrollStateChanged(view, scrollState);
		}
	}

	@Override
	public void onScroll(AbsListView view, int firstVisibleItem,
			int visibleItemCount, int totalItemCount) {
		// ���͵��û��ļ�����
		mTotalItemCount = totalItemCount;
		if (mScrollListener != null) {
			mScrollListener.onScroll(view, firstVisibleItem, visibleItemCount,
					totalItemCount);
		}
	}

	public void setPullRefreshListener(PullRefreshListener l) {
		mListViewListener = l;
	}

	/**
	 * ����Լ����б���ͼ��OnScrollListener �������. ��ᱻ���� , ��ͷ����ײ�������ʱ��
	 */
	public interface OnXScrollListener extends OnScrollListener {
		public void onXScrolling(View view);
	}

	/**
	 * ʵ������ӿ���ˢ��/���ظ����¼�
	 */
	public interface PullRefreshListener {
		public void onRefresh();

		public void onLoadMore();
	}

	public void setCanRefresh(boolean canRefresh) {
		mIsRefreshable = canRefresh;
	}

	// public void setCanLoadMore(boolean canLoadMore) {
	// mCanLoadMore = canLoadMore;
	// // changeLoadMoreViewByState();
	//
	// }

	public void triggerRefresh(boolean ignoreState) {
		if (!mPullRefreshing) {
			onRefresh();
		}
	}

	private void onRefresh() {
		if (mListViewListener != null) {
			mListViewListener.onRefresh();
		}
	}

	public void tryLoadMore() {
		if (mCanLoadMore && mListViewListener != null && !mPullLoading) {
			startLoadMore();
		}
	}

	/**
	 * 获得上次刷新时间
	 */
	public long getOldRefreshTime() {
		return oldRefreshTime;
	}

	/**
	 * 判断当前状态是否为加载中
	 */
	public boolean isLoading() {
		if (mPullRefreshing || mPullLoading) {
			return true;
		} else {
			return false;
		}
	}

	public void setmHeadArrowImage(int resId, int resIdPB) {
		mArrowImageView.setImageResource(resId);
		mProgressBar.setIndeterminateDrawable(this.getResources().getDrawable(
				resIdPB));
	}

	public void setHeadTextColor(int color) {
		// 松开刷新
		xlistview_header_hint_textview.setTextColor(color);
		// 最近更新
		xlistview_header_last_time.setTextColor(color);
		// 时间
		xlistview_header_time.setTextColor(color);
	}
}
