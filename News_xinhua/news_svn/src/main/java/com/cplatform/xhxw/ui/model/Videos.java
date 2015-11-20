package com.cplatform.xhxw.ui.model;

import java.io.Serializable;

/**
 * 新闻详情视频
 * Created by cy-love on 14-1-8.
 */
public class Videos implements Serializable {

    private String width;
    private String height;
    private String duration;
    private String url;
    private String thumbnail;

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

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(String thumbnail) {
        this.thumbnail = thumbnail;
    }
}
