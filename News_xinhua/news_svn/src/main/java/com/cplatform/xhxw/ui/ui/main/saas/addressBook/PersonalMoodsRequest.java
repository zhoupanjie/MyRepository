package com.cplatform.xhxw.ui.ui.main.saas.addressBook;

import com.cplatform.xhxw.ui.http.net.BaseRequest;

public class PersonalMoodsRequest extends BaseRequest {

    private String userid; // 用户id

	private String typeid;// Int 否 类型：1获取最新，2获取更多 默认1
	private String ctime;// Int 否 消息时间戳: 获取最新则为最新数据的时间戳，获取更多则为最后一条消息的时间戳，默认 0
	private String offset;// Int
	
	public void setUserid(String userid) {
		this.userid = userid;
	}

    public String getUserid() {
        return userid;
    }

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
