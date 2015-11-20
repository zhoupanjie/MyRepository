package com.cplatform.xhxw.ui.ui.main.saas.addressBook;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.Toast;

import com.cplatform.xhxw.ui.Constants;
import com.cplatform.xhxw.ui.R;
import com.cplatform.xhxw.ui.http.APIClient;
import com.cplatform.xhxw.ui.http.ResponseUtil;
import com.cplatform.xhxw.ui.ui.base.BaseActivity;
import com.cplatform.xhxw.ui.util.LogUtil;
import com.google.gson.Gson;
import com.loopj.android.http.AsyncHttpResponseHandler;

/**
 * 好友验证
 */
public class FriendsVerifyActivity extends BaseActivity {

	private static final String TAG = FriendsVerifyActivity.class
			.getSimpleName();

	
	private EditText editText;
	
	private String friendId;
	private AsyncHttpResponseHandler mHttpResponseHandler;

	public static Intent newIntent(Context context, String friendId) {
		Intent intent = new Intent(context, FriendsVerifyActivity.class);
		intent.putExtra("friendId", friendId);
		return intent;
	}

	@Override
	protected String getScreenName() {
		return TAG;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_friends_verify);

		init();
	}

	private void init() {
		initActionBar();
		friendId = getIntent().getStringExtra("friendId");
		
		editText = (EditText) findViewById(R.id.friends_verify_edit);
//		editText.setHint("我是" + Constants.userInfo.getEnterprise().getStaff_name());
//		editText.setText("我是" + Constants.userInfo.getEnterprise().getStaff_name());
		editText.setText("我是" + Constants.userInfo.getNickName());
		
		findViewById(R.id.send_verify).setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				sendFriendsVerify(friendId);
			}
		});
	}

	/** 向朋友发送验证信息 */
	private void sendFriendsVerify(String friendId) {

		if (TextUtils.isEmpty(editText.getText().toString().trim())) {
			Toast.makeText(this, "请输入验证信息", Toast.LENGTH_SHORT).show();
			return;
		}

		SendFriendsVerifyRequest request = new SendFriendsVerifyRequest();
		request.setUserid(friendId);
		request.setMsg(editText.getText().toString().trim());
		request.setDevRequest(true);
		
		APIClient.sendFriendsVerify(request, new AsyncHttpResponseHandler() {

			@Override
			public void onFinish() {
				mHttpResponseHandler = null;
				hideLoadingView();
			}

			@Override
			protected void onPreExecute() {
				if (mHttpResponseHandler != null) {
					mHttpResponseHandler.cancle();
				}
				mHttpResponseHandler = this;
				
				showLoadingView();
			}

			@Override
			public void onSuccess(String content) {
				SendFriendsVerifyResponse response;
				try {
					ResponseUtil.checkResponse(content);
					response = new Gson().fromJson(content,
							SendFriendsVerifyResponse.class);
				} catch (Exception e) {
					showToast(R.string.data_format_error);
					LogUtil.w(TAG, e);
					return;
				}
				if (response.isSuccess()) {
//					verifyDialog("验证信息发送成功", true);
					showToast("验证信息发送成功");
					setResult(Activity.RESULT_OK);
					FriendsVerifyActivity.this.finish();
				} else {
					// showToast(response.getMsg());
//					verifyDialog("验证信息发送失败请稍后再试", false);
					showToast("验证信息发送失败请稍后再试");
				}
			}

			@Override
			public void onFailure(Throwable error, String content) {
				showToast(R.string.load_server_failure);
			}
		});
	}

	/** 显示验证码对话框 
	 * true 表示成功
	 * false 表示失败
	 * */
	private void verifyDialog(String message, final boolean state) {
		new VerifyDialog(this, message,
				new VerifyDialog.OnVerifyDialogListener() {

					@Override
					public void verifyDialog() {
						if (state) {
							setResult(Activity.RESULT_OK);
							FriendsVerifyActivity.this.finish();
						}
					}
				}).show();
	}

}
