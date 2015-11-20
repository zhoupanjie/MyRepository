package com.cplatform.xhxw.ui.model;

import java.util.List;

public class FriendsFreshsMoodInfoData {

	private String infoid;// ' : 'aa' , // 新鲜事ID
	private String contenttype;// : 1 , // 内容类型
	private String content;// : 'hh' , // 内容详情
	private String ctime;// : '1400000000' , // 发布时间
	private String ftime;// : '刚刚', //友好时间
	private List<FriendsFreshsMoodInfoDataExtra> exrta;// 内容扩展信息
	private String parisa;// : 0,//是否已赞 1是 0否
	private String isparisa;// : '1',//是否可点赞 1是 2否
	private String iscomment;// : '1', // 是否可评论，1是，2否

	/** 新鲜事发布者人信息 */
	private FriendsFreshsMoodInfoDataUserInfo userinfo;

	/** 赞 */
	private FriendsFreshsMoodInfoDataPraise parisadata;

	/** 评论数据 */
	private List<FriendsFreshsMoodInfoDataComment> commentdata;

	public String getInfoid() {
		return infoid;
	}

	public void setInfoid(String infoid) {
		this.infoid = infoid;
	}

	public String getContenttype() {
		return contenttype;
	}

	public void setContenttype(String contenttype) {
		this.contenttype = contenttype;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getCtime() {
		return ctime;
	}

	public void setCtime(String ctime) {
		this.ctime = ctime;
	}

	public String getFtime() {
		return ftime;
	}

	public void setFtime(String ftime) {
		this.ftime = ftime;
	}

	public List<FriendsFreshsMoodInfoDataExtra> getExrta() {
		return exrta;
	}

	public void setExrta(List<FriendsFreshsMoodInfoDataExtra> exrta) {
		this.exrta = exrta;
	}

	public String getParisa() {
		return parisa;
	}

	public void setParisa(String parisa) {
		this.parisa = parisa;
	}

	public String getIsparisa() {
		return isparisa;
	}

	public void setIsparisa(String isparisa) {
		this.isparisa = isparisa;
	}

	public String getIscomment() {
		return iscomment;
	}

	public void setIscomment(String iscomment) {
		this.iscomment = iscomment;
	}

	public FriendsFreshsMoodInfoDataUserInfo getUserinfo() {
		return userinfo;
	}

	public void setUserinfo(FriendsFreshsMoodInfoDataUserInfo userinfo) {
		this.userinfo = userinfo;
	}

	public FriendsFreshsMoodInfoDataPraise getParisadata() {
		return parisadata;
	}

	public void setParisadata(FriendsFreshsMoodInfoDataPraise parisadata) {
		this.parisadata = parisadata;
	}

	public List<FriendsFreshsMoodInfoDataComment> getCommentdata() {
		return commentdata;
	}

	public void setCommentdata(
			List<FriendsFreshsMoodInfoDataComment> commentdata) {
		this.commentdata = commentdata;
	}

}
