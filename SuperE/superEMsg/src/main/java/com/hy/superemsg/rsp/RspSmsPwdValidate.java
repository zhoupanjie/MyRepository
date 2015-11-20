package com.hy.superemsg.rsp;

import android.text.TextUtils;

public class RspSmsPwdValidate extends RspSuccess {
	public int resultcode;
	public String resultdesc;
	public static final int[] codes = { 1, 101, 102, 103, 104, 110, 111 };
	public static final String[] descs = { "未知错误", "账号验证错误", "参数格式错误",
			"该运营点不支持外呼", "该运营点不支持铃音下载", "短信检验成功", "短信校验失败" };

	@Override
	public RspError transferToError() {
		if (!TextUtils.isEmpty(resultdesc)) {
			RspError error = RspError.getInst().renew();
			error.error = resultdesc;
			return error;
		}
		return super.transferToError();
	}
}
