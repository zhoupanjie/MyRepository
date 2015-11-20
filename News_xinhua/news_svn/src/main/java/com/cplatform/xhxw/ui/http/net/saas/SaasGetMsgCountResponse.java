package com.cplatform.xhxw.ui.http.net.saas;

import com.cplatform.xhxw.ui.http.net.BaseResponse;

public class SaasGetMsgCountResponse extends BaseResponse {
	private MsgCountModel data;
	
	public MsgCountModel getData() {
		return data;
	}

	public void setData(MsgCountModel data) {
		this.data = data;
	}
	
	public class MsgCountModel {
		private int usermsg;
		private int backcomment;
		
		public int getUsermsg() {
			return usermsg;
		}
		public void setUsermsg(int usermsg) {
			this.usermsg = usermsg;
		}
		public int getBackcomment() {
			return backcomment;
		}
		public void setBackcomment(int backcomment) {
			this.backcomment = backcomment;
		}
	}
}
