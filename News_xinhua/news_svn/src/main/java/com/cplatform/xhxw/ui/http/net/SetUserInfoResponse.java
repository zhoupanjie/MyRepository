package com.cplatform.xhxw.ui.http.net;

import com.cplatform.xhxw.ui.model.SetUserInfo;

public class SetUserInfoResponse extends BaseResponse{

	private SetUserInfo data;

	public SetUserInfo getData() {
		return data;
	}

	public void setData(SetUserInfo data) {
		this.data = data;
	}
	
}
