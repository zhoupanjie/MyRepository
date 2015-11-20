package com.cplatform.xhxw.ui.ui.main.cms.radiobroadcast;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;

import com.cplatform.xhxw.ui.R;

public class RadioBroadcastSettingActivity extends Activity {

	public static Intent getIntent(Context context) {
		Intent intent = new Intent(context, RadioBroadcastSettingActivity.class);
		Bundle bundle = new Bundle();
		// bundle.putString("newsid", newsid);
		// bundle.putBoolean("isEnterprise", isEnterprise);
		intent.putExtras(bundle);
		return intent;
	}

	// @Override
	// protected String getScreenName() {
	// // TODO Auto-generated method stub
	// return null;
	// }
	private ImageView ivGoBack;
	private RadioGroup rbMode;
	private RadioGroup rbTimer;
	private Context con;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_radiobroadcast_setting);
		con = this;
		ivGoBack = (ImageView) this.findViewById(R.id.iv_goback);
		rbMode = (RadioGroup) this.findViewById(R.id.rg_playmode);
		rbTimer = (RadioGroup) this.findViewById(R.id.rg_timer);
		ivGoBack.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				RadioBroadcastSettingActivity.this.finish();
			}
		});
		RadioBroadcastUtil.getRadioSettingInfo(con);
		((RadioButton) rbMode.findViewById(RadioBroadcastUtil
				.getIdByRadioModeSelect())).setChecked(true);
		((RadioButton) rbTimer.findViewById(RadioBroadcastUtil
				.getIdByRadioTimerSelect())).setChecked(true);

		rbMode.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(RadioGroup arg0, int arg1) {
				// TODO Auto-generated method stub
				switch (arg1) {
				case R.id.rb_singlechannelloop:// 单频道循环播放
					RadioBroadcastUtil.SETTING_RADIO_MODE = RadioBroadcastUtil.SETTING_RADIO_MODE_SINGLECHANNELLOOP;
					break;
				case R.id.rb_turnchannelorder:// 转频道顺序播放
					RadioBroadcastUtil.SETTING_RADIO_MODE = RadioBroadcastUtil.SETTING_RADIO_MODE_TURNCHANNELORDER;
					break;
				}
				RadioBroadcastUtil.saveIntToSP(con,
						RadioBroadcastUtil.VALUENAME_RADIO_MODE,
						RadioBroadcastUtil.SETTING_RADIO_MODE);
			}
		});
		rbTimer.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(RadioGroup arg0, int arg1) {
				// TODO Auto-generated method stub
				switch (arg1) {
				case R.id.rb_nolimit:
					RadioBroadcastUtil.SETTING_RADIO_TIME = RadioBroadcastUtil.SETTING_RADIO_TIME_NOLIMIT;
					break;
				case R.id.rb_minute60:
					RadioBroadcastUtil.SETTING_RADIO_TIME = RadioBroadcastUtil.SETTING_RADIO_TIME_MINUTE60;
					break;
				case R.id.rb_minute30:
					RadioBroadcastUtil.SETTING_RADIO_TIME = RadioBroadcastUtil.SETTING_RADIO_TIME_MINUTE30;
					break;
				case R.id.rb_minute10:
					RadioBroadcastUtil.SETTING_RADIO_TIME = RadioBroadcastUtil.SETTING_RADIO_TIME_MINUTE10;
					break;
				// case R.id.rb_custom:
				// RadioBroadcastUtil.SETTING_RADIO_TIME=RadioBroadcastUtil.SETTING_RADIO_TIME_MINUTE10;
				// break;
				}
				RadioBroadcastUtil.setPlayTimer(con);
			}
		});
	}

	@Override
	protected void onResume() {
		// initViews();
		super.onResume();
		// if (!TipsUtil.isTipShown(TipsUtil.TIP_DETAIL_MORE)
		// && PreferencesManager.getLanguageInfo(this).equals(
		// LanguageUtil.LANGUAGE_CH)) {
		// startActivity(TipsActivity.getTipIntent(this,
		// TipsUtil.TIP_DETAIL_MORE));
		// }
	}

	protected void onPause() {
		super.onPause();
	}

}
