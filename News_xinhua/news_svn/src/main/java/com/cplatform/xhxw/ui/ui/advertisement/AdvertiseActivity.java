package com.cplatform.xhxw.ui.ui.advertisement;

import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;

import com.cplatform.xhxw.ui.App;
import com.cplatform.xhxw.ui.R;
import com.cplatform.xhxw.ui.db.AdvertisementDB;
import com.cplatform.xhxw.ui.db.dao.AdvertisementDao;
import com.cplatform.xhxw.ui.ui.main.HomeActivity;
import com.cplatform.xhxw.ui.ui.web.WebViewActivity;

public class AdvertiseActivity extends Activity {

	private ImageView mAdverImgIV;
	private AdvertisementDao mAdverInfo;
	private boolean isEnterAdverDetailPage = false;
	private Handler mHandler = new Handler();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_advertisement);
		initViews();
		initData();
		
		mHandler.postDelayed(new Runnable() {
			
			@Override
			public void run() {
				if(!isEnterAdverDetailPage) {
					App app = App.getCurrentApp();
					app.startActivity(new Intent(app, HomeActivity.class)
					.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
				}
				finish();
			}
		}, 1000 * 3);
	}

	private void initViews() {
		mAdverImgIV = (ImageView) findViewById(R.id.adver_iv);
		mAdverImgIV.setOnClickListener(mOnClick);
	}

	private void initData() {
		List<AdvertisementDao> tmp = AdvertisementDB
				.getAdvertisementByShowPosition(this,
						AdvertisementDao.ADVER_SHOW_POSITION_LOADING);
		mAdverInfo = tmp.get(0);
		mAdverImgIV.setScaleType(ScaleType.FIT_XY);
		mAdverImgIV.setImageBitmap(AdvertiseUtil.scaleImgSize(
				AdvertiseUtil.generateAdverImgSaveFile(this, mAdverInfo)));
	}

	OnClickListener mOnClick = new OnClickListener() {

		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.adver_iv:
				isEnterAdverDetailPage = true;
				startActivity(WebViewActivity.getIntentByURL(AdvertiseActivity.this,
						mAdverInfo.getAdverUrl(), mAdverInfo.getAdverTitle()));
				break;
			}

		}
	};
}
