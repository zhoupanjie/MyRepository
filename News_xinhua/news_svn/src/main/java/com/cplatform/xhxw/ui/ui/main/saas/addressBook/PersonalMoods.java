package com.cplatform.xhxw.ui.ui.main.saas.addressBook;

import java.io.Serializable;
import java.util.List;

public class PersonalMoods implements Serializable {

	private String infoid;// ": "5cf083cbd11e407d5ba735fb553b8411",//信息ID
	private String companyid;// ": "101",//公司ID
	private String ctime;// ": "1408781897",//发表时间戳
	private String content;// ": "",//发表内容
	private String infotype;// ": 1,//1文本，2图片，3图文，4视频，5分享连接
	private List<PersonalMoodsUrl> exrta;// ": [
	private String showtime;

	public String getShowtime() {
		return showtime;
	}

	public void setShowtime(String showtime) {
		this.showtime = showtime;
	}

	public String getInfoid() {
		return infoid;
	}

	public void setInfoid(String infoid) {
		this.infoid = infoid;
	}

	public String getCompanyid() {
		return companyid;
	}

	public void setCompanyid(String companyid) {
		this.companyid = companyid;
	}

	public String getCtime() {
		return ctime;
	}

	public void setCtime(String ctime) {
		this.ctime = ctime;
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

	public List<PersonalMoodsUrl> getExrta() {
		return exrta;
	}

	public void setExrta(List<PersonalMoodsUrl> exrta) {
		this.exrta = exrta;
	}

}
