package com.cplatform.xhxw.ui.ui.main.saas.personalcenter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;

import com.cplatform.xhxw.ui.R;
import com.cplatform.xhxw.ui.ui.base.BaseActivity;

public class SaasUserinfoActivity extends BaseActivity {

	private SaasUserInfoFragment mUserinfoFrag;
	
	public static Intent getIntent(Context context) {
        Intent intent = new Intent(context, SaasUserinfoActivity.class);
        return intent;
    }
	
	@Override
	protected String getScreenName() {
		return null;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.saas_activity_userinfo);
		initActionBar();
		initFragment();
	}

	private void initFragment() {
        FragmentTransaction t = getSupportFragmentManager().beginTransaction();
        if(mUserinfoFrag == null) {
        	mUserinfoFrag= new SaasUserInfoFragment();
        }
        if(mUserinfoFrag.isAdded()) {
        	t.show(mUserinfoFrag);
        } else {
        	t.replace(R.id.saas_fg_userinfo, mUserinfoFrag);
        }
        
        t.commit();
    }
}
