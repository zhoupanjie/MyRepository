package com.cplatform.xhxw.ui.ui.main.saas.addressBook;

import com.cplatform.xhxw.ui.R;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

public class VerifyDialog extends Dialog implements OnClickListener {

	private Context context;
	private TextView messageText;
	private Button btn;

	private String message;

	private OnVerifyDialogListener listener;

	public VerifyDialog(Context context) {
		super(context);
		this.context = context;
	}

	public VerifyDialog(Context context, int theme) {
		super(context, theme);
		this.context = context;
	}

	public VerifyDialog(Context context, String message,
			OnVerifyDialogListener listener) {

		super(context, R.style.verify_dialog);

		this.context = context;
		this.message = message;
		this.listener = listener;

		/**
		 * 单击dialog之外的地方，可以dismiss掉dialog setCanceledOnTouchOutside(true);
		 * 不写此句话,默认也可以dismiss掉dialog
		 * 
		 * 如果写了此句话，为true时可以dismiss掉dialog，为false时不可以dismiss掉dialog
		 */
		setCanceledOnTouchOutside(false);
		setCancelable(false);
		setContentView(R.layout.verify_dialog);
		init();
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.verify_dialog);

		init();
	}

	private void init() {
		messageText = (TextView) findViewById(R.id.verify_dialog_message);
		btn = (Button) findViewById(R.id.verify_dialog_btn);

		btn.setOnClickListener(this);

		messageText.setText(message);
	}

	@Override
	public void onClick(View view) {

		dismiss();

		switch (view.getId()) {
		case R.id.verify_dialog_btn:
			if (listener != null) {
				listener.verifyDialog();
			}
			break;

		default:
			break;
		}
	}

	public interface OnVerifyDialogListener {
		public void verifyDialog();
	}
}
