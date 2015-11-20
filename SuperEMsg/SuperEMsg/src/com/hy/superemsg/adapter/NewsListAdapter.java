package com.hy.superemsg.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.hy.superemsg.R;
import com.hy.superemsg.SuperEMsgApplication;
import com.hy.superemsg.activity.NewsDetailActivity;
import com.hy.superemsg.activity.NewsPicActivity;
import com.hy.superemsg.rsp.NewsContentDetail;
import com.hy.superemsg.utils.ImageUtils;

public class NewsListAdapter extends AbsCommonAdapter<NewsContentDetail> {
	private Boolean isMeitu;

	public NewsListAdapter(Context ctx, boolean isMeitu) {
		super(ctx, R.layout.item_news_list);
		this.isMeitu = isMeitu;
	}

	@Override
	public void getView(int position, View convertView, NewsContentDetail data) {
		ImageView iv = (ImageView) convertView.findViewById(R.id.item_image);
		TextView title = (TextView) convertView.findViewById(R.id.item_text);
		TextView sub_title = (TextView) convertView
				.findViewById(R.id.item_text1);
		ImageUtils.Image.loadImage(data.newspicurl, iv);
		title.setText(data.newstitle);
		sub_title.setText(data.newsleads);
		convertView.setTag(data);
		convertView.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				Activity act = (Activity) v.getContext();
				Intent i = new Intent(act, NewsDetailActivity.class);
				if (isMeitu) {
					i = new Intent(act, NewsPicActivity.class);
				}
				i.putExtra(SuperEMsgApplication.EXTRA_NEWS_DETAIL,
						(NewsContentDetail) v.getTag());
				act.startActivity(i);
			}
		});
	}

}
