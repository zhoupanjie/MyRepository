package com.cplatform.xhxw.ui.model;

import java.io.Serializable;

/**
 * push 解析
 * Created by cy-love on 14-3-9.
 */
public class PushMessage implements Serializable {

    private String title; // 标题 null
    private String newsId; // 新闻id
    private String description; //新闻标题
    private int newsType; //新闻类型
    private String siteId;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getNewsId() {
        return newsId;
    }

    public void setNewsId(String newsId) {
        this.newsId = newsId;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
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
