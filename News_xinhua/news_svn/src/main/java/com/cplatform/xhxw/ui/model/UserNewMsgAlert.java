package com.cplatform.xhxw.ui.model;

import java.util.List;

public class UserNewMsgAlert {
	private int newMsgNum;
	private List<NewMsgInfo> data;
	
	public int getNewMsgNum() {
		return newMsgNum;
	}

	public void setNewMsgNum(int newMsgNum) {
		this.newMsgNum = newMsgNum;
	}

	public List<NewMsgInfo> getData() {
		return data;
	}

	public void setData(List<NewMsgInfo> data) {
		this.data = data;
	}

	class NewMsgInfo {
		private String name;
		private int num;
		
		public String getName() {
			return name;
		}
		public void setName(String name) {
			this.name = name;
		}
		public int getNum() {
			return num;
		}
		public void setNum(int num) {
			this.num = num;
		}
	}
}
