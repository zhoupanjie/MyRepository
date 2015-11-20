package com.cplatform.xhxw.ui.http.net;

import com.cplatform.xhxw.ui.http.net.BaseRequest;

public class AttentionRequest extends BaseRequest {
	private String userid;// String 是 用户ID
	private String msg;// String 否 说明

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
