package com.hy.superemsg.viewpager;

import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.Mode;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.hy.superemsg.R;
import com.hy.superemsg.SuperEMsgApplication;
import com.hy.superemsg.activity.AbsCollectionActivity;
import com.hy.superemsg.adapter.AbsCommonAdapter;
import com.hy.superemsg.db.DBUtils;
import com.hy.superemsg.db.DBUtils.DBHelper;
import com.hy.superemsg.req.BaseReqApi;
import com.hy.superemsg.req.ReqFestivalRemind;
import com.hy.superemsg.rsp.AbsContentDetail;
import com.hy.superemsg.rsp.BaseRspApi;
import com.hy.superemsg.rsp.Category;
import com.hy.superemsg.rsp.RspContentList;
import com.hy.superemsg.rsp.RspFestivalRemind;
import com.hy.superemsg.utils.CommonUtils;
import com.hy.superemsg.utils.HttpUtils;
import com.hy.superemsg.utils.HttpUtils.AsynHttpCallback;

public abstract class AbsListFragment extends AbsFragment {
	protected PullToRefreshListView list;
	protected Category category;
	protected int currPage;
	protected int totalPage;
	protected int totalRecord;
	protected final int constantPageSize = 10;
	private ProgressBar loadingBar;

	protected boolean hasLoadingMore() {
		return currPage < totalPage;
	}

	@Override
	protected void initUI() {
		list = (PullToRefreshListView) getView().findViewById(
				R.id.pull_refresh_list);
		totalRecord = 0;
		totalPage = 0;
		if (hasLoadingMore()) {
			list.setMode(Mode.BOTH);
		} else {
			list.setMode(Mode.PULL_FROM_START);
		}
		loadingBar = (ProgressBar) getView().findViewById(R.id.item_loading);
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		currPage = 1;
		category = getArguments().getParcelable("categoryid");
	}

	protected abstract void OnContentListFirstLoaded(RspContentList rsp);

	protected abstract void onContentListFirstError(String error);

	protected abstract void onContentListTopRefreshLoaded(RspContentList rsp);

	protected abstract void onContentListTopRefreshError(String error);

	protected abstract void onContentListBottomRefreshLoaded(RspContentList rsp);

	protected abstract void onContentListBottomRefreshError(String error);

	protected abstract String getDBName();

	protected abstract Class<? extends AbsCollectionActivity> getCollectionActivity();

	protected abstract List<? extends AbsContentDetail> getDataList(
			RspContentList rsp);

	public void startCollectionActivity() {
		startActivityForResult(new Intent(getActivity(),
				getCollectionActivity()),
				SuperEMsgApplication.REQUEST_LIST_TO_COLLECTION);
	}

