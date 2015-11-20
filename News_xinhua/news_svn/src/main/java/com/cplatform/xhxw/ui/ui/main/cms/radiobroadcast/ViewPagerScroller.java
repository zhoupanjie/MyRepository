package com.cplatform.xhxw.ui.ui.main.cms.radiobroadcast;

import java.lang.reflect.Field;
import android.annotation.SuppressLint;
import android.content.Context;
import android.support.v4.view.ViewPager;
import android.view.animation.Interpolator;
import android.widget.Scroller;
/**
 * 
 * @ClassName ViewPagerScroller 
 * @Description TODO ViewPage 滑动切换时间
 * @Version 1.0
 * @Author zxe
 * @Creation 2015年9月23日 上午10:33:42 
 * @Mender zxe
 * @Modification 2015年9月23日 上午10:33:42 
 * @Copyright Copyright © 2013 - 2015 Channelsoft (Beijing) Technology Co., Ltd.All Rights Reserved.
*
 */
public class ViewPagerScroller extends Scroller {
	private int mScrollDuration = 2000; // 滑动时间

	/**
	 * 设置速度速度
	 * 
	 * @param duration
	 */
	public void setScrollDuration(int duration) {
		this.mScrollDuration = duration;
	}

	public ViewPagerScroller(Context context) {
		super(context);
	}

	public ViewPagerScroller(Context context, Interpolator interpolator) {
		super(context, interpolator);
	}

	@SuppressLint("NewApi")
	public ViewPagerScroller(Context context, Interpolator interpolator,
			boolean flywheel) {
		super(context, interpolator, flywheel);
	}

	@Override
	public void startScroll(int startX, int startY, int dx, int dy, int duration) {
		super.startScroll(startX, startY, dx, dy, mScrollDuration);
	}

	@Override
	public void startScroll(int startX, int startY, int dx, int dy) {
		super.startScroll(startX, startY, dx, dy, mScrollDuration);
	}

	public void initViewPagerScroll(ViewPager viewPager) {
		try {
			Field mScroller = ViewPager.class.getDeclaredField("mScroller");
			mScroller.setAccessible(true);
			mScroller.set(viewPager, this);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
