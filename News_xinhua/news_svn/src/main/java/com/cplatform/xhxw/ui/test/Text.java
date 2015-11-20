package com.cplatform.xhxw.ui.test;

import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;
import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

import com.google.gson.Gson;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.cplatform.xhxw.ui.R;
import com.cplatform.xhxw.ui.http.APIClient;
import com.cplatform.xhxw.ui.http.ResponseUtil;
import com.cplatform.xhxw.ui.http.net.CommentPraiseRequest;
import com.cplatform.xhxw.ui.http.net.CommentPraiseResponse;
import com.cplatform.xhxw.ui.http.net.FeedBackRequest;
import com.cplatform.xhxw.ui.http.net.GetUserInfoRequest;
import com.cplatform.xhxw.ui.http.net.GetUserInfoResponse;
import com.cplatform.xhxw.ui.http.net.SetUserInfoRequest;
import com.cplatform.xhxw.ui.ui.base.BaseActivity;
import com.cplatform.xhxw.ui.util.UpdateVersion;

public class Text extends BaseActivity{

/**********************用户赞评论与取消赞***********************************/
	/**
	 * 用户赞评论与取消赞
	 * 
	 * */
	@InjectView(R.id.button1)
	Button button;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.text);
		
		ButterKnife.inject(this);
		
		getInfo();
	}

    @Override
    protected String getScreenName() {
        return "Text";
    }

    /** 用户赞评论与取消赞*/
	@OnClick(R.id.button1)
	public void button1() {
		setPraise(1);
	}
	
	/** 获取评论数据 */
	private AsyncHttpResponseHandler mHttpResponseHandler;

	private void setPraise(int id) {

		CommentPraiseRequest request = new CommentPraiseRequest();
		request.setCommentid(id);
		
		APIClient.setPraise(request, new AsyncHttpResponseHandler() {

			@Override
			public void onFinish() {
				mHttpResponseHandler = null;
				hideLoadingView();
			}

			@Override
			protected void onPreExecute() {
				if (mHttpResponseHandler != null)
					mHttpResponseHandler.cancle();
				mHttpResponseHandler = this;
				// mDefView.setStatus(DefaultView.Status.loading);
					showLoadingView(R.string.loading);
			}

			@Override
			public void onSuccess(String content) {
				CommentPraiseResponse response;
				try {
					ResponseUtil.checkResponse(content);
					response = new Gson().fromJson(content,
							CommentPraiseResponse.class);
				} catch (Exception e) {
                    showToast(R.string.data_format_error);
					return;
				}
				if (response != null) {
					if (response.isSuccess()) {
						if ("1".equals(response.getData().getActionType())) {
							Toast.makeText(Text.this, "成功取消赞", Toast.LENGTH_SHORT).show();
						}else if ("2".equals(response.getData().getActionType())) {
							Toast.makeText(Text.this, "赞成功", Toast.LENGTH_SHORT).show();
						}
					} else {
						// mDefView.setStatus(DefaultView.Status.error);
					}
				}
				
			}

			@Override
			public void onFailure(Throwable error, String content) {
				// mDefView.setStatus(DefaultView.Status.error);
			}
		});
	}
	
	
	
	/*************************获取用户属性********************************************/
	
	@OnClick(R.id.get_user_info)
	public void getUserInfo() {
//		getUserInfo("10b76898794711e39bf8001a6467c5a0");//10b76898794711e39bf8001a6467c5a0
		getUserInfo("57d3b34a802811e39bf8001a6467c5a0");//57d3b34a802811e39bf8001a6467c5a0
	}
	
	/**获取用户属性 */
	private AsyncHttpResponseHandler mHandler;

	private void getUserInfo(String userId) {

		GetUserInfoRequest request = new GetUserInfoRequest();
		request.setUid(userId);
		
		APIClient.getUserInfo(request, new AsyncHttpResponseHandler() {

			@Override
			public void onFinish() {
				mHandler = null;
				hideLoadingView();
			}

			@Override
			protected void onPreExecute() {
				if (mHandler != null)
					mHandler.cancle();
					mHandler = this;
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
						if (response.getDate() != null) {
							
						}
					} else {
						// mDefView.setStatus(DefaultView.Status.error);
						showToast(response.getMsg());
					}
				}
				
			}

			@Override
			public void onFailure(Throwable error, String content) {
				// mDefView.setStatus(DefaultView.Status.error);
			}
		});
	}
	
	
	
	/*************************设置用户属性********************************************/
	
	@OnClick(R.id.set_user_info)
	public void setUserInfo() {
		SetUserInfoRequest request = new SetUserInfoRequest();
		request.setLogo(null);//图像文件
		request.setNickname("我叫孙晓广");
		setUserInfo(request);
	}
	
	/** 设置用户属性 */
	private AsyncHttpResponseHandler handler;

	private void setUserInfo(SetUserInfoRequest request) {

		APIClient.setUserInfo(request, new AsyncHttpResponseHandler() {

			@Override
			public void onFinish() {
				mHandler = null;
				hideLoadingView();
			}

			@Override
			protected void onPreExecute() {
				if (mHandler != null)
					mHandler.cancle();
					mHandler = this;
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
			}
		});
	}
	
	
	
/*************************用户反馈意见********************************************/
	@OnClick(R.id.user_feedback)
	public void feedBack() {
		feedBack("项目很好，员工也好");
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
			}
		});
	};
	
	
	/**
	 * 判断是否需要更新项目
	 * */
	private void getInfo() {
		UpdateVersion updateVersion = UpdateVersion.getInstance(this);
		updateVersion.isUpdate();
	}
}
