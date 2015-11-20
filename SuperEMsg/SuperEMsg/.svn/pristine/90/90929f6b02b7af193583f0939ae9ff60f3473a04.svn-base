package com.hy.superemsg.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.hy.superemsg.R;
import com.hy.superemsg.SuperEMsgApplication;
import com.hy.superemsg.components.UITitle;
import com.hy.superemsg.db.DBUtils;
import com.hy.superemsg.db.DBUtils.DBHelper;
import com.hy.superemsg.rsp.MmsContentDetail;
import com.hy.superemsg.utils.ImageUtils;
import com.hy.superemsg.utils.ShareUtils;

public class MmsDetailActivity extends Activity {
	private MmsContentDetail mms;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_mms_detail);
		UITitle title = (UITitle) this.findViewById(R.id.ui_title);
		if (title != null) {
			title.setTitleText(R.string.mms_depot);
			title.setLeftButton(R.drawable.back, new View.OnClickListener() {

				@Override
				public void onClick(View v) {
					finish();
				}
			});
		}
		mms = getIntent().getParcelableExtra(
				SuperEMsgApplication.EXTRA_MMS_DETAIL);
		boolean collected = DBUtils.getInst().checkIfExist(
				DBHelper.TABLE_MMS_COLLECTION, mms.mmsid);
		mms.setCollected(collected);
		findViewById(R.id.item_btn).setSelected(mms.isCollected());
		TextView tvTitle = (TextView) this.findViewById(R.id.item_text);
		tvTitle.setText(mms.mmsname);
		TextView tvMsg = (TextView) this.findViewById(R.id.item_text1);
		tvMsg.setText(mms.mmscontent);
		final ImageView iv = (ImageView) this.findViewById(R.id.item_image);
		ImageUtils.Image.loadImage(mms.mmspicurl, iv);
		this.findViewById(R.id.item_btn).setOnClickListener(
				new View.OnClickListener() {

					@Override
					public void onClick(View v) {

						mms.setCollected(!mms.isCollected());
						if (mms.isCollected()) {
							DBUtils.getInst().insert(
									DBHelper.TABLE_MMS_COLLECTION, null, mms);
						} else {
							DBUtils.getInst().remove(
									DBHelper.TABLE_MMS_COLLECTION, mms);
						}
						findViewById(R.id.item_btn).setSelected(
								mms.isCollected());
						MmsDetailActivity.this.setResult(Activity.RESULT_OK);
					}
				});
		this.findViewById(R.id.item_btn1).setOnClickListener(
				new View.OnClickListener() {

					@Override
					public void onClick(View v) {
						String content = mms.mmscontent;
						if (TextUtils.isEmpty(content)) {
							content = mms.mmsname;
						}
						iv.setDrawingCacheEnabled(true);
						ShareUtils.shareByWeibo(MmsDetailActivity.this,
								content, iv.getDrawingCache());
					}
				});
		this.findViewById(R.id.item_btn2).setOnClickListener(
				new View.OnClickListener() {

					@Override
					public void onClick(View v) {
						Intent i = new Intent(MmsDetailActivity.this,
								ContactsChooserActivity.class);
						i.putExtra(SuperEMsgApplication.EXTRA_SEND_CONTENT, mms);
						startActivity(i);
					}
				});
	}
}
