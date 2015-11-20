package com.cplatform.xhxw.ui.ui.main.cms;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.cplatform.xhxw.ui.Constants;
import com.cplatform.xhxw.ui.R;
import com.cplatform.xhxw.ui.http.APIClient;
import com.cplatform.xhxw.ui.http.ResponseUtil;
import com.cplatform.xhxw.ui.http.net.CommentReplyMeRequest;
import com.cplatform.xhxw.ui.http.net.CommentReplyMeResponse;
import com.cplatform.xhxw.ui.model.CommentReplyMe;
import com.cplatform.xhxw.ui.ui.base.BaseFragment;
import com.cplatform.xhxw.ui.ui.base.view.CommentReplyMeListItem;
import com.cplatform.xhxw.ui.ui.base.view.CommentReplyMeListItem.onShowMoreReplyClickListener;
import com.cplatform.xhxw.ui.ui.base.widget.PullRefreshListView;
import com.cplatform.xhxw.ui.util.LogUtil;
import com.google.gson.Gson;
import com.loopj.android.http.AsyncHttpResponseHandler;

public class CommentReplyMeFragment extends BaseFragment implements
		PullRefreshListView.PullRefreshListener{
	private PullRefreshListView mListView;
	private List<CommentReplyMe> mData = new ArrayList<CommentReplyMe>();
	private CommentReplyAdapter mAdapter;
	private int pn = 0; //从服务器取数据时的页码
	
	private OnShowMoreBtnClick onShowMoreBtnClickLis;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View enterpriseHomeFragView = inflater.inflate(R.layout.fragment_comment_reply_me, container, false);
		initViews(enterpriseHomeFragView);
		return enterpriseHomeFragView;
	}

	private void initViews(View rootView) {
		mListView = (PullRefreshListView) rootView.findViewById(R.id.comment_reply_listview);
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		initEvents();
	}
	
	private void initEvents() {
		mListView.setPullRefreshListener(this);
        mListView.setCanLoadMore(true);
        mListView.setCanRefresh(false);
        mAdapter = new CommentReplyAdapter(mAct, false);
        mListView.setAdapter(mAdapter);
        mListView.triggerRefresh(true);
	}
	
	onShowMoreReplyClickListener mShowMoreReplyClick = new onShowMoreReplyClickListener() {
		
		@Override
		public void onMoreReplyClick(CommentReplyMe data) {
			if(onShowMoreBtnClickLis != null) {
				onShowMoreBtnClickLis.onShowMoreClick(data);
			}
		}
	};

	class CommentReplyAdapter extends android.widget.BaseAdapter {
		private boolean isEnterprise = false;
		private Context mCon;

		public CommentReplyAdapter(Context context, boolean isEnterprise) {
			this.mCon = context;
			this.isEnterprise = isEnterprise;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			CommentReplyMe item = (CommentReplyMe) getItem(position);
			if (convertView == null) {
				convertView = new CommentReplyMeListItem(mCon);
			}
			CommentReplyMeListItem view = (CommentReplyMeListItem) convertView;
			view.setOnShowMoreReplyClickLis(mShowMoreReplyClick);
			view.setmShowSource(item);
			return convertView;
		}

		@Override
		public int getCount() {
			return mData.size();
		}

		@Override
		public Object getItem(int arg0) {
			return mData.get(arg0);
		}

		@Override
		public long getItemId(int arg0) {
			return arg0;
		}

	}
	
	private void loadComments(final boolean isLoadMore) {
		CommentReplyMeRequest request = new CommentReplyMeRequest();
		request.setLastid("0");
		if(mData.size() > 0) {
			CommentReplyMe lastData = (CommentReplyMe) mAdapter.getItem(mAdapter.getCount() - 1);
			request.setLastid(lastData.getLastid());
		}
		request.setType(isLoadMore ? 0 : 1);
		if(mData.size() <=0) {
			request.setType(1);
		}
		request.setOffset(20);
		request.setPn(pn);
		if(Constants.hasEnterpriseAccountLoggedIn()) {
			request.setSaasRequest(true);
		}
		
		APIClient.getCommentReplyMe(request, new AsyncHttpResponseHandler(){

			@Override
			public void onFinish() {
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
			public void onSuccess(int statusCode, String content) {
				CommentReplyMeResponse response = null;
				try {
					ResponseUtil.checkResponse(content);
                    response = new Gson().fromJson(content, CommentReplyMeResponse.class);
				} catch (Exception e) {
					showToast(R.string.data_format_error);
                    LogUtil.w("comment reply", e);
				}
				
				if(response == null) return;
				
				if(response.isSuccess()) {
					List<CommentReplyMe> resdata = response.getData();
					if(pn == 1) {
                    	if(mAdapter.getCount() == 0) {
                    		mListView.setCanLoadMore(true);
                    	}
                    	mData.clear();
                    	mData.addAll(resdata);
                    	mListView.setAdapter(mAdapter);
                    } else {
                    	mData.addAll(resdata);
                    	boolean canMore = (resdata.size() >= 20);
                        mListView.setCanLoadMore(canMore);
                    }
					mAdapter.notifyDataSetChanged();
				} else {
					pn -= 1;
					showToast(response.getMsg());
				}
			}

			@Override
			public void onFailure(Throwable error, String content) {
				pn -= 1;
				showToast(R.string.load_server_failure);
			}
			
		});
	}

	@Override
	public void onRefresh() {
		pn = 1;
		loadComments(false);
	}

	@Override
	public void onLoadMore() {
		pn += 1;
		loadComments(true);
	}
	
	public OnShowMoreBtnClick getOnShowMoreBtnClickLis() {
		return onShowMoreBtnClickLis;
	}

	public void setOnShowMoreBtnClickLis(OnShowMoreBtnClick onShowMoreBtnClickLis) {
		this.onShowMoreBtnClickLis = onShowMoreBtnClickLis;
	}

	public interface OnShowMoreBtnClick{
		void onShowMoreClick(CommentReplyMe data);
	}
}
