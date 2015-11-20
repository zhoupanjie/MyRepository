package com.cplatform.xhxw.ui.ui.base.adapter;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;
import com.cplatform.xhxw.ui.R;
import com.cplatform.xhxw.ui.model.Comment;
import com.cplatform.xhxw.ui.ui.base.BaseAdapter;
import com.cplatform.xhxw.ui.ui.base.widget.CircleImageView;
import com.cplatform.xhxw.ui.ui.detailpage.expressions.XWExpressionUtil;
import com.cplatform.xhxw.ui.util.CommonUtils;
import com.cplatform.xhxw.ui.util.DateUtil;
import com.cplatform.xhxw.ui.util.StringUtil;

public class CommentAdapter extends BaseAdapter<Comment>{

	private Context context;
	private OnShowActionSheetListener onShowActionSheetListener;
	private int count;
	private int mHotCount;
	private int mTotalCount;
	
	private DisplayImageOptions opts = new DisplayImageOptions.Builder()
				.showStubImage(R.drawable.comment_userimage)
				.showImageForEmptyUri(R.drawable.comment_userimage)
				.showImageOnFail(R.drawable.comment_userimage)
				.displayer(new RoundedBitmapDisplayer(5)).cacheInMemory()
				.cacheOnDisc().build();
	
	public CommentAdapter(Context context) {
		super(context);
		this.context = context;
	}

