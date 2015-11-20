package com.cplatform.xhxw.ui.model;

import java.io.Serializable;

/**
 * 新闻详情相关新闻
 * Created by cy-love on 14-1-25.
 */
public class Relation implements Serializable {

    private String id;
    private String title;
    private String newsType;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

	public String getNewsType() {
		return newsType;
	}

	public void setNewsType(String newsType) {
		this.newsType = newsType;
	}
}
