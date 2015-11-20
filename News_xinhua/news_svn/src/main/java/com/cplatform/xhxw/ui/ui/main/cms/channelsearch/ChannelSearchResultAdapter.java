package com.cplatform.xhxw.ui.ui.main.cms.channelsearch;

import java.util.ArrayList;
import java.util.List;

import com.cplatform.xhxw.ui.R;
import com.cplatform.xhxw.ui.model.Channe;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class ChannelSearchResultAdapter extends BaseAdapter {
	private List<Channe> mData = new ArrayList<Channe>();
	private Context mCon;
	private OnChannelItemClickLisenter mItemClickLisenter;

	public ChannelSearchResultAdapter(Context con) {
		super();
		mCon = con;
	}

	@Override
	public int getCount() {
		return mData.size();
	}

	@Override
	public Channe getItem(int positoin) {
		return mData.get(positoin);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup arg2) {
		ViewHolder mvh;
		if(convertView == null) {
			mvh = new ViewHolder();
			LayoutInflater lif = (LayoutInflater) mCon.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			convertView = lif.inflate(R.layout.item_channel_search_list, null);
			mvh.channelName = (TextView) convertView.findViewById(R.id.item_channel_search_item_tv);
			mvh.operateBtn = (ImageView) convertView.findViewById(R.id.item_channel_search_add);
			convertView.setTag(mvh);
		} else {
			mvh = (ViewHolder) convertView.getTag();
		}
		
		final Channe item = getItem(position);
		mvh.channelName.setText(item.getChannelname());
		mvh.operateBtn.setImageResource(item.getIsadd() == 0 ? R.drawable.icon_search_add :
			R.drawable.icon_search_delete);
		
		mvh.channelName.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if(mItemClickLisenter != null) {
					mItemClickLisenter.onItemClick(item);
				}
			}
		});
		
		mvh.operateBtn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if(mItemClickLisenter != null) {
					mItemClickLisenter.onSubscribeChanne(item);
				}
			}
		});
		return convertView;
	}

	public List<Channe> getmData() {
		return mData;
	}

	public void setmData(List<Channe> mData) {
		this.mData.clear();
		if(mData != null) {
			this.mData.addAll(mData);
			this.mData = mData;
		}
		notifyDataSetChanged();
	}
	
	public OnChannelItemClickLisenter getmItemClickLisenter() {
		return mItemClickLisenter;
	}

	public void setmItemClickLisenter(OnChannelItemClickLisenter mItemClickLisenter) {
		this.mItemClickLisenter = mItemClickLisenter;
	}

	private class ViewHolder {
		TextView channelName;
		ImageView operateBtn;
	}
}
