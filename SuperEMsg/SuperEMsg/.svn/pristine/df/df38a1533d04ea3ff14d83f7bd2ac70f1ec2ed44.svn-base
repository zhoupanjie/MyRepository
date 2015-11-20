package com.hy.superemsg.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;

import com.hy.superemsg.R;

public class WelcomeActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		View contentView = LayoutInflater.from(this).inflate(
				R.layout.activity_welcome, null);
		setContentView(contentView);
		contentView.postDelayed(new Runnable() {

			@Override
			public void run() {
				startActivity(new Intent(WelcomeActivity.this,
						MainActivity.class));
				WelcomeActivity.this.finish();
			}
		}, 3000);
	}
}
