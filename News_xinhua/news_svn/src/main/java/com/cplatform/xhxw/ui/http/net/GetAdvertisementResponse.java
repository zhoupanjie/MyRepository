package com.cplatform.xhxw.ui.http.net;

import com.cplatform.xhxw.ui.model.AdvertismentsResponse;

public class GetAdvertisementResponse extends BaseResponse {
	
	private AdvertismentsResponse data;

	public AdvertismentsResponse getData() {
		return data;
	}

	public void setData(AdvertismentsResponse data) {
		this.data = data;
	}
}
