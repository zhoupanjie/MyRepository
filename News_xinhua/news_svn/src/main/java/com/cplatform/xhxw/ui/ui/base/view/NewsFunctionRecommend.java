package com.cplatform.xhxw.ui.ui.base.view;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

import com.cplatform.xhxw.ui.Constants;
import com.cplatform.xhxw.ui.R;
import com.cplatform.xhxw.ui.model.FunctionRecommend;

/**
 * 
 * @ClassName NewsFunctionRecommend 功能推荐
 * @Description TODO
 * @Version 1.0
 * @Author XingEn
 * @Creation 2015年4月23日 上午11:48:56
 * @Mender Administrator
 * @Modification 2015年4月23日 上午11:48:56
 * @Copyright Copyright © 2012 - 2015 Petro-CyberWorks Information Technology
 *            Company Limlted.All Rights Reserved.
 * 
 */
public class NewsFunctionRecommend extends RelativeLayout {

//	@InjectView(R.id.tv_tag)
//	TextView mTag;
//	@InjectView(R.id.tv_title)
//	TextView mTitle;
	@InjectView(R.id.iv_img1)
	ImageView mImg1;
	@InjectView(R.id.iv_img2)
	ImageView mImg2;
	@InjectView(R.id.iv_img3)
	ImageView mImg3;
	@InjectView(R.id.iv_img4)
	ImageView mImg4;
	@InjectView(R.id.ly_aimgs)
	View mAimgs;

	private OnFunctionRecommendOnClickListener mListener;
	private List<FunctionRecommend> listFR;

	public interface OnFunctionRecommendOnClickListener {

		/**
		 * 图片点击事件回调
		 * 
		 * @param v
		 * @param item
		 *            视图对应的源数据
		 * @param index
		 *            点击的图片位置 分别为 0、1、2、3
		 */
		public void onFunctionRecommendOnClick(View v, FunctionRecommend item,
				int index);
	}

	public NewsFunctionRecommend(Context context) {
		super(context);
		this.init();
	}

	private void init() {
		LayoutInflater inflater = (LayoutInflater) getContext()
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		inflater.inflate(R.layout.view_news_function_recommend, this);
		ButterKnife.inject(this);
//		ViewGroup.LayoutParams lp = mAimgs.getLayoutParams();
//		DisplayMetrics spaceWidth = getContext().getResources()
//				.getDisplayMetrics();
//		lp.height = (int) (((Constants.screenWidth - 50 * spaceWidth.density) / 3) * (23 * 1.0 / 30));
//		mAimgs.setLayoutParams(lp);
		listFR = new ArrayList<FunctionRecommend>();
	}

	public void setData(FunctionRecommend item, boolean isToShowTopBg,
			boolean isToShowBottomBg) {
//		mTopBg.setVisibility(View.GONE);
//		mBottomBg.setVisibility(View.GONE);
//		if (isToShowTopBg) {
//			mTopBg.setVisibility(View.VISIBLE);
//		}
//		if (isToShowBottomBg) {
//			mBottomBg.setVisibility(View.VISIBLE);
//		}
		setPic(item.getIndex(), item.getImgId());
		listFR.add(item);
	}

	FunctionRecommend getItem(int i) {
		if (listFR != null && (listFR.size() >= i + 1)) {
			return listFR.get(i);
		} else {
			return null;
		}
	}

	@OnClick(R.id.iv_img1)
	public void img1OnClickAction(View v) {
		FunctionRecommend item = getItem(0);
		if (mListener != null && item != null)
			mListener.onFunctionRecommendOnClick(v, item, 0);
	}

	@OnClick(R.id.iv_img2)
	public void img2OnClickAction(View v) {
		FunctionRecommend item = getItem(1);
		if (mListener != null && item != null)
			mListener.onFunctionRecommendOnClick(v, item, 1);
	}

	@OnClick(R.id.iv_img3)
	public void img3OnClickAction(View v) {
		FunctionRecommend item = getItem(2);
		if (mListener != null && item != null)
			mListener.onFunctionRecommendOnClick(v, item, 2);
	}

	@OnClick(R.id.iv_img4)
	public void img4OnClickAction(View v) {
		FunctionRecommend item = getItem(3);
		if (mListener != null && item != null)
			mListener.onFunctionRecommendOnClick(v, item, 3);
	}

	private void setPic(int index, int imgId) {
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
		case 3:
			img = mImg4;
			break;
		default:
			return;
		}

		if (imgId != 0) {
			img.setImageResource(imgId);
			// ImageLoader.getInstance().displayImage(uri, img,
			// DisplayImageOptionsUtil.newsMultiHorImgOptions);
		} else {
			img.setImageResource(R.drawable.ic_test);
		}
	}

	public void setOnFunctionRecommendOnClickListener(
			OnFunctionRecommendOnClickListener lis) {
		this.mListener = lis;
	}

}
