
package com.hy.superemsg.activity;

import com.hy.superemsg.SuperEMsgApplication;
import com.hy.superemsg.adapter.GameListAdapter;
import com.hy.superemsg.adapter.SmsListAdapter;
import com.hy.superemsg.req.ReqGameContentQuery;
import com.hy.superemsg.req.ReqSmsContentQuery;
import com.hy.superemsg.rsp.BaseRspApi;
import com.hy.superemsg.rsp.RspGameContentQuery;
import com.hy.superemsg.rsp.RspSmsContentQuery;
import com.hy.superemsg.utils.CommonUtils;
import com.hy.superemsg.utils.HttpUtils;
import com.hy.superemsg.utils.HttpUtils.AsynHttpCallback;

public class SearchGameActivity extends SearchSmsActivity {

    @Override
    protected void doSearch(String keyword) {

        HttpUtils.getInst().excuteTask(
                new ReqGameContentQuery("", keyword, currPage,
                        SuperEMsgApplication.PAGE_SIZE),
                new AsynHttpCallback() {

                    @Override
                    public void onSuccess(BaseRspApi rsp) {
                        RspGameContentQuery content = (RspGameContentQuery) rsp;
                        if (CommonUtils.isNotEmpty(content.contentlist)) {
                            final GameListAdapter adapter = new GameListAdapter(
                                    SearchGameActivity.this);
                            adapter.setDatas(content.contentlist);
                            list.setAdapter(adapter);
                        }
                    }

                    @Override
                    public void onError(String error) {
                        // TODO Auto-generated method stub

                    }
                });

    }
}
