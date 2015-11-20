package com.cplatform.xhxw.ui.ui.base.view;

import java.util.ArrayList;
import java.util.List;

import com.cplatform.xhxw.ui.R;
import com.cplatform.xhxw.ui.db.dao.ChanneDao;
import com.cplatform.xhxw.ui.util.DensityUtil;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.text.TextUtils.TruncateAt;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

public class EnterpriseChannelBar extends LinearLayout {
	private Context mCon;
	private List<ChanneDao> mChannels;
	private List<View> mChannelViews = new ArrayList<View>();
	
	private int mCurrentSelectedChannelIndex;
	
	private onChannelClickLisenter OnChannelClickLis;
	
	public EnterpriseChannelBar(Context context, AttributeSet attrs,
			int defStyle) {
		super(context, attrs, defStyle);
		mCon = context;
		init();
	}

	public EnterpriseChannelBar(Context context, AttributeSet attrs) {
		super(context, attrs);
		mCon = context;
		init();
	}

	public EnterpriseChannelBar(Context context) {
		super(context);
		mCon = context;
		init();
	}
	
	private void init() {
		setOrientation(HORIZONTAL);
		initChannels();
	}
	
	private void initChannels() {
		if(mChannels != null) {
			mChannelViews.clear();
			removeAllViews();
			for(int i = 0; i < mChannels.size(); i++) {
				TextView tv = getChannelView(mChannels.get(i));
				tv.setOnClickListener(mOnChannelClickLisenter);
				mChannelViews.add(tv);
				LinearLayout.LayoutParams lllp = new LayoutParams(0, LinearLayout.LayoutParams.MATCH_PARENT);
				lllp.weight = 1;
				if(i > 0) {
					lllp.leftMargin = -1 * DensityUtil.dip2px(mCon, 1);
				}
				addView(tv, lllp);
			}
			setChannelSelected(mCurrentSelectedChannelIndex);
		}
	}
	
	OnClickListener mOnChannelClickLisenter = new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			int index = getChannelOrder(v.getTag().toString());
			if(OnChannelClickLis != null) {
				OnChannelClickLis.onChannelClicked(index);
				setChannelSelected(index);
			}
		}
	};
	
	private TextView getChannelView(ChanneDao cd) {
		TextView channel = new TextView(mCon);
		channel.setText(cd.getChannelName());
		channel.setTextSize(18);
		channel.setTypeface(Typeface.DEFAULT_BOLD);
		channel.setGravity(Gravity.CENTER);
		channel.setSingleLine(true);
		channel.setEllipsize(TruncateAt.END);
		channel.setTag(cd.getChannelID());
		return channel;
	}

	public List<ChanneDao> getmChannels() {
		return mChannels;
	}

	public void setmChannels(List<ChanneDao> mChannels) {
		this.mChannels = mChannels;
		initChannels();
	}
	
	public void setChannelSelected(int index) {
		int channleCount = mChannelViews.size();
		int defaultColor = R.color.red_xuanwen;
		for(int i = 0; i < channleCount; i++) {
			TextView channel = (TextView) mChannelViews.get(i);
			int listOrder = getChannelOrder(channel.getTag().toString());
			if(listOrder == 0) {
				if(listOrder== index) {
					channel.setBackgroundResource(R.drawable.bg_left_circle_rect_focus);
					channel.setTextColor(Color.WHITE);
				} else {
					channel.setBackgroundResource(R.drawable.bg_left_circle_rect);
					channel.setTextColor(R.color.red_xuanwen);
				}
			} else if(listOrder == (channleCount - 1)) {
				if(listOrder == index) {
					channel.setBackgroundResource(R.drawable.bg_right_circle_rect_focus);
					channel.setTextColor(Color.WHITE);
				} else {
					channel.setBackgroundResource(R.drawable.bg_right_circle_rect);
					channel.setTextColor(R.color.red_xuanwen);
				}
			} else {
				if(listOrder == index) {
					channel.setBackgroundResource(R.drawable.bg_rect_focus);
					channel.setTextColor(Color.WHITE);
				} else {
					channel.setBackgroundResource(R.drawable.bg_rect);
					channel.setTextColor(defaultColor);
				}
			}
		}
	}
	
	private int getChannelOrder(String channelId) {
		if(mChannels == null) {
			return -1;
		}
		
		int len = mChannels.size();
		for(int i = 0; i < len; i++) {
			if(mChannels.get(i).getChannelID().equals(channelId)) {
				return i;
			}
		}
		return -1;
	}
	
	public onChannelClickLisenter getOnChannelClickLis() {
		return OnChannelClickLis;
	}

	public void setOnChannelClickLis(onChannelClickLisenter onChannelClickLis) {
		OnChannelClickLis = onChannelClickLis;
	}

	public interface onChannelClickLisenter {
		void onChannelClicked(int channelIndex);
	}
}
