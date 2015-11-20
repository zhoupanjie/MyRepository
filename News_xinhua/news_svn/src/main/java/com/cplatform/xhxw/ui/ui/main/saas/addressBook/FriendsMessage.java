package com.cplatform.xhxw.ui.ui.main.saas.addressBook;

import java.io.Serializable;

public class FriendsMessage implements Serializable {

	private FriendsMessageUserInfo userinfo;
	private String ctimel;// ' : '创建时间' ,
	private String friendtime;// ' : '友好时间',
	private String companyid;// ' : '' ,
	private String actiontype;// ': '消息类型' , //1 赞 4评论
	private String backinfo;// ' : '消息内容' ,
	private String infodata;// ' : '新鲜事内容',
	private String infoid;// ' : '新鲜事ID'

	public FriendsMessageUserInfo getUserinfo() {
		return userinfo;
	}

	public void setUserinfo(FriendsMessageUserInfo userinfo) {
		this.userinfo = userinfo;
	}

	public String getCtimel() {
		return ctimel;
	}

	public void setCtimel(String ctimel) {
		this.ctimel = ctimel;
	}

	public String getFriendtime() {
		return friendtime;
	}

	public void setFriendtime(String friendtime) {
		this.friendtime = friendtime;
	}

	public String getCompanyid() {
		return companyid;
	}

	public void setCompanyid(String companyid) {
		this.companyid = companyid;
	}

	public String getActiontype() {
		return actiontype;
	}

	public void setActiontype(String actiontype) {
		this.actiontype = actiontype;
	}

	public String getBackinfo() {
		return backinfo;
	}

	public void setBackinfo(String backinfo) {
		this.backinfo = backinfo;
	}

	public String getInfodata() {
		return infodata;
	}

	public void setInfodata(String infodata) {
		this.infodata = infodata;
	}

	public String getInfoid() {
		return infoid;
	}

	public void setInfoid(String infoid) {
		this.infoid = infoid;
	}

}
