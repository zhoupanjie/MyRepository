package com.cplatform.xhxw.ui.ui.message;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;

import com.cplatform.xhxw.ui.R;
import com.cplatform.xhxw.ui.ui.base.BaseActivity;
import com.cplatform.xhxw.ui.ui.main.saas.PushMessagesFragment;

/**
 * 获得push message
 * Created by cy-love on 13-12-24.
 */
public class MessageActivity extends BaseActivity {
	
    public static Intent getIntent(Context context) {
    	Intent intent = new Intent(context, MessageActivity.class);
    	return intent;
    }
    @Override
    protected String getScreenName() {
        return "MessageActivity";
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
    	super.onCreate(savedInstanceState);
    	setContentView(R.layout.activity_message);
    	initActionBar();
    	initFragment();
    }
    
    private void initFragment() {
    	// 添加fragment
        FragmentTransaction t = getSupportFragmentManager().beginTransaction();
        PushMessagesFragment mPushFrangment= new PushMessagesFragment();
        Bundle bundle = new Bundle();
    	bundle.putBoolean("isEnterprise", false);
    	mPushFrangment.setArguments(bundle);
    	t.replace(R.id.message_fragment_container, mPushFrangment);
        t.commit();
    }
}