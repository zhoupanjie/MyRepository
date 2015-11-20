package com.cplatform.xhxw.ui.model;

import java.io.Serializable;
import java.util.List;

/**
 * 焦点新闻
 * Created by cy-love on 14-1-8.
 */
public class Focus implements Serializable {

    private String newsId; //新闻ID
    private String posid; //推荐位ID
    private String order; //排序
    private String title; //标题
    private String thumbnail; //缩略图
    private String commentcount;

    private String bigthumbnail;
    private int footType;
    private List<String> minipics;
    private int newsType;
    private List<NewPic> picInfo;
    private String published;
    private int showType;
    private String summary;
    private String videourl;
    
    private String adid;
    private String adshorturl;
    private String adurl;
    
    private String liveurl;//直播wap页面地址

    //游戏频道
    private String id;
    private String name;
    private String normal;
    private String thumb;
    public String getAdid() {
		return adid;
	}

	public void setAdid(String adid) {
		this.adid = adid;
	}

	public String getAdshorturl() {
		return adshorturl;
	}

	public void setAdshorturl(String adshorturl) {
		this.adshorturl = adshorturl;
	}

	public String getAdurl() {
		return adurl;
	}

	public void setAdurl(String adurl) {
		this.adurl = adurl;
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

    public String getPublished() {
        return published;
    }

    public void setPublished(String published) {
        this.published = published;
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

    public String getVideourl() {
        return videourl;
    }

    public void setVideourl(String videourl) {
        this.videourl = videourl;
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

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getNormal() {
		return normal;
	}

	public void setNormal(String normal) {
		this.normal = normal;
	}

	public String getThumb() {
		return thumb;
	}

	public void setThumb(String thumb) {
		this.thumb = thumb;
	}
	
}
