package com.cplatform.xhxw.ui.model;

import java.io.Serializable;

public class Comment implements Serializable {

	private String id;// 56,
	private String newsId;// 871,
	private String content;// "你是谁",
	private String senderId;// "a713ad4d915d11e39bf8001a6467c5a0",
	private String reciverId;// "",
	private String newsUrl;// "http://cms.xw.feedss.com/index.php?m=content&c=index&a=show&catid=34&id=871",
	private String newsTitle;// "李娜完胜布沙尔四年三进决赛",
	private String snickName;// "4***@qq.com",
	private String rnickName;// "",
	private String praiseCount;// 0,
	private String published;// 1391932887,
	private String logo;// "http://api.xw.feedss.com/upload/app/a/7/a713ad4d915d11e39bf8001a6467c5a0.jpg",
	private String rlogo;// "",
	private String praise;// 1未赞 2已赞
	private String type;

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getNewsId() {
		return newsId;
	}

	public void setNewsId(String newsId) {
		this.newsId = newsId;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getSenderId() {
		return senderId;
	}

	public void setSenderId(String senderId) {
		this.senderId = senderId;
	}

	public String getReciverId() {
		return reciverId;
	}

	public void setReciverId(String reciverId) {
		this.reciverId = reciverId;
	}

	public String getNewsUrl() {
		return newsUrl;
	}

	public void setNewsUrl(String newsUrl) {
		this.newsUrl = newsUrl;
	}

	public String getNewsTitle() {
		return newsTitle;
	}

	public void setNewsTitle(String newsTitle) {
		this.newsTitle = newsTitle;
	}

	public String getSnickName() {
		return snickName;
	}

	public void setSnickName(String snickName) {
		this.snickName = snickName;
	}

	public String getRnickName() {
		return rnickName;
	}

	public void setRnickName(String rnickName) {
		this.rnickName = rnickName;
	}

	public String getPraiseCount() {
		return praiseCount;
	}

	public void setPraiseCount(String praiseCount) {
		this.praiseCount = praiseCount;
	}

	public String getPublished() {
		return published;
	}

	public void setPublished(String published) {
		this.published = published;
	}

	public String getLogo() {
		return logo;
	}

	public void setLogo(String logo) {
		this.logo = logo;
	}

	public String getRlogo() {
		return rlogo;
	}

	public void setRlogo(String rlogo) {
		this.rlogo = rlogo;
	}

	public String getPraise() {
		return praise;
	}

	public void setPraise(String praise) {
		this.praise = praise;
	}

}
