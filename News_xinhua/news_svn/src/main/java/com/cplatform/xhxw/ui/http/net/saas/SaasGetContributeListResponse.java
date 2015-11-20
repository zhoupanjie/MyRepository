package com.cplatform.xhxw.ui.http.net.saas;

import java.util.List;

import com.cplatform.xhxw.ui.http.net.BaseResponse;

public class SaasGetContributeListResponse extends BaseResponse {
	private List<ContributeStatus> data;

	public List<ContributeStatus> getData() {
		return data;
	}

	public void setData(List<ContributeStatus> data) {
		this.data = data;
	}
	
	public class ContributeStatus {
		private String contentId;
		private String title;
		private String create_date;
		private String status;
		
		public String getContentId() {
			return contentId;
		}
		public void setContentId(String contentId) {
			this.contentId = contentId;
		}
		public String getTitle() {
			return title;
		}
		public void setTitle(String title) {
			this.title = title;
		}
		public String getCreate_date() {
			return create_date;
		}
		public void setCreate_date(String create_date) {
			this.create_date = create_date;
		}
		public String getStatus() {
			return status;
		}
		public void setStatus(String status) {
			this.status = status;
		}
	}
}
