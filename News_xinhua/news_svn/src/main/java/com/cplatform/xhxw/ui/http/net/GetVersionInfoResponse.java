package com.cplatform.xhxw.ui.http.net;

import com.cplatform.xhxw.ui.model.GetVersionInfo;

public class GetVersionInfoResponse extends BaseResponse{

	private GetVersionInfo data;

	public GetVersionInfo getData() {
		return data;
	}

	public void setData(GetVersionInfo data) {
		this.data = data;
	}
	
}
