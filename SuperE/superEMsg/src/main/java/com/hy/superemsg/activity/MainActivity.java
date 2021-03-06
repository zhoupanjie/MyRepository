package com.hy.superemsg.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;

import com.hy.superemsg.R;
import com.hy.superemsg.SuperEMsgApplication;
import com.hy.superemsg.components.UITitle;
import com.hy.superemsg.utils.ImageUtils;

public class MainActivity extends FragmentActivity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		ImageUtils.initImage(this);
		UITitle title = (UITitle) this.findViewById(R.id.ui_title);
		if (title != null) {
			title.setTitleText(this.getString(R.string.app_name));
		}
		findViewById(R.id.item_sms).setOnClickListener(
				new View.OnClickListener() {

					@Override
					public void onClick(View v) {
						startActivity(new Intent(MainActivity.this,
								SmsDepotActivity.class));
					}
				});
		findViewById(R.id.item_mms).setOnClickListener(
				new View.OnClickListener() {

					@Override
					public void onClick(View v) {
						startActivity(new Intent(MainActivity.this,
								MmsDepotActivity.class));
					}
				});
		findViewById(R.id.item_holiday).setOnClickListener(
				new View.OnClickListener() {

					@Override
					public void onClick(View v) {
						// if (SuperEMsgApplication.account != null
						// && SuperEMsgApplication.account.province != null) {
						// startActivity(new Intent(MainActivity.this,
						// RingZoneActivity.class));
						// } else {
						// Intent i = new Intent(MainActivity.this,
						// GetPhoneNumberActivity.class);
						// i.putExtra(
						// SuperEMsgApplication.EXTRA_FROM_RING_TO_VALIDATE,
						// true);
						// startActivity(i);
						// }
						startActivity(new Intent(MainActivity.this,
								HolidayDepotActiviy.class));
					}
				});
		findViewById(R.id.item_game).setOnClickListener(
				new View.OnClickListener() {

					@Override
					public void onClick(View v) {
						startActivity(new Intent(MainActivity.this,
								GamesActivity.class));
					}
				});
		findViewById(R.id.item_cartoon).setOnClickListener(
				new View.OnClickListener() {

					@Override
					public void onClick(View v) {
						startActivity(new Intent(MainActivity.this,
								CartoonActivity.class));
					}
				});
		findViewById(R.id.item_news).setOnClickListener(
				new View.OnClickListener() {

					@Override
					public void onClick(View v) {
						startActivity(new Intent(MainActivity.this,
								NewsActivity.class));
					}
				});
	}

}
