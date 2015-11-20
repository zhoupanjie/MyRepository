package com.hy.superemsg.req;

public class ReqRingSupport extends BaseReqApi {
	public int corpid;
	public String provinceid;
	public ReqRingSupport(int corpid, String provinceid) {
		this.corpid = corpid;
		this.provinceid = provinceid;
	}
	
}
