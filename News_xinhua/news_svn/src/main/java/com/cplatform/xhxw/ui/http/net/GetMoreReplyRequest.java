package com.cplatform.xhxw.ui.http.net;

public class GetMoreReplyRequest extends BaseRequest {
	private String commentid;
	private String replyid;
	
	public String getCommentid() {
		return commentid;
	}
	public void setCommentid(String commentid) {
		this.commentid = commentid;
	}
	public String getReplyid() {
		return replyid;
	}
	public void setReplyid(String replyid) {
		this.replyid = replyid;
	}
}
