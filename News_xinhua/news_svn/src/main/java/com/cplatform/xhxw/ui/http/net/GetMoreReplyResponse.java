package com.cplatform.xhxw.ui.http.net;

import java.util.List;

import com.cplatform.xhxw.ui.model.ReplyComment;

public class GetMoreReplyResponse extends BaseResponse {
	private List<ReplyComment> data;

	public List<ReplyComment> getData() {
		return data;
	}

	public void setData(List<ReplyComment> data) {
		this.data = data;
	}
}
