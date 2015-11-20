package com.cplatform.xhxw.ui.ui.main.saas.addressBook;

import java.io.Serializable;

public class HistoryMessageData implements Serializable {
	
	private HistoryMessageUserInfo userinfo;// ": {},
	private String ctime;// ": "1408265467",//发送时间戳
	private String friendtime;// ": "2天前",//友好时间
	private String companyid;// ": "101",//公司ID
	private String actiontype;// ": "4",//行为类型：1赞，2踩，3收藏,4评论，5创建
	private String backinfo;// ": "哈哈哈",//回复信息
	private String infodata;// ": "来了来了连连水电费是否是否？？？？",//消息内容
	private String infoid;// ": "349f1afc711b53667efbc82106bc4ef4",//信息ID
	private String newstype;// ": 1//1公司新鲜事 2个人新鲜事

	public HistoryMessageUserInfo getUserinfo() {
		return userinfo;
	}

	public void setUserinfo(HistoryMessageUserInfo userinfo) {
		this.userinfo = userinfo;
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

	public String getNewstype() {
		return newstype;
	}

	public void setNewstype(String newstype) {
		this.newstype = newstype;
	}

}
