package com.cplatform.xhxw.ui.ui.main.saas.addressBook;

import com.cplatform.xhxw.ui.http.net.BaseRequest;

public class SendFriendsVerifyRequest extends BaseRequest {

	private String userid;// String 是 用户ID
	private String msg;// String 否 申请信息

	public String getUserid() {
		return userid;
	}

	public void setUserid(String userid) {
		this.userid = userid;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

}
