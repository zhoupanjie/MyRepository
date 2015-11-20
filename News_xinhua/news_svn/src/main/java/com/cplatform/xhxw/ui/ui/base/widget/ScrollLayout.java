package com.cplatform.xhxw.ui.ui.base.widget;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.support.v4.view.MotionEventCompat;
import android.support.v4.view.ViewCompat;
import android.support.v4.widget.ViewDragHelper;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.cplatform.xhxw.ui.Constants;
import com.cplatform.xhxw.ui.R;
import com.cplatform.xhxw.ui.StatisticalKey;
import com.cplatform.xhxw.ui.util.DisplayImageOptionsUtil;
import com.cplatform.xhxw.ui.util.LogUtil;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.umeng.analytics.MobclickAgent;
import com.wbtech.ums.UmsAgent;

/**
 * 首页标签布局 Created by cy-love on 13-12-26.
 */
public class ScrollLayout extends LinearLayout {

	private static final String TAG = ScrollLayout.class.getSimpleName();

	private static final int MIN_FLING_VELOCITY = 400; // dips per second
	private ImageView mHeaderView;
	private View mContentView;
	private View mBgView;

	private float mInitialMotionX;
	private float mInitialMotionY;

	private int mDragRange;
	private int mTop;
	private float mDragOffset;
	private boolean isUnderEnterprise = false;;

	private ViewDragHelper mDragHelper;
	private DragHelperCallback mDragCallBack;

	private OnFlagAreaExpandListener onFlagAreaExpandLis;

	public ScrollLayout(Context context) {
		super(context);
		init(context);
	}

	public ScrollLayout(Context context, AttributeSet attrs) {
		super(context, attrs);
		init(context);
	}

	@TargetApi(Build.VERSION_CODES.HONEYCOMB)
	public ScrollLayout(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init(context);
	}

	private void init(Context context) {
		final float density = context.getResources().getDisplayMetrics().density;
		mDragCallBack = new DragHelperCallback();
		mDragHelper = ViewDragHelper.create(this, 1.0f, mDragCallBack);
		mDragHelper.setMinVelocity(MIN_FLING_VELOCITY * density);
	}

	/**
	 * 判断角标是否打开
	 * 
	 * @return true表示打开，否则为关闭
	 */
	public boolean isOpen() {
		return mDragOffset != 0;
	}

	/**
	 * 关闭
	 */
	public void maximize() {
		if (mHeaderView.getVisibility() == View.VISIBLE) {
			if (!isUnderEnterprise) {
				if (Constants.hasEnterpriseAccountLoggedIn()) {
					ImageLoader.getInstance().displayImage(
							Constants.userInfo.getEnterprise().getLogo(),
							mHeaderView,
							DisplayImageOptionsUtil.flagDisplayOpts);
				} else {
					mHeaderView.setImageResource(R.drawable.flag);
				}
			} else {
				mHeaderView.setImageResource(R.drawable.flag);
			}
			mBgView.setVisibility(View.GONE);
			smoothSlideTo(0.0f);
		}
	}

	/**
	 * 打开
	 */
	public void minimize() {
		if (mHeaderView.getVisibility() == View.VISIBLE) {
			if (onFlagAreaExpandLis != null) {
				onFlagAreaExpandLis.onFlagAreaExpand();
			}
			smoothSlideTo(1.0f);
		}
	}

	private boolean smoothSlideTo(float slideOffset) {
		final int topBound = getPaddingTop();
		int y = (int) (topBound + slideOffset * mDragRange);

		if (mDragHelper
				.smoothSlideViewTo(mHeaderView, mHeaderView.getLeft(), y)) {
			ViewCompat.postInvalidateOnAnimation(this);
			return true;
		}
		return false;
	}

