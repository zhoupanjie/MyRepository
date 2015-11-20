package com.cplatform.xhxw.ui.http.net;

public class ChannelSearchAddRequest extends BaseRequest {

	private int channelid;
	private String channelname;
	private int type;
	
	public int getChannelid() {
		return channelid;
	}
	public void setChannelid(int channelid) {
		this.channelid = channelid;
	}
	public String getChannelname() {
		return channelname;
	}
	public void setChannelname(String channelname) {
		this.channelname = channelname;
	}
	public int getType() {
		return type;
	}
	public void setType(int type) {
		this.type = type;
	}
}
