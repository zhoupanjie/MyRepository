package com.cplatform.xhxw.ui.ui.main.saas.addressBook;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

import com.cplatform.xhxw.ui.R;
import com.cplatform.xhxw.ui.ui.main.saas.addressBook.VerifyDialog.OnVerifyDialogListener;

public class DeleteFriendDialog extends Dialog implements OnClickListener {

	private Context context;
	private TextView messageText;
	private Button sureBtn;
	private Button cancleBtn;
	
	private String message;

	private OnDeleteFriendDialogListener listener;

	public DeleteFriendDialog(Context context) {
		super(context);
		this.context = context;
	}

	public DeleteFriendDialog(Context context, int theme) {
		super(context, theme);
		this.context = context;
	}

	public DeleteFriendDialog(Context context, String message,
			OnDeleteFriendDialogListener listener) {

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
		setContentView(R.layout.delete_friend_dialog);
		init();
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.delete_friend_dialog);

		init();
	}

	private void init() {
		messageText = (TextView) findViewById(R.id.delete_dialog_message);
		sureBtn = (Button) findViewById(R.id.delete_sure_btn);
		cancleBtn = (Button) findViewById(R.id.delete_cancle_btn);
		
		sureBtn.setOnClickListener(this);
		cancleBtn.setOnClickListener(this);

		messageText.setText("将联系人" + message + "删除，同时删除与该联系人的聊天记录");
	}

	@Override
	public void onClick(View view) {

		dismiss();

		switch (view.getId()) {
		case R.id.delete_sure_btn:
			if (listener != null) {
				listener.deleteFriendDialog();
			}
			break;
		case R.id.delete_cancle_btn:
			
			break;
		default:
			break;
		}
	}

	public interface OnDeleteFriendDialogListener {
		public void deleteFriendDialog();
	}
}

