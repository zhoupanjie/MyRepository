package com.cplatform.xhxw.ui.ui.main.saas.addressBook;

import java.io.Serializable;

public class FriendsMessageUserInfo implements Serializable {
	
	private String logo;// ' : '头像' ,
	private String userid;// ' : '用户ID' ,
	private String nickname;// ' : '用户昵称' ,
	private String name;// ' : '用户名称' ,
	private String comment;// ' : '备注'

	public String getLogo() {
		return logo;
	}

	public void setLogo(String logo) {
		this.logo = logo;
	}

	public String getUserid() {
		return userid;
	}

	public void setUserid(String userid) {
		this.userid = userid;
	}

	public String getNickname() {
		return nickname;
	}

	public void setNickname(String nickname) {
		this.nickname = nickname;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

}
