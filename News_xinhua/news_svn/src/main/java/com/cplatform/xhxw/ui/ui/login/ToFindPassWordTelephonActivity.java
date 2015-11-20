package com.cplatform.xhxw.ui.ui.login;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.InputType;
import android.text.TextUtils;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

import com.google.gson.Gson;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.cplatform.xhxw.ui.R;
import com.cplatform.xhxw.ui.http.APIClient;
import com.cplatform.xhxw.ui.http.ResponseUtil;
import com.cplatform.xhxw.ui.http.net.*;
import com.cplatform.xhxw.ui.ui.base.BaseActivity;
import com.cplatform.xhxw.ui.util.MD5;
import com.cplatform.xhxw.ui.util.RegexUtil;

/**
 * 手机重置密码
 */
public class ToFindPassWordTelephonActivity extends BaseActivity{

	@InjectView(R.id.to_find_telephon)
	EditText mTelephone;
	@InjectView(R.id.to_find_code)
	EditText mCode;
	@InjectView(R.id.to_find_password)
	EditText mPassWord;
	@InjectView(R.id.to_find_code_btn)
	Button codeBtn;
	@InjectView(R.id.to_find_ok)
	Button toFindOk;
	@InjectView(R.id.to_find_time)
	TextView timeText;
	@InjectView(R.id.password_switch)
	ImageView passSwitch;
    private AsyncHttpResponseHandler mGetFindHandler, mGetFindPassWordHandler;
	
	private int time = 60;
	private boolean gone = true;//用于判断密码是否隐藏
	
    public static Intent getIntent(Context context) {
        Intent intent = new Intent(context, ToFindPassWordTelephonActivity.class);
        return intent;
    }

