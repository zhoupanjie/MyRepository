package com.cplatform.xhxw.ui.ui.base.view;

import com.cplatform.xhxw.ui.R;
import com.cplatform.xhxw.ui.model.New;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

public class NewsBroadcastItem extends LinearLayout {
	
	private TextView mTitle;
	
	public NewsBroadcastItem(Context context) {
		super(context);
		init();
	}

	private void init() {
		View root = LayoutInflater.from(getContext()).inflate(
				R.layout.view_news_item_broadcast, this);
		mTitle = (TextView) root.findViewById(R.id.broadcast_item_title);
	}

	public void setData(New item) {
		mTitle.setText(item.getTitle());
		mTitle.setTextSize(NewItem.getTitleTextSize());
	}
}
