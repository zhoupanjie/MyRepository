package com.cplatform.xhxw.ui.model;

import java.io.Serializable;

/**
 * 新闻详情热点评论
 * Created by cy-love on 14-1-25.
 */
public class HotComments implements Serializable {

    private String content;
    private String id;
    private String logo;
    private String newsId;
    private String newsTitle;
    private String newsUrl;
    private String praise;
    private String praiseCount;
    private String reciverId;
    private String rlogo;
    private String rnickName;
    private String senderId;
    private String snickName;

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getLogo() {
        return logo;
    }

    public void setLogo(String logo) {
        this.logo = logo;
    }

    public String getNewsId() {
        return newsId;
    }

    public void setNewsId(String newsId) {
        this.newsId = newsId;
    }

    public String getNewsTitle() {
        return newsTitle;
    }

    public void setNewsTitle(String newsTitle) {
        this.newsTitle = newsTitle;
    }

    public String getNewsUrl() {
        return newsUrl;
    }

    public void setNewsUrl(String newsUrl) {
        this.newsUrl = newsUrl;
    }

    public String getPraise() {
        return praise;
    }

    public void setPraise(String praise) {
        this.praise = praise;
    }

    public String getPraiseCount() {
        return praiseCount;
    }

    public void setPraiseCount(String praiseCount) {
        this.praiseCount = praiseCount;
    }

    public String getReciverId() {
        return reciverId;
    }

    public void setReciverId(String reciverId) {
        this.reciverId = reciverId;
    }

    public String getRlogo() {
        return rlogo;
    }

    public void setRlogo(String rlogo) {
        this.rlogo = rlogo;
    }

    public String getRnickName() {
        return rnickName;
    }

    public void setRnickName(String rnickName) {
        this.rnickName = rnickName;
    }

    public String getSenderId() {
        return senderId;
    }

    public void setSenderId(String senderId) {
        this.senderId = senderId;
    }

    public String getSnickName() {
        return snickName;
    }

    public void setSnickName(String snickName) {
        this.snickName = snickName;
    }
}