	@Override
	protected void excuteTask() {
		final RelativeLayout layout = (RelativeLayout) getView().findViewById(
				R.id.item_remind_layout);
		if (layout != null) {
			final TextView tv = (TextView) layout
					.findViewById(R.id.item_remind_text);
			ImageView iv = (ImageView) layout
					.findViewById(R.id.item_remind_delete);
			if (tv != null) {
				if (SuperEMsgApplication.festivalRemind != null) {
					tv.setText(SuperEMsgApplication.festivalRemind);
					layout.setVisibility(View.VISIBLE);
					iv.setOnClickListener(new View.OnClickListener() {

						@Override
						public void onClick(View v) {
							layout.setVisibility(View.GONE);
						}
					});
				} else {
					HttpUtils.getInst().excuteTask(new ReqFestivalRemind(),
							new AsynHttpCallback() {

								@Override
								public void onSuccess(BaseRspApi rsp) {
									RspFestivalRemind remind = (RspFestivalRemind) rsp;
									if (remind.festivalremind != null) {
										SuperEMsgApplication.festivalRemind = remind.festivalremind;
										tv.setText(SuperEMsgApplication.festivalRemind);
										layout.setVisibility(View.VISIBLE);
										layout.findViewById(
												R.id.item_remind_delete)
												.setOnClickListener(
														new View.OnClickListener() {

															@Override
															public void onClick(
																	View v) {
																layout.setVisibility(View.GONE);
															}
														});
									}

								}

								@Override
								public void onError(String error) {
									tv.setVisibility(View.GONE);
								}
							});
				}
			}
		}
		if (!CommonUtils.isNotEmpty(getDatasInList())) {
			loadingBar.setVisibility(View.VISIBLE);
			list.setVisibility(View.GONE);
			HttpUtils.getInst().excuteTask(getRequestApi(currPage),
					new AsynHttpCallback() {

						@Override
						public void onSuccess(BaseRspApi rsp) {
							RspContentList contentList = (RspContentList) rsp;
							totalPage = contentList.totalpage;
							totalRecord = contentList.totalrecord;
							if (hasLoadingMore()) {
								list.setMode(Mode.BOTH);
							} else {
								list.setMode(Mode.PULL_FROM_START);
							}
							if (!getDBName().equals(DBHelper.TABLE_NEWS)
									&& !getDBName().equals(
											DBHelper.TABLE_NEWS_COLLECTION)) {
								DBUtils.getInst().removeCategoryBasedContents(
										getDBName(), category.categoryid);
								List<? extends AbsContentDetail> datalist = getDataList(contentList);
								if (CommonUtils.isNotEmpty(datalist)) {
									for (AbsContentDetail content : datalist) {
										DBUtils.getInst().insert(getDBName(),
												category.categoryid, content);
										boolean collected = DBUtils
												.getInst()
												.checkIfExist(
														getDBName()
																+ "_collection",
														content.getId());
										content.setCollected(collected);
									}
								}
							}
							loadingBar.setVisibility(View.GONE);
							list.setVisibility(View.VISIBLE);
							OnContentListFirstLoaded(contentList);
						}

						@Override
						public void onError(String error) {
							loadingBar.setVisibility(View.GONE);
							list.setVisibility(View.VISIBLE);
							onContentListFirstError(error);
						}
					});
		}
		list.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {

			@Override
			public void onPullDownToRefresh(
					PullToRefreshBase<ListView> refreshView) {
				String label = DateUtils.formatDateTime(getActivity(),
						System.currentTimeMillis(), DateUtils.FORMAT_SHOW_TIME
								| DateUtils.FORMAT_SHOW_DATE
								| DateUtils.FORMAT_ABBREV_ALL);

				// Update the LastUpdatedLabel
				refreshView.getLoadingLayoutProxy().setLastUpdatedLabel(label);
				list.post(new Runnable() {

					@Override
					public void run() {
						HttpUtils.getInst().excuteTask(getRequestApi(currPage),
								new AsynHttpCallback() {

									@Override
									public void onSuccess(BaseRspApi rsp) {
										RspContentList contentList = (RspContentList) rsp;
										totalPage = contentList.totalpage;
										totalRecord = contentList.totalrecord;
										if (hasLoadingMore()) {
											list.setMode(Mode.BOTH);
										} else {
											list.setMode(Mode.PULL_FROM_START);
										}
										if (!getDBName().equals(
												DBHelper.TABLE_NEWS)
												&& !getDBName()
														.equals(DBHelper.TABLE_NEWS_COLLECTION)) {
											DBUtils.getInst()
													.removeCategoryBasedContents(
															getDBName(),
															category.categoryid);
											List<? extends AbsContentDetail> datalist = getDataList(contentList);
											if (CommonUtils
													.isNotEmpty(datalist)) {
												for (AbsContentDetail content : datalist) {
													DBUtils.getInst()
															.insert(getDBName(),
																	category.categoryid,
																	content);
													boolean collected = DBUtils
															.getInst()
															.checkIfExist(
																	getDBName()
																			+ "_collection",
																	content.getId());
													content.setCollected(collected);
												}
											}
										}
										onContentListTopRefreshLoaded(contentList);
										list.onRefreshComplete();
									}

									@Override
									public void onError(String error) {
										onContentListTopRefreshError(error);
										list.onRefreshComplete();
									}
								});
					}
				});

			}

			@Override
			public void onPullUpToRefresh(
					PullToRefreshBase<ListView> refreshView) {

				String label = DateUtils.formatDateTime(getActivity(),
						System.currentTimeMillis(), DateUtils.FORMAT_SHOW_TIME
								| DateUtils.FORMAT_SHOW_DATE
								| DateUtils.FORMAT_ABBREV_ALL);

				// Update the LastUpdatedLabel
				refreshView.getLoadingLayoutProxy().setLastUpdatedLabel(label);
				list.post(new Runnable() {

					@Override
					public void run() {
						final int nextPage = currPage + 1;
						HttpUtils.getInst().excuteTask(getRequestApi(nextPage),
								new AsynHttpCallback() {

									@Override
									public void onSuccess(BaseRspApi rsp) {
										currPage = nextPage;
										RspContentList contentList = (RspContentList) rsp;
										totalPage = contentList.totalpage;
										totalRecord = contentList.totalrecord;
										if (hasLoadingMore()) {
											list.setMode(Mode.BOTH);
										} else {
											list.setMode(Mode.PULL_FROM_START);
										}
										List<? extends AbsContentDetail> datalist = getDataList(contentList);
										if (CommonUtils.isNotEmpty(datalist)) {
											for (AbsContentDetail content : datalist) {
												boolean collected = DBUtils
														.getInst()
														.checkIfExist(
																getDBName()
																		+ "_collection",
																content.getId());
												content.setCollected(collected);
											}
										}
										onContentListBottomRefreshLoaded(contentList);
										list.onRefreshComplete();
									}

									@Override
									public void onError(String error) {
										onContentListBottomRefreshError(error);
										list.onRefreshComplete();
									}
								});
					}
				});

			}
		});
	}

	@Override
	protected void resetUI() {
		// TODO Auto-generated method stub

	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (getAdapter() == null)
			return;
		List<? extends AbsContentDetail> datas = getDatasInList();
		if (CommonUtils.isNotEmpty(datas)) {
			for (AbsContentDetail content : datas) {
				boolean collected = DBUtils.getInst().checkIfExist(
						getDBName() + "_collection", content.getId());
				content.setCollected(collected);
				getAdapter().notifyDataSetChanged();
			}

		}
	}

	protected abstract AbsCommonAdapter<? extends AbsContentDetail> getAdapter();

	protected abstract List<? extends AbsContentDetail> getDatasInList();

	protected abstract BaseReqApi getRequestApi(int page);

}
