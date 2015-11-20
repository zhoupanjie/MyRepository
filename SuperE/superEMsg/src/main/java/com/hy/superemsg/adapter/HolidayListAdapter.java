package com.hy.superemsg.adapter;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.View;
import android.widget.TextView;

import com.hy.superemsg.R;
import com.hy.superemsg.SuperEMsgApplication;
import com.hy.superemsg.activity.ContactsChooserActivity;
import com.hy.superemsg.db.DBUtils;
import com.hy.superemsg.db.DBUtils.DBHelper;
import com.hy.superemsg.rsp.HolidayContentDetail;
import com.hy.superemsg.rsp.SmsContentDetail;
import com.hy.superemsg.utils.ShareUtils;

public class HolidayListAdapter extends AbsCommonAdapter<HolidayContentDetail>{

		public HolidayListAdapter(Context ctx) {
			super(ctx, R.layout.item_sms_list);
		}

		public HolidayListAdapter(Context ctx, int layout) {
			super(ctx, layout);
		}

		@Override
		public void getView(int position, View convertView,
				final HolidayContentDetail data) {
			TextView msg = (TextView) convertView.findViewById(R.id.item_text);
			msg.setText(data.smscontent);
			convertView.setTag(data.smsid);
			convertView.findViewById(R.id.item_btn).setSelected(data.isCollected());
			convertView.findViewById(R.id.item_btn).setOnClickListener(
					new View.OnClickListener() {

						@Override
						public void onClick(View v) {
							data.setCollected(!data.isCollected());
							if (data.isCollected()) {
								DBUtils.getInst().insert(
										DBHelper.TABLE_HOLIDAY_COLLECTION, null, data);
							} else {
								DBUtils.getInst().remove(
										DBHelper.TABLE_HOLIDAY_COLLECTION, data);
							}
							notifyDataSetChanged();
						}
					});
			convertView.findViewById(R.id.item_btn1).setOnClickListener(
					new View.OnClickListener() {

						@Override
						public void onClick(View v) {
							new AlertDialog.Builder(getContext())
									.setItems(R.array.menu_sms_share,
											new DialogInterface.OnClickListener() {

												@Override
												public void onClick(
														DialogInterface dialog,
														int which) {
													if (which == 0) {
														// weixin
														ShareUtils.shareByWeixin(
																getContext(),
																data.smscontent);
													} else if (which == 1) {
														// weibo
														ShareUtils.shareByWeibo(
																getContext(),
																data.smscontent);
													}

												}
											}).create().show();
						}
					});
			convertView.findViewById(R.id.item_btn2).setOnClickListener(
					new View.OnClickListener() {

						@Override
						public void onClick(View v) {
							Activity act = (Activity) getContext();
							Intent i = new Intent(act,
									ContactsChooserActivity.class);
							i.putExtra(SuperEMsgApplication.EXTRA_SEND_CONTENT,
									data);
							act.startActivity(i);
						}
					});
		}
}
