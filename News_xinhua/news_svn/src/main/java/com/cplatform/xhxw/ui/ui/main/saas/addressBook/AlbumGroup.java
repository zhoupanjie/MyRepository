package com.cplatform.xhxw.ui.ui.main.saas.addressBook;

import java.io.Serializable;

public class AlbumGroup implements Serializable{
	
	private String url;
	private boolean isChecked;

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public boolean isChecked() {
		return isChecked;
	}

	public void setChecked(boolean isChecked) {
		this.isChecked = isChecked;
	}

}
