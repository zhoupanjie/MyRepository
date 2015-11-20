package com.cplatform.xhxw.ui.http.net;

import java.io.File;
import java.util.HashMap;

public class NewsCollectUploadRequest extends BaseRequest {
	public static final int COLLECT_TYPE_TEXT = 1;
	public static final int COLLECT_TYPE_PHOTO = 2;
	public static final int COLLECT_TYPE_AUDIO = 3;
	public static final int COLLECT_TYPE_VIDIO = 4;
	
	public static final String COLLECT_FILENAME_VIDIO_AUDIO = "upfile";
	public static final String COLLECT_FILENAME_THUMB = "thumb";
	public static final String COLLECT_FILENAME_PICS = "pics";

	private HashMap<String, File> upfile;
	private int listid;
	private String content;
	private int type;
	private int duration;
	private HashMap<String, File> thumb;
	private HashMap<String, File> pics;
	
	public HashMap<String, File> getUpfile() {
		return upfile;
	}
	public void setUpfile(HashMap<String, File> upfile) {
		this.upfile = upfile;
	}
	public int getListid() {
		return listid;
	}
	public void setListid(int listid) {
		this.listid = listid;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public int getType() {
		return type;
	}
	public void setType(int type) {
		this.type = type;
	}
	public int getDuration() {
		return duration;
	}
	public void setDuration(int duration) {
		this.duration = duration;
	}
	public HashMap<String, File> getThumb() {
		return thumb;
	}
	public void setThumb(HashMap<String, File> thumb) {
		this.thumb = thumb;
	}
	public HashMap<String, File> getPics() {
		return pics;
	}
	public void setPics(HashMap<String, File> pics) {
		this.pics = pics;
	}
}
