
package com.hy.superemsg.activity;

import com.hy.superemsg.SuperEMsgApplication;
import com.hy.superemsg.adapter.MmsListAdapter;
import com.hy.superemsg.adapter.SmsListAdapter;
import com.hy.superemsg.req.ReqMmsContentQuery;
import com.hy.superemsg.req.ReqSmsContentQuery;
import com.hy.superemsg.rsp.BaseRspApi;
import com.hy.superemsg.rsp.RspMmsContentQuery;
import com.hy.superemsg.rsp.RspSmsContentQuery;
import com.hy.superemsg.utils.CommonUtils;
import com.hy.superemsg.utils.HttpUtils;
import com.hy.superemsg.utils.HttpUtils.AsynHttpCallback;

public class SearchMmsActivity extends SearchSmsActivity {

    protected void doSearch(String keyword) {
        HttpUtils.getInst().excuteTask(
                new ReqMmsContentQuery("", keyword, currPage,
                        SuperEMsgApplication.PAGE_SIZE),
                new AsynHttpCallback() {

                    @Override
                    public void onSuccess(BaseRspApi rsp) {
                        RspMmsContentQuery content = (RspMmsContentQuery) rsp;
                        if (CommonUtils.isNotEmpty(content.contentlist)) {
                            final MmsListAdapter adapter = new MmsListAdapter(
                                    SearchMmsActivity.this);
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
