package com.cplatform.xhxw.ui.ui.comment;

import com.cplatform.xhxw.ui.R;
import com.cplatform.xhxw.ui.ui.base.BaseActivity;
import com.cplatform.xhxw.ui.ui.base.view.MessageInputBar;
import com.cplatform.xhxw.ui.util.InputViewSensitiveLinearLayout;
import com.cplatform.xhxw.ui.util.InputViewSensitiveLinearLayout.OnInputViewListener;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;

/**
 * 消息输入栏
 * 
 * @author sunxiaoguang
 * 
 */
public class MessageInputBarActivity extends BaseActivity implements
		OnInputViewListener {

	private MessageInputBar inputBar;
	private Bundle bundleExtra;

	public static void show(Activity activity, String msg, String hint,
			Bundle bundleExtra, Integer requestCode) {
		Intent intent = new Intent(activity, MessageInputBarActivity.class);
		intent.putExtra("msg", msg);
		intent.putExtra("hint", hint);
		intent.putExtra("bundleExtra", bundleExtra);

		if (requestCode == null) {
			activity.startActivity(intent);
		} else {
			activity.startActivityForResult(intent, requestCode);
		}
	}

    @Override
    protected String getScreenName() {
        return "MessageInputBarActivity";
    }

    @Override
	protected void onCreate(Bundle savedInstanceState) {
		setContentView(R.layout.activity_message_input_bar);
		super.onCreate(savedInstanceState);

		InputViewSensitiveLinearLayout layoutContent = ((InputViewSensitiveLinearLayout) findViewById(R.id.layout_content));
		layoutContent.setOnInputViewListener(this);

		layoutContent.setOnTouchListener(new View.OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				if (MotionEvent.ACTION_DOWN == event.getAction()) {
					onBackPressed();
				}
				return true;
			}
		});

		inputBar = (MessageInputBar) findViewById(R.id.message_input_bar);
		inputBar.setListener(new MessageInputBar.IListener() {

			@Override
			public void onSend(String msg) {
				Intent data = new Intent();
				data.putExtra("msg", msg);
				data.putExtra("bundleExtra", bundleExtra);
				setResult(Activity.RESULT_OK, data);
//				hideKeyboard();........................................................
				finish();
			}
		});
		inputBar.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
			}
		});

		Intent data = getIntent();
		inputBar.setMessage(data.getStringExtra("msg"));
		inputBar.setHint(data.getStringExtra("hint"));
		bundleExtra = data.getBundleExtra("bundleExtra");

	}

	@Override
	public void onShowInputView() {
		inputBar.setSoftInputVisible(true);
	}

	@Override
	public void onHideInputView() {
		inputBar.setSoftInputVisible(false);
	}

	@Override
	public void onBackPressed() {
		Intent data = new Intent();
		data.putExtra("msg", inputBar.getMessage());
		data.putExtra("bundleExtra", bundleExtra);
		setResult(Activity.RESULT_CANCELED, data);
		super.onBackPressed();
	}
}
