package com.hy.superemsg.req;

public class ReqSystemXsmcallout extends BaseReqApi {
	public String callingnum;
	public String callednum;
	public String contentid;

	public ReqSystemXsmcallout(String callingnum, String callednum,
			String contentid) {
		this.callingnum = callingnum;
		this.callednum = callednum;
		this.contentid = contentid;
	}

}
