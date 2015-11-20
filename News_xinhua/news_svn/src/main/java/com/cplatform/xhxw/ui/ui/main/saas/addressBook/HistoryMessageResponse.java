package com.cplatform.xhxw.ui.ui.main.saas.addressBook;

import java.util.List;

import com.cplatform.xhxw.ui.http.net.BaseResponse;

public class HistoryMessageResponse extends BaseResponse {

	private List<HistoryMessageData> data;

	public List<HistoryMessageData> getData() {
		return data;
	}

	public void setData(List<HistoryMessageData> data) {
		this.data = data;
	}

}
