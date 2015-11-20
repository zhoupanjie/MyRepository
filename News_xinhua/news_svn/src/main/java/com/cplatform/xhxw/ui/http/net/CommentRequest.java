package com.cplatform.xhxw.ui.http.net;

public class CommentRequest extends BaseRequest{

	private String newsId;//	int	是	新闻ID
	private int offset;//	Int	否	返回条数，默认为20
	private int hostLimit;//	int	否	最热评论条数默认为10，数据只出现在第一页上
	private int pn;//	int	是	页面ID，默认为1
	private String action;//	string	否	操作动作,down获取老数据，up拉扯新数据
	private String commentid;//	int	否	根据此ID或防止数据迭代
	
	public String getNewsId() {
		return newsId;
	}
	public void setNewsId(String newsId) {
		this.newsId = newsId;
	}
	public int getOffset() {
		return offset;
	}
	public void setOffset(int offset) {
		this.offset = offset;
	}
	public int getHostLimit() {
		return hostLimit;
	}
	public void setHostLimit(int hostLimit) {
		this.hostLimit = hostLimit;
	}
	public int getPn() {
		return pn;
	}
	public void setPn(int pn) {
		this.pn = pn;
	}
	public String getAction() {
		return action;
	}
	public void setAction(String action) {
		this.action = action;
	}
	public String getCommentid() {
		return commentid;
	}
	public void setCommentid(String commentid) {
		this.commentid = commentid;
	}

}
