package com.hy.superemsg.viewpager.fragments;

import java.util.List;

import android.R.layout;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.hy.superemsg.R;
import com.hy.superemsg.SuperEMsgApplication;
import com.hy.superemsg.activity.AbsCollectionActivity;
import com.hy.superemsg.activity.SmsCollectionsActivity;
import com.hy.superemsg.adapter.AbsCommonAdapter;
import com.hy.superemsg.adapter.HolidayListAdapter;
import com.hy.superemsg.db.DBUtils;
import com.hy.superemsg.db.DBUtils.DBHelper;
import com.hy.superemsg.req.BaseReqApi;
import com.hy.superemsg.req.ReqHolidayContentQuery;
import com.hy.superemsg.rsp.AbsContentDetail;
import com.hy.superemsg.rsp.HolidayContentDetail;
import com.hy.superemsg.rsp.RspContentList;
import com.hy.superemsg.rsp.RspHolidayContentQuery;
import com.hy.superemsg.utils.CommonUtils;
import com.hy.superemsg.viewpager.AbsListFragment;

public class HolidayDetailFragment extends AbsListFragment {
	private HolidayListAdapter adapter;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		return inflater.inflate(R.layout.fragment_list, null);
	}

	@Override
	protected BaseReqApi getRequestApi(int page) {
		return new ReqHolidayContentQuery(category.categoryid, "", page,
				constantPageSize);
	}

	@Override
	protected void initUI() {
		super.initUI();
		adapter = new HolidayListAdapter(getActivity());
		list.setAdapter(adapter);
		List<HolidayContentDetail> contents = SuperEMsgApplication.cached_holiday
				.get(category);
		if (CommonUtils.isNotEmpty(contents)) {
			for (HolidayContentDetail hly : contents) {
				boolean collected = DBUtils.getInst().checkIfExist(
						getDBName() + "_collection", hly.getId());
				hly.setCollected(collected);
			}
			adapter.setDatas(contents);
		}
	}

	@Override
	protected void OnContentListFirstLoaded(RspContentList rsp) {
		RspHolidayContentQuery content = (RspHolidayContentQuery) rsp;
		if (CommonUtils.isNotEmpty(content.contentlist)) {
			adapter.setDatas(content.contentlist);
		}
	}

	@Override
	protected void onContentListFirstError(String error) {
		SuperEMsgApplication.toast(error);
	}

	@Override
	protected void onContentListTopRefreshLoaded(RspContentList rsp) {
		RspHolidayContentQuery content = (RspHolidayContentQuery) rsp;
		if (CommonUtils.isNotEmpty(content.contentlist)) {
			adapter.setDatas(content.contentlist);
		}
	}

	@Override
	protected void onContentListTopRefreshError(String error) {
		SuperEMsgApplication.toast(error);
	}

	@Override
	protected void onContentListBottomRefreshLoaded(RspContentList rsp) {
		RspHolidayContentQuery content = (RspHolidayContentQuery) rsp;
		if (CommonUtils.isNotEmpty(content.contentlist)) {
			adapter.addDatas(content.contentlist);
		}
	}

	@Override
	protected void onContentListBottomRefreshError(String error) {
		SuperEMsgApplication.toast(error);
	}

	@Override
	protected String getDBName() {
		return DBHelper.TABLE_HOLIDAY;
	}

	@Override
	protected List<? extends AbsContentDetail> getDataList(RspContentList rsp) {
		RspHolidayContentQuery content = (RspHolidayContentQuery) rsp;
		SuperEMsgApplication.cached_holiday.put(category, content.contentlist);
		return content.contentlist;
	}

	@Override
	protected List<? extends AbsContentDetail> getDatasInList() {
		return adapter.getDatas();
	}

	@Override
	protected AbsCommonAdapter<? extends AbsContentDetail> getAdapter() {
		return adapter;
	}

	@Override
	protected Class<? extends AbsCollectionActivity> getCollectionActivity() {
		return SmsCollectionsActivity.class;
	}

}
