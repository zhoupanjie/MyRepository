package com.cplatform.xhxw.ui.http.net;

import com.cplatform.xhxw.ui.model.CareerOrBloodOptions;

public class GetUserSettingPropertiesResponse extends BaseResponse {
	private CareerOrBloodOptions data;

	public CareerOrBloodOptions getData() {
		return data;
	}

	public void setData(CareerOrBloodOptions data) {
		this.data = data;
	}
}
