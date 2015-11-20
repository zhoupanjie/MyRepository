package com.cplatform.xhxw.ui.http.net;

import com.cplatform.xhxw.ui.model.UserInfo.EnterpriseAlias;

public class CheckAccountStatusResponse extends BaseResponse {

	private Status data;
	
	public Status getData() {
		return data;
	}

	public void setData(Status data) {
		this.data = data;
	}
	
	public class Status {
		private int status;
		private String enterpriseId;
		private String logo;
		private EnterpriseAlias aliases;

		public String getEnterprise() {
			return enterpriseId;
		}

		public void setEnterprise(String enterprise) {
			this.enterpriseId = enterprise;
		}

		public int getStatus() {
			return status;
		}

		public void setStatus(int status) {
			this.status = status;
		}

		public EnterpriseAlias getAliases() {
			return aliases;
		}

		public void setAliases(EnterpriseAlias aliases) {
			this.aliases = aliases;
		}

		public String getLogo() {
			return logo;
		}

		public void setLogo(String logo) {
			this.logo = logo;
		}
    }
}
