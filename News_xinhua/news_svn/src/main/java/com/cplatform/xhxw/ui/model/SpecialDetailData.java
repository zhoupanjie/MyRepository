package com.cplatform.xhxw.ui.model;

import java.io.Serializable;
import java.util.List;

/**
 * 专题详情
 * Created by cy-love on 14-1-10.
 */
public class SpecialDetailData implements Serializable {

    private boolean _showHeadler; // 是否显示标题
    private boolean isRead; // 是否已读
    private String grouptitle; // 父类标题
    private int specialmodeltype; //专题类型;
    private String newsId;
    private String title;
    private String summary;
    private String img;
    private int orders;
    private String videoimg;
    private String videourl;
    private String friendTime; // 时间
    private int newsType;
    private List<NewPic> picInfo; // 图片新闻的内容
    private String commentcount;
    private String iscomment;//0:允许，1：不允许

    // -----推荐图 start -----
    private String specialimage;
    private int height;
    private int width;
    // ---- 推荐图 end ----
    
    private String liveurl;//直播wap页面地址

    public String getNewsId() {
        return newsId;
    }

    public String getIscomment() {
		return iscomment;
	}

	public void setIscomment(String iscomment) {
		this.iscomment = iscomment;
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

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public int getOrders() {
        return orders;
    }

    public void setOrders(int orders) {
        this.orders = orders;
    }

    public String getGrouptitle() {
        return grouptitle;
    }

    public void setGrouptitle(String grouptitle) {
        this.grouptitle = grouptitle;
    }

    public int getSpecialmodeltype() {
        return specialmodeltype;
    }

    public void setSpecialmodeltype(int specialmodeltype) {
        this.specialmodeltype = specialmodeltype;
    }

    public boolean isRead() {
        return isRead;
    }

    public void setRead(boolean isRead) {
        this.isRead = isRead;
    }

    public String getVideoimg() {
        return videoimg;
    }

    public void setVideoimg(String videoimg) {
        this.videoimg = videoimg;
    }

    public String getVideourl() {
        return videourl;
    }

    public void setVideourl(String videourl) {
        this.videourl = videourl;
    }

    public String getSpecialimage() {
        return specialimage;
    }

    public void setSpecialimage(String specialimage) {
        this.specialimage = specialimage;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public String getFriendTime() {
        return friendTime;
    }

    public void setFriendTime(String friendTime) {
        this.friendTime = friendTime;
    }

    public int getNewsType() {
        return newsType;
    }

    public void setNewsType(int newsType) {
        this.newsType = newsType;
    }

    public List<NewPic> getPicInfo() {
        return picInfo;
    }

    public void setPicInfo(List<NewPic> picInfo) {
        this.picInfo = picInfo;
    }

    public String getCommentcount() {
        return commentcount;
    }

    public void setCommentcount(String commentcount) {
        this.commentcount = commentcount;
    }

    public boolean is_showHeadler() {
        return _showHeadler;
    }

    public void set_showHeadler(boolean _showHeadler) {
        this._showHeadler = _showHeadler;
    }

	public String getLiveurl() {
		return liveurl;
	}

	public void setLiveurl(String liveurl) {
		this.liveurl = liveurl;
	}
}
