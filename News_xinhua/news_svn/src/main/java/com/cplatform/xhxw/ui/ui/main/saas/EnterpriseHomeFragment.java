package com.cplatform.xhxw.ui.ui.main.saas;

import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.cplatform.xhxw.ui.Constants;
import com.cplatform.xhxw.ui.R;
import com.cplatform.xhxw.ui.db.ChanneDB;
import com.cplatform.xhxw.ui.db.dao.ChanneDao;
import com.cplatform.xhxw.ui.ui.base.BaseFragment;
import com.cplatform.xhxw.ui.ui.base.view.EnterpriseChannelBar;
import com.cplatform.xhxw.ui.ui.base.view.EnterpriseChannelBar.onChannelClickLisenter;
import com.cplatform.xhxw.ui.ui.main.cms.NewsFragment;
import com.cplatform.xhxw.ui.ui.main.saas.contribute.ContributeActivity;
import com.cplatform.xhxw.ui.util.Actions;

public class EnterpriseHomeFragment extends BaseFragment  implements onChannelClickLisenter{
	private EnterpriseChannelBar mChannelBar;
	private ViewPager mViewPager;
	private TextView mEnterpriseTile;

	private List<ChanneDao> mEnterpriseChannels;
	
	private int mCurrentIndex = 0;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View enterpriseHomeFragView = inflater.inflate(R.layout.fragment_home_enterprise, container, false);
		initViews(enterpriseHomeFragView);
		return enterpriseHomeFragView;
	}

	private void initViews(View rootView) {
		mChannelBar = (EnterpriseChannelBar) rootView.findViewById(R.id.home_enterprise_channel_bar);
		mViewPager = (ViewPager) rootView.findViewById(R.id.home_enterprise_pager);
		mEnterpriseTile = (TextView) rootView.findViewById(R.id.home_enterprise_title);
        View mContributeBtn = rootView.findViewById(R.id.contribute_btn);
		mContributeBtn.setOnClickListener(mOnClickLis);
	}
	
	OnClickListener mOnClickLis = new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			int id = v.getId();
			switch (id) {
			case R.id.contribute_btn:
				Intent intent = new Intent(mAct, ContributeActivity.class);
				if(mEnterpriseChannels.size() > 0) {
					intent.putExtra("channel_name", mEnterpriseChannels.get(mCurrentIndex).getChannelName());
				} else {
					intent.putExtra("channel_name", "");
				}
				startActivity(intent);
				break;
				
			default:
				break;
			}
		}
	};

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		initEvent();
		loadContent();
	}
	
	private void initEvent() {
		mChannelBar.setOnChannelClickLis(this);
		mViewPager.setOnPageChangeListener(mOnPageChangeListener);
		mEnterpriseTile.setText(Constants.userInfo.getEnterprise().getName());
	}

	OnPageChangeListener mOnPageChangeListener = new OnPageChangeListener() {
		
		@Override
		public void onPageSelected(int arg0) {
			mCurrentIndex = arg0;
			mChannelBar.setChannelSelected(mCurrentIndex);
			
			String channelId = mEnterpriseChannels.get(mCurrentIndex).getChannelID();
            Intent intent = new Intent(Actions.ACTION_CHANNEL_RELOAD_CHECK_TIME);
            intent.putExtra("channelid", channelId);
            LocalBroadcastManager.getInstance(getActivity()).sendBroadcast(intent);
		}
		
		@Override
		public void onPageScrolled(int arg0, float arg1, int arg2) {
		}
		
		@Override
		public void onPageScrollStateChanged(int arg0) {
		}
	};
	
	public void loadContent() {
		if(!isChannelChanged()) {
			return;
		}
		mEnterpriseTile.setText(Constants.userInfo.getEnterprise().getName());
		this.mEnterpriseChannels = ChanneDB.getChannelsByEnterpriseId(mAct,
				Constants.getEnterpriseId());
		mChannelBar.setmChannels(mEnterpriseChannels);
		mViewPager.setAdapter(new ViewPagerAdapter(getFragmentManager(), this.mEnterpriseChannels));
		mChannelBar.setChannelSelected(mCurrentIndex);
		mViewPager.setCurrentItem(mCurrentIndex);
	}
	
	private boolean isChannelChanged() {
		List<ChanneDao> channels = ChanneDB.getChannelsByEnterpriseId(mAct,
				Constants.getEnterpriseId());
		if(mEnterpriseChannels == null || channels.size() != this.mEnterpriseChannels.size()) {
			return true;
		}
		for(ChanneDao chan : channels) {
			boolean isExist = false;
			for(ChanneDao currentChan : mEnterpriseChannels) {
				if(currentChan.getChannelID().equals(chan.getChannelID())) {
					isExist = true;
					break;
				}
			}
			if(!isExist) {
				return true;
			}
		}
		return false;
	}
	
	/**
     * 栏目内容适配器
     */
    private class ViewPagerAdapter extends FragmentStatePagerAdapter {
    	private List<ChanneDao> mChaanels;
        public ViewPagerAdapter(FragmentManager fm, List<ChanneDao> chaanels) {
            super(fm);
            mChaanels = chaanels;
        }

        @Override
        public Fragment getItem(int position) {
            int index = position % mChaanels.size();
            ChanneDao cd = mChaanels.get(index);
            if(cd.getChannelType() == 0) {
            	//新建一个Fragment来展示栏目的内容，并传递栏目id参数
                NewsFragment fragment = new NewsFragment();
                fragment.setSAASNews(true);
                Bundle bundle = new Bundle();
                bundle.putString("channelid", mChaanels.get(index).getChannelID());
                fragment.setArguments(bundle);
                return fragment;
            } else {
            	PushMessagesFragment pf = new PushMessagesFragment();
            	Bundle bundle = new Bundle();
            	bundle.putBoolean("isEnterprise", true);
            	pf.setArguments(bundle);
            	return pf;
            }
        }

        @Override
        public CharSequence getPageTitle(int position) {
            int index = position % mChaanels.size();
            return mChaanels.get(index).getChannelName();
        }

        @Override
        public int getCount() {
            return mChaanels.size();
        }
    }

	@Override
	public void onChannelClicked(int channelIndex) {
		mViewPager.setCurrentItem(channelIndex);
	}
}