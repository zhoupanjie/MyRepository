package com.cplatform.xhxw.ui.ui.login;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.InputType;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

import com.cplatform.xhxw.ui.Constants;
import com.cplatform.xhxw.ui.R;
import com.cplatform.xhxw.ui.http.APIClient;
import com.cplatform.xhxw.ui.http.ResponseUtil;
import com.cplatform.xhxw.ui.http.net.CodeRequest;
import com.cplatform.xhxw.ui.http.net.CodeResponse;
import com.cplatform.xhxw.ui.http.net.RedEnvelopesCodeRequest;
import com.cplatform.xhxw.ui.http.net.RedEnvelopesCodeResponse;
import com.cplatform.xhxw.ui.http.net.RegisterRequest;
import com.cplatform.xhxw.ui.http.net.RegisterResonse;
import com.cplatform.xhxw.ui.model.Channe;
import com.cplatform.xhxw.ui.ui.base.BaseActivity;
import com.cplatform.xhxw.ui.ui.picShow.PicShowActivity;
import com.cplatform.xhxw.ui.ui.web.WebViewActivity;
import com.cplatform.xhxw.ui.util.LogUtil;
import com.cplatform.xhxw.ui.util.MD5;
import com.cplatform.xhxw.ui.util.RegexUtil;
import com.google.gson.Gson;
import com.loopj.android.http.AsyncHttpResponseHandler;

/**
 * 手机注册
 */
