package com.cplatform.xhxw.ui.util;

import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;
import com.cplatform.xhxw.ui.R;

/**
 * 社区操作
 * Created by cy-love on 14-8-28.
 */
public class CompanyOptionPop {

    private PopupWindow mPop;
    private View mLike, mCommentary;

    public CompanyOptionPop(Context context, boolean isLiked) {
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View contentView = inflater.inflate(R.layout.view_company_circle_option, null);
        ImageView like = (ImageView) contentView.findViewById(R.id.btn_like);
        TextView likeStatus = (TextView) contentView.findViewById(R.id.tv_like);
		int likeRes = 0;
		if (isLiked) {
			likeRes = R.drawable.btn_option_like;
			likeStatus.setText(context.getString(R.string.popu_fail));
		} else {
			likeRes = R.drawable.btn_option_like;
			likeStatus.setText(context.getString(R.string.like));
		}
		like.setImageResource(likeRes);
        
        contentView.findViewById(R.id.ly_bg).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        mLike = contentView.findViewById(R.id.ly_like);
        mCommentary = contentView.findViewById(R.id.ly_commentary);
        mPop = new PopupWindow(contentView, ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.WRAP_CONTENT);
        mPop.setFocusable(true);
        mPop.setOutsideTouchable(true);
        mPop.setBackgroundDrawable(new BitmapDrawable());


    }

    public CompanyOptionPop show(View v) {
        int[] location = new int[2];
        v.getLocationOnScreen(location);
        mPop.showAtLocation(v, Gravity.NO_GRAVITY, location[0]-mPop.getWidth(), location[1] - v.getHeight() / 2);
        return this;
    }

    public CompanyOptionPop setLis(View.OnClickListener likeLis, View.OnClickListener comLis) {
        mLike.setOnClickListener(likeLis);
        mCommentary.setOnClickListener(comLis);
        return this;
    }

    public void dismiss() {
        mPop.dismiss();
    }
}
