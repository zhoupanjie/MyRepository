package com.cplatform.xhxw.ui.http.net;

import com.cplatform.xhxw.ui.model.Channe;

public class ChannelSearchAddResponse extends BaseResponse {
	private ChannelSearchAddResponseData data;

	public ChannelSearchAddResponseData getData() {
		return data;
	}

	public void setData(ChannelSearchAddResponseData data) {
		this.data = data;
	}
	
	public class ChannelSearchAddResponseData {
		private Channe list;

		public Channe getList() {
			return list;
		}

		public void setList(Channe list) {
			this.list = list;
		}
	}
}
