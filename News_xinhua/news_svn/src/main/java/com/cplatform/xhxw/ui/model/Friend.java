package com.cplatform.xhxw.ui.model;

/**
 * 上传通讯录
 */
public class Friend {

    private String userid;
    private String name;
    private String sname;
    private String phone;
    private String status;
	
    public String getUserid() {
        return userid;
    }

	public void setUserid(String userid) {
		this.userid = userid;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getSname() {
		return sname;
	}

	public void setSname(String sname) {
		this.sname = sname;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}
}
