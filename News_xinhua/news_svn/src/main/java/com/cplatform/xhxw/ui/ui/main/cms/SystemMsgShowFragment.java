package com.cplatform.xhxw.ui.ui.main.cms;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.cplatform.xhxw.ui.R;
import com.cplatform.xhxw.ui.http.APIClient;
import com.cplatform.xhxw.ui.http.ResponseUtil;
import com.cplatform.xhxw.ui.http.net.BaseResponse;
import com.cplatform.xhxw.ui.http.net.GetSysMsgListRequest;
import com.cplatform.xhxw.ui.http.net.NewsListNewsResponse;
import com.cplatform.xhxw.ui.http.net.NewsListResponse;
import com.cplatform.xhxw.ui.http.responseType.NewsType;
import com.cplatform.xhxw.ui.http.responseType.ShowType;
import com.cplatform.xhxw.ui.model.New;
import com.cplatform.xhxw.ui.ui.base.BaseFragment;
import com.cplatform.xhxw.ui.ui.base.widget.PullRefreshListView;
import com.cplatform.xhxw.ui.ui.base.widget.PullRefreshListView.PullRefreshListener;
import com.cplatform.xhxw.ui.ui.detailpage.NewsPageActivity;
import com.cplatform.xhxw.ui.ui.detailpage.VideoPlayerActivity;
import com.cplatform.xhxw.ui.ui.web.WebViewActivity;
import com.cplatform.xhxw.ui.ui.web.newscollect.NewsCollectWebActivity;
import com.cplatform.xhxw.ui.util.LogUtil;
import com.google.gson.Gson;
import com.loopj.android.http.AsyncHttpResponseHandler;

