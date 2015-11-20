package com.cplatform.xhxw.ui.ui.main.saas.addressBook;

import java.util.List;

import com.cplatform.xhxw.ui.http.net.BaseResponse;

public class FriendsMessageResponse extends BaseResponse {

	private List<FriendsMessage> data;

	public List<FriendsMessage> getData() {
		return data;
	}

	public void setData(List<FriendsMessage> data) {
		this.data = data;
	}

}
