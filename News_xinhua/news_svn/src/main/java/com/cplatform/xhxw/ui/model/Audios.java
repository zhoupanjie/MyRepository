package com.cplatform.xhxw.ui.model;

import java.io.Serializable;

/**
 * 新闻详情音频
 * Created by cy-love on 14-1-8.
 */
public class Audios implements Serializable {

    private String duration;
    private String url;

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
}
