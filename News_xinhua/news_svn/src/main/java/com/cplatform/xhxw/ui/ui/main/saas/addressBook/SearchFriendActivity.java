package com.cplatform.xhxw.ui.ui.main.saas.addressBook;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;
import com.cplatform.xhxw.ui.R;
import com.cplatform.xhxw.ui.ui.base.BaseActivity;
import com.cplatform.xhxw.ui.ui.base.TextWatcher;
import com.cplatform.xhxw.ui.util.EditUtil;
import com.cplatform.xhxw.ui.util.KeyboardUtil;

/**
 * 搜索添加好友
 */
public class SearchFriendActivity extends BaseActivity implements OnEditorActionListener{

	private  static final String TAG = SearchFriendActivity.class.getSimpleName();
	
	public static Intent newIntent(Context context) {
		Intent intent = new Intent(context, SearchFriendActivity.class);
		return intent;
	}
	
	private EditText editText;
	private Button searchBtn;
    private ImageView mSearchClean;
	
	@Override
	protected String getScreenName() {
		return TAG;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.activity_search_friend);
		
		init();
	}
	
	private void init() {
		initActionBar();
		editText = (EditText) findViewById(R.id.search_edit);
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() > 0) {
                    mSearchClean.setVisibility(View.VISIBLE);
                } else {
                    mSearchClean.setVisibility(View.GONE);
                }
            }
        });
		searchBtn = (Button) findViewById(R.id.search_btn);
        mSearchClean = (ImageView) findViewById(R.id.iv_search_clear);
        mSearchClean.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                editText.setText(null);
            }
        });
		editText.setOnEditorActionListener(this);
		
		searchBtn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View view) {
				if (!TextUtils.isEmpty(editText.getText().toString().trim())) {
					startActivity(SearchResultActivity.newIntent(SearchFriendActivity.this, editText.getText().toString().trim()));
				}else {
					showToast("请输入关键词");
				}
			}
		});
        KeyboardUtil.showSoftInputDelay(editText);
		
	}

	 @Override
	    public boolean onEditorAction(TextView arg0, int arg1, KeyEvent arg2) {
	        String editSearch = editText.getText().toString().trim();
	        if (!TextUtils.isEmpty(editSearch)) {
	        	EditUtil.hideMethod(this, editText);
				startActivity(SearchResultActivity.newIntent(SearchFriendActivity.this, editText.getText().toString().trim()));
			}else {
				showToast("请输入关键词");
			}
	        return true;
	    }
}
