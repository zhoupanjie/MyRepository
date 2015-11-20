package com.cplatform.xhxw.ui.util;

import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.cplatform.xhxw.ui.R;

/**
 * 新鲜事详情操作
 * 
 * 赞和评论
 */
public class FriendsFreshInfoNativePopu {

	private PopupWindow mPop;
	private ImageView mLike, mCommentary;

	/** 赞的布局 */
	private LinearLayout ly_like;
	/** 评论的布局 */
	private LinearLayout ly_commentary;
	private TextView tv_like;

	public FriendsFreshInfoNativePopu(Context context, boolean isLiked) {
		LayoutInflater inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View contentView = inflater.inflate(
				R.layout.view_company_circle_option, null);
		mLike = (ImageView) contentView.findViewById(R.id.btn_like);
		tv_like = (TextView) contentView.findViewById(R.id.tv_like);
		int likeRes = 0;
		if (isLiked) {
			likeRes = R.drawable.btn_option_like;
			tv_like.setText(context.getString(R.string.popu_fail));
		} else {
			likeRes = R.drawable.btn_option_like;
			tv_like.setText(context.getString(R.string.like));
		}
		mLike.setImageResource(likeRes);
		mCommentary = (ImageView) contentView.findViewById(R.id.btn_commentary);

		ly_like = (LinearLayout) contentView.findViewById(R.id.ly_like);
		ly_commentary = (LinearLayout) contentView
				.findViewById(R.id.ly_commentary);

		mPop = new PopupWindow(contentView,
				ViewGroup.LayoutParams.WRAP_CONTENT,
				ViewGroup.LayoutParams.WRAP_CONTENT);
		mPop.setFocusable(true);
		mPop.setOutsideTouchable(true);
		mPop.setBackgroundDrawable(new BitmapDrawable());
	}

	public FriendsFreshInfoNativePopu show(View v) {
		int[] location = new int[2];
		v.getLocationOnScreen(location);
		mPop.showAtLocation(v, Gravity.NO_GRAVITY,
				location[0] - mPop.getWidth(), location[1]);
		return this;
	}

	public FriendsFreshInfoNativePopu setListener(View.OnClickListener likeLis,
			View.OnClickListener comLis) {
		ly_like.setOnClickListener(likeLis);
		ly_commentary.setOnClickListener(comLis);
		return this;
	}

	public void dismiss() {
		mPop.dismiss();
	}
}
