package com.cplatform.xhxw.ui.model;

import java.util.List;

public class CompanyFreshsMoodInfo {

	/** 发布人信息 */
	private CompanyFreshsMoodInfoUserInfo userinfo;

	/** 信息详情 */
	private CompanyFreshsMoodInfoData data;

	/** 点赞人 */
	private CompanyFreshsMoodInfoParisa parisadata;

	/** 评论信息 */
	private List<CompanyFreshsMoodInfoComment> discussdata;

	/** 是否允许赞 2可取消赞，1可点赞，0不允许赞 */
	private String isparisa;// ,

	public CompanyFreshsMoodInfoUserInfo getUserinfo() {
		return userinfo;
	}

	public void setUserinfo(CompanyFreshsMoodInfoUserInfo userinfo) {
		this.userinfo = userinfo;
	}

	public CompanyFreshsMoodInfoData getData() {
		return data;
	}

	public void setData(CompanyFreshsMoodInfoData data) {
		this.data = data;
	}

	public CompanyFreshsMoodInfoParisa getParisadata() {
		return parisadata;
	}

	public void setParisadata(CompanyFreshsMoodInfoParisa parisadata) {
		this.parisadata = parisadata;
	}

	public List<CompanyFreshsMoodInfoComment> getDiscussdata() {
		return discussdata;
	}

	public void setDiscussdata(List<CompanyFreshsMoodInfoComment> discussdata) {
		this.discussdata = discussdata;
	}

	public String getIsparisa() {
		return isparisa;
	}

	public void setIsparisa(String isparisa) {
		this.isparisa = isparisa;
	}

}
