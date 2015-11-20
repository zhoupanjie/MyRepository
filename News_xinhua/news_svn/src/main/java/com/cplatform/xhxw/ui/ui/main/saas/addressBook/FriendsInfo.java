package com.cplatform.xhxw.ui.ui.main.saas.addressBook;

import java.io.Serializable;
import java.util.List;

public class FriendsInfo implements Serializable {

	private String userid;// String 是 用户ID
	private String logo;// string 是 用户头像
	private String name;// String 是 用户姓名
	private String nickname;// String 是 用户昵称
	private String mobile;// String 是 手机号
	private String phone;// String 是 座机
	private String sign;// String 是 用户签名
	private String gender;// String 是 性别：0未定义，1男，2女
	private String email;// String 是 邮箱
	private String comment;// String 是 备注
	private String isfriend;// String 是 是否好友：0未关注，1已关注，2好友
	private List<String> thumbpics;// Array 是 最新三张新鲜事三张图

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

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getSign() {
		return sign;
	}

	public void setSign(String sign) {
		this.sign = sign;
	}

	public String getGender() {
		return gender;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	public String getIsfriend() {
		return isfriend;
	}

	public void setIsfriend(String isfriend) {
		this.isfriend = isfriend;
	}

	public List<String> getThumbpics() {
		return thumbpics;
	}

	public void setThumbpics(List<String> thumbpics) {
		this.thumbpics = thumbpics;
	}

}
