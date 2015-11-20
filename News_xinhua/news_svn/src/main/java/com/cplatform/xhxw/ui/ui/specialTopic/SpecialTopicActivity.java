package com.cplatform.xhxw.ui.ui.specialTopic;

import java.util.List;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import butterknife.ButterKnife;
import butterknife.Bind;
import butterknife.OnClick;

import com.cplatform.xhxw.ui.App;
import com.cplatform.xhxw.ui.R;
import com.cplatform.xhxw.ui.StatisticalKey;
import com.cplatform.xhxw.ui.http.APIClient;
import com.cplatform.xhxw.ui.http.ResponseUtil;
import com.cplatform.xhxw.ui.http.net.SpecialDetailRequest;
import com.cplatform.xhxw.ui.http.net.SpecialDetailResponse;
import com.cplatform.xhxw.ui.http.responseType.NewsType;
import com.cplatform.xhxw.ui.http.responseType.SpecialModelType;
import com.cplatform.xhxw.ui.model.PicShow;
import com.cplatform.xhxw.ui.model.SpecialDataInfo;
import com.cplatform.xhxw.ui.model.SpecialDetail;
import com.cplatform.xhxw.ui.model.SpecialDetailData;
import com.cplatform.xhxw.ui.ui.base.BaseActivity;
import com.cplatform.xhxw.ui.ui.base.adapter.SpecialTopicAdapter;
import com.cplatform.xhxw.ui.ui.base.view.OnSpecialTopicClickListener;
import com.cplatform.xhxw.ui.ui.base.widget.DefaultView;
import com.cplatform.xhxw.ui.ui.detailpage.NewsPageActivity;
import com.cplatform.xhxw.ui.ui.main.HomeActivity;
import com.cplatform.xhxw.ui.ui.picShow.PicShowActivity;
import com.cplatform.xhxw.ui.ui.web.WebViewActivity;
import com.cplatform.xhxw.ui.ui.web.newscollect.NewsCollectWebActivity;
import com.cplatform.xhxw.ui.util.Actions;
import com.cplatform.xhxw.ui.util.CommonUtils;
import com.cplatform.xhxw.ui.util.LogUtil;
import com.cplatform.xhxw.ui.util.ShareUtil;
import com.google.gson.Gson;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.wbtech.ums.UmsAgent;

/**
 * 专题列表 Created by cy-love on 14-1-4.
 */
