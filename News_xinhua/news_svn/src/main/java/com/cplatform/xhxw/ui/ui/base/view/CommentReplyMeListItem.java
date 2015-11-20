package com.cplatform.xhxw.ui.ui.base.view;

import java.util.List;

import com.cplatform.xhxw.ui.R;
import com.cplatform.xhxw.ui.model.CommentReplyMe;
import com.cplatform.xhxw.ui.model.ReplyComment;
import com.cplatform.xhxw.ui.ui.detailpage.expressions.XWExpressionUtil;
import com.cplatform.xhxw.ui.util.DateUtil;
import com.cplatform.xhxw.ui.util.LogUtil;
import com.cplatform.xhxw.ui.util.StringUtil;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class CommentReplyMeListItem extends LinearLayout implements View.OnClickListener {
	
	private Context mCon;
	private TextView mNewsTitleTv;
	private ImageView mMyAvatarIv;
	private TextView mNickNameTv;
	private TextView mMyCommentTimeTv;
	private Button mPraiseCountBtn;
	private TextView mMyCommentContent;
	
	private ImageView mReply1Avatar;
	private TextView mReply1Content;
	
	private LinearLayout mReply2Lo;
	private ImageView mReply2Avatar;
	private TextView mReply2Content;
	
	private ImageView mShowMoreReplyIv;
	
	private CommentReplyMe mShowSource;
	private DisplayImageOptions opts = new DisplayImageOptions.Builder()
				.showStubImage(R.drawable.comment_userimage)
				.showImageForEmptyUri(R.drawable.comment_userimage)
				.showImageOnFail(R.drawable.comment_userimage)
				.displayer(new RoundedBitmapDisplayer(5)).cacheInMemory()
				.cacheOnDisc().build();
	private onShowMoreReplyClickListener onShowMoreReplyClickLis;

	public CommentReplyMeListItem(Context context, AttributeSet attrs,
			int defStyle) {
		super(context, attrs, defStyle);
		initViews(context);
	}

	public CommentReplyMeListItem(Context context, AttributeSet attrs) {
		super(context, attrs);
		initViews(context);
	}

	public CommentReplyMeListItem(Context context) {
		super(context);
		initViews(context);
	}

	private void initViews(Context con) {
		mCon = con;
		LayoutInflater lif = (LayoutInflater) mCon.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View rootView = lif.inflate(R.layout.item_comment_reply, null);
		mNewsTitleTv = (TextView) rootView.findViewById(R.id.comment_reply_news_title_tv);
		mMyAvatarIv = (ImageView) rootView.findViewById(R.id.comment_reply_my_avatar);
		mNickNameTv = (TextView) rootView.findViewById(R.id.comment_reply_my_nickname);
		mMyCommentTimeTv = (TextView) rootView.findViewById(R.id.comment_reply_my_sent_time);
		mPraiseCountBtn = (Button) rootView.findViewById(R.id.comment_reply_praise_count);
		mMyCommentContent = (TextView) rootView.findViewById(R.id.comment_reply_my_content);
		
		mReply1Avatar = (ImageView) rootView.findViewById(R.id.comment_reply1_avatar);
		mReply1Content = (TextView) rootView.findViewById(R.id.comment_reply_reply1_content);
		
		mReply2Lo = (LinearLayout) rootView.findViewById(R.id.comment_reply_reply_lo2);
		mReply2Avatar = (ImageView) rootView.findViewById(R.id.comment_reply2_avatar);
		mReply2Content = (TextView) rootView.findViewById(R.id.comment_reply_reply2_content);
		
		mShowMoreReplyIv = (ImageView) rootView.findViewById(R.id.comment_reply_load_more);
		mShowMoreReplyIv.setOnClickListener(this);
		addView(rootView);
	}

	public CommentReplyMe getmShowSource() {
		return mShowSource;
	}

	public void setmShowSource(CommentReplyMe mShowSource) {
		this.mShowSource = mShowSource;
		initData(this.mShowSource);
	}
	
	private void initData(CommentReplyMe data) {
		if(data == null) {
			return;
		}
		
		try {
			mNewsTitleTv.setText(data.getNewsTitle());
			ImageLoader.getInstance().displayImage(data.getLogo(), mMyAvatarIv, opts);
			mNickNameTv.setText(data.getNickname());
			mMyCommentTimeTv.setText(DateUtil.getCommentFormattedDate(Long.valueOf(data.getPublished())));
			mPraiseCountBtn.setText(data.getPraiseCount());
			mMyCommentContent.setText(XWExpressionUtil.generateSpanComment(
					mCon, data.getContent(), (int) mMyCommentContent.getTextSize()));
			if(data.getIsmore().equals("1")) {
				mShowMoreReplyIv.setVisibility(View.VISIBLE);
			} else {
				mShowMoreReplyIv.setVisibility(View.GONE);
			}
			
			List<ReplyComment> replies = data.getList();
			if(replies == null) return;
			mReply2Lo.setVisibility(View.GONE);
			ImageLoader.getInstance().displayImage(replies.get(0).getLogo(), 
					mReply1Avatar, opts);
			mReply1Content.setText("");
			mReply1Content.append(StringUtil.generateSpanComment(replies.get(0).getNickname() + "回复："));
			mReply1Content.append(XWExpressionUtil.generateSpanComment(
					mCon, replies.get(0).getContent(), (int) mReply1Content.getTextSize()));
			if(replies.size() > 1) {
				mReply2Lo.setVisibility(View.VISIBLE);
				ImageLoader.getInstance().displayImage(replies.get(1).getLogo(), 
						mReply2Avatar, opts);
				mReply2Content.setText("");
				mReply2Content.append(StringUtil.generateSpanComment(replies.get(1).getNickname() + "回复："));
				mReply2Content.append(XWExpressionUtil.generateSpanComment(
						mCon, replies.get(1).getContent(), (int) mReply2Content.getTextSize()));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public onShowMoreReplyClickListener getOnShowMoreReplyClickLis() {
		return onShowMoreReplyClickLis;
	}

	public void setOnShowMoreReplyClickLis(onShowMoreReplyClickListener onShowMoreReplyClickLis) {
		this.onShowMoreReplyClickLis = onShowMoreReplyClickLis;
	}

	public interface onShowMoreReplyClickListener {
		void onMoreReplyClick(CommentReplyMe data);
	}

	@Override
	public void onClick(View v) {
		if(v.getId() == R.id.comment_reply_load_more) {
			if(onShowMoreReplyClickLis != null) {
				onShowMoreReplyClickLis.onMoreReplyClick(mShowSource);
			}
		}
	}
}
