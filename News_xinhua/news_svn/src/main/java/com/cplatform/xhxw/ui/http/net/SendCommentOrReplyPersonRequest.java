package com.cplatform.xhxw.ui.http.net;

public class SendCommentOrReplyPersonRequest extends BaseRequest{

	private String content;//	string	是	意见内容
	private int newsId;//	int	是	新闻ID
	private String uid;//	string	否	如果有则视为回复
	private String commentid;//	int	否	如果有则是为回复（回复对应的评论ID）
	private double longitude;//经度
	private double latitude;//纬度
	private String logo;
	private String nickname;
	
	//本地加的字段
	private String toname;
	
	public String getToname() {
		return toname;
	}
	public void setToname(String toname) {
		this.toname = toname;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public int getNewsId() {
		return newsId;
	}
	public void setNewsId(int newsId) {
		this.newsId = newsId;
	}
	public String getUid() {
		return uid;
	}
	public void setUid(String uid) {
		this.uid = uid;
	}
	public String getCommentid() {
		return commentid;
	}
	public void setCommentid(String commentid) {
		this.commentid = commentid;
	}
	public double getLongitude() {
		return longitude;
	}
	public void setLongitude(double longitude) {
		this.longitude = longitude;
	}
	public double getLatitude() {
		return latitude;
	}
	public void setLatitude(double latitude) {
		this.latitude = latitude;
	}
	public String getLogo() {
		return logo;
	}
	public void setLogo(String logo) {
		this.logo = logo;
	}
	public String getNickname() {
		return nickname;
	}
	public void setNickname(String nickname) {
		this.nickname = nickname;
	}
}
