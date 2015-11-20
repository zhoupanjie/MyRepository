package com.cplatform.xhxw.ui.http.net;

import com.cplatform.xhxw.ui.model.CommentPraise;

public class CommentPraiseResponse extends BaseResponse{

	private CommentPraise data;

	public CommentPraise getData() {
		return data;
	}

	public void setData(CommentPraise data) {
		this.data = data;
	}
	
}
