package com.cplatform.xhxw.ui.http.net;

public class GetSysMsgListRequest extends BaseRequest {
	private String lastmsgid;
	private int type;
	
	public String getLastmsgid() {
		return lastmsgid;
	}
	public void setLastmsgid(String lastmsgid) {
		this.lastmsgid = lastmsgid;
	}
	public int getType() {
		return type;
	}
	public void setType(int type) {
		this.type = type;
	}
}