	@Override
	public View getView(final int position, View view, ViewGroup arg2) {

		final Comment comment = getItem(position);
		ViewHodler hodler;
		if (view == null) {
			hodler = new ViewHodler();
			view = LayoutInflater.from(mContext).inflate(R.layout.comment_item, null);
			
			hodler.commentImage = (CircleImageView) view.findViewById(R.id.comment_image);
			hodler.commentTitle = (TextView) view.findViewById(R.id.comment_title);
			hodler.commentTime = (TextView) view.findViewById(R.id.comment_time);
			hodler.commentText = (TextView) view.findViewById(R.id.comment_text);
			hodler.commentType = (TextView) view.findViewById(R.id.type);
			hodler.praiseCount = (Button) view.findViewById(R.id.comment_phraise_count_btn);
			hodler.view = (View) view.findViewById(R.id.view);
			hodler.linearLayout = (LinearLayout) view.findViewById(R.id.linear);
			
			view.setTag(hodler);
		}else {
			hodler = (ViewHodler) view.getTag();
		}
		
		if (count == 0) {//只有"最新",没有最热
			if (position == 0) {
				//显示最新的图标
				hodler.commentType.setBackgroundResource(R.drawable.comment_new);
				setLableDisplay(hodler.commentType, false);
				hodler.commentType.setVisibility(View.VISIBLE);
				hodler.view.setVisibility(View.VISIBLE);
			}else {
				//隐藏最新图标
				hodler.commentType.setBackgroundResource(R.drawable.comment_new);
				setLableDisplay(hodler.commentType, false);
				hodler.commentType.setVisibility(View.GONE);
				hodler.view.setVisibility(View.GONE);
			}
		}else if (count == getCount()) {// 只有“最热”，没有“最新”
			if (position == 0) {
				//显示最热图标
				hodler.commentType.setBackgroundResource(R.drawable.comment_hot);
				setLableDisplay(hodler.commentType, true);
				hodler.commentType.setVisibility(View.VISIBLE);
				hodler.view.setVisibility(View.VISIBLE);
			}else {
				//隐藏最热图标
				hodler.commentType.setBackgroundResource(R.drawable.comment_hot);
				setLableDisplay(hodler.commentType, true);
				hodler.commentType.setVisibility(View.GONE);
				hodler.view.setVisibility(View.GONE);
			}
		}else if (0 < count && count < getCount()){//既有“最热”，也有“最新”
			if (position == 0) {
				//显示最热
				hodler.commentType.setBackgroundResource(R.drawable.comment_hot);
				setLableDisplay(hodler.commentType, true);
				hodler.commentType.setVisibility(View.VISIBLE);
				hodler.view.setVisibility(View.VISIBLE);
			}else if(0 < position && position < count){
				//隐藏最热
				hodler.commentType.setBackgroundResource(R.drawable.comment_hot);
				setLableDisplay(hodler.commentType, true);
				hodler.commentType.setVisibility(View.GONE);
				hodler.view.setVisibility(View.GONE);
			}else if (position == count) {
				//显示最新
				hodler.commentType.setBackgroundResource(R.drawable.comment_new);
				setLableDisplay(hodler.commentType, false);
				hodler.commentType.setVisibility(View.VISIBLE);
				hodler.view.setVisibility(View.VISIBLE);
			}else if (position > count) {
				//隐藏最新
				hodler.commentType.setBackgroundResource(R.drawable.comment_new);
				setLableDisplay(hodler.commentType, false);
				hodler.commentType.setVisibility(View.GONE);
				hodler.view.setVisibility(View.GONE);
			}
		}
		if(comment.getPraise().equals("2")) {
			Drawable nav_up = this.context.getResources().getDrawable(R.drawable.comment_praise);  
			nav_up.setBounds(0, 0, nav_up.getMinimumWidth(), nav_up.getMinimumHeight());
			hodler.praiseCount.setCompoundDrawables(null, null, nav_up, null);
		} else {
			Drawable nav_up = this.context.getResources().getDrawable(R.drawable.comment_un_praise);  
			nav_up.setBounds(0, 0, nav_up.getMinimumWidth(), nav_up.getMinimumHeight());
			hodler.praiseCount.setCompoundDrawables(null, null, nav_up, null);
		}
		int praiseCount = comment.getPraiseCount() == null ? 0 : Integer.valueOf(comment.getPraiseCount());
		hodler.praiseCount.setText("" + praiseCount);
		
		if (comment.getPublished() != null) {
			hodler.commentTime.setText(DateUtil.getXHAPPDetailFormmatString(Long.valueOf(comment.getPublished()) * 1000));
		}
		
		ImageLoader.getInstance().displayImage(comment.getLogo(),
				hodler.commentImage, opts);
		
		if (comment.getSnickName() != null) {
			hodler.commentTitle.setText(comment.getSnickName());
		} else {
			hodler.commentTitle.setText("匿名用户");
		}
		
		String cotent = comment.getContent();
		hodler.commentText.setText("");
		if (cotent != null) {
			if(comment.getRnickName() != null && !TextUtils.isEmpty(comment.getRnickName())) {
				hodler.commentText.append(StringUtil.generateSpanComment("回复 " + comment.getRnickName() + "："));
			}
			hodler.commentText.append(XWExpressionUtil.generateSpanComment(
					context, cotent, 
					(int) hodler.commentText.getTextSize()));
		}
		
		hodler.praiseCount.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if (onShowActionSheetListener != null) {
					onShowActionSheetListener.onPraiseComment(comment);
				}
			}
		});
		
		hodler.linearLayout.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				if (CommonUtils.isFastDoubleClick()) {
					return;
				}
				if (onShowActionSheetListener != null) {
					onShowActionSheetListener.OnShowActionSheet(position);
				}
			}
		});
		
		return view;
	}

	private class ViewHodler{
		TextView commentType;
		CircleImageView commentImage;
		TextView commentTitle;
		TextView commentTime;
		TextView commentText;
		Button praiseCount;
		LinearLayout linearLayout;
		View view;
	}
	
	public void setHotCount(int count) {
		this.count = count;
	}
	
	public int getHotCount() {
		return count;
	}
	
	/**
	 * 评论一条新闻
	 * 
	 * @param sunxiaoguang
	 */
	
	public interface OnShowActionSheetListener{
//		public void OnShowCommentNews(Comment comment, String msg, String hint);
		
		public void OnShowActionSheet(int position);
		public void onPraiseComment(Comment comment);
	}
	
	public void setOnShowCommentNewsListener(OnShowActionSheetListener onShowActionSheetListener) {
		this.onShowActionSheetListener = onShowActionSheetListener;
	}
	
	public void setCommentCount(int hotCount, int TotalCount) {
		this.mHotCount = hotCount;
		this.mTotalCount = TotalCount;
	}
	
	private void setLableDisplay(TextView view, boolean isHotComment) {
		if(isHotComment) {
			if(mHotCount > 0) {
				view.setText(mContext.getString(R.string.comment_hot_lable, mHotCount));
			} else {
				view.setText("最热");
			}
		} else {
			if(mTotalCount > 0) {
				view.setText(mContext.getString(R.string.comment_new_lable, mTotalCount));
			} else {
				view.setText("最新");
			}
		}
	}
}
