package com.hy.superemsg.components;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.hy.superemsg.R;
import com.hy.superemsg.SuperEMsgApplication;
import com.hy.superemsg.activity.NewsDetailActivity;
import com.hy.superemsg.activity.NewsPicActivity;
import com.hy.superemsg.adapter.AbsCommonAdapter;
import com.hy.superemsg.rsp.NewsContentDetail;
import com.hy.superemsg.utils.ImageUtils;

public class NewsBanner extends RelativeLayout {
	private GalleryEx mGallery;
	private LinearLayout dots;
	private TextView title;
	private RelativeLayout content;
	private LinearLayout loading;
	private LinearLayout error;
	protected String channelId;
	private boolean isMeitu;

	public NewsBanner(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init();
	}

	public NewsBanner(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
	}

	public NewsBanner(Context context) {
		super(context);
		init();
	}

	public void setMeitu(boolean isMeitu) {
		this.isMeitu = isMeitu;
	}

	private void init() {
		View v = inflate(getContext(), R.layout.banner, null);
		int width = this.getResources().getDisplayMetrics().widthPixels;
		int height = width / 2;
		this.addView(v, new FrameLayout.LayoutParams(width, height));
		v.layout(0, 0, width, height);
		mGallery = (GalleryEx) this.findViewById(R.id.banner_gallery);
		dots = (LinearLayout) this.findViewById(R.id.banner_dot);
		title = (TextView) this.findViewById(R.id.banner_text);
		mGallery.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View v, int arg2,
					long arg3) {
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
		mGallery.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> arg0, View v,
					int position, long arg3) {
				for (int i = 0; i < dots.getChildCount(); i++) {
					dots.getChildAt(i).setSelected(false);
				}
				dots.getChildAt(position).setSelected(true);
				if (v == null || v.getTag() == null) {
					return;
				}
				NewsContentDetail news = (NewsContentDetail) v.getTag();
				String text = news.newstitle;
				if (!TextUtils.isEmpty(text)) {
					title.setText(text);
				}
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
				for (int i = 0; i < dots.getChildCount(); i++) {
					dots.getChildAt(i).setSelected(false);
				}
			}
		});
		content = (RelativeLayout) this.findViewById(R.id.banner_content);
		loading = (LinearLayout) this.findViewById(R.id.banner_loading);
		error = (LinearLayout) this.findViewById(R.id.banner_error);
		error.findViewById(R.id.reload).setOnClickListener(
				new View.OnClickListener() {

					@Override
					public void onClick(View v) {

					}
				});
		datas = new ArrayList<NewsContentDetail>();
	}

	protected void setViewVisibility(boolean isContentVisible,
			boolean isLoadingVisible, boolean isErrorVisible) {

		content.setVisibility(isContentVisible ? View.VISIBLE : View.GONE);
		loading.setVisibility(isLoadingVisible ? View.VISIBLE : View.GONE);
		error.setVisibility(isErrorVisible ? View.VISIBLE : View.GONE);
	}

	private List<NewsContentDetail> datas;

	public void setData(List<NewsContentDetail> list) {
		datas.clear();
		datas.addAll(list);
		setViewVisibility(true, false, false);
		this.setVisibility(View.VISIBLE);
		dots.removeAllViews();
		int count = list.size();
		for (int i = 0; i < count; i++) {
			ImageView rb = new ImageView(getContext());
			rb.setImageResource(R.drawable.button_banner);
			rb.setPadding(5, 0, 5, 0);
			dots.addView(rb);
		}
		ImageAdapter adapter = new ImageAdapter(getContext());
		adapter.setDatas(list.subList(0, count));
		mGallery.setAdapter(adapter);

	}

	private class ImageAdapter extends AbsCommonAdapter<NewsContentDetail> {
		public ImageAdapter(Context ctx) {
			super(ctx, R.layout.banner_item);
		}

		@Override
		public void getView(int position, View convertView,
				NewsContentDetail data) {
			ImageView iv = (ImageView) convertView
					.findViewById(R.id.banner_item_image);
			TextView tv = (TextView) convertView
					.findViewById(R.id.banner_item_title);
			tv.setText(data.newstitle);
			if (!TextUtils.isEmpty(data.newsfocuspicurl)) {
				ImageUtils.Image.loadImage(data.newsfocuspicurl, iv);
			}
			convertView.setTag(data);
		}

	}

}
