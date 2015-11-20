package com.cplatform.xhxw.ui.http.net;

public class RedEnvelopesResponse {

	private String type;
	private String url;
	private String title;
	private String content;
	private String thumb;
	private String callback;
	private String ReturnUrl;

	public String getType() {
		return type == null ? "" : type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getUrl() {
		return url == null ? "" : url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getReturnUrl() {
		return ReturnUrl == null ? "" : ReturnUrl;
	}

	public void setReturnUrl(String ReturnUrl) {
		this.ReturnUrl = ReturnUrl;
	}

	public String getTitle() {
		return title == null ? "" : title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getContent() {
		return content == null ? "" : content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getThumb() {
		return thumb == null ? "" : thumb;
	}

	public void setThumb(String thumb) {
		this.thumb = thumb;
	}

	public String getCallback() {
		return callback == null ? "" : callback;
	}

	public void setCallback(String callback) {
		this.callback = callback;
	}
}