public class RegisterTelephonActivity extends BaseActivity implements
		OnCheckedChangeListener {

	private static final String TAG = RegisterTelephonActivity.class
			.getSimpleName();
	@InjectView(R.id.register_telephon)
	EditText mTelephone;
	@InjectView(R.id.register_security_code)
	EditText mCode;
	@InjectView(R.id.register_password)
	EditText mPassWord;
	@InjectView(R.id.security_code)
	Button mSecurityCode;
	@InjectView(R.id.login_register)
	Button mRegister;
	@InjectView(R.id.register_checkBox)
	CheckBox mCheckBox;
	@InjectView(R.id.time)
	TextView mTimeText;
	@InjectView(R.id.agreement)
	TextView mAgreement;
	@InjectView(R.id.password_switch)
	ImageView passSwitch;

	private AsyncHttpResponseHandler mReigstHandler, mCodeHandler;

	private int time = 60;
	private boolean gone = true;// 用于判断密码是否隐藏
	private boolean isRedEnvelopes = false;

	private static final int TYPE_DISABLE = 0;// 无法使用流量快捷注册
	private static final int TYPE_SUCCESS = 1;// 注册成功
	private static final int TYPE_ALREADY = 2;// 已注册过

	public static Intent getIntent(Context context, boolean isRedEnvelopes) {
		Intent intent = new Intent(context, RegisterTelephonActivity.class);
		Bundle bun = new Bundle();
		bun.putBoolean("isRedEnvelopes", isRedEnvelopes);
		intent.putExtras(bun);
		return intent;
	}

	@Override
	protected String getScreenName() {
		return "RegisterTelephonActivity";
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_register_telephon);

		ButterKnife.inject(this);

		Bundle bun = getIntent().getExtras();
		if (bun == null) {
			LogUtil.w(TAG, "bundle is null!");
			return;
		}
		isRedEnvelopes = bun.getBoolean("isRedEnvelopes");

		mCheckBox.setOnCheckedChangeListener(this);
		mCheckBox.setBackgroundResource(R.drawable.ic_checkbox_uncheck);

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
						.getServiceIntent(RegisterTelephonActivity.this));
			}
		}, 3, 9, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
		tSS.setSpan(new ClickableSpan() {
			@Override
			public void onClick(View widget) {
				startActivity(WebViewActivity
						.getPrivacyIntent(RegisterTelephonActivity.this));
			}
		}, 10, tSS.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
		mAgreement.setText(tSS);
		mAgreement.setLinkTextColor(Color.RED);
		mAgreement.setMovementMethod(LinkMovementMethod.getInstance());
		mAgreement.setFocusable(false);
		mAgreement.setClickable(false);
		mAgreement.setLongClickable(false);

		passSwitch.setBackgroundResource(R.drawable.ic_showpassword_on_gone);
	}

	/** 获得验证码 */
	@OnClick(R.id.security_code)
	public void getCode() {
		String tetlephone = mTelephone.getText().toString().trim();

		if (TextUtils.isEmpty(tetlephone)) {
			showToast(R.string.register_telephon_account_demand);
			return;
		}

		if (!RegexUtil.isMobileNum(tetlephone)) {
			showToast(R.string.register_telephone_format_error);
			return;
		}
			registerCode(mTelephone.getText().toString().trim());
		}


	/** 注册 */
	@OnClick(R.id.login_register)
	public void register() {

		String tetlephone = mTelephone.getText().toString().trim();
		if (TextUtils.isEmpty(tetlephone)) {
			showToast(R.string.register_telephon_account_demand);
			return;
		}

		if (!RegexUtil.isMobileNum(tetlephone)) {
			showToast(R.string.register_telephone_format_error);
			return;
		}

		String password = mPassWord.getText().toString().trim();
		if (TextUtils.isEmpty(password)) {
			showToast(R.string.register_telephon_password_demand);
			return;
		}

		String code = mCode.getText().toString().trim();
		if (TextUtils.isEmpty(code)) {
			showToast(R.string.register_telephon_code_hint);
			return;
		}

		if (password.length() < 6 || password.length() > 16) {
			showToast(R.string.register_telephon_password_lenth_demand);
			return;
		}

		if (!mCheckBox.isChecked()) {
			showToast(R.string.register_agreement_demand);
			return;
		}

		RegisterRequest request = new RegisterRequest();

		request.setAccount(tetlephone);
		request.setPasswd(MD5.toMD5(password));
		if (isRedEnvelopes) {
			request.setRegsource("fluxredpack");
		}
		request.setValiedcode(code);

		register(request);
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
			mPassWord
					.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
			mPassWord.setSelection(mPassWord.getText().length());
			gone = false;
		} else {
			mPassWord.setInputType(InputType.TYPE_CLASS_TEXT
					| InputType.TYPE_TEXT_VARIATION_PASSWORD);
			mPassWord.setSelection(mPassWord.getText().length());
			gone = true;
			passSwitch
					.setBackgroundResource(R.drawable.ic_showpassword_on_gone);
		}
	}

	@Override
	public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
		if (isChecked) {
			mCheckBox.setBackgroundResource(R.drawable.ic_checkbox_check);
		} else {
			mCheckBox.setBackgroundResource(R.drawable.ic_checkbox_uncheck);
		}
	}

	/** 联网注册 */
	private void register(RegisterRequest request) {

		APIClient.register(request, new AsyncHttpResponseHandler() {

			@Override
			public void onFinish() {
				mReigstHandler = null;
				hideLoadingView();
			}

			@Override
			protected void onPreExecute() {
				if (mReigstHandler != null)
					mReigstHandler.cancle();
				mReigstHandler = this;
				showLoadingView();
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

	/** 获取验证码 */
	private void registerCode(String account) {

		CodeRequest request = new CodeRequest();
		request.setAccount(account);

		APIClient.getCode(request, new AsyncHttpResponseHandler() {

			@Override
			public void onFinish() {
				mCodeHandler = null;
			}

			@Override
			protected void onPreExecute() {
				if (mCodeHandler != null)
					mCodeHandler.cancle();
				mCodeHandler = this;
			}

			@Override
			public void onSuccess(String content) {
				CodeResponse response;
				try {
					ResponseUtil.checkResponse(content);
					response = new Gson().fromJson(content, CodeResponse.class);
				} catch (Exception e) {
					showToast(R.string.data_format_error);
					LogUtil.w(TAG, e);
					return;
				}
				if (response.isSuccess()) {
					// showToast(response.getData().getTips());
					handler.sendEmptyMessageDelayed(MSG_TIMER_REFRESH, 1000);
				} else {
					showToast(response.getData().getTips());
				}
			}

			@Override
			public void onFailure(Throwable error, String content) {
				showToast(R.string.load_server_failure);
			}
		});
	}

	private void dialog(final int type) {
		String message = "";
		switch (type) {
		case TYPE_DISABLE:
			message = getString(R.string.red_envelopes_register_disable);
			break;
		case TYPE_ALREADY:
			message = getString(R.string.red_envelopes_register_already);
			break;
		default:
			message = getString(R.string.red_envelopes_register_success);
			break;
		}

		new AlertDialog.Builder(this)
				// .setTitle(R.string.edit_channel_manage)
				.setMessage(message)
				.setPositiveButton(R.string.confirm,
						new DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								switch (type) {
								case TYPE_DISABLE:

									break;
								case TYPE_ALREADY:
									finish();
									break;
								default:
									setResult(RESULT_OK);
									finish();
									break;
								}
							}
						}).show();
	}

	private final int MSG_TIMER_REFRESH = 0;
	private Handler handler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case MSG_TIMER_REFRESH:

				if (time == 0) {
					mTimeText.setVisibility(View.VISIBLE);
					mSecurityCode.setVisibility(View.GONE);
				} else {
					time--;
					mTimeText.setText(String.valueOf(time) + "秒");
					mTimeText.setVisibility(View.VISIBLE);
					mSecurityCode.setVisibility(View.GONE);
				}
				break;
			default: {
				super.handleMessage(msg);
				break;
			}
			}
			if (time == 0) {
				mTimeText.setVisibility(View.GONE);
				mSecurityCode.setVisibility(View.VISIBLE);
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
		if (mReigstHandler != null) {
			mReigstHandler.cancle();
			mReigstHandler = null;
		}
		if (mCodeHandler != null) {
			mCodeHandler.cancle();
			mCodeHandler = null;
		}
		super.onDestroy();
	}
}
