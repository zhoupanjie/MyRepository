package com.cplatform.xhxw.ui.http.net;

/**
 * 手机重置密码
 */
public class PhoneResetPasswordRequest extends BaseRequest{

	private String account;
	private String password;
	private String valiedcode;
	
	public String getAccount() {
		return account;
	}
	public void setAccount(String account) {
		this.account = account;
	}

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getValiedcode() {
		return valiedcode;
	}
	public void setValiedcode(String valiedcode) {
		this.valiedcode = valiedcode;
	}
	
}