public class SystemMsgShowFragment extends BaseFragment implements
		PullRefreshListener {

	private static final int TYPE_LOAD_MORE = 0;
	private static final int TYPE_LOAD_NEW = 1;
	private static final String READ_STATUS_READ = "1";
	public static final String SYS_MSG_FLAG = "sysmsg_";

	private PullRefreshListView mListView;
	private List<New> mMsgsList = new ArrayList<New>();
	private SysMsgAdapter mAdapter;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View enterpriseHomeFragView = inflater.inflate(
				R.layout.fragment_system_msg, container, false);
		initViews(enterpriseHomeFragView);
		return enterpriseHomeFragView;
	}

	private void initViews(View rootView) {
		mListView = (PullRefreshListView) rootView
				.findViewById(R.id.sys_msg_listview);
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		initEvents();
	}

	private void initEvents() {
		mListView.setCanLoadMore(true);
		mListView.setCanRefresh(true);
		mListView.setPullRefreshListener(this);
		mAdapter = new SysMsgAdapter();
		mListView.setAdapter(mAdapter);
		mListView.triggerRefresh(true);
		mListView.setOnItemClickListener(mOnItemClick);
	}

	OnItemClickListener mOnItemClick = new OnItemClickListener() {

		@Override
		public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
				long arg3) {
			New item = mMsgsList.get(arg2 - 1);
			startActivity(item, 0);
		}
	};

	/**
	 * 跳转Activity
	 * 
	 * @param item
	 * @param picIndex
	 *            跨栏图片索引
	 */
	private void startActivity(New item, int picIndex) {
		item.setIsread(READ_STATUS_READ);
		mAdapter.notifyDataSetChanged();
		if (TextUtils.isEmpty(item.getNewsId())) {
			showToast("新闻id为空");
			return;
		}
		switch (item.getNewsType()) {
		case NewsType.LIVE:
			startActivity(WebViewActivity.getLiveNewsIntent(mAct,
					item.getNewsId(), item.getTitle(), true));
			break;
		case NewsType.COLLECT:
			startActivity(NewsCollectWebActivity.getCollectIntent(mAct,
					item.getNewsId(), item.getTitle(), true));
			break;
		case NewsType.DRAW_PRIZE:
			startActivity(WebViewActivity.getDrawPrizeIntent(mAct, null, item.getNewsId()));
			break;
		default:
			if (item.getNewsType() == NewsType.VIDEO
					&& item.getShowType() == ShowType.BIG_VIDEO) {
				startActivity(VideoPlayerActivity.getIntent(mAct,
						item.getNewsId(), item.getVideourl()));
			} else {
				startActivity(NewsPageActivity.getIntent(mAct, SYS_MSG_FLAG
						+ item.getNewsId(), false));
			}

			break;
		}
	}

	/**
	 * 加载消息列表
	 * 
	 * @param isLoadMore
	 */
	private void loadMsgs(final boolean isLoadMore) {
		GetSysMsgListRequest request = new GetSysMsgListRequest();
		request.setLastmsgid("0");
		request.setType(TYPE_LOAD_NEW);
		if (isLoadMore) {
			request.setLastmsgid(mMsgsList.size() > 0 ? mMsgsList.get(
					mMsgsList.size() - 1).getNewsId() : "0");
			request.setType(TYPE_LOAD_MORE);
		}

		APIClient.getSysMsg(request, new AsyncHttpResponseHandler() {
			@Override
			public void onFinish() {
				mListView.onRefreshComplete(null);
				mListView.onLoadMoreComplete();
			}

			@Override
			public void onSuccess(int statusCode, String content) {
				BaseResponse response_;
				try {
					ResponseUtil.checkResponse(content);
					if (!isLoadMore) {
						response_ = new Gson().fromJson(content,
								NewsListResponse.class);
					} else {
						response_ = new Gson().fromJson(content,
								NewsListNewsResponse.class);
					}
				} catch (Exception e) {
					LogUtil.w("", e);
					LogUtil.w("", "数据：" + content);
					return;
				}

				if (response_ == null)
					return;

				if (response_.isSuccess()) {
					if (response_ instanceof NewsListResponse) {
						NewsListResponse res = (NewsListResponse) response_;
						if (res.getData() != null) {
							NewsListResponse.Conetnt conetnt = res.getData();
							mMsgsList.clear();
							if (conetnt.getList() != null) {
								mMsgsList.addAll(conetnt.getList());
								if (conetnt.getList().size() < 20) {
									mListView.setCanLoadMore(false);
								}
							}
						}
					} else {
						NewsListNewsResponse res = (NewsListNewsResponse) response_;
						mMsgsList.addAll(res.getData());
						if (res.getData().size() < 20) {
							mListView.setCanLoadMore(false);
						}
					}

				} else {
					showToast(response_.getMsg());
				}
			}

			@Override
			public void onFailure(Throwable error, String content) {
				showToast(R.string.load_server_failure);
			}
		});
	}

	@Override
	public void onRefresh() {
		loadMsgs(false);
	}

	@Override
	public void onLoadMore() {
		loadMsgs(true);
	}

	class SysMsgAdapter extends BaseAdapter {

		@Override
		public int getCount() {
			return mMsgsList.size();
		}

		@Override
		public Object getItem(int position) {
			return mMsgsList.get(position);
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
				LayoutInflater lif = (LayoutInflater) getActivity()
						.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				convertView = lif.inflate(R.layout.item_sys_msg, null);
				vh.readStatusIv = (ImageView) convertView
						.findViewById(R.id.item_sys_msg_read_status_iv);
				vh.msgTitle = (TextView) convertView
						.findViewById(R.id.item_sys_msg_title_tv);
				vh.msgSummary = (TextView) convertView
						.findViewById(R.id.item_sys_msg_summary_tv);
				convertView.setTag(vh);
			} else {
				vh = (ViewHolder) convertView.getTag();
			}

			New item = (New) getItem(position);
			if (item.getIsread().equals(READ_STATUS_READ)) {
				vh.readStatusIv.setImageResource(R.drawable.icon_read);
				vh.msgTitle.setTextColor(Color.GRAY);
			} else {
				vh.readStatusIv.setImageResource(R.drawable.icon_unread);
				vh.msgTitle.setTextColor(Color.BLACK);
			}

			vh.msgTitle.setText(item.getTitle());

			if (item.getSummary() == null || item.getSummary().length() <= 0) {
				vh.msgSummary.setVisibility(View.GONE);
			} else {
				vh.msgSummary.setVisibility(View.VISIBLE);
				vh.msgSummary.setText(item.getSummary());
				vh.msgSummary.setTextColor(Color.GRAY);
			}

			return convertView;
		}

		class ViewHolder {
			ImageView readStatusIv;
			TextView msgTitle;
			TextView msgSummary;
		}
	}
}
