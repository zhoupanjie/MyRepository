package com.cplatform.xhxw.ui.ui.base.view;

import android.content.Context;
import android.text.TextPaint;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import butterknife.ButterKnife;
import butterknife.Bind;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.cplatform.xhxw.ui.Constants;
import com.cplatform.xhxw.ui.R;
import com.cplatform.xhxw.ui.model.New;
import com.cplatform.xhxw.ui.util.DisplayImageOptionsUtil;
import com.cplatform.xhxw.ui.util.TextUtil;
import com.cplatform.xhxw.ui.util.TextViewUtil;

/**
 * 炫纹音频布局 Created by cy-love on 13-12-25.
 */
public class NewsAudioItem extends RelativeLayout {

	@Bind(R.id.iv_img)
	ImageView mImg;
	@Bind(R.id.tv_title)
	TextView mTitle;
	@Bind(R.id.tv_desc)
	TextView mDesc;
	@Bind(R.id.au_comment)
	TextView mComment;

	public NewsAudioItem(Context context) {
		super(context);
		this.init();
	}

	private void init() {
		LayoutInflater inflater = (LayoutInflater) getContext()
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		inflater.inflate(R.layout.view_news_audio_item, this);
		ButterKnife.bind(this);
	}

	public void setData(New item) {
		TextViewUtil
				.setDisplayModel(getContext(), mTitle, mDesc, item.isRead());
		
		mTitle.setSingleLine(false);
		mTitle.setMaxLines(3);
		float textSize = NewItem.getTitleTextSize();
		mTitle.setTextSize(textSize);
		mImg.setVisibility(View.VISIBLE);
		mDesc.setVisibility(View.VISIBLE);
		
		if(item.getThumbnail() == null || item.getThumbnail().length() < 1) {
			mImg.setVisibility(View.GONE);
		} else {
			ImageLoader.getInstance().displayImage(item.getThumbnail(), mImg,
					DisplayImageOptionsUtil.listNewImgOptions);
		}
		
		if(textSize > 16) {
			mDesc.setTextSize(Constants.LIST_SUMMARY_TEXT_SIZE + 2);
		} else {
			mDesc.setTextSize(Constants.LIST_SUMMARY_TEXT_SIZE);
		}
		
		mTitle.setText(item.getTitle());
		mTitle.setTextSize(NewItem.getTitleTextSize());
		mDesc.setText(TextUtil.newsDescInterception(item.getSummary()));
		
		
		String titleStr = item.getTitle();
		mTitle.setText(titleStr);
		TextPaint tp = mTitle.getPaint();
		float textLen =0;
		if(titleStr != null) {
			textLen = tp.measureText(titleStr);
		}
		int titleWid = Constants.titleViewWidth;
		float lines = textLen / titleWid;
		if(lines > 2) {
			mImg.setVisibility(View.GONE);
			mTitle.setMaxLines(2);
			mTitle.setText(titleStr);
			mDesc.setVisibility(View.GONE);
		} else if(lines > 1) {
			mTitle.setMaxLines(2);
			mTitle.setText(titleStr);
			mDesc.setVisibility(View.GONE);
		} else {
			mTitle.setMaxLines(1);
			mTitle.setText(titleStr);
		}
		
		if((item.getThumbnail() == null || item.getThumbnail().length() < 1) || lines > 2) {
			RelativeLayout.LayoutParams rllp = new RelativeLayout.LayoutParams(
					RelativeLayout.LayoutParams.MATCH_PARENT, 
					RelativeLayout.LayoutParams.WRAP_CONTENT);
			rllp.addRule(CENTER_IN_PARENT);
			mTitle.setLayoutParams(rllp);
		} else {
			RelativeLayout.LayoutParams rllp = new RelativeLayout.LayoutParams(
					RelativeLayout.LayoutParams.MATCH_PARENT, 
					RelativeLayout.LayoutParams.WRAP_CONTENT);
			rllp.addRule(ALIGN_PARENT_TOP);
			mTitle.setLayoutParams(rllp);
		}
		
		if ("1".equals(item.getIscomment())) {
			mComment.setVisibility(View.GONE);
		} else {
			String com = (!TextUtils.isEmpty(item.getCommentcount())) ? String
					.format(getString(R.string.comment_count_string_format),
							item.getCommentcount()) : null;
			if (Integer.valueOf(item.getCommentcount()) < Constants.NEWS_LIST_SHOW_COMMENT_NUMBER_LOWER_BOUND) {
				mComment.setText("");
			} else {
				mComment.setText(com);
			}
		}

	}

	private String getString(int resId) {
		return getResources().getString(resId);
	}
}
