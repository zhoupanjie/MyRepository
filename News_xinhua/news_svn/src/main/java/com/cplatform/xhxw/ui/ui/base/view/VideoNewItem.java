package com.cplatform.xhxw.ui.ui.base.view;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import butterknife.ButterKnife;
import butterknife.Bind;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.cplatform.xhxw.ui.Constants;
import com.cplatform.xhxw.ui.R;
import com.cplatform.xhxw.ui.model.New;
import com.cplatform.xhxw.ui.util.DateUtil;
import com.cplatform.xhxw.ui.util.DisplayImageOptionsUtil;
import com.cplatform.xhxw.ui.util.TextViewUtil;
import com.cplatform.xhxw.ui.util.TimeUtil;

/**
 * 单张新闻视频布局 Created by cy-love on 13-12-25.
 */
public class VideoNewItem extends RelativeLayout {

	private boolean isVideo = false;

	@Bind(R.id.video_layout)
	public RelativeLayout videoLayout;
	@Bind(R.id.video_image)
	public ImageView videoImage;
	@Bind(R.id.video_title_tv)
	public TextView videoNameText;
	@Bind(R.id.video_play_btn)
	public ImageView videoPlayBtn;
	@Bind(R.id.video_video_layout)
	public LinearLayout videoVideoLayout;
	@Bind(R.id.video_recommend_layout)
	public LinearLayout videoRecommendLayout;
	@Bind(R.id.video_item_comment_btn)
	public LinearLayout videoCommentBtn;
	@Bind(R.id.video_item_share_btn)
	public LinearLayout videoShareBtn;
	@Bind(R.id.video_source_tv)
	public TextView videoSourceTv;
	@Bind(R.id.video_published_time_tv)
	public TextView videoPublishedTimeTv;

	public VideoNewItem(Context context, boolean isVideo) {
		super(context);
		this.isVideo = isVideo;
		this.init();
	}

	private void init() {
		LayoutInflater inflater = (LayoutInflater) getContext()
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		inflater.inflate(R.layout.video_item_layout, this);
		ButterKnife.bind(this);
		if (isVideo) {
			videoVideoLayout.setVisibility(View.VISIBLE);
		} else {
			videoRecommendLayout.setVisibility(View.VISIBLE);
		}
	}

	public void setData(New item) {
		// TextViewUtil.setDisplayModel(getContext(), videoNameText, mDesc,
		// item.isRead());
		// ImageLoader.getInstance().displayImage(item.getBigthumbnail(),
		// videoImage, DisplayImageOptionsUtil.listNewImgOptions);
		// if (!imageUrl.equals(imageThumb)) {
		// ImageLoader.getInstance().displayImage(
		// lvd.get(position).getThumb(), holder.videoImage,
		// DisplayImageOptionsUtil.listNewImgOptions);
		// }
		float textSize = NewItem.getTitleTextSize();
		videoNameText.setText(item.getTitle());
		videoNameText.setTextSize(textSize);
		videoSourceTv.setText(item.getShowSource());
		String time = DateUtil.getXHAPPDetailFormmatString(
				TextUtils.isEmpty(item.getPublished()) ? 0L :
					Long.valueOf(item.getPublished()));
		videoPublishedTimeTv.setText(time);
		
		// mDesc.setText(item.getSummary());
		// if(textSize > 16) {
		// mDesc.setTextSize(Constants.LIST_SUMMARY_TEXT_SIZE + 2);
		// } else {
		// mDesc.setTextSize(Constants.LIST_SUMMARY_TEXT_SIZE);
		// }
		RelativeLayout.LayoutParams rllp = new RelativeLayout.LayoutParams(
				RelativeLayout.LayoutParams.MATCH_PARENT, 
				getImgHeight());
		videoImage.setLayoutParams(rllp);
		TextViewUtil.setDisplayBgModel(getContext(), this);
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
}
