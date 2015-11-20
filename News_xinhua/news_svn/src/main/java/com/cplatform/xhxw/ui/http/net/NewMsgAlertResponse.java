package com.cplatform.xhxw.ui.http.net;

import com.cplatform.xhxw.ui.model.UserNewMsgAlert;

public class NewMsgAlertResponse extends BaseResponse {
	private UserNewMsgAlert data;

	public UserNewMsgAlert getData() {
		return data;
	}

	public void setData(UserNewMsgAlert data) {
		this.data = data;
	}
}
