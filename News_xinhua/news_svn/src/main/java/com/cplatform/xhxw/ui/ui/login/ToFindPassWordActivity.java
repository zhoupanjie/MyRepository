package com.cplatform.xhxw.ui.ui.login;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import butterknife.ButterKnife;
import butterknife.OnClick;
import com.cplatform.xhxw.ui.R;
import com.cplatform.xhxw.ui.ui.base.BaseActivity;

/**
 * 找回密码
 */
public class ToFindPassWordActivity extends BaseActivity{

    public static Intent getIntent(Context context) {
        Intent intent = new Intent(context, ToFindPassWordActivity.class);
        return intent;
    }

    @Override
    protected String getScreenName() {
        return "ToFindPassWordActivity";
    }

    @Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_find_password_check);
		ButterKnife.inject(this);
	}
	
	/** 手机 */
	@OnClick(R.id.find_password_telephon)
	public void telephon() {
		startActivity(ToFindPassWordTelephonActivity.getIntent(this));
	}
	
	/** 邮箱 */
	@OnClick(R.id.find_password_email)
	public void email() {
		startActivity(ToFindPassWordEmailActivity.getIntent(this));
	}
	
	/** 返回 */
	@OnClick(R.id.btn_back)
	public void back(){
		this.finish();
	}
}
