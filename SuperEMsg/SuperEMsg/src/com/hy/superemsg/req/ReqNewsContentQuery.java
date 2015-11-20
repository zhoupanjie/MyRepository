package com.hy.superemsg.req;

public class ReqNewsContentQuery extends BaseReqApi{
	public String categoryid;
	public int pageno;
	public int pagesize;
	public ReqNewsContentQuery(String categoryid, int pageno, int pagesize) {
		this.categoryid = categoryid;
		this.pageno = pageno;
		this.pagesize = pagesize;
	}
	
}