	@Override
	protected void onFinishInflate() {
		LogUtil.d(TAG, "onFInishInflate");
		mHeaderView = (ImageView) findViewById(R.id.view_header);
		mContentView = findViewById(R.id.view_content);
		mBgView = new View(getContext());
		mBgView.setBackgroundResource(R.color.translucent);

		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
			mBgView.setAlpha(0.0f);
		}
		mBgView.setVisibility(View.GONE);
		mBgView.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				maximize();
			}
		});
		addView(mBgView, 0);

		if (isUnderEnterprise) {
			mHeaderView.setImageResource(R.drawable.flag);
		}
	}

	@Override
	protected void onLayout(boolean changed, int l, int t, int r, int b) {
		mBgView.layout(l, t, r, b);
		int dragViewHeight = mHeaderView.getMeasuredHeight();
		int dragViewWidth = mHeaderView.getMeasuredWidth();
		mDragRange = mContentView.getMeasuredHeight();// parentViewHeight -
														// dragViewHeight;
		if (Constants.hasEnterpriseAccountLoggedIn()) {
			mTop = 0;
		}
		LogUtil.d(TAG, "onLayout:" + "changed:" + changed + ",l:" + l + ",t:"
				+ t + ",r:" + r + ",b:" + b + ",mDragRange" + mDragRange
				+ ",mtop----" + mTop + " drageViewWidth----" + dragViewWidth
				+ " dragViewHeight----" + dragViewHeight);
		mContentView.layout(0, 0, r, mTop);
		mHeaderView.layout(20, mTop + mHeaderView.getPaddingTop(),
				20 + dragViewWidth, mTop + dragViewHeight);

	}

	private class DragHelperCallback extends ViewDragHelper.Callback {

		@Override
		public boolean tryCaptureView(View child, int pointerId) {
			boolean ifRun = (child == mHeaderView && mHeaderView
					.getVisibility() == View.VISIBLE);
			if (ifRun && mDragOffset == 0) {
				MobclickAgent.onEvent(getContext(), StatisticalKey.menu);

				UmsAgent.onEvent(getContext(), StatisticalKey.menu);
			}
			return ifRun;
		}

		@Override
		public void onViewPositionChanged(View changedView, int left, int top,
				int dx, int dy) {
			LogUtil.i(TAG, "onViewPositionChanged:" + "left:" + left + ",top:"
					+ top + ",dx:" + dx + ",dy:" + dy);
			mTop = top;
			mDragOffset = (float) top / mDragRange;

			LogUtil.i(TAG, "onViewPositionChanged:" + "mDragOffset:"
					+ mDragOffset);
			if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
				mBgView.setAlpha(mDragOffset);
			}
			if (!isUnderEnterprise) {
				if (Constants.hasEnterpriseAccountLoggedIn()) {
					ImageLoader.getInstance().displayImage(
							Constants.userInfo.getEnterprise().getLogo(),
							mHeaderView,
							DisplayImageOptionsUtil.flagDisplayOpts);
					mBgView.setVisibility(View.GONE);
				} else {
					if (mDragOffset < 1) {
						mHeaderView.setImageResource(R.drawable.flag);
						mBgView.setVisibility(View.GONE);
					} else {
						mHeaderView.setImageResource(R.drawable.flag_up);
						mBgView.setVisibility(View.VISIBLE);
					}
				}
			} else {
				mHeaderView.setImageResource(R.drawable.flag);
				mBgView.setVisibility(View.GONE);
			}
			requestLayout();
		}

		@Override
		public int clampViewPositionVertical(View child, int top, int dy) {
			final int topBound = getPaddingTop();
			final int bottomBound = getHeight() - mHeaderView.getHeight()
					- mHeaderView.getPaddingBottom();

			final int newTop = Math.min(Math.max(top, topBound), bottomBound);
			return newTop;
		}

		@Override
		public void onViewReleased(View releasedChild, float xvel, float yvel) {
			LogUtil.i(TAG, "onViewReleased:" + "xvel:" + xvel + ",yvel:" + yvel);
			// yvel Fling产生的值，yvel > 0 则是快速往下Fling || yvel < 0 则是快速往上Fling

			int top = getPaddingTop();
			if (yvel > 0 || (yvel == 0 && mDragOffset > 0.4f)/* 后面这个小括号里判断处理拖动之后停下来但是未松手的情况 */) {
				top += mDragRange;
			}
			if (Constants.hasEnterpriseAccountLoggedIn()) {
				top = 0;
			}
			mDragHelper.settleCapturedViewAt(releasedChild.getLeft(), top);
			invalidate();
		}

		@Override
		public int getViewVerticalDragRange(View child) {
			return mDragRange;
		}

	}

	@Override
	public void computeScroll() {
		if (mDragHelper.continueSettling(true)) {
			ViewCompat.postInvalidateOnAnimation(this);
		}
	}

	@Override
	public boolean onInterceptTouchEvent(MotionEvent ev) {
		final int action = MotionEventCompat.getActionMasked(ev);

		if (action != MotionEvent.ACTION_DOWN) {
			mDragHelper.cancel();
			return super.onInterceptTouchEvent(ev);
		}

		if (action == MotionEvent.ACTION_CANCEL
				|| action == MotionEvent.ACTION_UP) {
			mDragHelper.cancel();
			return false;
		}

		final float x = ev.getX();
		final float y = ev.getY();
		boolean interceptTap = false;

		switch (action) {
		case MotionEvent.ACTION_DOWN:
			mInitialMotionX = x;
			mInitialMotionY = y;
			interceptTap = mDragHelper.isViewUnder(mHeaderView, (int) x,
					(int) y);
			break;

		case MotionEvent.ACTION_MOVE:
			final float adx = Math.abs(x - mInitialMotionX);
			final float ady = Math.abs(y - mInitialMotionY);
			final int slop = mDragHelper.getTouchSlop();
			LogUtil.d(TAG, "adx:" + adx + " ady:" + ady + " slop:" + slop);
			if (ady > slop && adx > ady) {
				mDragHelper.cancel();
				return false;
			}
			break;
		}

		return mDragHelper.shouldInterceptTouchEvent(ev) || interceptTap;
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		mDragHelper.processTouchEvent(event);

		final int action = event.getAction();
		final float x = event.getX();
		final float y = event.getY();

		// 触摸点是否落在HeaderView上
		boolean isHeaderViewUnder = mDragHelper.isViewUnder(mHeaderView,
				(int) x, (int) y);

		switch (action & MotionEventCompat.ACTION_MASK) {
		case MotionEvent.ACTION_DOWN: {
			mInitialMotionX = x;
			mInitialMotionY = y;
			break;
		}
		case MotionEvent.ACTION_UP: {
			final float dx = x - mInitialMotionX;
			final float dy = y - mInitialMotionY;
			final float slop = mDragHelper.getTouchSlop();

			LogUtil.i(TAG, "dx * dx + dy * dy = " + dx * dx + dy * dy);
			LogUtil.i(TAG, "slop * slop = " + slop * slop);
			LogUtil.i(TAG, "mDragOffset:" + mDragOffset);
			if (dx * dx + dy * dy < slop * slop && isHeaderViewUnder) {
				if (Constants.hasEnterpriseAccountLoggedIn()) {
					if (onFlagAreaExpandLis != null) {
						onFlagAreaExpandLis.onSAASCMSSwitch();
					}
				} else {
					if (mDragOffset == 0) {
						minimize();
					} else {
						maximize();
					}
				}
			} else {
				if (Constants.hasEnterpriseAccountLoggedIn()) {
					if (onFlagAreaExpandLis != null) {
						onFlagAreaExpandLis.onSAASCMSSwitch();
					}
				}
			}
			break;
		}
		}

		return isHeaderViewUnder && isViewHit(mHeaderView, (int) x, (int) y)
				|| isViewHit(mContentView, (int) x, (int) y);
	}

	private boolean isViewHit(View view, int x, int y) {
		int[] viewLocation = new int[2];
		view.getLocationOnScreen(viewLocation);
		int[] parentLocation = new int[2];
		this.getLocationOnScreen(parentLocation);
		int screenX = parentLocation[0] + x;
		int screenY = parentLocation[1] + y;
		return screenX >= viewLocation[0]
				&& screenX < viewLocation[0] + view.getWidth()
				&& screenY >= viewLocation[1]
				&& screenY < viewLocation[1] + view.getHeight();
	}

	public OnFlagAreaExpandListener getOnFlagAreaExpandLis() {
		return onFlagAreaExpandLis;
	}

	public void setOnFlagAreaExpandLis(
			OnFlagAreaExpandListener onFlagAreaExpandLis) {
		this.onFlagAreaExpandLis = onFlagAreaExpandLis;
	}

	public interface OnFlagAreaExpandListener {
		void onFlagAreaExpand();

		void onSAASCMSSwitch();

		void onSwitchLanguage(String language);
	}

	public void onResume(boolean isUnderEnterprise) {
		this.isUnderEnterprise = isUnderEnterprise;
		if (isUnderEnterprise) {
			mHeaderView.setImageResource(R.drawable.flag);
		} else {
			if (Constants.hasEnterpriseAccountLoggedIn()) {
				LogUtil.e(TAG, "-------------------login--------------->>>>");
				ImageLoader.getInstance().displayImage(
						Constants.userInfo.getEnterprise().getLogo(),
						mHeaderView, DisplayImageOptionsUtil.flagDisplayOpts);
			} else {
				mHeaderView.setImageResource(R.drawable.flag);
			}
		}
	}

	@Override
	public void removeAllViews() {
		// TODO Auto-generated method stub
		mDragHelper = null;
		mDragCallBack = null;
		onFlagAreaExpandLis = null;
		detachAllViewsFromParent();
		super.removeAllViews();
	}
}
