package com.cplatform.xhxw.ui.ui.main.saas.addressBook;

import java.io.Serializable;

public class NewFriends implements Serializable {

	// private String userid;// "123",
	// private String logo;//
	// "http://static.saas.feedss.com/11/22/1122aasdfad.jpg",
	// private String name;// "姓名1",
	// private String nickname;// "昵称1"
	private String statues = "1";// 1 添加 2 等待验证 3已添加 4接受

	private String infoid;// ": "8afeebee94e8368aef2df026e43a59e0",//信息ID
	private String userid;// ": "ed9e53ec80e611e39bf8001a6467c5a1",//用户ID
	private String logo;// ": "",//LOGO
	private String name;// ": "张琦",//名称
	private String nickname;// ": "",//昵称
	private String rename;// ": "",//昵称
	private String msg;// ": "你好",//请求信息
	private String ctime;// ": "1407741832"//请求时间戳
	private String myUserId;// ": "1407741832"//请求时间戳
	
	public String getRename() {
		return rename;
	}

	public void setRename(String rename) {
		this.rename = rename;
	}

	public String getMyUserId() {
		return myUserId;
	}

	public void setMyUserId(String myUserId) {
		this.myUserId = myUserId;
	}

	public String getStatues() {
		return statues;
	}

	public void setStatues(String statues) {
		this.statues = statues;
	}

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

	public String getInfoid() {
		return infoid;
	}

	public void setInfoid(String infoid) {
		this.infoid = infoid;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	public String getCtime() {
		return ctime;
	}

	public void setCtime(String ctime) {
		this.ctime = ctime;
	}

}
