package com.hy.superemsg.req;


public class ReqSmsDetailQuery extends BaseReqApi {
	public String smsid;

	public ReqSmsDetailQuery(String smsid) {
		this.smsid = smsid;
	}

}