public class SpecialTopicActivity extends BaseActivity implements
		AdapterView.OnItemClickListener, OnSpecialTopicClickListener {

	private static final String TAG = SpecialTopicActivity.class
			.getSimpleName();
	@Bind(R.id.list)
	ListView mListView;
	@Bind(R.id.def_view)
	DefaultView mDefView;
	@Bind(R.id.special_topic_title)
	TextView mSpecialTitle;
	private SpecialTopicAdapter mAdapter;
	private AsyncHttpResponseHandler mLoadHandler;
	private String mSpecialid;
	private String mChannelid;
	private String mTitle;
	private String shareurl;
	private Receiver mRecei;

	public static Intent getIntent(Context context, String specialid,
			String channelid) {
		Intent intent = new Intent(context, SpecialTopicActivity.class);
		intent.putExtra("specialid", specialid);
		intent.putExtra("channelid", channelid);
		return intent;
	}

	@Override
	protected String getScreenName() {
		return "SpecialTopicActivity";
	}

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_special_topic);
		ButterKnife.bind(this, this);
		initActionBar();
		mSpecialid = getIntent().getStringExtra("specialid");
		mChannelid = getIntent().getStringExtra("channelid");
		mAdapter = new SpecialTopicAdapter(this, this);
		mListView.setAdapter(mAdapter);
		mListView.setOnItemClickListener(this);
		mDefView.setHidenOtherView(mListView);
		mDefView.setStatus(DefaultView.Status.loading);
		mDefView.setListener(new DefaultView.OnTapListener() {
			@Override
			public void onTapAction() {
				loadData();
			}
		});
		loadData();

		mRecei = new Receiver();
		IntentFilter filter = new IntentFilter();
		filter.addAction(Actions.ACTION_NEWSLIST_TEXTSIZE_CHANGE);
		LocalBroadcastManager.getInstance(this)
				.registerReceiver(mRecei, filter);
	}

	private void loadData() {
		APIClient.specialDetail(new SpecialDetailRequest(mSpecialid),
				new AsyncHttpResponseHandler() {
					@Override
					public void onFinish() {
						mLoadHandler = null;
					}

					@Override
					protected void onPreExecute() {
						if (mLoadHandler != null)
							mLoadHandler.cancle();
						mLoadHandler = this;
						mDefView.setStatus(DefaultView.Status.loading);
					}

					@Override
					public void onFailure(Throwable error, String content) {
						mDefView.setStatus(DefaultView.Status.error);
					}

					@Override
					public void onSuccess(int statusCode, String content) {
						SpecialDetailResponse response;
						try {
							ResponseUtil.checkResponse(content);
							response = new Gson().fromJson(content,
									SpecialDetailResponse.class);
						} catch (Exception e) {
							mDefView.setStatus(DefaultView.Status.error);
							LogUtil.w(TAG, e);
							return;
						}

						if (response.isSuccess()) {
							SpecialDataInfo info = response.getData()
									.getSpecial();
							shareurl = info.getShareurl();
							mTitle = info.getSharetitle();
							mSpecialTitle.setText(info.getTitle());
							List<SpecialDetail> list = response.getData()
									.getList();
							if (list != null) {
								List<Object> data = mAdapter.getData();
								for (SpecialDetail item : list) {
									setListData(data, item);
								}
							}

							mAdapter.notifyDataSetChanged();
							mDefView.setStatus(DefaultView.Status.showData);
						} else {
							mDefView.setStatus(DefaultView.Status.error);
						}
					}

					private void setListData(List<Object> list,
							SpecialDetail item) {
						switch (getDataType(item)) {
						case 0:
							setGroupInfo(item);
							item.set_showHeadler(true);
							list.add(item);
							break;
						default:
							setItemsInfo(list, item);
							break;
						}
					}

					/**
					 * 设置单个分组内容信息
					 */
					private void setGroupInfo(SpecialDetail item) {
						String title = item.getTitle();
						int type = item.getSpecialmodeltype();
						List<SpecialDetailData> tmps = item.getData();
						for (SpecialDetailData tmp : tmps) {
							tmp.setSpecialmodeltype(type);
							tmp.setGrouptitle(title);
						}
					}

					/**
					 * 设置单个条目内容信息
					 */
					private void setItemsInfo(List<Object> list,
							SpecialDetail item) {
						String title = item.getTitle();
						int type = item.getSpecialmodeltype();
						List<SpecialDetailData> tmps = item.getData();
						boolean isShowHeader = true;
						for (SpecialDetailData tmp : tmps) {
							tmp.setSpecialmodeltype(type);
							tmp.setGrouptitle(title);
							tmp.set_showHeadler(isShowHeader);
							list.add(tmp);
							isShowHeader = false;
						}
					}

					private int getDataType(SpecialDetail item) {
						switch (item.getSpecialmodeltype()) {
						case SpecialModelType.ATLAS:
						case SpecialModelType.VIDEO:
						case SpecialModelType.IMAGE_5:
						case SpecialModelType.SLIDER:
							return 0;
						}
						return 1;
					}
				});
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		if (CommonUtils.isFastDoubleClick()) {
			return;
		}
		Object obj = mAdapter.getItem(position);
		if (obj instanceof SpecialDetailData) {
			SpecialDetailData item = (SpecialDetailData) obj;
			startMyActivity(item);
		}
	}

	@Override
	public void onSpecialTopicPicViewClick(SpecialDetailData item) {
		if (CommonUtils.isFastDoubleClick()) {
			return;
		}
		startMyActivity(item);
	}

	/**
	 * 跳转对应的activity
	 */
	private void startMyActivity(SpecialDetailData item) {
		UmsAgent.onEvent(this, StatisticalKey.news_details, new String[] {
				StatisticalKey.key_channelid, StatisticalKey.key_newsid,
				StatisticalKey.key_title },
				new String[] { mChannelid, item.getNewsId(), item.getTitle() });
		App.channel_id = mChannelid;
		App.news_id = item.getNewsId();
		item.setRead(true);
		switch (item.getNewsType()) {
		case NewsType.ATLAS: {
			PicShow picShow = new PicShow(new String());
			picShow.setCommentCount(item.getCommentcount());
			picShow.setNewsId(item.getNewsId());
			picShow.setTitle(item.getTitle());
			picShow.setPics(item.getPicInfo());
			startActivity(PicShowActivity.getIntent(this, picShow, 0,
					item.getNewsId(), true, HomeActivity.mIsUnderEnterprise, item.getTitle()));
		}
			break;
		case NewsType.LIVE:
			startActivity(WebViewActivity.getLiveNewsIntent(this,
					item.getNewsId(), item.getTitle(), true));
			break;
		case NewsType.COLLECT:
			startActivity(NewsCollectWebActivity.getCollectIntent(
					SpecialTopicActivity.this, item.getNewsId(),
					item.getTitle(), true));
			break;
		case NewsType.DRAW_PRIZE:
			startActivity(WebViewActivity.getDrawPrizeIntent(this, null,
					item.getNewsId()));
			break;
		default: {
			startActivity(NewsPageActivity.getIntent(this, item.getNewsId(),
					HomeActivity.mIsUnderEnterprise));
		}
			break;
		}
		mAdapter.notifyDataSetChanged();
	}

	/**
	 * 专题分享事件
	 */
	@OnClick(R.id.btn_share)
	public void onShareAction() {
		ShareUtil.sendTextIntent(this, null, getString(R.string.share_news),
				getString(R.string.share_news), mTitle, mTitle, shareurl,
				false, true, false, null, true);
	}

	protected void onPause() {
		has_channel_id = true;
		super.onPause();
	}

	@Override
	protected void onDestroy() {
		if (mLoadHandler != null) {
			mLoadHandler.cancle();
			mLoadHandler = null;
		}
		if (mRecei != null) {
			LocalBroadcastManager.getInstance(this).unregisterReceiver(mRecei);
			mRecei = null;
		}
		super.onDestroy();
	}

	private class Receiver extends BroadcastReceiver {
		@Override
		public void onReceive(Context context, Intent intent) {
			String action = intent.getAction();
			if (Actions.ACTION_NEWSLIST_TEXTSIZE_CHANGE.equals(action)) {
				mAdapter.notifyDataSetChanged();
			}

		}
	}
}