
package com.hy.superemsg.viewpager.fragments;

import android.os.Bundle;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.Toast;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshGridView;
import com.hy.superemsg.R;
import com.hy.superemsg.SuperEMsgApplication;
import com.hy.superemsg.adapter.BookGridAdapter;
import com.hy.superemsg.req.ReqAnimationContentQuery;
import com.hy.superemsg.rsp.BaseRspApi;
import com.hy.superemsg.rsp.RspAnimationContentQuery;
import com.hy.superemsg.utils.CommonUtils;
import com.hy.superemsg.utils.HttpUtils;
import com.hy.superemsg.utils.HttpUtils.AsynHttpCallback;
import com.hy.superemsg.viewpager.AbsFragment;

public class CartoonShelfFragment extends AbsFragment {
    private PullToRefreshGridView bookshelf;
    private String categoryid;
    private int currPage;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_bookshelf, null);
    }

    @Override
    protected void initUI() {
        bookshelf = (PullToRefreshGridView) this.getView().findViewById(
                R.id.pull_refresh_grid);
        categoryid = getArguments().getString("categoryid");
        HttpUtils.getInst().excuteTask(
                new ReqAnimationContentQuery(categoryid, "", currPage,
                        SuperEMsgApplication.PAGE_SIZE),
                new AsynHttpCallback() {

                    @Override
                    public void onSuccess(BaseRspApi rsp) {
                        RspAnimationContentQuery content = (RspAnimationContentQuery) rsp;
                        if (CommonUtils.isNotEmpty(content.contentlist)) {
                            BookGridAdapter adapter = new BookGridAdapter(
                                    getActivity());
                            adapter.setDatas(content.contentlist);
                            bookshelf.setAdapter(adapter);
                        }
                    }

                    @Override
                    public void onError(String error) {
                        SuperEMsgApplication.toast(error);
                    }
                });

        bookshelf
                .setOnRefreshListener(new PullToRefreshBase.OnRefreshListener<GridView>() {

                    @Override
                    public void onRefresh(
                            PullToRefreshBase<GridView> refreshView) {
                        String label = DateUtils.formatDateTime(getActivity(),
                                System.currentTimeMillis(),
                                DateUtils.FORMAT_SHOW_TIME
                                        | DateUtils.FORMAT_SHOW_DATE
                                        | DateUtils.FORMAT_ABBREV_ALL);

                        // Update the LastUpdatedLabel
                        refreshView.getLoadingLayoutProxy()
                                .setLastUpdatedLabel(label);
                        bookshelf.post(new Runnable() {

                            @Override
                            public void run() {
                                HttpUtils
                                        .getInst()
                                        .excuteTask(
                                                new ReqAnimationContentQuery(
                                                        categoryid,
                                                        "",
                                                        currPage,
                                                        SuperEMsgApplication.PAGE_SIZE),
                                                new AsynHttpCallback() {

                                                    @Override
                                                    public void onSuccess(
                                                            BaseRspApi rsp) {
                                                        RspAnimationContentQuery content = (RspAnimationContentQuery) rsp;
                                                        if (CommonUtils
                                                                .isNotEmpty(content.contentlist)) {
                                                            BookGridAdapter adapter = new BookGridAdapter(
                                                                    getActivity());
                                                            adapter.setDatas(content.contentlist);
                                                            bookshelf
                                                                    .setAdapter(adapter);

                                                        }
                                                        bookshelf
                                                                .onRefreshComplete();
                                                    }

                                                    @Override
                                                    public void onError(
                                                            String error) {
                                                        SuperEMsgApplication.toast(error);
                                                        bookshelf
                                                                .onRefreshComplete();

                                                    }
                                                });
                            }
                        });
                    }
                });
    }

    @Override
    protected void excuteTask() {
        // TODO Auto-generated method stub

    }

    @Override
    protected void resetUI() {
        // TODO Auto-generated method stub

    }

}
