package com.cplatform.xhxw.ui.ui.picShow;

import com.cplatform.xhxw.ui.R;
import com.cplatform.xhxw.ui.ui.base.widget.DefaultView.Status;

import butterknife.ButterKnife;
import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;

/**
 * Hacky fix for Issue #4 and
 * http://code.google.com/p/android/issues/detail?id=18990
 * 
 * ScaleGestureDetector seems to mess up the touch events, which means that
 * ViewGroups which make use of onInterceptTouchEvent throw a lot of
 * IllegalArgumentException: pointerIndex out of range.
 * 
 * There's not much I can do in my code for now, but we can mask the result by
 * just catching the problem and ignoring it.
 * 
 * @author Chris Banes
 */
public class HackyViewPager extends ViewPager {

	public HackyViewPager(Context context) {
		super(context);
		 this.init();
	}
	
    public HackyViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.init();
    }

//    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
//    public HackyViewPager(Context context, AttributeSet attrs, int defStyle) {
//        super(context, attrs, defStyle);
//        this.init();
//    }
    private void init() {
        ButterKnife.inject(this);
    }

	@Override
	public boolean onInterceptTouchEvent(MotionEvent ev) {
		try {
			return super.onInterceptTouchEvent(ev);
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
			return false;
		}
	}

}
