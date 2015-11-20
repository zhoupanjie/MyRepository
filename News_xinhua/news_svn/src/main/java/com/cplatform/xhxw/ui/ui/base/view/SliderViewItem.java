package com.cplatform.xhxw.ui.ui.base.view;

import android.content.Context;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewParent;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import butterknife.ButterKnife;
import butterknife.Bind;

import com.cplatform.xhxw.ui.Constants;
import com.cplatform.xhxw.ui.R;
import com.cplatform.xhxw.ui.model.Focus;
import com.cplatform.xhxw.ui.util.DisplayImageOptionsUtil;
import com.nostra13.universalimageloader.core.ImageLoader;

/**
 * 单个焦点新闻 Created by cy-love on 13-12-25.
 */
public class SliderViewItem extends LinearLayout {

	@Bind(R.id.iv_img)
	ImageView mImg;
	@Bind(R.id.tv_title)
	TextView mTitle;
	private OnSliderImgOnClickListener mLis;
	private Focus mFocus;

	public SliderViewItem(Context context, OnSliderImgOnClickListener listener) {
		super(context);
		mLis = listener;
		this.init();
	}

	private void init() {
		LayoutInflater inflater = (LayoutInflater) getContext()
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		inflater.inflate(R.layout.view_slider_item, this);
		ButterKnife.bind(this);

		setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if (mLis != null) {
					mLis.onSliderImgOnClick(mFocus);
				}
			}
		});
		setViewPaperEnabled(false);
		initSliderHeight();
	}

	public void initSliderHeight() {
		LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
				Constants.screenWidth, getSliderHeight());
		mImg.setLayoutParams(lp);
		int w = View.MeasureSpec.makeMeasureSpec(0,
				View.MeasureSpec.UNSPECIFIED);
		int h = View.MeasureSpec.makeMeasureSpec(0,
				View.MeasureSpec.UNSPECIFIED);
		measure(w, h);
		int height = getMeasuredHeight();
		if (Constants.HomeSliderSize.title_height == 0) {
			Constants.HomeSliderSize.title_height = height;
		}
	}

	private int getSliderHeight() {
		double height = Constants.screenWidth * Constants.HomeSliderSize.height
				/ Constants.HomeSliderSize.width;
		return (int) Math.ceil(height) + 1;
	}

	public void setData(Focus item) {
		mFocus = item;
		if (TextUtils.isEmpty(item.getBigthumbnail())) {
			ImageLoader.getInstance().displayImage(item.getThumb(), mImg,
					DisplayImageOptionsUtil.newsSliderImgOptions);
		} else {
			ImageLoader.getInstance().displayImage(item.getBigthumbnail(),
					mImg, DisplayImageOptionsUtil.newsSliderImgOptions);
		}
		if (TextUtils.isEmpty(item.getTitle())) {
			mTitle.setText(item.getName());
		} else {
			mTitle.setText(item.getTitle());
		}

	}

	// 拦截 TouchEvent
	@Override
	public boolean onInterceptTouchEvent(MotionEvent arg0) {
		// TODO Auto-generated method stub
		return super.onInterceptTouchEvent(arg0);
		// return true;
	}

	// 处理 TouchEvent
	@Override
	public boolean onTouchEvent(MotionEvent arg0) {
		// TODO Auto-generated method stub
		// return true;
		return super.onTouchEvent(arg0);
	}

	// 因为这个执行的顺序是 父布局先得到 action_down的事件

	/**
	 * onInterceptTouchEvent(MotionEvent ev)方法，这个方法只有ViewGroup类有
	 * 如LinearLayout，RelativeLayout等 可以包含子View的容器的
	 * 
	 * 用来分发 TouchEvent 此方法 返回true 就交给本 View的 onTouchEvent处理 此方法 返回false
	 * 就交给本View的 onInterceptTouchEvent 处理
	 */
	@Override
	public boolean dispatchTouchEvent(MotionEvent ev) {

		// 让父类不拦截触摸事件就可以了。
		setViewPaperEnabled(false);
		// 当放开时,恢复父布局的监听
		if (ev.getAction() == KeyEvent.ACTION_UP) {
			setViewPaperEnabled(true);
		}
		return super.dispatchTouchEvent(ev);

	}

	public void setViewPaperEnabled(boolean isEnabled) {
		ViewParent p = this.getParent();
		// 防止父布局拦截滑动事件,截止到listview
		for (int i = 0; i < 6; i++) {
			if (p != null) {
				p = p.getParent();
			} else {
				break;
			}
		}
		setViewTouchEvent(p, !isEnabled);
	}

	public void setViewTouchEvent(ViewParent v, boolean isIntercept) {
		if (v != null) {
			v.requestDisallowInterceptTouchEvent(isIntercept);
			setViewTouchEvent(v.getParent(), isIntercept);
		}
	}
}
