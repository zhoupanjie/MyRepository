
package com.cplatform.xhxw.ui.wxapi;

import com.cplatform.xhxw.ui.util.LogUtil;
import com.tencent.mm.sdk.modelbase.BaseResp;
import com.umeng.socialize.weixin.view.WXCallbackActivity;

public class WXEntryActivity extends WXCallbackActivity {

	@Override
	public void onResp(BaseResp arg0) {
		LogUtil.e("", "weixin response-------------->>>>" + arg0.errCode +
				"------errStr--------" + arg0.errStr);
		super.onResp(arg0);
	}
}
