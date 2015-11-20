package com.hy.superemsg.rsp;

public class RspSmsPwdSend extends RspSuccess {
	public int resultcode;
	public String resultdesc;
	public String smspwd;
	public static final int[] codes = { 0, 1, 101, 102 };
	public static final String[] descs = { "成功", "未知错误", "账号验证错误", "参数格式错误" };

	@Override
	public RspError transferToError() {
		if (resultcode != 0) {
			RspError error = RspError.getInst().renew();
			error.error = resultdesc;
			return error;
		}
		return super.transferToError();
	}
}
