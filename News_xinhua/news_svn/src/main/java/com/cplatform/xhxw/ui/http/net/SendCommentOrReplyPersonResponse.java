package com.cplatform.xhxw.ui.http.net;

import com.cplatform.xhxw.ui.model.SendCommentOrReplyPerson;

public class SendCommentOrReplyPersonResponse extends BaseResponse{

	private SendCommentOrReplyPerson data;

	public SendCommentOrReplyPerson getData() {
		return data;
	}

	public void setData(SendCommentOrReplyPerson data) {
		this.data = data;
	}

}
