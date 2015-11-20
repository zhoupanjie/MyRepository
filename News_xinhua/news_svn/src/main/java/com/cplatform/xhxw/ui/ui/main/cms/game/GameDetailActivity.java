package com.cplatform.xhxw.ui.ui.main.cms.game;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.widget.RelativeLayout;

import com.cplatform.xhxw.ui.R;
import com.cplatform.xhxw.ui.ui.base.BaseActivity;

public class GameDetailActivity extends BaseActivity {
	public static Intent getIntent(Context context, String newsid,
			boolean isEnterprise) {
		Intent intent = new Intent(context, GameDetailActivity.class);
		Bundle bundle = new Bundle();
		bundle.putString("newsid", newsid);
		bundle.putBoolean("isEnterprise", isEnterprise);
		intent.putExtras(bundle);
		return intent;
	}

	@Override
	protected String getScreenName() {
		// TODO Auto-generated method stub
		return null;
	}

	private GameDetailFragment mContent;
	private RelativeLayout rlBanner;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_news_page);
		// boolean isPush = getIntent().getBooleanExtra("isPush", false);
		// if (isPush) {
		// App.getPreferenceManager().messageNewCountMinus();
		// }
		// 添加fragment
		FragmentTransaction t = getSupportFragmentManager().beginTransaction();
		Bundle bundle = getIntent().getExtras();
		if (bundle == null) {
			this.finish();
			return;
		}

		mContent = new GameDetailFragment();
		mContent.setArguments(bundle);
		t.replace(R.id.fg_content, mContent);
		t.commit();
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
		has_channel_id = true;
		super.onPause();
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		switch (requestCode) {
		case GameUtil.REQUEST_CODE_INSTALL: {
			mContent.onActivityResult(requestCode, resultCode, data);
		}
			break;
		}
		super.onActivityResult(requestCode, resultCode, data);
	}
}
