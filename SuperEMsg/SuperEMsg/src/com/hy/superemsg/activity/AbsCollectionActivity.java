package com.hy.superemsg.activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;

import com.hy.superemsg.R;
import com.hy.superemsg.components.UITitle;

public abstract class AbsCollectionActivity extends Activity {
	protected abstract String getDBTable();

	protected abstract String getTitleName();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

	}

	@Override
	public void onAttachedToWindow() {
		super.onAttachedToWindow();
		UITitle title = (UITitle) this.findViewById(R.id.ui_title);
		if (title != null) {
			title.setTitleText(getTitleName());
			title.setLeftButton(R.drawable.back, new View.OnClickListener() {

				@Override
				public void onClick(View v) {
					finish();
				}
			});
		}
	}
}
