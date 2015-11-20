package com.cplatform.xhxw.ui.ui.settings;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import butterknife.ButterKnife;
import butterknife.Bind;
import butterknife.OnClick;

import com.cplatform.xhxw.ui.Constants;
import com.cplatform.xhxw.ui.R;
import com.cplatform.xhxw.ui.http.APIClient;
import com.cplatform.xhxw.ui.http.ResponseUtil;
import com.cplatform.xhxw.ui.http.net.BaseResponse;
import com.cplatform.xhxw.ui.http.net.BindMobileRequest;
import com.cplatform.xhxw.ui.http.net.RedEnvelopesCodeRequest;
import com.cplatform.xhxw.ui.http.net.RedEnvelopesCodeResponse;
import com.cplatform.xhxw.ui.ui.base.BaseActivity;
import com.cplatform.xhxw.ui.ui.web.RedenvelopesCallBack;
import com.cplatform.xhxw.ui.util.LogUtil;
import com.cplatform.xhxw.ui.util.RegexUtil;
import com.google.gson.Gson;
import com.loopj.android.http.AsyncHttpResponseHandler;

/**
 * 手机绑定
 */
public class BindPhoneActivity extends BaseActivity {

	private static final String TAG = BindPhoneActivity.class.getSimpleName();
	@Bind(R.id.bind_telephon)
	EditText mTelephone;
	@Bind(R.id.bind_security_code)
	EditText mCode;
	@Bind(R.id.security_code)
	Button mSecurityCode;
	@Bind(R.id.bind_mobile)
	Button bind_mobile;
	@Bind(R.id.time)
	TextView mTimeText;

	private AsyncHttpResponseHandler mReigstHandler, mCodeHandler;

	private int time = 60;

	private static final int TYPE_DISABLE = 0;// 无法使用流量快捷注册
	private static final int TYPE_SUCCESS = 1;// 注册成功
	private static final int TYPE_ALREADY = 2;// 已注册过

	private static RedenvelopesCallBack callback;// 从红包网页跳转的回调对象

	public static Intent getIntent(Context context) {
		Intent intent = new Intent(context, BindPhoneActivity.class);
		return intent;
	}

	public static Intent getIntent(Context context,
			RedenvelopesCallBack redenvelopesCallBack) {
		Intent intent = new Intent(context, BindPhoneActivity.class);
		callback = redenvelopesCallBack;
		return intent;
	}

	@Override
	protected String getScreenName() {
		return "BindPhoneActivity";
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_bind_phone);

		ButterKnife.bind(this);

		if (callback != null) {
			gotoHome = false;
		}
		Bundle bun = getIntent().getExtras();
		if (bun == null) {
			LogUtil.w(TAG, "bundle is null!");
			return;
		}
	}

	/** 获得验证码 */
	@OnClick(R.id.security_code)
	public void getCode() {
		String tetlephone = mTelephone.getText().toString().trim();

		if (TextUtils.isEmpty(tetlephone)) {
			showToast(R.string.bind_phone_number_input_please);
			return;
		}

		if (!RegexUtil.isMobileNum(tetlephone)) {
			showToast(R.string.register_telephone_format_error);
			return;
		}

		bindRedEnvelopesCode(mTelephone.getText().toString().trim());
	}

	/** 绑定手机号 */
	@OnClick(R.id.bind_mobile)
	public void bindMobile() {

		String tetlephone = mTelephone.getText().toString().trim();
		if (TextUtils.isEmpty(tetlephone)) {
			showToast(R.string.bind_phone_number_input_please);
			return;
		}

		if (!RegexUtil.isMobileNum(tetlephone)) {
			showToast(R.string.register_telephone_format_error);
			return;
		}

		String code = mCode.getText().toString().trim();
		if (TextUtils.isEmpty(code)) {
			showToast(R.string.register_telephon_code_hint);
			return;
		}

		BindMobileRequest request = new BindMobileRequest();

		request.setBindmobile(tetlephone);
		request.setCode(code);

		bind(request);
	}

	/** 红包注册-获取验证码 */
	private void bindRedEnvelopesCode(String tel_number) {

		RedEnvelopesCodeRequest request = new RedEnvelopesCodeRequest();
		request.setBindmobile(tel_number);

		APIClient.getRedEnvelopesCode(request, new AsyncHttpResponseHandler() {

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
				RedEnvelopesCodeResponse response;
				try {
					ResponseUtil.checkResponse(content);
					response = new Gson().fromJson(content,
							RedEnvelopesCodeResponse.class);
				} catch (Exception e) {
					showToast(R.string.data_format_error);
					LogUtil.w(TAG, e);
					return;
				}
				if (response.isSuccess()) {
					// showToast(response.getData().getTips());
					handler.sendEmptyMessageDelayed(MSG_TIMER_REFRESH, 1000);
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

	/** 返回 */
	@OnClick(R.id.btn_back)
	public void back() {
		this.finish();
	}

	/** 绑定 */
	private void bind(final BindMobileRequest request) {

		APIClient.bindMobile(request, new AsyncHttpResponseHandler() {

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
				BaseResponse response;
				try {
					ResponseUtil.checkResponse(content);
					response = new Gson().fromJson(content, BaseResponse.class);
				} catch (Exception e) {
					showToast(R.string.data_format_error);
					LogUtil.w(TAG, e);
					return;
				}
				if (response.isSuccess()) {
					showToast(R.string.bind_phone_number_success);
					if(Constants.userInfo!=null)
					{
						Constants.userInfo.setBindmobile(request.getBindmobile());
					}
					// setResult(RESULT_OK);
					// finish();
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

	// private void dialog(final int type) {
	// String message = "";
	// switch (type) {
	// case TYPE_DISABLE:
	// message = getString(R.string.red_envelopes_bind_disable);
	// break;
	// case TYPE_ALREADY:
	// message = getString(R.string.red_envelopes_bind_already);
	// break;
	// default:
	// message = getString(R.string.red_envelopes_bind_success);
	// break;
	// }
	//
	// new AlertDialog.Builder(this)
	// // .setTitle(R.string.edit_channel_manage)
	// .setMessage(
	// message)
	// .setPositiveButton(R.string.confirm,
	// new DialogInterface.OnClickListener() {
	//
	// @Override
	// public void onClick(DialogInterface dialog,
	// int which) {
	// switch (type) {
	// case TYPE_DISABLE:
	//
	// break;
	// case TYPE_ALREADY:
	// finish();
	// break;
	// default:
	// setResult(RESULT_OK);
	// finish();
	// break;
	// }
	// }
	// }).show();
	// }

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
		if (callback != null) {
			callback.bind();
			callback = null;
		}
		super.onDestroy();
	}
}
