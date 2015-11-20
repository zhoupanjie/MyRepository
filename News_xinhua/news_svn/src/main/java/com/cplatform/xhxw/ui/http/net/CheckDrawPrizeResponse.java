package com.cplatform.xhxw.ui.http.net;

public class CheckDrawPrizeResponse extends BaseResponse {
	private DrawPrizeData data;

	public DrawPrizeData getData() {
		return data;
	}

	public void setData(DrawPrizeData data) {
		this.data = data;
	}
	
	public class DrawPrizeData{
		private int count;
		private String url;
		
		public int getCount() {
			return count;
		}
		public void setCount(int count) {
			this.count = count;
		}
		public String getUrl() {
			return url;
		}
		public void setUrl(String url) {
			this.url = url;
		}
	}
}
