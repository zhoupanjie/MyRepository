package com.cplatform.xhxw.ui.http.net;

import com.cplatform.xhxw.ui.model.FeedBack;

public class FeedBackResponse extends BaseResponse{

	private FeedBack date;

	public FeedBack getDate() {
		return date;
	}

	public void setDate(FeedBack date) {
		this.date = date;
	}
	
}
