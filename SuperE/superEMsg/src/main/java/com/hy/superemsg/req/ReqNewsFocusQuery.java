package com.hy.superemsg.req;

public class ReqNewsFocusQuery extends BaseReqApi {
	public String categoryid;
	public int reqcount;
	public ReqNewsFocusQuery(String categoryid, int reqcount) {
		this.categoryid = categoryid;
		this.reqcount = reqcount;
	}
	
}
