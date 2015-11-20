package com.cplatform.xhxw.ui.ui.main.cms.radiobroadcast;

import java.util.ArrayList;
import java.util.List;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import butterknife.ButterKnife;
import butterknife.InjectView;

import com.cplatform.xhxw.ui.R;
import com.cplatform.xhxw.ui.model.New;
import com.cplatform.xhxw.ui.ui.base.BaseFragment;
import com.cplatform.xhxw.ui.ui.main.HomeActivity;
import com.cplatform.xhxw.ui.ui.main.cms.game.GameUtil;
import com.google.gson.Gson;

/**
 * 
 * @ClassName RadioBroadcastFragment
 * @Description TODO 广播Fragment
 * @Version 1.0
 * @Author zxe
 * @Creation 2015年8月18日 下午8:00:20
 * @Mender zxe
 * @Modification 2015年8月18日 下午8:00:20
 * @Copyright Copyright © 2013 - 2015 Channelsoft (Beijing) Technology Co.,
 *            Ltd.All Rights Reserved.
 * 
 */
public class RadioBroadcastFragment extends BaseFragment {
	public static ViewPager mViewPage;
	@InjectView(R.id.viewGroup)
	ViewGroup mViewGroup;
	@InjectView(R.id.iv_seeting)
	ImageView ivSetting;
	public static TextView tvTitle;
	private static Context con;
	private FragAdapter adapter;
	public static RadioPlayer player;// 广播管理
	public static InterfaceRadioPlayControl irpc;

