package com.hy.superemsg.req;


public class ReqSmsPwdValidate extends BaseReqApi {
	public String phonenum;
	public String smspwd;

	public ReqSmsPwdValidate(String phonenum, String smspwd) {
		this.phonenum = phonenum;
		this.smspwd = smspwd;
	}

}
