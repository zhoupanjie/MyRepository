package com.cplatform.xhxw.ui.http.net;

import java.util.List;

import com.cplatform.xhxw.ui.model.CommentReplyMe;

public class CommentReplyMeResponse extends BaseResponse {
	private List<CommentReplyMe> data;

	public List<CommentReplyMe> getData() {
		return data;
	}

	public void setData(List<CommentReplyMe> data) {
		this.data = data;
	}
}