	@SuppressLint("NewApi")
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.fragment_radiobroadcast,
				container, false);
		ButterKnife.inject(this, rootView);
		con = this.getActivity();
		tvTitle = (TextView) rootView.findViewById(R.id.tv_title);
		mViewPage = (ViewPager) rootView.findViewById(R.id.viewPager);
		ViewPagerScroller scroller = new ViewPagerScroller(con);
		scroller.setScrollDuration(1500);
		scroller.initViewPagerScroll(mViewPage);
		return rootView;
	}

	@SuppressWarnings("deprecation")
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		RadioBroadcastUtil.getRadioSettingInfo(con);
		List<String> fragments = new ArrayList<String>();
		fragments.add(RadioBroadcastUtil.TAG_FRAGMENT_PLAY);
		fragments.add(RadioBroadcastUtil.TAG_FRAGMENT_BROADCASTLIST);
		player = new RadioPlayer(con);
		adapter = new FragAdapter(this.getActivity()
				.getSupportFragmentManager(), fragments);
		mViewPage.setAdapter(adapter);
		if (Integer.parseInt(Build.VERSION.SDK) >= 9) {
			mViewPage.setOverScrollMode(View.OVER_SCROLL_NEVER);
		}
		initViewGroup(mViewPage, fragments.size(), mViewGroup);
		ivSetting.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				startActivity(RadioBroadcastSettingActivity.getIntent(mAct));
			}
		});
	}

	/**
	 * 
	 * @Name setAutoPlay
	 * @Description TODO //设置自动播放
	 * @return void
	 * @Author zxe
	 * @Date 2015年9月6日 上午11:23:43
	 * 
	 */
	public static void setAutoPlay() {
		if (HomeActivity.isPlayRadio
				&& (!TextUtils.isEmpty(HomeActivity.radioContent))) {
			New n = new Gson().fromJson(HomeActivity.radioContent, New.class);
			if (n != null) {
				DataRadioBroadcast drb = new DataRadioBroadcast();
				drb.setAudioid(n.getNewsId());
				drb.setName(n.getTitle());
				drb.setUrl(n.getVideourl());
				player.setCurDataRadioBroadcast(drb);
				if (GameUtil.isConnect(con)) {
					player.updateUI(RadioBroadcastUtil.STATE_RADIO_PLAY_AUTO);
				} else {
					RadioBroadcastUtil.showTipInformationDialog(con,
							R.string.text_play_tips, R.string.text_no_network);
				}
			}
		}
	}

	private void initViewGroup(ViewPager viewPager, int pageSize,
			ViewGroup viewGroup) {
		final ImageView[] imageViews = new ImageView[pageSize];
		for (int i = 0; i < pageSize; i++) {
			ImageView image = new ImageView(con);
			imageViews[i] = image;
			if (i == 0) {
				image.setImageResource(R.drawable.icon_radiobroadcast_tab_one_select);
			} else {
				image.setImageResource(R.drawable.icon_radiobroadcast_tab_last_default);
			}
			LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
					dp2Px(con, 15), dp2Px(con, 15));
			// params.setMargins(dp2Px(con, 10), 0, dp2Px(con, 10), 0);
			viewGroup.addView(image, params);
		}

		viewPager.setOnPageChangeListener(new OnPageChangeListener() {

			@Override
			public void onPageScrollStateChanged(int arg0) {

			}

			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {

			}

			@Override
			public void onPageSelected(int arg0) {
				// for (int i = 0; i < imageViews.length; i++) {
				Log.e("切换tab", arg0 + "");
				if (arg0 == 0) {
					imageViews[arg0 + 1]
							.setImageResource(R.drawable.icon_radiobroadcast_tab_last_default);
					imageViews[arg0]
							.setImageResource(R.drawable.icon_radiobroadcast_tab_one_select);
				} else {
					imageViews[0]
							.setImageResource(R.drawable.icon_radiobroadcast_tab_one_default);
					imageViews[arg0]
							.setImageResource(R.drawable.icon_radiobroadcast_tab_last_select);
				}
				// imageViews[arg0]
				// .setImageResource(R.drawable.icon_radiobroadcast_select);
				// if (arg0 != i) {
				// imageViews[i]
				// .setImageResource(R.drawable.icon_radiobroadcast_default);
				// }
				// }
			}
		});
	}

	// dp 转成 px
	public static int dp2Px(Context context, int dpValue) {
		final int scale = (int) context.getResources().getDisplayMetrics().density;
		return (int) (dpValue * scale + 0.5);
	}

	@Override
	public void onAttach(Activity activity) {
		// TODO Auto-generated method stub
		super.onAttach(activity);
		Log.d("RadioBroadcastFragment", "onAttach");
	}

	@Override
	public void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		Log.d("RadioBroadcastFragment", "onPause");
	}

	@Override
	public void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
		Log.d("RadioBroadcastFragment", "onStart");
	}

	@Override
	public void onStop() {
		// TODO Auto-generated method stub
		super.onStop();
		Log.d("RadioBroadcastFragment", "onStop");
	}

	@Override
	public void onResume() {
		super.onResume();
		Log.d("RadioBroadcastFragment", "onResume");
	}

	@Override
	public void onHiddenChanged(boolean hidden) {
		// TODO Auto-generated method stub
		super.onHiddenChanged(hidden);
		if (!hidden) {// 若显示该界面，则设置自动播放
			RadioBroadcastFragment.switchTab(0);
			// TODO Auto-generated method stub
			setAutoPlay();
		}
	}

	@Override
	public void onDestroyView() {
		// if (mReceiver != null) {
		// LocalBroadcastManager.getInstance(getActivity())
		// .unregisterReceiver(mReceiver);
		// mReceiver = null;
		// }
		// if (mTabbar != null) {
		// mTabbar.removeAllViews();
		// }
		// if (showData != null) {
		// showData.clear();
		// }
		// if (hideData != null) {
		// hideData.clear();
		// }
		ButterKnife.reset(this);
		player.stop();
		super.onDestroyView();
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		// LogUtil.e(TAG, "fragment request-------" + resultCode);
		// if (resultCode == Activity.RESULT_OK) {
		// if (requestCode == REQUEST_CODE_SEARCH_CHANNEL) {
		// mManageView.setmChange(true);
		// initChannels();
		// }
		// }
		super.onActivityResult(requestCode, resultCode, data);
	}

	public static void switchTab(int item) {
		mViewPage.setCurrentItem(item);
	}

	public static InterfaceRadioPlayControl getIrpc() {
		return irpc;
	}

	public static void setIrpc(InterfaceRadioPlayControl irpci) {
		irpc = irpci;
	}

	/**
	 * 
	 * @Name OnPausePlay
	 * @Description TODO 暂停
	 * @return void
	 * @Author zxe
	 * @Date 2015年9月18日 下午1:57:13
	 * 
	 */
	public static void OnPausePlay() {
		if (irpc != null && irpc.isPlaying()) {
			irpc.onRadioPause();
		}
	}

	/**
	 * 
	 * @Name OnResumePlay
	 * @Description TODO 重播
	 * @return void
	 * @Author zxe
	 * @Date 2015年9月18日 下午1:57:26
	 * 
	 */
	public static void OnResumePlay() {
		if (irpc != null && (false == irpc.isPlaying())) {
			irpc.onRadioResume();
		}
	}
}
