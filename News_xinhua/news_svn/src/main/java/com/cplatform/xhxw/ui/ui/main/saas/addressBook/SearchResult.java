package com.cplatform.xhxw.ui.ui.main.saas.addressBook;

import java.io.Serializable;

public class SearchResult implements Serializable {

	private String userid;// String 是 用户ID
	private String logo;// string 是 用户头像
	private String name;// String 是 用户头像
	private String nickname;// String 是 用户昵称
	private String isfriend;// 0:非好友，1是好友

	public String getUserid() {
		return userid;
	}

	public void setUserid(String userid) {
		this.userid = userid;
	}

	public String getLogo() {
		return logo;
	}

	public void setLogo(String logo) {
		this.logo = logo;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getNickname() {
		return nickname;
	}

	public void setNickname(String nickname) {
		this.nickname = nickname;
	}

	public String getIsfriend() {
		return isfriend;
	}

	public void setIsfriend(String isfriend) {
		this.isfriend = isfriend;
	}

}
