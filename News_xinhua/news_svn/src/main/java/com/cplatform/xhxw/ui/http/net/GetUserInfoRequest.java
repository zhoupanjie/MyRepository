package com.cplatform.xhxw.ui.http.net;

public class GetUserInfoRequest extends BaseRequest{

     private String uid;//	string	否	用户ID，如果该参数存在则使用改UID获取用户数值，否则使用Head中的userId

	public String getUid() {
		return uid;
	}

	public void setUid(String uid) {
		this.uid = uid;
	}
     
}
