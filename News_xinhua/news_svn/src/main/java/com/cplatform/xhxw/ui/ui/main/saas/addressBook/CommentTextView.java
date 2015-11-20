package com.cplatform.xhxw.ui.ui.main.saas.addressBook;

import android.content.Context;
import android.graphics.Color;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.cplatform.xhxw.ui.Constants;
import com.cplatform.xhxw.ui.R;
import com.cplatform.xhxw.ui.ui.detailpage.expressions.XWExpressionUtil;

/**
 * 朋友圈评论
 */
public class CommentTextView extends LinearLayout implements OnClickListener{

	private Object mTag;
	private int mIndex, mPosi;
	private OnCommentClickListener mLis;
	private TextView mContent;

	public CommentTextView(Context context) {
		super(context);
		this.init();
	}

	public void setData(int posi,String content, String uid, String userName, String replyUserId, String replyUserName) {
		SpannableStringBuilder span = new SpannableStringBuilder();
		String userId = Constants.getUid();
		if (!TextUtils.isEmpty(uid)) {

			if (userId.equals(uid)) {
				span.append("我");
			} else if (!TextUtils.isEmpty(userName)) {
				span.append(userName);
			}
			span.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.commentary_name_color)),
					0, span.length(),
					Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
			if (!TextUtils.isEmpty(replyUserId)) {
				span.append("回复");
                if (replyUserId.equals(uid)) {
                    span.append("我");
                    span.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.commentary_name_color)),
                            span.length()-"我".length(), span.length(),
                            Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
                } else if (!TextUtils.isEmpty(replyUserName)) {
					span.append(replyUserName);
					span.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.commentary_name_color)),
							span.length()-replyUserName.length(), span.length(),
							Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
				}
			}
			span.append(" : ");
		}
		span.append(getMsg(content,(int)mContent.getTextSize()));
		mContent.setText(span);
		mPosi = posi;
	}

    /**
     * 处理消息内容中的表情
     */
    private SpannableString getMsg(String msg, int textSize) {
        return XWExpressionUtil.generateSpanComment(
                getContext(), msg,textSize);
    }

	private void init() {
		LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context
				.LAYOUT_INFLATER_SERVICE);
		inflater.inflate(R.layout.view_comment_text, this);
		mContent = (TextView) findViewById(R.id.tv_content);
		mContent.setTextColor(Color.BLACK);
		setOnClickListener(this);
	}

	/**
	 * 设置回调的数据
	 * @param tag 
	 * @param index
	 */
	public void setOnClickData(Object tag, int index) {
		mTag = tag;
		mIndex = index;
	}

	/**
	 * 点击回调
	 */
	public void setOnCommentClickListener(OnCommentClickListener lis) {
		mLis = lis;
	}

	@Override
	public void onClick(View v) {
		if (mLis != null) {
			mLis.onCommentClick(mPosi, mTag, mIndex);
		}
	}

}
