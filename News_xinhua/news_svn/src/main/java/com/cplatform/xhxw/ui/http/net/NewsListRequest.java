package com.cplatform.xhxw.ui.http.net;

import java.util.Date;

/**
 * Created by cy-love on 14-1-7.
 */
public class NewsListRequest extends BaseRequest{


    private String channelid;
    private String published;
    private String keyword;
    private int type;
    private int typeId;

    public NewsListRequest(String channelid) {
        this.channelid = channelid;
        this.published = String.valueOf(new Date().getTime());
        this.type = 1;
    }

    public NewsListRequest(String channelid, String published) {
        this.channelid = channelid;
        this.published = published;
        this.type = 0;
    }

    public String getChannelid() {
        return channelid;
    }

    public void setChannelid(String channelid) {
        this.channelid = channelid;
    }

    public String getPublished() {
        return published;
    }

    public void setPublished(String published) {
        this.published = published;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

	public int getTypeId() {
		return typeId;
	}

	public void setTypeId(int typeId) {
		this.typeId = typeId;
	}

	public String getKeyword() {
		return keyword;
	}

	public void setKeyword(String keyword) {
		this.keyword = keyword;
	}
}
