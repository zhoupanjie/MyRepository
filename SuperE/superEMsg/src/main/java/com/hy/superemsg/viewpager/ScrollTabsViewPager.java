package com.hy.superemsg.viewpager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.R.anim;
import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewManager;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hy.superemsg.R;
import com.hy.superemsg.utils.CommonUtils;
import com.hy.superemsg.utils.MobileInfo;

public class ScrollTabsViewPager extends LinearLayout {
	private LinearLayout tabContainer;
	private ViewPager pager;
	private List<String> tabContents;
	private List<AbsFragment> fragments;
	private HorizontalScrollView scroll;

	public ScrollTabsViewPager(Context context) {
		super(context, null);
	}

	public ScrollTabsViewPager(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
	}

	private void setTabSelection(int index) {
		for (int i = 0; i < tabContainer.getChildCount(); i++) {
			tabContainer.getChildAt(i).setSelected(false);
		}
		DisplayMetrics dm = this.getResources().getDisplayMetrics();
		View selected = tabContainer.getChildAt(index);
		selected.setSelected(true);
		int offset = 0;
		int scrollx = scroll.getScrollX();

		if (lastPagerIndex < index) {
			// to right
			offset = selected.getRight() - scrollx - dm.widthPixels;
			if (offset > 0) {
				scroll.smoothScrollBy(offset, 0);
			}
		} else if (lastPagerIndex > index) {
			// to left
			offset = selected.getLeft() - scrollx;
			if (offset < 0) {
				scroll.smoothScrollBy(offset, 0);
			}
		}
		lastPagerIndex = index;
	}

	private int lastPagerIndex = -1;

	private void init() {
		DisplayMetrics metrics = new DisplayMetrics();
		((Activity) getContext()).getWindowManager().getDefaultDisplay()
				.getMetrics(metrics);
		LayoutInflater.from(getContext()).inflate(R.layout.viewpager_layout,
				this);
		tabContainer = (LinearLayout) this.findViewById(R.id.tabs);
		scroll = (HorizontalScrollView) this.findViewById(R.id.tab_scroll);
		pager = (ViewPager) this.findViewById(R.id.pager);
		pager.setOnPageChangeListener(new OnPageChangeListener() {

			@Override
			public void onPageSelected(int position) {
				AbsFragment fragment = fragments.get(position);
				fragment.resetUI();
				setTabSelection(position);

			}

			@Override
			public void onPageScrolled(int position, float positionOffset,
					int positionOffsetPixels) {

			}

			@Override
			public void onPageScrollStateChanged(int state) {
			}
		});
	}

	public AbsFragment getCurrentSelectedFragment() {
		if (CommonUtils.isNotEmpty(fragments)) {
			if (lastPagerIndex >= 0 && lastPagerIndex < fragments.size()) {
				return fragments.get(lastPagerIndex);
			}
		}
		return null;
	}

	public List<AbsFragment> getAllFragments() {
		return fragments;
	}

	public ScrollTabsViewPager addPager(String title, AbsFragment fragment) {
		if (fragments == null) {
			fragments = new ArrayList<AbsFragment>();
		}
		if (tabContents == null) {
			tabContents = new ArrayList<String>();
		}
		tabContents.add(title);
		Bundle b = fragment.getArguments();
		if (b == null) {
			b = new Bundle();
		}
		b.putString("title", title);
		fragment.setArguments(b);
		fragments.add(fragment);
		return this;
	}

	public void setOffScreenLimit(int off) {
		pager.setOffscreenPageLimit(off);
	}

	public void setUp(FragmentActivity act) {
		ViewPagerFragmentAdapter adapter = new ViewPagerFragmentAdapter(
				act.getSupportFragmentManager());
		adapter.setFragments(fragments);
		pager.setAdapter(adapter);
		pager.setOffscreenPageLimit(3);
		List<Map<String, String>> tabs = new ArrayList<Map<String, String>>();
		for (int i = 0; i < tabContents.size(); i++) {
			String tab = tabContents.get(i);
			Map<String, String> data = new HashMap<String, String>();
			data.put("title", tab);
			tabs.add(data);
			TextView tv = (TextView) LayoutInflater.from(getContext()).inflate(
					R.layout.item_tab_text, null);
			// tv.setTextColor(this.getResources().getColorStateList(R.color.tab_text));
			tv.setTag(Integer.valueOf(i));

			tv.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View v) {
					Integer index = (Integer) v.getTag();
					setTabSelection(index);
					pager.setCurrentItem(index);
				}
			});
			tv.setText(tab);
			int minWidth = (int) (tv.getPaint().getTextSize()
					* tv.getText().toString().length() + tv.getPaddingLeft() + tv
					.getPaddingRight());
			int expectedWidth = MobileInfo.getScreenWidth(getContext())
					/ tabContents.size();
			expectedWidth = Math.max(minWidth, expectedWidth);
			LinearLayout.LayoutParams lp = new LayoutParams(expectedWidth,
					LayoutParams.FILL_PARENT, 1);
			tabContainer.addView(tv, lp);
		}
		if (tabContainer.getChildCount() == 1) {
			android.view.ViewGroup.LayoutParams lParams = tabContainer
					.getLayoutParams();
			lParams.height = 0;
		}
		lastPagerIndex = 0;
		setTabSelection(0);
	}
}
