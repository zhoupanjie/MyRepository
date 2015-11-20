package com.hy.superemsg.activity;

import com.hy.superemsg.R;
import com.hy.superemsg.components.UITitle;
import com.hy.superemsg.rsp.Category;
import com.hy.superemsg.viewpager.ScrollTabsViewPager;
import com.hy.superemsg.viewpager.fragments.HolidayDetailFragment;
import com.hy.superemsg.viewpager.fragments.SmsListFragment;

import android.app.Fragment;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.Scroller;

public class HolidayDetailActivity extends FragmentActivity{
	private ScrollTabsViewPager pager;
private Category category;
	@Override
	protected void onCreate(Bundle arg0) {
		setContentView(R.layout.activity_holiday_detail);
		super.onCreate(arg0);
		category = getIntent().getExtras().getParcelable("categoryid");
		pager = (ScrollTabsViewPager)findViewById(R.id.viewPaper_holiday);
		initView();
	}
private void initView() {
	UITitle title = (UITitle) this.findViewById(R.id.ui_title);
	if (title != null) {
		title.setTitleText(category.categoryname);
		title.setLeftButton(R.drawable.back, new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				finish();
			}
		});
	}
	HolidayDetailFragment fragment = new HolidayDetailFragment();
	Bundle b = new Bundle();
	b.putParcelable("categoryid", category);
	fragment.setArguments(b);
	pager.addPager(category.categoryname, fragment);
	pager.setUp(HolidayDetailActivity.this);
}
}
