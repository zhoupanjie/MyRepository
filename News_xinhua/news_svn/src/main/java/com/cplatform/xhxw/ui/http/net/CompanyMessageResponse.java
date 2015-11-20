package com.cplatform.xhxw.ui.http.net;

import java.util.List;

import com.cplatform.xhxw.ui.http.net.BaseResponse;
import com.cplatform.xhxw.ui.ui.main.saas.addressBook.CompanyMessage;

public class CompanyMessageResponse extends BaseResponse{

	private List<CompanyMessage> data;

	public List<CompanyMessage> getData() {
		return data;
	}

	public void setData(List<CompanyMessage> data) {
		this.data = data;
	}
}
