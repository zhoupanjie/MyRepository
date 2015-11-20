
package com.hy.superemsg.activity;

import com.hy.superemsg.SuperEMsgApplication;
import com.hy.superemsg.adapter.RingListAdapter;
import com.hy.superemsg.adapter.SmsListAdapter;
import com.hy.superemsg.req.ReqRingContentQuery;
import com.hy.superemsg.req.ReqSmsContentQuery;
import com.hy.superemsg.rsp.BaseRspApi;
import com.hy.superemsg.rsp.Category;
import com.hy.superemsg.rsp.RspRingContentQuery;
import com.hy.superemsg.rsp.RspSmsContentQuery;
import com.hy.superemsg.utils.CommonUtils;
import com.hy.superemsg.utils.HttpUtils;
import com.hy.superemsg.utils.HttpUtils.AsynHttpCallback;

public class SearchRingActivity extends SearchSmsActivity {

    protected void doSearch(String keyword) {

        HttpUtils.getInst().excuteTask(
                new ReqRingContentQuery("", SuperEMsgApplication.account.operator,
                        SuperEMsgApplication.account.province, keyword, currPage,
                        SuperEMsgApplication.PAGE_SIZE),
                new AsynHttpCallback() {

                    @Override
                    public void onSuccess(BaseRspApi rsp) {
                        RspRingContentQuery content = (RspRingContentQuery) rsp;
                        if (CommonUtils.isNotEmpty(content.contentlist)) {
                            final RingListAdapter adapter = new RingListAdapter(
                                    SearchRingActivity.this);
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
