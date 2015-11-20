package com.cplatform.xhxw.ui.ui.main.saas.addressBook;

import java.io.File;
import java.util.HashMap;
import java.util.List;

import com.cplatform.xhxw.ui.http.net.BaseRequest;

public class SendCompanyCircleRequest extends BaseRequest {

	private String zonetype;// int 是 圈子类型：1朋友圈，2公司圈
	private String conntenttype;// Int 是 消息类型:1普通文本，2分享链接
	private String content;// string 否 文本内容,连接地址
	private HashMap<String, File> upfile;

	public String getZonetype() {
		return zonetype;
	}

	public void setZonetype(String zonetype) {
		this.zonetype = zonetype;
	}

	public String getConntenttype() {
		return conntenttype;
	}

	public void setConntenttype(String conntenttype) {
		this.conntenttype = conntenttype;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public HashMap<String, File> getUpfile() {
		return upfile;
	}

	public void setUpfile(HashMap<String, File> upfile) {
		this.upfile = upfile;
	}

	

}
