package com.cplatform.xhxw.ui.http.net;

public class CyanCommentRequest extends BaseRequest {
	private int pg;
	private int offset;
	
	public int getPg() {
		return pg;
	}
	public void setPg(int pg) {
		this.pg = pg;
	}
	public int getOffset() {
		return offset;
	}
	public void setOffset(int offset) {
		this.offset = offset;
	}
}
