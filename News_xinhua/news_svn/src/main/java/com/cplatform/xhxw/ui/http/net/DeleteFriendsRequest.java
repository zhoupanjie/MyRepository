package com.cplatform.xhxw.ui.http.net;

import com.cplatform.xhxw.ui.http.net.BaseRequest;

public class DeleteFriendsRequest extends BaseRequest {

	private String userid;// String 是 联系人ID

	public String getUserid() {
		return userid;
	}

	public void setUserid(String userid) {
		this.userid = userid;
	}

}
