package com.cplatform.xhxw.ui.model;

import java.util.List;

/**
 * 推荐位
 * Created by cy-love on 14-1-8.
 */
public class Other {

    private String newsId; //新闻ID
    private String posid; //推荐位ID
    private String order; //排序
    private String title; //标题
    private String thumbnail; //缩略图
    private String bigthumbnail;
    private String summary;
    private String videourl;
    private int footType;
    private int showType;
    private int newsType;
    private List<NewPic> picInfo;
    private List<String> minipics;
    private String published;
    private String commentcount;
    private String iscomment;//0:允许，1：不允许
    private String liveurl; // 直播新闻url
    private String isread;//后台记录的已读未读状态
    private int listStyle; //列表栏目显示类型
    private String showSource; //新闻来源
    private int liveType;
    private int signStatus; //圈阅状态 0：未圈阅， 1：已圈阅
    private String shareUrl;
    private int typeId;

    public New getNew() {
        New news = new New();
        news.setRead(false);
        news.setNewsId(newsId);
        news.setOrders(order);
        news.setTitle(title);
        news.setThumbnail(thumbnail);
        news.setBigthumbnail(bigthumbnail);
        news.setSummary(summary);
        news.setVideourl(videourl);
        news.setFootType(footType);
        news.setShowType(showType);
        news.setNewsType(newsType);
        news.setPicInfo(picInfo);
        news.setMinipics(minipics);
        news.setPublished(published);
        news.setCommentcount(commentcount);
        news.setIscomment(iscomment);
        news.setLiveurl(liveurl);
        news.setIsread(isread);
        news.setListStyle(listStyle);
        news.setShowSource(showSource);
        news.setLiveType(liveType);
        news.setSignStatus(signStatus);
        news.setShareUrl(shareUrl);
        news.setTypeId(typeId);
        return news;
    }

    public String getIscomment() {
		return iscomment;
	}

	public void setIscomment(String iscomment) {
		this.iscomment = iscomment;
	}

	public String getNewsId() {
        return newsId;
    }

    public void setNewsId(String newsId) {
        this.newsId = newsId;
    }

    public String getPosid() {
        return posid;
    }

    public void setPosid(String posid) {
        this.posid = posid;
    }

    public String getOrder() {
        return order;
    }

    public void setOrder(String order) {
        this.order = order;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(String thumbnail) {
        this.thumbnail = thumbnail;
    }

    public String getBigthumbnail() {
        return bigthumbnail;
    }

    public void setBigthumbnail(String bigthumbnail) {
        this.bigthumbnail = bigthumbnail;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public String getVideourl() {
        return videourl;
    }

    public void setVideourl(String videourl) {
        this.videourl = videourl;
    }

    public int getFootType() {
        return footType;
    }

    public void setFootType(int footType) {
        this.footType = footType;
    }

    public int getShowType() {
        return showType;
    }

    public void setShowType(int showType) {
        this.showType = showType;
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

    public List<String> getMinipics() {
        return minipics;
    }

    public void setMinipics(List<String> minipics) {
        this.minipics = minipics;
    }

    public String getPublished() {
        return published;
    }

    public void setPublished(String published) {
        this.published = published;
    }

    public String getCommentcount() {
        return commentcount;
    }

    public void setCommentcount(String commentcount) {
        this.commentcount = commentcount;
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
