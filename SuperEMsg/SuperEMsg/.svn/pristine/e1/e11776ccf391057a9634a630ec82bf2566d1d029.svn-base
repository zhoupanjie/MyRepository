package com.hy.superemsg.rsp;

public class RspRingCallOut extends RspSuccess {
	public int resultcode;
	public String resultdesc;
	public static final int[] codes = { 0, 101, 102, 103, 104 };
	public static final String[] descs = { "成功", "账号验证错误", "参数格式错误",
			"该运营点不支持外呼", "该运营点不支持铃音下载" };

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
