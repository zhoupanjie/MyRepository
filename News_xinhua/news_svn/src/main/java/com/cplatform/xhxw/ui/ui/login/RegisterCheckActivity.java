package com.cplatform.xhxw.ui.ui.login;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import butterknife.ButterKnife;
import butterknife.OnClick;
import com.cplatform.xhxw.ui.R;
import com.cplatform.xhxw.ui.ui.base.BaseActivity;

/**
 * 注册
 */
public class RegisterCheckActivity extends BaseActivity {

	public static Intent getIntent(Context context) {
		Intent intent = new Intent(context, RegisterCheckActivity.class);
		return intent;
	}

	@Override
	protected String getScreenName() {
		return "RegisterCheckActivity";
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_register_check);
		ButterKnife.bind(this);
	}

	/** 手机注册 */
	@OnClick(R.id.telephon_relative)
	public void goTelephon() {
		startActivityForResult(RegisterTelephonActivity.getIntent(this, false),
				1);
	}

	/** email注册 */
	@OnClick(R.id.email_relative)
	public void goEmail() {
		startActivityForResult(RegisterEmailActivity.getIntent(this), 2);
	}

	/** 返回 */
	@OnClick(R.id.btn_back)
	public void back() {
		this.finish();
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (resultCode == RESULT_OK) {
			setResult(RESULT_OK);
			this.finish();
		}
		super.onActivityResult(requestCode, resultCode, data);
	}
}
