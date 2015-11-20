package com.cplatform.xhxw.ui.ui.main.saas.addressBook;

import com.cplatform.xhxw.ui.http.net.BaseRequest;

public class SearchResultRequest extends BaseRequest {
	private String q;// String 是 查询词：姓名、昵称、电话
	private String type;// Int 否 1、姓名, 2、昵称，3、电话。默认0查询全部
	private String start;// Int 否 返回第一条记录在结果中的偏移位置。默认0从第一条返回
	private String rows;// Int 否 返回结果最多有多少条记录

	public String getQ() {
		return q;
	}

	public void setQ(String q) {
		this.q = q;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getStart() {
		return start;
	}

	public void setStart(String start) {
		this.start = start;
	}

	public String getRows() {
		return rows;
	}

	public void setRows(String rows) {
		this.rows = rows;
	}

}
