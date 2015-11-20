package com.hy.superemsg.components;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListAdapter;
import android.widget.ListView;

import com.hy.superemsg.R;
import com.hy.superemsg.adapter.AbsPinedContactAdapter;
import com.hy.superemsg.adapter.IPinnedHeaderAdapter;

public class PinnedHeaderListView extends ListView {

	private AbsPinedContactAdapter mAdapter;
	private View mPinnedHeaderView;
	private int mPinnedHeaderHeight;
	private int mPinnedHeaderWidth;
	private boolean hasLayoutPinnedHeader = false;

	public PinnedHeaderListView(Context context, AttributeSet attrs) {
		super(context, attrs);
		setPinnedHeaderView(LayoutInflater.from(context).inflate(
				R.layout.pined_item_contact_pointer, this, false));
	}

	public void setPinnedHeaderView(View pinnedHeaderView) {
		mPinnedHeaderView = pinnedHeaderView;
	}

	public void configPinnedHeaderView(int postion) {
		int pinnedHeaderState = mAdapter.getPinedHeaderState(postion);
		// Log.i("xxxx", "getPinedHeaderState:"+pinnedHeaderState);
		switch (pinnedHeaderState) {
		case IPinnedHeaderAdapter.PINNED_HEADER_GONE:
			hasLayoutPinnedHeader = false;
			if (mPinnedHeaderView != null) {
				// removeViewInLayout(mPinnedHeaderView);
			}
			break;
		case IPinnedHeaderAdapter.PINNED_HEADER_VISIBLE:
			if (mPinnedHeaderView.getTop() != 0) {
				mPinnedHeaderView.layout(0, 0, mPinnedHeaderWidth,
						mPinnedHeaderHeight);
			}
			hasLayoutPinnedHeader = true;
			mAdapter.configPinnedHeader(mPinnedHeaderView, postion);
			break;
		case IPinnedHeaderAdapter.PINNED_HEADER_PUSHED_UP:
			View itemView = getChildAt(0);
			// Log.i("xxxx",
			// "itemView.getTop:"+itemView.getTop()+",mPinnedHeaderView.getTop:"+mPinnedHeaderView.getTop());
			// int itemHeight = itemView.getHeight();
			int itemBotton = itemView.getBottom();
			int pinnedHeaderHeight = mPinnedHeaderView.getHeight();
			// Log.i("xxxx",
			// "itemBotton:"+itemBotton+",pinnedHeaderHeight:"+pinnedHeaderHeight+",mPinnedHeaderHeight:"+mPinnedHeaderHeight);
			int y;
			if (mPinnedHeaderHeight > itemBotton) {
				y = itemBotton - pinnedHeaderHeight;
			} else {
				y = 0;
			}
			mAdapter.configPinnedHeader(mPinnedHeaderView, postion);
			if (mPinnedHeaderView.getTop() != y) {
				mPinnedHeaderView.layout(0, y, mPinnedHeaderWidth,
						mPinnedHeaderHeight + y);
			}
			hasLayoutPinnedHeader = true;
			break;

		default:
			break;
		}
	}

	@Override
	public void setAdapter(ListAdapter adapter) {

		if (mAdapter != null) {
			setOnScrollListener(null);
		}

		mAdapter = (AbsPinedContactAdapter) adapter;
		setOnScrollListener(mAdapter);
		super.setAdapter(adapter);
	}

	@Override
	protected void onLayout(boolean changed, int l, int t, int r, int b) {
		super.onLayout(changed, l, t, r, b);
		if (mPinnedHeaderView != null) {
			mPinnedHeaderView.layout(0, 0, mPinnedHeaderWidth,
					mPinnedHeaderHeight);
			mAdapter.configPinnedHeader(mPinnedHeaderView,
					getFirstVisiblePosition());
		}
		// Log.i("xxxx", "onLayout");
	}

	@Override
	protected void dispatchDraw(Canvas canvas) {
		super.dispatchDraw(canvas);
		// Log.i("xxxx", "dispatchDraw");
		if (mPinnedHeaderView != null && hasLayoutPinnedHeader) {
			drawChild(canvas, mPinnedHeaderView, getDrawingTime());
		}
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
		if (mPinnedHeaderView != null) {
			ViewGroup.LayoutParams lp = mPinnedHeaderView.getLayoutParams();
			measureChild(mPinnedHeaderView, widthMeasureSpec, heightMeasureSpec);
			mPinnedHeaderHeight = mPinnedHeaderView.getMeasuredHeight();
			mPinnedHeaderWidth = mPinnedHeaderView.getMeasuredWidth();
		}
		// Log.i("xxxx", "onMeasure mPinnedHeaderWidth:"+mPinnedHeaderWidth);
	}
}
