package com.cplatform.xhxw.ui.http.net;

public class ChannelSearchCancelRequest extends BaseRequest {

	private int channelid;
	private String operatetime;
	
	public int getChannelid() {
		return channelid;
	}
	public void setChannelid(int channelid) {
		this.channelid = channelid;
	}
	public String getOperatetime() {
		return operatetime;
	}
	public void setOperatetime(String operatetime) {
		this.operatetime = operatetime;
	}
}
