package com.cplatform.xhxw.ui.model;

public class GetVersionInfo {

	/**服务器返回的数据全是String类型*/
	
	private String device_type;//	string	是	当前设备(iphone, ipad, android_phone, android_pad)
	private int version_no;//	int	是	最新版本
	private String update_info;//	string	是	更新信息
	private String down_url;//	int	是	下载地址
	private String update_type;//	int	是	强制更新是1，默认是0
	private String version;// string "v3.0.0",
	
	public String getVersion() {
		return version;
	}
	public void setVersion(String version) {
		this.version = version;
	}
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
	public String getUpdate_info() {
		return update_info;
	}
	public void setUpdate_info(String update_info) {
		this.update_info = update_info;
	}
	public String getDown_url() {
		return down_url;
	}
	public void setDown_url(String down_url) {
		this.down_url = down_url;
	}
	public String getUpdate_type() {
		return update_type;
	}
	public void setUpdate_type(String update_type) {
		this.update_type = update_type;
	}
	
}
