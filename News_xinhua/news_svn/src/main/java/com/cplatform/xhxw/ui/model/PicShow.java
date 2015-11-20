package com.cplatform.xhxw.ui.model;

import java.io.Serializable;
import java.util.List;

/**
 * 图片显示模型（用于PicShowAtivity）
 * Created by cy-love on 14-2-16.
 */
public class PicShow implements Serializable {

    private String newsId;
    private String title;
    private String commentCount;
    private boolean isComment;
    private List<NewPic> pics;
    private List<RecommendAtlas> RecommendAtlas;

    public PicShow(String t) {

    }

    public String getNewsId() {
        return newsId;
    }

    public void setNewsId(String newsId) {
        this.newsId = newsId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public List<NewPic> getPics() {
        return pics;
    }

    public void setPics(List<NewPic> pics) {
        this.pics = pics;
    }

    public String getCommentCount() {
        return commentCount;
    }

    public void setCommentCount(String commentCount) {
        this.commentCount = commentCount;
    }

	public boolean isComment() {
		return isComment;
	}

	public void setComment(boolean isComment) {
		this.isComment = isComment;
	}

	public List<RecommendAtlas> getRecommendAtlas() {
		return RecommendAtlas;
	}

	public void setRecommendAtlas(List<RecommendAtlas> recommendAtlas) {
		RecommendAtlas = recommendAtlas;
	}

	
}
