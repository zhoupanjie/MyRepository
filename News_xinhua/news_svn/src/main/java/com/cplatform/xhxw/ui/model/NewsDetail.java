package com.cplatform.xhxw.ui.model;

import java.io.Serializable;
import java.util.List;

/**
 * 新闻详情
 * Created by cy-love on 14-1-8.
 */
public class NewsDetail implements Serializable {

    private String id;
    private String title;
    private String summary;
    private String type;
    private int showType;
    private int footType;
    private String friendTime;
    private String source;
    private String content;
    private String thumbnail;
    private List<Pics> pics;
    private List<Audios> audios;
    private List<Videos> videos;
    private String commentcount;
    private List<HotComments> hotComments;
    private List<Relation> relation;
    private List<TopRelation> topRelation;
    private String iscomment;//0，可以评论,1，不可以评论
    private AdvertismentsResponse ad; // 广告
    private List<RecommendAtlas> recommendimg;//图集浏览完毕，推荐图集列表
    private int readCount;
    private int signStatus;
    private String signAccount;
    private List<DetailHotNews> hotnews;//热点新闻列表

	public String getIscomment() {
		return iscomment;
	}

	public void setIscomment(String iscomment) {
		this.iscomment = iscomment;
	}

	public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getShowType() {
        return showType;
    }

    public void setShowType(int showType) {
        this.showType = showType;
    }

    public int getFootType() {
        return footType;
    }

    public void setFootType(int footType) {
        this.footType = footType;
    }

    public String getFriendTime() {
        return friendTime;
    }

    public void setFriendTime(String friendTime) {
        this.friendTime = friendTime;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(String thumbnail) {
        this.thumbnail = thumbnail;
    }

    public List<Pics> getPics() {
        return pics;
    }

    public void setPics(List<Pics> pics) {
        this.pics = pics;
    }

    public List<Audios> getAudios() {
        return audios;
    }

    public void setAudios(List<Audios> audios) {
        this.audios = audios;
    }

    public List<Videos> getVideos() {
        return videos;
    }

    public void setVideos(List<Videos> videos) {
        this.videos = videos;
    }

    public String getCommentcount() {
        return commentcount;
    }

    public void setCommentcount(String commentcount) {
        this.commentcount = commentcount;
    }

    public List<HotComments> getHotComments() {
        return hotComments;
    }

    public void setHotComments(List<HotComments> hotComments) {
        this.hotComments = hotComments;
    }

    public List<Relation> getRelation() {
        return relation;
    }

    public void setRelation(List<Relation> relation) {
        this.relation = relation;
    }

    public List<TopRelation> getTopRelation() {
		return topRelation;
	}

	public void setTopRelation(List<TopRelation> topRelation) {
		this.topRelation = topRelation;
	}

	public AdvertismentsResponse getAd() {
        return ad;
    }

    public void setAd(AdvertismentsResponse ad) {
        this.ad = ad;
    }

	public List<RecommendAtlas> getRecommendimg() {
		return recommendimg;
	}

	public void setRecommendimg(List<RecommendAtlas> recommendimg) {
		this.recommendimg = recommendimg;
	}

	public int getReadCount() {
		return readCount;
	}

	public void setReadCount(int readCount) {
		this.readCount = readCount;
	}

	public int getSignStatus() {
		return signStatus;
	}

	public void setSignStatus(int signStatus) {
		this.signStatus = signStatus;
	}

	public String getSignAccount() {
		return signAccount;
	}

	public void setSignAccount(String signAccount) {
		this.signAccount = signAccount;
	}

	public List<DetailHotNews> getHotnews() {
		return hotnews;
	}

	public void setHotnews(List<DetailHotNews> hotnews) {
		this.hotnews = hotnews;
	}
}
