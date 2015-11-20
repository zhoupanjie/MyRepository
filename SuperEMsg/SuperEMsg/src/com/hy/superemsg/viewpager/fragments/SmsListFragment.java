
package com.hy.superemsg.viewpager.fragments;

import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.Mode;
import com.hy.superemsg.R;
import com.hy.superemsg.SuperEMsgApplication;
import com.hy.superemsg.activity.AbsCollectionActivity;
import com.hy.superemsg.activity.SmsCollectionsActivity;
import com.hy.superemsg.adapter.AbsCommonAdapter;
import com.hy.superemsg.adapter.SmsListAdapter;
import com.hy.superemsg.db.DBUtils;
import com.hy.superemsg.db.DBUtils.DBHelper;
import com.hy.superemsg.req.BaseReqApi;
import com.hy.superemsg.req.ReqSmsContentQuery;
import com.hy.superemsg.rsp.AbsContentDetail;
import com.hy.superemsg.rsp.BaseRspApi;
import com.hy.superemsg.rsp.MmsContentDetail;
import com.hy.superemsg.rsp.RspContentList;
import com.hy.superemsg.rsp.RspGameContentQuery;
import com.hy.superemsg.rsp.RspSmsContentQuery;
import com.hy.superemsg.rsp.SmsContentDetail;
import com.hy.superemsg.utils.CommonUtils;
import com.hy.superemsg.utils.HttpUtils;
import com.hy.superemsg.utils.HttpUtils.AsynHttpCallback;
import com.hy.superemsg.viewpager.AbsListFragment;

public class SmsListFragment extends AbsListFragment {
    private SmsListAdapter adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_list, null);
    }

    @Override
    protected BaseReqApi getRequestApi(int page) {
        return new ReqSmsContentQuery(category.categoryid, "", page,
                constantPageSize);
    }

    @Override
    protected void initUI() {
        super.initUI();
        adapter = new SmsListAdapter(getActivity());
        list.setAdapter(adapter);
        List<SmsContentDetail> contents = SuperEMsgApplication.cached_sms
                .get(category);
        if (CommonUtils.isNotEmpty(contents)) {
        	for (SmsContentDetail sms : contents) {
				boolean collected = DBUtils.getInst().checkIfExist(
						getDBName() + "_collection", sms.getId());
				sms.setCollected(collected);
			}
            adapter.setDatas(contents);
        }
    }

    @Override
    protected void OnContentListFirstLoaded(RspContentList rsp) {
        RspSmsContentQuery content = (RspSmsContentQuery) rsp;
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
        RspSmsContentQuery content = (RspSmsContentQuery) rsp;
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
        RspSmsContentQuery content = (RspSmsContentQuery) rsp;
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
        return DBHelper.TABLE_SMS;
    }

    @Override
    protected List<? extends AbsContentDetail> getDataList(RspContentList rsp) {
        RspSmsContentQuery content = (RspSmsContentQuery) rsp;
        SuperEMsgApplication.cached_sms.put(category, content.contentlist);
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
