package com.cplatform.xhxw.ui.http.net;

public class GetUserChannelRequest extends BaseRequest {
	private double lnt;
	private double lat;
	
	public double getLat() {
		return lat;
	}
	public void setLat(double lat) {
		this.lat = lat;
	}
	public double getLnt() {
		return lnt;
	}
	public void setLnt(double lnt) {
		this.lnt = lnt;
	}
}
