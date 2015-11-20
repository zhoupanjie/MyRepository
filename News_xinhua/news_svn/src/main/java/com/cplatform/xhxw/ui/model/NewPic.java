package com.cplatform.xhxw.ui.model;

import java.io.Serializable;

/**
 * 新闻缩略图
 * Created by cy-love on 14-1-11.
 */
public class NewPic implements Serializable {

    private String summary;
    private String thumbnail;
    /**
     * 图片类型：1，普通图片； 2，广告图片
     */
    private String type;
    private String url;

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public String getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(String thumbnail) {
        this.thumbnail = thumbnail;
    }

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}
}
