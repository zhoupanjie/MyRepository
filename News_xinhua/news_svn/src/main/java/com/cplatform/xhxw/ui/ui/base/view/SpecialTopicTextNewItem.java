package com.cplatform.xhxw.ui.ui.base.view;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import butterknife.ButterKnife;
import butterknife.Bind;

import com.cplatform.xhxw.ui.Constants;
import com.cplatform.xhxw.ui.R;
import com.cplatform.xhxw.ui.model.SpecialDetailData;
import com.cplatform.xhxw.ui.util.TextViewUtil;

/**
 * 新闻头条文字新闻布局 Created by cy-love on 13-12-25.
 */
public class SpecialTopicTextNewItem extends RelativeLayout {

	@Bind(R.id.tv_header)
	TextView mHeader;
	@Bind(R.id.tv_title)
	TextView mTitle;
	@Bind(R.id.tv_desc)
	TextView mDesc;
	@Bind(R.id.iv_action)
	ImageView mAction;
	@Bind(R.id.tv_comment)
	TextView mComment;

	private int mDisModel;

	public SpecialTopicTextNewItem(Context context) {
		super(context);
		this.init();
	}

	private void init() {
		LayoutInflater inflater = (LayoutInflater) getContext()
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		inflater.inflate(R.layout.view_special_topic_text_new_item, this);
		ButterKnife.bind(this);
		mDisModel = Constants.DISPLAY_MODEL_DAY;
	}

	public void setData(SpecialDetailData item, boolean isShowHeader) {
		TextViewUtil.setDisplayModel(getContext(), mHeader, mTitle, mDesc,
				item.isRead());
		mTitle.setSingleLine(false);
		mTitle.setMaxLines(2);
		mTitle.setTextSize(NewItem.getTitleTextSize());
		mTitle.setText(item.getTitle());
		mDesc.setText(item.getFriendTime());

		if ("1".equals(item.getIscomment())) {
			mComment.setVisibility(View.GONE);
		} else {
			String com = (!TextUtils.isEmpty(item.getCommentcount()) && Integer
					.parseInt(item.getCommentcount()) >= Constants.NEWS_LIST_SHOW_COMMENT_NUMBER_LOWER_BOUND) ? String
					.format(getString(R.string.comment_count_string_format),
							item.getCommentcount()) : null;
			mComment.setText(com);
		}

		mAction.setVisibility(View.GONE);
		if (isShowHeader) {
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
