package com.cplatform.xhxw.ui.model;

/**
 * 广告模型
 * Created by cy-love on 14-3-9.
 */
public class Ad {

    private String androidimg;
    private String end;
    private String id;
    private String iosimg;
    private String rank;
    private String shorturl;
    private String title;
    /**
     * 广告类型
     * 1:文字 2:图片 3:图文
     */
    private String typeid;
    private String url;
    private String width;
    private String height;

    public String getAndroidimg() {
        return androidimg;
    }

    public void setAndroidimg(String androidimg) {
        this.androidimg = androidimg;
    }

    public String getEnd() {
        return end;
    }

    public void setEnd(String end) {
        this.end = end;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getIosimg() {
        return iosimg;
    }

    public void setIosimg(String iosimg) {
        this.iosimg = iosimg;
    }

    public String getRank() {
        return rank;
    }

    public void setRank(String rank) {
        this.rank = rank;
    }

    public String getShorturl() {
        return shorturl;
    }

    public void setShorturl(String shorturl) {
        this.shorturl = shorturl;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTypeid() {
        return typeid;
    }

    public void setTypeid(String typeid) {
        this.typeid = typeid;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
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
}
