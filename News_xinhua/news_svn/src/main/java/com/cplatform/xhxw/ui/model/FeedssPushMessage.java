package com.cplatform.xhxw.ui.model;

import java.io.Serializable;

/**
 * feedss push解析
 */
public class FeedssPushMessage implements Serializable {

    private int msgType; // 消息业务类型，1: 要闻推送  2：聊天  3好友申请  5公司评论，赞
    private String content;
    private String friendId; // 好友id

    private String newsId;
    private int newsType;
    private String siteId;

    public int getMsgType() {
        return msgType;
    }

    public void setMsgType(int msgType) {
        this.msgType = msgType;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getFriendId() {
        return friendId;
    }

    public void setFriendId(String friendId) {
        this.friendId = friendId;
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

	public String getSiteId() {
		return siteId;
	}

	public void setSiteId(String siteId) {
		this.siteId = siteId;
	}
}
