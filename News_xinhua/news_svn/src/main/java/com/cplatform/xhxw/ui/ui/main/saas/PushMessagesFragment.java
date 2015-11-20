package com.cplatform.xhxw.ui.ui.main.saas;

import java.util.Date;
import java.util.List;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;

import com.cplatform.xhxw.ui.R;
import com.cplatform.xhxw.ui.http.APIClient;
import com.cplatform.xhxw.ui.http.ResponseUtil;
import com.cplatform.xhxw.ui.http.net.GetPushMessageRequest;
import com.cplatform.xhxw.ui.http.net.GetPushMessageResponse;
import com.cplatform.xhxw.ui.http.responseType.GetPushMessageAction;
import com.cplatform.xhxw.ui.model.Message;
import com.cplatform.xhxw.ui.ui.base.BaseFragment;
import com.cplatform.xhxw.ui.ui.base.adapter.MessageAdapter;
import com.cplatform.xhxw.ui.ui.base.widget.PullRefreshListView;
import com.cplatform.xhxw.ui.util.Actions;
import com.cplatform.xhxw.ui.util.ActivityUtil;
import com.cplatform.xhxw.ui.util.DateUtil;
import com.cplatform.xhxw.ui.util.LogUtil;
import com.google.gson.Gson;
import com.loopj.android.http.AsyncHttpResponseHandler;

public class PushMessagesFragment extends BaseFragment implements PullRefreshListView.PullRefreshListener,
		AdapterView.OnItemClickListener{
	
	private static final String TAG = PushMessagesFragment.class.getSimpleName();
	private static final int OFEESET = 20;
	
	private MessageAdapter mAdapter;
	private AsyncHttpResponseHandler mHttpHandler;
    private int mPage = 1;
    private boolean isEnterprsePush = false;
    
	private PullRefreshListView mListView;
	
	private Receiver mBroadCastReceiver;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View enterpriseHomeFragView = inflater.inflate(R.layout.fragment_push_messages, container, false);
		initViews(enterpriseHomeFragView);
		return enterpriseHomeFragView;
	}

	private void initViews(View rootView) {
		mListView = (PullRefreshListView) rootView.findViewById(R.id.message_list);
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		initEvent();
	}
	
	private void initEvent() {
		isEnterprsePush = getArguments().containsKey("isEnterprise") ? 
				getArguments().getBoolean("isEnterprise") : false;
		mListView.setPullRefreshListener(this);
        mListView.setCanLoadMore(false);
        mListView.setCanRefresh(true);
        mListView.setOnItemClickListener(this);
    	mAdapter = new MessageAdapter(getActivity(), isEnterprsePush);
        mListView.setAdapter(mAdapter);
        mListView.triggerRefresh(true);
	}
	
	@Override
	public void onResume() {
		mBroadCastReceiver = new Receiver();
        IntentFilter filter = new IntentFilter();
        filter.addAction(Actions.ACTION_CHANNE_RESELECTED);
        filter.addAction(Actions.ACTION_CHANNEL_RELOAD_CHECK_TIME);
        LocalBroadcastManager.getInstance(mAct).registerReceiver(mBroadCastReceiver, filter);
		super.onResume();
	}

	private void executeNet(final int pn) {
        GetPushMessageAction action;
        String published;
        int dataSize = mAdapter.getCount();
        
        if (pn <= 1) {
            action = GetPushMessageAction.up;
            if(dataSize > 0) {
            	published = mAdapter.getItem(0).getPublished();
            } else {
            	if(isEnterprsePush) {
                	action = GetPushMessageAction.down;
                }
            	published = String.valueOf(DateUtil.getTime() / 1000);
            }
        } else {
            action = GetPushMessageAction.down;
            if (dataSize > 0) {
                published = mAdapter.getItem(dataSize - 1).getPublished();
            } else {
                published = String.valueOf(DateUtil.getTime() / 1000);
            }
        }
        
        GetPushMessageRequest gmr = new GetPushMessageRequest(OFEESET,pn, action, published);
    	if(isEnterprsePush) {
    		gmr.setSaasRequest(true);
    	}
        APIClient.getPushMessage(gmr, new AsyncHttpResponseHandler() {
            @Override
            public void onFinish() {
                mHttpHandler = null;
                if (pn == 1) {
                    if (isSurcess) {
                        mListView.onRefreshComplete(new Date());
                    } else {
                        mListView.onRefreshComplete(null);
                    }
                } else {
                    mListView.onLoadMoreComplete();
                }

            }

            @Override
            protected void onPreExecute() {
                if (mHttpHandler != null) {
                    mHttpHandler.cancle();
                }
                mHttpHandler = this;
            }

            @Override
            public void onSuccess(int statusCode, String content) {
                GetPushMessageResponse response;
                try {
                	ResponseUtil.checkResponse(content);
                    response = new Gson().fromJson(content, GetPushMessageResponse.class);
                } catch (Exception e) {
                    showToast(R.string.data_format_error);
                    LogUtil.w(TAG, e);
                    return;
                }
                if (response.isSuccess()) {
                    List<Message> datas = response.getDatas(getActivity());
                    if(pn <= 1) {
                    	if(mAdapter.getCount() == 0) {
                    		mListView.setCanLoadMore(true);
                    	}
                    	if(!isEnterprsePush) {
                    		mAdapter.clearData();
                    	}
                    	mAdapter.addNewestData(datas);
                    } else {
                    	mAdapter.addAllData(datas);
                    	boolean canMore = (datas.size() >= OFEESET);
                        mListView.setCanLoadMore(canMore);
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

    @Override
	public void onPause() {
    	if (mBroadCastReceiver != null) {
            LocalBroadcastManager.getInstance(mAct).unregisterReceiver(mBroadCastReceiver);
            mBroadCastReceiver = null;
        }
		super.onPause();
	}

	@Override
	public void onDestroy() {
        if (mHttpHandler != null) {
            mHttpHandler.cancle();
            mHttpHandler = null;
        }
        super.onDestroy();
    }

    @Override
    public void onRefresh() {
        executeNet(1);
    }

    @Override
    public void onLoadMore() {
        executeNet(mPage + 1);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Message item = mAdapter.getItem(position - 1);
        int newsType = item.getNewstype() == null ? 0 : Integer.valueOf(item.getNewstype());
        ActivityUtil.goToNewsDetailPageByNewstype(getActivity(), newsType, 
        		item.getNewsid(), null, isEnterprsePush, item.getTitle());
    }
    
    private class Receiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (!isDestroyView && Actions.ACTION_CHANNEL_RELOAD_CHECK_TIME.equals(action)
                    && getArguments().getBoolean("isEnterprise")
                    && isReloadNetData()) {
            	mListView.triggerRefresh(true);
            }

        }
    }
    
    /**
     * 判断是否需要重新加载数据<\br>
     * 如果三分钟内刷新过就不在刷新
     */
    private boolean isReloadNetData() {
        return (DateUtil.getTime() - mListView.getOldRefreshTime()) > 180000;
    }
}
