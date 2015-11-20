package com.cplatform.xhxw.ui.http.net;

import com.cplatform.xhxw.ui.model.FriendsFreshsMoodInfoData;

public class FriendsFreshsMoodInfoResponse extends BaseResponse {

	private FriendsFreshsMoodInfoData data;

	public FriendsFreshsMoodInfoData getData() {
		return data;
	}

	public void setData(FriendsFreshsMoodInfoData data) {
		this.data = data;
	}

}
