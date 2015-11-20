package com.hy.superemsg.activity;

import java.net.URI;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.database.ContentObserver;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;
import android.widget.Toast;

import com.baseproject.image.ImageCallback;
import com.hy.superemsg.R;
import com.hy.superemsg.SuperEMsgApplication;
import com.hy.superemsg.adapter.AbsPinedContactAdapter.pointerExpressListener;
import com.hy.superemsg.adapter.ContactsListAdapter1;
import com.hy.superemsg.adapter.ContactsListAdapter1.CheckListener;
import com.hy.superemsg.components.UITitle;
import com.hy.superemsg.data.Contact;
import com.hy.superemsg.db.DBUtils;
import com.hy.superemsg.req.ReqRingCallOut;
import com.hy.superemsg.rsp.AbsContentDetail;
import com.hy.superemsg.rsp.BaseRspApi;
import com.hy.superemsg.rsp.MmsContentDetail;
import com.hy.superemsg.rsp.RingContentDetail;
import com.hy.superemsg.rsp.RspRingCallOut;
import com.hy.superemsg.rsp.SmsContentDetail;
import com.hy.superemsg.utils.CommonUtils;
import com.hy.superemsg.utils.HttpUtils;
import com.hy.superemsg.utils.HttpUtils.AsynHttpCallback;
import com.hy.superemsg.utils.ImageUtils;
import com.hy.superemsg.utils.MobileInfo;
import android.view.inputmethod.*;

