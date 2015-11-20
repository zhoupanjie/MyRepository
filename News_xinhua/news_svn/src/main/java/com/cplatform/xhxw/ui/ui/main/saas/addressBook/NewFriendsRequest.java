package com.cplatform.xhxw.ui.ui.main.saas.addressBook;

import com.cplatform.xhxw.ui.http.net.BaseRequest;

public class NewFriendsRequest extends BaseRequest {

	private String page;// Int 否 页数默认为1
	private String offset;// Int 否 返回条数默认20

	public String getPage() {
		return page;
	}

	public void setPage(String page) {
		this.page = page;
	}

	public String getOffset() {
		return offset;
	}

	public void setOffset(String offset) {
		this.offset = offset;
	}

}
