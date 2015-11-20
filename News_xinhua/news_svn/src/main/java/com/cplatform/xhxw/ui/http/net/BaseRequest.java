package com.cplatform.xhxw.ui.http.net;

public class BaseRequest {
	private boolean saasRequest;
    private boolean devRequest;

	public boolean getSaasRequest() {
		return saasRequest;
	}

	public void setSaasRequest(boolean saasRequest) {
		this.saasRequest = saasRequest;
	}

    public boolean isDevRequest() {
        return devRequest;
    }

    public void setDevRequest(boolean devRequest) {
        this.devRequest = devRequest;
    }
}
