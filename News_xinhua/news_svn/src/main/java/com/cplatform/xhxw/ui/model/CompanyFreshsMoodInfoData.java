package com.cplatform.xhxw.ui.model;

import java.util.List;

public class CompanyFreshsMoodInfoData {

	private String infoid;// ': '新鲜事ID',
	private String content;// ': '新鲜事内容',
	private String infotype;// ': '新鲜事类型',
	private String ctime;// ': '创建时间',
	private String friendtime;// ': '友好时间',
	private List<CompanyFreshsMoodInfoDataExtra> exrta;// ': {

	public String getInfoid() {
		return infoid;
	}

	public void setInfoid(String infoid) {
		this.infoid = infoid;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getInfotype() {
		return infotype;
	}

	public void setInfotype(String infotype) {
		this.infotype = infotype;
	}

	public String getCtime() {
		return ctime;
	}

	public void setCtime(String ctime) {
		this.ctime = ctime;
	}

	public String getFriendtime() {
		return friendtime;
	}

	public void setFriendtime(String friendtime) {
		this.friendtime = friendtime;
	}

	public List<CompanyFreshsMoodInfoDataExtra> getExrta() {
		return exrta;
	}

	public void setExrta(List<CompanyFreshsMoodInfoDataExtra> exrta) {
		this.exrta = exrta;
	}

}
