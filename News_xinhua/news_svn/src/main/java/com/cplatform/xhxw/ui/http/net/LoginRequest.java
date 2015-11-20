package com.cplatform.xhxw.ui.http.net;

public class LoginRequest extends BaseRequest{

	private String account;
	private String passwd;
	
	public String getAccount() {
		return account;
	}
	public void setAccount(String account) {
		this.account = account;
	}
	public String getPasswd() {
		return passwd;
	}
	public void setPasswd(String passwd) {
		this.passwd = passwd;
	}
	
}