    @Override
    protected String getScreenName() {
        return "ToFindPassWordTelephonActivity";
    }

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tofind_password_telephon);
		ButterKnife.inject(this);
		passSwitch.setBackgroundResource(R.drawable.ic_showpassword_on_gone);
	}
	
	/** 获得验证码 */
	@OnClick(R.id.to_find_code_btn)
	public void getCode() {
		
		hideMethod(mTelephone);
		
		String phone = mTelephone.getText().toString().trim();
		
		if (TextUtils.isEmpty(phone)) {
			showToast(this.getResources().getString(R.string.find_password_telephon_account_hint));
		}
		
		if (!RegexUtil.isMobileNum(phone)) {
            showToast(R.string.register_telephone_format_error);
            return;
        }
		
		getFindCode(phone);
	}
	
	/** 确认找回 */
	@OnClick(R.id.to_find_ok)
	public void register() {
		
		hideMethod(mTelephone);
		
        String tetlePhone = mTelephone.getText().toString().trim();
		if (TextUtils.isEmpty(tetlePhone)) {
			showToast(this.getResources().getString(
                    R.string.find_password_telephon_account_hint));
		
		}
		
		if (!RegexUtil.isMobileNum(tetlePhone)) {
            showToast(R.string.register_telephone_format_error);
            return;
        }
        
        String code = mCode.getText().toString().trim();
		if (TextUtils.isEmpty(code)) {
			showToast(R.string.register_telephon_code_hint);
			return;
		}

        String passwd = mPassWord.getText().toString().trim();
		if (TextUtils.isEmpty(passwd)) {
			showToast(R.string.register_telephon_password_demand);
			return;
		}

		if (passwd.length() < 6 || passwd.length() > 16) {
			showToast(R.string.register_telephon_password_lenth_demand);
			return;
		}

		PhoneResetPasswordRequest request = new PhoneResetPasswordRequest();

		request.setAccount(tetlePhone);
		request.setPassword(MD5.toMD5(passwd));
		request.setValiedcode(code);

        resetPassword(request);
	}

	/** 返回 */
	@OnClick(R.id.btn_back)
	public void back() {
		this.finish();
	}
	
	/** 改变密码状态 */
	@OnClick(R.id.password_switch)
	public void passSwitch() {
		String pass = mPassWord.getText().toString().trim();
		if (TextUtils.isEmpty(pass)) {
			return;
		}
		
		if (gone) {
			passSwitch.setBackgroundResource(R.drawable.ic_showpassword_on);
			mPassWord.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
			mPassWord.setSelection(mPassWord.getText().length());
			gone = false;
		}else {
			passSwitch.setBackgroundResource(R.drawable.ic_showpassword_on_gone);
			mPassWord.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
			mPassWord.setSelection(mPassWord.getText().length());
			gone = true;
		}
	}
	
	/** 获取验证码 */
	private void getFindCode(String account) {

		CodeRequest request = new CodeRequest();
		request.setAccount(account);

		APIClient.getFindCode(request, new AsyncHttpResponseHandler() {

			@Override
			public void onFinish() {
				mGetFindHandler = null;
                hideLoadingView();
			}

			@Override
			protected void onPreExecute() {
				if (mGetFindHandler != null)
                    mGetFindHandler.cancle();
                mGetFindHandler = this;
                showLoadingView(R.string.please_wait);
			}

			@Override
			public void onSuccess(String content) {
				CodeResponse response;
				try {
					ResponseUtil.checkResponse(content);
					response = new Gson().fromJson(content, CodeResponse.class);
				} catch (Exception e) {
					showToast(R.string.data_format_error);
					return;
				}
				if (response.isSuccess()) {
//                  showToast(response.getData().getTips()); 
					showToast("验证码已发送，请等待60秒");
                    handler.sendEmptyMessageDelayed(MSG_TIMER_REFRESH, 1000);
				} else {
					if (response.getCode() == -4) {
//						showToast(response.getData().getTips());
						showToast("该用户不存在");
					}else if (response.getCode() == -407){
						showToast(response.getMsg());
					}
				}
			}

			@Override
			public void onFailure(Throwable error, String content) {
				showToast(R.string.load_server_failure);
			}
		});
	}

	/** 联网注册 */
	private void resetPassword(PhoneResetPasswordRequest request) {

		APIClient.phoneResetPassword(request, new AsyncHttpResponseHandler() {

            @Override
            public void onFinish() {
                mGetFindPassWordHandler = null;
                hideLoadingView();
            }

            @Override
            protected void onPreExecute() {
                if (mGetFindPassWordHandler != null) {
                    mGetFindPassWordHandler.cancle();
                }
                mGetFindPassWordHandler = this;
                showLoadingView(R.string.sending_now);
            }

            @Override
            public void onSuccess(String content) {
                BaseResponse response;
                try {
                	ResponseUtil.checkResponse(content);
                    response = new Gson().fromJson(content,
                            BaseResponse.class);
                } catch (Exception e) {
                    showToast(R.string.data_format_error);
                    return;
                }
                if (response.isSuccess()) {
                    setResult(RESULT_OK);
//                    showMessage("重置密码成功！");
                    showToast("重置密码成功！");
                    finish();
                } else {
                    showToast(response.getMsg());
                }
            }

            @Override
            public void onFailure(Throwable error, String content) {
                showToast(R.string.load_server_failure);
            }
        });
	}

	private final int MSG_TIMER_REFRESH = 0;
	private Handler handler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case MSG_TIMER_REFRESH:

				if (time == 0) {
					timeText.setVisibility(View.VISIBLE);
					codeBtn.setVisibility(View.GONE);
				} else {
					time--;
					timeText.setText(String.valueOf(time) + "秒");
					timeText.setVisibility(View.VISIBLE);
					codeBtn.setVisibility(View.GONE);
				}
				break;
			default: {
				super.handleMessage(msg);
				break;
			}
			}
			if (time == 0) {
				timeText.setVisibility(View.GONE);
				codeBtn.setVisibility(View.VISIBLE);
				time = 60;
			} else {
				handler.sendEmptyMessageDelayed(MSG_TIMER_REFRESH, 1000);
			}
		}
	};

	@Override
	protected void onDestroy() {
        if (handler != null) {
            handler.removeMessages(MSG_TIMER_REFRESH);
            handler = null;
        }
        if (mGetFindHandler != null) {
            mGetFindHandler.cancle();
            mGetFindHandler = null;
        }
        if (mGetFindPassWordHandler != null) {
            mGetFindPassWordHandler.cancle();
            mGetFindPassWordHandler = null;
        }
        super.onDestroy();
	}
	
	/** 隐藏输入法 */
	private void hideMethod(EditText editText) {
		InputMethodManager imm = (InputMethodManager) this
				.getSystemService(Context.INPUT_METHOD_SERVICE);
		imm.hideSoftInputFromWindow(editText.getWindowToken(), 0);
	}
}
