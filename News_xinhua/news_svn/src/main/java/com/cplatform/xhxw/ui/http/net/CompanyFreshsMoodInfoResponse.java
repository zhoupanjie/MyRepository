package com.cplatform.xhxw.ui.http.net;

import com.cplatform.xhxw.ui.model.CompanyFreshsMoodInfo;

public class CompanyFreshsMoodInfoResponse extends BaseResponse {

	private CompanyFreshsMoodInfo data;

	public CompanyFreshsMoodInfo getData() {
		return data;
	}

	public void setData(CompanyFreshsMoodInfo data) {
		this.data = data;
	}

}
