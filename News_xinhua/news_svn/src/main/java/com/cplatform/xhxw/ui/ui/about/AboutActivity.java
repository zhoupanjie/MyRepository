package com.cplatform.xhxw.ui.ui.about;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

import com.google.gson.Gson;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.cplatform.xhxw.ui.R;
import com.cplatform.xhxw.ui.http.APIClient;
import com.cplatform.xhxw.ui.http.ResponseUtil;
import com.cplatform.xhxw.ui.http.net.GetVersionInfoRequest;
import com.cplatform.xhxw.ui.http.net.GetVersionInfoResponse;
import com.cplatform.xhxw.ui.ui.base.BaseActivity;
import com.cplatform.xhxw.ui.util.Engine;
import com.cplatform.xhxw.ui.util.InstallAppTask;
import com.cplatform.xhxw.ui.util.InstallAppTask.OnLoadListener;

public class AboutActivity extends BaseActivity implements OnLoadListener{

	private String url;
	private InstallAppTask task;
	
    @InjectView(R.id.about_now_layout)
    LinearLayout nowLayout;
    
    @InjectView(R.id.about_now_version1)
    TextView nowVersion1;
    
    @InjectView(R.id.about_new_layout)
    LinearLayout newLayout;
    
    @InjectView(R.id.about_now_version2)
    TextView nowVersion2;
    
    @InjectView(R.id.about_new_version)
    TextView newVersion;
    
    @InjectView(R.id.about_update)
    Button updateBtn;
	
	public static Intent getIntent(Context context) {
        Intent intent = new Intent(context, AboutActivity.class);
        return intent;
    }

    @Override
    protected String getScreenName() {
        return "AboutActivity";
    }

    @Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.about);
		
		ButterKnife.inject(this);
		
		getVersionInfo();
	}
	
	/**
	 * 获取最新版本进行安装
	 * 
	 * */
	@OnClick(R.id.about_update)
	public void update() {
		/*new InstallAppTask(
				this,
				getString(R.string.version_update_string),
				url)
				.execute();*/
		
		if (task == null) {
			task = new InstallAppTask(AboutActivity.this,
					getString(R.string.version_update_string),
					url);
		}
		task.setOnLoadListener(AboutActivity.this);
		task.execute();
	}
	
	/** 获取版本属性 */
	private AsyncHttpResponseHandler getHandler;

	private void getVersionInfo() {

		GetVersionInfoRequest request = new GetVersionInfoRequest();
		request.setDevice_type("android_phone");
		request.setVersion_no(Engine.getVersionCode(this));
		
		APIClient.getVersionInfo(request, new AsyncHttpResponseHandler() {

			@Override
			public void onFinish() {
				getHandler = null;
				hideLoadingView();
			}

			@Override
			protected void onPreExecute() {
				if (getHandler != null)
					getHandler.cancle();
					getHandler = this;
				// mDefView.setStatus(DefaultView.Status.loading);
					showLoadingView(R.string.loading);
			}

			@Override
			public void onSuccess(String content) {
				GetVersionInfoResponse response;
				try {
					ResponseUtil.checkResponse(content);
					response = new Gson().fromJson(content,
							GetVersionInfoResponse.class);
				} catch (Exception e) {
                    showToast(R.string.data_format_error);
					return;
				}
				if (response != null) {
					if (response.isSuccess()) {
						if (response.getData() != null) {
							int a = Engine.getVersionCode(AboutActivity.this);
							int b = Integer.valueOf(response.getData().getVersion_no());
							int c = response.getData().getVersion_no();
							if (Engine.getVersionCode(AboutActivity.this) < response.getData().getVersion_no()) {
								nowLayout.setVisibility(View.GONE);
								newLayout.setVisibility(View.VISIBLE);
								
								nowVersion2.setText(String.format(AboutActivity.this.getResources().getString(R.string.about_now_versions),
						        		Engine.getVersionName(AboutActivity.this)));
								newVersion.setText(String.format(AboutActivity.this.getResources().getString(R.string.about_new_versions),
						        		response.getData().getVersion()));
								
								updateBtn.setVisibility(View.VISIBLE);
								
								if (response.getData().getDown_url() != null) {
									url = response.getData().getDown_url();
								}
							}else {
//								nowLayout.setVisibility(View.VISIBLE);
								newLayout.setVisibility(View.GONE);
								
								nowVersion1.setText(String.format(AboutActivity.this.getResources().getString(R.string.about_now_versions),
						        		Engine.getVersionName(AboutActivity.this)));
								
								updateBtn.setVisibility(View.GONE);
							}
						}
					} else {
						// mDefView.setStatus(DefaultView.Status.error);
						showToast(response.getMsg());
					}
				}
			}

			@Override
			public void onFailure(Throwable error, String content) {
				getHandler = null;
				hideLoadingView();
//				nowLayout.setVisibility(View.VISIBLE);
				newLayout.setVisibility(View.GONE);
				
				nowVersion1.setText(String.format(AboutActivity.this.getResources().getString(R.string.about_now_versions),
		        		Engine.getVersionName(AboutActivity.this)));
				
				updateBtn.setVisibility(View.GONE);
				// mDefView.setStatus(DefaultView.Status.error);
			}
		});
	}
	
	
	/**返回*/
	@OnClick(R.id.btn_back)
	public void goBack() {
		this.finish();
	}
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
		if (getHandler != null) {
			getHandler.cancle();
			getHandler = null;
		}
	}

	@Override
	public void onFail() {
		task = null;
		showToast("更新失败");
	}

	@Override
	public void onSecuses() {
		
	}
}
