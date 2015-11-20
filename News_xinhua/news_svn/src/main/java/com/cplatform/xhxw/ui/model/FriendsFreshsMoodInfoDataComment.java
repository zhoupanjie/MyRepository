package com.cplatform.xhxw.ui.model;

public class FriendsFreshsMoodInfoDataComment {

	private String commentid;// ": "cdef1",// 评论ID
	private String ctime;// ": "0",// 评论时间
	private String friendtime;// ": "1天前",
	private String content;// ": "回复黑老大",// 评论内容
	private String pid;// ": "",//父节点ID

	/** 发送用户信息 */
	private FriendsFreshsMoodInfoDataCommentSender senduser;

	/** 被回复用户信息 */
	private FriendsFreshsMoodInfoDataCommentReply replyuser;

	public String getCommentid() {
		return commentid;
	}

	public void setCommentid(String commentid) {
		this.commentid = commentid;
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

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getPid() {
		return pid;
	}

	public void setPid(String pid) {
		this.pid = pid;
	}

	public FriendsFreshsMoodInfoDataCommentSender getSenduser() {
		return senduser;
	}

	public void setSenduser(FriendsFreshsMoodInfoDataCommentSender senduser) {
		this.senduser = senduser;
	}

	public FriendsFreshsMoodInfoDataCommentReply getReplyuser() {
		return replyuser;
	}

	public void setReplyuser(FriendsFreshsMoodInfoDataCommentReply replyuser) {
		this.replyuser = replyuser;
	}

}
