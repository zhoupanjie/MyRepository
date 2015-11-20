package com.cplatform.xhxw.ui.http.net.saas;

import com.cplatform.xhxw.ui.http.net.BaseRequest;

public class SaasSetUserinfoRequest extends BaseRequest {
	private String staff_landline;
	private String staff_signature;
	private String logo;//	string	否	头像，如果没有值不作处理base64_encode()
	private String nickname;//	string	否	昵称，如果没有不作处理
	private String sex;
	
	public String getStaff_landline() {
		return staff_landline;
	}
	public void setStaff_landline(String staff_landline) {
		this.staff_landline = staff_landline;
	}
	public String getStaff_signature() {
		return staff_signature;
	}
	public void setStaff_signature(String staff_signature) {
		this.staff_signature = staff_signature;
	}
	public String getLogo() {
		return logo;
	}
	public void setLogo(String logo) {
		this.logo = logo;
	}
	public String getNickname() {
		return nickname;
	}
	public void setNickname(String nickname) {
		this.nickname = nickname;
	}
	public String getSex() {
		return sex;
	}
	public void setSex(String sex) {
		this.sex = sex;
	}
}
