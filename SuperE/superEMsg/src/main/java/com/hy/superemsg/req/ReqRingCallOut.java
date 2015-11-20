package com.hy.superemsg.req;

public class ReqRingCallOut extends BaseReqApi {
	public String callingnum;
	public String callednum;
	public String ringid;
	public String ringname;

	public ReqRingCallOut(String callingnum, String callednum, String ringid,
			String ringname) {
		this.callingnum = callingnum;
		this.callednum = callednum;
		this.ringid = ringid;
		this.ringname = ringname;
	}

}
