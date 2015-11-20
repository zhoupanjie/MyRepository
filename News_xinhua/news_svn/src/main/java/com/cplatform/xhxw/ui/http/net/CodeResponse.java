package com.cplatform.xhxw.ui.http.net;

public class CodeResponse extends BaseResponse{

	private Code data;

    public Code getData() {
		return data;
	}

	public void setData(Code data) {
		this.data = data;
	}

	public class Code {
		String tips;

		public String getTips() {
			return tips;
		}

		public void setTips(String tips) {
			this.tips = tips;
		}
    }
}
