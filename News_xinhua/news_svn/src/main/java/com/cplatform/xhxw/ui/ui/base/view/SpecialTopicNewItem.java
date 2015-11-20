package com.cplatform.xhxw.ui.ui.base.view;

import android.content.Context;
import android.text.TextPaint;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.cplatform.xhxw.ui.Constants;
import com.cplatform.xhxw.ui.R;
import com.cplatform.xhxw.ui.model.SpecialDetailData;
import com.cplatform.xhxw.ui.util.DisplayImageOptionsUtil;
import com.cplatform.xhxw.ui.util.TextViewUtil;
import com.nostra13.universalimageloader.core.ImageLoader;

/**
 * 新闻头条缩略图新闻布局 Created by cy-love on 13-12-25.
 */
public class SpecialTopicNewItem extends RelativeLayout {

	TextView mHeader;
	ImageView mImg;
	TextView mTitle;
	ImageView mAction;
	TextView mComment;
	TextView mDateTv;
	LinearLayout mCommentLo;

	public SpecialTopicNewItem(Context context) {
		super(context);
		this.init();
	}

	private void init() {
		LayoutInflater inflater = (LayoutInflater) getContext()
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View root = inflater.inflate(R.layout.view_special_topic_new_item, this);
		initViews(root);
	}

	private void initViews(View root) {
		mHeader = (TextView) root.findViewById(R.id.tv_header);
		mImg = (ImageView) root.findViewById(R.id.iv_img);
		mTitle = (TextView) root.findViewById(R.id.tv_title);
		mAction = (ImageView) root.findViewById(R.id.iv_action);
		mComment = (TextView) root.findViewById(R.id.tv_comment);
		mDateTv = (TextView) root.findViewById(R.id.news_list_date_tv);
		mCommentLo = (LinearLayout) root.findViewById(R.id.news_item_comment_lo);
	}

	public void setData(SpecialDetailData item, boolean isShow) {
		TextViewUtil.setDisplayModel(getContext(), mHeader, mTitle,
				item.isRead());
		ImageLoader.getInstance().displayImage(item.getImg(), mImg,
				DisplayImageOptionsUtil.listNewImgOptions);
		
		mTitle.setSingleLine(false);
		mTitle.setMaxLines(3);
		float textSize = NewItem.getTitleTextSize();
		mTitle.setTextSize(textSize);
		mImg.setVisibility(View.VISIBLE);
		
		String titleStr = item.getTitle();
		mTitle.setText(titleStr);
		TextPaint tp = mTitle.getPaint();
		float textLen = tp.measureText(titleStr);
		int titleWid = Constants.titleViewWidth;
		float lines = textLen / titleWid;
		if(lines > 2) {
			mImg.setVisibility(View.GONE);
			mTitle.setMaxLines(3);
			mTitle.setText(titleStr);
		} else if(lines > 1) {
			mTitle.setMaxLines(2);
			mTitle.setText(titleStr);
		} else {
			mTitle.setMaxLines(1);
			mTitle.setText(titleStr);
		}
		
		if ("1".equals(item.getIscomment())) {
			mCommentLo.setVisibility(View.GONE);
		} else {
			mCommentLo.setVisibility(View.VISIBLE);
			String com = (!TextUtils.isEmpty(item.getCommentcount()) && Integer
					.parseInt(item.getCommentcount()) >= Constants.NEWS_LIST_SHOW_COMMENT_NUMBER_LOWER_BOUND) ? String
					.format(getString(R.string.comment_count_string_format),
							item.getCommentcount()) : null;
			mComment.setText(com);
		}

		mAction.setVisibility(View.GONE);
		if (isShow) {
			mHeader.setVisibility(View.VISIBLE);
			mHeader.setText(item.getGrouptitle());
		} else {
			mHeader.setVisibility(View.GONE);
		}
	}

	private String getString(int resId) {
		return getResources().getString(resId);
	}

}
