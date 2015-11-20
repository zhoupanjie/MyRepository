package com.cplatform.xhxw.ui.ui.feedback;

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
import com.cplatform.xhxw.ui.http.net.FeedBackRequest;
import com.cplatform.xhxw.ui.http.net.GetUserInfoResponse;
import com.cplatform.xhxw.ui.ui.base.BaseActivity;

public class FeedbackActivity extends BaseActivity{

	@InjectView(R.id.publish_feed_edit)
	EditText editText;
	
	public static Intent getIntent(Context context) {
        Intent intent = new Intent(context, FeedbackActivity.class);
        return intent;
    }

    @Override
    protected String getScreenName() {
        return "FeedbackActivity";
    }

    @Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.feedback);
		
		ButterKnife.inject(this);
	}
	
	/** 返回 */
	@OnClick(R.id.btn_back)
	public void goBack() {
		this.finish();
	}
	
	/** 提交 */
	@OnClick(R.id.publish_feed)
	public void publishFeed() {
		hideMethod(editText);
		if (!TextUtils.isEmpty(editText.getText().toString().trim())) {
			feedBack(editText.getText().toString().trim());
		}else {
			showToast(this.getResources().getString(R.string.publish_feed_content));
		}
	}
	
	/** 设置用户属性 */
	private AsyncHttpResponseHandler backHandler;

	private void feedBack(String content) {

		FeedBackRequest request = new FeedBackRequest();
		request.setContent(content);
		
		APIClient.feedBack(request, new AsyncHttpResponseHandler() {

			@Override
			public void onFinish() {
				backHandler = null;
				hideLoadingView();
			}

			@Override
			protected void onPreExecute() {
				if (backHandler != null)
					backHandler.cancle();
					backHandler = this;
				// mDefView.setStatus(DefaultView.Status.loading);
					showLoadingView(R.string.loading);
			}

			@Override
			public void onSuccess(String content) {
				GetUserInfoResponse response;
				try {
					ResponseUtil.checkResponse(content);
					response = new Gson().fromJson(content,
							GetUserInfoResponse.class);
				} catch (Exception e) {
                    showToast(R.string.data_format_error);
					return;
				}
				if (response != null) {
					if (response.isSuccess()) {
						editText.setText("");
						showToast(response.getMsg());
					} else {
						// mDefView.setStatus(DefaultView.Status.error);
						showToast(response.getMsg());
					}
				}
			}

			@Override
			public void onFailure(Throwable error, String content) {
				// mDefView.setStatus(DefaultView.Status.error);
                showToast(R.string.load_server_failure);
			}
		});
	}

    @Override
    protected void onDestroy() {
        if (backHandler != null) {
            backHandler.cancle();
            backHandler = null;
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
