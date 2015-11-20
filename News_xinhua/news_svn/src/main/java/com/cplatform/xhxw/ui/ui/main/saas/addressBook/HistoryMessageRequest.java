package com.cplatform.xhxw.ui.ui.main.saas.addressBook;

import com.cplatform.xhxw.ui.http.net.BaseRequest;

public class HistoryMessageRequest extends BaseRequest {

	private String typeid; // Int 否 类型：0获取最新，1获取更多 默认0
	private String ctime; // Int 否 消息时间戳: 获取最新则为最新数据的时间戳，获取更多则为最后一条消息的时间戳，默认 0
	private String offset; // Int 否 获取条数 0则获取所有，默认0

	public String getTypeid() {
		return typeid;
	}

	public void setTypeid(String typeid) {
		this.typeid = typeid;
	}

	public String getCtime() {
		return ctime;
	}

	public void setCtime(String ctime) {
		this.ctime = ctime;
	}

	public String getOffset() {
		return offset;
	}

	public void setOffset(String offset) {
		this.offset = offset;
	}

}
