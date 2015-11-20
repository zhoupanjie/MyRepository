package com.cplatform.xhxw.ui.model;

import java.util.List;

public class FriendsFreshsMoodInfoDataPraise {

	private int count;// ' : '1' , // 赞的数
	private List<FriendsFreshsMoodInfoDataPraisePersonInfo> list;// 赞的人的数据

	public int getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
	}

	public List<FriendsFreshsMoodInfoDataPraisePersonInfo> getList() {
		return list;
	}

	public void setList(List<FriendsFreshsMoodInfoDataPraisePersonInfo> list) {
		this.list = list;
	}

}
