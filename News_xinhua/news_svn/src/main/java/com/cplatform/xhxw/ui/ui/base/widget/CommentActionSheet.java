package com.cplatform.xhxw.ui.ui.base.widget;

import com.cplatform.xhxw.ui.Constants;
import com.cplatform.xhxw.ui.R;

import butterknife.ButterKnife;
import butterknife.Bind;

import android.app.Activity;
import android.graphics.drawable.BitmapDrawable;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.PopupWindow;

public class CommentActionSheet extends PopupWindow{

	private static OnCommentClickListener listener;
	private static Activity context;
	private LinearLayout touch;
	private LinearLayout reply;
	private LinearLayout delete;
	private LinearLayout mCancelPraiseLO;
	private LinearLayout copy;
	private boolean isDismissed = false;
	private static CommentActionSheet actionSheet;

	@Bind(R.id.popu_layout_content)
	LinearLayout linearLayout;
	
	@Bind(R.id.comment_touch_layout)
	LinearLayout touchLayout;
	
	@Bind(R.id.comment_cancel_touch_layout)
	LinearLayout mCommentCancelTouchLayout;
	
	@Bind(R.id.comment_reply_layout)
	LinearLayout replyLayout;
	
	@Bind(R.id.comment_delete_layout)
	LinearLayout deleteLayout;
	
	@Bind(R.id.comment_copy_layout)
	LinearLayout mCopyLayout;
	
	private CommentActionSheet(View contentView, int width, int height,
			boolean focusable) {
		super(contentView, width, height, focusable);
	}

	public static CommentActionSheet getInstance(Activity baseActivity, boolean isPraised, String userId) {
		context = baseActivity;
		View contentView = LayoutInflater.from(baseActivity).inflate(
				R.layout.comment_popuwindow, null);
		ButterKnife.bind(baseActivity);
		
		actionSheet = new CommentActionSheet(contentView,
				ViewGroup.LayoutParams.FILL_PARENT,
				ViewGroup.LayoutParams.WRAP_CONTENT, true);

		actionSheet.touch = (LinearLayout) contentView
				.findViewById(R.id.comment_touch_layout);
		actionSheet.mCancelPraiseLO = (LinearLayout) contentView
				.findViewById(R.id.comment_cancel_touch_layout);
		
		if(isPraised) {
			actionSheet.touch.setVisibility(View.GONE);
			actionSheet.mCancelPraiseLO.setVisibility(View.VISIBLE);
			actionSheet.mCancelPraiseLO.setOnTouchListener(new OnTouchListener() {
				
				@Override
				public boolean onTouch(View v, MotionEvent event) {
					actionSheet.dismiss();
					if (listener != null) {
						listener.onCommentCancelTouch();
					}
					return true;
				}
			});
		} else {
			actionSheet.touch.setVisibility(View.VISIBLE);
			actionSheet.mCancelPraiseLO.setVisibility(View.GONE);
			actionSheet.touch.setOnTouchListener(new OnTouchListener() {
				
				@Override
				public boolean onTouch(View v, MotionEvent event) {
					actionSheet.dismiss();
					if (listener != null) {
						listener.onCommentTouch();
					}
					return true;
				}
			});
		}
		
		actionSheet.reply = (LinearLayout) contentView
				.findViewById(R.id.comment_reply_layout);
		actionSheet.reply.setOnTouchListener(new OnTouchListener() {
			
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				actionSheet.dismiss();
				if (listener != null) {
					listener.onCommentReply();
				}
				return true;
			}
		});
		
		actionSheet.delete = (LinearLayout) contentView
				.findViewById(R.id.comment_delete_layout);
		actionSheet.delete.setOnTouchListener(new OnTouchListener() {
			
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				actionSheet.dismiss();
				if (listener != null) {
					listener.onCommentDelete();
				}
				return true;
			}
		});
		
		actionSheet.copy = (LinearLayout) contentView.findViewById(R.id.comment_copy_layout);
		actionSheet.copy.setOnTouchListener(new OnTouchListener() {
			
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				actionSheet.dismiss();
				if (listener != null) {
					listener.onCommentCopy();
				}
				return true;
			}
		});

		actionSheet.setBackgroundDrawable(new BitmapDrawable());
		actionSheet.setOutsideTouchable(true); 
		
		actionSheet.reply.setVisibility(View.VISIBLE);
		actionSheet.delete.setVisibility(View.VISIBLE);
		if(Constants.getUid().equals(userId)) {
			actionSheet.reply.setVisibility(View.GONE);
		} else {
			actionSheet.delete.setVisibility(View.GONE);
		}
		
		return actionSheet;
	}
	
	public void show(View view, int x, int y) {
		if (context.getWindow().isActive()) {
			/*showAtLocation(view, Gravity.BOTTOM,
					0, y);*/
			
			showAsDropDown(view, x, y);
		}
	}
	
	public interface OnCommentClickListener {
		public void onCommentTouch();
		public void onCommentCancelTouch();
		public void onCommentReply();
		public void onCommentDelete();
		public void onCommentCopy();
	}
	
	public void setOnCommentClickListener(OnCommentClickListener listener) {
		this.listener = listener;
	}

	@Override
	public void dismiss() {
		if (isDismissed) {
			return;
		}
		isDismissed = true;
		superDismiss();
	}

	public void superDismiss() {
		super.dismiss();
	}
}
