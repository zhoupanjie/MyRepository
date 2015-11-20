package com.cplatform.xhxw.ui.ui.main.saas.addressBook;

import java.util.List;

import android.annotation.TargetApi;
import android.os.Build;
import com.cplatform.xhxw.ui.Constants;
import com.cplatform.xhxw.ui.R;
import com.cplatform.xhxw.ui.util.DisplayImageOptionsUtil;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;

public class FriendInfoPhoto extends LinearLayout {

	private Context context;
	private View view;
	private LinearLayout linearLayout;

	public FriendInfoPhoto(Context context) {
		super(context);
		init(context);
	}

	public FriendInfoPhoto(Context context, AttributeSet attrs) {
		super(context, attrs);
		init(context);
	}

	@TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public FriendInfoPhoto(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init(context);
	}

	private void init(Context context) {
		this.context = context;

		LayoutInflater inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		view = inflate(context, R.layout.friend_info_photo, this);

		linearLayout = (LinearLayout) view
				.findViewById(R.id.friend_info_photo_layout);
	}

	public void setData(List<String> list) {
		if (list != null) {
			for (int i = 0; i < list.size(); i++) {
				if (i >= 3) {
					return;
				}
				ImageView imageView = new ImageView(context);
				imageView.setScaleType(ScaleType.CENTER_CROP);
				linearLayout.addView(imageView);

				LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) imageView
						.getLayoutParams();
				params.width = getImageWidth();
				params.height = getImageWidth();

				imageView.setLayoutParams(params);
				
				ImageLoader.getInstance().displayImage(list.get(i), imageView, DisplayImageOptionsUtil.avatarImagesOptions);
			}
		}

	}

	/** 获取每个图片可占用的宽度 */
	private int getImageWidth() {
		int width = (int) ((Constants.screenWidth - getDistance() * 2 - context
				.getResources().getDimensionPixelOffset(
						R.dimen.left_layout_width)) / 3);
		return width;
	}

	/** 获取每个图片之间的间隙 暂时用10dp计算 */
	private int getDistance() {
		int distance = context.getResources().getDimensionPixelOffset(
				R.dimen.left_right);
		return distance;
	}
}
