package com.cplatform.xhxw.ui.http.net;

public class RegisterRequest extends BaseRequest{

	private String account;
	private String passwd;
	private String valiedcode;
	private String regsource;
	
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
	public String getValiedcode() {
		return valiedcode;
	}
	public void setValiedcode(String valiedcode) {
		this.valiedcode = valiedcode;
	}
	public String getRegsource() {
		return regsource;
	}
	public void setRegsource(String regsource) {
		this.regsource = regsource;
	}
	
}
