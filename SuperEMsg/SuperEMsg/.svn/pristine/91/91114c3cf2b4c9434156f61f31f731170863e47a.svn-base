package com.hy.superemsg.adapter;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.hy.superemsg.R;
import com.hy.superemsg.SuperEMsgApplication;
import com.hy.superemsg.activity.ContactsChooserActivity;
import com.hy.superemsg.activity.MmsCollectionsActivity;
import com.hy.superemsg.activity.MmsDetailActivity;
import com.hy.superemsg.db.DBUtils;
import com.hy.superemsg.db.DBUtils.DBHelper;
import com.hy.superemsg.rsp.MmsContentDetail;
import com.hy.superemsg.utils.ImageUtils;
import com.hy.superemsg.utils.ShareUtils;

public class MmsListAdapter extends AbsCommonAdapter<MmsContentDetail> {

	public MmsListAdapter(Context ctx) {
		super(ctx, R.layout.item_mms_list);
	}

	@Override
	public void getView(int position, final View convertView,
			final MmsContentDetail data) {

		TextView msg = (TextView) convertView.findViewById(R.id.item_text);
		String content = data.mmscontent;
		if (TextUtils.isEmpty(content)) {
			content = data.mmsname;
		}
		msg.setText(content);
		convertView.findViewById(R.id.item_btn).setSelected(data.isCollected());
		convertView.findViewById(R.id.item_btn).setOnClickListener(
				new View.OnClickListener() {

					@Override
					public void onClick(View v) {
						data.setCollected(!data.isCollected());
						if (data.isCollected()) {
							DBUtils.getInst().insert(
									DBHelper.TABLE_MMS_COLLECTION, null, data);
						} else {
							DBUtils.getInst().remove(
									DBHelper.TABLE_MMS_COLLECTION, data);
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
															data.mmscontent);
												} else if (which == 1) {
													// weibo
													ImageView iv = (ImageView) convertView
															.findViewById(R.id.item_image);
													iv.setDrawingCacheEnabled(true);
													String content = data.mmscontent;
													if (TextUtils
															.isEmpty(content)) {
														content = data.mmsname;
													}
													ShareUtils.shareByWeibo(
															getContext(),
															content,
															iv.getDrawingCache());
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
		convertView.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				Activity act = (Activity) getContext();
				Intent i = new Intent(act, MmsDetailActivity.class);
				i.putExtra(SuperEMsgApplication.EXTRA_MMS_DETAIL,
						(MmsContentDetail) v.getTag());
				act.startActivityForResult(i,
						SuperEMsgApplication.REQUEST_LIST_TO_MMS_DETAIL);
			}
		});
		ImageView iv = (ImageView) convertView.findViewById(R.id.item_image);
		if (!TextUtils.isEmpty(data.mmspicurl)) {
			ImageUtils.Image.loadImage(data.mmspicurl, iv);
		}
		convertView.setTag(data);
	}

}
