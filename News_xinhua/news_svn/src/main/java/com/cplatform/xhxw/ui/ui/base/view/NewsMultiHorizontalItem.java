package com.cplatform.xhxw.ui.ui.base.view;

import android.content.Context;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import butterknife.OnClick;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.cplatform.xhxw.ui.Constants;
import com.cplatform.xhxw.ui.R;
import com.cplatform.xhxw.ui.model.New;
import com.cplatform.xhxw.ui.util.DateUtil;
import com.cplatform.xhxw.ui.util.DisplayImageOptionsUtil;
import com.cplatform.xhxw.ui.util.TextViewUtil;

import java.util.List;

/**
 * 新闻列表图集布局 Created by cy-love on 13-12-25.
 */
public class NewsMultiHorizontalItem extends RelativeLayout {

	TextView mTitle;
	ImageView mImg1;
	ImageView mImg2;
	ImageView mImg3;
	View mAimgs;
	TextView mComment;
	LinearLayout mCommentLo;
	TextView mSourceTv;
	TextView mDateTv;
	
	private OnMultiImgOnClickListener mListener;
	private New mItem;

	public interface OnMultiImgOnClickListener {

		/**
		 * 图片点击事件回调
		 * 
		 * @param v
		 * @param item
		 *            视图对应的源数据
		 * @param index
		 *            点击的图片位置 分别为 0、1、2
		 */
		public void onMultiImgOnClick(View v, New item, int index);
	}

	public NewsMultiHorizontalItem(Context context) {
		super(context);
		this.init();
	}

	private void init() {
		LayoutInflater inflater = (LayoutInflater) getContext()
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View root = inflater.inflate(R.layout.view_news_multi_horizontal_item, this);
		initViews(root);
		ViewGroup.LayoutParams lp = mAimgs.getLayoutParams();
		DisplayMetrics spaceWidth = getContext().getResources().getDisplayMetrics();
		lp.height = (int)(((Constants.screenWidth - 30 * spaceWidth.density) / 3) * (23 * 1.0 / 30));
		mAimgs.setLayoutParams(lp);
	}

	private void initViews(View root) {
		mTitle = (TextView) root.findViewById(R.id.tv_title);
		mImg1 = (ImageView) root.findViewById(R.id.iv_img1);
		mImg2 = (ImageView) root.findViewById(R.id.iv_img2);
		mImg3 = (ImageView) root.findViewById(R.id.iv_img3);
		mAimgs = root.findViewById(R.id.ly_aimgs);
		mComment = (TextView) root.findViewById(R.id.tv_comment);
		mCommentLo = (LinearLayout) root.findViewById(R.id.news_item_comment_lo);
		mSourceTv = (TextView) root.findViewById(R.id.news_item_source_tv);
		mDateTv = (TextView) root.findViewById(R.id.news_list_date_tv);
		
		mImg1.setOnClickListener(mOnclick);
		mImg2.setOnClickListener(mOnclick);
		mImg3.setOnClickListener(mOnclick);
	}
	
	OnClickListener mOnclick = new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			int index = 0;
			if(v.getId() == R.id.iv_img1) {
				index = 0;
			} else if(v.getId() == R.id.iv_img2) {
				index = 1;
			} else if(v.getId() == R.id.iv_img3) {
				index = 2;
			}
			
			if (mListener != null && mItem != null)
				mListener.onMultiImgOnClick(v, mItem, index);
		}
	};

	public void setData(New item, boolean isToShowTopBg, boolean isToShowBottomBg) {
		
		mItem = item;
		TextViewUtil.setDisplayModel(getContext(), mTitle, item.isRead());
		List<String> pics = item.getMinipics();
		int i = 0;
		if (pics != null) {
			for (String pic : pics) {
				setPic(i++, pic);
			}
		}
		while (i < 3) {
			setPic(i++, null);
		}
		
		mTitle.setTextSize(NewItem.getTitleTextSize());
		mTitle.setSingleLine(true);
		mTitle.setText(item.getTitle());
		mCommentLo.setVisibility(View.VISIBLE);
		if ("1".equals(item.getIscomment())) {
			mCommentLo.setVisibility(View.INVISIBLE);
		} else {
			String com = (!TextUtils.isEmpty(item.getCommentcount())) ? String
					.format(getString(R.string.comment_count_string_format),
							item.getCommentcount()) : null;
			if (Integer.valueOf(item.getCommentcount()) < Constants.NEWS_LIST_SHOW_COMMENT_NUMBER_LOWER_BOUND) {
				mCommentLo.setVisibility(View.INVISIBLE);
			} else {
				mComment.setText(com);
			}
		}

		mSourceTv.setText(item.getShowSource());
		mDateTv.setText(DateUtil.getXHAPPDetailFormmatString(
				TextUtils.isEmpty(item.getPublished()) ? 0L :
					Long.valueOf(item.getPublished())));
	}

	private String getString(int resId) {
		return getResources().getString(resId);
	}

	private void setPic(int index, String uri) {
		ImageView img;
		switch (index) {
		case 0:
			img = mImg1;
			break;
		case 1:
			img = mImg2;
			break;
		case 2:
			img = mImg3;
			break;
		default:
			return;
		}

		if (!TextUtils.isEmpty(uri)) {
			ImageLoader.getInstance().displayImage(uri, img,
					DisplayImageOptionsUtil.newsMultiHorImgOptions);
		} else {
			img.setImageResource(R.drawable.ic_test);
		}
	}

	public void setOnMultiImgOnClickListener(OnMultiImgOnClickListener lis) {
		this.mListener = lis;
	}

}
