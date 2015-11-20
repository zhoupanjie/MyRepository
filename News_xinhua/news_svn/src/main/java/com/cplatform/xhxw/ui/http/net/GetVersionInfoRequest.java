package com.cplatform.xhxw.ui.http.net;

public class GetVersionInfoRequest extends BaseRequest{

	private String device_type;//	string	是	当前设备
	private int version_no;//	int;	是	当前版本
	
	public String getDevice_type() {
		return device_type;
	}
	public void setDevice_type(String device_type) {
		this.device_type = device_type;
	}
	public int getVersion_no() {
		return version_no;
	}
	public void setVersion_no(int version_no) {
		this.version_no = version_no;
	}
	
}
