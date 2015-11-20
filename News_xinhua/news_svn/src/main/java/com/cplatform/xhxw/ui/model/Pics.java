package com.cplatform.xhxw.ui.model;

import java.io.Serializable;

/**
 * 新闻详情图片
 * Created by cy-love on 14-1-8.
 */
public class Pics implements Serializable {

    private String width;
    private String height;
    private String url;
    private String summary;
    private String type;
    private String adurl;

    public String getAdurl() {
		return adurl;
	}

	public void setAdurl(String adurl) {
		this.adurl = adurl;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
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

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }
}
