package com.cplatform.xhxw.ui.ui.main.saas.addressBook;

import java.util.List;

import com.cplatform.xhxw.ui.http.net.BaseResponse;

public class PersonalMoodsResponse extends BaseResponse{

	private List<PersonalMoodsData> data;

	public List<PersonalMoodsData> getData() {
		return data;
	}

	public void setData(List<PersonalMoodsData> data) {
		this.data = data;
	}
	
}
