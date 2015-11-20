package com.cplatform.xhxw.ui.ui.settings;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

import com.cplatform.xhxw.ui.R;
import com.cplatform.xhxw.ui.http.APIClient;
import com.cplatform.xhxw.ui.http.ResponseUtil;
import com.cplatform.xhxw.ui.http.net.BaseResponse;
import com.cplatform.xhxw.ui.http.net.ChangePasswordRequest;
import com.cplatform.xhxw.ui.ui.base.BaseActivity;
import com.cplatform.xhxw.ui.util.MD5;
import com.google.gson.Gson;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.wbtech.ums.UmsAgent;

public class ChangePasswordActivity extends BaseActivity {
	private EditText mOldPwEt;
	private EditText mNewPwEt;
	private EditText mNewPwVerifyEt;
	private Button mCommitBtn;
	private ProgressDialog mProgressDia;

	@Override
	protected String getScreenName() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_change_password);
		initActionBar();
		initViews();
	}

	private void initViews() {
		mOldPwEt = (EditText) findViewById(R.id.change_pw_old_pw_et);
		mNewPwEt = (EditText) findViewById(R.id.change_pw_new_pw_tv);
		mNewPwVerifyEt = (EditText) findViewById(R.id.change_pw_new_pw_verify_tv);
		mCommitBtn = (Button) findViewById(R.id.change_pw_commit_btn);
		mCommitBtn.setOnClickListener(mOnclickLis);
	}
	
	OnClickListener mOnclickLis = new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			if(v.getId() == R.id.change_pw_commit_btn) {
				final String oldPw = mOldPwEt.getText().toString().trim();
				final String newPw = mNewPwEt.getText().toString().trim();
				final String newPwVerify = mNewPwVerifyEt.getText().toString().trim();
				if(isValuesValid(oldPw, newPw, newPwVerify)) {
					new AlertDialog.Builder(ChangePasswordActivity.this)
					.setTitle(R.string.change_pw_title)
					.setMessage(R.string.change_pw_commmit_verify)
					.setPositiveButton(R.string.confirm, new DialogInterface.OnClickListener() {
						
						@Override
						public void onClick(DialogInterface dialog, int which) {
							UmsAgent.onEvent(ChangePasswordActivity.this, "change_pw_commit_btn");
							commitPwd(oldPw, newPw, newPwVerify);
						}
					})
					.setNegativeButton(R.string.cancel, null).show();
				}
			}
		}
	};
	
	private void commitPwd(String oldPw, String newPw, String newPwVerify) {
		
		mProgressDia = ProgressDialog.show(this, getString(R.string.change_pw_title), 
				getString(R.string.commit_dialog_msg));
		ChangePasswordRequest request = new ChangePasswordRequest();
		request.setOldpwd(MD5.toMD5(oldPw));
		request.setNewpwd(MD5.toMD5(newPw));
		request.setSecpwd(MD5.toMD5(newPwVerify));
		APIClient.changePassword(request, new AsyncHttpResponseHandler(){

			@Override
			public void onFinish() {
				mProgressDia.dismiss();
				super.onFinish();
			}

			@Override
			public void onSuccess(int statusCode, String content) {
				BaseResponse response;
				try {
					ResponseUtil.checkResponse(content);
					response = new Gson().fromJson(content, BaseResponse.class);
				} catch (Exception e) {
					showToast(R.string.change_pw_fail);
					return;
				}
				
				if(response != null && response.isSuccess()) {
					showToast(R.string.change_pw_success);
					finish();
				} else {
					if(response == null) {
						showToast(R.string.change_pw_fail);
					} else {
						showToast(response.getMsg());
					}
				}
				
				super.onSuccess(statusCode, content);
			}

			@Override
			public void onFailure(Throwable error, String content) {
				showToast(content);
				super.onFailure(error, content);
			}
		});
	}
	
	private boolean isValuesValid(String oldPw, String newPw, String newPwVerify) {
		if(oldPw == null || oldPw.length() < 1 || newPw == null || newPw.length() < 1
				|| newPwVerify == null || newPwVerify.length() < 1) {
			showToast(R.string.change_pw_empty_content);
			return false;
		}
		if(!newPw.equals(newPwVerify)) {
			showToast(R.string.change_pw_new_pw_verify_error);
			return false;
		}
		if(newPw.length() < 6 || newPw.length() > 16) {
			showToast(R.string.change_pw_invalid_length);
			return false;
		}
		return true;
	}
}
