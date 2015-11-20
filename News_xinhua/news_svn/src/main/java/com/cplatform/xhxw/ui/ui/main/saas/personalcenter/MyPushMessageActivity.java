package com.cplatform.xhxw.ui.ui.main.saas.personalcenter;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;

import com.cplatform.xhxw.ui.R;
import com.cplatform.xhxw.ui.ui.base.BaseActivity;
import com.cplatform.xhxw.ui.ui.base.view.PagerSlidingTabStrip;
import com.cplatform.xhxw.ui.ui.main.cms.SystemMsgShowFragment;
import com.cplatform.xhxw.ui.ui.main.saas.PushMessagesFragment;

public class MyPushMessageActivity extends BaseActivity {
	private PagerSlidingTabStrip mTabbar;
	private ViewPager mViewPager;
	private SectionsPagerAdapter mVPAdapter;
	
	private String[] mTabNames = new String[]{"要闻消息", "系统消息"};
	@Override
	protected String getScreenName() {
		return null;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.saas_activity_my_push_msg);
		initActionBar();
		initViews();
	}

	private void initViews() {
		mTabbar = (PagerSlidingTabStrip) findViewById(R.id.saas_my_pushmsg_tabbar);
		mViewPager = (ViewPager) findViewById(R.id.saas_my_pushmsg_vp);
		mVPAdapter = new SectionsPagerAdapter(getSupportFragmentManager());
		mViewPager.setAdapter(mVPAdapter);
		
		mTabbar.setShouldExpand(true);
		mTabbar.setIndicatorColor(Color.rgb(65, 105, 225));
		mTabbar.setViewPager(mViewPager);
	}

	@Override
	protected void onResume() {
		super.onResume();
	}
	
	private class SectionsPagerAdapter extends FragmentPagerAdapter {

		public SectionsPagerAdapter(FragmentManager fm) {
			super(fm);
		}

		@Override
		public Fragment getItem(int arg0) {
			Fragment frag = null;
			if(getPageTitle(arg0).equals(mTabNames[0])) {
				frag = new PushMessagesFragment();
				Bundle bundle = new Bundle();
		    	bundle.putBoolean("isEnterprise", false);
		    	frag.setArguments(bundle);
			} else {
				frag = new SystemMsgShowFragment();
			}
			return frag;
		}

		@Override
		public int getCount() {
			return mTabNames.length;
		}

		@Override
		public CharSequence getPageTitle(int position) {
			return mTabNames[position];
		}
	}
}
