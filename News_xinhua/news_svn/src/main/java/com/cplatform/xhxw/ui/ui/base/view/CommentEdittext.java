package com.cplatform.xhxw.ui.ui.base.view;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

import com.google.gson.Gson;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.cplatform.xhxw.ui.R;
import com.cplatform.xhxw.ui.http.APIClient;
import com.cplatform.xhxw.ui.http.ResponseUtil;
import com.cplatform.xhxw.ui.http.net.SendCommentOrReplyPersonRequest;
import com.cplatform.xhxw.ui.http.net.SendCommentOrReplyPersonResponse;
import com.cplatform.xhxw.ui.model.SendCommentOrReplyPerson;

import android.content.Context;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Toast;

public class CommentEdittext extends RelativeLayout{

	private Context context;
	private int newsId;
	
	@InjectView(R.id.comment_edit)
	EditText editText;
	@InjectView(R.id.comment_send)
	Button button;
	
	public CommentEdittext(Context context) {
		super(context);
		init(context);
	}
	
	public CommentEdittext(Context context, AttributeSet attrs) {
		super(context, attrs);
		init(context);
	}
	
	public CommentEdittext(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init(context);
	}
	
	private void init(Context context) {
		
		this.context = context;
		
		LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		inflater.inflate(R.layout.layout_comment_edittext, this);
		
		ButterKnife.inject(this);
	}
	
	public void setId(int newsId) {
		this.newsId = newsId;
	}
	
	public EditText getEdit() {
		return editText;
	}
	
	/** 提交 */
	@OnClick(R.id.comment_send)
	public void send() {
		if (TextUtils.isEmpty(editText.getText().toString())) {
			Toast.makeText(context, context.getResources().getString(R.string.comment_content_demand), Toast.LENGTH_SHORT).show();
			return;
		}
		
		SendCommentOrReplyPersonRequest request = new SendCommentOrReplyPersonRequest();
		request.setNewsId(newsId);
		request.setContent(editText.getText().toString());
		
		sendDate(request);
	}
	
	/** 提交评论或者回复 */
	private AsyncHttpResponseHandler mHttpResponseHandler;
	
	public void sendDate(SendCommentOrReplyPersonRequest request) {
		
		APIClient.sendCommentOrReplyPerson(request, new AsyncHttpResponseHandler() {

			@Override
			public void onFinish() {
				mHttpResponseHandler = null;
//				context.hideLoadingView();
			}

			@Override
			protected void onPreExecute() {
				if (mHttpResponseHandler != null)
					mHttpResponseHandler.cancle();
				mHttpResponseHandler = this;
//				context.showLoadingView(R.string.comment_content_upping);
			}

			@Override
			public void onSuccess(String content) {
				SendCommentOrReplyPersonResponse response;
				try {
					ResponseUtil.checkResponse(content);
					response = new Gson().fromJson(content,
							SendCommentOrReplyPersonResponse.class);
				} catch (Exception e) {
					// mDefView.setStatus(DefaultView.Status.error);
					return;
				}
				if (response.isSuccess()) {
					
				} else {
				}
			}

			@Override
			public void onFailure(Throwable error, String content) {
				// mDefView.setStatus(DefaultView.Status.error);
			}
		});
	}
}