public class ContactsChooserActivity extends Activity {
	private ListView contactList;
	private ArrayList<Contact> sendToContacts;
	private Button selectAll;
	private Button unSelectAll;
	private CheckBox voiceWish;
	private Button send;
	private ContactsListAdapter1 adapter;
	private AbsContentDetail content;
	private LinearLayout pointersList;
	private ProgressDialog dialogForRing;
	private int sendCount;
	private EditText inputPhone;
	private ArrayList<Contact> tempContacts;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_contacts_chooser);
		UITitle title = (UITitle) this.findViewById(R.id.ui_title);
		if (title != null) {
			title.setTitleText("选择联系人");
			title.setLeftButton(R.drawable.back, new View.OnClickListener() {

				@Override
				public void onClick(View v) {
					finish();
				}
			});
		}
		tempContacts = new ArrayList<Contact>();
		content = this.getIntent().getParcelableExtra(
				SuperEMsgApplication.EXTRA_SEND_CONTENT);
		contactList = (ListView) this.findViewById(R.id.contact_list);
		contactList.setItemsCanFocus(false);
		contactList.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
		sendToContacts = new ArrayList<Contact>();
		selectAll = (Button) this.findViewById(R.id.item_btn);
		pointersList = (LinearLayout) this.findViewById(R.id.pointers_list);
		inputPhone = (EditText) this.findViewById(R.id.item_input);
		inputPhone.setOnEditorActionListener(new OnEditorActionListener() {

			@Override
			public boolean onEditorAction(TextView v, int actionId,
					KeyEvent event) {
				if (actionId == EditorInfo.IME_ACTION_DONE) {
					InputMethodManager imm = (InputMethodManager) ContactsChooserActivity.this
							.getSystemService(INPUT_METHOD_SERVICE);
					imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
					return true;
				}
				return false;
			}
		});
		selectAll.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				adapter.checkAll(true);
				int count = adapter.getCount();
				for (int i = 0; i < count; i++) {
					Contact contact = adapter.getItem(i);
					if (!contact.isPointer) {
						sendToContacts.add(contact);
					}
				}
			}
		});
		unSelectAll = (Button) this.findViewById(R.id.item_btn1);
		unSelectAll.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				adapter.checkAll(false);
				sendToContacts.clear();
			}
		});
		voiceWish = (CheckBox) this.findViewById(R.id.item_btn2);
		if (true) {
			voiceWish.setVisibility(View.GONE);
		} else {
			voiceWish.setOnCheckedChangeListener(new OnCheckedChangeListener() {

				@Override
				public void onCheckedChanged(CompoundButton buttonView,
						boolean isChecked) {
					if (isChecked) {
						if (SuperEMsgApplication.account == null) {
							startActivity(new Intent(
									ContactsChooserActivity.this,
									GetPhoneNumberActivity.class));
						} else {
							ContactsChooserActivity.this.getContentResolver()
									.registerContentObserver(
											Uri.parse("content://sms"), true,
											new SmsSentObserver());
						}
					}
				}
			});
		}
		send = (Button) this.findViewById(R.id.item_btn3);
		send.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				if (content != null) {

					if (CommonUtils.isNotEmpty(sendToContacts)
							|| !TextUtils.isEmpty(inputPhone.getText()
									.toString())) {
						tempContacts.clear();
						if (content instanceof SmsContentDetail) {
							// send sms
							if (!TextUtils.isEmpty(inputPhone.getText()
									.toString())) {
								String[] phones = inputPhone.getText()
										.toString().split("#");
								if (phones != null && phones.length > 0) {
									for (int i = 0; i < phones.length; i++) {
										Contact c = new Contact();
										c.phone = phones[i];
										c.name = phones[i];
										tempContacts.add(c);
									}
								}
							}
							if (CommonUtils.isNotEmpty(sendToContacts)) {
								tempContacts.addAll(sendToContacts);
							}

							Intent i = new Intent(ContactsChooserActivity.this,
									SendSmsActivity.class);
							i.putParcelableArrayListExtra(
									SuperEMsgApplication.EXTRA_SEND_NAMES,
									tempContacts);
							i.putExtra(SuperEMsgApplication.EXTRA_SEND_CONTENT,
									(SmsContentDetail) content);
							startActivityForResult(i,
									SuperEMsgApplication.REQUEST_SEND_SMS);
						} else if (content instanceof MmsContentDetail) {
							// send mms
							final MmsContentDetail mms = (MmsContentDetail) content;
							ImageUtils.Image
									.loadImage(mms.mmspicurl, new ImageView(
											ContactsChooserActivity.this),
											new ImageCallback() {

												@Override
												public void imageLoaded(
														Bitmap bitmap,
														String imageUrl) {
													StringBuilder sb = new StringBuilder();
													String separator = MobileInfo
															.getSeparateIcon();
													if (inputPhone.getText() != null) {
														String[] phones = inputPhone
																.getText()
																.toString()
																.split("#");
														if (phones != null
																&& phones.length > 0) {
															for (int i = 0; i < phones.length; i++) {
																sb.append(
																		phones[i])
																		.append(separator);
															}
														}
													}
													if (CommonUtils
															.isNotEmpty(sendToContacts)) {
														for (Contact c : sendToContacts) {
															sb.append(c.phone)
																	.append(separator);
														}
														sb.delete(
																sb.length() - 1,
																sb.length());
													}
													Intent intent = new Intent(
															Intent.ACTION_SEND);
													Uri uri = Uri
															.parse(MediaStore.Images.Media
																	.insertImage(
																			getContentResolver(),
																			bitmap,
																			null,
																			null));
													intent.putExtra(
															Intent.EXTRA_STREAM,
															uri);// uri为你的附件的uri

													intent.putExtra("subject",
															mms.mmsname); // 彩信的主题
													intent.putExtra("address",
															sb.toString()); // 彩信发送目的号码
													intent.putExtra("sms_body",
															mms.mmscontent); // 彩信中文字内容
													intent.putExtra(
															Intent.EXTRA_TEXT,
															mms.mmscontent);
													intent.setType("image/*");// 彩信附件类型
													startActivity(intent);
												}
											});
						} else if (content instanceof RingContentDetail) {
							final RingContentDetail ring = (RingContentDetail) content;
							if (CommonUtils.isNotEmpty(tempContacts)
									|| inputPhone.getText() != null) {

								if (inputPhone.getText() != null) {
									String[] phones = inputPhone.getText()
											.toString().split("#");
									if (phones != null && phones.length > 0) {
										for (int i = 0; i < phones.length; i++) {
											Contact c = new Contact();
											c.phone = phones[i];
											c.name = phones[i];
											tempContacts.add(c);
										}
									}
								}
								if (CommonUtils.isNotEmpty(sendToContacts)) {
									tempContacts.addAll(sendToContacts);
								}

								new AlertDialog.Builder(
										ContactsChooserActivity.this)
										.setMessage(
												"是否将" + ring.ringname
														+ "赠送给选中用户?")
										.setPositiveButton("赠送",
												new OnClickListener() {

													@Override
													public void onClick(
															DialogInterface dialog,
															int which) {
														sendCount = tempContacts
																.size();
														for (Contact contact : tempContacts) {
															if (contact.phone != null) {
																if (TextUtils
																		.isEmpty(SuperEMsgApplication.account.phonenum)
																		|| TextUtils
																				.isEmpty(contact.phone)) {
																	sendCount -= 1;
																	continue;
																}
																HttpUtils
																		.getInst()
																		.excuteTask(
																				new ReqRingCallOut(
																						SuperEMsgApplication.account.phonenum,
																						contact.phone,
																						ring.ringid,
																						ring.ringname),
																				new AsynHttpCallback() {

																					@Override
																					public void onSuccess(
																							BaseRspApi rsp) {
																						RspRingCallOut callout = (RspRingCallOut) rsp;
																						SuperEMsgApplication
																								.toast(callout.resultdesc);
																						sendCount -= 1;
																						if (sendCount == 0) {
																							dialogForRing
																									.dismiss();
																						}
																					}

																					@Override
																					public void onError(
																							String error) {
																						SuperEMsgApplication
																								.toast(error);
																						sendCount -= 1;
																						if (sendCount == 0) {
																							dialogForRing
																									.dismiss();
																						}
																					}
																				});
															}
														}
														dialogForRing = new ProgressDialog(
																ContactsChooserActivity.this);
														dialogForRing
																.setMessage("赠送彩铃中...");
														dialogForRing.show();

													}
												})
										.setNegativeButton("取消", null).create()
										.show();
							}
						}
					} else {
						Toast.makeText(ContactsChooserActivity.this, "收件人不能为空",
								Toast.LENGTH_SHORT).show();
					}
				}
			}
		});
		List<Contact> contacts = DBUtils.getInst().getContacts(this);
		Collections.sort(contacts, Contact.pinyinASCComparator);
		adapter = new ContactsListAdapter1(this);
		adapter.setListener(new CheckListener() {

			@Override
			public void onItemChecked(Contact contact) {
				configCheckState(contact);
			}
		});
		adapter.setDatas(contacts, new pointerExpressListener() {

			@Override
			public void onPointerChanged(List<Contact> datas) {

				if (CommonUtils.isNotEmpty(datas)) {
					LayoutParams lp = new LayoutParams(
							LayoutParams.WRAP_CONTENT,
							LayoutParams.WRAP_CONTENT);
					lp.weight = 1;
					for (Contact contact : datas) {
						TextView tv = new TextView(ContactsChooserActivity.this);
						tv.setText(contact.name);
						tv.setTextSize(getResources()
								.getDimension(R.dimen.px14));
						tv.setTag(contact);
						tv.setOnClickListener(new View.OnClickListener() {

							@Override
							public void onClick(View v) {
								Contact contact = (Contact) v.getTag();
								int pos = adapter.getPositionByContact(contact);
								contactList.setSelection(pos);
							}
						});
						pointersList.addView(tv, lp);
					}
				}
			}
		});
		contactList.setAdapter(adapter);
	}

	private void configCheckState(Contact contact) {
		if (contact.isSelected) {
			sendToContacts.add(contact);
		} else {
			sendToContacts.remove(contact);
		}
	}

	@Override
	protected void onResume() {
		super.onResume();
		if (voiceWish.getVisibility() == View.VISIBLE) {
			if (SuperEMsgApplication.account == null) {
				voiceWish.setChecked(false);
			}
		}
	}

	private class SmsSentObserver extends ContentObserver {

		public SmsSentObserver() {
			super(new Handler());
		}

		@Override
		public void onChange(boolean selfChange) {
			super.onChange(selfChange);
		}
	}
}
