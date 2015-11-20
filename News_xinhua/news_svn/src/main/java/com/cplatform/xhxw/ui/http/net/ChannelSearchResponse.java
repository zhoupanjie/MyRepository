package com.cplatform.xhxw.ui.http.net;

import java.util.List;

import com.cplatform.xhxw.ui.model.Channe;

public class ChannelSearchResponse extends BaseResponse {
	private ChannelSearchData data;
	
	public ChannelSearchData getData() {
		return data;
	}

	public void setData(ChannelSearchData data) {
		this.data = data;
	}

	public class ChannelSearchData {
		private String keyword;
		private List<Channe> list;
		
		public String getKeyword() {
			return keyword;
		}
		public void setKeyword(String keyword) {
			this.keyword = keyword;
		}
		public List<Channe> getList() {
			return list;
		}
		public void setList(List<Channe> list) {
			this.list = list;
		}
	}
}
