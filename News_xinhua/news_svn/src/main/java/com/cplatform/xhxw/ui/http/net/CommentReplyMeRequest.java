package com.cplatform.xhxw.ui.http.net;

public class CommentReplyMeRequest extends BaseRequest {
	private String lastid;
	private int type; //0获取更多， 1获取最新
	private int offset; //单次获取条数
	private int pn; // 翻页码
	
	public String getLastid() {
		return lastid;
	}
	public void setLastid(String lastid) {
		this.lastid = lastid;
	}
	public int getType() {
		return type;
	}
	public void setType(int type) {
		this.type = type;
	}
	public int getOffset() {
		return offset;
	}
	public void setOffset(int offset) {
		this.offset = offset;
	}
	public int getPn() {
		return pn;
	}
	public void setPn(int pn) {
		this.pn = pn;
	}
}
