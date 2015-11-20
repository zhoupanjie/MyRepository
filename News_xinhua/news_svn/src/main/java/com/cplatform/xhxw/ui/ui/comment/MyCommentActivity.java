package com.cplatform.xhxw.ui.ui.comment;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;

import com.cplatform.xhxw.ui.R;
import com.cplatform.xhxw.ui.ui.base.BaseActivity;
import com.cplatform.xhxw.ui.ui.cyancomment.CYanMyCommentFragment;

/**
 * 我的评论
 */
public class MyCommentActivity extends BaseActivity {

    private static final String TAG = MyCommentActivity.class.getSimpleName();
    
    private CYanMyCommentFragment mCommentFragment;

	public static Intent getIntent(Context context) {
    	Intent intent = new Intent(context, MyCommentActivity.class);
    	return intent;
    }

    @Override
    protected String getScreenName() {
        return "MyCommentActivity";
    }

    @Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_my_comment);
		initActionBar();
		initFragment();
	}
    
    private void initFragment() {
    	// 添加fragment
        FragmentTransaction t = getSupportFragmentManager().beginTransaction();
        if(mCommentFragment == null) {
        	mCommentFragment= new CYanMyCommentFragment();
        }
        if(mCommentFragment.isAdded()) {
        	t.show(mCommentFragment);
        } else {
        	t.replace(R.id.fg_my_comment, mCommentFragment);
        }
        
        t.commit();
    }
}
