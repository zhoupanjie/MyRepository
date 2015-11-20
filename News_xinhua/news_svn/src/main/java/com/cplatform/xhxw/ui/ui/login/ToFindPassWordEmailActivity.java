package com.cplatform.xhxw.ui.ui.login;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
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
import com.cplatform.xhxw.ui.util.RegexUtil;

/**
 * 手机重置密码
 */
public class ToFindPassWordEmailActivity extends BaseActivity{

	@InjectView(R.id.et_email) EditText mEmail;
    private AsyncHttpResponseHandler mGetFindPassWordHandler;

    public static Intent getIntent(Context context) {
        Intent intent = new Intent(context, ToFindPassWordEmailActivity.class);
        return intent;
    }

    @Override
    protected String getScreenName() {
        return "ToFindPassWordEmailActivity";
    }

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tofind_password_email);
		ButterKnife.inject(this);
	}

	/** 确认找回 */
	@OnClick(R.id.to_find_ok)
	public void register() {
		hideMethod(mEmail);
        String email = mEmail.getText().toString().trim();
		if (TextUtils.isEmpty(email)) {
            showToast(R.string.find_password_telephon_account_hint);
		}

        if (!RegexUtil.isEmail(email)) {
            showToast(R.string.find_email_format_error);
        }

        findEmailPassword(email);
	}

	/** 返回 */
	@OnClick(R.id.btn_back)
	public void back() {
		this.finish();
	}

	private void findEmailPassword(String account) {

		APIClient.findEmailPassword(new FindEmailPasswordRequest(account), new AsyncHttpResponseHandler() {

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
//                    showMessage("");
                    showToast("链接已发送至邮箱");
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

	@Override
	protected void onDestroy() {
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
