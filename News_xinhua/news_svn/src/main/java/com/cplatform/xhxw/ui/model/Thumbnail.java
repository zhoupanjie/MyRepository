package com.cplatform.xhxw.ui.model;

import java.io.Serializable;

/**
 * 新闻推荐图
 * Created by cy-love on 14-1-13.
 */
public class Thumbnail implements Serializable {

    private String url;
    private String proportion;

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getProportion() {
        return proportion;
    }

    public void setProportion(String proportion) {
        this.proportion = proportion;
    }
}
