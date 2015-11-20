
package com.hy.superemsg.viewpager.fragments;

import java.util.List;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.hy.superemsg.R;
import com.hy.superemsg.SuperEMsgApplication;
import com.hy.superemsg.activity.AbsCollectionActivity;
import com.hy.superemsg.adapter.AbsCommonAdapter;
import com.hy.superemsg.adapter.NewsListAdapter;
import com.hy.superemsg.adapter.PicListAdapter;
import com.hy.superemsg.components.NewsBanner;
import com.hy.superemsg.db.DBUtils.DBHelper;
import com.hy.superemsg.req.BaseReqApi;
import com.hy.superemsg.req.ReqNewsContentQuery;
import com.hy.superemsg.req.ReqNewsFocusQuery;
import com.hy.superemsg.rsp.AbsContentDetail;
import com.hy.superemsg.rsp.BaseRspApi;
import com.hy.superemsg.rsp.NewsCategory;
import com.hy.superemsg.rsp.RspContentList;
import com.hy.superemsg.rsp.RspNewsContentQuery;
import com.hy.superemsg.rsp.RspNewsFocusQuery;
import com.hy.superemsg.utils.CommonUtils;
import com.hy.superemsg.utils.HttpUtils;
import com.hy.superemsg.utils.HttpUtils.AsynHttpCallback;
import com.hy.superemsg.viewpager.AbsListFragment;

public class PicListFragment extends AbsListFragment {
    private NewsBanner banner;
    private static final int FOCUS_COUNT = 4;
    private PicListAdapter adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_list_with_banner, null);
    }

    @Override
    protected void initUI() {
        super.initUI();
        banner = (NewsBanner) this.getView().findViewById(R.id.banner);
        NewsCategory newsCate = (NewsCategory) category;
        banner.setMeitu(newsCate.contenttype == NewsCategory.TYPE_PIC);
        adapter = new PicListAdapter(getActivity());
        list.setAdapter(adapter);
        HttpUtils.getInst().excuteTask(
                new ReqNewsFocusQuery(category.categoryid, FOCUS_COUNT),
                new AsynHttpCallback() {

                    @Override
                    public void onSuccess(BaseRspApi rsp) {
                        RspNewsFocusQuery focus = (RspNewsFocusQuery) rsp;
                        if (CommonUtils.isNotEmpty(focus.contentlist)) {
                            banner.setData(focus.contentlist);
                        }
                    }

                    @Override
                    public void onError(String error) {
                        SuperEMsgApplication.toast(error);
                    }
                });
    }

    @Override
    protected void OnContentListFirstLoaded(RspContentList rsp) {
        RspNewsContentQuery content = (RspNewsContentQuery) rsp;
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
        RspNewsContentQuery content = (RspNewsContentQuery) rsp;
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
        RspNewsContentQuery content = (RspNewsContentQuery) rsp;
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
        return new ReqNewsContentQuery(category.categoryid, page,
                constantPageSize);
    }

    @Override
    protected String getDBName() {
        return DBHelper.TABLE_NEWS;
    }

    @Override
    protected List<? extends AbsContentDetail> getDataList(RspContentList rsp) {
        RspNewsContentQuery content = (RspNewsContentQuery) rsp;
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
        return null;
    }

}
