package com.cplatform.xhxw.ui.ui.base.view;

import com.cplatform.xhxw.ui.Constants;
import com.cplatform.xhxw.ui.R;
import com.cplatform.xhxw.ui.model.New;
import com.cplatform.xhxw.ui.util.DisplayImageOptionsUtil;
import com.nostra13.universalimageloader.core.ImageLoader;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

public class NewsAdvertiseItem extends LinearLayout {
	private ImageView mAdverImag;

	public NewsAdvertiseItem(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init();
	}

	public NewsAdvertiseItem(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
	}

	public NewsAdvertiseItem(Context context) {
		super(context);
		init();
	}
	
	private void init() {
		LayoutInflater inflater = (LayoutInflater) getContext()
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View root = inflater.inflate(R.layout.view_news_ad_item, this);
		mAdverImag = (ImageView) root.findViewById(R.id.view_news_ad_iv);
		
	}
	
	public void setData(New item) {
		int caHei = getAdDisplayHeight(item);
		if(caHei > 0) {
			LinearLayout.LayoutParams lllp = new LinearLayout.LayoutParams(
					Constants.screenWidth, caHei);
			mAdverImag.setLayoutParams(lllp);
		}
		ImageLoader.getInstance().displayImage(item.getBigthumbnail(), mAdverImag,
				DisplayImageOptionsUtil.listNewImgOptions);
	}
	
private int getAdDisplayHeight(New item) {
		int height = 0;
		if(item.getWidth() > 0 && item.getHeight() > 0) {
			height = (int) ((Constants.screenWidth * item.getHeight() * 1.0f) / item.getWidth());
		}
		return height;
	}
}
