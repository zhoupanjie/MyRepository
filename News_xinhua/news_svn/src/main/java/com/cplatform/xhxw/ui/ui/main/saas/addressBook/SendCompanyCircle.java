package com.cplatform.xhxw.ui.ui.main.saas.addressBook;

import java.io.Serializable;
import java.util.List;

public class SendCompanyCircle implements Serializable {
	private String infoid;// : "1111",
	private String ctime;// : "1400000000",
	private List<SendCompanyCircleExtra> exrta;// " : {}

	public String getInfoid() {
		return infoid;
	}

	public void setInfoid(String infoid) {
		this.infoid = infoid;
	}

	public String getCtime() {
		return ctime;
	}

	public void setCtime(String ctime) {
		this.ctime = ctime;
	}

	public List<SendCompanyCircleExtra> getExrta() {
		return exrta;
	}

	public void setExrta(List<SendCompanyCircleExtra> exrta) {
		this.exrta = exrta;
	}

}
