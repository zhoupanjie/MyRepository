package com.cplatform.xhxw.ui.ui.base.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

import com.cplatform.xhxw.ui.model.Message;
import com.cplatform.xhxw.ui.ui.base.BaseAdapter;
import com.cplatform.xhxw.ui.ui.base.view.MessageItem;

/**
 * 消息推送列表适配器 Created by cy-love on 13-12-25.
 */
public class MessageAdapter extends BaseAdapter<Message> {
	private boolean isEnterprise = false;

	public MessageAdapter(Context context, boolean isEnterprise) {
		super(context);
		this.isEnterprise = isEnterprise;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		Message item = getItem(position);
		if (convertView == null) {
			convertView = new MessageItem(mContext);
		}

		MessageItem view = (MessageItem) convertView;
		int headerVis = (position == 0 || !getItem(position - 1).getTime()
				.equals(item.getTime())) ? View.VISIBLE : View.GONE;
		if (isEnterprise) {
			headerVis = View.GONE;
		}
		view.setData(item, headerVis);
		return convertView;
	}

}
