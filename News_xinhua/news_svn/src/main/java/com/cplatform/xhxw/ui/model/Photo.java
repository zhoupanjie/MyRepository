package com.cplatform.xhxw.ui.model;

/**
 * 新闻图片
 * Created by cy-love on 13-12-29.
 */
public class Photo {
    private String url;
    private String title;
    private String desc;

    public Photo(){}
    public Photo(String url, String title, String desc) {
        this.url = url;
        this.title = title;
        this.desc = desc;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }
}
