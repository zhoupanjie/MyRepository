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
import com.cplatform.xhxw.ui.http.responseType.FootType;
import com.cplatform.xhxw.ui.http.responseType.NewsDetTextSize;
import com.cplatform.xhxw.ui.model.New;
import com.cplatform.xhxw.ui.util.DisplayImageOptionsUtil;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.cplatform.xhxw.ui.Constants;
import com.cplatform.xhxw.ui.R;
import com.cplatform.xhxw.ui.util.DateUtil;
import com.cplatform.xhxw.ui.util.StringUtil;
import com.cplatform.xhxw.ui.util.TextViewUtil;

/**
 * 新闻布局 Created by cy-love on 13-12-25.
 */
public class NewItem extends RelativeLayout {

	ImageView mImg;
	TextView mTitle;
	ImageView mAction;
	TextView mComment;
	LinearLayout mCommentLo;
	TextView mSourceTv;
	TextView mDateTv;
	Context mCon;
	RelativeLayout mOptionLo;

	public NewItem(Context context) {
		super(context);
		mCon = context;
		this.init();
	}

	private void init() {
		LayoutInflater inflater = (LayoutInflater) getContext()
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View rootView = inflater.inflate(R.layout.view_new_item, this);
		initViews(rootView);
	}

	private void initViews(View rootView) {
		mImg = (ImageView) rootView.findViewById(R.id.iv_img);
		mTitle = (TextView) rootView.findViewById(R.id.tv_title);
		mAction = (ImageView) rootView.findViewById(R.id.iv_action);
		mComment = (TextView) rootView.findViewById(R.id.tv_comment);
		mSourceTv = (TextView) rootView.findViewById(R.id.news_item_source_tv);
		mDateTv = (TextView) rootView.findViewById(R.id.news_list_date_tv);
		mCommentLo = (LinearLayout) rootView.findViewById(R.id.news_item_comment_lo);
		mOptionLo = (RelativeLayout) rootView.findViewById(R.id.news_item_options_lo);
	}

	public void setData(New item) {
		TextViewUtil
				.setDisplayModel(getContext(), mTitle, item.isRead());
		mTitle.setSingleLine(false);
		mTitle.setMaxLines(3);
		float textSize = getTitleTextSize();
		mTitle.setTextSize(textSize);
		mImg.setVisibility(View.VISIBLE);
		
		if(item.getThumbnail() == null || item.getThumbnail().length() < 1) {
			mImg.setVisibility(View.GONE);
		} else {
			ImageLoader.getInstance().displayImage(item.getThumbnail(), mImg,
					DisplayImageOptionsUtil.listNewImgOptions);
		}
		
		if(item.getListStyle() == New.LIST_STYLE_LONG_TITLE) {
			mTitle.setSingleLine(false);
		}
		String titleStr = item.getTitle();
		mTitle.setText(titleStr);
		TextPaint tp = mTitle.getPaint();
		float textLen =0;
		if(titleStr != null) {
			textLen = tp.measureText(titleStr);
		}
		int titleWid = Constants.titleViewWidth;
		float lines = textLen / titleWid;
		mOptionLo.setVisibility(View.VISIBLE);
		if(lines > 2) {
//			mImg.setVisibility(View.GONE);
			mTitle.setMaxLines(3);
			mTitle.setText(titleStr);
			mOptionLo.setVisibility(View.GONE);
		} else if(lines > 1) {
			mTitle.setMaxLines(2);
			mTitle.setText(titleStr);
		} else {
			mTitle.setMaxLines(1);
			mTitle.setText(titleStr);
		}
		
		if((item.getThumbnail() == null || item.getThumbnail().length() < 1)) {
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
		
		setFoodType(item.getFootType());
		if ("1".equals(item.getIscomment())) {
			mCommentLo.setVisibility(View.INVISIBLE);
		} else {
			String com = (!TextUtils.isEmpty(item.getCommentcount())) ? String
					.format(getString(R.string.comment_count_string_format),
							item.getCommentcount()) : null;
			if (StringUtil.parseIntegerFromString(item.getCommentcount()) < Constants.NEWS_LIST_SHOW_COMMENT_NUMBER_LOWER_BOUND) {
				mCommentLo.setVisibility(View.INVISIBLE);
			} else {
				mCommentLo.setVisibility(View.VISIBLE);
				mComment.setText(com);
			}
		}
		
		mSourceTv.setText(TextUtils.isEmpty(item.getShowSource()) ? "" : 
			item.getShowSource());

		mDateTv.setText(DateUtil.getXHAPPDetailFormmatString(
				TextUtils.isEmpty(item.getPublished()) ? 0L :
					Long.valueOf(item.getPublished())));
	}

	private String getString(int resId) {
		return getResources().getString(resId);
	}

	private void setFoodType(int type) {
		switch (type) {
		case FootType.LOCAL:
			mAction.setVisibility(View.VISIBLE);
			mAction.setImageResource(R.drawable.tags_local);
			break;
		case FootType.AUDIO:
			mAction.setVisibility(View.VISIBLE);
			mAction.setImageResource(R.drawable.tags_audio);
			break;
		case FootType.VIDEO:
			mAction.setVisibility(View.VISIBLE);
			mAction.setImageResource(R.drawable.tags_video);
			break;
		case FootType.SPECIAL_TOPIC:
			mAction.setVisibility(View.VISIBLE);
			mAction.setImageResource(R.drawable.tags_specialnews);
			break;
		case FootType.ATLAS:
			mAction.setVisibility(View.VISIBLE);
			mAction.setImageResource(R.drawable.tags_pics);
			break;
		case FootType.SPREAD:
			mAction.setVisibility(View.VISIBLE);
			mAction.setImageResource(R.drawable.tags_spread);
			break;
		case FootType.PROGRESS:
			mAction.setVisibility(View.VISIBLE);
			mAction.setImageResource(R.drawable.tags_progress);
			break;
		case FootType.HOTSPOT:
			mAction.setVisibility(View.VISIBLE);
			mAction.setImageResource(R.drawable.tags_hotspot);
			break;
		case FootType.HOTTEST:
			mAction.setVisibility(View.VISIBLE);
			mAction.setImageResource(R.drawable.tags_hottest);
			break;
		case FootType.LATEST:
			mAction.setVisibility(View.VISIBLE);
			mAction.setImageResource(R.drawable.tags_latest);
			break;
		case FootType.TRACK:
			mAction.setVisibility(View.VISIBLE);
			mAction.setImageResource(R.drawable.tags_track);
			break;
		case FootType.LIVE:
			mAction.setVisibility(View.VISIBLE);
			mAction.setImageResource(R.drawable.tags_live);
			break;
		case FootType.RECOMMENT:
			mAction.setVisibility(View.VISIBLE);
			mAction.setImageResource(R.drawable.tags_recommend);
			break;
		case FootType.EXCLUSIVE:
		default:
			mAction.setVisibility(View.GONE);
			break;
		}
	}

	/**
	 * 获取新闻列表页标题文字大小
	 * @return
	 */
	public static float getTitleTextSize() {
		int textStyle = Constants.getNewsDetTextSize();
		int textSize = 16;
		switch (textStyle) {
		case NewsDetTextSize.SMALL:
			textSize = 12;
			break;
		case NewsDetTextSize.MIDDLE:
			textSize = 16;
			break;
		case NewsDetTextSize.BIG:
			textSize = 18;
			break;
		case NewsDetTextSize.SUPER_BIG:
			textSize = 20;
			break;
		}
		return textSize;
	}
}
