package com.cplatform.xhxw.ui.http.net;

import java.util.List;

import com.cplatform.xhxw.ui.model.HotSearchWord;

public class HotSearchWordResponse extends BaseResponse {
	private List<HotSearchWord> data;

	public List<HotSearchWord> getData() {
		return data;
	}

	public void setData(List<HotSearchWord> data) {
		this.data = data;
	}
}
