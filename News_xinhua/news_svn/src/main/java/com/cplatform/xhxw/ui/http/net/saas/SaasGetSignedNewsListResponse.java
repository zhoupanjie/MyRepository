package com.cplatform.xhxw.ui.http.net.saas;

import java.util.List;

import com.cplatform.xhxw.ui.http.net.BaseResponse;

public class SaasGetSignedNewsListResponse extends BaseResponse {

	private List<SignedNewsStatus> data;

	public List<SignedNewsStatus> getData() {
		return data;
	}

	public void setData(List<SignedNewsStatus> data) {
		this.data = data;
	}
	
	public class SignedNewsStatus {
		private String newsId;
		private String title;
		private int newsType;
		private String summary;
		private String liveurl;
		
		public String getNewsId() {
			return newsId;
		}
		public void setNewsId(String newsId) {
			this.newsId = newsId;
		}
		public String getTitle() {
			return title;
		}
		public void setTitle(String title) {
			this.title = title;
		}
		public int getNewsType() {
			return newsType;
		}
		public void setNewsType(int newsType) {
			this.newsType = newsType;
		}
		public String getSummary() {
			return summary;
		}
		public void setSummary(String summary) {
			this.summary = summary;
		}
		public String getLiveurl() {
			return liveurl;
		}
		public void setLiveurl(String liveurl) {
			this.liveurl = liveurl;
		}
		
	}
}
