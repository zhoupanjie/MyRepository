package com.cplatform.xhxw.ui.ui.base.view;


import com.cplatform.xhxw.ui.R;
import com.cplatform.xhxw.ui.util.DensityUtil;

import android.app.Activity;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Handler;
import android.text.TextUtils;
import android.text.TextUtils.TruncateAt;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.PopupWindow;
import android.widget.TextView;

public class FriendsActionSheet extends PopupWindow {

	private IListener listener;
	private View contentView;
	private Activity context;
	private boolean isDismissed = false;
	private View layoutContent;
	private String title;
	private TextView tvTitle;

	private FriendsActionSheet(View contentView, int width, int height,
			boolean focusable) {
		super(contentView, width, height, focusable);
	}

	public static FriendsActionSheet getInstance(Activity baseActivity) {
		View contentView = LayoutInflater.from(baseActivity).inflate(
				R.layout.friends_action_sheet, null);
		final FriendsActionSheet actionSheet = new FriendsActionSheet(contentView,
				ViewGroup.LayoutParams.FILL_PARENT,
				ViewGroup.LayoutParams.FILL_PARENT, true);

		actionSheet.contentView = contentView;
		actionSheet.context = baseActivity;
		actionSheet.tvTitle = (TextView) contentView
				.findViewById(R.id.tv_title);
		actionSheet.layoutContent = contentView
				.findViewById(R.id.layout_content);

		actionSheet.contentView.setOnTouchListener(new View.OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				if (event.getAction() == MotionEvent.ACTION_DOWN) {
					actionSheet.dismiss();
				}
				return true;
			}
		});
		actionSheet.layoutContent
				.setOnTouchListener(new View.OnTouchListener() {

					@Override
					public boolean onTouch(View v, MotionEvent event) {
						return true;
					}
				});

		actionSheet.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
		actionSheet.setAnimationStyle(R.style.action_sheet_no_animation);

		return actionSheet;
	}

	public void show() {
		if (context.getWindow().isActive()) {
			showAtLocation(context.getWindow().getDecorView(), Gravity.BOTTOM,
					0, 0);

			Animation animation = AnimationUtils.loadAnimation(context,
					R.anim.actionsheet_bottom_in);
			animation.setFillEnabled(true);
			animation.setFillAfter(true);
			layoutContent.startAnimation(animation);
		} else {
			new Handler().postDelayed(new Runnable() {

				@Override
				public void run() {
					if (context.getWindow().isActive()) {
						showAtLocation(context.getWindow().getDecorView(),
								Gravity.BOTTOM, 0, 0);

						Animation animation = AnimationUtils.loadAnimation(
								context, R.anim.actionsheet_top_in);
						animation.setFillEnabled(true);
						animation.setFillAfter(true);
						layoutContent.startAnimation(animation);
					}
				}
			}, 600);
		}

	}

	/**
	 * 必须在setItems之前调用
	 *
	 * @param title
	 */
	public void setTitle(String title) {
		this.title = title;
		tvTitle.setText(title);
		tvTitle.setVisibility(View.VISIBLE);
	}

    /**
     * 设置操作选项
     * @param items 操作项
     */
    public void setItems(String[] items) {
        setItems(items, null);
    }

    /**
     * 设置操作选项
     * @param items 操作项
     * @param bgs 操作项背景
     */
	public void setItems(String[] items, int[] bgs) {
		LinearLayout layoutBtn = (LinearLayout) contentView
				.findViewById(R.id.layout_btn);
		int size = items.length;

		int btnHeight = DensityUtil.dip2px(context, 40);
		int btnMarginTop = DensityUtil.dip2px(context, 10);

		for (int i = 0; i < size; i++) {
			final int index = i;
			final String item = items[i];
			Button btn = new Button(context);
			btn.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 15);
			btn.setTextColor(Color.WHITE);
			btn.setEllipsize(TruncateAt.END);
			btn.setSingleLine(true);
//			btn.setBackgroundDrawable(context.getResources().getDrawable(
//					R.drawable.ic_launcher));
            int bgRes;
            if (bgs != null) {
                bgRes = bgs[i];
            } else {
                bgRes = R.drawable.btn_cancle;
            }
			btn.setBackgroundResource(bgRes);
			btn.setText(item);

			LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
					LinearLayout.LayoutParams.FILL_PARENT, btnHeight);
			if (i != 0) {
				lp.topMargin = btnMarginTop;
			}
			btn.setLayoutParams(lp);

			btn.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View arg0) {
					dismiss();
					if (listener != null) {
						listener.onItemClicked(index, item);
					}
				}
			});

			layoutBtn.addView(btn);
		}

		// 调整背景图片大小
		int height = items.length * btnHeight + (items.length - 1)
				* btnMarginTop;
		LinearLayout.LayoutParams lp = (LayoutParams) layoutBtn
				.getLayoutParams();
		height += (lp.topMargin + lp.bottomMargin);

		if (!TextUtils.isEmpty(title)) {
			LinearLayout.LayoutParams lpTitle = (LayoutParams) tvTitle
					.getLayoutParams();
			height += (lpTitle.height + lpTitle.topMargin + lpTitle.bottomMargin);
		}

		lp = (LayoutParams) layoutContent.getLayoutParams();
		lp.height = height + layoutContent.getPaddingTop()
				+ layoutContent.getPaddingBottom();

		layoutContent.setLayoutParams(lp);
	}

	public interface IListener {
		public void onItemClicked(int index, String item);
	}

	public void setListener(IListener listener) {
		this.listener = listener;
	}

	@Override
	public void dismiss() {
		if (isDismissed) {
			return;
		}
		isDismissed = true;
		Animation animation = AnimationUtils.loadAnimation(context,
				R.anim.actionsheet_bottom_out);
		animation.setFillEnabled(true);
		animation.setFillAfter(true);
		animation.setAnimationListener(new AnimationListener() {

			@Override
			public void onAnimationStart(Animation arg0) {

			}

			@Override
			public void onAnimationRepeat(Animation arg0) {

			}

			@Override
			public void onAnimationEnd(Animation arg0) {
				layoutContent.setVisibility(View.INVISIBLE);
				new Handler().post(new Runnable() {

					@Override
					public void run() {
						try {
							superDismiss();
						} catch (Exception e) {

						}
					}
				});
			}
		});
		layoutContent.startAnimation(animation);
	}

	public void superDismiss() {
		super.dismiss();
	}
}
