package com.hy.superemsg.receiver;

import com.hy.superemsg.SuperEMsgApplication;
import com.hy.superemsg.data.Contact;
import com.hy.superemsg.req.ReqSystemXsmcallout;
import com.hy.superemsg.rsp.BaseRspApi;
import com.hy.superemsg.utils.HttpUtils;
import com.hy.superemsg.utils.HttpUtils.AsynHttpCallback;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

public class SmsSentReceiver extends BroadcastReceiver {

	@Override
	public void onReceive(final Context context, Intent intent) {
		if (intent.getAction().equals("com.hy.superemsg.SmsSentSuccess")) {
			boolean shouldCallVoiceWish = intent.getBooleanExtra(
					SuperEMsgApplication.EXTRA_SEND_VOICEWISH, false);
			if (shouldCallVoiceWish) {
				final Contact c = intent
						.getParcelableExtra(SuperEMsgApplication.EXTRA_SEND_NAMES);
				String smsid = intent
						.getStringExtra(SuperEMsgApplication.EXTRA_SEND_SMSID);
				HttpUtils.getInst().excuteTask(
						new ReqSystemXsmcallout(
								SuperEMsgApplication.account.phonenum, c.phone,
								smsid), new AsynHttpCallback() {

							@Override
							public void onSuccess(BaseRspApi rsp) {
								Toast.makeText(context, "对"+c.getContactVisibleContent()+"发送祝福成功!",
										Toast.LENGTH_SHORT).show();
							}

							@Override
							public void onError(String error) {
								Toast.makeText(context, "对"+c.getContactVisibleContent()+"发送祝福失败!",
										Toast.LENGTH_SHORT).show();
							}
						});
			}
		}
	}

}
