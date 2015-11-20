package com.cplatform.xhxw.ui.ui.main.cms;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
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
import android.widget.LinearLayout;
import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

import com.cplatform.xhxw.ui.Constants;
import com.cplatform.xhxw.ui.R;
import com.cplatform.xhxw.ui.StatisticalKey;
import com.cplatform.xhxw.ui.db.ChanneDB;
import com.cplatform.xhxw.ui.db.dao.ChanneDao;
import com.cplatform.xhxw.ui.receiver.StartServiceReceiver;
import com.cplatform.xhxw.ui.ui.apptips.TipsActivity;
import com.cplatform.xhxw.ui.ui.apptips.TipsUtil;
import com.cplatform.xhxw.ui.ui.base.BaseFragment;
import com.cplatform.xhxw.ui.ui.base.view.ChannelManageView;
import com.cplatform.xhxw.ui.ui.base.view.PagerSlidingTabStrip;
import com.cplatform.xhxw.ui.ui.main.HomeActivity;
import com.cplatform.xhxw.ui.ui.main.cms.channelsearch.ChannelSearchActivity;
import com.cplatform.xhxw.ui.ui.main.cms.game.GameFragment;
import com.cplatform.xhxw.ui.ui.main.cms.game.GameUtil;
import com.cplatform.xhxw.ui.util.Actions;
import com.cplatform.xhxw.ui.util.DateUtil;
import com.cplatform.xhxw.ui.util.ListUtil;
import com.cplatform.xhxw.ui.util.LogUtil;
import com.umeng.analytics.MobclickAgent;
import com.wbtech.ums.UmsAgent;

/**
 * 主视图 Created by cy-love on 13-12-24.
 */
