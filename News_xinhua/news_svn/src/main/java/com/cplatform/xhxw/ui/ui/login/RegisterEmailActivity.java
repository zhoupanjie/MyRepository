package com.cplatform.xhxw.ui.ui.login;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.TextView;
import butterknife.ButterKnife;
import butterknife.Bind;
import butterknife.OnClick;

import com.cplatform.xhxw.ui.Constants;
import com.cplatform.xhxw.ui.R;
import com.cplatform.xhxw.ui.http.APIClient;
import com.cplatform.xhxw.ui.http.ResponseUtil;
import com.cplatform.xhxw.ui.http.net.RegisterRequest;
import com.cplatform.xhxw.ui.http.net.RegisterResonse;
import com.cplatform.xhxw.ui.ui.base.BaseActivity;
import com.cplatform.xhxw.ui.ui.web.WebViewActivity;
import com.cplatform.xhxw.ui.util.LogUtil;
import com.cplatform.xhxw.ui.util.MD5;
import com.cplatform.xhxw.ui.util.RegexUtil;
import com.google.gson.Gson;
import com.loopj.android.http.AsyncHttpResponseHandler;

/**
 * 邮箱注册
 */
public class RegisterEmailActivity extends BaseActivity implements
		OnCheckedChangeListener {

	private static final String TAG = RegisterEmailActivity.class
			.getSimpleName();
	@Bind(R.id.email_account)
	EditText account;
	@Bind(R.id.email_password)
	EditText passWord;
	@Bind(R.id.register_email)
	Button register;
	@Bind(R.id.register_checkBox)
	CheckBox checkBox;
//	@Bind(R.id.register_checkBox)
	CheckBox terms_service_text;
	@Bind(R.id.agreement)
	TextView agreement;
	private AsyncHttpResponseHandler mHttpHandler;

	public static Intent getIntent(Context context) {
		Intent intent = new Intent(context, RegisterEmailActivity.class);
		return intent;
	}

	@Override
	protected String getScreenName() {
		return "RegisterEmailActivity";
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_register_email);

		ButterKnife.bind(this);

		checkBox.setOnCheckedChangeListener(this);
		checkBox.setBackgroundResource(R.drawable.ic_checkbox_uncheck);

		SpannableString tSS = new SpannableString(this.getResources()
				.getString(R.string.register_agreement));
		/** 设置字体颜色 */
		tSS.setSpan(new ForegroundColorSpan(Color.RED), 3, 9,
				Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
		tSS.setSpan(new ForegroundColorSpan(Color.RED), 10, tSS.length(),
				Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
		tSS.setSpan(new ClickableSpan() {
			@Override
			public void onClick(View widget) {
				startActivity(WebViewActivity
						.getServiceIntent(RegisterEmailActivity.this));
			}
		}, 3, 9, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
		tSS.setSpan(new ClickableSpan() {
			@Override
			public void onClick(View widget) {
				startActivity(WebViewActivity
						.getPrivacyIntent(RegisterEmailActivity.this));
			}
		}, 10, tSS.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
		agreement.setText(tSS);
		agreement.setText(tSS);
		agreement.setLinkTextColor(Color.RED);
		agreement.setMovementMethod(LinkMovementMethod.getInstance());
		agreement.setFocusable(false);
		agreement.setClickable(false);
		agreement.setLongClickable(false);
	}

	/** 注册 */
	@OnClick(R.id.register_email)
	public void register() {

		hideMethod(account);

		String email = account.getText().toString().trim();
		if (TextUtils.isEmpty(email)) {
			showToast(R.string.register_email_account_demand);
			return;
		}

		if (!RegexUtil.isEmail(email)) {
			showToast(R.string.register_email_format_error);
			return;
		}
		String passwd = passWord.getText().toString().trim();
		if (TextUtils.isEmpty(passwd)) {
			showToast(R.string.register_email_password_demand);
			return;
		}

		if (passwd.length() < 6 || passwd.length() > 16) {
			showToast(R.string.register_email_password_lenth_demand);
			return;
		}

		if (!checkBox.isChecked()) {
			showToast(R.string.register_agreement_demand);
			return;
		}

		RegisterRequest request = new RegisterRequest();
		request.setAccount(account.getText().toString().trim());
		request.setPasswd(MD5.getMd5(passWord.getText().toString().trim()));

		register(request);
	}

	/** 返回 */
	@OnClick(R.id.btn_back)
	public void back() {
		this.finish();
	}

	private void register(RegisterRequest request) {

		APIClient.register(request, new AsyncHttpResponseHandler() {

			@Override
			public void onFinish() {
				mHttpHandler = null;
				hideLoadingView();
			}

			@Override
			protected void onPreExecute() {
				if (mHttpHandler != null) {
					mHttpHandler.cancle();
				}
				mHttpHandler = this;
				showLoadingView(R.string.sending_now);
			}

			@Override
			public void onSuccess(String content) {
				RegisterResonse response;
				try {
					ResponseUtil.checkResponse(content);
					response = new Gson().fromJson(content,
							RegisterResonse.class);
				} catch (Exception e) {
					showToast(R.string.data_format_error);
					LogUtil.w(TAG, e);
					return;
				}
				if (response.isSuccess()) {
					Constants.userInfo = response.getData();
					setResult(RESULT_OK);
					showToast(R.string.register_email_ok__to);
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
	public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
		if (isChecked) {
			checkBox.setBackgroundResource(R.drawable.ic_checkbox_check);
		} else {
			checkBox.setBackgroundResource(R.drawable.ic_checkbox_uncheck);
		}
	}

	@Override
	protected void onDestroy() {
		if (mHttpHandler != null) {
			mHttpHandler.cancle();
			mHttpHandler = null;
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
