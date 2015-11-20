package com.cplatform.xhxw.ui.ui.main.saas.addressBook;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;

import com.cplatform.xhxw.ui.Constants;
import com.cplatform.xhxw.ui.R;
import com.cplatform.xhxw.ui.http.APIClient;
import com.cplatform.xhxw.ui.http.ResponseUtil;
import com.cplatform.xhxw.ui.http.net.AttentionRequest;
import com.cplatform.xhxw.ui.http.net.AttentionResponse;
import com.cplatform.xhxw.ui.ui.base.BaseActivity;
import com.cplatform.xhxw.ui.ui.base.widget.PullRefreshListView;
import com.cplatform.xhxw.ui.ui.base.widget.PullRefreshListView.PullRefreshListener;
import com.cplatform.xhxw.ui.ui.main.saas.addressBook.SearchResultAdapter.OnAttentionFriendListener;
import com.cplatform.xhxw.ui.util.EditUtil;
import com.cplatform.xhxw.ui.util.ListUtil;
import com.cplatform.xhxw.ui.util.LogUtil;
import com.google.gson.Gson;
import com.loopj.android.http.AsyncHttpResponseHandler;

/**
 * 搜索好友---》搜索结果
 */
public class SearchResultActivity extends BaseActivity implements PullRefreshListener, OnAttentionFriendListener, OnItemClickListener{

	private static final String TAG = SearchResultActivity.class.getSimpleName();
	private static final int VERIFY = 1;
	private static final int FRIENDSINFO = 2;
	
	private PullRefreshListView mListView;
	
	private SearchResultAdapter mAdapter;
	
	private int page = 1;
	private String key;
	private int position;
	private AsyncHttpResponseHandler mHttpResponseHandler;
	
	public static Intent newIntent(Context context, String key) {
		Intent intent = new Intent(context, SearchResultActivity.class);
		intent.putExtra("key", key);
		return intent;
	}
	
	@Override
	protected String getScreenName() {
		return TAG;
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.activity_search_result);
	
		init();
	}

	private void init() {
		initActionBar();
		
		mListView = (PullRefreshListView) findViewById(R.id.search_result_listview);
		
		mListView.setPullRefreshListener(this);
		mListView.setOnItemClickListener(this);
		mAdapter = new SearchResultAdapter(this);
		mAdapter.setOnAddFriendListener(this);
		mListView.setAdapter(mAdapter);
		
		mListView.setCanRefresh(true);
		mListView.setCanLoadMore(false);
		
		key = getIntent().getStringExtra("key");
		
		mListView.triggerRefresh(true);
	}
	
	@Override
	public void onRefresh() {
		getSearchResult(key, 1);
	}

	@Override
	public void onLoadMore() {
		getSearchResult(key, page + 1);
	}

	@Override
	public void attentionListener(int position) {
		this.position = position;
		startActivityForResult(FriendsVerifyActivity.newIntent(this, mAdapter.getData().get(position).getUserid()), VERIFY);
		
//		attention(position);
	}
	

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {
		if ("3".equals(mAdapter.getData().get(position - 1).getIsfriend())) {//已是好友
			startActivity(FriendInfoActivity.newIntent(this, mAdapter.getData().get(position - 1).getUserid(), true));
		}else {
			startActivityForResult(FriendInfoActivity.newIntent(this, mAdapter.getData().get(position - 1).getUserid(),false), FRIENDSINFO);
		}
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		
		if (resultCode != Activity.RESULT_OK) {
			return;
		}
		
		switch (requestCode) {
		case VERIFY:
			mAdapter.getData().get(position).setIsfriend("0");
			mAdapter.notifyDataSetChanged();
			break;
		case FRIENDSINFO:
			mAdapter.getData().get(position).setIsfriend("0");
			mAdapter.notifyDataSetChanged();
			break;
		default:
			break;
		}
	}
	
	/** 搜索新的朋友列表 */
	private void getSearchResult(final String key, final int p) {

		final SearchResultRequest request = new SearchResultRequest();
		request.setQ(key);
		request.setDevRequest(true);
		
		APIClient.getSearchResult(request, new AsyncHttpResponseHandler() {

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
                LogUtil.d(content);
				SearchResultResponse response;
				try {
					response = new Gson().fromJson(content,
							SearchResultResponse.class);
                    ResponseUtil.checkObjResponse(response);
				} catch (Exception e) {
					showToast(R.string.data_format_error);
					LogUtil.w(TAG, e);
					return;
				}
				if (response.isSuccess()) {
					page = p;
					if (p == 1 && mAdapter.getCount() > 0) {
						mAdapter.clearData();
                        mAdapter.notifyDataSetChanged();
					}

                    if (!ListUtil.isEmpty(response.getData())) {
                        if (EditUtil.isMobileNum(key)) {
                            if ("2".equals(response.getData().get(0).getIsfriend())) {//好友
                                startActivity(FriendInfoActivity.newIntent(SearchResultActivity.this, response.getData().get(0).getUserid(), true));
                            }else {
                                startActivityForResult(FriendInfoActivity.newIntent(SearchResultActivity.this, response.getData().get(0).getUserid(), true), FRIENDSINFO);
                            }

                            finish();
                        }else {
                            mAdapter.addAllData(response.getData());
                            mAdapter.notifyDataSetChanged();
                        }
                    } else {
                        showToast(R.string.no_search_results);
                    }
				} else {
					showToast(response.getMsg());
				}
			}

			@Override
			public void onFailure(Throwable error, String content) {
				if (mAdapter.getCount() == 0) {
//					mDefaultView.setStatus(DefaultView.Status.error);
				} else {
					showToast(R.string.load_server_failure);
				}
			}
		});
	}
	
	/** 关注 */
	private void attention(final int position) {

		AttentionRequest request = new AttentionRequest();
		request.setUserid(Constants.getUid());
		request.setDevRequest(true);
		
		APIClient.attention(request, new AsyncHttpResponseHandler() {

			@Override
			public void onFinish() {
				mHttpResponseHandler = null;
				mListView.onRefreshComplete(null);
				mListView.onLoadMoreComplete();
				/*
				 * if (!Constants.hasLogin()) {
				 * showToast(R.string.please_loging); }
				 */
			}

			@Override
			protected void onPreExecute() {
				if (mHttpResponseHandler != null) {
					mHttpResponseHandler.cancle();
				}
				mHttpResponseHandler = this;
				// if (mAdapter.getCount() == 0) {
				// mDefView.setStatus(DefaultView.Status.loading);
				// }
			}

			@Override
			public void onSuccess(String content) {
				AttentionResponse response;
				try {
					ResponseUtil.checkResponse(content);
					response = new Gson().fromJson(content,
							AttentionResponse.class);
				} catch (Exception e) {
					showToast(R.string.data_format_error);
					LogUtil.w(TAG, e);
					return;
				}
				if (response.isSuccess()) {
					mAdapter.getData().get(position).setIsfriend("1");
					mAdapter.notifyDataSetChanged();
				} else {
					showToast(response.getMsg());
				}
			}

			@Override
			public void onFailure(Throwable error, String content) {
				if (mAdapter.getCount() == 0) {
					
				} else {
					showToast(R.string.load_server_failure);
				}
			}
		});
	}

}
