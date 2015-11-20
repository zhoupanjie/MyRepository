package com.cplatform.xhxw.ui.http.net;

public class DeleteCompanyFreshsMoodRequest extends BaseRequest {

	private String infoid;// string 是 新鲜事ID
	private String infouserid;// string 是 新鲜事创建者USERID

	public String getInfoid() {
		return infoid;
	}

	public void setInfoid(String infoid) {
		this.infoid = infoid;
	}

	public String getInfouserid() {
		return infouserid;
	}

	public void setInfouserid(String infouserid) {
		this.infouserid = infouserid;
	}

}
