package com.cplatform.xhxw.ui.ui.main.saas.addressBook;

import java.util.List;

import com.cplatform.xhxw.ui.http.net.BaseResponse;

public class NewFriendsResponse extends BaseResponse {
	
	private List<NewFriends> data;

	public List<NewFriends> getData() {
		return data;
	}

	public void setData(List<NewFriends> data) {
		this.data = data;
	}

}
