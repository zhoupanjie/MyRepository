package com.hy.superemsg.utils;

import com.hy.superemsg.R;
import com.hy.superemsg.SuperEMsgApplication;
import com.hy.superemsg.activity.GetPhoneNumberActivity;
import com.hy.superemsg.activity.RingZoneActivity;
import com.hy.superemsg.data.Account;
import com.hy.superemsg.req.ReqMobileGetSeg;
import com.hy.superemsg.req.ReqSmsPwdSend;
import com.hy.superemsg.rsp.BaseRspApi;
import com.hy.superemsg.rsp.RspMobileGetSeg;
import com.hy.superemsg.rsp.RspSmsPwdSend;
import com.hy.superemsg.utils.HttpUtils.AsynHttpCallback;

import android.app.Activity;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.widget.EditText;

public class AndroidUtil {

	private static final String TAG = AndroidUtil.class.getSimpleName();

	/*
	 * ����ҳ
	 */
	public static void openUriInBrowser(Activity _activity, String url) {
		if (null == _activity || ConverUtil.isEmpty(url)) {
			return;
		}
		Uri uri = Uri.parse(url);
		_activity.startActivity(new Intent(Intent.ACTION_VIEW, uri));
	}

	/*
	 * ���Ͷ���
	 */
	public static boolean sendSMS(String address, String message, Context context) {

		boolean isSended = false;
		android.telephony.SmsManager smsMgr = android.telephony.SmsManager.getDefault();
		Log.d("SendSms", address + " " + message);
		try {
			PendingIntent pi = PendingIntent.getBroadcast(context, 0, new Intent(), 0);
			smsMgr.sendTextMessage(address, null, message, pi, null);
			isSended = true;
		} catch (Exception e) {
			Log.d(TAG, e.toString());
			isSended = false;
		}
		return isSended;
	}
	
	

}
