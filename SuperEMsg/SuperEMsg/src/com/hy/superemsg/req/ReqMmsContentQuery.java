package com.hy.superemsg.req;


public class ReqMmsContentQuery extends BaseReqApi {
	public String categoryid;
	public String key;
	public int pageno;
	public int pagesize;

	public ReqMmsContentQuery(String categoryid, String key, int pageno,
			int pagesize) {
		this.categoryid = categoryid;
		this.key = key;
		this.pageno = pageno;
		this.pagesize = pagesize;
	}

}
