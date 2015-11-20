package com.hy.superemsg.viewpager.fragments;

import java.util.List;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.hy.superemsg.R;
import com.hy.superemsg.SuperEMsgApplication;
import com.hy.superemsg.activity.AbsCollectionActivity;
import com.hy.superemsg.activity.MmsCollectionsActivity;
import com.hy.superemsg.adapter.AbsCommonAdapter;
import com.hy.superemsg.adapter.MmsListAdapter;
import com.hy.superemsg.db.DBUtils;
import com.hy.superemsg.db.DBUtils.DBHelper;
import com.hy.superemsg.req.BaseReqApi;
import com.hy.superemsg.req.ReqMmsContentQuery;
import com.hy.superemsg.rsp.AbsContentDetail;
import com.hy.superemsg.rsp.MmsContentDetail;
import com.hy.superemsg.rsp.RspContentList;
import com.hy.superemsg.rsp.RspMmsContentQuery;
import com.hy.superemsg.utils.CommonUtils;
import com.hy.superemsg.viewpager.AbsListFragment;

public class MmsListFragment extends AbsListFragment {
	private MmsListAdapter adapter;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		return inflater.inflate(R.layout.fragment_list, null);
	}

	@Override
	protected void initUI() {
		super.initUI();
		adapter = new MmsListAdapter(getActivity());
		list.setAdapter(adapter);
		List<MmsContentDetail> contents = SuperEMsgApplication.cached_mms
				.get(category);
		if (CommonUtils.isNotEmpty(contents)) {
			for (MmsContentDetail mms : contents) {
				boolean collected = DBUtils.getInst().checkIfExist(
						getDBName() + "_collection", mms.getId());
				mms.setCollected(collected);
			}
			adapter.setDatas(contents);
		}
	}

	@Override
	protected void OnContentListFirstLoaded(RspContentList rsp) {
		RspMmsContentQuery content = (RspMmsContentQuery) rsp;
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
		RspMmsContentQuery content = (RspMmsContentQuery) rsp;
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
		RspMmsContentQuery content = (RspMmsContentQuery) rsp;
		if (CommonUtils.isNotEmpty(content.contentlist)) {
			adapter.addDatas(content.contentlist);
		}
	}

	@Override
	protected void onContentListBottomRefreshError(String error) {
		SuperEMsgApplication.toast(error);
	}

	@Override
	protected BaseReqApi getRequestApi(int page) {
		return new ReqMmsContentQuery(category.categoryid, "", page,
				constantPageSize);
	}

	@Override
	protected String getDBName() {
		return DBHelper.TABLE_MMS;
	}

	@Override
	protected List<? extends AbsContentDetail> getDataList(RspContentList rsp) {
		RspMmsContentQuery content = (RspMmsContentQuery) rsp;
		SuperEMsgApplication.cached_mms.put(category, content.contentlist);
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
		return MmsCollectionsActivity.class;
	}

}
