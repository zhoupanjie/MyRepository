package com.cplatform.xhxw.ui.http.net;

public class NewsCollectUploadResponse extends BaseResponse {

	private NewsCollectUploadResData data;
	
	public NewsCollectUploadResData getData() {
		return data;
	}
	public void setData(NewsCollectUploadResData data) {
		this.data = data;
	}
	
	
	public class NewsCollectUploadResData {
		private int callbackid;

		public int getCallbackid() {
			return callbackid;
		}

		public void setCallbackid(int callbackid) {
			this.callbackid = callbackid;
		}
	}
}
