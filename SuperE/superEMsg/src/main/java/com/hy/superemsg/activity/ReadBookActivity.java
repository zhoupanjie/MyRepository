package com.hy.superemsg.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.hy.superemsg.R;
import com.hy.superemsg.SuperEMsgApplication;
import com.hy.superemsg.components.BookBanner;
import com.hy.superemsg.components.UITitle;
import com.hy.superemsg.rsp.AnimationDramaDetail;
import com.hy.superemsg.utils.AndroidUtil;

public class ReadBookActivity extends FragmentActivity implements
		OnClickListener {
	private BookBanner banner;
	private AnimationDramaDetail drama;
	private ImageView readbook_advert;
	private ImageView readbook_advert_cancel;
	private RelativeLayout readbook_advert_layout;
	private RelativeLayout readbook_advert_dialog_layout;
	private Button readbook_advert_dialog_send_btn;
	private Button readbook_advert_dialog_cancel_btn;

	@Override
	protected void onCreate(Bundle b) {
		super.onCreate(b);
		setContentView(R.layout.activity_readbook);
		banner = (BookBanner) this.findViewById(R.id.banner);
		drama = getIntent().getParcelableExtra(
				SuperEMsgApplication.EXTRA_DRAMA_DETAIL);
		UITitle title = (UITitle) this.findViewById(R.id.ui_title);
		if (title != null) {
			title.setTitleText(drama.dramaname);
			title.setLeftButton(R.drawable.back, new View.OnClickListener() {

				@Override
				public void onClick(View v) {
					finish();
				}
			});
		}
		banner.setDrama(drama);

		WindowManager wm = this.getWindowManager();

		int width = wm.getDefaultDisplay().getWidth();
		int height = wm.getDefaultDisplay().getHeight();

		int advert_bg_width = (int) (width * 0.8);
		int advert_bg_height = (int) (advert_bg_width / 1.36);

		readbook_advert_layout = (RelativeLayout) findViewById(R.id.readbook_advert_layout);
		readbook_advert_dialog_layout = (RelativeLayout) findViewById(R.id.readbook_advert_dialog_layout);
		readbook_advert_dialog_layout.setVisibility(View.GONE);
		readbook_advert = (ImageView) findViewById(R.id.readbook_advert);
		readbook_advert.setOnClickListener(this);
		readbook_advert_cancel = (ImageView) findViewById(R.id.readbook_advert_cancel);
		readbook_advert_cancel.setOnClickListener(this);
		readbook_advert_dialog_send_btn = (Button) findViewById(R.id.readbook_advert_dialog_send_btn);
		readbook_advert_dialog_send_btn.setOnClickListener(this);
		readbook_advert_dialog_cancel_btn = (Button) findViewById(R.id.readbook_advert_dialog_cancel_btn);
		readbook_advert_dialog_cancel_btn.setOnClickListener(this);
		readbook_advert_dialog_layout.getLayoutParams().height = advert_bg_height;
		readbook_advert_dialog_layout.getLayoutParams().width = advert_bg_width;
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.readbook_advert:// 开启对话框
			readbook_advert_layout.setVisibility(View.GONE);
			readbook_advert_dialog_layout.setVisibility(View.VISIBLE);
			break;
		case R.id.readbook_advert_cancel:// 关闭广告条
			readbook_advert_layout.setVisibility(View.GONE);
			break;
		case R.id.readbook_advert_dialog_send_btn:// 发送短信
			send("1065556554501", "DM");
			readbook_advert_dialog_layout.setVisibility(View.GONE);
			break;
		case R.id.readbook_advert_dialog_cancel_btn:// 关闭广告条
			readbook_advert_dialog_layout.setVisibility(View.GONE);
			break;

		}
	}

	private void send(String number, String message) {
		Uri uri = Uri.parse("smsto:" + number);
		Intent sendIntent = new Intent(Intent.ACTION_VIEW, uri);
		sendIntent.putExtra("sms_body", message);
		startActivity(sendIntent);
	}
}
