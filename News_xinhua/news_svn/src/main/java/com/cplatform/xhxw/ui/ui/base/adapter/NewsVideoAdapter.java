package com.cplatform.xhxw.ui.ui.base.adapter;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

import com.cplatform.xhxw.ui.http.responseType.ShowType;
import com.cplatform.xhxw.ui.model.New;
import com.cplatform.xhxw.ui.model.Today;
import com.cplatform.xhxw.ui.ui.base.BaseAdapter;
import com.cplatform.xhxw.ui.ui.base.view.*;
import com.cplatform.xhxw.ui.ui.base.view.NewsMultiHorizontalItem.OnMultiImgOnClickListener;
import com.cplatform.xhxw.ui.util.LogUtil;

/**
 * 新闻列表适配器 Created by cy-love on 13-12-25.
 */
public class NewsVideoAdapter extends BaseAdapter<Today> {

	private OnMultiImgOnClickListener mMultClickLis;

	public NewsVideoAdapter(Context context, OnMultiImgOnClickListener multClickLis) {
		super(context);
		this.mMultClickLis = multClickLis;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		Today item = getItem(position);
		if (convertView == null
				|| needCreateNewView((Integer) convertView.getTag(),
						item.getListStyle())) {
			convertView = new VideoGridViewItem(mContext);
			convertView.setTag(item.getListStyle());
		}
		VideoGridViewItem view = (VideoGridViewItem) convertView;
		view.setData(item);
		return convertView;
	}

	@Override
	public int getItemViewType(int position) {
		int type = getItem(position).getShowType();
		return type > 7 ? 0 : type;
	}

	@Override
	public int getViewTypeCount() {
		return 8;
	}

	private boolean needCreateNewView(int olderViewType, int newViewTpe) {

		return (olderViewType == New.LIST_STYLE_PICS && newViewTpe != New.LIST_STYLE_PICS)
				|| (olderViewType != New.LIST_STYLE_PICS && newViewTpe == New.LIST_STYLE_PICS);
	}

	public void close() {
		clearData();
		mData = null;
		mContext = null;
		mInflater = null;
	}
	
	private boolean isToShowDividerBg(int position, boolean isTop) {
		if(isTop) {
			if(position == 0) {
				return true;
			} else {
				int type = getItemViewType(position - 1);
				if(type == ShowType.IMAGE || getItem(position - 1).getListStyle() == New.LIST_STYLE_PICS) {
					return false;
				}
				return true;
			}
		} else {
			if(position == getCount() - 1) {
				return true;
			} else {
				int type = getItemViewType(position + 1);
				if(type == ShowType.IMAGE || getItem(position + 1).getListStyle() == New.LIST_STYLE_PICS) {
					return false;
				}
				return true;
			}
		}
	}
}
