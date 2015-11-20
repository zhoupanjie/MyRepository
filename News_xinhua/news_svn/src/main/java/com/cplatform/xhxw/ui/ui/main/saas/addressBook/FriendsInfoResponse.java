package com.cplatform.xhxw.ui.ui.main.saas.addressBook;

import com.cplatform.xhxw.ui.http.net.BaseResponse;

public class FriendsInfoResponse extends BaseResponse {

	private FriendsInfo data;

	public FriendsInfo getData() {
		return data;
	}

	public void setData(FriendsInfo data) {
		this.data = data;
	}

}
