package com.hy.superemsg.activity;

import java.util.ArrayList;
import java.util.List;

import com.hy.superemsg.R;
import com.hy.superemsg.SuperEMsgApplication;
import com.hy.superemsg.components.UITitle;
import com.hy.superemsg.data.Account;
import com.hy.superemsg.data.Contact;
import com.hy.superemsg.req.ReqMobileGetSeg;
import com.hy.superemsg.req.ReqSmsPwdSend;
import com.hy.superemsg.rsp.BaseRspApi;
import com.hy.superemsg.rsp.RspMobileGetSeg;
import com.hy.superemsg.rsp.RspSmsPwdSend;
import com.hy.superemsg.rsp.SmsContentDetail;
import com.hy.superemsg.utils.CommonUtils;
import com.hy.superemsg.utils.HttpUtils;
import com.hy.superemsg.utils.MobileInfo;
import com.hy.superemsg.utils.HttpUtils.AsynHttpCallback;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.Toast;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.TextView;

public class SendSmsActivity extends Activity {
	private TextView names;
	private EditText contents;
	private CheckBox hasVoiceWish;
	private List<Contact> sendToContacts;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_send_sms);
		UITitle title = (UITitle) this.findViewById(R.id.ui_title);
		if (title != null) {
			title.setTitleText("发送短信");
			title.setLeftButton(R.drawable.back, new View.OnClickListener() {

				@Override
				public void onClick(View v) {
					finish();
				}
			});
		}
		sendToContacts = getIntent().getParcelableArrayListExtra(
				SuperEMsgApplication.EXTRA_SEND_NAMES);
		sms = getIntent().getParcelableExtra(
				SuperEMsgApplication.EXTRA_SEND_CONTENT);
		names = (TextView) this.findViewById(R.id.item_input);
		contents = (EditText) this.findViewById(R.id.item_input1);
		if (CommonUtils.isNotEmpty(sendToContacts)) {
			StringBuilder sb = new StringBuilder();
			String seperator = MobileInfo.getSeparateIcon();
			for (Contact c : sendToContacts) {
				sb.append(c.getContactVisibleContent()).append(seperator);
			}
			names.setText(sb.substring(0, sb.length() - 1));
		}
		String content = sms.smscontent.trim();
		contents.setText(content);
		contents.setSelection(content.length());
		this.findViewById(R.id.item_btn).setOnClickListener(
				new View.OnClickListener() {

					@Override
					public void onClick(View v) {
						if (MobileInfo.getSimState(SendSmsActivity.this) == TelephonyManager.SIM_STATE_READY) {

							if (CommonUtils.isNotEmpty(sendToContacts)) {
								SmsManager manager = SmsManager.getDefault();
								for (Contact c : sendToContacts) {
									String phone = c.phone;
									Intent sendOk = new Intent(
											"com.hy.superemsg.SmsSentSuccess");
									sendOk.putExtra(
											SuperEMsgApplication.EXTRA_SEND_NAMES,
											c);
									sendOk.putExtra(
											SuperEMsgApplication.EXTRA_SEND_SMSID,
											sms.smsid);
									boolean hasVoice = hasVoiceWish.isChecked();
									sendOk.putExtra(
											SuperEMsgApplication.EXTRA_SEND_VOICEWISH,
											hasVoice);
									PendingIntent sendReceiver = PendingIntent
											.getBroadcast(
													getApplicationContext(),
													0,
													sendOk,
													PendingIntent.FLAG_UPDATE_CURRENT);
									ArrayList<String> smslist = manager
											.divideMessage(contents.getText()
													.toString());
									if (CommonUtils.isNotEmpty(smslist)) {
										for (int i = 0; i < smslist.size(); i++) {
											String sms = smslist.get(i);
											if (i == smslist.size() - 1) {

												manager.sendTextMessage(phone,
														null, sms,
														sendReceiver, null);
											} else {

												manager.sendTextMessage(phone,
														null, sms, null, null);
											}
										}
									}
									Toast.makeText(SendSmsActivity.this,
											"短信发送成功!", Toast.LENGTH_SHORT)
											.show();
								}
							} else {
								Toast.makeText(SendSmsActivity.this,
										"请返回重新选择联系人", Toast.LENGTH_SHORT)
										.show();
							}
							finish();
						} else {
							Toast.makeText(SendSmsActivity.this, "您的SIM卡没设置好",
									Toast.LENGTH_SHORT).show();
						}
					}
				});
		hasVoiceWish = (CheckBox) this.findViewById(R.id.item_check);
		hasVoiceWish.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(CompoundButton buttonView,
					boolean isChecked) {
				if (isChecked) {

					if (SuperEMsgApplication.account != null
							&& SuperEMsgApplication.account.province != null) {

					} else {
						hasVoiceWish.setChecked(false);
						Intent i = new Intent(SendSmsActivity.this,
								GetPhoneNumberActivity.class);
						i.putExtra(
								SuperEMsgApplication.EXTRA_FROM_RING_TO_VALIDATE,
								false);
						startActivityForResult(i,
								SuperEMsgApplication.REQUEST_VALIDATE_FROM_SMS);
					}

				}
			}
		});
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		if (requestCode == SuperEMsgApplication.REQUEST_VALIDATE_FROM_SMS) {
			if (SuperEMsgApplication.account != null
					&& SuperEMsgApplication.account.province != null) {
				hasVoiceWish.setChecked(true);
			} else {
				hasVoiceWish.setChecked(false);
			}
		}
	}

	private SmsContentDetail sms;
}
