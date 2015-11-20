package com.hy.superemsg.components;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
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
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;

import com.hy.superemsg.R;
import com.hy.superemsg.adapter.AbsCommonAdapter;
import com.hy.superemsg.rsp.AnimationDramaDetail;
import com.hy.superemsg.rsp.NewsContentDetail;
import com.hy.superemsg.utils.CommonUtils;
import com.hy.superemsg.utils.ImageUtils;

public class BookBanner extends RelativeLayout {
	private GalleryEx mGallery;
	private SeekBar seek;
	private RelativeLayout content;
	private LinearLayout loading;
	private LinearLayout error;
	protected String channelId;
	private AnimationDramaDetail drama;

	public void setDrama(AnimationDramaDetail drama) {
		List<PageInfo> datas = new ArrayList<PageInfo>();
		this.drama = drama;
		String prefix = drama.dramafilepicurl.substring(0,
				drama.dramafilepicurl.lastIndexOf("/"));
		String relativeUrl = drama.dramafilepicurl
				.substring(drama.dramafilepicurl.lastIndexOf("/"));
		String imageFormat = relativeUrl
				.substring(relativeUrl.lastIndexOf("."));
		for (int i = 1; i <= drama.dramapiccount; i++) {
			PageInfo p = new PageInfo();
			p.index = i;
			p.fileUrl = prefix + "/" + i + imageFormat;
			datas.add(p);
		}
		seek.setMax(drama.dramapiccount);
		setData(datas);
	}

	public BookBanner(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init();
	}

	public BookBanner(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
	}

	public BookBanner(Context context) {
		super(context);
		init();
	}

	private void init() {
		View v = inflate(getContext(), R.layout.banner1, null);
		this.addView(v, new FrameLayout.LayoutParams(LayoutParams.FILL_PARENT,
				LayoutParams.FILL_PARENT));
		mGallery = (GalleryEx) this.findViewById(R.id.banner_gallery);
		seek = (SeekBar) this.findViewById(R.id.seek);
		seek.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {

			@Override
			public void onStopTrackingTouch(SeekBar seekBar) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onStartTrackingTouch(SeekBar seekBar) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onProgressChanged(SeekBar seekBar, int progress,
					boolean fromUser) {
				if (fromUser) {
					mGallery.setSelection(progress);
				}
			}
		});
		mGallery.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View v, int arg2,
					long arg3) {

			}
		});
		mGallery.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> arg0, View v,
					int position, long arg3) {
				if (v == null || v.getTag() == null) {
					return;
				}
				seek.setProgress(position);
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
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
	}

	protected void setViewVisibility(boolean isContentVisible,
			boolean isLoadingVisible, boolean isErrorVisible) {

		content.setVisibility(isContentVisible ? View.VISIBLE : View.GONE);
		loading.setVisibility(isLoadingVisible ? View.VISIBLE : View.GONE);
		error.setVisibility(isErrorVisible ? View.VISIBLE : View.GONE);
	}

	private void setData(List<PageInfo> pages) {
		setViewVisibility(true, false, false);
		this.setVisibility(View.VISIBLE);
		ImageAdapter adapter = new ImageAdapter(getContext());
		adapter.setDatas(pages);
		mGallery.setAdapter(adapter);

	}

	private class PageInfo {
		public int index;
		public String fileUrl;
	}

	private class ImageAdapter extends AbsCommonAdapter<PageInfo> {
		public ImageAdapter(Context ctx) {
			super(ctx, R.layout.banner_item);
		}

		@Override
		public void getView(int position, View convertView, PageInfo data) {
			ImageView iv = (ImageView) convertView
					.findViewById(R.id.banner_item_image);
			if (!TextUtils.isEmpty(data.fileUrl)) {
				ImageUtils.Image.loadImage(data.fileUrl, iv);
			}
			convertView.setTag(data);
		}

	}

}
