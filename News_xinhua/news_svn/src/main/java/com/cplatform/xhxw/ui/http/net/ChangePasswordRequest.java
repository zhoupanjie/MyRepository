package com.cplatform.xhxw.ui.http.net;

public class ChangePasswordRequest extends BaseRequest {
	private String oldpwd;
	private String newpwd;
	private String secpwd;
	
	public String getOldpwd() {
		return oldpwd;
	}
	public void setOldpwd(String oldpwd) {
		this.oldpwd = oldpwd;
	}
	public String getNewpwd() {
		return newpwd;
	}
	public void setNewpwd(String newpwd) {
		this.newpwd = newpwd;
	}
	public String getSecpwd() {
		return secpwd;
	}
	public void setSecpwd(String secpwd) {
		this.secpwd = secpwd;
	}
}
