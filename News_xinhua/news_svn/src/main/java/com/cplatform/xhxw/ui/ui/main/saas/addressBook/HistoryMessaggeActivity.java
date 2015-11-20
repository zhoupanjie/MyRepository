package com.cplatform.xhxw.ui.ui.main.saas.addressBook;

import java.util.ArrayList;
import java.util.List;

import com.cplatform.xhxw.ui.R;
import com.cplatform.xhxw.ui.http.APIClient;
import com.cplatform.xhxw.ui.http.ResponseUtil;
import com.cplatform.xhxw.ui.http.httputils.HttpHanderUtil;
import com.cplatform.xhxw.ui.http.net.ClearHistoryMessageRequest;
import com.cplatform.xhxw.ui.http.net.ClearHistoryMessageResponse;
import com.cplatform.xhxw.ui.ui.base.BaseActivity;
import com.cplatform.xhxw.ui.ui.base.widget.PullRefreshListView;
import com.cplatform.xhxw.ui.ui.base.widget.PullRefreshListView.PullRefreshListener;
import com.cplatform.xhxw.ui.util.LogUtil;
import com.google.gson.Gson;
import com.loopj.android.http.AsyncHttpResponseHandler;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.AdapterView.OnItemClickListener;

public class HistoryMessaggeActivity extends BaseActivity implements
		PullRefreshListener, OnItemClickListener {

	private static final String TAG = HistoryMessaggeActivity.class
			.getSimpleName();
	private static final String REFESH = "0";
	private static final String LOADMORE = "1";
	private static final String COUNT = "10";

	private Button rightBtn;
	private PullRefreshListView mListView;
	private HistoryMessaggeAdapter mAdapter;
	private AsyncHttpResponseHandler mHttpResponseHandler;

	public static Intent newIntent(Context context) {
		Intent intent = new Intent(context, HistoryMessaggeActivity.class);
		return intent;
	}

	@Override
	protected String getScreenName() {
		return TAG;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_history_message);

		init();

		getHistoryMessage(REFESH, "", "");
	}

	private void init() {
		initActionBar();

		rightBtn = (Button) findViewById(R.id.personal_mood_message);
		mListView = (PullRefreshListView) findViewById(R.id.mine_mood_listView);
		mAdapter = new HistoryMessaggeAdapter(this);
		mListView.setAdapter(mAdapter);
		mListView.setCanRefresh(true);
		mListView.setCanLoadMore(false);
		mListView.setOnItemClickListener(this);
		mListView.setPullRefreshListener(this);

		getHistoryMessage(REFESH, "", COUNT);

		rightBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View view) {
				AlertDialog.Builder builder = new AlertDialog.Builder(HistoryMessaggeActivity.this);
				builder.setTitle("提醒！");
				builder.setMessage("确定要清空消息记录吗");
				builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
					
					@Override
					public void onClick(DialogInterface arg0, int arg1) {
						clearHistoryMessage();
					}
				});
				builder.setNegativeButton("取消", null);
				
				builder.show();
			}
		});
	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {

	}

	@Override
	public void onRefresh() {
		getHistoryMessage(REFESH, mAdapter.getCount() > 0 ? mAdapter.getItem(0)
				.getCtime() : "0", "0");
	}

	@Override
	public void onLoadMore() {
		getHistoryMessage(
				LOADMORE,
				mAdapter.getCount() > 0 ? mAdapter.getItem(
						mAdapter.getCount() - 1).getCtime() : "0", "0");
	}

	/** 获取历史消息 */
	private void getHistoryMessage(final String typeId, String ctime,
			String offset) {

		HistoryMessageRequest request = new HistoryMessageRequest();
		request.setTypeid(typeId);
		request.setCtime(ctime);
		request.setOffset(offset);
		request.setDevRequest(true);

		APIClient.getHistoryMessage(request, new AsyncHttpResponseHandler() {

			@Override
			public void onFinish() {
				mHttpResponseHandler = null;
				mListView.onRefreshComplete(null);
				mListView.onLoadMoreComplete();

				hideLoadingView();
			}

			@Override
			protected void onPreExecute() {
				if (mHttpResponseHandler != null) {
					mHttpResponseHandler.cancle();
				}
				mHttpResponseHandler = this;

				showLoadingView();
			}

			@Override
			public void onSuccess(String content) {
				HistoryMessageResponse response;
				try {
					ResponseUtil.checkResponse(content);
					response = new Gson().fromJson(content,
							HistoryMessageResponse.class);
				} catch (Exception e) {
					showToast(R.string.data_format_error);
					LogUtil.w(TAG, e);
					return;
				}

				if (response.isSuccess()) { 
					
					/**如果是刷新， 清空适配器，重新加载数据*/
					if (REFESH.equals(typeId)) {
						mAdapter.clearData();
					}
					/**将数据拆分重新进行组装---转换成“新鲜事集合”*/
					List<PersonalMoods> list = new ArrayList<PersonalMoods>();
					if (response != null) {
						if (response.getData() != null && response.getData().size() > 0) {
							
							/**联网正常， 并且能获取数据，才将其设为true*/
							mListView.setCanLoadMore(true);
							
							mAdapter.addAllData(response.getData());
							mAdapter.notifyDataSetChanged();
						}else {
							/**联网正常， 没有获取到数据，说明服务器已经没有数据了，设为false*/
							mListView.setCanLoadMore(false);
						}
					}
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

	/** 清除历史消息 */
	private void clearHistoryMessage() {

		ClearHistoryMessageRequest request = new ClearHistoryMessageRequest();
		request.setDevRequest(true);

		APIClient.clearHistoryMessage(request, new AsyncHttpResponseHandler() {

			@Override
			public void onFinish() {
				mHttpResponseHandler = null;
				mListView.onRefreshComplete(null);
				mListView.onLoadMoreComplete();

				hideLoadingView();
			}

			@Override
			protected void onPreExecute() {
				if (mHttpResponseHandler != null) {
					mHttpResponseHandler.cancle();
				}
				mHttpResponseHandler = this;

				showLoadingView();
			}

			@Override
			public void onSuccess(String content) {
				ClearHistoryMessageResponse response;
				try {
					ResponseUtil.checkResponse(content);
					response = new Gson().fromJson(content,
							ClearHistoryMessageResponse.class);
				} catch (Exception e) {
					showToast(R.string.data_format_error);
					LogUtil.w(TAG, e);
					return;
				}

				if (response.isSuccess()) {
					mAdapter.clearData();
					mAdapter.notifyDataSetChanged();
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
		super.onDestroy();
		HttpHanderUtil.cancel(mHttpResponseHandler);
	}

}
