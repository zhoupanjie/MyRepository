package com.hy.superemsg.rsp;

public class RspSuccess extends BaseRspApi {
	public RspError transferToError() {
		RspError error = RspError.getInst().renew();
		if (this.epheader.errcode != 0) {
			error.error = this.epheader.errmsg;
			return error;
		}
		return null;
	}
}
