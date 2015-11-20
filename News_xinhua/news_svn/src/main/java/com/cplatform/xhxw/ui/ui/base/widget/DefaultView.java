package com.cplatform.xhxw.ui.ui.base.widget;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.*;
import butterknife.ButterKnife;
import butterknife.Bind;
import butterknife.OnClick;

import com.cplatform.xhxw.ui.R;

/**
 * 加载状态布局 Created by cy-love on 13-12-25.
 */
public class DefaultView extends LinearLayout {

	public static enum Status {
		/**
		 * 加载中
		 */
		loading,
		/**
		 * 显示数据
		 */
		showData,
		/**
		 * 无数据
		 */
		nodata,
		/**
		 * 异常
		 */
		error
	}

	@Bind(R.id.pb_loading)
	ProgressBar mLoading;
	@Bind(R.id.iv_bg)
	ImageView mBg;

	private OnTapListener mLis;
	private Status mStu = Status.nodata;
	private View mHidVs[];

	public interface OnTapListener {
		/**
		 * 背景图，开始加载
		 */
		public void onTapAction();
	}

	public DefaultView(Context context) {
		super(context);
		this.init();
	}

	public DefaultView(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.init();
	}

	@TargetApi(Build.VERSION_CODES.HONEYCOMB)
	public DefaultView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		this.init();
	}

	private void init() {
		LayoutInflater inflater = (LayoutInflater) getContext()
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		inflater.inflate(R.layout.view_default, this);
		ButterKnife.bind(this);
		setStatus(Status.nodata);
	}
	/**
	 * 
	 * @Name setDefaultImage 
	 * @Description TODO 设置图片 广播里用到
	 * @param resId 
	 * @return void
	 * @Author zxe
	 * @Date 2015年9月21日 下午1:39:16
	*
	 */
	public void setDefaultImage(int resId){
		if(mBg!=null){
			mBg.setImageResource(resId);
		}
	}
	@OnClick(R.id.ly_defult)
	public void onTapAction(View v) {
		if (mStu != Status.loading && null != mLis) {
			mLis.onTapAction();
		}
	}

	public void setListener(OnTapListener lis) {
		mLis = lis;
	}

	/**
	 * 设置当前状态（注：设置nodata或者error状态前，必须调用过loading状态）
	 * 
	 * @param status
	 */
	public void setStatus(Status status) {
		if (mBg != null && mLoading != null) {
			mStu = status;
			switch (status) {
			case loading: // 加载中
				mBg.setVisibility(View.GONE);
				mLoading.setVisibility(View.VISIBLE);
				setVisibility(View.VISIBLE);
				if (mHidVs != null) {
					for (View v : mHidVs)
						v.setVisibility(View.INVISIBLE);
				}
				break;
			case nodata: // 无数据
			case error: // 异常
				mBg.setVisibility(View.VISIBLE);
				mLoading.setVisibility(View.GONE);
				break;
			case showData: // 显示数据
				setVisibility(View.GONE);
				if (mHidVs != null) {
					for (View v : mHidVs) {
						v.setVisibility(View.VISIBLE);
					}
				}
				break;
			}
		}
	}

	public Status getStatus() {
		return mStu;
	}

	/**
	 * 设置加载时隐藏的view
	 */
	public void setHidenOtherView(View... v) {
		mHidVs = v;
	}

	@Override
	protected void onDetachedFromWindow() {
		mLis = null;
		if (mHidVs != null) {
			for (View v : mHidVs) {
				// ((ListView)v).removeAllViews();
				v.setVisibility(View.GONE);
				v = null;
			}
		}
		mHidVs = null;
		mStu = null;
		removeAllViews();
		ButterKnife.bind(this);
		super.onDetachedFromWindow();
	}
}
