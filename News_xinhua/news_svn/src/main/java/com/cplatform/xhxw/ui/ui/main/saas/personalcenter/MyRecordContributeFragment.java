package com.cplatform.xhxw.ui.ui.main.saas.personalcenter;

import java.util.ArrayList;
import java.util.Date;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.cplatform.xhxw.ui.R;
import com.cplatform.xhxw.ui.http.APIClient;
import com.cplatform.xhxw.ui.http.HttpClientConfig;
import com.cplatform.xhxw.ui.http.ResponseUtil;
import com.cplatform.xhxw.ui.http.net.saas.SaasGetContributeListRequest;
import com.cplatform.xhxw.ui.http.net.saas.SaasGetContributeListResponse;
import com.cplatform.xhxw.ui.http.net.saas.SaasGetContributeListResponse.ContributeStatus;
import com.cplatform.xhxw.ui.ui.base.BaseFragment;
import com.cplatform.xhxw.ui.ui.base.widget.PullRefreshListView;
import com.cplatform.xhxw.ui.ui.base.widget.PullRefreshListView.PullRefreshListener;
import com.cplatform.xhxw.ui.ui.web.WebViewActivity;
import com.cplatform.xhxw.ui.util.DateUtil;
import com.cplatform.xhxw.ui.util.LogUtil;
import com.google.gson.Gson;
import com.loopj.android.http.AsyncHttpResponseHandler;

public class MyRecordContributeFragment extends BaseFragment implements PullRefreshListener {
	private static final int COUNT_TO_FETCH = 20;
	private PullRefreshListView mListView;
	private ContributeRecordListAdapter mAdapter;

	private ArrayList<ContributeStatus> mContributes = new ArrayList<ContributeStatus>();
	private int mCurrentDatapage = 1;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.saas_fragment_my_record_contribute,
				container, false);
		initViews(rootView);
		return rootView;
	}
	
	private void initViews(View rootView) {
		mListView = (PullRefreshListView) rootView.findViewById(R.id.saas_fragment_my_record_contribute_list);
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		initEvents();
	}

	private void initEvents() {
		mAdapter = new ContributeRecordListAdapter();
		mListView.setAdapter(mAdapter);
		mListView.setPullRefreshListener(this);
		mListView.setCanRefresh(true);
		mListView.setCanLoadMore(false);
		mListView.triggerRefresh(true);
		mListView.setOnItemClickListener(mOnItemClick);
	}
	
	OnItemClickListener mOnItemClick = new OnItemClickListener() {
		@Override
		public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
				long arg3) {
			String contentid = mContributes.get(arg2 - 1).getContentId();
			startActivity(WebViewActivity.getIntentByURL(mAct, 
					HttpClientConfig.BASE_SAAS_URL + "/user/contributeDetail?contentId=" + contentid, 
					mContributes.get(arg2 - 1).getTitle()));
		}
	};

	@Override
	public void onRefresh() {
		fetchData(1);
	}

	@Override
	public void onLoadMore() {
		fetchData(mCurrentDatapage + 1);
	}
	
	private void fetchData(final int pageToFetch) {
		SaasGetContributeListRequest request = new SaasGetContributeListRequest();
		request.setSaasRequest(true);
		request.setPn(pageToFetch);
		request.setOffset(COUNT_TO_FETCH);
		
		APIClient.saasGetContributeList(request, new AsyncHttpResponseHandler(){

			@Override
			public void onFinish() {
				if (pageToFetch == 1) {
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
				SaasGetContributeListResponse response;
				try {
					ResponseUtil.checkResponse(content);
					response = new Gson().fromJson(content, SaasGetContributeListResponse.class);
				} catch (Exception e) {
					showToast(R.string.data_format_error);
                    LogUtil.w("", e);
                    return;
				}
				
				if(response.isSuccess()) {
					mCurrentDatapage = pageToFetch;
					if(pageToFetch == 1) {
						mContributes.clear();
					}
					mContributes.addAll(response.getData());
					mAdapter.notifyDataSetChanged();
					
					if(response.getData().size() < COUNT_TO_FETCH) {
						mListView.setCanLoadMore(false);
					} else {
						mListView.setCanLoadMore(true);
					}
				} else {
                    showToast(response.getMsg());
                }
			}

			@Override
			public void onFailure(Throwable error, String content) {
				// TODO Auto-generated method stub
				super.onFailure(error, content);
			}
		});
	}
	
	/**
	 * 数据适配
	 */
	class ContributeRecordListAdapter extends BaseAdapter{

		@Override
		public int getCount() {
			return mContributes.size();
		}

		@Override
		public ContributeStatus getItem(int arg0) {
			return mContributes.get(arg0);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			ViewHolder vh;
			if(convertView == null) {
				vh = new ViewHolder();
				convertView = LayoutInflater.from(mAct).inflate(R.layout.saas_item_contribute_list, null);
				vh.title = (TextView) convertView.findViewById(R.id.contribute_list_title_tv);
				vh.date = (TextView) convertView.findViewById(R.id.contribute_list_time_tv);
				vh.status = (TextView) convertView.findViewById(R.id.contribute_list_status_tv);
				convertView.setTag(vh);
			} else {
				vh = (ViewHolder) convertView.getTag();
			}
			ContributeStatus item = getItem(position);
			vh.title.setText(item.getTitle());
			vh.date.setText(DateUtil.getXHAPPDetailFormmatString(Long.valueOf(item.getCreate_date()) * 1000));
			vh.status.setText(item.getStatus());
			return convertView;
		}
		
		class ViewHolder {
			TextView title;
			TextView date;
			TextView status;
		}
	}
}
