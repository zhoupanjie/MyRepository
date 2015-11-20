package com.hy.superemsg.rsp;

public class RspError extends BaseRspApi {
	public String error;

	private RspError() {

	}

	private static RspError _inst = new RspError();

	public static RspError getInst() {
		return _inst;
	}

	public RspError renew() {
		error = null;
		return this;
	}
}
