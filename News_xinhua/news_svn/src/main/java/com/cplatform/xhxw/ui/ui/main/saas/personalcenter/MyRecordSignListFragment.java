package com.cplatform.xhxw.ui.ui.main.saas.personalcenter;

import java.util.ArrayList;
import java.util.Date;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.cplatform.xhxw.ui.R;
import com.cplatform.xhxw.ui.http.APIClient;
import com.cplatform.xhxw.ui.http.ResponseUtil;
import com.cplatform.xhxw.ui.http.net.saas.SaasGetContributeListRequest;
import com.cplatform.xhxw.ui.http.net.saas.SaasGetSignedNewsListResponse;
import com.cplatform.xhxw.ui.http.net.saas.SaasGetSignedNewsListResponse.SignedNewsStatus;
import com.cplatform.xhxw.ui.http.responseType.NewsType;
import com.cplatform.xhxw.ui.model.PicShow;
import com.cplatform.xhxw.ui.ui.base.BaseFragment;
import com.cplatform.xhxw.ui.ui.base.widget.PullRefreshListView;
import com.cplatform.xhxw.ui.ui.base.widget.PullRefreshListView.PullRefreshListener;
import com.cplatform.xhxw.ui.ui.detailpage.NewsPageActivity;
import com.cplatform.xhxw.ui.ui.main.HomeActivity;
import com.cplatform.xhxw.ui.ui.picShow.PicShowActivity;
import com.cplatform.xhxw.ui.ui.specialTopic.SpecialTopicActivity;
import com.cplatform.xhxw.ui.ui.web.WebViewActivity;
import com.cplatform.xhxw.ui.ui.web.newscollect.NewsCollectWebActivity;
import com.cplatform.xhxw.ui.util.LogUtil;
import com.google.gson.Gson;
import com.loopj.android.http.AsyncHttpResponseHandler;

public class MyRecordSignListFragment extends BaseFragment implements
		PullRefreshListener {
	private static final int COUNT_TO_FETCH = 20;
	private PullRefreshListView mListView;
	private SignedNewsListAdapter mAdapter;

	private ArrayList<SignedNewsStatus> mSignedNewsList = new ArrayList<SignedNewsStatus>();
	private int mCurrentDatapage = 1;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.saas_fragment_my_record_sign,
				container, false);
		initViews(rootView);
		return rootView;
	}

	private void initViews(View rootView) {
		mListView = (PullRefreshListView) rootView
				.findViewById(R.id.saas_fragment_my_record_sign_list);
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		initEvents();
	}

	private void initEvents() {
		mAdapter = new SignedNewsListAdapter();
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
			goToDetailPage(mSignedNewsList.get(arg2 - 1));
		}
	};

	private void goToDetailPage(SignedNewsStatus item) {
		mAdapter.notifyDataSetChanged();
		if (TextUtils.isEmpty(item.getNewsId())) {
			showToast("新闻id为空");
			return;
		}
		switch (item.getNewsType()) {
		case NewsType.SPECIAL_TOPIC:
			startActivity(SpecialTopicActivity.getIntent(mAct,
					item.getNewsId(), null));
			break;
		case NewsType.ATLAS: {
			PicShow picShow = new PicShow(new String());
			startActivity(PicShowActivity.getIntent(mAct, picShow, 0,
					item.getNewsId(), true, HomeActivity.mIsUnderEnterprise, item.getTitle()));
		}
			break;
		case NewsType.LIVE:
			startActivity(WebViewActivity.getIntentByURL(mAct,
					item.getLiveurl(), item.getTitle()));
			break;
		case NewsType.COLLECT:
			startActivity(NewsCollectWebActivity.getCollectIntent(mAct,
					item.getNewsId(), item.getTitle(), true));
			break;
		case NewsType.DRAW_PRIZE:
			startActivity(WebViewActivity.getDrawPrizeIntent(mAct, null,
					item.getNewsId()));
			break;
		default:
			startActivity(NewsPageActivity.getIntent(mAct, item.getNewsId(),
					true));
			break;
		}
	}

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

		APIClient.saasGetSignNewsList(request, new AsyncHttpResponseHandler() {

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
				SaasGetSignedNewsListResponse response;
				try {
					ResponseUtil.checkResponse(content);
					response = new Gson().fromJson(content,
							SaasGetSignedNewsListResponse.class);
				} catch (Exception e) {
					showToast(R.string.data_format_error);
					LogUtil.w("", e);
					return;
				}

				if (response.isSuccess()) {
					mCurrentDatapage = pageToFetch;
					if (pageToFetch == 1) {
						mSignedNewsList.clear();
					}
					mSignedNewsList.addAll(response.getData());
					mAdapter.notifyDataSetChanged();

					if (response.getData().size() < COUNT_TO_FETCH) {
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
	class SignedNewsListAdapter extends BaseAdapter {

		@Override
		public int getCount() {
			return mSignedNewsList.size();
		}

		@Override
		public SignedNewsStatus getItem(int arg0) {
			return mSignedNewsList.get(arg0);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			ViewHolder vh;
			if (convertView == null) {
				vh = new ViewHolder();
				convertView = LayoutInflater.from(mAct).inflate(
						R.layout.saas_item_contribute_list, null);
				vh.title = (TextView) convertView
						.findViewById(R.id.contribute_list_title_tv);
				vh.date = (TextView) convertView
						.findViewById(R.id.contribute_list_time_tv);
				vh.status = (TextView) convertView
						.findViewById(R.id.contribute_list_status_tv);
				convertView.setTag(vh);
			} else {
				vh = (ViewHolder) convertView.getTag();
			}
			SignedNewsStatus item = getItem(position);
			vh.title.setText(item.getTitle());
			vh.date.setText(item.getSummary());
			vh.status.setText(null);
			return convertView;
		}

		class ViewHolder {
			TextView title;
			TextView date;
			TextView status;
		}
	}
}
