package com.cplatform.xhxw.ui.model;

import java.io.Serializable;
import java.util.List;

/**
 * Created by cy-love on 13-12-25.
 */
public class New implements Serializable {
	public static final int LIST_STYLE_NORMAL = 0;
	public static final int LIST_STYLE_NO_SUMMARY = 1;
	public static final int LIST_STYLE_LONG_TITLE = 2;
	public static final int LIST_STYLE_PICS = 3;

    private boolean isRead; // 已读
    private String bigthumbnail;
    private int footType;
    private List<String> minipics;
    private String newsId;
    private String specialId;
    private int newsType;
    private String newsUrl;
    private String orders;
    private int showType;
    private String summary;
    private String thumbnail;
    private String title;
    private String created;
    private String updated;
    private String published;
    private List<NewPic> picInfo;
    private String commentcount;
    private String videourl;
    private String iscomment;//0：允许，1：不允许
    private String liveurl;//直播wap页面地址
    private String isread;//后台记录的已读未读状态
    private int listStyle; //列表栏目显示类型
    private String showSource; //新闻来源
    private int liveType;
    private int signStatus; //圈阅状态 0：未圈阅， 1：已圈阅
    private String shareUrl;
    private int typeId; //1:普通新闻 2:置顶新闻
    private int width; //thumbnail图片宽度
    private int height; //thumbnail图片高度

    public int getWidth() {
		return width;
	}

	public void setWidth(int width) {
		this.width = width;
	}

	public int getHeight() {
		return height;
	}

	public void setHeight(int height) {
		this.height = height;
	}

	public String getIscomment() {
		return iscomment;
	}

	public void setIscomment(String iscomment) {
		this.iscomment = iscomment;
	}

	public int getFootType() {
        return footType;
    }

    public void setFootType(int footType) {
        this.footType = footType;
    }

    public List<String> getMinipics() {
        return minipics;
    }

    public void setMinipics(List<String> minipics) {
        this.minipics = minipics;
    }

    public String getNewsId() {
        return newsId;
    }

    public void setNewsId(String newsId) {
        this.newsId = newsId;
    }

    public int getNewsType() {
        return newsType;
    }

    public void setNewsType(int newsType) {
        this.newsType = newsType;
    }

    public String getNewsUrl() {
        return newsUrl;
    }

    public void setNewsUrl(String newsUrl) {
        this.newsUrl = newsUrl;
    }

    public String getOrders() {
        return orders;
    }

    public void setOrders(String orders) {
        this.orders = orders;
    }

    public int getShowType() {
        return showType;
    }

    public void setShowType(int showType) {
        this.showType = showType;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public String getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(String thumbnail) {
        this.thumbnail = thumbnail;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSpecialId() {
        return specialId;
    }

    public void setSpecialId(String specialId) {
        this.specialId = specialId;
    }

    public String getCreated() {
        return created;
    }

    public void setCreated(String created) {
        this.created = created;
    }

    public String getUpdated() {
        return updated;
    }

    public void setUpdated(String updated) {
        this.updated = updated;
    }

    public String getPublished() {
        return published;
    }

    public void setPublished(String published) {
        this.published = published;
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

    public String getVideourl() {
        return videourl;
    }

    public void setVideourl(String videourl) {
        this.videourl = videourl;
    }

    public void setRead(boolean isRead) {
        this.isRead = isRead;
    }

    public boolean isRead() {
        return isRead;
    }

    public String getBigthumbnail() {
        return bigthumbnail;
    }

    public void setBigthumbnail(String bigthumbnail) {
        this.bigthumbnail = bigthumbnail;
    }

	public String getLiveurl() {
		return liveurl;
	}

	public void setLiveurl(String liveurl) {
		this.liveurl = liveurl;
	}

	public String getIsread() {
		return isread;
	}

	public void setIsread(String isread) {
		this.isread = isread;
	}

	public int getListStyle() {
		return listStyle;
	}

	public void setListStyle(int listStyle) {
		this.listStyle = listStyle;
	}

	public String getShowSource() {
		return showSource;
	}

	public void setShowSource(String showSource) {
		this.showSource = showSource;
	}

	public int getLiveType() {
		return liveType;
	}

	public void setLiveType(int liveType) {
		this.liveType = liveType;
	}

	public int getSignStatus() {
		return signStatus;
	}

	public void setSignStatus(int signStatus) {
		this.signStatus = signStatus;
	}

	public String getShareUrl() {
		return shareUrl;
	}

	public void setShareUrl(String shareUrl) {
		this.shareUrl = shareUrl;
	}

	public int getTypeId() {
		return typeId;
	}

	public void setTypeId(int typeId) {
		this.typeId = typeId;
	}
}
