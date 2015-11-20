package com.hy.superemsg.viewpager.fragments;

import java.util.List;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.hy.superemsg.R;
import com.hy.superemsg.SuperEMsgApplication;
import com.hy.superemsg.activity.AbsCollectionActivity;
import com.hy.superemsg.activity.RingsCollectionsActivity;
import com.hy.superemsg.adapter.AbsCommonAdapter;
import com.hy.superemsg.adapter.RingListAdapter;
import com.hy.superemsg.db.DBUtils;
import com.hy.superemsg.db.DBUtils.DBHelper;
import com.hy.superemsg.req.BaseReqApi;
import com.hy.superemsg.req.ReqRingContentQuery;
import com.hy.superemsg.rsp.AbsContentDetail;
import com.hy.superemsg.rsp.RingContentDetail;
import com.hy.superemsg.rsp.RspContentList;
import com.hy.superemsg.rsp.RspRingContentQuery;
import com.hy.superemsg.utils.CommonUtils;
import com.hy.superemsg.viewpager.AbsListFragment;

public class RingListFragment extends AbsListFragment {
	private String provinceid;
	private int corpid;
	private RingListAdapter adapter;
	private TextView empty;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		return inflater.inflate(R.layout.fragment_list, null);
	}

	@Override
	protected void initUI() {
		super.initUI();
		empty = (TextView) this.getView()
				.findViewById(R.id.item_empty);
		provinceid = getArguments().getString("provinceid");
		corpid = getArguments().getInt("corpid");
		adapter = new RingListAdapter(getActivity());
		list.setAdapter(adapter);
		List<RingContentDetail> contents = SuperEMsgApplication.cached_ring
				.get(category);
		if (CommonUtils.isNotEmpty(contents)) {

			for (RingContentDetail ring : contents) {
				boolean collected = DBUtils.getInst().checkIfExist(
						getDBName() + "_collection", ring.getId());
				ring.setCollected(collected);
			}
			adapter.setDatas(contents);
			list.setVisibility(View.VISIBLE);
			empty.setVisibility(View.GONE);
		} else {
			list.setVisibility(View.GONE);
			empty = (TextView) this.getView().findViewById(
					R.id.item_empty);
			empty.setVisibility(View.VISIBLE);
			empty.setText("很抱歉，\n您的SIM卡所在地区未开通此项服务");
		}
	}

	@Override
	protected void OnContentListFirstLoaded(RspContentList rsp) {
		RspRingContentQuery content = (RspRingContentQuery) rsp;
		if (CommonUtils.isNotEmpty(content.contentlist)) {
			adapter.setDatas(content.contentlist);
			list.setVisibility(View.VISIBLE);
			empty.setVisibility(View.GONE);
		} else {
			list.setVisibility(View.GONE);
			empty = (TextView) this.getView().findViewById(
					R.id.item_empty);
			empty.setVisibility(View.VISIBLE);
			empty.setText("很抱歉，\n您的SIM卡所在地区未开通此项服务");
		}
	}

	@Override
	protected void onContentListFirstError(String error) {
		SuperEMsgApplication.toast(error);
	}

	@Override
	protected void onContentListTopRefreshLoaded(RspContentList rsp) {
		RspRingContentQuery content = (RspRingContentQuery) rsp;
		if (CommonUtils.isNotEmpty(content.contentlist)) {
			adapter.setDatas(content.contentlist);
			list.setVisibility(View.VISIBLE);
			empty.setVisibility(View.GONE);
		}else {
			list.setVisibility(View.GONE);
			empty = (TextView) this.getView().findViewById(
					R.id.item_empty);
			empty.setVisibility(View.VISIBLE);
			empty.setText("很抱歉，\n您的SIM卡所在地区未开通此项服务");
		}
	}

	@Override
	protected void onContentListTopRefreshError(String error) {
		SuperEMsgApplication.toast(error);
	}

	@Override
	protected void onContentListBottomRefreshLoaded(RspContentList rsp) {
		RspRingContentQuery content = (RspRingContentQuery) rsp;
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
		return new ReqRingContentQuery(category.categoryid, corpid, provinceid,
				"", page, constantPageSize);
	}

	@Override
	protected String getDBName() {
		return DBHelper.TABLE_RING;
	}

	@Override
	protected List<? extends AbsContentDetail> getDataList(RspContentList rsp) {
		RspRingContentQuery content = (RspRingContentQuery) rsp;
		SuperEMsgApplication.cached_ring.put(category, content.contentlist);
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
		return RingsCollectionsActivity.class;
	}

}
