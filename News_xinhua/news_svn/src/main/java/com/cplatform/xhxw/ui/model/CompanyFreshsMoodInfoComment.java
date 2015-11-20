package com.cplatform.xhxw.ui.model;

public class CompanyFreshsMoodInfoComment {

	private String sendtype;// ': '评论类型',
	private String content;// ': '评论内容',
	private String ctime;// ': '创建时间',
	private String friendtime;// ': '友好时间',
	private String commentid;// ': '评论ID'
	private CompanyFreshsMoodInfoCommentSender userinfo;
	private CompanyFreshsMoodInfoCommentReplyer friendinfo;

	public String getSendtype() {
		return sendtype;
	}

	public void setSendtype(String sendtype) {
		this.sendtype = sendtype;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getCtime() {
		return ctime;
	}

	public void setCtime(String ctime) {
		this.ctime = ctime;
	}

	public String getFriendtime() {
		return friendtime;
	}

	public void setFriendtime(String friendtime) {
		this.friendtime = friendtime;
	}

	public String getCommentid() {
		return commentid;
	}

	public void setCommentid(String commentid) {
		this.commentid = commentid;
	}

	public CompanyFreshsMoodInfoCommentSender getUserinfo() {
		return userinfo;
	}

	public void setUserinfo(CompanyFreshsMoodInfoCommentSender userinfo) {
		this.userinfo = userinfo;
	}

	public CompanyFreshsMoodInfoCommentReplyer getFriendinfo() {
		return friendinfo;
	}

	public void setFriendinfo(CompanyFreshsMoodInfoCommentReplyer friendinfo) {
		this.friendinfo = friendinfo;
	}

}
