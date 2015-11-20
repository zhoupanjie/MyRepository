package com.cplatform.xhxw.ui.ui.base.view;

import com.cplatform.xhxw.ui.Constants;
import com.cplatform.xhxw.ui.R;
import com.cplatform.xhxw.ui.model.New;
import com.cplatform.xhxw.ui.util.DateUtil;
import com.cplatform.xhxw.ui.util.DisplayImageOptionsUtil;
import com.nostra13.universalimageloader.core.ImageLoader;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class NewsVideoItem extends LinearLayout {
	private boolean isVideo = true;
	private ImageView mThumbIV;
	private ImageView mPlayBtn;
	private TextView mTitle;
	private TextView mResource;
	private TextView mDate;
	private onItemVideoPlayLisenter mLis;
	
	public NewsVideoItem(Context context, boolean isVideo) {
		super(context);
		this.isVideo = isVideo;
		init();
	}

	private void init() {
		LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context
                .LAYOUT_INFLATER_SERVICE);
        View root = inflater.inflate(R.layout.view_news_item_video, this);
		mThumbIV = (ImageView) root.findViewById(R.id.video_item_img);
		mPlayBtn = (ImageView) root.findViewById(R.id.video_item_playbtn);
		mTitle = (TextView) root.findViewById(R.id.video_item_title);
		mResource = (TextView) root.findViewById(R.id.video_item_source_tv);
		mDate = (TextView) root.findViewById(R.id.video_item_date);
	}

	public void setData(final New item) {
		mPlayBtn.setVisibility(View.VISIBLE);
		if(!isVideo) {
			mPlayBtn.setVisibility(View.GONE);
		}
		mPlayBtn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if(mLis != null) {
					mLis.onPlayVideo(v, item);
				}
			}
		});

		mTitle.setText(item.getTitle());
		mTitle.setTextSize(NewItem.getTitleTextSize());
		mResource.setText(item.getShowSource());
		
		mDate.setText(DateUtil.getXHAPPDetailFormmatString(
				TextUtils.isEmpty(item.getPublished()) ? 0L :
					Long.valueOf(item.getPublished())));
		
		RelativeLayout.LayoutParams rllp = new RelativeLayout.LayoutParams(
				RelativeLayout.LayoutParams.MATCH_PARENT, 
				getImgHeight());
		mThumbIV.setLayoutParams(rllp);
		ImageLoader.getInstance().displayImage(item.getBigthumbnail(), mThumbIV,
					DisplayImageOptionsUtil.listNewImgOptions);
	}
	
	private int getImgHeight() {
		if(Constants.HomeSliderSize.title_height > 0) {
			return Constants.HomeSliderSize.title_height;
		} else {
			double height = Constants.screenWidth * Constants.HomeSliderSize.height
					/ Constants.HomeSliderSize.width;
			Constants.HomeSliderSize.title_height = 
					(int) Math.ceil(height) + 1;
			return Constants.HomeSliderSize.title_height;
		}
	}
	
	public onItemVideoPlayLisenter getmLis() {
		return mLis;
	}

	public void setOnItemVideoPlayLisenter(onItemVideoPlayLisenter mLis) {
		this.mLis = mLis;
	}

	public interface onItemVideoPlayLisenter{
		void onPlayVideo(View v, New item);
	}
}
