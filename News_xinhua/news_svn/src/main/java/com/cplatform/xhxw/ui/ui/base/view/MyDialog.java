package com.cplatform.xhxw.ui.ui.base.view;

import com.cplatform.xhxw.ui.R;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.TextView;

public class MyDialog extends Dialog implements OnClickListener{

	private Context context;
	private String title;
	private String message;
	private String leftButtonText;
	private String rightButtonText;
	private MyOnClickListener myOnClickListener;
	
	public MyDialog(Context context) {
		super(context);
		this.context = context; 
	}
	
	public MyDialog(Context context, int theme) {
			super(context, theme);
			this.context = context;
		}
	
	public MyDialog(Context context, String title, String message,
			        String leftButtonText, String rightButtonText,
			        MyOnClickListener myOnClickListener) {
		
		super(context, R.style.dialog);
		
		this.context = context;
		this.title = title;
		this.message = message;
		this.leftButtonText = leftButtonText;
		this.rightButtonText = rightButtonText;
		this.myOnClickListener = myOnClickListener;
		
		/**
		 * 单击dialog之外的地方，可以dismiss掉dialog
		 * setCanceledOnTouchOutside(true);
		 * 不写此句话,默认也可以dismiss掉dialog
		 * 
		 * 如果写了此句话，为true时可以dismiss掉dialog，为false时不可以dismiss掉dialog
		 */
		setCanceledOnTouchOutside(false);
		setCancelable(false);
		setContentView(R.layout.dialog);
		initView();
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		/**
		 * 实例化数据，也可以在构造方法中实现
		 */
		setContentView(R.layout.dialog);
		initView();
	}
	
	private void initView() {
		TextView titleText = (TextView) findViewById(R.id.dialog_title);
		TextView messageText = (TextView) findViewById(R.id.dialog_message);
		LinearLayout linear_ok= (LinearLayout) findViewById(R.id.dialog_ok);
		TextView text_ok= (TextView) findViewById(R.id.dialog_text_ok);
		
		linear_ok.setOnClickListener(this);
		
		titleText.setText(title);
		messageText.setText(message);
		text_ok.setText(leftButtonText);
	}
	
	@Override
	public void onClick(View v) {
		dismiss();
		if (v.getId() == R.id.dialog_ok) {
			if (myOnClickListener != null) {
				myOnClickListener.myOnClick(v);
			}
		}
	}

	public interface MyOnClickListener {
		public void myOnClick(View view);
	}
}
