package com.cplatform.xhxw.ui.http.net;

public class CommentPraiseRequest extends BaseRequest{

	private int commentid;//	int	是	评论ID
	private double longitude;// 经度
	private double latitude;//纬度

	public int getCommentid() {
		return commentid;
	}

	public void setCommentid(int commentid) {
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

}
