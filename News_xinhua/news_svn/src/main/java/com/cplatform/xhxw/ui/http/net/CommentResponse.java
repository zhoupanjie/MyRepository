package com.cplatform.xhxw.ui.http.net;

import com.cplatform.xhxw.ui.model.CommentList;

public class CommentResponse extends BaseResponse{

	private CommentList data;

	public CommentList getData() {
		return data;
	}

	public void setData(CommentList data) {
		this.data = data;
	}

}
