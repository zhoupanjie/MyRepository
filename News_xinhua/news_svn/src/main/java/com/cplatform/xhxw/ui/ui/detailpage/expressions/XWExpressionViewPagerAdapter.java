package com.cplatform.xhxw.ui.ui.detailpage.expressions;

import java.util.List;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;

public class XWExpressionViewPagerAdapter extends PagerAdapter {
	private List<GridView> mExpressionsGroups;
	private Context con;

	public XWExpressionViewPagerAdapter(Context con, List<GridView> mExpressions) {
		super();
		this.mExpressionsGroups = mExpressions;
		this.con = con;
	}
	
	@Override
	public int getCount() {
		return mExpressionsGroups.size();
	}

	@Override
	public boolean isViewFromObject(View arg0, Object arg1) {
		return arg0 == arg1;
	}

	@Override
	public void destroyItem(ViewGroup container, int position, Object object) {
		((ViewPager) container).removeView(mExpressionsGroups.get(position));
	}

	@Override
	public int getItemPosition(Object object) {
		return super.getItemPosition(object);
	}

	@Override
	public Object instantiateItem(ViewGroup container, int position) {
		((ViewPager) container).addView(mExpressionsGroups.get(position), 0);
		return mExpressionsGroups.get(position);
	}
	
}
