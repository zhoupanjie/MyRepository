package com.cplatform.xhxw.ui.ui.apptips;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.ImageView;

import com.cplatform.xhxw.ui.R;
import com.cplatform.xhxw.ui.ui.base.BaseNormalActivity;

public class TipsActivity extends BaseNormalActivity {
	
	private static final String PARAM_TIPS_TYPE = "tips_type";
	
	private ImageView mIv;

	public static Intent getTipIntent(Context con, String tipsType) {
		Intent it = new Intent();
		it.setClass(con, TipsActivity.class);
		it.putExtra(PARAM_TIPS_TYPE, tipsType);
		return it;
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_tips);
		
		init();
	}

	private void init() {
		mIv = (ImageView) findViewById(R.id.tips_iv);
		mIv.setBackgroundResource(getTipImageResourceId());
		mIv.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();
			}
		});
	}

	@Override
	protected void onResume() {
		super.onResume();
	}
	
	private int getTipImageResourceId() {
		int resId = R.drawable.tip_detail_more;
		String tipsType = getIntent().getStringExtra(PARAM_TIPS_TYPE);
		if(tipsType.equals(TipsUtil.TIP_LANGUAGE_CHANGE)) {
			resId = R.drawable.tip_change_language;
			TipsUtil.saveTipStatus(TipsUtil.TIP_LANGUAGE_CHANGE);
		} else if(tipsType.equals(TipsUtil.TIP_CHANNEL_ADD)) {
			resId = R.drawable.tip_add_channel;
			TipsUtil.saveTipStatus(TipsUtil.TIP_CHANNEL_ADD);
		} else if(tipsType.equals(TipsUtil.TIP_CHANNEL_SEARCH)) {
			resId = R.drawable.tip_channel_search;
			TipsUtil.saveTipStatus(TipsUtil.TIP_CHANNEL_SEARCH);
		} else if(tipsType.equals(TipsUtil.TIP_DETAIL_MORE)) {
			resId = R.drawable.tip_detail_more;
			TipsUtil.saveTipStatus(TipsUtil.TIP_DETAIL_MORE);
		}
		return resId;
	}
}
