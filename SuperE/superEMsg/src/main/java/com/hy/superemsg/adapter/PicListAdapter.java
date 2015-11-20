package com.hy.superemsg.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Parcelable;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.hy.superemsg.R;
import com.hy.superemsg.SuperEMsgApplication;
import com.hy.superemsg.activity.NewsDetailActivity;
import com.hy.superemsg.activity.NewsPicActivity;
import com.hy.superemsg.rsp.NewsContentDetail;
import com.hy.superemsg.utils.ImageUtils;

public class PicListAdapter extends AbsCommonAdapter<NewsContentDetail> {

	public PicListAdapter(Context ctx) {
		super(ctx, R.layout.item_pic);
	}

	@Override
	public void getView(int position, View convertView, NewsContentDetail data) {
		ImageView iv = (ImageView) convertView.findViewById(R.id.item_image);
		TextView title = (TextView) convertView.findViewById(R.id.item_text);
		String imgurl = data.newsfocuspicurl;
		if (TextUtils.isEmpty(imgurl)) {
			imgurl = data.newspicurl;
		}
		ImageUtils.Image.loadImage(imgurl, iv);
		title.setText(data.newstitle);
		convertView.setTag(data);
		convertView.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				Activity act = (Activity) v.getContext();
				Intent i = new Intent(act, NewsPicActivity.class);
				i.putExtra(SuperEMsgApplication.EXTRA_NEWS_DETAIL,
						(NewsContentDetail) v.getTag());
				act.startActivity(i);
			}
		});
	}

}
