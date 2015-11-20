package com.cplatform.xhxw.ui.ui.cyancomment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;

import com.cplatform.xhxw.ui.Constants;
import com.cplatform.xhxw.ui.R;
import com.cplatform.xhxw.ui.http.APIClient;
import com.cplatform.xhxw.ui.http.ResponseUtil;
import com.cplatform.xhxw.ui.http.net.CyanCommentRequest;
import com.cplatform.xhxw.ui.http.net.MyCommentResponse;
import com.cplatform.xhxw.ui.ui.base.BaseFragment;
import com.cplatform.xhxw.ui.ui.base.adapter.MyCommentAdapter;
import com.cplatform.xhxw.ui.ui.base.widget.DefaultView;
import com.cplatform.xhxw.ui.ui.base.widget.PullRefreshListView;
import com.cplatform.xhxw.ui.ui.base.widget.PullRefreshListView.PullRefreshListener;
import com.cplatform.xhxw.ui.util.LogUtil;
import com.google.gson.Gson;
import com.loopj.android.http.AsyncHttpResponseHandler;

public class CYanMyCommentFragment extends BaseFragment implements PullRefreshListener, 
		OnItemClickListener{

	private static final int COUNT = 10;
	private int page = 1;

	private PullRefreshListView mListView;
    private DefaultView mDefView;
    
	private MyCommentAdapter mAdapter;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.fragment_my_comment, container, false);
		initViews(rootView);
		return rootView;
	}

	private void initViews(View rootView) {
		mListView = (PullRefreshListView) rootView.findViewById(R.id.saas_mycomment_listview);
		mDefView = (DefaultView) rootView.findViewById(R.id.def_view);
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		initEvents();
	}

    private void initEvents() {
    	mListView.setPullRefreshListener(this);
		mListView.setCanRefresh(true);
		mListView.setCanLoadMore(false);
		mListView.setOnItemClickListener(this);
		
		mAdapter = new MyCommentAdapter(mAct);
		mListView.setAdapter(mAdapter);
        mDefView.setHidenOtherView(mListView);
        mDefView.setStatus(DefaultView.Status.loading);
        mDefView.setListener(new DefaultView.OnTapListener() {
            @Override
            public void onTapAction() {
                getComment(1);
            }
        });
//		getComment(1);
        
        mListView.triggerRefresh(true);
	}

	@Override
	public void onRefresh() {
		getComment(1);
	}

	@Override
	public void onLoadMore() {
		getComment(page + 1);
	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {
		
	}

	/** 获取评论数据 */
	private AsyncHttpResponseHandler mHttpResponseHandler;

	private void getComment(final int p) {

		CyanCommentRequest request = new CyanCommentRequest();
		request.setPg(p);
		request.setOffset(COUNT);
		if(Constants.hasEnterpriseAccountLoggedIn()) {
			request.setSaasRequest(true);
		}
		APIClient.CyanGetMyComment(request, new AsyncHttpResponseHandler() {

            @Override
            public void onFinish() {
                mHttpResponseHandler = null;
                mListView.onRefreshComplete(null);
                mListView.onLoadMoreComplete();
               /* if (!Constants.hasLogin()) {
                    showToast(R.string.please_loging);
                }*/
            }

            @Override
            protected void onPreExecute() {
                if (mHttpResponseHandler != null) {
                    mHttpResponseHandler.cancle();
                }
                mHttpResponseHandler = this;
//                if (mAdapter.getCount() == 0) {
//                    mDefView.setStatus(DefaultView.Status.loading);
//                }
            }

            @Override
            public void onSuccess(String content) {
            	MyCommentResponse response = null;
                try {
                	ResponseUtil.checkResponse(content);
                    response = new Gson().fromJson(content, MyCommentResponse.class);
                } catch (Exception e) {
                    LogUtil.w("", e);
                }
                
                if (response!= null && response.isSuccess()) {

                	page = p;

                    if (response.getData() != null) {
                        if (response.getData().getList() != null && response.getData().getList().size() > 0) {
                            if (p == 1) {
                                mAdapter.clearData();
                            }
                            mAdapter.addAllData(response.getData().getList());
                            mAdapter.notifyDataSetChanged();
                        }

                        if (response.getData().getIsnext() == 1) {
                            mListView.setCanLoadMore(true);
                        } else if (response.getData().getIsnext() == 0) {
                            mListView.setCanLoadMore(false);
                        }
                    }
                } else {
                    showToast(response.getMsg());
//                    if (mAdapter.getCount() == 0) {
//                        mDefView.setStatus(DefaultView.Status.nodata);
//                    }
                }
                mDefView.setStatus(DefaultView.Status.showData);
            }

            @Override
            public void onFailure(Throwable error, String content) {
                if (mAdapter.getCount() == 0) {
                    mDefView.setStatus(DefaultView.Status.error);
                } else {
                    showToast(R.string.load_server_failure);
                }
            }
        });
	}
}
