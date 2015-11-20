package com.cplatform.xhxw.ui.http.net;

import java.io.File;
import java.util.HashMap;

public class ContributeRequest extends BaseRequest {
	private String channelid;
	private String title;
	private String shareUrl;
	private String description;
	private HashMap<String, File> picsToUpload;
	private String fileName;
	private String width;
	private String height;
	private int fileCount;
	
	public String getChannelid() {
		return channelid;
	}
	public void setChannelid(String channelid) {
		this.channelid = channelid;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getShareUrl() {
		return shareUrl;
	}
	public void setShareUrl(String shareUrl) {
		this.shareUrl = shareUrl;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public HashMap<String, File> getPicsToUpload() {
		return picsToUpload;
	}
	public void setPicsToUpload(HashMap<String, File> picsToUpload) {
		this.picsToUpload = picsToUpload;
	}
	public String getFileName() {
		return fileName;
	}
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}
	public String getWidth() {
		return width;
	}
	public void setWidth(String width) {
		this.width = width;
	}
	public String getHeight() {
		return height;
	}
	public void setHeight(String height) {
		this.height = height;
	}
	public int getFileCount() {
		return fileCount;
	}
	public void setFileCount(int fileCount) {
		this.fileCount = fileCount;
	}
}
