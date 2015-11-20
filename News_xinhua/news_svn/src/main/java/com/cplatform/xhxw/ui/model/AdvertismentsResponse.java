package com.cplatform.xhxw.ui.model;

import java.util.List;

public class AdvertismentsResponse {
	private List<Ad> top;
	private List<Ad> content;
	private List<Ad> bottom;
	
	public List<Ad> getTop() {
		return top;
	}
	public void setTop(List<Ad> top) {
		this.top = top;
	}
	public List<Ad> getContent() {
		return content;
	}
	public void setContent(List<Ad> content) {
		this.content = content;
	}
	public List<Ad> getBottom() {
		return bottom;
	}
	public void setBottom(List<Ad> bottom) {
		this.bottom = bottom;
	}
}
