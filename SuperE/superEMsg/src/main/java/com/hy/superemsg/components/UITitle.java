package com.hy.superemsg.components;

import android.content.Context;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.hy.superemsg.R;

public class UITitle extends RelativeLayout {
	private TextView titleText;
	private ImageView leftBtn;
	private ImageView rightBtn1;
	private ImageView rightBtn2;

	public UITitle(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init();
	}

	public UITitle(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}

	public UITitle(Context context) {
		this(context, null);
	}

	public void setBackGround(int rid) {
		this.findViewById(R.id.title_background).setBackgroundResource(rid);
	}

	public void setBackGroundColor(int color) {
		this.findViewById(R.id.title_background).setBackgroundResource(0);
		this.findViewById(R.id.title_background).setBackgroundColor(color);
	}

	private void init() {
		LayoutInflater.from(getContext()).inflate(R.layout.ui_title_layout,
				this);
		this.setId(R.id.ui_title);
		leftBtn = (ImageView) this.findViewById(R.id.ui_title_lbtn);
		rightBtn1 = (ImageView) this.findViewById(R.id.ui_title_rbtn1);
		rightBtn2 = (ImageView) this.findViewById(R.id.ui_title_rbtn2);
		titleText = (TextView) this.findViewById(R.id.ui_title_text);
	}

	public void setTitleText(String title) {
		titleText.setText(title);
	}

	public void setTitleText(int stringResource) {
		titleText.setText(stringResource);
	}

	public void setLeftButton(int drawable, View.OnClickListener listener) {
		if (drawable == 0) {
			leftBtn.setVisibility(View.INVISIBLE);
		} else {
			leftBtn.setVisibility(View.VISIBLE);
			leftBtn.setImageResource(drawable);
			leftBtn.setOnClickListener(listener);
		}
	}

	public void setRightButton1(int drawable, View.OnClickListener listener) {
		if (drawable == 0) {
			rightBtn1.setVisibility(View.INVISIBLE);
		} else {
			rightBtn1.setVisibility(View.VISIBLE);
			rightBtn1.setImageResource(drawable);
			rightBtn1.setOnClickListener(listener);
		}
	}

	public void setRightButton2(int drawable, View.OnClickListener listener) {
		if (drawable == 0) {
			rightBtn2.setVisibility(View.INVISIBLE);
		} else {
			rightBtn2.setVisibility(View.VISIBLE);
			rightBtn2.setImageResource(drawable);
			rightBtn2.setOnClickListener(listener);
		}
	}
}
