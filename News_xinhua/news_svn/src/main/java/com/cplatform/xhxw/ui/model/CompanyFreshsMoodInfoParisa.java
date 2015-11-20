package com.cplatform.xhxw.ui.model;

import java.util.List;

public class CompanyFreshsMoodInfoParisa {
	
	/** 评论数量 */
	private int count;// ': 1,

	/** 评论人数据 */
	private List<CompanyFreshsMoodInfoParisaPerson> list;

	public int getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
	}

	public List<CompanyFreshsMoodInfoParisaPerson> getList() {
		return list;
	}

	public void setList(List<CompanyFreshsMoodInfoParisaPerson> list) {
		this.list = list;
	}

}
