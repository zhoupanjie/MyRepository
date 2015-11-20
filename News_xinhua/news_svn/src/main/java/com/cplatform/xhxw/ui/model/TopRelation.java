package com.cplatform.xhxw.ui.model;

import java.io.Serializable;

/**
 * 
 * @ClassName TopRelation 新闻详情顶部相关新闻
 * @Description TODO 
 * @Version 1.0
 * @Author Xing'En
 * @Creation 2015年4月24日 下午4:04:38 
 * @Mender Xing'En
 * @Modification 2015年4月24日 下午4:04:38 
 * @Copyright Copyright © 2012 - 2015 Petro-CyberWorks Information Technology Company Limlted.All Rights Reserved.
*
 */
public class TopRelation implements Serializable {

    private String id;
    private String title;
    private String newsType;
    private String url;
    private boolean isYaoWen;
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

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public boolean isYaoWen() {
		return isYaoWen;
	}

	public void setYaoWen(boolean isYaoWen) {
		this.isYaoWen = isYaoWen;
	}
	
}
