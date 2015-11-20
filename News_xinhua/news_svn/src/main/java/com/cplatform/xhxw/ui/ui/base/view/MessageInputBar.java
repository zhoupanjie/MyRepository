package com.cplatform.xhxw.ui.ui.base.view;

import com.cplatform.xhxw.ui.R;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

/**
 * 消息输入栏
 * 
 * @author baiwenlong
 * 
 */
public class MessageInputBar extends LinearLayout implements
		View.OnClickListener {

	private Button btnFaceSwitch;
	private EditText etInput;
	private Button btnSend;

	private IListener listener;
	private boolean isSoftInputVisible;

	public MessageInputBar(Context context, AttributeSet attrs) {
		super(context, attrs);
		init(context);
	}

	public MessageInputBar(Context context) {
		super(context);
		init(context);
	}

	private void init(Context context) {
		View contentView = LayoutInflater.from(context).inflate(
				R.layout.message_input_bar, this, false);
		addView(contentView);

		etInput = (EditText) findViewById(R.id.et_input);
		btnSend = (Button) findViewById(R.id.btn_send);

		btnFaceSwitch.setOnClickListener(this);
		btnSend.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_send:
			if (listener != null) {
				listener.onSend(getMessage());
			}
			break;
		}
	}

	public String getMessage() {
		return etInput.getText().toString().trim();
	}

	private void hideKeyboard() {
		InputMethodManager im = (InputMethodManager) getContext()
				.getSystemService(Context.INPUT_METHOD_SERVICE);
		im.hideSoftInputFromWindow(getWindowToken(),
				InputMethodManager.HIDE_NOT_ALWAYS);
	}

	private void showKeyboard() {
		etInput.requestFocus();
		InputMethodManager imm = (InputMethodManager) getContext()
				.getSystemService(Context.INPUT_METHOD_SERVICE);
		imm.showSoftInput(etInput, InputMethodManager.RESULT_UNCHANGED_SHOWN);
	}

	public static interface IListener {
		public void onSend(String msg);
	}

	public void setListener(IListener listener) {
		this.listener = listener;
	}

	public void setMessage(String msg) {
		if (msg != null) {
			etInput.setSelection(msg.length());
		}
	}

	public void setHint(String hint) {
		etInput.setHint(hint);
	}

	public boolean isSoftInputVisible() {
		return isSoftInputVisible;
	}

	public void setSoftInputVisible(boolean isSoftInputVisible) {
		this.isSoftInputVisible = isSoftInputVisible;
	}
}
