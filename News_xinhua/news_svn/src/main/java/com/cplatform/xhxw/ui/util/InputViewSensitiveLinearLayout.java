package com.cplatform.xhxw.ui.util;

import android.content.Context;
import android.os.Handler;
import android.util.AttributeSet;
import android.widget.LinearLayout;

/**
 * 监视输入框的LinearLayout<br/>
 * 有输入框弹出和隐藏的接口
 * @author baiwenlong
 *
 */
public class InputViewSensitiveLinearLayout extends LinearLayout {

	private final String TAG = InputViewSensitiveLinearLayout.class
			.getSimpleName();
	private OnInputViewListener mOnInputViewListener;
	private int mInitHeight;
	private boolean mIsShowingInputView;

	private Handler mHandler = new Handler();

	public void setOnInputViewListener(OnInputViewListener listener) {
		mOnInputViewListener = listener;
	}

	public InputViewSensitiveLinearLayout(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public InputViewSensitiveLinearLayout(Context context) {
		super(context);
	}

	@Override
	protected void onSizeChanged(int w, int h, int oldw, int oldh) {
		super.onSizeChanged(w, h, oldw, oldh);
		LogUtil.d(TAG, "onSizeChanged:height = " + h + ", old height = " + oldh);
		if (oldh == 0) {
			mInitHeight = h;
		} else {
			if (mOnInputViewListener != null) {
				if (h == mInitHeight) {
					mIsShowingInputView = false;
					mHandler.post(mHideRunnable);
				} else if (h < mInitHeight && !mIsShowingInputView) {
					mIsShowingInputView = true;
					mHandler.post(mShowRunnable);
				} else {
					LogUtil.e(TAG, "not recongnized onSizeChanged");
				}
			}
		}
	}

	private Runnable mHideRunnable = new Runnable() {

		@Override
		public void run() {
			LogUtil.d(TAG, "onHide");
			mOnInputViewListener.onHideInputView();
		}
	};
	private Runnable mShowRunnable = new Runnable() {

		@Override
		public void run() {
			LogUtil.d(TAG, "onShow");
			mOnInputViewListener.onShowInputView();
		}
	};

	public static interface OnInputViewListener {
		public void onShowInputView();

		public void onHideInputView();
	}

}
