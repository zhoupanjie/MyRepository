package com.cplatform.xhxw.ui.http.net;

import com.cplatform.xhxw.ui.http.net.BaseRequest;

public class GuideUploadRequest extends BaseRequest {
	private String sex;//性别
	private String decade;// 年代
	private String profession;//所属行业
	private String preferences;// 内容喜好


	public String getSex() {
		return sex;
	}

	public void setSex(String sex) {
		this.sex = sex;
	}

	public String getDecade() {
		return decade;
	}

	public void setDecade(String decade) {
		this.decade = decade;
	}

	public String getProfession() {
		return profession;
	}

	public void setProfession(String profession) {
		this.profession = profession;
	}

	public String getPreferences() {
		return preferences;
	}

	public void setPreferences(String preferences) {
		this.preferences = preferences;
	}

}
