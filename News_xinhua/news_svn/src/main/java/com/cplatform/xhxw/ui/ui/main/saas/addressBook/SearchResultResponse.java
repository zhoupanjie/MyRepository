package com.cplatform.xhxw.ui.ui.main.saas.addressBook;

import java.util.List;

import com.cplatform.xhxw.ui.http.net.BaseResponse;

public class SearchResultResponse extends BaseResponse {

	private List<SearchResult> data;

	public List<SearchResult> getData() {
		return data;
	}

	public void setData(List<SearchResult> data) {
		this.data = data;
	}

}
