package com.hy.superemsg.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.hy.superemsg.R;
import com.hy.superemsg.SuperEMsgApplication;
import com.hy.superemsg.components.UITitle;
import com.hy.superemsg.data.Account;
import com.hy.superemsg.req.ReqMobileGetSeg;
import com.hy.superemsg.req.ReqSmsPwdSend;
import com.hy.superemsg.rsp.BaseRspApi;
import com.hy.superemsg.rsp.RspMobileGetSeg;
import com.hy.superemsg.rsp.RspSmsPwdSend;
import com.hy.superemsg.utils.HttpUtils;
import com.hy.superemsg.utils.HttpUtils.AsynHttpCallback;
import com.hy.superemsg.utils.MobileInfo;

public class GetPhoneNumberActivity extends Activity {
	private ProgressDialog progress;
	private EditText editPwd;
	private EditText editPhone;
	private Button btnEnter;
	private boolean isValidOk;
	private boolean isFromMain;
	private ImageView imgValidateFlag;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_getphone);
		isFromMain=getIntent().getExtras().getBoolean(SuperEMsgApplication.EXTRA_FROM_RING_TO_VALIDATE, false);
		UITitle title = (UITitle) this.findViewById(R.id.ui_title);
		if (title != null) {
			title.setTitleText("短信验证");
			title.setLeftButton(R.drawable.back, new View.OnClickListener() {

				@Override
				public void onClick(View v) {
					finish();
				}
			});
		}
		isValidOk = false;
		editPhone = (EditText) this.findViewById(R.id.item_input);
		imgValidateFlag = (ImageView) findViewById(R.id.item_image);
		imgValidateFlag.setVisibility(View.INVISIBLE);
		this.findViewById(R.id.item_btn).setOnClickListener(
				new View.OnClickListener() {

					@Override
					public void onClick(View v) {
						EditText et = (EditText) findViewById(R.id.item_input);
						final String phone = et.getText().toString();
						if (TextUtils.isDigitsOnly(phone)) {
							doGetPhonePwd(phone);
						}
					}
				});
		editPwd = (EditText) this.findViewById(R.id.item_input1);
		editPwd.addTextChangedListener(new TextWatcher() {
			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
			}

			@Override
			public void afterTextChanged(Editable text) {
				String word = text.toString();
				btnEnter.setEnabled(false);
				if (editPwd.getTag() != null) {
					if (!TextUtils.isEmpty(word)) {
						imgValidateFlag.setVisibility(View.VISIBLE);
						if (!TextUtils.isEmpty(editPwd.getTag().toString())) {
							if (word.equals(editPwd.getTag().toString())) {
								imgValidateFlag
										.setImageResource(R.drawable.checkmark);
								btnEnter.setEnabled(true);
								if (!isValidOk) {
									isValidOk = true;
									SuperEMsgApplication.account = new Account();
									SuperEMsgApplication.account.operator = MobileInfo
											.getOperator(GetPhoneNumberActivity.this);
									SuperEMsgApplication.account.phonenum = editPhone
											.getText().toString();
									SuperEMsgApplication.account.sim = MobileInfo
											.getSIMSN(GetPhoneNumberActivity.this);
									doGetSimLocation(editPhone.getText()
											.toString());
								}
							} else {
								imgValidateFlag
										.setImageResource(R.drawable.cross);
							}
						}
					} else {
						imgValidateFlag.setVisibility(View.INVISIBLE);
					}
				}
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
			}
		});

		btnEnter = (Button) this.findViewById(R.id.item_btn1);
		btnEnter.setEnabled(false);
		this.findViewById(R.id.item_btn1).setOnClickListener(
				new View.OnClickListener() {

					@Override
					public void onClick(View v) {
						if (SuperEMsgApplication.account != null
								&& SuperEMsgApplication.account.province != null) {
							if (isFromMain) {
								startActivity(new Intent(
										GetPhoneNumberActivity.this,
										RingZoneActivity.class));
							}
							finish();
						}
					}
				});

	}

	private void doGetPhonePwd(final String phoneNum) {
		if (TextUtils.isEmpty(phoneNum)) {
			Toast.makeText(this, "请先输入手机号码", Toast.LENGTH_SHORT).show();
			return;
		}
		progress = new ProgressDialog(GetPhoneNumberActivity.this);
		progress.setMessage("正在获取验证码，请稍后。。。");
		progress.show();
		HttpUtils.getInst().excuteTask(new ReqSmsPwdSend(phoneNum),
				new AsynHttpCallback() {

					@Override
					public void onSuccess(BaseRspApi rsp) {
						progress.dismiss();
						RspSmsPwdSend pwd = (RspSmsPwdSend) rsp;
						if (pwd.resultcode == RspSmsPwdSend.codes[0]) {

							RspSmsPwdSend send = (RspSmsPwdSend) rsp;
							editPwd.setTag(send.smspwd);
						}
					}

					@Override
					public void onError(String error) {
						progress.dismiss();
						SuperEMsgApplication.toast(error);
					}
				});
	}

	public void doGetSimLocation(String phonenum) {
		// progress = new ProgressDialog(GetPhoneNumberActivity.this);
		// progress.setMessage("正在查询手机归属地，请稍后。。。");
		// progress.show();
		if (TextUtils.isEmpty(phonenum)) {
			Toast.makeText(this, "请先输入手机号码", Toast.LENGTH_SHORT).show();
			return;
		}
		HttpUtils.getInst().excuteTask(new ReqMobileGetSeg(phonenum),
				new AsynHttpCallback() {

					@Override
					public void onSuccess(BaseRspApi rsp) {
						RspMobileGetSeg get = (RspMobileGetSeg) rsp;
						if (get.resultcode == 0) {
							SuperEMsgApplication.account.province = get.provinceid;
							SuperEMsgApplication.account.operator = get.corpid;
							((SuperEMsgApplication) getApplicationContext())
									.saveAccount();
						}
						// progress.dismiss();
					}

					@Override
					public void onError(String error) {
						// progress.dismiss();
						SuperEMsgApplication.toast(error);
					}
				});
	}
}
