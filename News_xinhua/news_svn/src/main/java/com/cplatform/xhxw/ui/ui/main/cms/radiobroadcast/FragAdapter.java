package com.cplatform.xhxw.ui.ui.main.cms.radiobroadcast;

import java.util.List;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
/**
 * 
 * @ClassName FragAdapter 
 * @Description TODO 广播ViewPager适配器
 * @Version 1.0
 * @Author zxe
 * @Creation 2015年9月23日 上午10:06:08 
 * @Mender zxe
 * @Modification 2015年9月23日 上午10:06:08 
 * @Copyright Copyright © 2013 - 2015 Channelsoft (Beijing) Technology Co., Ltd.All Rights Reserved.
*
 */
public class FragAdapter extends FragmentPagerAdapter {

	private List<String> fragments;

	public FragAdapter(FragmentManager fm) {
		super(fm);
	}

	public FragAdapter(FragmentManager fm, List<String> fragments) {
		super(fm);
		this.fragments = fragments;
	}

	@Override
	public Fragment getItem(int position) {
		if (fragments.get(position).equals(
				RadioBroadcastUtil.TAG_FRAGMENT_BROADCASTLIST)) {
			RadioBroadcastListFragment fBroadcastList = new RadioBroadcastListFragment();
			return fBroadcastList;
		} else if (fragments.get(position).equals(
				RadioBroadcastUtil.TAG_FRAGMENT_PLAY)) {
			RadioBroadcastPlayFragment fPlay = new RadioBroadcastPlayFragment();
			return fPlay;
		} else {
			RadioBroadcastListFragment fBroadcastList = new RadioBroadcastListFragment();
			return fBroadcastList;
		}
	}

	@Override
	public int getCount() {
		return fragments.size();
	}
}