public class HomeFragment extends BaseFragment implements
		ChannelManageView.OnChanneChangeListener {

	public static final int REQUEST_CODE_SEARCH_CHANNEL = 101;
	public static final int REQUEST_CODE_ADD_CHANNEL = 102;

	private static final String TAG = HomeFragment.class.getSimpleName();
	/**
	 * Tab标题
	 */
	private static final ArrayList<ChanneDao> showData = new ArrayList<ChanneDao>();
	private static final ArrayList<ChanneDao> hideData = new ArrayList<ChanneDao>();

	private TabPageIndicatorAdapter mAdapter;
	@InjectView(R.id.saas_my_record_tabbar)
	PagerSlidingTabStrip mTabbar;
	@InjectView(R.id.saas_my_record_tabbar_layout)
	LinearLayout mTabbarLayout;
	@InjectView(R.id.pager)
	ViewPager mViewPage;
	@InjectView(R.id.ly_channel_manage_view)
	public ChannelManageView mManageView;
	// private boolean isTabItemClick; // 判断栏目切换是否为点击
	private Receiver mReceiver;

	public static String currentShowChannel = null;
	private boolean isLoad = false;
	private static Context con;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.fragment_home, container,
				false);
		ButterKnife.inject(this, rootView);
		con = this.getActivity();
		return rootView;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		// mTabbar.setOnTabReselectedListener(new
		// TabPageIndicator.OnTabReselectedListener() {
		// @Override
		// public void onTabReselected(int position) {
		// String chId = showData.get(position).getChannelID();
		// Intent intent = new Intent(Actions.ACTION_CHANNE_RESELECTED);
		// intent.putExtra("channelid", chId);
		// LocalBroadcastManager.getInstance(getActivity()).sendBroadcast(intent);
		// }
		// });
		// mTabbar.setOnTabItemClickListener(new
		// TabPageIndicator.OnTabItemClickListener() {
		// @Override
		// public void onTabItemClick(int position) {
		// MobclickAgent.onEvent(getActivity(), StatisticalKey.channel_onclick);
		// isTabItemClick = true;
		// }
		// });
		mTabbar.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
			@Override
			public void onPageScrolled(int i, float v, int i2) {
			}

			@Override
			public void onPageSelected(int i) {
				String channelId = showData.get(i).getChannelID();
				// 判断栏目切换是否为滑动
				mTabbar.isTabItemClick = false;
				currentShowChannel = channelId;
				Intent intent = new Intent(
						Actions.ACTION_CHANNEL_RELOAD_CHECK_TIME);
				intent.putExtra("channelid", channelId);
				LocalBroadcastManager.getInstance(getActivity()).sendBroadcast(
						intent);
				if (!mTabbar.isTabItemClick) {
					MobclickAgent.onEvent(getActivity(),
							StatisticalKey.channel_slide);
					UmsAgent.onEvent(getActivity(),
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
		filter.addAction(Actions.ACTION_SYNC_SYSTEM_CHANNE_MUST);
		LocalBroadcastManager.getInstance(getActivity()).registerReceiver(
				mReceiver, filter);
	}

	@Override
	public void onResume() {
		initChannels();
		super.onResume();
	}

	public void loadData() {
		initChannels();
		mAdapter = new TabPageIndicatorAdapter(getFragmentManager());
		mViewPage.setAdapter(mAdapter);
		mTabbar.setShouldExpand(true);
		// mTabbar.setIndicatorColor(Color.rgb(255, 0, 0));
		mTabbar.setViewPager(mViewPage);
		mTabbar.notifyDataSetChanged();
		// mTabbar.setCurrentItem(0);
	}

	private void initChannels() {
		List<ChanneDao> showList = ChanneDB.getShowChanne(mAct,
				Constants.getDbUserId());
		List<ChanneDao> hideList = ChanneDB.getHideChanne(mAct,
				Constants.getDbUserId());
		mTabbar.setInHome(true);
		mTabbar.setShowList(showList);
		showData.clear();
		hideData.clear();
		if (!ListUtil.isEmpty(showList)) {
			showData.addAll(showList);
		}
		if (!ListUtil.isEmpty(hideList)) {
			hideData.addAll(hideList);
		}
		mManageView.setOnChangeListener(this);
		mManageView.setShow(showData);
		mManageView.setHide(hideData);
		if (mAdapter != null) {
			mAdapter.notifyDataSetChanged();
		}
	}

	@Override
	public void onChanneChage(boolean isChange) {
		if (mAct instanceof HomeActivity) {
			HomeActivity act = (HomeActivity) mAct;
			act.showFlag();
		}
		if (!isChange) {
			return;
		}
		loadData();

		Intent intent = new Intent(getActivity(), StartServiceReceiver.class);
		intent.setAction(StartServiceReceiver.ACTION_SYNC_SERVICE_START);
		getActivity().sendBroadcast(intent);
	}

	/**
	 * 栏目内容适配器
	 */
	private class TabPageIndicatorAdapter extends FragmentStatePagerAdapter {
		public TabPageIndicatorAdapter(FragmentManager fm) {
			super(fm);
		}

		@Override
		public BaseFragment getItem(int position) {
			int index = position % showData.size();
			String channelid = showData.get(index).getChannelID();
			if (GameUtil.isGameChannel(channelid)) {
				GameFragment fragment = new GameFragment();
				Bundle bundle = new Bundle();
				bundle.putString("channelid", channelid);
				fragment.setArguments(bundle);
				return fragment;
			} else {
				// 新建一个Fragment来展示栏目的内容，并传递栏目id参数
				NewsFragment fragment = new NewsFragment();
				Bundle bundle = new Bundle();
				bundle.putString("channelid", channelid);
				fragment.setArguments(bundle);
				return fragment;
			}

		}

		@Override
		public CharSequence getPageTitle(int position) {
			int index = position % showData.size();
			return showData.get(index).getChannelName();
		}

		public String getChannelId(int position) {
			int index = position % showData.size();
			return showData.get(index).getChannelID();
		}

		@Override
		public int getCount() {
			return showData.size();
		}
	}

	@OnClick(R.id.iv_addchannel)
	public void addChannelAction() {
		MobclickAgent.onEvent(getActivity(), StatisticalKey.channel_edit);

		UmsAgent.onEvent(getActivity(), StatisticalKey.channel_edit);
		if (mAct instanceof HomeActivity) {
			HomeActivity act = (HomeActivity) mAct;
			act.hideFlag();
		}
		initChannels();
		mManageView.setMyVisibility(View.VISIBLE);
		if (!TipsUtil.isTipShown(TipsUtil.TIP_CHANNEL_SEARCH)) {
			startActivity(TipsActivity.getTipIntent(mAct,
					TipsUtil.TIP_CHANNEL_SEARCH));
		}
	}

	@Override
	public void onDestroyView() {
		if (mReceiver != null) {
			LocalBroadcastManager.getInstance(getActivity())
					.unregisterReceiver(mReceiver);
			mReceiver = null;
		}
		if (mTabbar != null) {
			mTabbar.removeAllViews();
		}
		if (showData != null) {
			showData.clear();
		}
		if (hideData != null) {
			hideData.clear();
		}
		ButterKnife.reset(this);
		super.onDestroyView();
	}

	private class Receiver extends BroadcastReceiver {

		@Override
		public void onReceive(Context context, Intent intent) {
			String action = intent.getAction();
			// 用户栏目同步完成
			if (Actions.ACTION_SYNC_SYSTEM_CHANNE_DONE.equals(action)) {
				boolean isChange = intent.getBooleanExtra("ischange", false);
				if (isLoad || isChange) {
					loadData();
					isLoad = false;
				}
			} else if (Actions.ACTION_ALL_CHANNEL_RELOAD.equals(action)) {
				if (ListUtil.isEmpty(showData)) {
					return;
				}
				// 大于三分钟
				if (DateUtil.getTime() - Constants.getsOldNewsLoadTime() < 180000) {
					return;
				}

				// 选中首个栏目
				// if (mTabbar.getSelectedTabIndex() != 0) {
				// mTabbar.setCurrentItem(0);
				// }
				String chId = showData.get(0).getChannelID();
				Intent intent1 = new Intent(Actions.ACTION_CHANNE_RESELECTED);
				intent1.putExtra("channelid", chId);
				LocalBroadcastManager.getInstance(getActivity()).sendBroadcast(
						intent1);
			} else if (Actions.ACTION_SYNC_SYSTEM_CHANNE_MUST.equals(action)) {
				isLoad = true;
			}
		}
	}

	@Override
	public void onSearchBtnClick() {
		startActivityForResult(new Intent(mAct, ChannelSearchActivity.class),
				REQUEST_CODE_SEARCH_CHANNEL);
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		LogUtil.e(TAG, "fragment request-------" + resultCode);
		if (resultCode == Activity.RESULT_OK) {
			if (requestCode == REQUEST_CODE_SEARCH_CHANNEL) {
				mManageView.setmChange(true);
				initChannels();
			}
		}
		super.onActivityResult(requestCode, resultCode, data);
	}

	public void setTabbarShow(boolean isShow) {
		if (mTabbarLayout != null) {
			if (isShow) {
				mTabbarLayout.setVisibility(View.VISIBLE);
			} else {
				mTabbarLayout.setVisibility(View.GONE);
			}
		}
	}
}