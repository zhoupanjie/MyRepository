package com.cplatform.xhxw.ui.ui.main.forelanguage.activity;

import java.util.ArrayList;
import java.util.List;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.cplatform.xhxw.ui.Constants;
import com.cplatform.xhxw.ui.R;
import com.cplatform.xhxw.ui.StatisticalKey;
import com.cplatform.xhxw.ui.db.ChanneDB;
import com.cplatform.xhxw.ui.db.dao.ChanneDao;
import com.cplatform.xhxw.ui.ui.base.BaseFragment;
import com.cplatform.xhxw.ui.ui.base.view.PagerSlidingTabStrip;
import com.cplatform.xhxw.ui.ui.main.cms.NewsFragment;
import com.cplatform.xhxw.ui.util.Actions;
import com.cplatform.xhxw.ui.util.DateUtil;
import com.cplatform.xhxw.ui.util.ListUtil;
import com.umeng.analytics.MobclickAgent;
import com.viewpagerindicator.TabPageIndicator;
import com.wbtech.ums.UmsAgent;

public class ForeignLanguageHomeFragment extends BaseFragment {
	/**
	 * Tab标题
	 */
	private static final ArrayList<ChanneDao> showData = new ArrayList<ChanneDao>();

	private TabPageIndicatorAdapter mAdapter;
	private PagerSlidingTabStrip mIndicator;
	private ViewPager mViewPager;
	private boolean isTabItemClick; // 判断栏目切换是否为点击
	private Receiver mReceiver;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = inflater.inflate(
				R.layout.fragment_foreign_language_home, container, false);
		initViews(rootView);
		return rootView;
	}

	private void initViews(View rootView) {
		mIndicator = (PagerSlidingTabStrip) rootView
				.findViewById(R.id.foreign_lang_indicator);
		mViewPager = (ViewPager) rootView
				.findViewById(R.id.foreign_lang_viewpager);
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		initEvents();
	}

	private void initEvents() {
		mIndicator.setShouldExpand(true);
		mIndicator
				.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
					@Override
					public void onPageScrolled(int i, float v, int i2) {
					}

					@Override
					public void onPageSelected(int i) {
						// 判断栏目切换是否为滑动
						isTabItemClick = false;
						String channelId = showData.get(i).getChannelID();
						Intent intent = new Intent(
								Actions.ACTION_CHANNEL_RELOAD_CHECK_TIME);
						intent.putExtra("channelid", channelId);
						LocalBroadcastManager.getInstance(getActivity())
								.sendBroadcast(intent);
						if (!isTabItemClick) {
							MobclickAgent.onEvent(getActivity(),
									StatisticalKey.channel_slide);
							UmsAgent.onEvent(
									getActivity(),
									StatisticalKey.channel_slide,
									new String[] { StatisticalKey.key_channelid },
									new String[] { channelId });
						}
					}

					@Override
					public void onPageScrollStateChanged(int i) {
					}
				});
		loadData();
		if (mReceiver != null) {
			LocalBroadcastManager.getInstance(getActivity())
					.unregisterReceiver(mReceiver);
			mReceiver = null;
		}
		mReceiver = new Receiver();
		IntentFilter filter = new IntentFilter();
		filter.addAction(Actions.ACTION_SYNC_SYSTEM_CHANNE_DONE);
		filter.addAction(Actions.ACTION_ALL_CHANNEL_RELOAD);
		LocalBroadcastManager.getInstance(getActivity()).registerReceiver(
				mReceiver, filter);
	}

	/**
	 * 栏目内容适配器
	 */
	private class TabPageIndicatorAdapter extends FragmentStatePagerAdapter {
		public TabPageIndicatorAdapter(FragmentManager fm) {
			super(fm);
		}

		@Override
		public NewsFragment getItem(int position) {
			int index = position % showData.size();
			// 新建一个Fragment来展示栏目的内容，并传递栏目id参数
			NewsFragment fragment = new NewsFragment();
			Bundle bundle = new Bundle();
			bundle.putString("channelid", showData.get(index).getChannelID());
			fragment.setArguments(bundle);
			return fragment;
		}

		@Override
		public CharSequence getPageTitle(int position) {
			int index = position % showData.size();
			return showData.get(index).getChannelName();
		}

		@Override
		public int getCount() {
			return showData.size();
		}
	}

	private class Receiver extends BroadcastReceiver {

		@Override
		public void onReceive(Context context, Intent intent) {
			String action = intent.getAction();
			// 用户栏目同步完成
			if (Actions.ACTION_SYNC_SYSTEM_CHANNE_DONE.equals(action)) {
				boolean isChange = intent.getBooleanExtra("ischange", false);
				if (isChange) {
					loadData();
				}
			} else if (Actions.ACTION_ALL_CHANNEL_RELOAD.equals(action)) {
				if (ListUtil.isEmpty(showData)) {
					return;
				}
				// 大于三分钟
				if (DateUtil.getTime() - Constants.getsOldNewsLoadTime() < 180000) {
					return;
				}

				String chId = showData.get(0).getChannelID();
				Intent intent1 = new Intent(Actions.ACTION_CHANNE_RESELECTED);
				intent1.putExtra("channelid", chId);
				LocalBroadcastManager.getInstance(getActivity()).sendBroadcast(
						intent1);
			}
		}
	}

	public void loadData() {
		List<ChanneDao> showList = ChanneDB.getShowChanne(mAct,
				Constants.getDbUserId());
		showData.clear();
		if (!ListUtil.isEmpty(showList)) {
			showData.addAll(showList);
		}

		try {
			mAdapter = new TabPageIndicatorAdapter(getFragmentManager());
			mViewPager.setAdapter(mAdapter);

			mIndicator.setViewPager(mViewPager);
			mIndicator.notifyDataSetChanged();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
