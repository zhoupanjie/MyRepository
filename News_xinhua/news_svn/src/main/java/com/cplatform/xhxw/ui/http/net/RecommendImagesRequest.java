package com.cplatform.xhxw.ui.http.net;

public class RecommendImagesRequest extends BaseRequest{
	
	private String channelid;
	private String currentnewsid;
	
	public String getChannelid() {
		return channelid;
	}
	public void setChannelid(String channelid) {
		this.channelid = channelid;
	}
	public String getCurrentnewsid() {
		return currentnewsid;
	}
	public void setCurrentnewsid(String currentnewsid) {
		this.currentnewsid = currentnewsid;
	}
	
}
