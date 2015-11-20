package com.cplatform.xhxw.ui.model;

/**
 * push json
 * Created by cy-love on 14-2-9.
 */
public class PushNotification {

    private String newsId;
    private int newsType;
    private String siteId;

    public String getNewsId() {
        return newsId;
    }

    public void setNewsId(String newsId) {
        this.newsId = newsId;
    }

    public int getNewsType() {
        return newsType;
    }

    public void setNewsType(int newsType) {
        this.newsType = newsType;
    }

	public String getSiteId() {
		return siteId;
	}

	public void setSiteId(String siteId) {
		this.siteId = siteId;
	}
}
